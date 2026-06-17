package com.nred.nuclearcraft.compat.recipe_viewer.info;

import com.nred.nuclearcraft.compat.recipe_viewer.info.builder.RecipeViewerSimpleCategoryInfoBuilder;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.util.CollectionHelper;
import com.nred.nuclearcraft.util.ContainerInfoHelper;

import java.util.List;

public class RecipeViewerSimpleCategoryInfo<RECIPE extends BasicRecipe> extends RecipeViewerCategoryInfo {
    public final int itemInputSize;
    public final int fluidInputSize;
    public final int itemOutputSize;
    public final int fluidOutputSize;

    public final int[] itemInputSlots;
    public final int[] itemOutputSlots;

    public final int[] fluidInputTanks;
    public final int[] fluidOutputTanks;

    public final int guiWidth;
    public final int guiHeight;

    public final List<int[]> itemInputGuiXYWH;
    public final List<int[]> fluidInputGuiXYWH;
    public final List<int[]> itemOutputGuiXYWH;
    public final List<int[]> fluidOutputGuiXYWH;

    public final List<int[]> itemInputStackXY;
    public final List<int[]> itemOutputStackXY;

    public final int playerGuiX;
    public final int playerGuiY;

    public final int progressBarGuiX;
    public final int progressBarGuiY;
    public final int progressBarGuiW;
    public final int progressBarGuiH;
    public final int progressBarGuiU;
    public final int progressBarGuiV;

    public final String jeiTexture;
    public final String screenTexture;

    public final int jeiBackgroundX;
    public final int jeiBackgroundY;
    public final int jeiBackgroundW;
    public final int jeiBackgroundH;

    public final int jeiTooltipX;
    public final int jeiTooltipY;
    public final int jeiTooltipW;
    public final int jeiTooltipH;

    public RecipeViewerSimpleCategoryInfo(RecipeViewerSimpleCategoryInfoBuilder<RECIPE> builder) {
        super(builder.name, builder.crafters);

        itemInputSize = builder.itemInputGuiXYWH.size();
        fluidInputSize = builder.fluidInputGuiXYWH.size();
        itemOutputSize = builder.itemOutputGuiXYWH.size();
        fluidOutputSize = builder.fluidOutputGuiXYWH.size();

        itemInputSlots = CollectionHelper.increasingArray(itemInputSize);
        itemOutputSlots = CollectionHelper.increasingArray(itemInputSize, itemOutputSize);

        fluidInputTanks = CollectionHelper.increasingArray(fluidInputSize);
        fluidOutputTanks = CollectionHelper.increasingArray(fluidInputSize, fluidOutputSize);

        guiWidth = builder.guiWH[0];
        guiHeight = builder.guiWH[1];

        itemInputGuiXYWH = builder.itemInputGuiXYWH;
        fluidInputGuiXYWH = builder.fluidInputGuiXYWH;
        itemOutputGuiXYWH = builder.itemOutputGuiXYWH;
        fluidOutputGuiXYWH = builder.fluidOutputGuiXYWH;

        itemInputStackXY = ContainerInfoHelper.stackXYList(itemInputGuiXYWH);
        itemOutputStackXY = ContainerInfoHelper.stackXYList(itemOutputGuiXYWH);

        playerGuiX = builder.playerGuiXY[0];
        playerGuiY = builder.playerGuiXY[1];

        progressBarGuiX = builder.progressBarGuiXYWHUV[0];
        progressBarGuiY = builder.progressBarGuiXYWHUV[1];
        progressBarGuiW = builder.progressBarGuiXYWHUV[2];
        progressBarGuiH = builder.progressBarGuiXYWHUV[3];
        progressBarGuiU = builder.progressBarGuiXYWHUV[4];
        progressBarGuiV = builder.progressBarGuiXYWHUV[5];

        jeiTexture = builder.recipeViewerTexture;
        screenTexture = builder.screenTexture;

        jeiBackgroundX = builder.recipeViewerBackgroundXYWH[0];
        jeiBackgroundY = builder.recipeViewerBackgroundXYWH[1];
        jeiBackgroundW = builder.recipeViewerBackgroundXYWH[2];
        jeiBackgroundH = builder.recipeViewerBackgroundXYWH[3];

        jeiTooltipX = builder.recipeViewerTooltipXYWH[0];
        jeiTooltipY = builder.recipeViewerTooltipXYWH[1];
        jeiTooltipW = builder.recipeViewerTooltipXYWH[2];
        jeiTooltipH = builder.recipeViewerTooltipXYWH[3];
    }

    @Override
    public int getItemInputSize() {
        return itemInputSize;
    }

    @Override
    public int getFluidInputSize() {
        return fluidInputSize;
    }

    @Override
    public int getItemOutputSize() {
        return itemOutputSize;
    }

    @Override
    public int getFluidOutputSize() {
        return fluidOutputSize;
    }

    @Override
    public List<int[]> getFluidInputGuiXYWH() {
        return fluidInputGuiXYWH;
    }

    @Override
    public List<int[]> getFluidOutputGuiXYWH() {
        return fluidOutputGuiXYWH;
    }

    @Override
    public List<int[]> getItemInputStackXY() {
        return itemInputStackXY;
    }

    @Override
    public List<int[]> getItemOutputStackXY() {
        return itemOutputStackXY;
    }

    @Override
    public BasicRecipeHandler<RECIPE> getRecipeHandler() {
        return NCRecipes.getHandler(getName());
    }

    @Override
    public String getRecipeViewerTexture() {
        return jeiTexture;
    }

    @Override
    public String getScreenTexture() {
        return screenTexture;
    }

    @Override
    public int getRecipeViewerBackgroundX() {
        return jeiBackgroundX;
    }

    @Override
    public int getRecipeViewerBackgroundY() {
        return jeiBackgroundY;
    }

    @Override
    public int getRecipeViewerBackgroundW() {
        return jeiBackgroundW;
    }

    @Override
    public int getRecipeViewerBackgroundH() {
        return jeiBackgroundH;
    }

    @Override
    public int getRecipeViewerTooltipW() {
        return jeiTooltipW;
    }

    @Override
    public int getRecipeViewerTooltipH() {
        return jeiTooltipH;
    }

    @Override
    public int getProgressBarGuiX() {
        return progressBarGuiX;
    }

    @Override
    public int getProgressBarGuiY() {
        return progressBarGuiY;
    }

    @Override
    public int getProgressBarGuiW() {
        return progressBarGuiW;
    }

    @Override
    public int getProgressBarGuiH() {
        return progressBarGuiH;
    }

    @Override
    public int getProgressBarGuiU() {
        return progressBarGuiU;
    }

    @Override
    public int getProgressBarGuiV() {
        return progressBarGuiV;
    }
}