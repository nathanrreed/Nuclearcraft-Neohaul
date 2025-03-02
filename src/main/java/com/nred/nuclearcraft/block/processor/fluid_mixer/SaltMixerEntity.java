package com.nred.nuclearcraft.block.processor.fluid_mixer;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import com.nred.nuclearcraft.menu.SaltMixerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class SaltMixerEntity extends ProcessorEntity {
    public SaltMixerEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "fluid_mixer", new HandlerInfo(0, 3, 0, 2));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SaltMixerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "fluid_mixer"), this.progressSlot);
    }
}