package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.HeatExchangerRecipeViewer;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_HEAT_EXCHANGER_CATEGORY;

public class EmiHeatExchangerRecipe extends RecipeViewerRecipe {
    public EmiHeatExchangerRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, HeatExchangerRecipe recipe) {
        super(EMI_HEAT_EXCHANGER_CATEGORY, id, new HeatExchangerRecipeViewer(recipe));

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}