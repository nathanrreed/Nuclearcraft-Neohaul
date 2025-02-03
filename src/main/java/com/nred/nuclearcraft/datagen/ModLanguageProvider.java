package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
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
    }


    private void tooltips() {
        add("tooltip.radiation", "Radiation: %s %sRad/t");
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
        return String.join(" ", Arrays.stream(input.split("_")).map(StringUtils::capitalize).toList());
    }
}