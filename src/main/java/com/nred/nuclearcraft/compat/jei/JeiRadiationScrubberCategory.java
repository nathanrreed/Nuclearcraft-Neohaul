package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.RadiationScrubberRecipeViewer;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.RADIATION_SCRUBBER;

public class JeiRadiationScrubberCategory extends JeiRecipeViewerCategory<RadiationScrubberRecipe> {
    protected static final ResourceLocation UID = ncLoc("radiation_scrubber");
    private static final RecipeType<RadiationScrubberRecipe> TYPE = new RecipeType<>(UID, RadiationScrubberRecipe.class);

    public JeiRadiationScrubberCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), RadiationScrubberRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<RadiationScrubberRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RADIATION_SCRUBBER.get()));
    }
}