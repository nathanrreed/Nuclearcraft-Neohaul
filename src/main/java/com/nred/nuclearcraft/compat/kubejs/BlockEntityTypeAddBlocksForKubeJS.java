package com.nred.nuclearcraft.compat.kubejs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

import java.util.*;

@EventBusSubscriber
public class BlockEntityTypeAddBlocksForKubeJS {
    private static final Map<ResourceLocation, List<ResourceLocation>> _BLOCK_ENTITY_TYPES_MAP = new HashMap<>();
    public static final Map<ResourceLocation, List<ResourceLocation>> BLOCK_ENTITY_TYPES_MAP = Collections.synchronizedMap(_BLOCK_ENTITY_TYPES_MAP);


    @SubscribeEvent
    public static void AddBlocksForKubeJS(BlockEntityTypeAddBlocksEvent event) {
        for (Map.Entry<ResourceLocation, List<ResourceLocation>> entry : BLOCK_ENTITY_TYPES_MAP.entrySet()) {
            event.modify(Objects.requireNonNull(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(entry.getKey())), entry.getValue().stream().map(BuiltInRegistries.BLOCK::get).toArray(Block[]::new));
        }

        // TODO add PlacementRules

        BLOCK_ENTITY_TYPES_MAP.clear();
    }
}