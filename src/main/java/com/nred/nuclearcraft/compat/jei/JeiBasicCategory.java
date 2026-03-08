package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;

public abstract class JeiBasicCategory<RECIPE extends BasicRecipe> implements IRecipeCategory<RECIPE> {
    protected final IGuiHelper helper;
    protected static final Font font = Minecraft.getInstance().font;
    protected RecipeViewerInfo recipeViewerInfo;
    protected final String name;

    public JeiBasicCategory(IGuiHelper helper, String name) {
        this.helper = helper;
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(name);
        this.name = name;
    }

    @Override
    public int getWidth() {
        return recipeViewerInfo.rect().width();
    }

    @Override
    public int getHeight() {
        return recipeViewerInfo.rect().height();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("emi.category." + MODID + "." + name);
    }
}