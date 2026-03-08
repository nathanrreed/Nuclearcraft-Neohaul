package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.CondenserRecipeViewer;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
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

public class JeiCondenserCategory extends JeiRecipeViewerCategory<CondenserRecipe> {
    protected static final ResourceLocation UID = ncLoc("condenser");
    private static final RecipeType<CondenserRecipe> TYPE = new RecipeType<>(UID, CondenserRecipe.class);

    public JeiCondenserCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), CondenserRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<CondenserRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(HX_MAP.get("condenser_controller").get()));
    }
}