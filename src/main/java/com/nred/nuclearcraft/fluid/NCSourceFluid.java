package com.nred.nuclearcraft.fluid;

import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class NCSourceFluid extends BaseFlowingFluid {
    private final int amount;

    public NCSourceFluid(Properties properties, int level) {
        super(properties);
        this.amount = level;
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    public int getAmount(FluidState state) {
        return this.amount;
    }
}