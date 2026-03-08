package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.FissionEmergencyCoolingRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.FissionEmergencyCoolingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_EMERGENCY_COOLING_CATEGORY;

public class EmiFissionEmergencyCoolingRecipe extends RecipeViewerRecipe {
    public EmiFissionEmergencyCoolingRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, FissionEmergencyCoolingRecipe recipe) {
        super(EMI_EMERGENCY_COOLING_CATEGORY, id, new FissionEmergencyCoolingRecipeViewer(recipe));

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}