package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.PebbleFissionRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;

public class JeiPebbleFissionCategory extends JeiRecipeViewerCategory<PebbleFissionRecipe> {
    protected static final ResourceLocation UID = ncLoc("pebble_fission");
    private static final RecipeType<PebbleFissionRecipe> TYPE = new RecipeType<>(UID, PebbleFissionRecipe.class);

    public JeiPebbleFissionCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), PebbleFissionRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<PebbleFissionRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("pebble_bed_fission_controller").get()));
    }
}