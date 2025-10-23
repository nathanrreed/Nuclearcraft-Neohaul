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

public class NCBlock extends Block {
//    protected TriState canSustainPlant = TriState.TRUE;
//    protected TriState canCreatureSpawn = TriState.TRUE;

    protected static boolean keepInventory;

    public NCBlock(BlockBehaviour.Properties properties) {
        super(properties);
//        setHarvestLevel("pickaxe", 0); TODO
//        setHardness(2F);
//        setResistance(15F);
    }

//    @Override TODO
//    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
//        return canSustainPlant && super.canSustainPlant(state, level, soilPosition, facing, plant);
//    }
//
//    @Override
//    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
//        return canCreatureSpawn && super.canCreatureSpawn(state, world, pos, type);
//    }

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

    // NBT Stuff

//    @Override TODO
//    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
//        if (this instanceof INBTDrop && willHarvest) {
//            onBlockHarvested(world, pos, state, player);
//            return true;
//        }
//        return super.removedByPlayer(state, world, pos, player, willHarvest);
//    }
//    @Override
//    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
//        if (this instanceof INBTDrop drop) {
//            return Lists.newArrayList(drop.getNBTDrop(world, pos, state));
//        }
//        return super.getDrops(world, pos, state, fortune);
//    }
//    @Override
//    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
//        super.onBlockPlacedBy(world, pos, state, placer, stack);
//        if (this instanceof INBTDrop drop && stack.hasTagCompound()) {
//            drop.readStackData(world, pos, placer, stack);
//            world.notifyBlockUpdate(pos, state, state, 3);
//        }
//    }
}