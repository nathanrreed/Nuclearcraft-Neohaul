package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.TurbineRecipeViewer;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_TURBINE_CATEGORY;

public class EmiTurbineRecipe extends RecipeViewerRecipe {
    public EmiTurbineRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, TurbineRecipe recipe) {
        super(EMI_TURBINE_CATEGORY, id, new TurbineRecipeViewer(recipe));

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}