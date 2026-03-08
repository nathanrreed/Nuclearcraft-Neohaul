package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.SolidFissionRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.SolidFissionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_SOLID_FISSION_CATEGORY;

public class EmiSolidFissionRecipe extends RecipeViewerRecipe {
    public EmiSolidFissionRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, SolidFissionRecipe recipe) {
        super(EMI_SOLID_FISSION_CATEGORY, id, new SolidFissionRecipeViewer(recipe));

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}