package com.nred.nuclearcraft.registration;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class ToolMaterialRegistration {
    public static final Tier BORON_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            547, 8f, 2.5f, 6,
            () -> Ingredient.of(INGOT_MAP.get("boron"))
    );
    public static final Tier BORON_SPAXELHOE_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            547 * 5, 8f, 2.5f, 6,
            () -> Ingredient.of(INGOT_MAP.get("boron"))
    );
    public static final Tier BORON_NITRIDE_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1928, 12f, 3.5f, 20,
            () -> Ingredient.of(GEM_MAP.get("boron_nitride"))
    );
    public static final Tier BORON_NITRIDE_SPAXELHOE_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1928 * 5, 12f, 3.5f, 20,
            () -> Ingredient.of(GEM_MAP.get("boron_nitride"))
    );
    public static final Tier HARD_CARBON_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            1245, 11f, 3f, 12,
            () -> Ingredient.of(ALLOY_MAP.get("hard_carbon"))
    );
    public static final Tier HARD_CARBON_SPAXELHOE_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            1245 * 5, 11f, 3f, 12,
            () -> Ingredient.of(ALLOY_MAP.get("hard_carbon"))
    );
    public static final Tier TOUGH_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            929, 10f, 3f, 15,
            () -> Ingredient.of(ALLOY_MAP.get("tough"))
    );
    public static final Tier TOUGH_SPAXELHOE_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            929 * 5, 10f, 3f, 15,
            () -> Ingredient.of(ALLOY_MAP.get("tough"))
    );

    // TODO add tool handle from Tinkers

    public static void init() {
    }
}