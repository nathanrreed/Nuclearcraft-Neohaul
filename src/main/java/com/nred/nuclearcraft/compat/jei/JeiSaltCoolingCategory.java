package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.FissionCoolantHeaterRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
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

public class JeiSaltCoolingCategory extends JeiRecipeViewerCategory<FissionCoolantHeaterRecipe> {
    protected static final ResourceLocation UID = ncLoc("salt_cooling");
    private static final RecipeType<FissionCoolantHeaterRecipe> TYPE = new RecipeType<>(UID, FissionCoolantHeaterRecipe.class);

    public JeiSaltCoolingCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), FissionCoolantHeaterRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<FissionCoolantHeaterRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater").get()));
    }
}