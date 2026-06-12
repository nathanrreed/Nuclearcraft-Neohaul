package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.DecayGeneratorRecipeViewer;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.DECAY_GENERATOR;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.DECAY_GENERATOR_RECIPE_TYPE;

public class JeiDecayGeneratorCategory extends JeiRecipeViewerCategory<DecayGeneratorRecipe> {
    protected static final String UID = ncLoc("decay_generator").getPath();

    public JeiDecayGeneratorCategory(IGuiHelper helper) {
        super(helper, UID, DecayGeneratorRecipeViewer.class, DECAY_GENERATOR_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(DECAY_GENERATOR.get()));
    }
}
