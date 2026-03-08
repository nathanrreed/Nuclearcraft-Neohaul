package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.DecayGeneratorRecipeViewer;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_DECAY_GENERATOR_CATEGORY;

public class EmiDecayGeneratorRecipe extends RecipeViewerRecipe {
    public EmiDecayGeneratorRecipe(ResourceLocation id, DecayGeneratorRecipe recipe) {
        super(EMI_DECAY_GENERATOR_CATEGORY, id, new DecayGeneratorRecipeViewer(recipe));

        this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
    }
}