package com.nred.nuclearcraft.block_entity.distributor;

import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.multiblock.distributor.Distributor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.DISTRIBUTOR_INLET_ENTITY_TYPE;


public class DistributorInletEntity extends AbstractDistributorEntity implements ITileInventory, ITileFluid, ITileEnergy {
    protected final @Nonnull EnergyStorage backupStorage = new EnergyStorage(0L);
    protected final @Nonnull NonNullList<ItemStack> backupInventory = NonNullList.withSize(0, ItemStack.EMPTY);
    protected final @Nonnull List<Tank> backupTanks = Collections.emptyList();

    protected final @Nonnull EnergyConnection[] energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.IN);
    protected final @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(ItemSorption.IN);
    protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);

    protected final @Nonnull EnergyTileWrapper[] energySides = ITileEnergy.getDefaultEnergySides(this);
    protected final @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
    protected final @Nonnull ChemicalTileWrapper[] chemicalSides = ITileFluid.getDefaultChemicalSides(this);

    public DistributorInletEntity(BlockPos position, BlockState blockState) {
        super(DISTRIBUTOR_INLET_ENTITY_TYPE.get(), position, blockState);
    }

    // Inventory

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        Distributor multiblock = getMultiblockController().orElse(null);
        return multiblock == null ? backupInventory : multiblock.inventoryStacks;
    }

    @Override
    public int getMaxStackSize() {
        Distributor multiblock = getMultiblockController().orElse(null);
        return multiblock == null ? Distributor.BASE_ITEM_STACK_LIMIT : multiblock.itemStackLimit;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot != 0 || stack.isEmpty()) {
            return false;
        }

        ItemStack slotStack = getItem(slot);
        return slotStack.isEmpty() || ItemStack.isSameItemSameComponents(slotStack, stack);
    }

    @Override
    public @Nonnull InventoryConnection[] getInventoryConnections() {
        return inventoryConnections;
    }

    @Override
    public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
        System.arraycopy(connections, 0, inventoryConnections, 0, Math.min(connections.length, inventoryConnections.length));
    }

    @Override
    public ItemOutputSetting getItemOutputSetting(int slot) {
        return ItemOutputSetting.DEFAULT;
    }

    @Override
    public void setItemOutputSetting(int slot, ItemOutputSetting setting) {
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ITileInventory.super.setItem(slot, stack);
        Distributor multiblock = getMultiblockController().orElse(null);
        if (multiblock != null) {
            multiblock.storageUpdated = true;
        }
    }

    // Fluids

    @Override
    public @Nonnull List<Tank> getTanks() {
        Distributor multiblock = getMultiblockController().orElse(null);
        return multiblock == null ? backupTanks : Collections.singletonList(multiblock.tank);
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
    public @NonNull ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public void onWrapperFill(int fillAmount, IFluidHandler.FluidAction doFill) {
        ITileFluid.super.onWrapperFill(fillAmount, doFill);
        if (doFill.execute() && fillAmount > 0) {
            getMultiblockController().ifPresent(multiblock -> multiblock.storageUpdated = true);
        }
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

    // Energy

    @Override
    public EnergyStorage getEnergyStorage() {
        Distributor multiblock = getMultiblockController().orElse(null);
        return multiblock == null ? backupStorage : multiblock.energyStorage;
    }

    @Override
    public EnergyConnection[] getEnergyConnections() {
        return energyConnections;
    }

    @Override
    public @Nonnull EnergyTileWrapper[] getEnergySides() {
        return energySides;
    }

    @Override
    public int receiveEnergy(int maxReceive, Direction side, boolean simulate) {
        int received = ITileEnergy.super.receiveEnergy(maxReceive, side, simulate);
        if (!simulate && received > 0) {
            getMultiblockController().ifPresent(multiblock -> multiblock.storageUpdated = true);
        }
        return received;
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        if (getMultiblockController().isEmpty()) {
            writeInventory(nbt, registries);
            writeTanks(nbt, registries);
            writeEnergy(nbt, registries);
        }
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readInventory(nbt, registries);
        readTanks(nbt, registries);
        readEnergy(nbt, registries);
    }
}