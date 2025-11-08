package com.nred.nuclearcraft.multiblock.hx;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.hx.IHeatExchangerController;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.util.PosHelper;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class HeatExchanger extends Multiblock<HeatExchanger> implements ILogicMultiblock<HeatExchanger, HeatExchangerLogic>, IPacketMultiblock<HeatExchanger, HeatExchangerUpdatePacket> {
    protected @Nonnull HeatExchangerLogic logic = new HeatExchangerLogic(this);

    protected IHeatExchangerController<?> controller;

    public boolean refreshFlag = false;
    public int packetFlag = 0;

    public double shellSpeedMultiplier = 0D;

    public @Nullable HeatExchangerInletEntity masterShellInlet;

    public final ObjectSet<HeatExchangerTubeNetwork> networks = new ObjectOpenHashSet<>();

    public static final int BASE_MAX_INPUT = 4000, BASE_MAX_OUTPUT = 16000;

    public final @Nonnull List<Tank> shellTanks = Lists.newArrayList(new Tank(BASE_MAX_INPUT, NCRecipes.heat_exchanger.validFluids.get(0)), new Tank(BASE_MAX_OUTPUT, null));

    public RecipeInfo<BasicRecipe> shellRecipe;

    public boolean isExchangerOn, computerActivated;

    public int totalNetworkCount = 0, activeNetworkCount = 0;
    public int activeTubeCount = 0, activeContactCount = 0;
    public double tubeInputRate = 0D, tubeInputRateFP = 0D;
    public double shellInputRate = 0D, shellInputRateFP = 0D;
    public double heatTransferRate = 0D, heatTransferRateFP = 0D;
    public double totalTempDiff = 0D;

    public boolean shouldSpecialRender = false;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public HeatExchanger(Level level) {
        super(level);
    }

    @Override
    public @Nonnull HeatExchangerLogic getLogic() {
        return logic;
    }

    @Override
    public void setLogic(String logicID) {
        if (logicID.equals(logic.getID())) {
            return;
        }

        UnaryOperator<HeatExchangerLogic> constructor = switch (logicID) {
            case "heat_exchanger" -> HeatExchangerLogic::new;
            case "condenser" -> CondenserLogic::new;
            default -> throw new IllegalStateException("Unexpected logicID: " + logicID);
        };

        logic = getNewLogic(constructor);
    }


    // Multiblock Size Limits

    @Override
    public int getMinimumInteriorLength() {
        return logic.getMinimumInteriorLength();
    }

    @Override
    public int getMaximumInteriorLength() {
        return logic.getMaximumInteriorLength();
    }

    // Multiblock Methods

    @Override
    protected void onPartAdded(IMultiblockPart<HeatExchanger> iMultiblockPart) {
        super.onPartAdded(iMultiblockPart);
        logic.onBlockAdded(iMultiblockPart);
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<HeatExchanger> iMultiblockPart) {
        super.onPartRemoved(iMultiblockPart);
        logic.onBlockRemoved(iMultiblockPart);
    }

    @Override
    protected void onMachineAssembled() {
        logic.onMachineAssembled();
    }

    @Override
    protected void onMachineRestored() {
        logic.onMachineRestored();
    }

    @Override
    protected void onMachinePaused() {
        logic.onMachinePaused();
    }

    @Override
    protected void onMachineDisassembled() {
        logic.onMachineDisassembled();
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        shouldSpecialRender = false;

        return setLogic(this) && super.isMachineWhole(validatorCallback) && logic.isMachineWhole();
    }

    public boolean setLogic(HeatExchanger multiblock) {
        if (getPartMap(IHeatExchangerController.class).isEmpty()) {
            multiblock.setLastError(MODID + ".multiblock_validation.no_controller");
            return false;
        }
        if (getPartCount(IHeatExchangerController.class) > 1) {
            multiblock.setLastError(MODID + ".multiblock_validation.too_many_controllers");
            return false;
        }

        for (IHeatExchangerController<?> contr : getParts(IHeatExchangerController.class)) {
            controller = contr;
            break;
        }

        setLogic(controller.getLogicID());

        return true;
    }

    @Override
    protected void onAssimilate(IMultiblockController<HeatExchanger> assimilated) {
        logic.onAssimilate(assimilated);
    }

    @Override
    protected void onAssimilated(IMultiblockController<HeatExchanger> assimilator) {
        logic.onAssimilated(assimilator);
    }

    // Server

    @Override
    protected boolean updateServer() {
        super.updateServer();
        return logic.onUpdateServer();
    }

    public BlockPos getMasterShellInletPos() {
        return masterShellInlet == null ? PosHelper.DEFAULT_NON : masterShellInlet.getBlockPos();
    }

    public Stream<HeatExchangerInletEntity> getMasterInlets() {
        return Stream.concat(networks.stream().map(x -> x.masterInlet), Stream.of(masterShellInlet)).filter(Objects::nonNull);
    }

    // Client

    @Override
    protected void updateClient() {
        logic.onUpdateClient();
    }

    // NBT

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        data.putBoolean("isExchangerOn", isExchangerOn);
        data.putBoolean("computerActivated", computerActivated);

        writeTanks(shellTanks, data, registries, "shellTanks");

        writeLogicNBT(data, registries, syncReason);

        return data;
    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        isExchangerOn = data.getBoolean("isExchangerOn");
        computerActivated = data.getBoolean("computerActivated");

        readTanks(shellTanks, data, registries, "shellTanks");

        readLogicNBT(data, registries, syncReason);
    }

    // Packets

    @Override
    public Set<Player> getMultiblockUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public HeatExchangerUpdatePacket getMultiblockUpdatePacket() {
        return logic.getMultiblockUpdatePacket();
    }

    @Override
    public void onMultiblockUpdatePacket(HeatExchangerUpdatePacket message) {
        logic.onMultiblockUpdatePacket(message);
    }

    protected HeatExchangerRenderPacket getRenderPacket() {
        return logic.getRenderPacket();
    }

    public void onRenderPacket(HeatExchangerRenderPacket message) {
        logic.onRenderPacket(message);
    }

    public void sendRenderPacketToPlayer(Player player) {
        if (getWorld().isClientSide()) {
            return;
        }
        HeatExchangerRenderPacket packet = getRenderPacket();
        if (packet == null) {
            return;
        }
        packet.sendTo(player);
    }

    public void sendRenderPacketToAll() {
        if (getWorld().isClientSide()) {
            return;
        }
        HeatExchangerRenderPacket packet = getRenderPacket();
        if (packet == null) {
            return;
        }
        packet.sendToAll();
    }

    // Multiblock Validators

    @Override
    protected boolean isBlockGoodForInterior(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return logic.isBlockGoodForInterior(level, x, y, z);
    }

    // Clear Material

    @Override
    public void clearAllMaterial() {
        logic.clearAllMaterial();
        super.clearAllMaterial();

        for (Tank tank : shellTanks) {
            tank.setFluidStored(null);
        }
    }
}