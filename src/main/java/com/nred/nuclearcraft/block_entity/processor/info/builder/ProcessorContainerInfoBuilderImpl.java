package com.nred.nuclearcraft.block_entity.processor.info.builder;

import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorContainerInfoImpl.BasicProcessorContainerInfo;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorContainerInfoImpl.BasicUpgradableProcessorContainerInfo;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class ProcessorContainerInfoBuilderImpl {
    public static class BasicProcessorContainerInfoBuilder<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ProcessorContainerInfoBuilder<TILE, PACKET, BasicProcessorContainerInfo<TILE, PACKET>, BasicProcessorContainerInfoBuilder<TILE, PACKET>> {
        public BasicProcessorContainerInfoBuilder(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier) {
            super(name, tileClass, tileSupplier);
        }

        @Override
        public BasicProcessorContainerInfo<TILE, PACKET> buildContainerInfo() {
            return new BasicProcessorContainerInfo<>(name, tileClass, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
        }
    }

    public static class BasicUpgradableProcessorContainerInfoBuilder<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends UpgradableProcessorContainerInfoBuilder<TILE, PACKET, BasicUpgradableProcessorContainerInfo<TILE, PACKET>, BasicUpgradableProcessorContainerInfoBuilder<TILE, PACKET>> {
        public BasicUpgradableProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier) {
            super(name, tileClass, tileSupplier);
        }

        @Override
        public BasicUpgradableProcessorContainerInfo<TILE, PACKET> buildContainerInfo() {
            return new BasicUpgradableProcessorContainerInfo<>(name, tileClass, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH, speedUpgradeGuiXYWH, energyUpgradeGuiXYWH);
        }
    }
}