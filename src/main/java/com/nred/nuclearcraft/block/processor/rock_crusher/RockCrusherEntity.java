package com.nred.nuclearcraft.block.processor.rock_crusher;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import com.nred.nuclearcraft.menu.RockCrusherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class RockCrusherEntity extends ProcessorEntity {
    public RockCrusherEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "rock_crusher", 6);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RockCrusherMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, "rock_crusher"), this.progressSlot);
    }
}