package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.PebbleFissionRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_PEBBLE_FISSION_CATEGORY;

public class EmiPebbleFissionRecipe extends RecipeViewerRecipe {
    public EmiPebbleFissionRecipe(ResourceLocation id, EmiIngredient input, EmiIngredient result, PebbleFissionRecipe recipe) {
        super(EMI_PEBBLE_FISSION_CATEGORY, id, new PebbleFissionRecipeViewer(recipe));

        this.inputs.add(input);
        this.outputs.addAll(result.getEmiStacks());
    }
}