package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.FLAMMABLE_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.COMPOUND_MAP;

public class RadiationScrubberProvider {
    public RadiationScrubberProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new RadiationScrubberRecipe(SizedChanceItemIngredient.of(COMPOUND_MAP.get("borax"), 1), null, SizedChanceItemIngredient.of(COMPOUND_MAP.get("irradiated_borax"), 1), null, 12000, 200, 1.0)).save(recipeOutput);
        new RadiationScrubberRecipeBuilder(new RadiationScrubberRecipe(null, Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("radaway"), 250), null, Fluids.sizedIngredient(FLAMMABLE_MAP.get("ethanol"), 250), 2400, 40, 5.0)).save(recipeOutput, ncLoc("radiation_scrubber_from_radaway"));
        new RadiationScrubberRecipeBuilder(new RadiationScrubberRecipe(null, Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("radaway_slow"), 250), null, Fluids.sizedIngredient(FLAMMABLE_MAP.get("redstone_ethanol"), 250), 96000, 20, 0.25)).save(recipeOutput, ncLoc("radiation_scrubber_from_radaway_slow"));
    }

    private static class RadiationScrubberRecipeBuilder extends BasicRecipeBuilder<RadiationScrubberRecipe> { // Required since Empty ingredient is passed
        public RadiationScrubberRecipeBuilder(RadiationScrubberRecipe recipe) {
            super(recipe, ItemStack.EMPTY);
        }
    }
}