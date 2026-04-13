package com.nred.nuclearcraft.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class DataMapHelper {
    public static <T> T getData(BlockState blockState, DataMapType<Item, T> dataMapType) {
        return BuiltInRegistries.ITEM.wrapAsHolder(blockState.getBlock().asItem()).getData(dataMapType);
    }

    public static <T> T getData(ItemStack stack, DataMapType<Item, T> dataMapType) {
        return BuiltInRegistries.ITEM.wrapAsHolder(stack.getItem()).getData(dataMapType);
    }

    public static <T> T getData(FluidStack stack, DataMapType<Fluid, T> dataMapType) {
        return BuiltInRegistries.FLUID.wrapAsHolder(stack.getFluid()).getData(dataMapType);
    }
}