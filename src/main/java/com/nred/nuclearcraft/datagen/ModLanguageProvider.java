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