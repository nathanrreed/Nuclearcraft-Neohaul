package com.nred.nuclearcraft.recipe.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FLUID_ENRICHER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FLUID_ENRICHER_RECIPE_TYPE;

public class FluidEnricherRecipe extends ProcessorRecipe {
    public FluidEnricherRecipe(List<SizedChanceItemIngredient> itemInputs, List<SizedChanceItemIngredient> itemResults, List<SizedChanceFluidIngredient> fluidInputs, List<SizedChanceFluidIngredient> fluidResults, double timeModifier, double powerModifier, double radiation) {
        super(itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier, radiation);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FLUID_ENRICHER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return FLUID_ENRICHER_RECIPE_TYPE.get();
    }
}