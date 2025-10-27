package com.nred.nuclearcraft.block_entity.processor.info.builder;

import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.menu.MenuFunction;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class ProcessorContainerInfoBuilderImpl {
    public static class BasicProcessorContainerInfoBuilder<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ProcessorContainerInfoBuilder<TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET>, BasicProcessorContainerInfoBuilder<TILE, PACKET>> {
        public BasicProcessorContainerInfoBuilder(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier, MenuFunction<TILE> menuFunction) {
            super(name, tileClass, tileSupplier, menuFunction);
        }

        @Override
        public BasicProcessorMenuInfo<TILE, PACKET> buildContainerInfo() {
            return new BasicProcessorMenuInfo<>(name, tileClass, menuFunction, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
        }
    }

    public static class BasicUpgradableProcessorContainerInfoBuilder<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends UpgradableProcessorContainerInfoBuilder<TILE, PACKET, BasicUpgradableProcessorMenuInfo<TILE, PACKET>, BasicUpgradableProcessorContainerInfoBuilder<TILE, PACKET>> {
        public BasicUpgradableProcessorContainerInfoBuilder(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier, MenuFunction<TILE> menuFunction) {
            super(name, tileClass, tileSupplier, menuFunction);
        }

        @Override
        public BasicUpgradableProcessorMenuInfo<TILE, PACKET> buildContainerInfo() {
            return new BasicUpgradableProcessorMenuInfo<>(name, tileClass, menuFunction, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH, speedUpgradeGuiXYWH, energyUpgradeGuiXYWH);
        }
    }
}