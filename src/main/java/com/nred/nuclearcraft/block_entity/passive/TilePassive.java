package com.nred.nuclearcraft.block_entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import static com.nred.nuclearcraft.config.Config2.cobble_gen_power;
import static com.nred.nuclearcraft.config.Config2.processor_passive_rate;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class TilePassive {
    public static abstract class CobblestoneGeneratorAbstract extends TilePassiveAbstract {
        public CobblestoneGeneratorAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String collector_type, int rateMult) {
            super(type, pos, blockState, "cobblestone_generator" + collector_type, Ingredient.of(Blocks.COBBLESTONE), processor_passive_rate[0] * rateMult, -cobble_gen_power * rateMult);
        }
    }

    public static class CobblestoneGenerator extends CobblestoneGeneratorAbstract {
        public CobblestoneGenerator(BlockPos pos, BlockState blockState) {
            super(COBBLESTONE_GENERATOR_ENTITY_TYPE.get(), pos, blockState, "", 1);
        }
    }

    public static class CobblestoneGeneratorCompact extends CobblestoneGeneratorAbstract {
        public CobblestoneGeneratorCompact(BlockPos pos, BlockState blockState) {
            super(COBBLESTONE_GENERATOR_COMPACT_ENTITY_TYPE.get(), pos, blockState, "_compact", 8);
        }
    }

    public static class CobblestoneGeneratorDense extends CobblestoneGeneratorAbstract {
        public CobblestoneGeneratorDense(BlockPos pos, BlockState blockState) {
            super(COBBLESTONE_GENERATOR_DENSE_ENTITY_TYPE.get(), pos, blockState, "_dense", 64);
        }
    }

    public static abstract class WaterSourceAbstract extends TilePassiveAbstract {
        public WaterSourceAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String collector_type, int rateMult) {
            super(type, pos, blockState, "water_source" + collector_type, FluidIngredient.of(Fluids.WATER), processor_passive_rate[1] * rateMult);
        }
    }

    public static class WaterSource extends WaterSourceAbstract {
        public WaterSource(BlockPos pos, BlockState blockState) {
            super(WATER_SOURCE_ENTITY_TYPE.get(), pos, blockState, "", 1);
        }
    }

    public static class WaterSourceCompact extends WaterSourceAbstract {
        public WaterSourceCompact(BlockPos pos, BlockState blockState) {
            super(WATER_SOURCE_COMPACT_ENTITY_TYPE.get(), pos, blockState, "_compact", 8);
        }
    }

    public static class WaterSourceDense extends WaterSourceAbstract {
        public WaterSourceDense(BlockPos pos, BlockState blockState) {
            super(WATER_SOURCE_DENSE_ENTITY_TYPE.get(), pos, blockState, "_dense", 64);
        }
    }

    public static abstract class NitrogenCollectorAbstract extends TilePassiveAbstract {
        public NitrogenCollectorAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String collector_type, int rateMult) {
            super(type, pos, blockState, "nitrogen_collector" + collector_type, FluidIngredient.of(GAS_MAP.get("nitrogen").still.get()), processor_passive_rate[2] * rateMult);
        }
    }

    public static class NitrogenCollector extends NitrogenCollectorAbstract {
        public NitrogenCollector(BlockPos pos, BlockState blockState) {
            super(NITROGEN_COLLECTOR_ENTITY_TYPE.get(), pos, blockState, "", 1);
        }
    }

    public static class NitrogenCollectorCompact extends NitrogenCollectorAbstract {
        public NitrogenCollectorCompact(BlockPos pos, BlockState blockState) {
            super(NITROGEN_COLLECTOR_COMPACT_ENTITY_TYPE.get(), pos, blockState, "_compact", 8);
        }
    }

    public static class NitrogenCollectorDense extends NitrogenCollectorAbstract {
        public NitrogenCollectorDense(BlockPos pos, BlockState blockState) {
            super(NITROGEN_COLLECTOR_DENSE_ENTITY_TYPE.get(), pos, blockState, "_dense", 64);
        }
    }
}