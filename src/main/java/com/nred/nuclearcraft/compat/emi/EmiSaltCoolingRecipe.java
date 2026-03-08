package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.FissionCoolantHeaterRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_SALT_COOLING_CATEGORY;

public class EmiSaltCoolingRecipe extends RecipeViewerRecipe {
    public EmiSaltCoolingRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, FissionCoolantHeaterRecipe recipe) {
        super(EMI_SALT_COOLING_CATEGORY, id, new FissionCoolantHeaterRecipeViewer(recipe));

        this.inputs.add(EmiStack.of(recipe.getHeater()));
        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }
}