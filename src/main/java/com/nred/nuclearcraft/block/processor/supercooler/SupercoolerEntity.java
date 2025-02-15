package com.nred.nuclearcraft.block.processor.supercooler;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import com.nred.nuclearcraft.menu.SupercoolerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class SupercoolerEntity extends ProcessorEntity {
    public SupercoolerEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "supercooler", new HandlerInfo(0, 2, 0, 1));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SupercoolerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, "supercooler"), this.progressSlot);
    }
}