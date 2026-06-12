package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.FissionEmergencyCoolingRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.FissionEmergencyCoolingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_EMERGENCY_COOLING_RECIPE_TYPE;

public class JeiFissionEmergencyCoolingCategory extends JeiRecipeViewerCategory<FissionEmergencyCoolingRecipe> {
    protected static final String UID = ncLoc("fission_emergency_cooling").getPath();

    public JeiFissionEmergencyCoolingCategory(IGuiHelper helper) {
        super(helper, UID, FissionEmergencyCoolingRecipeViewer.class, FISSION_EMERGENCY_COOLING_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("fission_vent").get()));
    }
}
