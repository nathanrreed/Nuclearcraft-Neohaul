package com.nred.nuclearcraft.helpers;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class CustomFluidStackHandler extends FluidTank {
    private final boolean allowInput;
    private final boolean allowOutput;

    public CustomFluidStackHandler(int capacity, boolean allowInput, boolean allowOutput) {
        super(capacity);
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
    }

    // Stop other mods using capability from doing unexpected things


    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (allowInput) {
            return super.fill(resource, action);
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (allowOutput) {
            return super.drain(maxDrain, action);
        }
        return FluidStack.EMPTY;
    }

    // Normal insert and extract for use within the mod
    public int internalInsertFluid(FluidStack stack, FluidAction simulate) {
        return super.fill(stack, simulate);
    }

    public FluidStack internalExtractFluid(int amount, FluidAction simulate) {
        return super.drain(amount, simulate);
    }
}
