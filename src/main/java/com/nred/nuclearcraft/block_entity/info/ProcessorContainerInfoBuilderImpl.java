package com.nred.nuclearcraft.block_entity.info;

import com.nred.nuclearcraft.block.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.info.ProcessorContainerInfoImpl.BasicProcessorContainerInfo;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ProcessorContainerInfoBuilderImpl {
    public static class BasicProcessorContainerInfoBuilder<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ProcessorContainerInfoBuilder<TILE, PACKET, BasicProcessorContainerInfo<TILE, PACKET>, BasicProcessorContainerInfoBuilder<TILE, PACKET>> {
        public BasicProcessorContainerInfoBuilder(String name, Class<TILE> tileClass) {
            super(name, tileClass);
        }

        @Override
        public BasicProcessorContainerInfo<TILE, PACKET> buildContainerInfo() {
            return new BasicProcessorContainerInfo<>(name, tileClass, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
        }
    }
//TODO
//	public static class BasicUpgradableProcessorContainerInfoBuilder<TILE extends TileEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends UpgradableProcessorContainerInfoBuilder<TILE, PACKET, BasicUpgradableProcessorContainerInfo<TILE, PACKET>, BasicUpgradableProcessorContainerInfoBuilder<TILE, PACKET>> {
//
//		public BasicUpgradableProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TILE> guiFunction) {
//			super(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, guiFunction);
//		}
//
//		@Override
//		public BasicUpgradableProcessorContainerInfo<TILE, PACKET> buildContainerInfo() {
//			return new BasicUpgradableProcessorContainerInfo<>(modId, name, tileClass, containerClass, containerFunction, guiClass, guiFunction, configContainerFunction, configGuiFunction, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH, speedUpgradeGuiXYWH, energyUpgradeGuiXYWH);
//		}
//	}
}
