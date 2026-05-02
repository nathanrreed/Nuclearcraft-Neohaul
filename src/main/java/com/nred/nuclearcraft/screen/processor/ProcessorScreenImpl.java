package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.block_entity.ITileFiltered;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.TileProcessorImpl.*;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.radiation.RadiationScrubberEntity;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.*;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ProcessorScreenImpl {

    public static class BasicProcessorScreen<MENU extends BasicProcessorMenu<TILE, PACKET>, TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ProcessorScreen<MENU, TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET>> {
        public BasicProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicUpgradableProcessorScreen<MENU extends BasicUpgradableProcessorMenu<TILE, PACKET>, TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends UpgradableProcessorScreen<MENU, TILE, PACKET, BasicUpgradableProcessorMenuInfo<TILE, PACKET>> {
        public BasicUpgradableProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicFilteredItemProcessorScreen<MENU extends BasicFilteredProcessorMenu<TILE, PACKET>, TILE extends BlockEntity & IBasicProcessor<TILE, PACKET> & ITileFiltered & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket> extends FilteredProcessorScreen<MENU, TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET>> {
        public BasicFilteredItemProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }
    public static class BasicFilteredFluidProcessorScreen<MENU extends BasicProcessorMenu<TILE, PACKET>, TILE extends BlockEntity & IBasicProcessor<TILE, PACKET> & ITileFiltered, PACKET extends ProcessorUpdatePacket> extends FilteredProcessorScreen<MENU, TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET>> {
        public BasicFilteredFluidProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicEnergyProcessorScreen<MENU extends BasicEnergyProcessorMenu<TILE>, TILE extends BasicEnergyProcessorEntity<TILE>> extends BasicProcessorScreen<MENU, TILE, EnergyProcessorUpdatePacket> {
        public BasicEnergyProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicUpgradableEnergyProcessorScreen<MENU extends BasicUpgradableEnergyProcessorMenu<TILE>, TILE extends BasicUpgradableEnergyProcessorEntity<TILE>> extends BasicUpgradableProcessorScreen<MENU, TILE, EnergyProcessorUpdatePacket> {
        public BasicUpgradableEnergyProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicEnergyProcessorDynScreen<MENU extends BasicEnergyProcessorMenuDyn> extends BasicEnergyProcessorScreen<MENU, BasicEnergyProcessorEntityDyn> {
        public BasicEnergyProcessorDynScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicUpgradableEnergyProcessorDynScreen<MENU extends BasicUpgradableEnergyProcessorMenuDyn> extends BasicUpgradableEnergyProcessorScreen<MENU, BasicUpgradableEnergyProcessorEntityDyn> {
        public BasicUpgradableEnergyProcessorDynScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class ManufactoryScreen extends BasicUpgradableEnergyProcessorScreen<ManufactoryMenu, ManufactoryEntity> {
        public ManufactoryScreen(ManufactoryMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "manufactory"));
        }
    }

    public static class SeparatorScreen extends BasicUpgradableEnergyProcessorScreen<SeparatorMenu, SeparatorEntity> {
        public SeparatorScreen(SeparatorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "separator"));
        }
    }

    public static class DecayHastenerScreen extends BasicUpgradableEnergyProcessorScreen<DecayHastenerMenu, DecayHastenerEntity> {
        public DecayHastenerScreen(DecayHastenerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "decay_hastener"));
        }
    }

    public static class FuelReprocessorScreen extends BasicUpgradableEnergyProcessorScreen<FuelReprocessorMenu, FuelReprocessorEntity> {
        public FuelReprocessorScreen(FuelReprocessorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fuel_reprocessor"));
        }
    }

    public static class AlloyFurnaceScreen extends BasicUpgradableEnergyProcessorScreen<AlloyFurnaceMenu, AlloyFurnaceEntity> {
        public AlloyFurnaceScreen(AlloyFurnaceMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "alloy_furnace"));
        }
    }

    public static class InfuserScreen extends BasicUpgradableEnergyProcessorScreen<InfuserMenu, InfuserEntity> {
        public InfuserScreen(InfuserMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_infuser"));
        }
    }

    public static class MelterScreen extends BasicUpgradableEnergyProcessorScreen<MelterMenu, MelterEntity> {
        public MelterScreen(MelterMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "melter"));
        }
    }

    public static class SupercoolerScreen extends BasicUpgradableEnergyProcessorScreen<SupercoolerMenu, SupercoolerEntity> {
        public SupercoolerScreen(SupercoolerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "supercooler"));
        }
    }

    public static class ElectrolyzerScreen extends BasicUpgradableEnergyProcessorScreen<ElectrolyzerMenu, ElectrolyzerEntity> {
        public ElectrolyzerScreen(ElectrolyzerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "electrolyzer"));
        }
    }

    public static class AssemblerScreen extends BasicUpgradableEnergyProcessorScreen<AssemblerMenu, AssemblerEntity> {
        public AssemblerScreen(AssemblerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "assembler"));
        }
    }

    public static class IngotFormerScreen extends BasicUpgradableEnergyProcessorScreen<IngotFormerMenu, IngotFormerEntity> {
        public IngotFormerScreen(IngotFormerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "ingot_former"));
        }
    }

    public static class PressurizerScreen extends BasicUpgradableEnergyProcessorScreen<PressurizerMenu, PressurizerEntity> {
        public PressurizerScreen(PressurizerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "pressurizer"));
        }
    }

    public static class ChemicalReactorScreen extends BasicUpgradableEnergyProcessorScreen<ChemicalReactorMenu, ChemicalReactorEntity> {
        public ChemicalReactorScreen(ChemicalReactorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "chemical_reactor"));
        }
    }

    public static class SaltMixerScreen extends BasicUpgradableEnergyProcessorScreen<SaltMixerMenu, SaltMixerEntity> {
        public SaltMixerScreen(SaltMixerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_mixer"));
        }
    }

    public static class CrystallizerScreen extends BasicUpgradableEnergyProcessorScreen<CrystallizerMenu, CrystallizerEntity> {
        public CrystallizerScreen(CrystallizerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "crystallizer"));
        }
    }

    public static class EnricherScreen extends BasicUpgradableEnergyProcessorScreen<EnricherMenu, EnricherEntity> {
        public EnricherScreen(EnricherMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_enricher"));
        }
    }

    public static class ExtractorScreen extends BasicUpgradableEnergyProcessorScreen<ExtractorMenu, ExtractorEntity> {
        public ExtractorScreen(ExtractorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_extractor"));
        }
    }

    public static class CentrifugeScreen extends BasicUpgradableEnergyProcessorScreen<CentrifugeMenu, CentrifugeEntity> {
        public CentrifugeScreen(CentrifugeMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "centrifuge"));
        }
    }

    public static class RockCrusherScreen extends BasicUpgradableEnergyProcessorScreen<RockCrusherMenu, RockCrusherEntity> {
        public RockCrusherScreen(RockCrusherMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "rock_crusher"));
        }
    }

    public static class ElectricFurnaceScreen extends BasicUpgradableEnergyProcessorScreen<ElectricFurnaceMenu, ElectricFurnaceEntity> {
        public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "electric_furnace"));
        }
    }

    public static class RadiationScrubberScreen extends BasicEnergyProcessorScreen<RadiationScrubberMenu, RadiationScrubberEntity> {
        public RadiationScrubberScreen(RadiationScrubberMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "radiation_scrubber"));
        }
    }
}
