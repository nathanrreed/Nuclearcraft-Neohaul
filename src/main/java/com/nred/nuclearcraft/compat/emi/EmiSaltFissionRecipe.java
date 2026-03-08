package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.SaltFissionRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.SaltFissionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_SALT_FISSION_CATEGORY;

public class EmiSaltFissionRecipe extends RecipeViewerRecipe {
    public EmiSaltFissionRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, SaltFissionRecipe recipe) {
        super(EMI_SALT_FISSION_CATEGORY, id, new SaltFissionRecipeViewer(recipe));

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}