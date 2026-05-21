package com.nred.nuclearcraft.multiblock.hx;

import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity;

public class HeatExchangerTransferProposal {
    public final HeatExchangerInletEntity inlet;

    public final double sharedTransferRate;
    public final double tubeRecipeRateMultiplier;
    public final double shellRecipeRateMultiplier;
    public final double heatTransferRateMultiplier;
    public final double absMeanTempDiff;
    public final int usefulTubeCount;

    public HeatExchangerTransferProposal(HeatExchangerInletEntity inlet, double sharedTransferRate, double tubeRecipeRateMultiplier, double shellRecipeRateMultiplier, double heatTransferRateMultiplier, double absMeanTempDiff, int usefulTubeCount) {
        this.inlet = inlet;
        this.sharedTransferRate = sharedTransferRate;
        this.tubeRecipeRateMultiplier = tubeRecipeRateMultiplier;
        this.shellRecipeRateMultiplier = shellRecipeRateMultiplier;
        this.heatTransferRateMultiplier = heatTransferRateMultiplier;
        this.absMeanTempDiff = absMeanTempDiff;
        this.usefulTubeCount = usefulTubeCount;
    }

    public HeatExchangerTransferProposal withSharedTransferRate(double newSharedTransferRate) {
        return new HeatExchangerTransferProposal(inlet, newSharedTransferRate, tubeRecipeRateMultiplier, shellRecipeRateMultiplier, heatTransferRateMultiplier, absMeanTempDiff, usefulTubeCount);
    }
}