package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IPushEnergy extends IPush {
    default void pushEnergy(Level level, CustomEnergyHandler energyHandler, BlockPos pos, BlockEntity be) {
        if (!level.isClientSide && energyHandler.getEnergyStored() > 0) {
            for (Direction dir : getDirections()) {
                if (getCapCache().get(dir) == null) {
                    getCapCache().put(dir, BlockCapabilityCache.create(Capabilities.EnergyStorage.BLOCK, ((ServerLevel) level), pos.relative(dir), dir.getOpposite(), () -> !be.isRemoved(), this::onCapInvalidate));
                }
                if (getCapCache().get(dir).getCapability() != null) {
                    @Nullable IEnergyStorage cap = getCapCache().get(dir).getCapability();
                    int internal = energyHandler.internalExtractEnergy(energyHandler.getEnergyStored(), false);
                    energyHandler.internalInsertEnergy(internal - cap.receiveEnergy(internal, false), false);
                }
            }
        }
    }

    Map<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> getCapCache();
}