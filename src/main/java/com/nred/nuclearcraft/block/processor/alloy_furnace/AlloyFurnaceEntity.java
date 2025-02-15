package com.nred.nuclearcraft.block.processor.alloy_furnace;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.AlloyFurnaceMenu;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class AlloyFurnaceEntity extends ProcessorEntity {
    public AlloyFurnaceEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "alloy_furnace", new HandlerInfo(3, 0, 2, 0));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AlloyFurnaceMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, "alloy_furnace"), this.progressSlot);
    }
}