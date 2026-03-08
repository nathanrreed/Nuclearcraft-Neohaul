package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.HeatExchangerRecipeViewer;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.HX_MAP;

public class JeiHeatExchangerCategory extends JeiRecipeViewerCategory<HeatExchangerRecipe> {
    protected static final ResourceLocation UID = ncLoc("heat_exchanger");
    private static final RecipeType<HeatExchangerRecipe> TYPE = new RecipeType<>(UID, HeatExchangerRecipe.class);

    public JeiHeatExchangerCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), HeatExchangerRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<HeatExchangerRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(HX_MAP.get("heat_exchanger_controller").get()));
    }
}