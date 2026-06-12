package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.SolidFissionRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.SolidFissionRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.SOLID_FISSION_RECIPE_TYPE;

public class JeiSolidFissionCategory extends JeiRecipeViewerCategory<SolidFissionRecipe> {
    protected static final String UID = ncLoc("solid_fission").getPath();

    public JeiSolidFissionCategory(IGuiHelper helper) {
        super(helper, UID, SolidFissionRecipeViewer.class, SOLID_FISSION_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller").get()));
    }
}
