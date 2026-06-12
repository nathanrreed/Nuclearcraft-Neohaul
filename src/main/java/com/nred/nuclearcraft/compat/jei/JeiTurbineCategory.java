package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.TurbineRecipeViewer;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.TURBINE_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.TURBINE_RECIPE_TYPE;

public class JeiTurbineCategory extends JeiRecipeViewerCategory<TurbineRecipe> {
    protected static final String UID = ncLoc("turbine").getPath();

    public JeiTurbineCategory(IGuiHelper helper) {
        super(helper, UID, TurbineRecipeViewer.class, TURBINE_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TURBINE_MAP.get("turbine_controller").get()));
    }
}
