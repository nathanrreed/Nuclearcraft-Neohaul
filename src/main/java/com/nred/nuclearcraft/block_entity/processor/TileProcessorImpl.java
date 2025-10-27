package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.PROCESSOR_ENTITY_TYPE;

public class TileProcessorImpl {
    public static abstract class BasicEnergyProcessorEntity<TILE extends BasicEnergyProcessorEntity<TILE>> extends EnergyProcessorEntity<TILE, ProcessorMenuInfoImpl.BasicProcessorMenuInfo<TILE, EnergyProcessorUpdatePacket>> implements IBasicProcessor<TILE, EnergyProcessorUpdatePacket> {
        protected BasicEnergyProcessorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name) {
            super(type, pos, blockState, name);
        }
    }

    public static abstract class BasicUpgradableEnergyProcessorEntity<TILE extends BasicUpgradableEnergyProcessorEntity<TILE>> extends UpgradableEnergyProcessorEntity<TILE, ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo<TILE, EnergyProcessorUpdatePacket>> implements IBasicUpgradableProcessor<TILE, EnergyProcessorUpdatePacket> {
        protected BasicUpgradableEnergyProcessorEntity(BlockPos pos, BlockState blockState, String name) {
            super(PROCESSOR_ENTITY_TYPE.get(name).get(), pos, blockState, name);
        }
    }

    public static class BasicEnergyProcessorEntityDyn extends BasicEnergyProcessorEntity<BasicEnergyProcessorEntityDyn> {
        public BasicEnergyProcessorEntityDyn(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name) {
            super(type, pos, blockState, name);
        }
    }

    public static class BasicUpgradableEnergyProcessorEntityDyn extends BasicUpgradableEnergyProcessorEntity<BasicUpgradableEnergyProcessorEntityDyn> {
        public BasicUpgradableEnergyProcessorEntityDyn(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name) {
            super(pos, blockState, name);
        }
    }

    public static class ManufactoryEntity extends BasicUpgradableEnergyProcessorEntity<ManufactoryEntity> {
        public ManufactoryEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "manufactory");
        }
    }

    public static class SeparatorEntity extends BasicUpgradableEnergyProcessorEntity<SeparatorEntity> {
        public SeparatorEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "separator");
        }
    }

    public static class DecayHastenerEntity extends BasicUpgradableEnergyProcessorEntity<DecayHastenerEntity> {
        public DecayHastenerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "decay_hastener");
        }
    }

    public static class FuelReprocessorEntity extends BasicUpgradableEnergyProcessorEntity<FuelReprocessorEntity> {
        public FuelReprocessorEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "fuel_reprocessor");
        }
    }

    public static class AlloyFurnaceEntity extends BasicUpgradableEnergyProcessorEntity<AlloyFurnaceEntity> {
        public AlloyFurnaceEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "alloy_furnace");
        }
    }

    public static class InfuserEntity extends BasicUpgradableEnergyProcessorEntity<InfuserEntity> {
        public InfuserEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "infuser");
        }
    }

    public static class MelterEntity extends BasicUpgradableEnergyProcessorEntity<MelterEntity> {
        public MelterEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "melter");
        }
    }

    public static class SupercoolerEntity extends BasicUpgradableEnergyProcessorEntity<SupercoolerEntity> {
        public SupercoolerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "supercooler");
        }
    }

    public static class ElectrolyzerEntity extends BasicUpgradableEnergyProcessorEntity<ElectrolyzerEntity> {
        public ElectrolyzerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "electrolyzer");
        }
    }

    public static class AssemblerEntity extends BasicUpgradableEnergyProcessorEntity<AssemblerEntity> {
        public AssemblerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "assembler");
        }
    }

    public static class IngotFormerEntity extends BasicUpgradableEnergyProcessorEntity<IngotFormerEntity> {
        public IngotFormerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "ingot_former");
        }
    }

    public static class PressurizerEntity extends BasicUpgradableEnergyProcessorEntity<PressurizerEntity> {
        public PressurizerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "pressurizer");
        }
    }

    public static class ChemicalReactorEntity extends BasicUpgradableEnergyProcessorEntity<ChemicalReactorEntity> {
        public ChemicalReactorEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "chemical_reactor");
        }
    }

    public static class SaltMixerEntity extends BasicUpgradableEnergyProcessorEntity<SaltMixerEntity> {
        public SaltMixerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "salt_mixer");
        }
    }

    public static class CrystallizerEntity extends BasicUpgradableEnergyProcessorEntity<CrystallizerEntity> {
        public CrystallizerEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "crystallizer");
        }
    }

    public static class EnricherEntity extends BasicUpgradableEnergyProcessorEntity<EnricherEntity> {
        public EnricherEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "enricher");
        }
    }

    public static class ExtractorEntity extends BasicUpgradableEnergyProcessorEntity<ExtractorEntity> {
        public ExtractorEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "extractor");
        }
    }

    public static class CentrifugeEntity extends BasicUpgradableEnergyProcessorEntity<CentrifugeEntity> {
        public CentrifugeEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "centrifuge");
        }
    }

    public static class RockCrusherEntity extends BasicUpgradableEnergyProcessorEntity<RockCrusherEntity> {
        public RockCrusherEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "rock_crusher");
        }
    }

    public static class ElectricFurnaceEntity extends BasicUpgradableEnergyProcessorEntity<ElectricFurnaceEntity> {
        public ElectricFurnaceEntity(BlockPos pos, BlockState blockState) {
            super(pos, blockState, "electric_furnace");
        }
    }
}