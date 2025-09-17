package com.nred.nuclearcraft.block.processor.electrolyzer;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.processor.ElectrolyzerMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class ElectrolyzerEntity extends ProcessorEntity {
    public ElectrolyzerEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "electrolyzer", new HandlerInfo(0, 5, 0, 1));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ElectrolyzerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "electrolyzer"), this.progressSlot);
    }
}