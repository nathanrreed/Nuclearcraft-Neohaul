package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.RadiationScrubberRecipeViewer;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_RADIATION_SCRUBBER_CATEGORY;

public class EmiRadiationScrubberRecipe extends RecipeViewerRecipe {
    public EmiRadiationScrubberRecipe(ResourceLocation id, RadiationScrubberRecipe recipe) {
        super(EMI_RADIATION_SCRUBBER_CATEGORY, id, new RadiationScrubberRecipeViewer(recipe));

        if (!recipe.getItemIngredient().isEmpty())
            this.inputs.add(EmiIngredient.of(recipe.getItemIngredient().ingredient(), recipe.getItemIngredient().count()));
        if (!recipe.getFluidIngredient().isEmpty())
            this.inputs.add(NeoForgeEmiIngredient.of(recipe.getFluidIngredient().sized()));
        if (!recipe.getItemProduct().isEmpty())
            this.outputs.add(EmiStack.of(recipe.getItemProduct().getStack()));
        if (!recipe.getFluidProduct().isEmpty())
            this.outputs.add(NeoForgeEmiStack.of(recipe.getFluidProduct().getStack()));
    }
}