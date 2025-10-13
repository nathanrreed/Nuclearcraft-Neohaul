package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block.ITileFiltered;
import com.nred.nuclearcraft.block.fluid.ITileFluid;
import com.nred.nuclearcraft.block.internal.fluid.Tank;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public interface ITileFilteredFluid extends ITileFiltered, ITileFluid {

    @Nonnull
    List<Tank> getTanksInternal();

    @Nonnull
    List<Tank> getFilterTanks();

    default void clearFilterTank(int tankNumber) {
        getFilterTanks().get(tankNumber).setFluidStored(FluidStack.EMPTY);
    }
}
