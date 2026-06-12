package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockDistillerRecipeViewer;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.MACHINE_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MULTIBLOCK_DISTILLER_RECIPE_TYPE;

public class JeiMultiblockDistillerCategory extends JeiRecipeViewerCategory<MultiblockDistillerRecipe> {
    protected static final String UID = ncLoc("multiblock_distiller").getPath();

    public JeiMultiblockDistillerCategory(IGuiHelper helper) {
        super(helper, UID, MultiblockDistillerRecipeViewer.class, MULTIBLOCK_DISTILLER_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MACHINE_MAP.get("distiller_controller").get()));
    }
}
