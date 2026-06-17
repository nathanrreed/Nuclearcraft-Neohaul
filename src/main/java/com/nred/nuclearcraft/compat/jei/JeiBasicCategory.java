package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.info.RecipeViewerCategoryInfo;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.handler.BlockEntityInfoHandler.RECIPE_VIEWER_CATEGORY_INFO_MAP;

public abstract class JeiBasicCategory<T> implements IRecipeCategory<T> {
    protected final IGuiHelper helper;
    protected static final Font font = Minecraft.getInstance().font;
    public final RecipeViewerCategoryInfo categoryInfo;
    protected final String name;

    public JeiBasicCategory(IGuiHelper helper, String name) {
        this.helper = helper;
        this.categoryInfo = RECIPE_VIEWER_CATEGORY_INFO_MAP.get(name);
        this.name = name;
    }

    @Override
    public int getWidth() {
        return categoryInfo.getRecipeViewerBackgroundW();
    }

    @Override
    public int getHeight() {
        return categoryInfo.getRecipeViewerBackgroundH();
    }

    @Override
    public Component getTitle() {
        if (name.contains(":")) {
            return Component.translatable("emi.category." + name.replace(':', '.'));
        } else {
            return Component.translatable("emi.category." + MODID + "." + name);
        }
    }
}