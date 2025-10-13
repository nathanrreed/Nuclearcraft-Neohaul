package com.nred.nuclearcraft.block.energy;

import com.nred.nuclearcraft.block.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block.internal.inventory.InventoryConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public abstract class TileEnergySidedInventory extends TileEnergyInventory {
    public TileEnergySidedInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections) {
        super(type, pos, blockState, name, size, inventoryConnections, capacity, energyConnections);
    }

    public TileEnergySidedInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
        super(type, pos, blockState, name, size, inventoryConnections, capacity, maxTransfer, energyConnections);
    }

//    // Capability TODO
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
//                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(side));
//            }
//            return null;
//        }
//        return super.getCapability(capability, side);
//    }
}