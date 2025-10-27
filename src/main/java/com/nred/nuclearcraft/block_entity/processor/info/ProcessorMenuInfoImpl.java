package com.nred.nuclearcraft.block_entity.processor.info;

import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.menu.MenuFunction;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.function.Supplier;

public class ProcessorMenuInfoImpl {
    public static class BasicProcessorMenuInfo<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ProcessorMenuInfo<TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET>> {
        public BasicProcessorMenuInfo(String name, Class<TILE> tileClass, MenuFunction<TILE> menuFunction, String recipeHandlerName, int inputTankCapacity, int outputTankCapacity, Supplier<Integer> defaultProcessTime, Supplier<Integer> defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, String ocComponentName, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiCategoryEnabled, String jeiCategoryUid, String jeiTitle, String jeiTexture, int[] jeiBackgroundXYWH, int[] jeiTooltipXYWH, int[] jeiClickAreaXYWH) {
            super(name, tileClass, menuFunction, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
        }
    }

    public static class BasicUpgradableProcessorMenuInfo<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends UpgradableProcessorMenuInfo<TILE, PACKET, BasicUpgradableProcessorMenuInfo<TILE, PACKET>> {
        public BasicUpgradableProcessorMenuInfo(String name, Class<TILE> tileClass, MenuFunction<TILE> menuFunction, String recipeHandlerName, int inputTankCapacity, int outputTankCapacity, Supplier<Integer> defaultProcessTime, Supplier<Integer> defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, String ocComponentName, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiCategoryEnabled, String jeiCategoryUid, String jeiTitle, String jeiTexture, int[] jeiBackgroundXYWH, int[] jeiTooltipXYWH, int[] jeiClickAreaXYWH, int[] speedUpgradeGuiXYWH, int[] energyUpgradeGuiXYWH) {
            super(name, tileClass, menuFunction, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH, speedUpgradeGuiXYWH, energyUpgradeGuiXYWH);
        }
    }
}