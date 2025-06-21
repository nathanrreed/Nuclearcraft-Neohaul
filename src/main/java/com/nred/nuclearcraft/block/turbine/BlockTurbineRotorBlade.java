package com.nred.nuclearcraft.block.turbine;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockTurbineRotorBlade extends DirectionalBlock {
    public static final MapCodec<BlockTurbineRotorBlade> CODEC = simpleCodec(BlockTurbineRotorBlade::new);

    protected static final VoxelShape X_AXIS_AABB = Block.box(0, 7, 2, 16, 9, 14);
    protected static final VoxelShape Y_AXIS_AABB = Block.box(2, 0, 7, 14, 16, 9);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(2, 7, 0, 14, 9, 16);
//    private final TurbineRotorBladeType bladeType;

    public BlockTurbineRotorBlade(Properties properties) {
        super(properties);

//        this.bladeType = bladeType;
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    //
//    public BlockTurbineRotorBlade(TurbineRotorBladeType bladeType) {
//        super();
//        setDefaultState(blockState.getBaseState().withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.Y));
//        this.bladeType = bladeType;
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, TurbineRotorBladeUtil.DIR);
//    }
//
//    @Override
//    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
//        return getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.fromFacingAxis(facing.getAxis()));
//    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
        return blockstate.is(this) && blockstate.getValue(FACING) == direction
                ? this.defaultBlockState().setValue(FACING, direction.getOpposite())
                : this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING).getAxis()) {
            case X -> X_AXIS_AABB;
            case Z -> Z_AXIS_AABB;
            case Y -> Y_AXIS_AABB;
        };
    }


//
//    @Override
//    public TileEntity createNewTileEntity(World world, int metadata) {
//        return switch (bladeType) {
//            case STEEL -> new TileTurbineRotorBlade.Steel();
//            case EXTREME -> new TileTurbineRotorBlade.Extreme();
//            case SIC_SIC_CMC -> new TileTurbineRotorBlade.SicSicCMC();
//        };
//    }
//
//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        if (state.getBlock() != this) {
//            return super.getBoundingBox(state, source, pos);
//        }
//
//        return switch (state.getValue(TurbineRotorBladeUtil.DIR)) {
//            case X -> BLADE_AABB[0];
//            case Y -> BLADE_AABB[1];
//            case Z -> BLADE_AABB[2];
//            default -> super.getBoundingBox(state, source, pos);
//        };
//    }



//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockStateIn, IBlockAccess worldIn, BlockPos pos) {
//        return NULL_AABB;
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
//        if (state.getBlock() != this) {
//            return super.getSelectedBoundingBox(state, worldIn, pos);
//        }
//
//        return switch (state.getValue(TurbineRotorBladeUtil.DIR)) {
//            case X -> BLADE_AABB[0].offset(pos);
//            case Y -> BLADE_AABB[1].offset(pos);
//            case Z -> BLADE_AABB[2].offset(pos);
//            default -> super.getSelectedBoundingBox(state, worldIn, pos);
//        };
//    }
//
//    @Override
//    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
//            return false;
//        }
//        return rightClickOnPart(world, pos, player, hand, facing);
//    }
//
//    @Override
//    public int getMetaFromState(IBlockState state) {
//        return state.getValue(TurbineRotorBladeUtil.DIR).ordinal();
//    }
//
//    @Override
//    public IBlockState getStateFromMeta(int meta) {
//        return getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.values()[meta]);
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public BlockRenderLayer getRenderLayer() {
//        return BlockRenderLayer.CUTOUT;
//    }
//
//    @Override
//    public boolean isOpaqueCube(IBlockState state) {
//        return false;
//    }
//
//    @Override
//    public boolean isFullCube(IBlockState state) {
//        return false;
//    }
//
//    @Override
//    public boolean isTopSolid(IBlockState state) {
//        return false;
//    }
//
//    @Override
//    public boolean causesSuffocation(IBlockState state) {
//        return false;
//    }
}