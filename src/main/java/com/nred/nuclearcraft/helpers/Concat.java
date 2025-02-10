package com.nred.nuclearcraft.helpers;

import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Concat {
    @SafeVarargs
    public static List<String> fluidKeys(Map<String, Fluids>... args) {
        return Arrays.stream(args).flatMap(m -> m.keySet().stream()).toList();
    }

    @SafeVarargs
    public static List<Map.Entry<String, Fluids>> fluidEntries(Map<String, Fluids>... args) {
        return Arrays.stream(args).flatMap(m -> m.entrySet().stream()).toList();
    }

    @SafeVarargs
    public static List<Fluids> fluidValues(Map<String, Fluids>... args) {
        return Arrays.stream(args).flatMap(m -> m.values().stream()).toList();
    }

    @SafeVarargs
    public static List<Block> blockValues(Map<String, DeferredBlock<Block>>... args) {
        return Arrays.stream(args).flatMap(m -> m.values().stream()).map(DeferredHolder::get).toList();
    }

    @SafeVarargs
    public static List<ItemStack> blockStackValues(Map<String, DeferredBlock<Block>>... args) {
        return Arrays.stream(args).flatMap(m -> m.values().stream()).map(block -> new ItemStack(block.asItem())).toList();
    }

    @SafeVarargs
    public static List<ItemStack> itemStackValues(Map<String, DeferredItem<Item>>... args) {
        return Arrays.stream(args).flatMap(m -> m.values().stream()).map(block -> new ItemStack(block.asItem())).toList();
    }
}