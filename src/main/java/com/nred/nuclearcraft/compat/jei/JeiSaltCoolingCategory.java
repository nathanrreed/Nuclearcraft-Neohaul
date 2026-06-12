package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.FissionCoolantHeaterRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.COOLANT_HEATER_RECIPE_TYPE;

public class JeiSaltCoolingCategory extends JeiRecipeViewerCategory<FissionCoolantHeaterRecipe> {
    protected static final String UID = ncLoc("salt_cooling").getPath();

    public JeiSaltCoolingCategory(IGuiHelper helper) {
        super(helper, UID, FissionCoolantHeaterRecipeViewer.class, COOLANT_HEATER_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater").get()));
    }
}
