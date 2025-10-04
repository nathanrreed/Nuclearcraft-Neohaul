package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.fission.IFissionFuelComponent.ModeratorBlockInfo;
import com.nred.nuclearcraft.block.fission.IFissionFuelComponent.ModeratorLine;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionNeutronShieldType;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.PosHelper;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Iterator;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class FissionShieldEntity extends AbstractFissionEntity implements IFissionHeatingComponent {
    private final FissionNeutronShieldType fissionNeutronShieldType;

    public FissionShieldEntity(final BlockPos position, final BlockState blockState, FissionNeutronShieldType fissionNeutronShieldType) {
        super(FISSION_ENTITY_TYPE.get("shield").get(), position, blockState);
        this.fissionNeutronShieldType = fissionNeutronShieldType;
    }

    public double heatPerFlux, efficiency;

    public boolean isShielding = false, inCompleteModeratorLine = false, activeModerator = false;
    protected boolean[] validActiveModeratorPos = new boolean[]{false, false, false, false, false, false};

    protected FissionCluster cluster = null;
    protected long heat = 0L;

    protected int flux = 0;
    protected ModeratorLine[] activeModeratorLines = new ModeratorLine[]{null, null, null};

    public static class Variant extends FissionShieldEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionNeutronShieldType fissionNeutronShieldType) {
            super(position, blockState, fissionNeutronShieldType);
        }
    }

    public static class BoronSilver extends Variant {
        public BoronSilver(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionNeutronShieldType.BORON_SILVER);
        }
    }

    @Override
    public @javax.annotation.Nullable FissionCluster getCluster() {
        return cluster;
    }

    @Override
    public void setClusterInternal(@Nullable FissionCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        return inCompleteModeratorLine;
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return inCompleteModeratorLine;
    }

    @Override
    public boolean isActiveModerator() {
        return activeModerator;
    }

    @Override
    public void resetStats() {
        inCompleteModeratorLine = activeModerator = false;
        for (Direction dir : Direction.values()) {
            validActiveModeratorPos[dir.ordinal()] = false;
        }
        flux = 0;
        for (Axis axis : PosHelper.AXES) {
            activeModeratorLines[PosHelper.getAxisIndex(axis)] = null;
        }
    }

    @Override
    public boolean isClusterRoot() {
        return true;
    }

    @Override
    public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        IFissionHeatingComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache, simulate);
    }

    @Override
    public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {

    }

    @Override
    public boolean isNullifyingSources(Direction side, boolean simulate) {
        return isShielding;
    }

    @Override
    public long getHeatStored() {
        return heat;
    }

    @Override
    public void setHeatStored(long heat) {
        this.heat = heat;
    }

    @Override
    public long getRawHeating(boolean simulate) {
        return isFunctional(simulate) ? (long) Math.min(Long.MAX_VALUE, Math.floor(flux * heatPerFlux)) : 0L;
    }

    @Override
    public long getRawHeatingIgnoreCoolingPenalty(boolean simulate) {
        return 0L;
    }

    @Override
    public double getEffectiveHeating(boolean simulate) {
        return isFunctional(simulate) ? flux * heatPerFlux * efficiency : 0D;
    }

    @Override
    public double getEffectiveHeatingIgnoreCoolingPenalty(boolean simulate) {
        return 0D;
    }

    // Moderator Line

    @Override
    public ModeratorBlockInfo getModeratorBlockInfo(Direction dir, boolean validActiveModeratorPosIn) {
        FissionReactorLogic logic = getLogic();
        int index = dir.ordinal();
        this.validActiveModeratorPos[index] = logic != null && logic.isShieldActiveModerator(this, validActiveModeratorPosIn);
        return logic != null ? logic.getShieldModeratorBlockInfo(this, this.validActiveModeratorPos[index]) : null;
    }

    @Override
    public void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {
    }

    @Override
    public void onModeratorLineComplete(ModeratorLine line, ModeratorBlockInfo thisInfo, Direction dir) {
        inCompleteModeratorLine = true;
        if (validActiveModeratorPos[dir.ordinal()]) {
            activeModerator = true;
        }
        int index = PosHelper.getAxisIndex(dir.getAxis());
        if (activeModeratorLines[index] == null) {
            flux += getLineFluxContribution(line, thisInfo);
            activeModeratorLines[index] = line;
        }
    }

    protected int getLineFluxContribution(ModeratorLine line, ModeratorBlockInfo thisInfo) {
        long innerFlux = 0, outerFlux = 0;
        boolean inner = true;
        for (ModeratorBlockInfo info : line.info) {
            if (info == thisInfo) {
                inner = false;
            }
            if (inner) {
                innerFlux += info.fluxFactor;
            } else {
                outerFlux += info.fluxFactor;
            }
        }

        if (line.fluxSink != null) {
            if (line.fluxSink instanceof IFissionFuelComponent) {
                return NCMath.toInt(innerFlux + outerFlux);
            } else {
                return NCMath.toInt(innerFlux);
            }
        }
//        else if (line.reflectorRecipe != null) { TODO
//            return NCMath.toInt(Math.floor((innerFlux + outerFlux) * (1D + line.reflectorRecipe.getFissionReflectorReflectivity())));
//        }
        return NCMath.toInt(innerFlux);
    }
}