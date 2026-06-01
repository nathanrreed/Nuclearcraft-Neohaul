package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.CondenserRecipeViewer;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.HX_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.CONDENSER_RECIPE_TYPE;

public class JeiCondenserCategory extends JeiRecipeViewerCategory<CondenserRecipe> {
    protected static final String UID = ncLoc("condenser").getPath();

    public JeiCondenserCategory(IGuiHelper helper) {
        super(helper, UID, CondenserRecipeViewer.class, CONDENSER_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(HX_MAP.get("condenser_controller").get()));
    }
}
