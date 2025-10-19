package com.nred.nuclearcraft.block_entity.energyFluid;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.FluidConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class TileEnergyFluidInventory extends TileEnergyFluid implements ITileInventory {
    private @Nonnull NonNullList<ItemStack> inventoryStacks = null;

    private @Nonnull InventoryConnection[] inventoryConnections = null;

    private @Nonnull List<ItemOutputSetting> itemOutputSettings = null;

    public TileEnergyFluidInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, Set<ResourceLocation> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        this(type, pos, blockState, name, size, inventoryConnections, capacity, NCMath.toInt(capacity), energyConnections, fluidCapacity, allowedFluids, fluidConnections);
    }

    public TileEnergyFluidInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<Set<ResourceLocation>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        this(type, pos, blockState, name, size, inventoryConnections, capacity, NCMath.toInt(capacity), energyConnections, fluidCapacity, allowedFluids, fluidConnections);
    }

    public TileEnergyFluidInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, Set<ResourceLocation> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, capacity, maxTransfer, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
        initTileEnergyFluidInventory(name, size, inventoryConnections);
    }

    public TileEnergyFluidInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<Set<ResourceLocation>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, capacity, maxTransfer, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
        initTileEnergyFluidInventory(name, size, inventoryConnections);
    }

    protected void initTileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections) {
        inventoryStacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.inventoryConnections = inventoryConnections;
        itemOutputSettings = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            itemOutputSettings.add(ItemOutputSetting.DEFAULT);
        }
    }

    // Inventory

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        return inventoryStacks;
    }

    @Override
    public @Nonnull InventoryConnection[] getInventoryConnections() {
        return inventoryConnections;
    }

    @Override
    public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
        inventoryConnections = connections;
    }

    @Override
    public ItemOutputSetting getItemOutputSetting(int slot) {
        return itemOutputSettings.get(slot);
    }

    @Override
    public void setItemOutputSetting(int slot, ItemOutputSetting setting) {
        itemOutputSettings.set(slot, setting);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeInventory(nbt, registries);
        writeInventoryConnections(nbt, registries);
        writeSlotSettings(nbt, registries);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readInventory(nbt, registries);
        readInventoryConnections(nbt, registries);
        readSlotSettings(nbt, registries);
    }
}