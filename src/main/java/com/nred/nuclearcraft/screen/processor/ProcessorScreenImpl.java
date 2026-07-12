package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.block_entity.ITileFiltered;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.ProcessorEntityImpl.*;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.radiation.RadiationScrubberEntity;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.*;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.recipe.processor.ProcessorRecipeDyn;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ProcessorScreenImpl {

    public static class BasicProcessorScreen<MENU extends BasicProcessorMenu<TILE, PACKET, RECIPE>, TILE extends BlockEntity & IBasicProcessor<TILE, PACKET, RECIPE>, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends ProcessorScreen<MENU, TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE> {
        public BasicProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicUpgradableProcessorScreen<MENU extends BasicUpgradableProcessorMenu<TILE, PACKET, RECIPE>, TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET, RECIPE>, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends UpgradableProcessorScreen<MENU, TILE, PACKET, BasicUpgradableProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE> {
        public BasicUpgradableProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicFilteredItemProcessorScreen<MENU extends BasicFilteredProcessorMenu<TILE, PACKET, RECIPE>, TILE extends BlockEntity & IBasicProcessor<TILE, PACKET, RECIPE> & ITileFiltered & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends FilteredProcessorScreen<MENU, TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE> {
        public BasicFilteredItemProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicFilteredFluidProcessorScreen<MENU extends BasicProcessorMenu<TILE, PACKET, RECIPE>, TILE extends BlockEntity & IBasicProcessor<TILE, PACKET, RECIPE> & ITileFiltered, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends FilteredProcessorScreen<MENU, TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE> {
        public BasicFilteredFluidProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicEnergyProcessorScreen<MENU extends BasicEnergyProcessorMenu<TILE, RECIPE>, TILE extends BasicEnergyProcessorEntity<RECIPE, TILE>, RECIPE extends BasicRecipe> extends BasicProcessorScreen<MENU, TILE, EnergyProcessorUpdatePacket, RECIPE> {
        public BasicEnergyProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicUpgradableEnergyProcessorScreen<MENU extends BasicUpgradableEnergyProcessorMenu<TILE, RECIPE>, TILE extends BasicUpgradableEnergyProcessorEntity<RECIPE, TILE>, RECIPE extends BasicRecipe> extends BasicUpgradableProcessorScreen<MENU, TILE, EnergyProcessorUpdatePacket, RECIPE> {
        public BasicUpgradableEnergyProcessorScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicEnergyProcessorDynScreen extends BasicEnergyProcessorScreen<BasicEnergyProcessorMenuDyn, BasicEnergyProcessorEntityDyn, ProcessorRecipeDyn> {
        public BasicEnergyProcessorDynScreen(BasicEnergyProcessorMenuDyn menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class BasicUpgradableEnergyProcessorDynScreen extends BasicUpgradableEnergyProcessorScreen<BasicUpgradableEnergyProcessorMenuDyn, BasicUpgradableEnergyProcessorEntityDyn, ProcessorRecipeDyn> {
        public BasicUpgradableEnergyProcessorDynScreen(BasicUpgradableEnergyProcessorMenuDyn menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class ManufactoryScreen extends BasicUpgradableEnergyProcessorScreen<ManufactoryMenu, ManufactoryEntity, ProcessorRecipe> {
        public ManufactoryScreen(ManufactoryMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "manufactory"));
        }
    }

    public static class SeparatorScreen extends BasicUpgradableEnergyProcessorScreen<SeparatorMenu, SeparatorEntity, ProcessorRecipe> {
        public SeparatorScreen(SeparatorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "separator"));
        }
    }

    public static class DecayHastenerScreen extends BasicUpgradableEnergyProcessorScreen<DecayHastenerMenu, DecayHastenerEntity, ProcessorRecipe> {
        public DecayHastenerScreen(DecayHastenerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "decay_hastener"));
        }
    }

    public static class FuelReprocessorScreen extends BasicUpgradableEnergyProcessorScreen<FuelReprocessorMenu, FuelReprocessorEntity, ProcessorRecipe> {
        public FuelReprocessorScreen(FuelReprocessorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fuel_reprocessor"));
        }
    }

    public static class AlloyFurnaceScreen extends BasicUpgradableEnergyProcessorScreen<AlloyFurnaceMenu, AlloyFurnaceEntity, ProcessorRecipe> {
        public AlloyFurnaceScreen(AlloyFurnaceMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "alloy_furnace"));
        }
    }

    public static class InfuserScreen extends BasicUpgradableEnergyProcessorScreen<InfuserMenu, InfuserEntity, ProcessorRecipe> {
        public InfuserScreen(InfuserMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_infuser"));
        }
    }

    public static class MelterScreen extends BasicUpgradableEnergyProcessorScreen<MelterMenu, MelterEntity, ProcessorRecipe> {
        public MelterScreen(MelterMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "melter"));
        }
    }

    public static class SupercoolerScreen extends BasicUpgradableEnergyProcessorScreen<SupercoolerMenu, SupercoolerEntity, ProcessorRecipe> {
        public SupercoolerScreen(SupercoolerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "supercooler"));
        }
    }

    public static class ElectrolyzerScreen extends BasicUpgradableEnergyProcessorScreen<ElectrolyzerMenu, ElectrolyzerEntity, ProcessorRecipe> {
        public ElectrolyzerScreen(ElectrolyzerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "electrolyzer"));
        }
    }

    public static class AssemblerScreen extends BasicUpgradableEnergyProcessorScreen<AssemblerMenu, AssemblerEntity, ProcessorRecipe> {
        public AssemblerScreen(AssemblerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "assembler"));
        }
    }

    public static class IngotFormerScreen extends BasicUpgradableEnergyProcessorScreen<IngotFormerMenu, IngotFormerEntity, ProcessorRecipe> {
        public IngotFormerScreen(IngotFormerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "ingot_former"));
        }
    }

    public static class PressurizerScreen extends BasicUpgradableEnergyProcessorScreen<PressurizerMenu, PressurizerEntity, ProcessorRecipe> {
        public PressurizerScreen(PressurizerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "pressurizer"));
        }
    }

    public static class ChemicalReactorScreen extends BasicUpgradableEnergyProcessorScreen<ChemicalReactorMenu, ChemicalReactorEntity, ProcessorRecipe> {
        public ChemicalReactorScreen(ChemicalReactorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "chemical_reactor"));
        }
    }

    public static class MixerScreen extends BasicUpgradableEnergyProcessorScreen<SaltMixerMenu, SaltMixerEntity, ProcessorRecipe> {
        public MixerScreen(SaltMixerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_mixer"));
        }
    }

    public static class CrystallizerScreen extends BasicUpgradableEnergyProcessorScreen<CrystallizerMenu, CrystallizerEntity, ProcessorRecipe> {
        public CrystallizerScreen(CrystallizerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "crystallizer"));
        }
    }

    public static class EnricherScreen extends BasicUpgradableEnergyProcessorScreen<EnricherMenu, EnricherEntity, ProcessorRecipe> {
        public EnricherScreen(EnricherMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_enricher"));
        }
    }

    public static class ExtractorScreen extends BasicUpgradableEnergyProcessorScreen<ExtractorMenu, ExtractorEntity, ProcessorRecipe> {
        public ExtractorScreen(ExtractorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_extractor"));
        }
    }

    public static class CentrifugeScreen extends BasicUpgradableEnergyProcessorScreen<CentrifugeMenu, CentrifugeEntity, ProcessorRecipe> {
        public CentrifugeScreen(CentrifugeMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "centrifuge"));
        }
    }

    public static class RockCrusherScreen extends BasicUpgradableEnergyProcessorScreen<RockCrusherMenu, RockCrusherEntity, ProcessorRecipe> {
        public RockCrusherScreen(RockCrusherMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "rock_crusher"));
        }
    }

    public static class ElectricFurnaceScreen extends BasicUpgradableEnergyProcessorScreen<ElectricFurnaceMenu, ElectricFurnaceEntity, ProcessorRecipe> {
        public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "electric_furnace"));
        }
    }

    public static class RadiationScrubberScreen extends BasicEnergyProcessorScreen<RadiationScrubberMenu, RadiationScrubberEntity, RadiationScrubberRecipe> {
        public RadiationScrubberScreen(RadiationScrubberMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "radiation_scrubber"));
        }
    }
}