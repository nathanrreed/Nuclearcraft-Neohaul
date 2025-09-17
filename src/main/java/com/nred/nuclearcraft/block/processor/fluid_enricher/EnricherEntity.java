package com.nred.nuclearcraft.block.processor.fluid_enricher;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.processor.EnricherMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class EnricherEntity extends ProcessorEntity {
    public EnricherEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "fluid_enricher", new HandlerInfo(1, 2, 1, 1));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new EnricherMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "fluid_enricher"), this.progressSlot);
    }
}