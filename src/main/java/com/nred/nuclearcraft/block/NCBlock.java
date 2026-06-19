package com.nred.nuclearcraft.block;

import com.nred.nuclearcraft.block_entity.ITile;
import com.nred.nuclearcraft.util.NCInventoryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.UnaryOperator;

public class NCBlock extends Block {
    protected static boolean keepInventory;

    public NCBlock(UnaryOperator<Properties> properties) {
        super(properties.apply(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0f, 15.0f)));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (this instanceof EntityBlock) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof ITile t) {
                t.onBlockNeighborChanged(state, level, pos, neighborPos);
            }
        }
    }

    // Inventory

    public void dropItems(Level level, BlockPos pos, Container inventory) {
        NCInventoryHelper.dropInventoryItems(level, pos, inventory);
    }

    public void dropItems(Level level, BlockPos pos, List<ItemStack> stacks) {
        NCInventoryHelper.dropInventoryItems(level, pos, stacks);
    }
}