package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Iterator;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class SolidFissionHeatSinkEntity extends AbstractFissionEntity implements IFissionCoolingComponent {
    public final FissionHeatSinkType heatSinkType;

    public PlacementRule<FissionReactor, AbstractFissionEntity> placementRule;

    private FissionCluster cluster = null;
    private long heat = 0L;
    public int coolingRate;
    public boolean isInValidPosition = false;

    public SolidFissionHeatSinkEntity(final BlockPos position, final BlockState blockState, FissionHeatSinkType heatSinkType) {
        super(FISSION_ENTITY_TYPE.get("heat_sink").get(), position, blockState);
        this.heatSinkType = heatSinkType;
        this.placementRule = FissionPlacement.RULE_MAP.get(heatSinkType.getName() + "_sink");
        this.coolingRate = heatSinkType.getCoolingRate();
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }

    // IFissionComponent

    @Override
    public @Nullable FissionCluster getCluster() {
        return cluster;
    }

    @Override
    public void setClusterInternal(@Nullable FissionCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        if (componentFailCache.containsKey(worldPosition.asLong())) {
            return isInValidPosition = false;
        } else if (placementRule.requiresRecheck()) {
            isInValidPosition = placementRule.satisfied(this, simulate);
            if (isInValidPosition) {
                assumedValidCache.put(worldPosition.asLong(), this);
            }
            return isInValidPosition;
        } else if (isInValidPosition) {
            return true;
        }
        return isInValidPosition = placementRule.satisfied(this, simulate);
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return isInValidPosition;
    }

    @Override
    public void resetStats() {
        isInValidPosition = false;
    }

    @Override
    public boolean isClusterRoot() {
        return false;
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
    public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {

    }

    @Override
    public boolean isNullifyingSources(Direction side, boolean simulate) {
        return false;
    }

    @Override
    public long getCooling(boolean simulate) {
        return coolingRate;
    }

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putLong("clusterHeat", heat);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        heat = nbt.getLong("clusterHeat");
    }

    // ComputerCraft

    @Override
    public String getCCKey() {
        return "sink";
    }

    @Override
    public Object getCCInfo() {
        Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
        entry.put("type", heatSinkType.getSerializedName());
        entry.put("cooling", getCooling(false));
        entry.put("is_valid", isInValidPosition);
        return entry;
    }
}