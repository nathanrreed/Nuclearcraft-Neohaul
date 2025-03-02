package com.nred.nuclearcraft.block.processor.ingot_former;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.IngotFormerMenu;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class IngotFormerEntity extends ProcessorEntity {
    public IngotFormerEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "ingot_former", new HandlerInfo(1, 1, 1, 1));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new IngotFormerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "ingot_former"), this.progressSlot);
    }
}