package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.RadiationScrubberRecipeViewer;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.RADIATION_SCRUBBER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.RADIATION_SCRUBBER_RECIPE_TYPE;

public class JeiRadiationScrubberCategory extends JeiRecipeViewerCategory<RadiationScrubberRecipe> {
    protected static final String UID = ncLoc("radiation_scrubber").getPath();

    public JeiRadiationScrubberCategory(IGuiHelper helper) {
        super(helper, UID, RadiationScrubberRecipeViewer.class, RADIATION_SCRUBBER_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RADIATION_SCRUBBER.get()));
    }
}
