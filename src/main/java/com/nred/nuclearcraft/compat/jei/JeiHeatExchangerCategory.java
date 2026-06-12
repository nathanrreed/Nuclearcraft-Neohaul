package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.HeatExchangerRecipeViewer;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.HX_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.HEAT_EXCHANGER_RECIPE_TYPE;

public class JeiHeatExchangerCategory extends JeiRecipeViewerCategory<HeatExchangerRecipe> {
    protected static final String UID = ncLoc("heat_exchanger").getPath();

    public JeiHeatExchangerCategory(IGuiHelper helper) {
        super(helper, UID, HeatExchangerRecipeViewer.class, HEAT_EXCHANGER_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(HX_MAP.get("heat_exchanger_controller").get()));
    }
}
