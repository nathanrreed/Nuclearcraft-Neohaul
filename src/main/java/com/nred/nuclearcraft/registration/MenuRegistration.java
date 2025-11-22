package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.menu.multiblock.controller.*;
import com.nred.nuclearcraft.menu.multiblock.port.*;
import com.nred.nuclearcraft.menu.processor.NuclearFurnaceMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.*;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;

import static com.nred.nuclearcraft.registration.Registers.MENUS;

public class MenuRegistration {
    public static final HashMap<String, DeferredHolder<MenuType<?>, MenuType<? extends ProcessorMenu<?, ?, ?>>>> PROCESSOR_MENU_TYPES = createProcessors();

    // Controllers
    public static final DeferredHolder<MenuType<?>, MenuType<SaltFissionControllerMenu>> SALT_FISSION_CONTROLLER_MENU_TYPE = MENUS.register("salt_fission_controller", () -> IMenuTypeExtension.create(SaltFissionControllerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<SolidFissionControllerMenu>> SOLID_FISSION_CONTROLLER_MENU_TYPE = MENUS.register("solid_fission_controller", () -> IMenuTypeExtension.create(SolidFissionControllerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<TurbineControllerMenu>> TURBINE_CONTROLLER_MENU_TYPE = MENUS.register("turbine_controller", () -> IMenuTypeExtension.create(TurbineControllerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<HeatExchangerControllerMenu>> HX_CONTROLLER_MENU_TYPE = MENUS.register("hx_controller", () -> IMenuTypeExtension.create(HeatExchangerControllerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CondenserControllerMenu>> CONDENSER_CONTROLLER_MENU_TYPE = MENUS.register("condenser_controller", () -> IMenuTypeExtension.create(CondenserControllerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DistillerControllerMenu>> DISTILLER_CONTROLLER_MENU_TYPE = MENUS.register("distiller_controller", () -> IMenuTypeExtension.create(DistillerControllerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ElectrolyzerControllerMenu>> ELECTROLYZER_CONTROLLER_MENU_TYPE = MENUS.register("electrolyzer_controller", () -> IMenuTypeExtension.create(ElectrolyzerControllerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<InfiltratorControllerMenu>> INFILTRATOR_CONTROLLER_MENU_TYPE = MENUS.register("infiltrator_controller", () -> IMenuTypeExtension.create(InfiltratorControllerMenu::new));

    // Ports
    public static final DeferredHolder<MenuType<?>, MenuType<FissionCellPortMenu>> FISSION_CELL_PORT_MENU_TYPE = MENUS.register("turbine_cell", () -> IMenuTypeExtension.create(FissionCellPortMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FissionCoolerPortMenu>> FISSION_COOLER_PORT_MENU_TYPE = MENUS.register("fission_cooler_port", () -> IMenuTypeExtension.create(FissionCoolerPortMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FissionHeaterPortMenu>> FISSION_HEATER_PORT_MENU_TYPE = MENUS.register("fission_heater_port", () -> IMenuTypeExtension.create(FissionHeaterPortMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FissionIrradiatorPortMenu>> FISSION_IRRADIATOR_PORT_MENU_TYPE = MENUS.register("fission_irradiator_port", () -> IMenuTypeExtension.create(FissionIrradiatorPortMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FissionVesselPortMenu>> FISSION_VESSEL_PORT_MENU_TYPE = MENUS.register("fission_vessel_port", () -> IMenuTypeExtension.create(FissionVesselPortMenu::new));

    // Parts
    public static final DeferredHolder<MenuType<?>, MenuType<FissionIrradiatorMenu>> FISSION_IRRADIATOR_MENU_TYPE = MENUS.register("fission_irradiator", () -> IMenuTypeExtension.create(FissionIrradiatorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FissionCoolerMenu>> FISSION_COOLER_MENU_TYPE = MENUS.register("fission_cooler", () -> IMenuTypeExtension.create(FissionCoolerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<SolidFissionCellMenu>> FISSION_SOLID_CELL_MENU_TYPE = MENUS.register("fission_cell", () -> IMenuTypeExtension.create(SolidFissionCellMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<SaltFissionVesselMenu>> FISSION_SALT_VESSEL_MENU_TYPE = MENUS.register("fission_vessel", () -> IMenuTypeExtension.create(SaltFissionVesselMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<SaltFissionHeaterMenu>> FISSION_SALT_HEATER_MENU_TYPE = MENUS.register("fission_heater", () -> IMenuTypeExtension.create(SaltFissionHeaterMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<NuclearFurnaceMenu>> NUCLEAR_FURNACE_MENU_TYPE = MENUS.register("nuclear_furnace", () -> IMenuTypeExtension.create(NuclearFurnaceMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<RadiationScrubberMenu>> RADIATION_SCRUBBER_MENU_TYPE = MENUS.register("radiation_scrubber", () -> IMenuTypeExtension.create(RadiationScrubberMenu::new));

    private static HashMap<String, DeferredHolder<MenuType<?>, MenuType<? extends ProcessorMenu<?, ?, ?>>>> createProcessors() {
        HashMap<String, DeferredHolder<MenuType<?>, MenuType<? extends ProcessorMenu<?, ?, ?>>>> map = new HashMap<>();
        map.put("alloy_furnace", MENUS.register("alloy_furnace", () -> IMenuTypeExtension.create(AlloyFurnaceMenu::new)));
        map.put("assembler", MENUS.register("assembler", () -> IMenuTypeExtension.create(AssemblerMenu::new)));
        map.put("centrifuge", MENUS.register("centrifuge", () -> IMenuTypeExtension.create(CentrifugeMenu::new)));
        map.put("chemical_reactor", MENUS.register("chemical_reactor", () -> IMenuTypeExtension.create(ChemicalReactorMenu::new)));
        map.put("crystallizer", MENUS.register("crystallizer", () -> IMenuTypeExtension.create(CrystallizerMenu::new)));
        map.put("decay_hastener", MENUS.register("decay_hastener", () -> IMenuTypeExtension.create(DecayHastenerMenu::new)));
        map.put("electric_furnace", MENUS.register("electric_furnace", () -> IMenuTypeExtension.create(ElectricFurnaceMenu::new)));
        map.put("electrolyzer", MENUS.register("electrolyzer", () -> IMenuTypeExtension.create(ElectrolyzerMenu::new)));
        map.put("fluid_enricher", MENUS.register("fluid_enricher", () -> IMenuTypeExtension.create(EnricherMenu::new)));
        map.put("fluid_extractor", MENUS.register("fluid_extractor", () -> IMenuTypeExtension.create(ExtractorMenu::new)));
        map.put("fuel_reprocessor", MENUS.register("fuel_reprocessor", () -> IMenuTypeExtension.create(FuelReprocessorMenu::new)));
        map.put("fluid_infuser", MENUS.register("fluid_infuser", () -> IMenuTypeExtension.create(InfuserMenu::new)));
        map.put("ingot_former", MENUS.register("ingot_former", () -> IMenuTypeExtension.create(IngotFormerMenu::new)));
        map.put("manufactory", MENUS.register("manufactory", () -> IMenuTypeExtension.create(ManufactoryMenu::new)));
        map.put("melter", MENUS.register("melter", () -> IMenuTypeExtension.create(MelterMenu::new)));
        map.put("pressurizer", MENUS.register("pressurizer", () -> IMenuTypeExtension.create(PressurizerMenu::new)));
        map.put("rock_crusher", MENUS.register("rock_crusher", () -> IMenuTypeExtension.create(RockCrusherMenu::new)));
        map.put("fluid_mixer", MENUS.register("fluid_mixer", () -> IMenuTypeExtension.create(SaltMixerMenu::new)));
        map.put("separator", MENUS.register("separator", () -> IMenuTypeExtension.create(SeparatorMenu::new)));
        map.put("supercooler", MENUS.register("supercooler", () -> IMenuTypeExtension.create(SupercoolerMenu::new)));
        return map;
    }

    public static void init() {
    }
}