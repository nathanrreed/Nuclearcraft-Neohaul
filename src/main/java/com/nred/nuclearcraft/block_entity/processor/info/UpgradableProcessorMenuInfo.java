package com.nred.nuclearcraft.block_entity.processor.info;

import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.builder.UpgradableProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.ContainerInfoHelper;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public abstract class UpgradableProcessorMenuInfo<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends UpgradableProcessorMenuInfo<TILE, PACKET, INFO>> extends ProcessorMenuInfo<TILE, PACKET, INFO> {
    public final int speedUpgradeSlot;
    public final int energyUpgradeSlot;

    public final int[] speedUpgradeGuiXYWH;
    public final int[] energyUpgradeGuiXYWH;

    public final int[] speedUpgradeStackXY;
    public final int[] energyUpgradeStackXY;

    public final int speedUpgradeSorptionButtonID;
    public final int energyUpgradeSorptionButtonID;

    protected UpgradableProcessorMenuInfo(UpgradableProcessorContainerInfoBuilder<TILE, PACKET, INFO, ?> builder) {
        super(builder);

        speedUpgradeSlot = itemInputSize + itemOutputSize;
        energyUpgradeSlot = speedUpgradeSlot + 1;

        this.speedUpgradeGuiXYWH = builder.speedUpgradeGuiXYWH;
        this.energyUpgradeGuiXYWH = builder.energyUpgradeGuiXYWH;

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