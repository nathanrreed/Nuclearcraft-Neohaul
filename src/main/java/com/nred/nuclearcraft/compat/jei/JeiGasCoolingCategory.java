package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.PebbleFissionCoolerRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionCoolerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.COOLER_RECIPE_TYPE;

public class JeiGasCoolingCategory extends JeiRecipeViewerCategory<PebbleFissionCoolerRecipe> {
    protected static final String UID = ncLoc("gas_cooling").getPath();

    public JeiGasCoolingCategory(IGuiHelper helper) {
        super(helper, UID, PebbleFissionCoolerRecipeViewer.class, COOLER_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("oxygen_fission_gas_cooler").get()));
    }
}
