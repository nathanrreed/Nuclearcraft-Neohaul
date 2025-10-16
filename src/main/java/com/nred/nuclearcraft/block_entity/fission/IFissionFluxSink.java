package com.nred.nuclearcraft.block_entity.fission;

import net.minecraft.core.Direction;

public interface IFissionFluxSink extends IFissionComponent {

    /**
     * True if neutron flux can be used by and should affect this part.
     */
    boolean isAcceptingFlux(Direction side, boolean simulate);

    default boolean canSupportActiveModerator(boolean activeModeratorPos, boolean simulate) {
        return false;
    }

    /**
     * Additional multiplier for the actively searching fuel component's moderator line efficiency.
     */
    double moderatorLineEfficiencyFactor();

    long getFlux();

    void addFlux(long addedFlux);

    void refreshIsProcessing(boolean checkCluster, boolean simulate);

    default void onEndModeratorLine(boolean simulate) {
        refreshIsProcessing(false, simulate);
    }
}
