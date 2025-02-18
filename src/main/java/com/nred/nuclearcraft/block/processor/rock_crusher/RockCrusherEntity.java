package com.nred.nuclearcraft.block.processor.rock_crusher;

import com.ibm.icu.impl.Pair;
import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import com.nred.nuclearcraft.menu.RockCrusherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.probabilityUnpacker;
import static com.nred.nuclearcraft.menu.ProcessorMenu.ENERGY;

public class RockCrusherEntity extends ProcessorEntity {
    public RockCrusherEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "rock_crusher", new HandlerInfo(4, 0, 1, 0));
    }

    @Override
    public void recipeOutputs() {
        for (int i = 0; i < recipe.value().itemResults.size(); i++) {
            ItemStack temp = recipe.value().itemResults.get(i).getItems()[0].copy();
            Pair<Short, Short> probability = probabilityUnpacker(temp.getCount());

            if (RandomSource.create().nextInt(0, 100) <= probability.first) {
                itemStackHandler.internalInsertItem(i + ENERGY + 2, temp.copyWithCount(probability.second), false);
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RockCrusherMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, "rock_crusher"), this.progressSlot);
    }
}