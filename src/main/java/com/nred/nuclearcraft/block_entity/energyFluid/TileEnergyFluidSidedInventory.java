package com.nred.nuclearcraft.block_entity.energyFluid;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.FluidConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public abstract class TileEnergyFluidSidedInventory extends TileEnergyFluidInventory {
    public TileEnergyFluidSidedInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, Set<ResourceKey<Fluid>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, name, size, inventoryConnections, capacity, NCMath.toInt(capacity), energyConnections, fluidCapacity, allowedFluids, fluidConnections);
    }

    public TileEnergyFluidSidedInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<Set<ResourceKey<Fluid>>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, name, size, inventoryConnections, capacity, NCMath.toInt(capacity), energyConnections, fluidCapacity, allowedFluids, fluidConnections);
    }

    public TileEnergyFluidSidedInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, Set<ResourceKey<Fluid>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, name, size, inventoryConnections, capacity, maxTransfer, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
    }

    public TileEnergyFluidSidedInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<Set<ResourceKey<Fluid>>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, name, size, inventoryConnections, capacity, maxTransfer, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
    }

//	// Capability TODO
//
//	@Override
//	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//			if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
//				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(side));
//			}
//			return null;
//		}
//		return super.getCapability(capability, side);
//	}
}
