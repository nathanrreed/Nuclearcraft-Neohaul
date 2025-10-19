package com.nred.nuclearcraft.block_entity.turbine;

import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class TurbineDynamoEntityPart extends AbstractTurbineEntity implements ITickable, ITileEnergy {
    protected final EnergyStorage backupStorage = new EnergyStorage(0L);

    protected final EnergyConnection[] energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.OUT);

    protected final EnergyTileWrapper[] energySides = ITileEnergy.getDefaultEnergySides(this);

    public Double conductivity;
    public String ruleID;

    public boolean isSearched = false, isInValidPosition = false;

    public PlacementRule<Turbine, AbstractTurbineEntity> placementRule;

    public TurbineDynamoEntityPart(BlockEntityType<?> type, BlockPos position, BlockState blockState) {
        super(type, position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    public void dynamoSearch(final ObjectSet<TurbineDynamoEntityPart> validCache, final ObjectSet<TurbineDynamoEntityPart> searchCache, final Long2ObjectMap<TurbineDynamoEntityPart> partFailCache, final Long2ObjectMap<TurbineDynamoEntityPart> assumedValidCache) {
        if (!isDynamoPartValid(partFailCache, assumedValidCache)) {
            return;
        }

        if (isSearched) {
            return;
        }

        isSearched = true;
        validCache.add(this);

        for (Direction dir : Direction.values()) {
            TurbineDynamoEntityPart part = getMultiblockController().get().getPartMap(TurbineDynamoEntityPart.class).get(getBlockPos().relative(dir).asLong());
            if (part != null) {
                searchCache.add(part);
            }
        }
    }

    public boolean isDynamoPartValid(final Long2ObjectMap<TurbineDynamoEntityPart> partFailCache, final Long2ObjectMap<TurbineDynamoEntityPart> assumedValidCache) {
        if (partFailCache.containsKey(worldPosition.asLong())) {
            return isInValidPosition = false;
        } else if (placementRule.requiresRecheck()) {
            isInValidPosition = placementRule.satisfied(this, false);
            if (isInValidPosition) {
                assumedValidCache.put(worldPosition.asLong(), this);
            }
            return isInValidPosition;
        } else if (isInValidPosition) {
            return true;
        }
        return isInValidPosition = placementRule.satisfied(this, false);
    }

    public boolean isSearchRoot() {
        for (String dep : placementRule.getDependencies()) {
            if (dep.equals("bearing")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update() {
        if (!level.isClientSide) {
            pushEnergy();
        }
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        if (!isInValidPosition || !isMachineAssembled()) {
            return backupStorage;
        }
        return getMultiblockController().get().energyStorage;
    }

    @Override
    public EnergyConnection[] getEnergyConnections() {
        return energyConnections;
    }

    @Override
    public @Nonnull EnergyTileWrapper[] getEnergySides() {
        return energySides;
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);

        writeEnergyConnections(nbt, registries);
        nbt.putBoolean("isInValidPosition", isInValidPosition);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);

        readEnergyConnections(nbt, registries);
        isInValidPosition = nbt.getBoolean("isInValidPosition");
    }
}