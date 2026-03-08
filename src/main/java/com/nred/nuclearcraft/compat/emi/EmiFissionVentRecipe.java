package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.FissionVentRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.FissionHeatingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_VENT_CATEGORY;

public class EmiFissionVentRecipe extends RecipeViewerRecipe {
    public EmiFissionVentRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, FissionHeatingRecipe recipe) {
        super(EMI_VENT_CATEGORY, id, new FissionVentRecipeViewer(recipe));

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}