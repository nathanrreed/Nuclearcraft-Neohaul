package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.recipe.base_types.ItemToItemRecipe;
import com.nred.nuclearcraft.recipe.collector.CollectorRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.Registers.RECIPE_TYPES;

public class RecipeTypeRegistration {
    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(final String identifier) {
        return RECIPE_TYPES.register(identifier, registryName -> new RecipeType<>() {
            @Override
            public String toString() {
                return registryName.toString();
            }
        });
    }

    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> COBBLE_GENERATOR_RECIPE_TYPE = register("cobblestone_generator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> COBBLE_GENERATOR_COMPACT_RECIPE_TYPE = register("cobblestone_generator_compact");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> COBBLE_GENERATOR_DENSE_RECIPE_TYPE = register("cobblestone_generator_dense");

    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> WATER_SOURCE_RECIPE_TYPE = register("water_source");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> WATER_SOURCE_COMPACT_RECIPE_TYPE = register("water_source_compact");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> WATER_SOURCE_DENSE_RECIPE_TYPE = register("water_source_dense");

    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> NITROGEN_COLLECTOR_RECIPE_TYPE = register("nitrogen_collector");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> NITROGEN_COLLECTOR_COMPACT_RECIPE_TYPE = register("nitrogen_collector_compact");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE = register("nitrogen_collector_dense");

    public static final Map<String, DeferredHolder<RecipeType<?>, RecipeType<ItemToItemRecipe>>> PROCESSOR_RECIPE_TYPES = PROCESSOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), RecipeTypeRegistration::register));

    public static void init() {
    }
}