package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockElectrolyzerRecipeViewer;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.MACHINE_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE;

public class JeiMultiblockElectrolyzerCategory extends JeiRecipeViewerCategory<MultiblockElectrolyzerRecipe> {
    protected static final String UID = ncLoc("multiblock_electrolyzer").getPath();

    public JeiMultiblockElectrolyzerCategory(IGuiHelper helper) {
        super(helper, UID, MultiblockElectrolyzerRecipeViewer.class, MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE.get());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MACHINE_MAP.get("electrolyzer_controller").get()));
    }
}
