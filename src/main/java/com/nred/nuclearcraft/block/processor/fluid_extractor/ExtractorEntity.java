package com.nred.nuclearcraft.block.processor.fluid_extractor;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.ExtractorMenu;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class ExtractorEntity extends ProcessorEntity {
    public ExtractorEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "fluid_extractor", new HandlerInfo(2, 1, 1, 0));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ExtractorMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "fluid_extractor"), this.progressSlot);
    }
}