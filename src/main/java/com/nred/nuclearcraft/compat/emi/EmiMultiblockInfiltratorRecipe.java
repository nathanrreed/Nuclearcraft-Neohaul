package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockInfiltratorRecipeViewer;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_MULTIBLOCK_INFILTRATOR_CATEGORY;

public class EmiMultiblockInfiltratorRecipe extends RecipeViewerRecipe {
    public EmiMultiblockInfiltratorRecipe(ResourceLocation id, MultiblockInfiltratorRecipe recipe) {
        super(EMI_MULTIBLOCK_INFILTRATOR_CATEGORY, id, new MultiblockInfiltratorRecipeViewer(recipe));

        this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        this.inputs.addAll(recipe.fluidIngredients.stream().map(ModEmiPlugin::getEmiFluidIngredient).toList());
        this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
        this.outputs.addAll(recipe.fluidProducts.stream().map(ModEmiPlugin::getEmiFluidStack).toList());
    }
}