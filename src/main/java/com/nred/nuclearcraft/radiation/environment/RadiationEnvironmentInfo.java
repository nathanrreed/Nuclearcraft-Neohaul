package com.nred.nuclearcraft.radiation.environment;

import com.nred.nuclearcraft.block_entity.radiation.ITileRadiationEnvironment;
import com.nred.nuclearcraft.util.FourPos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RadiationEnvironmentInfo {
    public final FourPos pos;
    public final ConcurrentMap<FourPos, ITileRadiationEnvironment> tileMap = new ConcurrentHashMap<>();

    public RadiationEnvironmentInfo(FourPos pos) {
        this.pos = pos;
    }

    public RadiationEnvironmentInfo(FourPos pos, ITileRadiationEnvironment tile) {
        this.pos = pos;
        addToTileMap(tile);
    }

    public void addToTileMap(ITileRadiationEnvironment tile) {
        FourPos tilePos = tile.getFourPos();
        if (tilePos.getDimension() != pos.getDimension()) {
            return;
        }
        tileMap.put(tilePos, tile);
    }
}