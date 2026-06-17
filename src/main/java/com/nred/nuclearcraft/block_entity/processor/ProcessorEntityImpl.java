package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.processor.ProcessorRecipeDyn;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.PROCESSOR_ENTITY_TYPE;

public class ProcessorEntityImpl {
    public static abstract class BasicEnergyProcessorEntity<RECIPE extends BasicRecipe, TILE extends BasicEnergyProcessorEntity<RECIPE, TILE>> extends EnergyProcessorEntity<TILE, BasicProcessorMenuInfo<TILE, EnergyProcessorUpdatePacket, RECIPE>, RECIPE> implements IBasicProcessor<TILE, EnergyProcessorUpdatePacket, RECIPE> {
        protected BasicEnergyProcessorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name) {
            super(type, pos, blockState, name);
        }
    }

    public static abstract class BasicUpgradableEnergyProcessorEntity<RECIPE extends BasicRecipe, TILE extends BasicUpgradableEnergyProcessorEntity<RECIPE, TILE>> extends UpgradableEnergyProcessorEntity<TILE, BasicUpgradableProcessorMenuInfo<TILE, EnergyProcessorUpdatePacket, RECIPE>, RECIPE> implements IBasicUpgradableProcessor<TILE, EnergyProcessorUpdatePacket, RECIPE> {
        protected BasicUpgradableEnergyProcessorEntity(BlockPos pos, BlockState blockState, String name) {
            super(PROCESSOR_ENTITY_TYPE.get(name).get(), pos, blockState, name);
        }
    }

    public static class BasicEnergyProcessorEntityDyn extends BasicEnergyProcessorEntity<ProcessorRecipeDyn, BasicEnergyProcessorEntityDyn> {
        public BasicEnergyProcessorEntityDyn(BlockPos pos, BlockState blockState, String name) {
            super(PROCESSOR_ENTITY_TYPE.get(name).get(), pos, blockState, name);
        }
    }

    public static class BasicUpgradableEnergyProcessorEntityDyn extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipeDyn, BasicUpgradableEnergyProcessorEntityDyn> {
        public BasicUpgradableEnergyProcessorEntityDyn(BlockPos pos, BlockState blockState, String name) {
            super(pos, blockState, name);
        }
    }

    public static class ManufactoryEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, ManufactoryEntity> {
        public ManufactoryEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "manufactory");
        }
    }

    public static class SeparatorEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, SeparatorEntity> {
        public SeparatorEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "separator");
        }
    }

    public static class DecayHastenerEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, DecayHastenerEntity> {
        public DecayHastenerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "decay_hastener");
        }
    }

    public static class FuelReprocessorEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, FuelReprocessorEntity> {
        public FuelReprocessorEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "fuel_reprocessor");
        }
    }

    public static class AlloyFurnaceEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, AlloyFurnaceEntity> {
        public AlloyFurnaceEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "alloy_furnace");
        }
    }

    public static class InfuserEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, InfuserEntity> {
        public InfuserEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "fluid_infuser");
        }
    }

    public static class MelterEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, MelterEntity> {
        public MelterEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "melter");
        }
    }

    public static class SupercoolerEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, SupercoolerEntity> {
        public SupercoolerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "supercooler");
        }
    }

    public static class ElectrolyzerEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, ElectrolyzerEntity> {
        public ElectrolyzerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "electrolyzer");
        }
    }

    public static class AssemblerEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, AssemblerEntity> {
        public AssemblerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "assembler");
        }
    }

    public static class IngotFormerEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, IngotFormerEntity> {
        public IngotFormerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "ingot_former");
        }
    }

    public static class PressurizerEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, PressurizerEntity> {
        public PressurizerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "pressurizer");
        }
    }

    public static class ChemicalReactorEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, ChemicalReactorEntity> {
        public ChemicalReactorEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "chemical_reactor");
        }
    }

    public static class SaltMixerEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, SaltMixerEntity> {
        public SaltMixerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "fluid_mixer");
        }
    }

    public static class CrystallizerEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, CrystallizerEntity> {
        public CrystallizerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "crystallizer");
        }
    }

    public static class EnricherEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, EnricherEntity> {
        public EnricherEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "fluid_enricher");
        }
    }

    public static class ExtractorEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, ExtractorEntity> {
        public ExtractorEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "fluid_extractor");
        }
    }

    public static class CentrifugeEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, CentrifugeEntity> {
        public CentrifugeEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "centrifuge");
        }
    }

    public static class RockCrusherEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, RockCrusherEntity> {
        public RockCrusherEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "rock_crusher");
        }
    }

    public static class ElectricFurnaceEntity extends BasicUpgradableEnergyProcessorEntity<ProcessorRecipe, ElectricFurnaceEntity> {
        public ElectricFurnaceEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "electric_furnace");
        }
    }
}