package com.nred.nuclearcraft.block.processor.chemical_reactor;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.menu.ChemicalReactorMenu;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

public class ChemicalReactorEntity extends ProcessorEntity {
    public ChemicalReactorEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "chemical_reactor", 2, 4);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ChemicalReactorMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, "chemical_reactor"), this.progressSlot);
    }
}