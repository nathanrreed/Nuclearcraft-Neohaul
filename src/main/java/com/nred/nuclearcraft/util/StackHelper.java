package com.nred.nuclearcraft.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import java.util.List;

public class StackHelper {
    public static ItemStack fixItemStack(Object object) {
        if (object instanceof ItemStack stack) {
            ItemStack copy = stack.copy();
            if (copy.getCount() == 0) {
                copy.setCount(1);
            }
            return copy;
        } else if (object instanceof Item item) {
            return new ItemStack(item, 1);
        } else if (object instanceof Block block) {
            return new ItemStack(block, 1);
        } else {
            throw new RuntimeException(String.format("Invalid ItemStack: %s", object));
        }
    }

    public static ItemStack blockStateToStack(BlockState blockState) {
        if (blockState == null || blockState.isAir()) {
            return ItemStack.EMPTY;
        }
        Block block = blockState.getBlock();
        if (block == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(block, 1);
    }

    public static BlockState getBlockStateFromStack(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (stack.getItem() == Items.AIR) {
            return Blocks.AIR.defaultBlockState();
        }
        if (stack.isEmpty()) {
            return null;
        }
        Item item = stack.getItem();
        if (!(item instanceof BlockItem itemBlock)) {
            return null;
        }
        return itemBlock.getBlock().defaultBlockState();
    }

    public static ItemStack changeStackSize(ItemStack stack, int size) {
        ItemStack newStack = stack.copy();
        newStack.setCount(size);
        return newStack;
    }

    public static String stackPath(ItemStack stack) {
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return loc.getPath();
    }

    public static String stackName(ItemStack stack) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return resourcelocation.toString();
    }

    public static String stackListNames(List<ItemStack> list) {
        StringBuilder names = new StringBuilder();
        for (ItemStack stack : list) {
            names.append(", ").append(stackName(stack));
        }
        return names.substring(2);
    }

    //	/** TODO
//	 * Stack tag comparison without checking capabilities such as radiation
//	 */
//	public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
//		boolean isAEmpty = stackA.isEmpty(), isBEmpty = stackB.isEmpty();
//		if (isAEmpty && isBEmpty) {
//			return true;
//		}
//		else if (!isAEmpty && !isBEmpty) {
//			CompoundTag stackANBT = stackA.get(), stackBNBT = stackB.getTagCompound();
//			if (stackANBT == null) {
//				return stackBNBT == null;
//			}
//			else {
//				return stackANBT.equals(stackBNBT);
//			}
//		}
//
//		return false;
//	}
//
//	public static ItemStack getBucket(String fluidName) {
//		return getBucket(new FluidStack(FluidRegistry.getFluid(fluidName), FluidType.BUCKET_VOLUME));
//	}
    public static ItemStack getBucket(@Nonnull FluidStack fluidStack) {
        return FluidUtil.getFilledBucket(fluidStack);
    }
}
