package com.nred.nuclearcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Random;

public class NCInventoryHelper {

    private static final Random RAND = new Random();

    public static void dropInventoryItems(Level world, BlockPos pos, Container inventory, int... slots) {
        dropInventoryItems(world, pos.getX(), pos.getY(), pos.getZ(), inventory, slots);
    }

    public static void dropInventoryItems(Level world, Entity entityAt, Container inventory, int... slots) {
        dropInventoryItems(world, entityAt.getX(), entityAt.getY(), entityAt.getZ(), inventory, slots);
    }

    private static void dropInventoryItems(Level world, double x, double y, double z, Container inventory, int... slots) {
        if (slots.length == 0) {
            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemstack = inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    spawnItemStack(world, x, y, z, itemstack);
                }
            }
        } else {
            for (int i : slots) {
                ItemStack itemstack = inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    spawnItemStack(world, x, y, z, itemstack);
                }
            }
        }
    }

    public static void dropInventoryItems(Level worldIn, BlockPos pos, List<ItemStack> stacks) {
        dropInventoryItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), stacks);
    }

    public static void dropInventoryItems(Level worldIn, Entity entityAt, List<ItemStack> stacks) {
        dropInventoryItems(worldIn, entityAt.getX(), entityAt.getY(), entityAt.getZ(), stacks);
    }

    private static void dropInventoryItems(Level worldIn, double x, double y, double z, List<ItemStack> stacks) {
        for (ItemStack itemstack : stacks) {
            if (!itemstack.isEmpty()) {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }

    private static void spawnItemStack(Level world, double x, double y, double z, ItemStack stack) {
        float fx = RAND.nextFloat() * 0.8F + 0.1F;
        float fy = RAND.nextFloat() * 0.8F + 0.1F;
        float fz = RAND.nextFloat() * 0.8F + 0.1F;

        while (!stack.isEmpty()) {
            int split = RAND.nextInt(21) + 10;
            ItemEntity entityitem = new ItemEntity(world, x + fx, y + fy, z + fz, stack.split(split));
            entityitem.setDeltaMovement(RAND.nextGaussian() * 0.05D, RAND.nextGaussian() * 0.05D + 0.2D, RAND.nextGaussian() * 0.05D);
            world.addFreshEntity(entityitem);
        }
    }

    /**
     * Returns the stack remaining after insertion
     */
    public static ItemStack addStackToInventory(IItemHandler inv, ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        stack = ItemHandlerHelper.insertItemStacked(inv, stack, false);
        return stack;
    }
}
