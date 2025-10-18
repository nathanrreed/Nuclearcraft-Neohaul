package com.nred.nuclearcraft.block_entity.fission.port;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.payload.multiblock.port.FluidPortUpdatePacket;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.Config2.smart_processor_input;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public abstract class FissionFluidPortEntity<PORT extends FissionFluidPortEntity<PORT, TARGET> & ITileFilteredFluid, TARGET extends IFissionPortTarget<PORT, TARGET> & ITileFilteredFluid> extends FissionPortEntity<PORT, TARGET> implements ITileFilteredFluid, ITileGui<PORT, FluidPortUpdatePacket, TileContainerInfo<PORT>> {
    protected final TileContainerInfo<PORT> info;

    protected final @Nonnull List<Tank> tanks;
    protected final @Nonnull List<Tank> filterTanks;
    protected final int capacity;

    protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.IN, TankSorption.NON));

    protected @Nonnull FluidTileWrapper[] fluidSides;
    protected @Nonnull ChemicalTileWrapper[] chemicalSides;

    protected final BasicRecipeHandler<?> recipeHandler;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public FissionFluidPortEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, Class<PORT> portClass, int capacity, Set<ResourceLocation> validFluids, BasicRecipeHandler<?> recipeHandler) {
        super(type, pos, blockState, portClass);
        info = TileInfoHandler.getTileContainerInfo(name);

        tanks = Lists.newArrayList(new Tank(capacity, validFluids), new Tank(capacity, new ObjectOpenHashSet<>()));
        filterTanks = Lists.newArrayList(new Tank(1000, validFluids), new Tank(1000, new ObjectOpenHashSet<>()));
        this.capacity = capacity;

        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);

        this.recipeHandler = recipeHandler;
    }

    @Override
    public TileContainerInfo<PORT> getContainerInfo() {
        return info;
    }

    // MenuProvider

    @Override
    public Component getDisplayName() {
        return getTileBlockDisplayName();
    }

    @Override
    public int getTankCapacityPerConnection() {
        return 36;
    }

    @Override
    public Object getFilterKey() {
        return getFilterTanks().get(0).getFluidName();
    }

    @Override
    public void update() {
        super.update();
        if (!level.isClientSide) {
            Optional<Direction> facing = getPartPosition().getDirection();
            if (facing.isPresent() && !getTanks().get(1).isEmpty() && getTankSorption(facing.get(), 1).canDrain()) {
                pushFluidToSide(facing.get());
            }
            sendTileUpdatePacketToListeners();
        }
    }

    @Override
    public void setInventoryStackLimit(int stackLimit) {
    }

    @Override
    public int getTankBaseCapacity() {
        return capacity;
    }

    @Override
    public void setTankCapacity(int capacity) {
        tanks.get(0).setCapacity(capacity);
        tanks.get(1).setCapacity(capacity);
    }

    @Override
    public boolean canModifyFilter(int tank) {
        return !isMachineAssembled();
    }

    @Override
    public void onFilterChanged(int tank) {
        setChanged();
    }

    // Fluids

    @Override
    public @Nonnull List<Tank> getTanks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
    }

    @Override
    public @Nonnull List<Tank> getTanksInternal() {
        return tanks;
    }

    @Override
    public @Nonnull List<Tank> getFilterTanks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
    }

    @Override
    public @Nonnull FluidConnection[] getFluidConnections() {
        return fluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
        fluidConnections = connections;
    }

    @Override
    public @Nonnull FluidTileWrapper[] getFluidSides() {
        return fluidSides;
    }

    @Override
    public @Nonnull ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public boolean getInputTanksSeparated() {
        return false;
    }

    @Override
    public void setInputTanksSeparated(boolean separated) {
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        return false;
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        return TankOutputSetting.DEFAULT;
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
    }

    @Override
    public boolean hasConfigurableFluidConnections() {
        return true;
    }

    @Override
    public boolean isFluidValidForTank(int tankNumber, FluidStack stack) {
        if (stack == null || stack.getAmount() <= 0 || tankNumber >= recipeHandler.fluidInputSize) {
            return false;
        }

        if (smart_processor_input) {
            return recipeHandler.isValidFluidInput(stack, tankNumber, getTanks().subList(0, recipeHandler.fluidInputSize), Collections.emptyList(), null);
        } else {
            return recipeHandler.isValidFluidInput(stack);
        }
    }

    // ITileGui

    @Override
    public Set<Player> getTileUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public FluidPortUpdatePacket getTileUpdatePacket() {
        return new FluidPortUpdatePacket(worldPosition, masterPortPos, getTanks(), getFilterTanks());
    }

    @Override
    public void onTileUpdatePacket(FluidPortUpdatePacket message) {
        masterPortPos = message.masterPortPos;
        if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
            refreshMasterPort();
        }
        Tank.TankInfo.readInfoList(message.tankInfos, getTanks());
        Tank.TankInfo.readInfoList(message.filterTankInfos, getFilterTanks());
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        if (!player.isCrouching()) {
            if (getMultiblockController().isPresent()) {
                if (getTankSorption(facing, 0) != TankSorption.IN) {
                    for (Direction side : Direction.values()) {
                        setTankSorption(side, 0, TankSorption.IN);
                        setTankSorption(side, 1, TankSorption.NON);
                    }
                    setActivity(false);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.side_config.input").withStyle(ChatFormatting.DARK_AQUA)));
                } else {
                    for (Direction side : Direction.values()) {
                        setTankSorption(side, 0, TankSorption.NON);
                        setTankSorption(side, 1, TankSorption.OUT);
                    }
                    setActivity(true);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.side_config.output").withStyle(ChatFormatting.RED)));
                }
                markDirtyAndNotify(true);
                return true;
            }
        }
        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeTanks(nbt, registries);
        writeFluidConnections(nbt, registries);
        writeTankSettings(nbt, registries);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readTanks(nbt, registries);
        readFluidConnections(nbt, registries);
        readTankSettings(nbt, registries);

        for (Direction side : Direction.values()) {
            if (getTankSorption(side, 0).equals(TankSorption.IN)) {
                setTankSorption(side, 1, TankSorption.NON);
            }
        }
    }

    @Override
    public CompoundTag writeTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        ITileFilteredFluid.super.writeTanks(nbt, registries);
        for (int i = 0; i < filterTanks.size(); ++i) {
            getTanks().get(i).writeToNBT(nbt, registries, "filterTanks" + i + filterTanks.size());
        }
        return nbt;
    }

    @Override
    public void readTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        ITileFilteredFluid.super.readTanks(nbt, registries);
        for (int i = 0; i < filterTanks.size(); ++i) {
            getTanks().get(i).readFromNBT(nbt, registries, "filterTanks" + i + filterTanks.size());
        }
    }
}