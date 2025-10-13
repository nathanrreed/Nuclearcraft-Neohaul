package com.nred.nuclearcraft.util;

import com.nred.nuclearcraft.block.fluid.ITileFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.function.UnaryOperator;

import static com.nred.nuclearcraft.config.Config2.processor_particles;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;


public class BlockHelper {

    public static void setDefaultFacing(Level level, BlockPos pos, BlockState state, EnumProperty<Direction> property) {
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(property, getDefaultFacing(level, pos, state, property)), 2);
        }
    }

    public static Direction getDefaultFacing(Level level, BlockPos pos, BlockState state, EnumProperty<Direction> property) {
        Direction facing = state.getValue(property);
        if (!level.isClientSide) {
            boolean n = level.getBlockState(pos.north()).isSolid();
            boolean s = level.getBlockState(pos.south()).isSolid();

            if (facing == Direction.NORTH && n && !s) {
                facing = Direction.SOUTH;
            } else if (facing == Direction.SOUTH && s && !n) {
                facing = Direction.NORTH;
            } else {
                boolean w = level.getBlockState(pos.west()).isSolid();
                boolean e = level.getBlockState(pos.east()).isSolid();

                if (facing == Direction.WEST && w && !e) {
                    facing = Direction.EAST;
                } else if (facing == Direction.EAST && e && !w) {
                    facing = Direction.WEST;
                } else if (property == FACING_ALL) {
                    boolean u = level.getBlockState(pos.above()).isSolid();
                    boolean d = level.getBlockState(pos.below()).isSolid();

                    if (facing == Direction.UP && u && !d) {
                        facing = Direction.DOWN;
                    } else if (facing == Direction.DOWN && d && !u) {
                        facing = Direction.UP;
                    }
                }
            }
        }
        return facing;
    }

    public static void spawnParticleOnProcessor(BlockState state, Level level, BlockPos pos, Random rand, Direction side, String particleName) {
        if (particleName.isEmpty() || !processor_particles) {
            return;
        }

        double d0 = pos.getX() + 0.5D;
        double d1 = pos.getY() + 0.125D + rand.nextDouble() * 0.75D;
        double d2 = pos.getZ() + 0.5D;
        double d3 = 0.52D;
        double d4 = rand.nextDouble() * 0.6D - 0.3D;

        switch (side) {
            case WEST:
                level.addParticle((ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(particleName)), d0 - d3, d1, d2 + d4, 0D, 0D, 0D);
                break;
            case EAST:
                level.addParticle((ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(particleName)), d0 + d3, d1, d2 + d4, 0D, 0D, 0D);
                break;
            case NORTH:
                level.addParticle((ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(particleName)), d0 + d4, d1, d2 - d3, 0D, 0D, 0D);
                break;
            case SOUTH:
                level.addParticle((ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(particleName)), d0 + d4, d1, d2 + d3, 0D, 0D, 0D);
                break;
            default:
                break;
        }
    }

    // Accessing machine tanks - taken from net.minecraftforge.fluids.FluidUtil and modified to correctly handle ITileFluids
    public static boolean accessTanks(Player player, InteractionHand hand, Direction facing, ITileFluid tile) {
        if (player == null || tile == null) {
            return false;
        }
        ItemStack heldItem = player.getItemInHand(hand);
        if (!heldItem.isEmpty()) {

            IItemHandler playerInventory = player.getCapability(Capabilities.ItemHandler.ENTITY);

            if (playerInventory != null) {
                Optional<IFluidHandlerItem> container = FluidUtil.getFluidHandler(heldItem.copyWithCount(1));
                if (container.isEmpty()) {
                    return false;
                }
                for (int i = 0; i < tile.getTanks().size(); ++i) {
                    FluidActionResult fluidActionResult = !tile.getTankSorption(facing, i).canDrain() ? FluidActionResult.FAILURE : FluidUtil.tryFillContainerAndStow(heldItem, tile.getTanks().get(i), playerInventory, Integer.MAX_VALUE, player, true);
                    if (!fluidActionResult.isSuccess()) {
                        if (tile.getTankSorption(facing, i).canFill() && tile.isNextToFill(facing, i, container.get().drain(Integer.MAX_VALUE, FluidAction.SIMULATE))) {
                            fluidActionResult = FluidUtil.tryEmptyContainerAndStow(heldItem, tile.getTanks().get(i), playerInventory, Integer.MAX_VALUE, player, true);
                        }
                    }
                    if (fluidActionResult.isSuccess()) {
                        player.setItemInHand(hand, fluidActionResult.getResult());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static final AABB REDUCED_BLOCK_AABB = new AABB(0.002D, 0.002D, 0.002D, 0.998D, 0.998D, 0.998D);

    // Taken from cofh.core.util.helpers.BlockHelper. Good idea Lemming!

    private static final byte[] BOTTOM = {2, 3, 0, 0, 0, 0};
    private static final byte[] TOP = {3, 2, 1, 1, 1, 1};
    private static final byte[] LEFT = {4, 5, 5, 4, 2, 3};
    private static final byte[] RIGHT = {5, 4, 4, 5, 3, 2};
    private static final byte[] FRONT = {0, 1, 2, 3, 4, 5};
    private static final byte[] BACK = {1, 0, 3, 2, 5, 4};

    public static Direction bottom(@Nullable Direction facing) {
        return facing == null ? Direction.DOWN : Direction.from3DDataValue(BOTTOM[facing.ordinal()]);
    }

    public static Direction top(@Nullable Direction facing) {
        return facing == null ? Direction.UP : Direction.from3DDataValue(TOP[facing.ordinal()]);
    }

    public static Direction left(@Nullable Direction facing) {
        return facing == null ? Direction.NORTH : Direction.from3DDataValue(LEFT[facing.ordinal()]);
    }

    public static Direction right(@Nullable Direction facing) {
        return facing == null ? Direction.SOUTH : Direction.from3DDataValue(RIGHT[facing.ordinal()]);
    }

    public static Direction front(@Nullable Direction facing) {
        return facing == null ? Direction.WEST : Direction.from3DDataValue(FRONT[facing.ordinal()]);
    }

    public static Direction back(@Nullable Direction facing) {
        return facing == null ? Direction.EAST : Direction.from3DDataValue(BACK[facing.ordinal()]);
    }

    public interface EnumFacingUnaryOperator extends UnaryOperator<Direction> {
    }

    public static EnumFacingUnaryOperator[] DIR_FROM_FACING = {BlockHelper::bottom, BlockHelper::top, BlockHelper::left, BlockHelper::right, BlockHelper::front, BlockHelper::back};
}
