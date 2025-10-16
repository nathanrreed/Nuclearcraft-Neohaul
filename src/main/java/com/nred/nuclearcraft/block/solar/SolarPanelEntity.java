package com.nred.nuclearcraft.block.solar;

import com.nred.nuclearcraft.block_entity.IPushEnergy;
import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.HashMap;
import java.util.Map;

import static com.nred.nuclearcraft.config.Config.SOLAR_CONFIG_CAPACITY;
import static com.nred.nuclearcraft.config.Config.SOLAR_CONFIG_PRODUCTION;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.SOLAR_PANEL_ENTITY_TYPE;

public class SolarPanelEntity extends BlockEntity implements IPushEnergy {
    private final int tier;
    public CustomEnergyHandler energyHandler;

    public SolarPanelEntity(BlockPos pos, BlockState blockState, int tier) {
        super(SOLAR_PANEL_ENTITY_TYPE.get(tier).get(), pos, blockState);
        this.tier = tier;

        energyHandler = new CustomEnergyHandler(SOLAR_CONFIG_CAPACITY.get(this.tier), false, true) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if (level.canSeeSky(worldPosition.above()) && level.isDay()) { // Have effectiveness if its raining
            energyHandler.internalInsertEnergy((int) ((level.isRainingAt(worldPosition.above()) ? 0.5 : 1) * SOLAR_CONFIG_PRODUCTION.get(tier)), false);
        }

        pushEnergy(level, energyHandler, pos, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("energy", energyHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyHandler.deserializeNBT(registries, tag.get("energy"));
    }

    Map<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> capCache = new HashMap<>();

    @Override
    public Map<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> getCapCache() {
        return capCache;
    }

    public void onCapInvalidate() {

    }
}