package com.nred.nuclearcraft.block.turbine;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockTurbineGlass extends TransparentBlock {
    public BlockTurbineGlass() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
    }

//	@Override
//	public TileEntity createNewTileEntity(World world, int metadata) {
//		return new TileTurbineGlass();
//	}
//
//	@Override
//	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
//			return false;
//		}
//		return rightClickOnPart(world, pos, player, hand, facing);
//	}
}
