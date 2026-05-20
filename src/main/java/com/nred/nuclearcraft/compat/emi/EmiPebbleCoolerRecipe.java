package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.PebbleFissionCoolerRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionCoolerRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_PEBBLE_COOLER_CATEGORY;

public class EmiPebbleCoolerRecipe extends RecipeViewerRecipe {
    public EmiPebbleCoolerRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, PebbleFissionCoolerRecipe recipe) {
        super(EMI_PEBBLE_COOLER_CATEGORY, id, new PebbleFissionCoolerRecipeViewer(recipe));

        this.inputs.add(EmiStack.of(recipe.getCooler()));
        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}