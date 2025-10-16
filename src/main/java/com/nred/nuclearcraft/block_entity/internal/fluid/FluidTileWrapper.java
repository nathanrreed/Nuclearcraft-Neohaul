package com.nred.nuclearcraft.block_entity.internal.fluid;

import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class FluidTileWrapper implements IFluidHandler {
    public final ITileFluid tile;
    public final @Nonnull Direction side;

    public FluidTileWrapper(ITileFluid tile, @Nonnull Direction side) {
        this.tile = tile;
        this.side = side;
    }

    @Override
    public int getTanks() {
        return tile.getTanks().size();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return tile.getTanks().get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return tile.getTanks().get(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return tile.getTanks().get(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int amount = tile.fill(side, resource, action);
        tile.onWrapperFill(amount, action);
        return amount;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack stack = tile.drain(side, resource, action);
        tile.onWrapperDrain(stack, action);
        return stack;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack stack = tile.drain(side, maxDrain, action);
        tile.onWrapperDrain(stack, action);
        return stack;
    }
}