package com.nred.nuclearcraft.handler;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.util.MinMax.MinMaxInt;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public abstract class MenuInfoBuilder<BUILDER extends MenuInfoBuilder<BUILDER>> {
    public final String name;

    public String recipeHandlerName;

    public int[] guiWH = new int[]{176, 166};

    public List<int[]> itemInputGuiXYWH = new ArrayList<>();
    public List<int[]> fluidInputGuiXYWH = new ArrayList<>();
    public List<int[]> itemOutputGuiXYWH = new ArrayList<>();
    public List<int[]> fluidOutputGuiXYWH = new ArrayList<>();

    public int[] playerGuiXY = new int[]{8, 84};

    public int[] progressBarGuiXYWHUV = new int[]{74, 35, 37, 16, 176, 3};
    public int[] energyBarGuiXYWHUV = new int[]{8, 6, 16, 74, 176, 90};

    public int[] machineConfigGuiXY = new int[]{27, 63};
    public int[] redstoneControlGuiXY = new int[]{47, 63};

    public String recipeViewerTexture;
    public String screenTexture;

    public int[] recipeViewerBackgroundXYWH = new int[]{51, 30, 86, 26};
    public int[] recipeViewerTooltipXYWH = new int[]{73, 34, 38, 18};
    public int[] recipeViewerClickAreaXYWH = new int[]{73, 34, 38, 18};

    protected MenuInfoBuilder(String name) {
        this.name = name;

        if (name.contains(":")) {
            this.screenTexture = ResourceLocation.parse(name).withPrefix("screen/").toString();
        } else {
            ncLoc("screen/" + name);
        }

        setRecipeHandlerName(name);
    }

    @SuppressWarnings("unchecked")
    protected BUILDER getThis() {
        return (BUILDER) this;
    }

    public BUILDER setGuiWH(int w, int h) {
        guiWH = new int[]{w, h};
        return getThis();
    }

    public BUILDER setItemInputSlots(int[]... slots) {
        itemInputGuiXYWH = Lists.newArrayList(slots);
        autoRecipeViewerBackgroundXY();
        return getThis();
    }

    public BUILDER setFluidInputSlots(int[]... slots) {
        fluidInputGuiXYWH = Lists.newArrayList(slots);
        autoRecipeViewerBackgroundXY();
        return getThis();
    }

    public BUILDER setItemOutputSlots(int[]... slots) {
        itemOutputGuiXYWH = Lists.newArrayList(slots);
        autoRecipeViewerBackgroundXY();
        return getThis();
    }

    public BUILDER setFluidOutputSlots(int[]... slots) {
        fluidOutputGuiXYWH = Lists.newArrayList(slots);
        autoRecipeViewerBackgroundXY();
        return getThis();
    }

    public BUILDER setPlayerGuiXY(int x, int y) {
        playerGuiXY = new int[]{x, y};
        return getThis();
    }

    public BUILDER setProgressBarGuiXYWHUV(int x, int y, int w, int h, int u, int v) {
        progressBarGuiXYWHUV = new int[]{x, y, w, h, u, v};
        recipeViewerTooltipXYWH = new int[]{x - 1, y - 1, w + 1, h + 2};
        recipeViewerClickAreaXYWH = new int[]{x - 1, y - 1, w + 1, h + 2};
        return getThis();
    }

    public BUILDER setEnergyBarGuiXYWHUV(int x, int y, int w, int h, int u, int v) {
        energyBarGuiXYWHUV = new int[]{x, y, w, h, u, v};
        return getThis();
    }

    public BUILDER setMachineConfigGuiXY(int x, int y) {
        machineConfigGuiXY = new int[]{x, y};
        return getThis();
    }

    public BUILDER setRedstoneControlGuiXY(int x, int y) {
        redstoneControlGuiXY = new int[]{x, y};
        return getThis();
    }

    public BUILDER setRecipeHandlerName(String recipeHandlerName) {
        this.recipeHandlerName = recipeHandlerName;

        if (recipeHandlerName.contains(":")) {
            recipeViewerTexture = ResourceLocation.parse(recipeHandlerName).withPrefix("textures/gui/sprites/screen/") + ".png";
        } else {
            recipeViewerTexture = MODID + ":textures/gui/sprites/screen/" + recipeHandlerName + ".png";
        }
        return getThis();
    }

    public BUILDER setRecipeViewerTexture(String recipeViewerTexture) {
        this.recipeViewerTexture = recipeViewerTexture;
        return getThis();
    }

    public BUILDER setScreenTexture(String screenTexture) {
        this.screenTexture = screenTexture;
        return getThis();
    }

    public BUILDER setRecipeViewerBackgroundXYWH(int x, int y, int w, int h) {
        recipeViewerBackgroundXYWH = new int[]{x, y, w, h};
        return getThis();
    }

    public BUILDER setRecipeViewerTooltipXYWH(int x, int y, int w, int h) {
        recipeViewerTooltipXYWH = new int[]{x, y, w, h};
        return getThis();
    }

    public BUILDER setRecipeViewerClickAreaXYWH(int x, int y, int w, int h) {
        recipeViewerClickAreaXYWH = new int[]{x, y, w, h};
        return getThis();
    }

    public BUILDER standardExtend(int x, int y) {
        guiWH[0] += x;
        guiWH[1] += y;

        playerGuiXY[0] += x;
        playerGuiXY[1] += y;

        energyBarGuiXYWHUV[3] += y;

        machineConfigGuiXY[0] += x;
        machineConfigGuiXY[1] += y;

        redstoneControlGuiXY[0] += x;
        redstoneControlGuiXY[1] += y;

        return getThis();
    }

    public BUILDER disableProgressBar() {
        progressBarGuiXYWHUV = new int[]{-1, -1, -1, -1, -1, -1};
        recipeViewerTooltipXYWH = new int[]{-1, -1, -1, -1};
        recipeViewerClickAreaXYWH = new int[]{-1, -1, -1, -1};
        return getThis();
    }

    protected void autoRecipeViewerBackgroundXY() {
        MinMaxInt minMaxCenterX = new MinMaxInt(), minMaxCenterY = new MinMaxInt();
        for (List<int[]> xywhList : Arrays.asList(itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH)) {
            for (int[] xywh : xywhList) {
                minMaxCenterX.update(xywh[0] - 4);
                minMaxCenterY.update(xywh[1] - 4);
                minMaxCenterX.update(xywh[0] + xywh[2] + 4);
                minMaxCenterY.update(xywh[1] + xywh[3] + 4);
            }
        }

        recipeViewerBackgroundXYWH = new int[]{minMaxCenterX.getMin(), minMaxCenterY.getMin(), minMaxCenterX.getDiff(), minMaxCenterY.getDiff()};
    }
}