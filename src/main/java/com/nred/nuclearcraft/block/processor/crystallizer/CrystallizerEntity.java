package com.nred.nuclearcraft.block.processor.crystallizer;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.menu.CrystallizerMenu;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class CrystallizerEntity extends ProcessorEntity {
    public CrystallizerEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "crystallizer", 3, 1);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CrystallizerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, "crystallizer"), this.progressSlot);
    }
}