package com.nred.nuclearcraft.recipe.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.MELTER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MELTER_RECIPE_TYPE;

public class MelterRecipe extends ProcessorRecipe {
    public MelterRecipe(List<SizedChanceItemIngredient> itemInputs, List<SizedChanceItemIngredient> itemResults, List<SizedChanceFluidIngredient> fluidInputs, List<SizedChanceFluidIngredient> fluidResults, double timeModifier, double powerModifier, double radiation) {
        super(itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier, radiation);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MELTER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MELTER_RECIPE_TYPE.get();
    }
}