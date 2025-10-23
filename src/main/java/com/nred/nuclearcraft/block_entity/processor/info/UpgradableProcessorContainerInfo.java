package com.nred.nuclearcraft.block_entity.processor.info;

import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.ContainerInfoHelper;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public abstract class UpgradableProcessorContainerInfo<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends UpgradableProcessorContainerInfo<TILE, PACKET, INFO>> extends ProcessorContainerInfo<TILE, PACKET, INFO> {
    public final int speedUpgradeSlot;
    public final int energyUpgradeSlot;

    public final int[] speedUpgradeGuiXYWH;
    public final int[] energyUpgradeGuiXYWH;

    public final int[] speedUpgradeStackXY;
    public final int[] energyUpgradeStackXY;

    public final int speedUpgradeSorptionButtonID;
    public final int energyUpgradeSorptionButtonID;

    protected UpgradableProcessorContainerInfo(String name, Class<TILE> tileClass, String recipeHandlerName, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, String ocComponentName, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiCategoryEnabled, String jeiCategoryUid, String jeiTitle, String jeiTexture, int[] jeiBackgroundXYWH, int[] jeiTooltipXYWH, int[] jeiClickAreaXYWH, int[] speedUpgradeGuiXYWH, int[] energyUpgradeGuiXYWH) {
        super(name, tileClass, recipeHandlerName, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);

        speedUpgradeSlot = itemInputSize + itemOutputSize;
        energyUpgradeSlot = speedUpgradeSlot + 1;

        this.speedUpgradeGuiXYWH = speedUpgradeGuiXYWH;
        this.energyUpgradeGuiXYWH = energyUpgradeGuiXYWH;

        speedUpgradeStackXY = ContainerInfoHelper.stackXY(speedUpgradeGuiXYWH);
        energyUpgradeStackXY = ContainerInfoHelper.stackXY(energyUpgradeGuiXYWH);

        speedUpgradeSorptionButtonID = itemInputSize + fluidInputSize + itemOutputSize + fluidOutputSize;
        energyUpgradeSorptionButtonID = speedUpgradeSorptionButtonID + 1;
    }

    @Override
    public int getInventorySize() {
        return itemInputSize + itemOutputSize + 2;
    }

    @Override
    public List<ItemSorption> defaultItemSorptions() {
        List<ItemSorption> itemSorptions = super.defaultItemSorptions();
        itemSorptions.add(ItemSorption.IN);
        itemSorptions.add(ItemSorption.IN);
        return itemSorptions;
    }
}