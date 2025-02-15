package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.recipe.base_types.ItemToItemSerializer;
import com.nred.nuclearcraft.recipe.collector.CollectorSerializer;
import com.nred.nuclearcraft.recipe.processor.AlloyFurnaceRecipe;
import com.nred.nuclearcraft.recipe.processor.ManufactoryRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.registration.Registers.RECIPE_SERIALIZERS;

public class RecipeSerializerRegistration {
    public static final DeferredHolder<RecipeSerializer<?>, CollectorSerializer> COLLECTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("collector_recipe", CollectorSerializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, ItemToItemSerializer> ALLOY_FURNACE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("alloy_furnace_recipe", () -> new ItemToItemSerializer(AlloyFurnaceRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ItemToItemSerializer> MANUFACTORY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("manufactory_recipe", () -> new ItemToItemSerializer(ManufactoryRecipe.class));

    public static void init() {
    }
}