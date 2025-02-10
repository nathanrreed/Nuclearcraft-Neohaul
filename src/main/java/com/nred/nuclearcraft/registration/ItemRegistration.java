package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.item.ToolTipItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;

import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.Registers.ITEMS;

public class ItemRegistration {
    public static final HashMap<String, DeferredItem<Item>> INGOT_MAP = createItems(INGOTS, "ingot");
    public static final HashMap<String, DeferredItem<Item>> GEM_MAP = createItems(GEMS, "gem");
    public static final HashMap<String, DeferredItem<Item>> DUST_MAP = createItems(DUSTS, "dust");
    public static final HashMap<String, DeferredItem<Item>> GEM_DUST_MAP = createItems(GEM_DUSTS, "gem_dust");
    public static final HashMap<String, DeferredItem<Item>> RAW_MAP = createItems(RAWS, "", "raw");
    public static final HashMap<String, DeferredItem<Item>> NUGGET_MAP = createItems(NUGGETS, "", "nugget");
    public static final HashMap<String, DeferredItem<Item>> ALLOY_MAP = createItems(ALLOYS, "", "alloy");
    public static final HashMap<String, DeferredItem<Item>> PART_MAP = createItems(PARTS, "");
    public static final HashMap<String, DeferredItem<Item>> PART_BLOCK_MAP = createItems(PART_BLOCKS, "");
    public static final HashMap<String, DeferredItem<Item>> COMPOUND_MAP = createItems(COMPOUNDS, "");
    public static final HashMap<String, DeferredItem<Item>> UPGRADE_MAP = createItems(UPGRADES, "upgrade", true);
    public static final HashMap<String, DeferredItem<Item>> URANIUM_MAP = createItems(URANIUMS, "uranium", "");


    private static HashMap<String, DeferredItem<Item>> createItems(List<String> names, String append) {
        return createItems(names, "", append, false);
    }

    private static HashMap<String, DeferredItem<Item>> createItems(List<String> names, String append, boolean byPassShift) {
        return createItems(names, "", append, byPassShift);
    }

    private static HashMap<String, DeferredItem<Item>> createItems(List<String> names, String prepend, String append) {
        return createItems(names, prepend, append, false);
    }

    private static HashMap<String, DeferredItem<Item>> createItems(List<String> names, String prepend, String append, boolean byPassShift) {
        HashMap<String, DeferredItem<Item>> map = new HashMap<>();
        for (String name : names) {
            map.put(name, ITEMS.register((!prepend.isEmpty() ? prepend + "_" : "") + name + (!append.isEmpty() ? "_" + append : ""), () -> new ToolTipItem(new Item.Properties(), byPassShift)));
        }
        return map;
    }

    public static void init() {
    }
}