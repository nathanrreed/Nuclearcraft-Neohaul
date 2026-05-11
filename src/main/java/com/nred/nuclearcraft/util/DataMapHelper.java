package com.nred.nuclearcraft.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.Optional;

public class DataMapHelper {
    public static <T> T getData(BlockState blockState, DataMapType<Block, T> dataMapType) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(blockState.getBlock()).getData(dataMapType);
    }

    public static <T> T getData(ItemStack stack, DataMapType<Block, T> dataMapType) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            return BuiltInRegistries.BLOCK.wrapAsHolder(blockItem.getBlock()).getData(dataMapType);
        }
        return null;
    }

    public static <T> Block getFirst(DataMapType<Block, T> dataMapType) {
        Optional<ResourceKey<Block>> block = BuiltInRegistries.BLOCK.getDataMap(dataMapType).keySet().stream().findAny();
        return block.isPresent() ? BuiltInRegistries.BLOCK.get(block.get()) : Blocks.AIR;
    }

    public static <T> T getData(FluidStack stack, DataMapType<Fluid, T> dataMapType) {
        return BuiltInRegistries.FLUID.wrapAsHolder(stack.getFluid()).getData(dataMapType);
    }

    public static <T> T getData(Fluid fluid, DataMapType<Fluid, T> dataMapType) {
        return BuiltInRegistries.FLUID.wrapAsHolder(fluid).getData(dataMapType);
    }
}