package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
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

public class FissionConductorEntity extends AbstractFissionEntity implements IFissionComponent {
    private FissionCluster cluster = null;
    private long heat = 0L;

    public FissionConductorEntity(BlockPos position, BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("conductor").get(), position, blockState);
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
        return true;
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return false;
    }

    @Override
    public void resetStats() {
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

    // OpenComputers

    @Override
    public String getCCKey() {
        return "conductor";
    }

    @Override
    public Object getCCInfo() {
        return new Object2ObjectLinkedOpenHashMap<String, Object>();
    }
}