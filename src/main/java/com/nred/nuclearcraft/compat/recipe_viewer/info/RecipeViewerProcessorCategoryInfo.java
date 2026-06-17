package com.nred.nuclearcraft.compat.recipe_viewer.info;

import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.BlockEntityInfoHandler;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class RecipeViewerProcessorCategoryInfo<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO, RECIPE>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO, RECIPE>, RECIPE extends BasicRecipe> extends RecipeViewerCategoryInfo {
    public final INFO containerInfo;

    public RecipeViewerProcessorCategoryInfo(String name, List<ItemLike> crafters) {
        this(BlockEntityInfoHandler.<TILE, PACKET, INFO, RECIPE>getProcessorMenuInfo(name), crafters);
    }

    private RecipeViewerProcessorCategoryInfo(INFO containerInfo, List<ItemLike> crafters) {
        super(containerInfo.recipeHandlerName, crafters);

        this.containerInfo = containerInfo;
    }

    @Override
    public int getItemInputSize() {
        return containerInfo.itemInputSize;
    }

    @Override
    public int getFluidInputSize() {
        return containerInfo.fluidInputSize;
    }

    @Override
    public int getItemOutputSize() {
        return containerInfo.itemOutputSize;
    }

    @Override
    public int getFluidOutputSize() {
        return containerInfo.fluidOutputSize;
    }

    @Override
    public List<int[]> getFluidInputGuiXYWH() {
        return containerInfo.fluidInputGuiXYWH;
    }

    @Override
    public List<int[]> getFluidOutputGuiXYWH() {
        return containerInfo.fluidOutputGuiXYWH;
    }

    @Override
    public List<int[]> getItemInputStackXY() {
        return containerInfo.itemInputStackXY;
    }

    @Override
    public List<int[]> getItemOutputStackXY() {
        return containerInfo.itemOutputStackXY;
    }

    @Override
    public BasicRecipeHandler<RECIPE> getRecipeHandler() {
        return containerInfo.getRecipeHandler();
    }

    @Override
    public String getRecipeViewerTexture() {
        return containerInfo.recipeViewerTexture;
    }

    @Override
    public String getScreenTexture() {
        return containerInfo.screenTexture;
    }

    @Override
    public int getRecipeViewerBackgroundX() {
        return containerInfo.recipeViewerBackgroundX;
    }

    @Override
    public int getRecipeViewerBackgroundY() {
        return containerInfo.recipeViewerBackgroundY;
    }

    @Override
    public int getRecipeViewerBackgroundW() {
        return containerInfo.recipeViewerBackgroundW;
    }

    @Override
    public int getRecipeViewerBackgroundH() {
        return containerInfo.recipeViewerBackgroundH;
    }

    @Override
    public int getRecipeViewerTooltipW() {
        return containerInfo.jeiTooltipW;
    }

    @Override
    public int getRecipeViewerTooltipH() {
        return containerInfo.jeiTooltipH;
    }

    @Override
    public int getProgressBarGuiX() {
        return containerInfo.progressBarGuiX;
    }

    @Override
    public int getProgressBarGuiY() {
        return containerInfo.progressBarGuiY;
    }

    @Override
    public int getProgressBarGuiW() {
        return containerInfo.progressBarGuiW;
    }

    @Override
    public int getProgressBarGuiH() {
        return containerInfo.progressBarGuiH;
    }

    @Override
    public int getProgressBarGuiU() {
        return containerInfo.progressBarGuiU;
    }

    @Override
    public int getProgressBarGuiV() {
        return containerInfo.progressBarGuiV;
    }
}