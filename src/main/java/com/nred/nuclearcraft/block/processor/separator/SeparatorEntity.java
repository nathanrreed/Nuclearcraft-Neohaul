package com.nred.nuclearcraft.block.processor.separator;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import com.nred.nuclearcraft.menu.SeparatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class SeparatorEntity extends ProcessorEntity {
    public SeparatorEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "separator", new HandlerInfo(3, 0, 1, 0));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SeparatorMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, "separator"), this.progressSlot);
    }
}