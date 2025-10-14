package com.nred.nuclearcraft.menu;

import com.nred.nuclearcraft.block.fission.*;
import com.nred.nuclearcraft.block.info.ProcessorContainerInfoImpl;
import com.nred.nuclearcraft.block.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block.processor.IBasicProcessor;
import com.nred.nuclearcraft.payload.multiblock.*;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.nred.nuclearcraft.registration.MenuRegistration.*;

public class ContainerProcessorImpl {
    public static class ContainerBasicProcessor<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ContainerProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, PACKET>> {
        public ContainerBasicProcessor(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
            super(menuType, containerId, inventory, tile);
        }
    }

//	public static class ContainerBasicUpgradableProcessor<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ContainerUpgradableProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicUpgradableProcessorContainerInfo<TILE, PACKET>> {
//		public ContainerBasicUpgradableProcessor(Player player, TILE tile) {
//			super(player, tile);
//		}
//	}

    public static class ContainerBasicFilteredProcessor<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET> & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket> extends ContainerFilteredProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, PACKET>> {
        public ContainerBasicFilteredProcessor(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
            super(menuType, containerId, inventory, tile);
        }
    }
//
//	public static class ContainerBasicEnergyProcessor<TILE extends TileBasicEnergyProcessor<TILE>> extends ContainerBasicProcessor<TILE, EnergyProcessorUpdatePacket> {
//		public ContainerBasicEnergyProcessor(Player player, TILE tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerBasicUpgradableEnergyProcessor<TILE extends TileBasicUpgradableEnergyProcessor<TILE>> extends ContainerBasicUpgradableProcessor<TILE, EnergyProcessorUpdatePacket> {
//		public ContainerBasicUpgradableEnergyProcessor(Player player, TILE tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerBasicEnergyProcessorDyn extends ContainerBasicEnergyProcessor<TileBasicEnergyProcessorDyn> {
//		public ContainerBasicEnergyProcessorDyn(Player player, TileBasicEnergyProcessorDyn tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerBasicUpgradableEnergyProcessorDyn extends ContainerBasicUpgradableEnergyProcessor<TileBasicUpgradableEnergyProcessorDyn> {
//		public ContainerBasicUpgradableEnergyProcessorDyn(Player player, TileBasicUpgradableEnergyProcessorDyn tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerManufactory extends ContainerBasicUpgradableEnergyProcessor<TileManufactory> {
//		public ContainerManufactory(Player player, TileManufactory tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerSeparator extends ContainerBasicUpgradableEnergyProcessor<TileSeparator> {
//		public ContainerSeparator(Player player, TileSeparator tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerDecayHastener extends ContainerBasicUpgradableEnergyProcessor<TileDecayHastener> {
//		public ContainerDecayHastener(Player player, TileDecayHastener tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerFuelReprocessor extends ContainerBasicUpgradableEnergyProcessor<TileFuelReprocessor> {
//		public ContainerFuelReprocessor(Player player, TileFuelReprocessor tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerAlloyFurnace extends ContainerBasicUpgradableEnergyProcessor<TileAlloyFurnace> {
//		public ContainerAlloyFurnace(Player player, TileAlloyFurnace tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerInfuser extends ContainerBasicUpgradableEnergyProcessor<TileInfuser> {
//		public ContainerInfuser(Player player, TileInfuser tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerMelter extends ContainerBasicUpgradableEnergyProcessor<TileMelter> {
//		public ContainerMelter(Player player, TileMelter tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerSupercooler extends ContainerBasicUpgradableEnergyProcessor<TileSupercooler> {
//		public ContainerSupercooler(Player player, TileSupercooler tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerElectrolyzer extends ContainerBasicUpgradableEnergyProcessor<TileElectrolyzer> {
//		public ContainerElectrolyzer(Player player, TileElectrolyzer tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerAssembler extends ContainerBasicUpgradableEnergyProcessor<TileAssembler> {
//		public ContainerAssembler(Player player, TileAssembler tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerIngotFormer extends ContainerBasicUpgradableEnergyProcessor<TileIngotFormer> {
//		public ContainerIngotFormer(Player player, TileIngotFormer tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerPressurizer extends ContainerBasicUpgradableEnergyProcessor<TilePressurizer> {
//		public ContainerPressurizer(Player player, TilePressurizer tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerChemicalReactor extends ContainerBasicUpgradableEnergyProcessor<TileChemicalReactor> {
//		public ContainerChemicalReactor(Player player, TileChemicalReactor tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerSaltMixer extends ContainerBasicUpgradableEnergyProcessor<TileSaltMixer> {
//		public ContainerSaltMixer(Player player, TileSaltMixer tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerCrystallizer extends ContainerBasicUpgradableEnergyProcessor<TileCrystallizer> {
//		public ContainerCrystallizer(Player player, TileCrystallizer tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerEnricher extends ContainerBasicUpgradableEnergyProcessor<TileEnricher> {
//		public ContainerEnricher(Player player, TileEnricher tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerExtractor extends ContainerBasicUpgradableEnergyProcessor<TileExtractor> {
//		public ContainerExtractor(Player player, TileExtractor tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerCentrifuge extends ContainerBasicUpgradableEnergyProcessor<TileCentrifuge> {
//		public ContainerCentrifuge(Player player, TileCentrifuge tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerRockCrusher extends ContainerBasicUpgradableEnergyProcessor<TileRockCrusher> {
//		public ContainerRockCrusher(Player player, TileRockCrusher tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerElectricFurnace extends ContainerBasicUpgradableEnergyProcessor<TileElectricFurnace> {
//		public ContainerElectricFurnace(Player player, TileElectricFurnace tile) {
//			super(player, tile);
//		}
//	}
//
//	public static class ContainerRadiationScrubber extends ContainerBasicEnergyProcessor<TileRadiationScrubber> {
//		public ContainerRadiationScrubber(Player player, TileRadiationScrubber tile) {
//			super(player, tile);
//		}
//	}

