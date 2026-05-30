package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.SupercoolerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.datagen.ModFluidTagProvider.HELIUM_TAG;
import static com.nred.nuclearcraft.datagen.ModFluidTagProvider.NITROGEN_TAG;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.util.FluidStackHelper.BUCKET_VOLUME;

public class SupercoolerProvider {
    public SupercoolerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 1, 1).addFluidInput(HELIUM_TAG, BUCKET_VOLUME * 8).addFluidResult(CUSTOM_FLUID_MAP.get("liquid_helium"), 25).save(recipeOutput);
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 0.5, 0.5).addFluidInput(NITROGEN_TAG, BUCKET_VOLUME * 8).addFluidResult(CUSTOM_FLUID_MAP.get("liquid_nitrogen"), 25).save(recipeOutput);
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 0.25, 0.5).addFluidInput(Fluids.WATER, BUCKET_VOLUME / 4).addFluidResult(CUSTOM_FLUID_MAP.get("ice"), BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 0.25, 1).addFluidInput(CUSTOM_FLUID_MAP.get("emergency_coolant_heated"), BUCKET_VOLUME / 4).addFluidResult(CUSTOM_FLUID_MAP.get("emergency_coolant"), BUCKET_VOLUME / 4).save(recipeOutput);
    }
}