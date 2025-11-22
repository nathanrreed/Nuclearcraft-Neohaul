package com.nred.nuclearcraft.menu.processor;

import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.TileProcessorImpl.*;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl;
import com.nred.nuclearcraft.block_entity.radiation.RadiationScrubberEntity;
import com.nred.nuclearcraft.payload.multiblock.*;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.nred.nuclearcraft.registration.MenuRegistration.*;

public class ProcessorMenuImpl {
    public static class BasicProcessorMenu<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ProcessorMenu<TILE, PACKET, ProcessorMenuInfoImpl.BasicProcessorMenuInfo<TILE, PACKET>> {
        public BasicProcessorMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
            super(menuType, containerId, inventory, tile);
        }
    }

    public static class BasicUpgradableProcessorMenu<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends UpgradableProcessorMenu<TILE, PACKET, ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo<TILE, PACKET>> {
        public BasicUpgradableProcessorMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
            super(menuType, containerId, inventory, tile);
        }
    }

    public static class BasicFilteredProcessorMenu<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET> & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket> extends FilteredProcessorMenu<TILE, PACKET, ProcessorMenuInfoImpl.BasicProcessorMenuInfo<TILE, PACKET>> {
        public BasicFilteredProcessorMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
            super(menuType, containerId, inventory, tile);
        }
    }

    public static class BasicEnergyProcessorMenu<TILE extends BasicEnergyProcessorEntity<TILE>> extends BasicProcessorMenu<TILE, EnergyProcessorUpdatePacket> {
        public BasicEnergyProcessorMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
            super(menuType, containerId, inventory, tile);
        }
    }

    public static class BasicUpgradableEnergyProcessorMenu<TILE extends BasicUpgradableEnergyProcessorEntity<TILE>> extends BasicUpgradableProcessorMenu<TILE, EnergyProcessorUpdatePacket> {
        public BasicUpgradableEnergyProcessorMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
            super(menuType, containerId, inventory, tile);
        }
    }

    public static class BasicEnergyProcessorMenuDyn extends BasicEnergyProcessorMenu<BasicEnergyProcessorEntityDyn> {
        public BasicEnergyProcessorMenuDyn(MenuType<?> menuType, int containerId, Inventory inventory, BasicEnergyProcessorEntityDyn tile) {
            super(menuType, containerId, inventory, tile);
        }
    }

    public static class BasicUpgradableEnergyProcessorMenuDyn extends BasicUpgradableEnergyProcessorMenu<BasicUpgradableEnergyProcessorEntityDyn> {
        public BasicUpgradableEnergyProcessorMenuDyn(MenuType<?> menuType, int containerId, Inventory inventory, BasicUpgradableEnergyProcessorEntityDyn tile) {
            super(menuType, containerId, inventory, tile);
        }
    }

    public static class ManufactoryMenu extends BasicUpgradableEnergyProcessorMenu<ManufactoryEntity> {
        public ManufactoryMenu(int containerId, Inventory inventory, ManufactoryEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("manufactory").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public ManufactoryMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (ManufactoryEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class SeparatorMenu extends BasicUpgradableEnergyProcessorMenu<SeparatorEntity> {
        public SeparatorMenu(int containerId, Inventory inventory, SeparatorEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("separator").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public SeparatorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (SeparatorEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class DecayHastenerMenu extends BasicUpgradableEnergyProcessorMenu<DecayHastenerEntity> {
        public DecayHastenerMenu(int containerId, Inventory inventory, DecayHastenerEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("decay_hastener").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public DecayHastenerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (DecayHastenerEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class FuelReprocessorMenu extends BasicUpgradableEnergyProcessorMenu<FuelReprocessorEntity> {
        public FuelReprocessorMenu(int containerId, Inventory inventory, FuelReprocessorEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("fuel_reprocessor").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public FuelReprocessorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (FuelReprocessorEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class AlloyFurnaceMenu extends BasicUpgradableEnergyProcessorMenu<AlloyFurnaceEntity> {
        public AlloyFurnaceMenu(int containerId, Inventory inventory, AlloyFurnaceEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("alloy_furnace").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public AlloyFurnaceMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (AlloyFurnaceEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class InfuserMenu extends BasicUpgradableEnergyProcessorMenu<InfuserEntity> {
        public InfuserMenu(int containerId, Inventory inventory, InfuserEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("fluid_infuser").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public InfuserMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (InfuserEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class MelterMenu extends BasicUpgradableEnergyProcessorMenu<MelterEntity> {
        public MelterMenu(int containerId, Inventory inventory, MelterEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("melter").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public MelterMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (MelterEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class SupercoolerMenu extends BasicUpgradableEnergyProcessorMenu<SupercoolerEntity> {
        public SupercoolerMenu(int containerId, Inventory inventory, SupercoolerEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("supercooler").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public SupercoolerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (SupercoolerEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class ElectrolyzerMenu extends BasicUpgradableEnergyProcessorMenu<ElectrolyzerEntity> {
        public ElectrolyzerMenu(int containerId, Inventory inventory, ElectrolyzerEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("electrolyzer").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public ElectrolyzerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (ElectrolyzerEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class AssemblerMenu extends BasicUpgradableEnergyProcessorMenu<AssemblerEntity> {
        public AssemblerMenu(int containerId, Inventory inventory, AssemblerEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("assembler").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public AssemblerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (AssemblerEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class IngotFormerMenu extends BasicUpgradableEnergyProcessorMenu<IngotFormerEntity> {
        public IngotFormerMenu(int containerId, Inventory inventory, IngotFormerEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("ingot_former").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public IngotFormerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (IngotFormerEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class PressurizerMenu extends BasicUpgradableEnergyProcessorMenu<PressurizerEntity> {
        public PressurizerMenu(int containerId, Inventory inventory, PressurizerEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("pressurizer").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public PressurizerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (PressurizerEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class ChemicalReactorMenu extends BasicUpgradableEnergyProcessorMenu<ChemicalReactorEntity> {
        public ChemicalReactorMenu(int containerId, Inventory inventory, ChemicalReactorEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("chemical_reactor").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public ChemicalReactorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (ChemicalReactorEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class SaltMixerMenu extends BasicUpgradableEnergyProcessorMenu<SaltMixerEntity> {
        public SaltMixerMenu(int containerId, Inventory inventory, SaltMixerEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("fluid_mixer").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public SaltMixerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (SaltMixerEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class CrystallizerMenu extends BasicUpgradableEnergyProcessorMenu<CrystallizerEntity> {
        public CrystallizerMenu(int containerId, Inventory inventory, CrystallizerEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("crystallizer").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public CrystallizerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (CrystallizerEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class EnricherMenu extends BasicUpgradableEnergyProcessorMenu<EnricherEntity> {
        public EnricherMenu(int containerId, Inventory inventory, EnricherEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("fluid_enricher").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public EnricherMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (EnricherEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class ExtractorMenu extends BasicUpgradableEnergyProcessorMenu<ExtractorEntity> {
        public ExtractorMenu(int containerId, Inventory inventory, ExtractorEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("fluid_extractor").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public ExtractorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (ExtractorEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class CentrifugeMenu extends BasicUpgradableEnergyProcessorMenu<CentrifugeEntity> {
        public CentrifugeMenu(int containerId, Inventory inventory, CentrifugeEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("centrifuge").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public CentrifugeMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (CentrifugeEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class RockCrusherMenu extends BasicUpgradableEnergyProcessorMenu<RockCrusherEntity> {
        public RockCrusherMenu(int containerId, Inventory inventory, RockCrusherEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("rock_crusher").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public RockCrusherMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (RockCrusherEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class ElectricFurnaceMenu extends BasicUpgradableEnergyProcessorMenu<ElectricFurnaceEntity> {
        public ElectricFurnaceMenu(int containerId, Inventory inventory, ElectricFurnaceEntity tile) {
            super(PROCESSOR_MENU_TYPES.get("electric_furnace").get(), containerId, inventory, tile);
        }

        // Client Constructor
        public ElectricFurnaceMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (ElectricFurnaceEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class RadiationScrubberMenu extends BasicEnergyProcessorMenu<RadiationScrubberEntity> {
        public RadiationScrubberMenu(int containerId, Inventory inventory, RadiationScrubberEntity tile) {
            super(RADIATION_SCRUBBER_MENU_TYPE.get(), containerId, inventory, tile);
        }

        // Client Constructor
        public RadiationScrubberMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, (RadiationScrubberEntity) WorldHelper.getClientTile(extraData.readBlockPos()).orElseThrow(NullPointerException::new));
        }
    }

    public static class FissionIrradiatorMenu extends BasicFilteredProcessorMenu<FissionIrradiatorEntity, FissionIrradiatorUpdatePacket> {
        public FissionIrradiatorMenu(int containerId, Inventory inventory, FissionIrradiatorEntity irradiator) {
            super(FISSION_IRRADIATOR_MENU_TYPE.get(), containerId, inventory, irradiator);
        }

        // Client Constructor
        public FissionIrradiatorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }

    public static class FissionCoolerMenu extends BasicProcessorMenu<FissionCoolerEntity, FissionCoolerUpdatePacket> {
        public FissionCoolerMenu(int containerId, Inventory inventory, FissionCoolerEntity cooler) {
            super(FISSION_COOLER_MENU_TYPE.get(), containerId, inventory, cooler);
        }

        // Client Constructor
        public FissionCoolerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }

    public static class SolidFissionCellMenu extends BasicFilteredProcessorMenu<SolidFissionCellEntity, SolidFissionCellUpdatePacket> {
        public SolidFissionCellMenu(int containerId, Inventory inventory, SolidFissionCellEntity cell) {
            super(FISSION_SOLID_CELL_MENU_TYPE.get(), containerId, inventory, cell);
        }

        // Client Constructor
        public SolidFissionCellMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }

    public static class SaltFissionVesselMenu extends BasicProcessorMenu<SaltFissionVesselEntity, SaltFissionVesselUpdatePacket> {
        public SaltFissionVesselMenu(int containerId, Inventory inventory, SaltFissionVesselEntity vessel) {
            super(FISSION_SALT_VESSEL_MENU_TYPE.get(), containerId, inventory, vessel);
        }

        // Client Constructor
        public SaltFissionVesselMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }

    public static class SaltFissionHeaterMenu extends BasicProcessorMenu<SaltFissionHeaterEntity, SaltFissionHeaterUpdatePacket> {
        public SaltFissionHeaterMenu(int containerId, Inventory inventory, SaltFissionHeaterEntity heater) {
            super(FISSION_SALT_HEATER_MENU_TYPE.get(), containerId, inventory, heater);
        }

        // Client Constructor
        public SaltFissionHeaterMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }
}