    public static class FissionIrradiatorMenu extends ContainerBasicFilteredProcessor<FissionIrradiatorEntity, FissionIrradiatorUpdatePacket> {
        public FissionIrradiatorMenu(int containerId, Inventory inventory, FissionIrradiatorEntity irradiator) {
            super(FISSION_IRRADIATOR_MENU_TYPE.get(), containerId, inventory, irradiator);
        }

        // Client Constructor
        public FissionIrradiatorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }

    public static class FissionCoolerMenu extends ContainerBasicProcessor<FissionCoolerEntity, FissionCoolerUpdatePacket> {
        public FissionCoolerMenu(int containerId, Inventory inventory, FissionCoolerEntity cooler) {
            super(FISSION_COOLER_MENU_TYPE.get(), containerId, inventory, cooler);
        }

        // Client Constructor
        public FissionCoolerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }

    public static class SolidFissionCellMenu extends ContainerBasicFilteredProcessor<SolidFissionCellEntity, SolidFissionCellUpdatePacket> {
        public SolidFissionCellMenu(int containerId, Inventory inventory, SolidFissionCellEntity cell) {
            super(FISSION_SOLID_CELL_MENU_TYPE.get(), containerId, inventory, cell);
        }

        // Client Constructor
        public SolidFissionCellMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }

    public static class SaltFissionVesselMenu extends ContainerBasicProcessor<SaltFissionVesselEntity, SaltFissionVesselUpdatePacket> {
        public SaltFissionVesselMenu(int containerId, Inventory inventory, SaltFissionVesselEntity vessel) {
            super(FISSION_SALT_VESSEL_MENU_TYPE.get(), containerId, inventory, vessel);
        }

        // Client Constructor
        public SaltFissionVesselMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }

    public static class SaltFissionHeaterMenu extends ContainerBasicProcessor<SaltFissionHeaterEntity, SaltFissionHeaterUpdatePacket> {
        public SaltFissionHeaterMenu(int containerId, Inventory inventory, SaltFissionHeaterEntity heater) {
            super(FISSION_SALT_HEATER_MENU_TYPE.get(), containerId, inventory, heater);
        }

        // Client Constructor
        public SaltFissionHeaterMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
            this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
        }
    }
}