package com.nred.nuclearcraft.block_entity.processor.info.builder;

import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.UpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.menu.MenuFunction;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.ContainerInfoHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public abstract class UpgradableProcessorContainerInfoBuilder<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends UpgradableProcessorMenuInfo<TILE, PACKET, INFO>, BUILDER extends UpgradableProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER>> extends ProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER> {
    protected int[] speedUpgradeGuiXYWH = ContainerInfoHelper.standardSlot(132, 64);
    protected int[] energyUpgradeGuiXYWH = ContainerInfoHelper.standardSlot(152, 64);

    protected UpgradableProcessorContainerInfoBuilder(String name, Class<TILE> menuClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier, MenuFunction<TILE> menuFunction) {
        super(name, menuClass, tileSupplier, menuFunction);
    }

    public BUILDER setSpeedUpgradeSlot(int x, int y, int w, int h) {
        speedUpgradeGuiXYWH = new int[]{x, y, w, h};
        return getThis();
    }

    public BUILDER setEnergyUpgradeSlot(int x, int y, int w, int h) {
        energyUpgradeGuiXYWH = new int[]{x, y, w, h};
        return getThis();
    }

    @Override
    public BUILDER standardExtend(int x, int y) {
        super.standardExtend(x, y);

        speedUpgradeGuiXYWH[0] += x;
        speedUpgradeGuiXYWH[1] += y;

        energyUpgradeGuiXYWH[0] += x;
        energyUpgradeGuiXYWH[1] += y;

        return getThis();
    }
}