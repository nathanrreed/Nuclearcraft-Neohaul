package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.PebbleFissionRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PEBBLE_FISSION_RECIPE_TYPE;

public class JeiPebbleFissionCategory extends JeiRecipeViewerCategory<PebbleFissionRecipe> {
    protected static final String UID = ncLoc("pebble_fission").getPath();

    public JeiPebbleFissionCategory(IGuiHelper helper) {
        super(helper, UID, PebbleFissionRecipeViewer.class, PEBBLE_FISSION_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("pebble_bed_fission_controller").get()));
    }
}
