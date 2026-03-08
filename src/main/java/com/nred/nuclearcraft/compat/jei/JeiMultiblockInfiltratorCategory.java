package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockInfiltratorRecipeViewer;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
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

public class JeiMultiblockInfiltratorCategory extends JeiRecipeViewerCategory<MultiblockInfiltratorRecipe> {
    protected static final ResourceLocation UID = ncLoc("multiblock_infiltrator");
    private static final RecipeType<MultiblockInfiltratorRecipe> TYPE = new RecipeType<>(UID, MultiblockInfiltratorRecipe.class);

    public JeiMultiblockInfiltratorCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), MultiblockInfiltratorRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<MultiblockInfiltratorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MACHINE_MAP.get("infiltrator_controller").get()));
    }
}