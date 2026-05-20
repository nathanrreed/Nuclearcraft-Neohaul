package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionCoolerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;

import static com.nred.nuclearcraft.info.Names.GAS_COOLANTS;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.HOT_GAS_MAP;

public class PebbleFissionCoolerProvider {
    public PebbleFissionCoolerProvider(RecipeOutput recipeOutput) {
        for (String coolant : GAS_COOLANTS) {
            new BasicRecipeBuilder<>(new PebbleFissionCoolerRecipe(FISSION_REACTOR_MAP.get(coolant + "_fission_gas_cooler").toStack(), SizedChanceFluidIngredient.of(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", coolant)), 1), SizedChanceFluidIngredient.of(HOT_GAS_MAP.get(coolant + "_hot").still.get(), 1), coolant + "_cooler")).save(recipeOutput, "_cooler");
        }
    }
}