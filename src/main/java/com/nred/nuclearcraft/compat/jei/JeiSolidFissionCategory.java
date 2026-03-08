package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.SolidFissionRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.SolidFissionRecipe;
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

public class JeiSolidFissionCategory extends JeiRecipeViewerCategory<SolidFissionRecipe> {
    protected static final ResourceLocation UID = ncLoc("solid_fission");
    private static final RecipeType<SolidFissionRecipe> TYPE = new RecipeType<>(UID, SolidFissionRecipe.class);

    public JeiSolidFissionCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), SolidFissionRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<SolidFissionRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller").get()));
    }
}