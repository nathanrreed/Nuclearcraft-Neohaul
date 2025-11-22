package com.nred.nuclearcraft.radiation;

import com.nred.nuclearcraft.capability.radiation.resistance.RadiationResistanceItem;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.util.ArmorHelper;
import com.nred.nuclearcraft.util.RegistryHelper;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.DataComponentRegistration.RADIATION_RESISTANCE_ITEM;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class RadArmor {
    public static final IntSet ARMOR_STACK_SHIELDING_BLACKLIST = new IntOpenHashSet();
    public static final IntSet ARMOR_STACK_SHIELDING_LIST = new IntOpenHashSet();

    public static final Int2DoubleMap ARMOR_RAD_RESISTANCE_MAP = new Int2DoubleOpenHashMap();

    public static void init() {
        for (String stackInfo : radiation_shielding_item_blacklist) {
            ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo);
            if (stack != null) {
                int packed = RecipeHelper.pack(stack);
                if (packed != 0) {
                    ARMOR_STACK_SHIELDING_BLACKLIST.add(packed);
                }
            }
        }

        for (String stackInfo : radiation_shielding_custom_stacks) {
            ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo);
            if (stack != null) {
                int packed = RecipeHelper.pack(stack);
                if (packed != 0) {
                    ARMOR_STACK_SHIELDING_LIST.add(packed);
                }
            }
        }
    }

    public static void postInit() {
        if (radiation_shielding_default_levels == null) return;
        for (String stackInfo : radiation_shielding_default_levels) {
            int scorePos = stackInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo.substring(0, scorePos));
            if (stack == null || stack.isEmpty() || !ArmorHelper.isArmor(stack.getItem(), radiation_horse_armor_public)) {
                continue;
            }
            int packed = RecipeHelper.pack(stack);
            if (packed == 0) {
                continue;
            }
            ARMOR_RAD_RESISTANCE_MAP.put(packed, Double.parseDouble(stackInfo.substring(scorePos + 1)));
        }
    }

    public static void refreshRadiationArmor() { // TODO
        ARMOR_RAD_RESISTANCE_MAP.clear();

        postInit();
    }

    public static ItemStack armorWithRadResistance(ItemStack armor, double resistance) {
        ItemStack stack = armor.copy();
        if (!ArmorHelper.isArmor(armor.getItem(), radiation_horse_armor_public)) {
            return stack;
        }
        stack.set(RADIATION_RESISTANCE_ITEM, new RadiationResistanceItem(resistance));
        return stack;
    }

    private static final List<DeferredItem<Item>> rad_shielding = List.of(LIGHT_RADIATION_SHIELDING, MEDIUM_RADIATION_SHIELDING, HEAVY_RADIATION_SHIELDING);

    private static ArrayList<RecipeHolder<ShapelessRecipe>> addArmorShieldingRecipes(ItemStack stack) {
        ArrayList<RecipeHolder<ShapelessRecipe>> holders = new ArrayList<>(3);
        for (int i : List.of(0, 1, 2)) {
            holders.add(new RecipeHolder<>(ncLoc(stack.getItem().toString().replace(':', '_') + "_" + rad_shielding.get(i).getId().getPath()), new ShapelessRecipe("", CraftingBookCategory.MISC, armorWithRadResistance(stack, radiation_shielding_level[i]), NonNullList.copyOf(List.of(Ingredient.of(stack), Ingredient.of(rad_shielding.get(i)))))));
        }
        return holders;
    }

    public static void addArmorShieldingRecipes(ServerAboutToStartEvent event) {
        RecipeManager recipeManager = event.getServer().getRecipeManager();
        ArrayList<RecipeHolder<?>> shielding_recipes = new ArrayList<>();
        if (radiation_shielding_default_recipes) {
            for (Item item : BuiltInRegistries.ITEM.stream().toList()) {
                if (ArmorHelper.isArmor(item, radiation_horse_armor_public)) {
                    int packed = RecipeHelper.pack(item);
                    if (!RadArmor.ARMOR_STACK_SHIELDING_BLACKLIST.contains(packed)) {
                        shielding_recipes.addAll(RadArmor.addArmorShieldingRecipes(new ItemStack(item)));
                    }
                }
            }
        }

        for (int packed : RadArmor.ARMOR_STACK_SHIELDING_LIST) {
            shielding_recipes.addAll(RadArmor.addArmorShieldingRecipes(RecipeHelper.unpack(packed)));
        }
        shielding_recipes.addAll(recipeManager.getOrderedRecipes());
        recipeManager.replaceRecipes(shielding_recipes);
        ClientHooks.onRecipesUpdated(recipeManager);
    }
}