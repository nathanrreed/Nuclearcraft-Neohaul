package com.nred.nuclearcraft.compat.recipe_viewer.info;

import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public abstract class RecipeViewerCategoryInfo {
    public final String name;

    public final List<? extends ItemLike> crafters;

    protected RecipeViewerCategoryInfo(String name, List<? extends ItemLike> crafters) {
        this.name = name;
        this.crafters = crafters;
    }

    public String getName() {
        return name;
    }

    public abstract int getItemInputSize();

    public abstract int getFluidInputSize();

    public abstract int getItemOutputSize();

    public abstract int getFluidOutputSize();

    public abstract List<int[]> getFluidInputGuiXYWH();

    public abstract List<int[]> getFluidOutputGuiXYWH();

    public abstract List<int[]> getItemInputStackXY();

    public abstract List<int[]> getItemOutputStackXY();

    public abstract BasicRecipeHandler<?> getRecipeHandler();

    public abstract String getRecipeViewerTexture();

    public abstract String getScreenTexture();

    public abstract int getRecipeViewerBackgroundX();

    public abstract int getRecipeViewerBackgroundY();

    public abstract int getRecipeViewerBackgroundW();

    public abstract int getRecipeViewerBackgroundH();

    public abstract int getRecipeViewerTooltipW();

    public abstract int getRecipeViewerTooltipH();

    public abstract int getProgressBarGuiX();

    public abstract int getProgressBarGuiY();

    public abstract int getProgressBarGuiW();

    public abstract int getProgressBarGuiH();

    public abstract int getProgressBarGuiU();

    public abstract int getProgressBarGuiV();
}