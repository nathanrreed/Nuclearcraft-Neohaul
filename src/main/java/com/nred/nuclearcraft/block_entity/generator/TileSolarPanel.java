package com.nred.nuclearcraft.block_entity.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.config.NCConfig.solar_power;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.SOLAR_PANEL_ENTITY_TYPE;

public class TileSolarPanel extends TilePassiveGenerator {
    public static class Basic extends TileSolarPanel {
        public Basic(BlockPos pos, BlockState blockState) {
            super(SOLAR_PANEL_ENTITY_TYPE.get(0).get(), pos, blockState, solar_power[0]);
        }
    }

    public static class Advanced extends TileSolarPanel {
        public Advanced(BlockPos pos, BlockState blockState) {
            super(SOLAR_PANEL_ENTITY_TYPE.get(1).get(), pos, blockState, solar_power[1]);
        }
    }

    public static class DU extends TileSolarPanel {
        public DU(BlockPos pos, BlockState blockState) {
            super(SOLAR_PANEL_ENTITY_TYPE.get(2).get(), pos, blockState, solar_power[2]);
        }
    }

    public static class Elite extends TileSolarPanel {
        public Elite(BlockPos pos, BlockState blockState) {
            super(SOLAR_PANEL_ENTITY_TYPE.get(3).get(), pos, blockState, solar_power[3]);
        }
    }

    public TileSolarPanel(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int power) {
        super(type, pos, blockState, power);
    }

    @Override
    public long getGenerated() {
        if (level.canSeeSky(worldPosition.above()) && level.isDay()) {
            return (long) ((level.isRainingAt(worldPosition.above()) ? 0.5 : 1) * power);
        }
        return 0;
    }
}