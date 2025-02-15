package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.menu.*;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;

import static com.nred.nuclearcraft.registration.Registers.MENUS;

public class MenuRegistration {
    public static final HashMap<String, DeferredHolder<MenuType<?>, MenuType<? extends ProcessorMenu>>> PROCESSOR_MENU_TYPES = createProcessors();

    private static HashMap<String, DeferredHolder<MenuType<?>, MenuType<? extends ProcessorMenu>>> createProcessors() {
        HashMap<String, DeferredHolder<MenuType<?>, MenuType<? extends ProcessorMenu>>> map = new HashMap<>();
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