package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockDistillerRecipeViewer;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_MULTIBLOCK_DISTILLER_CATEGORY;

public class EmiMultiblockDistillerRecipe extends RecipeViewerRecipe {
    public EmiMultiblockDistillerRecipe(ResourceLocation id, MultiblockDistillerRecipe recipe) {
        super(EMI_MULTIBLOCK_DISTILLER_CATEGORY, id, new MultiblockDistillerRecipeViewer(recipe));

        this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        this.inputs.addAll(recipe.fluidIngredients.stream().map(ModEmiPlugin::getEmiFluidIngredient).toList());
        this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
        this.outputs.addAll(recipe.fluidProducts.stream().map(ModEmiPlugin::getEmiFluidStack).toList());
    }
}