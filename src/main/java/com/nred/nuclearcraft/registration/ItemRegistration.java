package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.item.FoodItem;
import com.nred.nuclearcraft.item.LithiumIonCell;
import com.nred.nuclearcraft.item.PortableEnderChest;
import com.nred.nuclearcraft.item.TooltipItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;

import static com.nred.nuclearcraft.helpers.SimpleHelper.newEffect;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.Registers.ITEMS;
import static com.nred.nuclearcraft.registration.SoundRegistration.*;

public class ItemRegistration {
    public static final HashMap<String, DeferredItem<Item>> INGOT_MAP = createItems(INGOTS, "ingot");
    public static final HashMap<String, DeferredItem<Item>> GEM_MAP = createItems(GEMS, "gem");
    public static final HashMap<String, DeferredItem<Item>> DUST_MAP = createItems(DUSTS, "dust");
    public static final HashMap<String, DeferredItem<Item>> FISSION_DUST_MAP = createItems(FISSION_DUSTS, "dust");
    public static final HashMap<String, DeferredItem<Item>> GEM_DUST_MAP = createItems(GEM_DUSTS, "gem_dust");
    public static final HashMap<String, DeferredItem<Item>> RAW_MAP = createItems(RAWS, "", "raw");
    public static final HashMap<String, DeferredItem<Item>> NUGGET_MAP = createItems(NUGGETS, "", "nugget");
    public static final HashMap<String, DeferredItem<Item>> ALLOY_MAP = createItems(ALLOYS, "", "alloy");
    public static final HashMap<String, DeferredItem<Item>> PART_MAP = createItems(PARTS, "");
    public static final HashMap<String, DeferredItem<Item>> PART_BLOCK_MAP = createItems(PART_BLOCKS, "");
    public static final HashMap<String, DeferredItem<Item>> COMPOUND_MAP = createItems(COMPOUNDS, "");
    public static final HashMap<String, DeferredItem<Item>> UPGRADE_MAP = createItems(UPGRADES, "upgrade", true);
    public static final HashMap<String, DeferredItem<Item>> URANIUM_MAP = createItems(URANIUMS, "uranium", "");
    public static final HashMap<String, DeferredItem<Item>> FOOD_MAP = createFoods();
    public static final HashMap<String, DeferredItem<Item>> MUSIC_DISC_MAP = createMusicDiscs();
    public static final DeferredItem<Item> PORTABLE_ENDER_CHEST = ITEMS.register("portable_ender_chest", () -> new PortableEnderChest(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FOURSMORE = ITEMS.register("foursmore", () -> new FoodItem(48, 8.6F, List.of(newEffect(MobEffects.MOVEMENT_SPEED, 1, 1200), newEffect(MobEffects.DIG_SPEED, 2, 1200), newEffect(MobEffects.ABSORPTION, 2, 1200))));
    public static final DeferredItem<Item> LITHIUM_ION_CELL = ITEMS.register("lithium_ion_cell", () -> new LithiumIonCell(new Item.Properties()));

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
            map.put(name, ITEMS.register((!prepend.isEmpty() ? prepend + "_" : "") + name + (!append.isEmpty() ? "_" + append : ""), () -> new TooltipItem(new Item.Properties(), byPassShift)));
        }
        return map;
    }

    public static HashMap<String, DeferredItem<Item>> createFoods() {
        HashMap<String, DeferredItem<Item>> map = new HashMap<>();
        map.put("cocoa_butter", ITEMS.register("cocoa_butter", () -> new FoodItem(2, 0.2F, List.of(newEffect(MobEffects.ABSORPTION, 1, 300)))));
        map.put("cocoa_solids", ITEMS.register("cocoa_solids", () -> new Item(new Item.Properties())));
        map.put("dark_chocolate", ITEMS.register("dark_chocolate", () -> new FoodItem(3, 0.4F, List.of(newEffect(MobEffects.DIG_SPEED, 1, 300), newEffect(MobEffects.MOVEMENT_SPEED, 1, 300)))));
        map.put("dominos", ITEMS.register("dominos", () -> new FoodItem(16, 1.8F, List.of(newEffect(MobEffects.MOVEMENT_SPEED, 2, 600), newEffect(MobEffects.DIG_SPEED, 2, 600)), "tooltip.dominos")));
        map.put("flour", ITEMS.register("flour", () -> new Item(new Item.Properties())));
        map.put("gelatin", ITEMS.register("gelatin", () -> new Item(new Item.Properties())));
        map.put("graham_cracker", ITEMS.register("graham_cracker", () -> new FoodItem(1, 0.2f)));
        map.put("ground_cocoa_nibs", ITEMS.register("ground_cocoa_nibs", () -> new FoodItem(1, 0.2F)));
        map.put("marshmallow", ITEMS.register("marshmallow", () -> new FoodItem(1, 0.4F, List.of(newEffect(MobEffects.MOVEMENT_SPEED, 1, 300)), "tooltip.marshmallow")));
        map.put("milk_chocolate", ITEMS.register("milk_chocolate", () -> new FoodItem(4, 0.6F, List.of(newEffect(MobEffects.DIG_SPEED, 1, 300), newEffect(MobEffects.MOVEMENT_SPEED, 1, 300), newEffect(MobEffects.ABSORPTION, 1, 300)))));
        map.put("moresmore", ITEMS.register("moresmore", () -> new FoodItem(20, 3.8F, List.of(newEffect(MobEffects.MOVEMENT_SPEED, 1, 600), newEffect(MobEffects.DIG_SPEED, 2, 600), newEffect(MobEffects.ABSORPTION, 2, 600)))));
        map.put("roasted_cocoa_beans", ITEMS.register("roasted_cocoa_beans", () -> new Item(new Item.Properties())));
        map.put("smore", ITEMS.register("smore", () -> new FoodItem(8, 1.4F, List.of(newEffect(MobEffects.MOVEMENT_SPEED, 2, 300), newEffect(MobEffects.DIG_SPEED, 2, 300), newEffect(MobEffects.ABSORPTION, 2, 300)))));
        map.put("unsweetened_chocolate", ITEMS.register("unsweetened_chocolate", () -> new FoodItem(2, 0.2F, List.of(newEffect(MobEffects.DIG_SPEED, 1, 300)))));
        return map;
    }

    public static HashMap<String, DeferredItem<Item>> createMusicDiscs() {
        HashMap<String, DeferredItem<Item>> map = new HashMap<>();
        map.put("music_disc_wanderer", ITEMS.register("music_disc_wanderer", () -> new TooltipItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).jukeboxPlayable(WANDERER_KEY), List.of("music_disc.wanderer.credit"))));
        map.put("music_disc_end_of_the_world", ITEMS.register("music_disc_end_of_the_world", () -> new TooltipItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).jukeboxPlayable(END_OF_THE_WORLD_KEY), List.of("music_disc.end_of_the_world.credit"))));
        map.put("music_disc_money_for_nothing", ITEMS.register("music_disc_money_for_nothing", () -> new TooltipItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).jukeboxPlayable(MONEY_FOR_NOTHING_KEY), List.of("music_disc.money_for_nothing.credit"))));
        map.put("music_disc_hyperspace", ITEMS.register("music_disc_hyperspace", () -> new TooltipItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).jukeboxPlayable(HYPERSPACE_KEY), List.of("music_disc.hyperspace.credit"))));
        return map;
    }

    public static void init() {
    }
}