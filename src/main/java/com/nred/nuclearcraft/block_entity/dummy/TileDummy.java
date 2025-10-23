package com.nred.nuclearcraft.block_entity.dummy;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.energyFluid.TileEnergyFluidSidedInventory;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemHandler;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public abstract class TileDummy<MASTER extends IDummyMaster> extends TileEnergyFluidSidedInventory implements ITickable {
    public BlockPos masterPosition = null;
    protected final int updateRate;

    protected int checkCount;

    protected final Class<MASTER> tClass;

    public TileDummy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MASTER> tClass, String name, int updateRate, Set<ResourceLocation> allowedFluids) {
        this(type, pos, blockState, tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
    }

    public TileDummy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MASTER> tClass, String name, @Nonnull EnergyConnection[] energyConnections, int updateRate, Set<ResourceLocation> allowedFluids) {
        this(type, pos, blockState, tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), energyConnections, updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
    }

    public TileDummy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MASTER> tClass, String name, int updateRate, Set<ResourceLocation> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        this(type, pos, blockState, tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, fluidConnections);
    }

    public TileDummy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MASTER> tClass, String name, @Nonnull EnergyConnection[] energyConnections, int updateRate, Set<ResourceLocation> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        this(type, pos, blockState, tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), energyConnections, updateRate, allowedFluids, fluidConnections);
    }

    public TileDummy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MASTER> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, int updateRate, Set<ResourceLocation> allowedFluids) {
        this(type, pos, blockState, tClass, name, inventoryConnections, ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
    }

    public TileDummy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MASTER> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, @Nonnull EnergyConnection[] energyConnections, int updateRate, Set<ResourceLocation> allowedFluids) {
        this(type, pos, blockState, tClass, name, inventoryConnections, energyConnections, updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
    }

    public TileDummy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MASTER> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, int updateRate, Set<ResourceLocation> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        this(type, pos, blockState, tClass, name, inventoryConnections, ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, fluidConnections);
    }

    public TileDummy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MASTER> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, @Nonnull EnergyConnection[] energyConnections, int updateRate, Set<ResourceLocation> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, name, 1, inventoryConnections, 1, energyConnections, 1, allowedFluids, fluidConnections);
        this.updateRate = updateRate;
        this.tClass = tClass;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            findMaster();
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide) {
            if (checkCount == 0) {
                findMaster();
            }
            tickDummy();
        }
    }

    public void tickDummy() {
        ++checkCount;
        checkCount %= updateRate;
    }

    @Override
    public void onBlockNeighborChanged(BlockState state, Level levelIn, BlockPos posIn, BlockPos fromPos) {
        super.onBlockNeighborChanged(state, levelIn, posIn, fromPos);
        if (hasMaster()) {
            getMaster().onDummyNeighborChanged(state, levelIn, posIn, fromPos);
        }
    }

    // Inventory

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        if (getMaster() instanceof ITileInventory) {
            return ((ITileInventory) getMaster()).getInventoryStacks();
        }
        return super.getInventoryStacks();
    }

    @Override
    public int getMaxStackSize() {
        if (getMaster() instanceof ITileInventory) {
            return ((ITileInventory) getMaster()).getMaxStackSize();
        }
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(Player player) {
        return false;
    }

    @Override
    public @Nonnull InventoryConnection[] getInventoryConnections() {
        if (getMaster() instanceof ITileInventory) {
            return ((ITileInventory) getMaster()).getInventoryConnections();
        }
        return super.getInventoryConnections();
    }

    @Override
    public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
        if (getMaster() instanceof ITileInventory) {
            ((ITileInventory) getMaster()).setInventoryConnections(connections);
        }
        super.setInventoryConnections(connections);
    }

    @Override
    public ItemOutputSetting getItemOutputSetting(int tankNumber) {
        if (getMaster() instanceof ITileInventory) {
            return ((ITileInventory) getMaster()).getItemOutputSetting(tankNumber);
        }
        return super.getItemOutputSetting(tankNumber);
    }

    @Override
    public void setItemOutputSetting(int tankNumber, ItemOutputSetting setting) {
        if (getMaster() instanceof ITileInventory) {
            ((ITileInventory) getMaster()).setItemOutputSetting(tankNumber, setting);
        } else {
            super.setItemOutputSetting(tankNumber, setting);
        }
    }

    @Override
    public IItemHandler getItemHandler(@Nullable Direction side) {
        ITileInventory tile = getMaster() instanceof ITileInventory ? (ITileInventory) getMaster() : this;
        return new ItemHandler<>(tile, side);
    }

    // ITileEnergy

    @Override
    public EnergyStorage getEnergyStorage() {
        if (getMaster() instanceof ITileEnergy) {
            return ((ITileEnergy) getMaster()).getEnergyStorage();
        }
        return super.getEnergyStorage();
    }

    @Override
    public EnergyConnection getEnergyConnection(@Nonnull Direction side) {
        if (getMaster() instanceof ITileEnergy) {
            return ((ITileEnergy) getMaster()).getEnergyConnection(side);
        }
        return super.getEnergyConnection(side);
    }

    @Override
    public int getEnergyStored() {
        if (getMaster() instanceof ITileEnergy) {
            return ((ITileEnergy) getMaster()).getEnergyStored();
        }
        return super.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        if (getMaster() instanceof ITileEnergy) {
            return ((ITileEnergy) getMaster()).getMaxEnergyStored();
        }
        return super.getMaxEnergyStored();
    }

    @Override
    public boolean canReceiveEnergy(Direction side) {
        if (getMaster() instanceof ITileEnergy) {
            return ((ITileEnergy) getMaster()).canReceiveEnergy(side);
        }
        return false;
    }

    @Override
    public boolean canExtractEnergy(Direction side) {
        if (getMaster() instanceof ITileEnergy) {
            return ((ITileEnergy) getMaster()).canExtractEnergy(side);
        }
        return false;
    }

    @Override
    public int receiveEnergy(int maxReceive, Direction side, boolean simulate) {
        if (getMaster() instanceof ITileEnergy) {
            return ((ITileEnergy) getMaster()).receiveEnergy(maxReceive, side, simulate);
        }
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, Direction side, boolean simulate) {
        if (getMaster() instanceof ITileEnergy) {
            return ((ITileEnergy) getMaster()).extractEnergy(maxExtract, side, simulate);
        }
        return 0;
    }

    // Energy Distribution

    @Override
    public void pushEnergy() {
        if (getMaster() == null) {
            return;
        }
        super.pushEnergy();
    }

    // Fluids

    // Tanks

    @Override
    public @Nonnull List<Tank> getTanks() {
        if (getMaster() instanceof ITileFluid) {
            return ((ITileFluid) getMaster()).getTanks();
        }
        return super.getTanks();
    }

    // Fluid Connections

    @Override
    public @Nonnull FluidConnection[] getFluidConnections() {
        if (getMaster() instanceof ITileFluid) {
            return ((ITileFluid) getMaster()).getFluidConnections();
        }
        return super.getFluidConnections();
    }

    @Override
    public @Nonnull FluidTileWrapper[] getFluidSides() {
        if (getMaster() instanceof ITileFluid) {
            return ((ITileFluid) getMaster()).getFluidSides();
        }
        return super.getFluidSides();
    }

    @Override
    public @Nonnull ChemicalTileWrapper[] getChemicalSides() {
        if (getMaster() instanceof ITileFluid) {
            return ((ITileFluid) getMaster()).getChemicalSides();
        }
        return super.getChemicalSides();
    }

    @Override
    public boolean getInputTanksSeparated() {
        if (getMaster() instanceof ITileFluid) {
            return ((ITileFluid) getMaster()).getInputTanksSeparated();
        }
        return super.getInputTanksSeparated();
    }

    @Override
    public void setInputTanksSeparated(boolean shared) {
        if (getMaster() instanceof ITileFluid) {
            ((ITileFluid) getMaster()).setInputTanksSeparated(shared);
        } else {
            super.setInputTanksSeparated(shared);
        }
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        if (getMaster() instanceof ITileFluid) {
            return ((ITileFluid) getMaster()).getVoidUnusableFluidInput(tankNumber);
        }
        return super.getVoidUnusableFluidInput(tankNumber);
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
        if (getMaster() instanceof ITileFluid) {
            ((ITileFluid) getMaster()).setVoidUnusableFluidInput(tankNumber, voidUnusableFluidInput);
        } else {
            super.setVoidUnusableFluidInput(tankNumber, voidUnusableFluidInput);
        }
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        if (getMaster() instanceof ITileFluid) {
            return ((ITileFluid) getMaster()).getTankOutputSetting(tankNumber);
        }
        return super.getTankOutputSetting(tankNumber);
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
        if (getMaster() instanceof ITileFluid) {
            ((ITileFluid) getMaster()).setTankOutputSetting(tankNumber, setting);
        } else {
            super.setTankOutputSetting(tankNumber, setting);
        }
    }

    // Fluid Distribution

    @Override
    public void pushFluid() {
        if (getMaster() == null) {
            return;
        }
        super.pushFluid();
    }

    // Find Master

    /**
     * Find the BlockPos of the master tile entity
     */
    public abstract void findMaster();

    public boolean hasMaster() {
        if (masterPosition == null) {
            return false;
        }
        return isMaster(masterPosition);
    }

    public boolean isMaster(BlockPos posIn) {
        return tClass.isInstance(level.getBlockEntity(posIn));
    }

    public MASTER getMaster() {
        if (hasMaster()) {
            return tClass.cast(level.getBlockEntity(masterPosition));
        }
        return null;
    }

    // NBT

    @Override
    public CompoundTag writeInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        ContainerHelper.saveAllItems(nbt, super.getInventoryStacks(), registries);
        return nbt;
    }

    @Override
    public void readInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        ContainerHelper.loadAllItems(nbt, super.getInventoryStacks(), registries);
    }

    @Override
    public CompoundTag writeInventoryConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        for (Direction side : Direction.values()) {
            super.getInventoryConnections()[side.ordinal()].writeToNBT(nbt, side);
        }
        return nbt;
    }

    @Override
    public void readInventoryConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        if (!super.hasConfigurableInventoryConnections()) {
            return;
        }
        for (Direction side : Direction.values()) {
            super.getInventoryConnections()[side.ordinal()].readFromNBT(nbt, side);
        }
    }

    @Override
    public CompoundTag writeSlotSettings(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < super.getInventoryStacks().size(); ++i) {
            nbt.putInt("itemOutputSetting" + i, super.getItemOutputSetting(i).ordinal());
        }
        return nbt;
    }

    @Override
    public void readSlotSettings(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < super.getInventoryStacks().size(); ++i) {
            super.setItemOutputSetting(i, ItemOutputSetting.values()[nbt.getInt("itemOutputSetting" + i)]);
        }
    }

    @Override
    public CompoundTag writeTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < super.getTanks().size(); ++i) {
            super.getTanks().get(i).writeToNBT(nbt, registries, "tanks" + i);
        }
        return nbt;
    }

    @Override
    public void readTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < super.getTanks().size(); ++i) {
            super.getTanks().get(i).readFromNBT(nbt, registries, "tanks" + i);
        }
    }

    @Override
    public CompoundTag writeFluidConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        for (Direction side : Direction.values()) {
            super.getFluidConnections()[side.ordinal()].writeToNBT(nbt, registries, side);
        }
        return nbt;
    }

    @Override
    public void readFluidConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFluidConnections(nbt, registries);
        if (!super.hasConfigurableFluidConnections()) {
            return;
        }
        for (Direction side : Direction.values()) {
            super.getFluidConnections()[side.ordinal()].readFromNBT(nbt, registries, side);
        }
    }

    @Override
    public CompoundTag writeTankSettings(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putBoolean("inputTanksSeparated", super.getInputTanksSeparated());
        for (int i = 0; i < super.getTanks().size(); ++i) {
            nbt.putBoolean("voidUnusableFluidInput" + i, super.getVoidUnusableFluidInput(i));
            nbt.putInt("tankOutputSetting" + i, super.getTankOutputSetting(i).ordinal());
        }
        return nbt;
    }

    @Override
    public void readTankSettings(CompoundTag nbt, HolderLookup.Provider registries) {
        super.setInputTanksSeparated(nbt.getBoolean("inputTanksSeparated"));
        for (int i = 0; i < super.getTanks().size(); ++i) {
            super.setVoidUnusableFluidInput(i, nbt.getBoolean("voidUnusableFluidInput" + i));
            int ordinal = nbt.contains("voidExcessFluidOutput" + i) ? nbt.getBoolean("voidExcessFluidOutput" + i) ? 1 : 0 : nbt.getInt("tankOutputSetting" + i);
            super.setTankOutputSetting(i, TankOutputSetting.values()[ordinal]);
        }
    }

    @Override
    public CompoundTag writeEnergy(CompoundTag nbt, HolderLookup.Provider registries) {
        super.getEnergyStorage().writeToNBT(nbt, registries, "energyStorage");
        return nbt;
    }

    @Override
    public void readEnergy(CompoundTag nbt, HolderLookup.Provider registries) {
        super.getEnergyStorage().readFromNBT(nbt, registries, "energyStorage");
    }

    @Override
    public CompoundTag writeEnergyConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < 6; ++i) {
            nbt.putInt("energyConnections" + i, super.getEnergyConnections()[i].ordinal());
        }
        return nbt;
    }

    @Override
    public void readEnergyConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        if (super.hasConfigurableEnergyConnections()) {
            for (int i = 0; i < 6; ++i) {
                if (nbt.contains("energyConnections" + i)) {
                    super.getEnergyConnections()[i] = EnergyConnection.values()[nbt.getInt("energyConnections" + i)];
                }
            }
        }
    }
}