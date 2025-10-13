package com.nred.nuclearcraft.block.energy;

import com.nred.nuclearcraft.block.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block.inventory.ITileInventory;
import com.nred.nuclearcraft.block.internal.fluid.TankVoid;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class TileEnergyInventory extends TileEnergy implements ITileInventory {
    private @Nonnull
    final String inventoryName;

    private @Nonnull
    final NonNullList<ItemStack> inventoryStacks;

    private @Nonnull InventoryConnection[] inventoryConnections;

    private @Nonnull
    final List<ItemOutputSetting> itemOutputSettings;

    public TileEnergyInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections) {
        this(type, pos, blockState, name, size, inventoryConnections, capacity, NCMath.toInt(capacity), energyConnections);
    }

    public TileEnergyInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
        super(type, pos, blockState, capacity, maxTransfer, energyConnections);
        inventoryName = MODID + ".container." + name;
        inventoryStacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.inventoryConnections = inventoryConnections;
        itemOutputSettings = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            itemOutputSettings.add(ItemOutputSetting.DEFAULT);
        }
    }

    // Inventory

//    @Override TODO REMOVE
//    public Component getName() {
//        return Component.translatable(inventoryName);
//    }

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

//    // Capability TODO
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return !getInventoryStacks().isEmpty() && hasInventorySideCapability(side);
//        }
//        return super.hasCapability(capability, side);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
//                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(null));
//            }
//            return null;
//        }
//        return super.getCapability(capability, side);
//    }
}
