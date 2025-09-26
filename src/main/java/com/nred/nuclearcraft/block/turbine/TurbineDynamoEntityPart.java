package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.IPushEnergy;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurbineDynamoEntityPart extends AbstractTurbineEntity implements IPushEnergy {
    public TurbineDynamoEntityPart(BlockEntityType<?> type, BlockPos position, BlockState blockState) {
        super(type, position, blockState);
    }

    public boolean isSearched = false, isInValidPosition = false;
    public PlacementRule<Turbine, AbstractTurbineEntity> placementRule;
    public Double conductivity;
    public String ruleID;
//    @Override
//    public void onMachineAssembled(Turbine multiblock) {
//        doStandardNullControllerResponse(multiblock);
//        super.onMachineAssembled(multiblock);
//    }

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

//    @Override TODO
//    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
//        super.syncDataFrom(data, registries, syncReason);
//    }
//
//    @Override
//    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
//        return super.syncDataTo(data, registries, syncReason);
//    }

    Map<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> capCache = new HashMap<>();

    @Override
    public Map<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> getCapCache() {
        return capCache;
    }

    public void onCapInvalidate() {

    }

    public void update() {
        pushEnergy(level, getMultiblockController().get().energyStorage, worldPosition, this);
    }

    @Override
    public Collection<Direction> getDirections() {
        Direction flow = getMultiblockController().get().flowDir;
        return List.of(flow, flow.getOpposite());
    }
}