package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.FissionIrradiatorRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.FissionIrradiatorRecipe;
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

public class JeiFissionIrradiatorCategory extends JeiRecipeViewerCategory<FissionIrradiatorRecipe> {
    protected static final ResourceLocation UID = ncLoc("fission_irradiator");
    private static final RecipeType<FissionIrradiatorRecipe> TYPE = new RecipeType<>(UID, FissionIrradiatorRecipe.class);

    public JeiFissionIrradiatorCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), FissionIrradiatorRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<FissionIrradiatorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("fission_irradiator").get()));
    }
}