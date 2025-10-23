package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.block_entity.processor.info.ProcessorContainerInfoImpl;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.menu.ContainerProcessorImpl;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GuiProcessorImpl {
    public static class GuiBasicProcessor<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket, MENU extends ContainerProcessorImpl.ContainerBasicProcessor<TILE, PACKET>> extends GuiProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, PACKET>, MENU> {
        public GuiBasicProcessor(MENU menu, Inventory playerInventory, Component title, ResourceLocation gui_texture) {
            super(menu, playerInventory, title, gui_texture);
        }
    }

//    public static class GuiBasicUpgradableProcessor<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends GuiUpgradableProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicUpgradableProcessorContainerInfo<TILE, PACKET>> {
//        public GuiBasicUpgradableProcessor(MENU menu, Inventory playerInventory, Component title) { TODO
//            super(inventory, player, tile, textureLocation);
//        }
//    }

    public static class GuiBasicFilteredProcessor<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET> & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket, MENU extends ContainerProcessorImpl.ContainerBasicFilteredProcessor<TILE, PACKET>> extends GuiFilteredProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, PACKET>, MENU> {
        public GuiBasicFilteredProcessor(MENU menu, Inventory playerInventory, Component title, ResourceLocation gui_texture) {
            super(menu, playerInventory, title, gui_texture);
        }
    }

//    TODO
//    public static class GuiBasicEnergyProcessor<TILE extends TileBasicEnergyProcessor<TILE>> extends GuiBasicProcessor<TILE, EnergyProcessorUpdatePacket> {
//        public GuiBasicEnergyProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
//            super(inventory, player, tile, textureLocation);
//        }
//    }
//
//    public static class GuiBasicUpgradableEnergyProcessor<TILE extends TileBasicUpgradableEnergyProcessor<TILE>> extends GuiBasicUpgradableProcessor<TILE, EnergyProcessorUpdatePacket> {
//        public GuiBasicUpgradableEnergyProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
//            super(inventory, player, tile, textureLocation);
//        }
//    }
//
//    public static class GuiBasicEnergyProcessorDyn extends GuiBasicEnergyProcessor<TileBasicEnergyProcessorDyn> {
//        public GuiBasicEnergyProcessorDyn(Container inventory, EntityPlayer player, TileBasicEnergyProcessorDyn tile, String textureLocation) {
//            super(inventory, player, tile, textureLocation);
//        }
//    }
//
//    public static class GuiBasicUpgradableEnergyProcessorDyn extends GuiBasicUpgradableEnergyProcessor<TileBasicUpgradableEnergyProcessorDyn> {
//        public GuiBasicUpgradableEnergyProcessorDyn(Container inventory, EntityPlayer player, TileBasicUpgradableEnergyProcessorDyn tile, String textureLocation) {
//            super(inventory, player, tile, textureLocation);
//        }
//    }
//
//
//	public static class GuiManufactory extends GuiBasicUpgradableEnergyProcessor<TileManufactory> {
//		public GuiManufactory(Container inventory, EntityPlayer player, TileManufactory tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiSeparator extends GuiBasicUpgradableEnergyProcessor<TileSeparator> {
//		public GuiSeparator(Container inventory, EntityPlayer player, TileSeparator tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiDecayHastener extends GuiBasicUpgradableEnergyProcessor<TileDecayHastener> {
//		public GuiDecayHastener(Container inventory, EntityPlayer player, TileDecayHastener tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiFuelReprocessor extends GuiBasicUpgradableEnergyProcessor<TileFuelReprocessor> {
//		public GuiFuelReprocessor(Container inventory, EntityPlayer player, TileFuelReprocessor tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiAlloyFurnace extends GuiBasicUpgradableEnergyProcessor<TileAlloyFurnace> {
//		public GuiAlloyFurnace(Container inventory, EntityPlayer player, TileAlloyFurnace tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiInfuser extends GuiBasicUpgradableEnergyProcessor<TileInfuser> {
//		public GuiInfuser(Container inventory, EntityPlayer player, TileInfuser tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiMelter extends GuiBasicUpgradableEnergyProcessor<TileMelter> {
//		public GuiMelter(Container inventory, EntityPlayer player, TileMelter tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiSupercooler extends GuiBasicUpgradableEnergyProcessor<TileSupercooler> {
//		public GuiSupercooler(Container inventory, EntityPlayer player, TileSupercooler tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiElectrolyzer extends GuiBasicUpgradableEnergyProcessor<TileElectrolyzer> {
//		public GuiElectrolyzer(Container inventory, EntityPlayer player, TileElectrolyzer tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiAssembler extends GuiBasicUpgradableEnergyProcessor<TileAssembler> {
//		public GuiAssembler(Container inventory, EntityPlayer player, TileAssembler tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiIngotFormer extends GuiBasicUpgradableEnergyProcessor<TileIngotFormer> {
//		public GuiIngotFormer(Container inventory, EntityPlayer player, TileIngotFormer tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiPressurizer extends GuiBasicUpgradableEnergyProcessor<TilePressurizer> {
//		public GuiPressurizer(Container inventory, EntityPlayer player, TilePressurizer tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiChemicalReactor extends GuiBasicUpgradableEnergyProcessor<TileChemicalReactor> {
//		public GuiChemicalReactor(Container inventory, EntityPlayer player, TileChemicalReactor tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiSaltMixer extends GuiBasicUpgradableEnergyProcessor<TileSaltMixer> {
//		public GuiSaltMixer(Container inventory, EntityPlayer player, TileSaltMixer tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiCrystallizer extends GuiBasicUpgradableEnergyProcessor<TileCrystallizer> {
//		public GuiCrystallizer(Container inventory, EntityPlayer player, TileCrystallizer tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiEnricher extends GuiBasicUpgradableEnergyProcessor<TileEnricher> {
//		public GuiEnricher(Container inventory, EntityPlayer player, TileEnricher tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiExtractor extends GuiBasicUpgradableEnergyProcessor<TileExtractor> {
//		public GuiExtractor(Container inventory, EntityPlayer player, TileExtractor tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiCentrifuge extends GuiBasicUpgradableEnergyProcessor<TileCentrifuge> {
//		public GuiCentrifuge(Container inventory, EntityPlayer player, TileCentrifuge tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiRockCrusher extends GuiBasicUpgradableEnergyProcessor<TileRockCrusher> {
//		public GuiRockCrusher(Container inventory, EntityPlayer player, TileRockCrusher tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiElectricFurnace extends GuiBasicUpgradableEnergyProcessor<TileElectricFurnace> {
//		public GuiElectricFurnace(Container inventory, EntityPlayer player, TileElectricFurnace tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
//
//	public static class GuiRadiationScrubber extends GuiBasicEnergyProcessor<TileRadiationScrubber> {
//		public GuiRadiationScrubber(Container inventory, EntityPlayer player, TileRadiationScrubber tile, String textureLocation) {
//			super(inventory, player, tile, textureLocation);
//		}
//	}
}