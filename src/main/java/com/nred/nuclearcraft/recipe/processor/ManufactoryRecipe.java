package com.nred.nuclearcraft.recipe.processor;

import com.nred.nuclearcraft.recipe.SizedItemIngredient;
import com.nred.nuclearcraft.recipe.base_types.ItemToItemRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.MANUFACTORY_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_TYPES;

public class ManufactoryRecipe extends ItemToItemRecipe {
    public final double timeModifier;
    public final double powerModifier;

    public ManufactoryRecipe(List<SizedItemIngredient> itemInputs, List<SizedItemIngredient> itemResults, double timeModifier, double powerModifier) {
        super(itemInputs, itemResults, timeModifier, powerModifier);
        this.timeModifier = timeModifier;
        this.powerModifier = powerModifier;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MANUFACTORY_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PROCESSOR_RECIPE_TYPES.get("manufactory").get();
    }
}