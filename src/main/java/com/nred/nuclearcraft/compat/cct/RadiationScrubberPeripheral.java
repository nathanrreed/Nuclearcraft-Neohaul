package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block_entity.radiation.RadiationScrubberEntity;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jspecify.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record RadiationScrubberPeripheral(RadiationScrubberEntity entity) implements IPeripheral {
    @LuaFunction(mainThread = true)
    public double getRadiationRemovalRate() {
        return entity.getRawScrubberRate();
    }

    @LuaFunction(mainThread = true)
    public double getEfficiency() {
        return Math.abs(100D * entity.getRadiationContributionFraction() / RadiationScrubberEntity.getMaxScrubberFraction());
    }

    @Override
    public String getType() {
        return ncLoc("radiation_scrubber").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof RadiationScrubberPeripheral && ((RadiationScrubberPeripheral) other).entity.equals(entity);
    }
}