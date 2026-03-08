package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockDistillerRecipeViewer;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
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

public class JeiMultiblockDistillerCategory extends JeiRecipeViewerCategory<MultiblockDistillerRecipe> {
    protected static final ResourceLocation UID = ncLoc("multiblock_distiller");
    private static final RecipeType<MultiblockDistillerRecipe> TYPE = new RecipeType<>(UID, MultiblockDistillerRecipe.class);

    public JeiMultiblockDistillerCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), MultiblockDistillerRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<MultiblockDistillerRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MACHINE_MAP.get("distiller_controller").get()));
    }
}