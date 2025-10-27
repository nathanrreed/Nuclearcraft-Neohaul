package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.block_entity.ITileFiltered;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.TileProcessorImpl.*;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.*;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class GuiProcessorImpl {

    public static class GuiBasicProcessor<MENU extends BasicProcessorMenu<TILE, PACKET>, TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends GuiProcessor<MENU, TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET>> {
        public GuiBasicProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class GuiBasicUpgradableProcessor<MENU extends BasicUpgradableProcessorMenu<TILE, PACKET>, TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends GuiUpgradableProcessor<MENU, TILE, PACKET, BasicUpgradableProcessorMenuInfo<TILE, PACKET>> {
        public GuiBasicUpgradableProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class GuiBasicFilteredProcessor<MENU extends BasicFilteredProcessorMenu<TILE, PACKET>, TILE extends BlockEntity & IBasicProcessor<TILE, PACKET> & ITileFiltered & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket> extends GuiFilteredProcessor<MENU, TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET>> {
        public GuiBasicFilteredProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class GuiBasicEnergyProcessor<MENU extends BasicEnergyProcessorMenu<TILE>, TILE extends BasicEnergyProcessorEntity<TILE>> extends GuiBasicProcessor<MENU, TILE, EnergyProcessorUpdatePacket> {
        public GuiBasicEnergyProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class GuiBasicUpgradableEnergyProcessor<MENU extends BasicUpgradableEnergyProcessorMenu<TILE>, TILE extends BasicUpgradableEnergyProcessorEntity<TILE>> extends GuiBasicUpgradableProcessor<MENU, TILE, EnergyProcessorUpdatePacket> {
        public GuiBasicUpgradableEnergyProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class GuiBasicEnergyProcessorDyn<MENU extends BasicEnergyProcessorMenuDyn> extends GuiBasicEnergyProcessor<MENU, BasicEnergyProcessorEntityDyn> {
        public GuiBasicEnergyProcessorDyn(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class GuiBasicUpgradableEnergyProcessorDyn<MENU extends BasicUpgradableEnergyProcessorMenuDyn> extends GuiBasicUpgradableEnergyProcessor<MENU, BasicUpgradableEnergyProcessorEntityDyn> {
        public GuiBasicUpgradableEnergyProcessorDyn(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
        }
    }

    public static class GuiManufactory extends GuiBasicUpgradableEnergyProcessor<ManufactoryMenu, ManufactoryEntity> {
        public GuiManufactory(ManufactoryMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "manufactory"));
        }
    }

    public static class GuiSeparator extends GuiBasicUpgradableEnergyProcessor<SeparatorMenu, SeparatorEntity> {
        public GuiSeparator(SeparatorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "separator"));
        }
    }

    public static class GuiDecayHastener extends GuiBasicUpgradableEnergyProcessor<DecayHastenerMenu, DecayHastenerEntity> {
        public GuiDecayHastener(DecayHastenerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "decay_hastener"));
        }
    }

    public static class GuiFuelReprocessor extends GuiBasicUpgradableEnergyProcessor<FuelReprocessorMenu, FuelReprocessorEntity> {
        public GuiFuelReprocessor(FuelReprocessorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fuel_reprocessor"));
        }
    }

    public static class GuiAlloyFurnace extends GuiBasicUpgradableEnergyProcessor<AlloyFurnaceMenu, AlloyFurnaceEntity> {
        public GuiAlloyFurnace(AlloyFurnaceMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "alloy_furnace"));
        }
    }

    public static class GuiInfuser extends GuiBasicUpgradableEnergyProcessor<InfuserMenu, InfuserEntity> {
        public GuiInfuser(InfuserMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_infuser"));
        }
    }

    public static class GuiMelter extends GuiBasicUpgradableEnergyProcessor<MelterMenu, MelterEntity> {
        public GuiMelter(MelterMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "melter"));
        }
    }

    public static class GuiSupercooler extends GuiBasicUpgradableEnergyProcessor<SupercoolerMenu, SupercoolerEntity> {
        public GuiSupercooler(SupercoolerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "supercooler"));
        }
    }

    public static class GuiElectrolyzer extends GuiBasicUpgradableEnergyProcessor<ElectrolyzerMenu, ElectrolyzerEntity> {
        public GuiElectrolyzer(ElectrolyzerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "electrolyzer"));
        }
    }

    public static class GuiAssembler extends GuiBasicUpgradableEnergyProcessor<AssemblerMenu, AssemblerEntity> {
        public GuiAssembler(AssemblerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "assembler"));
        }
    }

    public static class GuiIngotFormer extends GuiBasicUpgradableEnergyProcessor<IngotFormerMenu, IngotFormerEntity> {
        public GuiIngotFormer(IngotFormerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "ingot_former"));
        }
    }

    public static class GuiPressurizer extends GuiBasicUpgradableEnergyProcessor<PressurizerMenu, PressurizerEntity> {
        public GuiPressurizer(PressurizerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "pressurizer"));
        }
    }

    public static class GuiChemicalReactor extends GuiBasicUpgradableEnergyProcessor<ChemicalReactorMenu, ChemicalReactorEntity> {
        public GuiChemicalReactor(ChemicalReactorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "chemical_reactor"));
        }
    }

    public static class GuiSaltMixer extends GuiBasicUpgradableEnergyProcessor<SaltMixerMenu, SaltMixerEntity> {
        public GuiSaltMixer(SaltMixerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_mixer"));
        }
    }

    public static class GuiCrystallizer extends GuiBasicUpgradableEnergyProcessor<CrystallizerMenu, CrystallizerEntity> {
        public GuiCrystallizer(CrystallizerMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "crystallizer"));
        }
    }

    public static class GuiEnricher extends GuiBasicUpgradableEnergyProcessor<EnricherMenu, EnricherEntity> {
        public GuiEnricher(EnricherMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_enricher"));
        }
    }

    public static class GuiExtractor extends GuiBasicUpgradableEnergyProcessor<ExtractorMenu, ExtractorEntity> {
        public GuiExtractor(ExtractorMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "fluid_extractor"));
        }
    }

    public static class GuiCentrifuge extends GuiBasicUpgradableEnergyProcessor<CentrifugeMenu, CentrifugeEntity> {
        public GuiCentrifuge(CentrifugeMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "centrifuge"));
        }
    }

    public static class GuiRockCrusher extends GuiBasicUpgradableEnergyProcessor<RockCrusherMenu, RockCrusherEntity> {
        public GuiRockCrusher(RockCrusherMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "rock_crusher"));
        }
    }

    public static class GuiElectricFurnace extends GuiBasicUpgradableEnergyProcessor<ElectricFurnaceMenu, ElectricFurnaceEntity> {
        public GuiElectricFurnace(ElectricFurnaceMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, ncLoc("screen/" + "electric_furnace"));
        }
    }

//    public static class GuiRadiationScrubber extends GuiBasicEnergyProcessor<RadiationScrubberMenu, RadiationScrubberEntity> { TODO
//        public GuiRadiationScrubber(RadiationScrubberMenu menu, Inventory inventory, Component title) {
//            super(menu, inventory, title, ncLoc("screen/" + "radiation_scrubber"));
//        }
//    }
}
