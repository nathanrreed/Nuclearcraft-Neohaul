package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), NuclearcraftNeohaul.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        ores();
        tooltips();
        menus();
        creativeTabs();
        musicDiscs();
    }

    private void ores() {
        for (String ore : ORES) {
            add(ORE_MAP.get(ore).get(), StringUtils.capitalize(ore) + " Ore");
            add(ORE_MAP.get(ore + "_deepslate").get(), StringUtils.capitalize(ore) + " Deepslate Ore");
        }

        simpleItems(INGOTS, INGOT_MAP, " Ingot");
        simpleItems(GEMS, GEM_MAP, "");
        simpleItems(DUSTS, DUST_MAP, " Dust");
        replacesItems(GEM_DUSTS, GEM_DUST_MAP, "Crushed ", "", Map.of("boron_nitride", "Hexagonal Boron Nitride", "sulfur", "Sulfur"));
        simpleItems(RAWS, RAW_MAP, "Raw ", "");
        simpleItems(NUGGETS, NUGGET_MAP, " Nugget");
        simpleItems(ALLOYS, ALLOY_MAP, " Alloy Ingot");
        replacesItems(PARTS, PART_MAP, "", "", Map.of("du_plating", "DU Plating"));
        simpleItems(PART_BLOCKS, PART_BLOCK_MAP, "");
        replacesItems(COMPOUNDS, COMPOUND_MAP, "", "", Map.of("c_mn_blend", "Carbon-Manganese Blend"));
        simpleItems(UPGRADES, UPGRADE_MAP, " Upgrade");
        fuelTypeItems(URANIUMS, URANIUM_MAP, "Uranium-", "");
        simpleItems(FOOD_MAP, Map.of("dominos", "Domino's Special", "smore", "S'more S'mingot", "moresmore", "MoreS'more DoubleS'mingot"));
        add(PORTABLE_ENDER_CHEST.get(), "Portable Ender Chest");
        add(FOURSMORE.get(), "FourS'more QuadS'mingot");

        //Blocks
        simpleBlocks(INGOTS, INGOT_BLOCK_MAP, " Block");
        simpleBlocks(RAWS, RAW_BLOCK_MAP, "Block of Raw ", "");

        buckets(GASSES, Map.of("helium_3", "Helium-3"));
        buckets(MOLTEN, Map.of("boron_10", "Boron-10",
                "boron_11", "Boron-11",
                "lithium_6", "Lithium-6",
                "lithium_7", "Lithium-7",
                "lif", "Lithium Fluoride",
                "bef2", "Beryllium Fluoride",
                "flibe", "FLiBe Salt Mixture",
                "naoh", "Sodium Hydroxide",
                "koh", "Potassium Hydroxide",
                "bas", "Boron Arsenide"), "Molten ");
        buckets(HOT_GAS, Map.of());
        buckets(SUGAR, Map.of());
        buckets(CHOCOLATE, Map.of());
        buckets(FISSION, Map.of("strontium_90", "Strontium-90",
                "ruthenium_106", "Ruthenium-106",
                "caesium_137", "Caesium-137",
                "promethium_147", "Promethium-147",
                "europium_155", "Europium-155"));
        buckets(STEAM, Map.of());
        buckets(SALT_SOLUTION, Map.of());
        buckets(ACID, Map.of());
        buckets(FLAMMABLE, Map.of());
        buckets(HOT_COOLANT, Map.of());
        buckets(COOLANT, Map.of("nak", "Nak Alloy",
                "nak_hot", "Nak Alloy"), "Hot Eutectic ");
        buckets(CUSTOM_FLUID, Map.of("radaway", "RadAway Fluid",
                "radaway_slow", "Slow-Acting RadAway Fluid"));

        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            for (String machine : List.of("cobblestone_generator", "water_source", "nitrogen_collector")) {
                String type = level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase();
                add(COLLECTOR_MAP.get(machine + type).asItem(), capitalize(type + "_" + machine));
            }
        }

        for (String typeName : PROCESSOR_MAP.keySet()) {
            add(PROCESSOR_MAP.get(typeName).get(), capitalize(typeName));
        }
    }

    private void buckets(Map<String, Fluids> type, Map<String, String> replacers) {
        buckets(type, replacers, "");
    }

    private void buckets(Map<String, Fluids> type, Map<String, String> replacers, String prefix) {
        for (String fluid : type.keySet()) {
            String name = replacers.getOrDefault(fluid, capitalize(fluid));
            add(type.get(fluid).bucket.asItem(), prefix + name + " Bucket");
            add(type.get(fluid).type.get().getDescriptionId(), prefix + name);
            add(type.get(fluid).block.get(), prefix + name);
        }
    }

    private void tooltips() {
        add("tooltip.radiation", "Radiation: %s %sRad/t");
        add("tooltip.cobblestone_generator", "Produces %s Cobblestone/t constantly.");
        add("tooltip.collector", "Produces %s mb/t of %s constantly.");
        add("tooltip.processor.energy.stored", "§dEnergy Stored:§r %s / %s");
        add("tooltip.processor.energy.using", "§dProcess Power:§r %s/t");
        add("tooltip.processor.energy.speed", "§bSpeed Multiplier:§r x%s");
        add("tooltip.processor.energy.energy", "§bPower Multiplier:§r x%s");
        add("tooltip.processor.energy.not_required", "Does not require energy!");

        add("tooltip.alloy_furnace", "Combines base metals into alloys.");
        add("tooltip.assembler", "Combines components into a complex product.");
        add("tooltip.centrifuge", "Separates the isotopes of fluid materials.");
        add("tooltip.chemical_reactor", "Houses reactions between fluids.");
        add("tooltip.crystallizer", "Precipitates solids from solution.");
        add("tooltip.decay_hastener", "Forces radioactive materials to decay.");
        add("tooltip.electric_furnace", "Smelts items using energy.");
        add("tooltip.electrolyzer", "Splits compounds into their elements.");
        add("tooltip.fluid_enricher", "Enriches fluids with materials.");
        add("tooltip.fluid_extractor", "Draws fluids from materials.");
        add("tooltip.fuel_reprocessor", "Extracts materials from depleted fuel.");
        add("tooltip.fluid_infuser", "Enhances materials with fluids.");
        add("tooltip.ingot_former", "Forms ingots and gems from molten materials.");
        add("tooltip.manufactory", "A handy machine that has many uses.");
        add("tooltip.melter", "Melts down materials.");
        add("tooltip.pressurizer", "Processes items under immense pressure.");
        add("tooltip.rock_crusher", "Smashes up rock to produce mineral dusts.");
        add("tooltip.fluid_mixer", "Blends fluids together.");
        add("tooltip.separator", "Breaks materials into their constituents.");
        add("tooltip.supercooler", "Lowers the temperature of fluids.");

        add("tooltip.tank", "%s [%s mB / %s mB]");
        add("tooltip.tank.clear", "Shift Click to clear tank");

        add("tooltip.redstone_control", "Redstone Control");
        add("tooltip.side_config", "Side Configuration");
        add("tooltip.side_config.energy_upgrade", "Energy Upgrade Slot Configuration");
        add("tooltip.side_config.speed_upgrade", "Speed Upgrade Slot Configuration");
        add("tooltip.side_config.fluid_input", "Input Tank Configuration");
        add("tooltip.side_config.fluid_output", "Output Tank Configuration");
        add("tooltip.side_config.item_input", "Input Slot Configuration");
        add("tooltip.side_config.item_output", "Output Slot Configuration");

        add("tooltip.side_config.top", "TOP: %s");
        add("tooltip.side_config.left", "LEFT: %s");
        add("tooltip.side_config.front", "FRONT: %s");
        add("tooltip.side_config.right", "RIGHT: %s");
        add("tooltip.side_config.bottom", "BOTTOM: %s");
        add("tooltip.side_config.back", "BACK: %s");

        add("tooltip.side_config.disabled", "DISABLED");
        add("tooltip.side_config.input", "INPUT");
        add("tooltip.side_config.output", "OUTPUT");
        add("tooltip.side_config.auto_output", "AUTO-OUTPUT");

        add("tooltip.side_config.slot_setting.slot", "Slot Setting: %s");
        add("tooltip.side_config.slot_setting.tank", "Tank Setting: %s");

        add("tooltip.side_config.slot_setting.default", "DEFAULT");
        add("tooltip.side_config.slot_setting.void_excess", "VOID EXCESS");
        add("tooltip.side_config.slot_setting.void_all", "VOID ALL");

        add("tooltip.shift_for_info", "Hold Shift for more info");

        add("tooltip.portable.ender_chest", "Access your Ender Chest on the move.");
        add("tooltip.marshmallow", "Many civilizations would not have fallen if they had these on their side.");
        add("tooltip.dominos", "Paul's favourite - restore 16 hunger points with this beauty.");
    }

    private void menus() {
        add("menu.title.alloy_furnace", "Alloy Furnace");
        add("menu.title.assembler", "Assembler");
        add("menu.title.centrifuge", "Centrifuge");
        add("menu.title.chemical_reactor", "Chemical Reactor");
        add("menu.title.crystallizer", "Crystallizer");
        add("menu.title.decay_hastener", "Decay Hastener");
        add("menu.title.electric_furnace", "Electric Furnace");
        add("menu.title.electrolyzer", "Electrolyzer");
        add("menu.title.fluid_enricher", "Fluid Enricher");
        add("menu.title.fluid_extractor", "Fluid Extractor");
        add("menu.title.fuel_reprocessor", "Fuel Reprocessor");
        add("menu.title.fluid_infuser", "Fluid Infuser");
        add("menu.title.ingot_former", "Ingot Former");
        add("menu.title.manufactory", "Manufactory");
        add("menu.title.melter", "Melter");
        add("menu.title.pressurizer", "Pressurizer");
        add("menu.title.rock_crusher", "Rock Crusher");
        add("menu.title.fluid_mixer", "Fluid Mixer");
        add("menu.title.separator", "Separator");
        add("menu.title.supercooler", "Supercooler");
    }

    private void creativeTabs() {
        add("creative_tab.title.materials", "NuclearCraft Materials");
        add("creative_tab.title.machines", "NuclearCraft Machines");
        add("creative_tab.title.multiblocks", "NuclearCraft Multiblocks");
        add("creative_tab.title.radiation", "NuclearCraft Radiation");
        add("creative_tab.title.miscellaneous", "NuclearCraft Miscellaneous");
    }

    private void musicDiscs() {
        for (DeferredItem<Item> disc : MUSIC_DISC_MAP.values()) {
            add(disc.get(), "Music Disc");
        }

        add("music_disc.money_for_nothing", "Dire Straits - Money For Nothing");
        add("music_disc.money_for_nothing.credit", "8-Bit Cover by 'Omnigrad'");
        add("music_disc.wanderer", "Dion - The Wanderer");
        add("music_disc.wanderer.credit", "8-Bit Cover by '8 Bit Universe'");
        add("music_disc.end_of_the_world", "Skeeter Davis - The End of the World");
        add("music_disc.end_of_the_world.credit", "8-Bit Cover by 'GermanPikachuGaming'");
        add("music_disc.hyperspace", "Ur-Quan Masters - Hyperspace");
        add("music_disc.hyperspace.credit", "8-Bit Cover by 'Riku Nuottajärvi'");
    }

    private void fuelTypeItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String prepend, String append) {
        for (String name : list) {
            add(map.get(name).asItem(), prepend + capitalize(fuelTypes(name)) + append);
        }
    }

    private String fuelTypes(String name) {
        if (!name.contains("_")) return name;
        return name.substring(0, name.lastIndexOf('_')) + " " + switch (name.substring(name.lastIndexOf('_') + 1)) {
            case "c" -> "Carbide";
            case "ni" -> "Nitride";
            case "ox" -> "Oxide";
            case "za" -> "Zirconium Alloy";
            default -> "";
        };
    }

    private void simpleItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String append) {
        simpleItems(list, map, "", append);
    }

    private void simpleItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String prepend, String append) {
        replacesItems(list, map, prepend, append, Map.of());
    }

    private void simpleItems(HashMap<String, DeferredItem<Item>> map) {
        replacesItems(map.keySet().stream().toList(), map, "", "", Map.of());
    }

    private void simpleItems(HashMap<String, DeferredItem<Item>> map, Map<String, String> replacers) {
        replacesItems(map.keySet().stream().toList(), map, "", "", replacers);
    }

    private void replacesItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String prepend, String append, Map<String, String> replacers) {
        for (String name : list) {
            if (replacers.containsKey(name)) {
                add(map.get(name).asItem(), replacers.get(name));
            } else {
                add(map.get(name).asItem(), prepend + capitalize(name) + append);
            }
        }
    }

    private void simpleBlocks(List<String> list, HashMap<String, DeferredBlock<Block>> map, String append) {
        simpleBlocks(list, map, "", append);
    }

    private void simpleBlocks(List<String> list, HashMap<String, DeferredBlock<Block>> map, String prepend, String append) {
        for (String name : list) {
            add(map.get(name).asItem(), prepend + capitalize(name) + append);
        }
    }

    private String capitalize(String input) {
        return String.join(" ", Arrays.stream(input.split("_")).map(StringUtils::capitalize).toList()).trim();
    }
}