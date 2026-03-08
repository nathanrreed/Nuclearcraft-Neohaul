package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockElectrolyzerRecipeViewer;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.MACHINE_MAP;

public class JeiMultiblockElectrolyzerCategory extends JeiRecipeViewerCategory<MultiblockElectrolyzerRecipe> {
    protected static final ResourceLocation UID = ncLoc("multiblock_electrolyzer");
    private static final RecipeType<MultiblockElectrolyzerRecipe> TYPE = new RecipeType<>(UID, MultiblockElectrolyzerRecipe.class);

    public JeiMultiblockElectrolyzerCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), MultiblockElectrolyzerRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<MultiblockElectrolyzerRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MACHINE_MAP.get("electrolyzer_controller").get()));
    }
}