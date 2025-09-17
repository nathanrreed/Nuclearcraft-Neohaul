package com.nred.nuclearcraft.block.processor.fluid_infuser;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.processor.InfuserMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class InfuserEntity extends ProcessorEntity {
    public InfuserEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "fluid_infuser", new HandlerInfo(2, 1, 1, 1));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new InfuserMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "fluid_infuser"), this.progressSlot);
    }
}