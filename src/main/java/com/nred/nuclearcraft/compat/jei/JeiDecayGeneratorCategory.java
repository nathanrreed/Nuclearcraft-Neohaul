package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.DecayGeneratorRecipeViewer;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.DECAY_GENERATOR;

public class JeiDecayGeneratorCategory extends JeiRecipeViewerCategory<DecayGeneratorRecipe> {
    protected static final ResourceLocation UID = ncLoc("decay_generator");
    private static final RecipeType<DecayGeneratorRecipe> TYPE = new RecipeType<>(UID, DecayGeneratorRecipe.class);

    public JeiDecayGeneratorCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), DecayGeneratorRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<DecayGeneratorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(DECAY_GENERATOR.get()));
    }
}