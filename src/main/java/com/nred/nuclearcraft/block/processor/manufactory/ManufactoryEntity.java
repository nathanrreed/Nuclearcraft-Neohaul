package com.nred.nuclearcraft.block.processor.manufactory;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.ManufactoryMenu;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class ManufactoryEntity extends ProcessorEntity {
    public ManufactoryEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "manufactory", new HandlerInfo(2, 0, 1, 0));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ManufactoryMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "manufactory"), this.progressSlot);
    }
}