package com.nred.nuclearcraft.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        return block == null ? null : new ItemStack(block, stackSize); //TODO, getMeta(location)
    }

    public static ItemStack blockStackFromRegistry(String location) {
        return blockStackFromRegistry(location, 1);
    }

    public static ItemStack itemStackFromRegistry(String location, int stackSize) {
        Item item = getItem(removeMeta(location));
        return item == null ? null : new ItemStack(item, stackSize); //, getMeta(location)
    }

    public static ItemStack itemStackFromRegistry(String location) {
        return itemStackFromRegistry(location, 1);
    }

//	public static IBlockState blockStateFromRegistry(String location) {
//		Block block = getBlock(removeMeta(location));
//		return block == null ? null : block.getStateFromMeta(getMeta(location));
//	}
//
//	private static int getMeta(String location) {
//		if (StringUtils.countMatches(location, ':') < 2) {
//			return 0;
//		}
//		return Integer.parseInt(location.substring(location.lastIndexOf(':') + 1));
//	}

    private static String removeMeta(String location) {
        if (StringUtils.countMatches(location, ':') < 2) {
            return location;
        }
        return StringHelper.starting(location, location.lastIndexOf(':'));
    }

//	public static Biome biomeFromRegistry(String location) {
//		ResourceLocation resLoc = new ResourceLocation(location);
//		if (!Loader.isModLoaded(resLoc.getNamespace())) {
//			return null;
//		}
//		return ForgeRegistries.BIOMES.getValue(resLoc);
//	}
//
//	public static EntityEntry getEntityEntry(String location) {
//		ResourceLocation resLoc = new ResourceLocation(location);
//		if (!Loader.isModLoaded(resLoc.getNamespace())) {
//			return null;
//		}
//		return ForgeRegistries.ENTITIES.getValue(resLoc);
//	}
//
//	public static String getModID(ItemStack stack) {
//		if (stack == null) {
//			return "";
//		}
//		Item item = stack.getItem();
//		if (item == null || item.delegate == null || item.delegate.name() == null || item.delegate.name().getNamespace() == null) {
//			return "";
//		}
//		return item.delegate.name().getNamespace();
//	}
}