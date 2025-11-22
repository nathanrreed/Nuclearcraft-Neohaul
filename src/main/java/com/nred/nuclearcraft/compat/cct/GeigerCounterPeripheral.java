package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block_entity.radiation.GeigerCounterEntity;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jspecify.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record GeigerCounterPeripheral(GeigerCounterEntity entity) implements IPeripheral {
    @LuaFunction(mainThread = true)
    public double getChunkRadiationLevel() {
        return entity.getChunkRadiationLevel();
    }

    @Override
    public String getType() {
        return ncLoc("geiger_counter").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof GeigerCounterPeripheral && ((GeigerCounterPeripheral) other).entity.equals(entity);
    }
}