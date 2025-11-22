package com.nred.nuclearcraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import org.apache.commons.lang3.StringUtils;

public class RegistryHelper {
    public static Block getBlock(String location) {
        ResourceLocation resLoc = ResourceLocation.parse(location);
        if (ModList.get().getModContainerById(resLoc.getNamespace()).isEmpty()) {
            return null;
        }
        return BuiltInRegistries.BLOCK.get(resLoc);
    }

    public static Item getItem(String location) {
        ResourceLocation resLoc = ResourceLocation.parse(location);
        if (ModList.get().getModContainerById(resLoc.getNamespace()).isEmpty()) {
            return null;
        }
        return BuiltInRegistries.ITEM.get(resLoc);
    }

    public static ItemStack blockStackFromRegistry(String location, int stackSize) {
        Block block = getBlock(removeMeta(location));
        return block == null ? null : new ItemStack(block, stackSize);
    }

    public static ItemStack blockStackFromRegistry(String location) {
        return blockStackFromRegistry(location, 1);
    }

    public static ItemStack itemStackFromRegistry(String location, int stackSize) {
        Item item = getItem(removeMeta(location));
        return item == null ? null : new ItemStack(item, stackSize);
    }

    public static ItemStack itemStackFromRegistry(String location) {
        return itemStackFromRegistry(location, 1);
    }

    private static String removeMeta(String location) {
        if (StringUtils.countMatches(location, ':') < 2) {
            return location;
        }
        return StringHelper.starting(location, location.lastIndexOf(':'));
    }

    public static Holder.Reference<Biome> biomeFromRegistry(String location) {
        ResourceLocation resLoc = ResourceLocation.parse(location);
        if (!ModList.get().isLoaded(resLoc.getNamespace())) {
            return null;
        }

        return Minecraft.getInstance().level.registryAccess().asGetterLookup().get(Registries.BIOME, ResourceKey.create(Registries.BIOME, resLoc)).orElse(null);
    }

    public static EntityType<?> getEntityEntry(String location) {
        ResourceLocation resLoc = ResourceLocation.parse(location);
        if (!ModList.get().isLoaded(resLoc.getNamespace())) {
            return null;
        }
        return BuiltInRegistries.ENTITY_TYPE.get(resLoc);
    }
}