package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.SaltFissionRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.SaltFissionRecipe;
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

public class JeiSaltFissionCategory extends JeiRecipeViewerCategory<SaltFissionRecipe> {
    protected static final ResourceLocation UID = ncLoc("salt_fission");
    private static final RecipeType<SaltFissionRecipe> TYPE = new RecipeType<>(UID, SaltFissionRecipe.class);

    public JeiSaltFissionCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), SaltFissionRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<SaltFissionRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("molten_salt_fission_controller").get()));
    }
}