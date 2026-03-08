package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.CondenserRecipeViewer;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_CONDENSER_CATEGORY;

public class EmiCondenserRecipe extends RecipeViewerRecipe {
    public EmiCondenserRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, CondenserRecipe recipe) {
        super(EMI_CONDENSER_CATEGORY, id, new CondenserRecipeViewer(recipe));

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}