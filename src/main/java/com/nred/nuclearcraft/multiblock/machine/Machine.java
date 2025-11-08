package com.nred.nuclearcraft.multiblock.machine;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.fluid.FluidConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.processor.AbstractProcessorElement;
import com.nred.nuclearcraft.block_entity.machine.IMachineController;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.payload.multiblock.MachineRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeUnitInfo;
import com.nred.nuclearcraft.util.InventoryStackList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class Machine extends Multiblock<Machine> implements ILogicMultiblock<Machine, MachineLogic>, IPacketMultiblock<Machine, MachineUpdatePacket> {
    protected IMachineController<?> controller;

//	public final IRadiationSource radiation = new RadiationSource(0D); TODO

    public @Nonnull EnergyStorage energyStorage = new EnergyStorage(1);

    public @Nonnull List<Tank> reservoirTanks = Collections.emptyList();

    public BasicRecipeHandler<?> recipeHandler;

    public int itemInputSize, itemOutputSize, fluidInputSize, fluidOutputSize;

    public @Nonnull InventoryStackList inventoryStacks = new InventoryStackList(new ArrayList<>());
    public @Nonnull List<Tank> tanks = Collections.emptyList();

    public @Nonnull List<InventoryConnection[]> inventoryConnections = Collections.emptyList();
    public @Nonnull List<FluidConnection[]> fluidConnections = Collections.emptyList();

    public double baseProcessPower, baseProcessRadiation;

    public int productionCount;

    public double baseSpeedMultiplier, basePowerMultiplier;

    public RecipeUnitInfo recipeUnitInfo = RecipeUnitInfo.DEFAULT;

    public boolean isMachineOn, fullHalt;

    @OnlyIn(Dist.CLIENT)
    protected Object2ObjectMap<BlockPos, SoundInstance> soundMap;
    public boolean refreshSounds = true;

    protected double prevSpeedMultiplier = 0D;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public AbstractProcessorElement processor = new AbstractProcessorElement(false) {

        @Override
        public Level getWorld() {
            return getWorld();
        }

        @Override
        public BasicRecipeHandler getRecipeHandler() {
            return recipeHandler;
        }

        @Override
        public boolean setRecipeStats() {
            boolean recipeNonNull = super.setRecipeStats();

            if (recipeNonNull) {
                recipeUnitInfo = recipeInfo.getRecipeUnitInfo(1D);
            } else {
                if (productionCount > 0) {
                    recipeUnitInfo = recipeUnitInfo.withRateMultiplier(recipeUnitInfo.rateMultiplier / (1D + 1D / productionCount));
                } else {
                    recipeUnitInfo = RecipeUnitInfo.DEFAULT;
                }
            }

            return recipeNonNull;
        }

        @Override
        public void setRecipeStats(@Nullable BasicRecipe recipe) {
            logic.setRecipeStats(recipe);
        }

        @Override
        public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
            return inventoryStacks;
        }

        @Override
        public @Nonnull List<Tank> getTanks() {
            return tanks;
        }

        @Override
        public boolean getConsumesInputs() {
            return logic.getConsumesInputs();
        }

        @Override
        public boolean getLosesProgress() {
            return logic.getLosesProgress();
        }

        @Override
        public int getItemInputSize() {
            return itemInputSize;
        }

        @Override
        public int getFluidInputSize() {
            return fluidInputSize;
        }

        @Override
        public int getItemOutputSize() {
            return itemOutputSize;
        }

        @Override
        public int getFluidOutputSize() {
            return fluidOutputSize;
        }

        @Override
        public int getItemInputSlot(int index) {
            return index;
        }

        @Override
        public int getFluidInputTank(int index) {
            return index;
        }

        @Override
        public int getItemOutputSlot(int index) {
            return itemInputSize + index;
        }

        @Override
        public int getFluidOutputTank(int index) {
            return fluidInputSize + index;
        }

        @Override
        public double getSpeedMultiplier() {
            return logic.getSpeedMultiplier();
        }

        @Override
        public boolean isHalted() {
            return logic.isHalted();
        }

        @Override
        public boolean readyToProcess() {
            return logic.readyToProcess();
        }

        public void process() {
            energyStorage.changeEnergyStored(logic.isGenerator() ? logic.getProcessPower() : -logic.getProcessPower());
//			radiation.setRadiationLevel(baseProcessRadiation * getSpeedMultiplier()); TODO
            super.process(getWorld());
            productionCount = 0;
        }

        @Override
        public void finishProcess(Level level) {
            ++productionCount;
            super.finishProcess(level);
        }

        @Override
        public boolean onIdle(boolean wasProcessing) {
//			radiation.setRadiationLevel(0D); TODO
            return super.onIdle(wasProcessing);
        }

        @Override
        public void onResumeProcessingState() {
            sendMultiblockUpdatePacketToListeners();
        }

        @Override
        public void onChangeProcessingState() {
            logic.setIsMachineOn(isProcessing);
        }


        @Override
        public void refreshRecipe(Level level) {
            logic.refreshRecipe(level);
        }

        @Override
        public void refreshActivity() {
            logic.refreshActivity();
        }
    };

    // Moved to fix constructor null pointer exceptions
    protected @Nonnull MachineLogic logic = new MachineLogic(this);

    public Machine(Level level) {
        super(level);
    }

    @Override
    public @Nonnull MachineLogic getLogic() {
        return logic;
    }

    @Override
    public void setLogic(String logicID) {
        if (logicID.equals(logic.getID())) {
            return;
        }
        UnaryOperator<MachineLogic> constructor = switch (logicID) {
            case "distiller" -> DistillerLogic::new;
            case "electrolyzer" -> ElectrolyzerLogic::new;
            case "infiltrator" -> InfiltratorLogic::new;
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
    protected void onPartAdded(IMultiblockPart<Machine> iMultiblockPart) {
        super.onPartAdded(iMultiblockPart);
        logic.onBlockAdded(iMultiblockPart);
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<Machine> iMultiblockPart) {
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
        return setLogic(this) && super.isMachineWhole(validatorCallback) && logic.isMachineWhole();
    }

    public boolean setLogic(Machine multiblock) {
        if (getPartMap(IMachineController.class).isEmpty()) {
            multiblock.setLastError(MODID + ".multiblock_validation.no_controller");
            return false;
        }
        if (getPartCount(IMachineController.class) > 1) {
            multiblock.setLastError(MODID + ".multiblock_validation.too_many_controllers");
            return false;
        }

        for (IMachineController<?> contr : getParts(IMachineController.class)) {
            controller = contr;
            break;
        }

        setLogic(controller.getLogicID());

        return true;
    }

    @Override
    protected void onAssimilate(IMultiblockController<Machine> assimilated) {
        logic.onAssimilate(assimilated);
    }

    @Override
    protected void onAssimilated(IMultiblockController<Machine> assimilated) {
        logic.onAssimilate(assimilated);
    }

    // Server

    @Override
    protected boolean updateServer() {
        return logic.onUpdateServer();
    }

    // Client

    @Override
    protected void updateClient() {
        logic.onUpdateClient();
    }

    // NBT

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        writeLogicNBT(data, registries, syncReason);
        return data;
    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        readLogicNBT(data, registries, syncReason);
    }

    // Packets

    @Override
    public Set<Player> getMultiblockUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public MachineUpdatePacket getMultiblockUpdatePacket() {
        return logic.getMultiblockUpdatePacket();
    }

    @Override
    public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
        isMachineOn = message.isMachineOn;
        logic.onMultiblockUpdatePacket(message);
    }

    protected MachineRenderPacket getRenderPacket() {
        return logic.getRenderPacket();
    }

    public void onRenderPacket(MachineRenderPacket message) {
        isMachineOn = message.isMachineOn;
        logic.onRenderPacket(message);
    }

    public void sendRenderPacketToPlayer(Player player) {
        if (getWorld().isClientSide()) {
            return;
        }
        MachineRenderPacket packet = getRenderPacket();
        if (packet == null) {
            return;
        }
        packet.sendTo(player);
    }

    public void sendRenderPacketToAll() {
        if (getWorld().isClientSide()) {
            return;
        }
        MachineRenderPacket packet = getRenderPacket();
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

        Collections.fill(inventoryStacks, ItemStack.EMPTY);
        for (Tank tank : tanks) {
            tank.setFluidStored(null);
        }

        Collections.fill(processor.consumedStacks, ItemStack.EMPTY);
        for (Tank tank : processor.consumedTanks) {
            tank.setFluidStored(null);
        }

        for (Tank reservoirTank : reservoirTanks) {
            reservoirTank.setFluidStored(null);
        }
    }
}