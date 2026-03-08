package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.TurbineRecipeViewer;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.TURBINE_MAP;

public class JeiTurbineCategory extends JeiRecipeViewerCategory<TurbineRecipe> {
    protected static final ResourceLocation UID = ncLoc("turbine");
    private static final RecipeType<TurbineRecipe> TYPE = new RecipeType<>(UID, TurbineRecipe.class);

    public JeiTurbineCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), TurbineRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<TurbineRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TURBINE_MAP.get("turbine_controller").get()));
    }
}