package com.nred.nuclearcraft.datagen;

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
import java.util.regex.Pattern;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), MODID, locale);
    }

    @Override
    protected void addTranslations() {
        ores();
        items();
        blocks();
        buckets();
        tooltips();
        menus();
        creativeTabs();
        musicDiscs();
        damage_types();

        add(SUPERCOLD_ICE.asItem(), capitalize(SUPERCOLD_ICE.getId().getPath()));
        add(UNIVERSAL_BIN.asItem(), capitalize(UNIVERSAL_BIN.getId().getPath()));
        add(SOLIDIFIED_CORIUM.asItem(), capitalize(SOLIDIFIED_CORIUM.getId().getPath()));
        add(LITHIUM_ION_CELL.asItem(), capitalize(LITHIUM_ION_CELL.getId().getPath()));
        add("jei.probability", "Production chance: %s%%");
    }

    private void ores() {
        for (String ore : ORES) {
            add(ORE_MAP.get(ore).get(), StringUtils.capitalize(ore) + " Ore");
            add(ORE_MAP.get(ore + "_deepslate").get(), StringUtils.capitalize(ore) + " Deepslate Ore");
        }
    }

    private void items() {
        simpleItems(INGOTS, INGOT_MAP, " Ingot");
        simpleItems(GEMS, GEM_MAP, "");
        simpleItems(DUSTS, DUST_MAP, " Dust");
        replaceItems(FISSION_DUSTS, FISSION_DUST_MAP, "", " Dust", Map.of("tbp", "Protactinium-Enriched Thorium Dust"));
        replaceItems(GEM_DUSTS, GEM_DUST_MAP, "Crushed ", "", Map.of("boron_nitride", "Hexagonal Boron Nitride", "sulfur", "Sulfur"));
        simpleItems(RAWS, RAW_MAP, "Raw ", "");
        simpleItems(NUGGETS, NUGGET_MAP, " Nugget");
        replaceItems(ALLOYS, ALLOY_MAP, "", " Alloy Ingot", Map.of("hsla_steel", "HSLA Steel"));
        replaceItems(PARTS, PART_MAP, "", "", Map.of("du_plating", "DU Plating"));
        simpleItems(PART_BLOCKS, PART_BLOCK_MAP, "");
        replaceItems(COMPOUNDS, COMPOUND_MAP, "", "", Map.of("c_mn_blend", "Carbon-Manganese Blend"));
        simpleItems(UPGRADES, UPGRADE_MAP, " Upgrade");

        add(GLOWING_MUSHROOM.get(), "Glowing Mushroom");

        fuelTypeItems(AMERICIUM_MAP, "Americium-", "");
        fuelTypeItems(BERKELIUM_MAP, "Berkelium-", "");
        fuelTypeItems(BORON_MAP, "Boron-", "");
        fuelTypeItems(CALIFORNIUM_MAP, "Californium-", "");
        fuelTypeItems(CURIUM_MAP, "Curium-", "");
        fuelTypeItems(LITHIUM_MAP, "Lithium-", "");
        fuelTypeItems(NEPTUNIUM_MAP, "Neptunium-", "");
        fuelTypeItems(PLUTONIUM_MAP, "Plutonium-", "");
//        fuelTypeItems(THORIUM_MAP, "Thorium-", "");
        fuelTypeItems(URANIUM_MAP, "Uranium-", "");

        fuelPelletTypeItems(FUEL_AMERICIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_BERKELIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_CALIFORNIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_CURIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_NEPTUNIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_PLUTONIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_THORIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_URANIUM_MAP, "", " Fuel Pellet");

        add(FUEL_MIXED_MAP.get("mix_239").get(), "MIX-239");
        add(FUEL_MIXED_MAP.get("mix_239_c").get(), "MIX-239 Carbide");
        add(FUEL_MIXED_MAP.get("mix_239_ni").get(), "MNI-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_ox").get(), "MOX-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_tr").get(), "MTRISO-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_za").get(), "MZA-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241").get(), "MIX-241");
        add(FUEL_MIXED_MAP.get("mix_241_c").get(), "MIX-241 Carbide");
        add(FUEL_MIXED_MAP.get("mix_241_ni").get(), "MNI-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_ox").get(), "MOX-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_tr").get(), "MTRISO-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_za").get(), "MZA-241 Fuel Pellet");

        fuelPelletTypeItems(DEPLETED_FUEL_AMERICIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_BERKELIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_CALIFORNIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_CURIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_IC2_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_MIXED_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_NEPTUNIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_PLUTONIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_THORIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_URANIUM_MAP, "Depleted ", " Fuel Pellet");

        simpleItems(FOOD_MAP, Map.of("dominos", "Domino's Special", "smore", "S'more S'mingot", "moresmore", "MoreS'more DoubleS'mingot"));
        add(PORTABLE_ENDER_CHEST.get(), "Portable Ender Chest");
        add(FOURSMORE.get(), "FourS'more QuadS'mingot");
    }

    private void blocks() {
        simpleBlocks(INGOTS, INGOT_BLOCK_MAP, " Block");
        simpleBlocks(MATERIAL_BLOCKS, MATERIAL_BLOCK_MAP, " Block");
        simpleBlocks(RAWS, RAW_BLOCK_MAP, "Block of Raw ", "");

        add(FERTILE_ISOTOPE_MAP.get("americium").get(), "Americium-243 Block");
        add(FERTILE_ISOTOPE_MAP.get("berkelium").get(), "Berkelium-247 Block");
        add(FERTILE_ISOTOPE_MAP.get("californium").get(), "Californium-252 Block");
        add(FERTILE_ISOTOPE_MAP.get("curium").get(), "Curium-246 Block");
        add(FERTILE_ISOTOPE_MAP.get("neptunium").get(), "Neptunium-237 Block");
        add(FERTILE_ISOTOPE_MAP.get("plutonium").get(), "Plutonium-242 Block");
        add(FERTILE_ISOTOPE_MAP.get("uranium").get(), "Uranium-238 Block");

        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            for (String machine : List.of("cobblestone_generator", "water_source", "nitrogen_collector")) {
                String type = level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase();
                add(COLLECTOR_MAP.get(machine + type).asItem(), capitalize(type + "_" + machine));
            }
        }

        simpleBlocks(PROCESSOR_MAP);
        simpleBlocks(SOLAR_MAP, Map.of("solar_panel_basic", "Basic Solar Panel", "solar_panel_advanced", "Advanced Solar Panel", "solar_panel_du", "DU Solar Panel", "solar_panel_elite", "Elite Solar Panel"));
    }

    private void buckets() {
        buckets(GAS_MAP, Map.of("helium_3", "Helium-3"));
        buckets(MOLTEN_MAP, Map.of("boron_10", "Boron-10",
                "boron_11", "Boron-11",
                "lithium_6", "Lithium-6",
                "lithium_7", "Lithium-7",
                "lif", "Lithium Fluoride",
                "bef2", "Beryllium Fluoride",
                "flibe", "FLiBe Salt Mixture",
                "naoh", "Sodium Hydroxide",
                "koh", "Potassium Hydroxide",
                "bas", "Boron Arsenide"), "Molten ");
        buckets(HOT_GAS_MAP, Map.of());
        buckets(SUGAR_MAP, Map.of());
        buckets(CHOCOLATE_MAP, Map.of());
        buckets(FISSION_MAP, Map.of("strontium_90", "Strontium-90",
                "ruthenium_106", "Ruthenium-106",
                "caesium_137", "Caesium-137",
                "promethium_147", "Promethium-147",
                "europium_155", "Europium-155"));
        buckets(STEAM_MAP, Map.of());
        buckets(SALT_SOLUTION_MAP, Map.of());
        buckets(ACID_MAP, Map.of());
        buckets(FLAMMABLE_MAP, Map.of());
        nakBuckets(HOT_COOLANT_MAP);
        nakBuckets(COOLANT_MAP);
        buckets(CUSTOM_FLUID, Map.of("radaway", "RadAway Fluid", "radaway_slow", "Slow-Acting RadAway Fluid"));
        fissionBuckets(FISSION_FUEL_MAP);
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

    private void fissionBuckets(Map<String, Fluids> type) {
        for (String fluid : type.keySet()) {
            StringBuilder prefix = new StringBuilder("Molten ");
            String name = fluid;
            String suffix = "";

            if (fluid.contains("flibe")) {
                prefix.append("FLiBe Salt Solution of ");
                suffix = " Fuel";
                name = name.replace("flibe", "");
            }

            if (fluid.contains("depleted")) {
                prefix.append("Depleted ");
                name = name.replace("depleted_", "");
            }

            if (Pattern.compile("hec([mf])").matcher(fluid).find()) {
                name = capitalize(name.replace("hec", "HEC").replaceFirst("_", "-"));
            } else if (Pattern.compile("lec([mf])").matcher(fluid).find()) {
                name = capitalize(name.replace("lec", "LEC").replaceFirst("_", "-"));
            } else if (Pattern.compile("^.{3}(_\\d{3})?").matcher(name).find()) {
                name = capitalize(name.substring(0, 3).toUpperCase() + (name.length() > 3 ? (Character.isDigit(name.charAt(4)) ? "-" : "_") + name.substring(4) : ""));
            } else {
                name = capitalize(name);
            }

            add(type.get(fluid).bucket.asItem(), prefix + name + suffix + " Bucket");
            add(type.get(fluid).type.get().getDescriptionId(), prefix + name + suffix);
            add(type.get(fluid).block.get(), prefix + name + suffix);
        }
    }

    private void nakBuckets(Map<String, Fluids> type) {
        for (String fluid : type.keySet()) {
            String name = switch (fluid) {
                case "nak" -> "Eutectic NaK Alloy";
                case "nak_hot" -> "Hot Eutectic NaK Alloy";
                case String temp -> {
                    String prefix = "";
                    if (temp.contains("nak_hot")) {
                        prefix = "Hot ";
                    }
                    yield prefix + "Eutectic NaK-" + capitalize(temp.substring(0, temp.indexOf("_nak"))) + " Mixture";
                }
            };
            add(type.get(fluid).bucket.asItem(), name + " Bucket");
            add(type.get(fluid).type.get().getDescriptionId(), name);
            add(type.get(fluid).block.get(), name);
        }
    }

    private void tooltips() {
        add("tooltip.radiation", "Radiation: %s %sRad/t");
        add("tooltip.cobblestone_generator", "Produces %s Cobblestone/t constantly.");
        add("tooltip.collector", "Produces %s mb/t of %s constantly.");
        add("tooltip.solar", "Produces %s/t constantly during the daytime.");
        add("tooltip.universal_bin", "Destroys items, fluids and energy.");
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

        add("tooltip.solidified_corium", "The solidified form of a poisonous mixture of fuel and components produced in fission reactor meltdowns.");

        add("tooltip.energy_stored", "Energy Stored: %s / %s");
        add("tooltip.process_time", "§aBase Process Time:§r %s");
        add("tooltip.process_power", "§dBase Process Power:§r %s/t");
    }

    private void damage_types() {
        add("death.attack.superfluid_freeze", "%swas Bose-Einstein condensated");
        add("death.attack.plasma_burn", "%s was incinerated by plasma");
        add("death.attack.gas_burn", "%s was excruciatingly vaporized");
        add("death.attack.steam_burn", "%s would have fared better in a kettle");
        add("death.attack.molten_burn", "%s went to hell but didn't come back");
        add("death.attack.corium_burn", "%s decided to follow the fate of their fission reactor");
        add("death.attack.hot_coolant_burn", "%s cooled off at the wrong time and place");
        add("death.attack.acid_burn", "%s turned out to be far too alkaline");
        add("death.attack.fluid_burn", "%s was effectively pan-fried");
        add("death.attack.hypothermia", "%s died as a result of extreme hypothermia");
        add("death.attack.fission_burn", "%s was fried by a fission reactor");
        add("death.attack.fatal_rads", "%s died due to fatal radiation poisoning");
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

    private void fuelTypeItems(HashMap<String, DeferredItem<Item>> map, String prepend, String append) {
        for (String name : map.keySet()) {
            add(map.get(name).asItem(), prepend + capitalize(fuelTypes(name, false)) + append);
        }
    }

    private void fuelPelletTypeItems(HashMap<String, DeferredItem<Item>> map, String prepend, String append) {
        for (String name : map.keySet()) {
            add(map.get(name).asItem(), prepend + fuelTypes(name, true).replace("_", "-") + (name.endsWith("_c") || name.split("_").length == 2 ? "" : append));
        }
    }

    private String fuelTypes(String name, boolean upperCase) {
        if (!name.contains("_")) return upperCase ? name.toUpperCase() : name;
        String suffix = switch (name.substring(name.lastIndexOf('_') + 1)) {
            case "c" -> " Carbide";
            case "ni" -> " Nitride";
            case "ox" -> " Oxide";
            case "za" -> "-Zirconium Alloy";
            case "tr" -> " TRISO";
            case String val -> {
                try {
                    yield "-" + Integer.parseInt(val);
                } catch (Exception e) {
                    yield "ERROR";
                }
            }
        };

        if (upperCase) {
            return name.substring(0, name.lastIndexOf('_')).toUpperCase().replaceAll("(?<=[LH]EC)M", "m").replaceAll("(?<=[LH]EC)F", "f") + suffix;
        }
        return name.substring(0, name.lastIndexOf('_')) + suffix;
    }

    private void simpleItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String append) {
        simpleItems(list, map, "", append);
    }

    private void simpleItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String prepend, String append) {
        replaceItems(list, map, prepend, append, Map.of());
    }

    private void simpleItems(HashMap<String, DeferredItem<Item>> map) {
        replaceItems(map.keySet().stream().toList(), map, "", "", Map.of());
    }

    private void simpleItems(HashMap<String, DeferredItem<Item>> map, Map<String, String> replacers) {
        replaceItems(map.keySet().stream().toList(), map, "", "", replacers);
    }

    private void replaceItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String prepend, String append, Map<String, String> replacers) {
        for (String name : list) {
            if (replacers.containsKey(name)) {
                add(map.get(name).asItem(), replacers.get(name));
            } else {
                add(map.get(name).asItem(), prepend + capitalize(name) + append);
            }
        }
    }

    private void simpleBlocks(HashMap<String, DeferredBlock<Block>> map) {
        simpleBlocks(map, Map.of());
    }

    private void simpleBlocks(HashMap<String, DeferredBlock<Block>> map, Map<String, String> replacers) {
        replaceBlocks(map.keySet().stream().toList(), map, "", "", replacers);
    }

    private void replaceBlocks(List<String> list, HashMap<String, DeferredBlock<Block>> map, String prepend, String append, Map<String, String> replacers) {
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