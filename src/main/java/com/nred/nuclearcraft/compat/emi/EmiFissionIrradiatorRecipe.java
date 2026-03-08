package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.FissionIrradiatorRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.FissionIrradiatorRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_IRRADIATOR_CATEGORY;

public class EmiFissionIrradiatorRecipe extends RecipeViewerRecipe {
    public EmiFissionIrradiatorRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, FissionIrradiatorRecipe recipe) {
        super(EMI_IRRADIATOR_CATEGORY, id, new FissionIrradiatorRecipeViewer(recipe));

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}