package com.nred.nuclearcraft.block.processor.assembler;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.processor.AssemblerMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class AssemblerEntity extends ProcessorEntity {
    public AssemblerEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "assembler", new HandlerInfo(5, 0, 4, 0));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AssemblerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "assembler"), this.progressSlot);
    }
}