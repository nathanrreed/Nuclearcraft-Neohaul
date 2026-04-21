package com.nred.nuclearcraft.compat.kubejs;

import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.turbine.TurbinePlacement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

import java.util.*;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

@EventBusSubscriber
public class BlockEntityTypeAddBlocksForKubeJS {
    private static final Map<ResourceLocation, List<ResourceLocation>> _BLOCK_ENTITY_TYPES_MAP = new HashMap<>();
    public static final Map<ResourceLocation, List<ResourceLocation>> BLOCK_ENTITY_TYPES_MAP = Collections.synchronizedMap(_BLOCK_ENTITY_TYPES_MAP);
    private static final Map<ResourceLocation, String> _PLACEMENT_RULE_MAP = new HashMap<>();
    public static final Map<ResourceLocation, String> PLACEMENT_RULE_MAP = Collections.synchronizedMap(_PLACEMENT_RULE_MAP);


    @SubscribeEvent
    public static void AddBlocksForKubeJS(BlockEntityTypeAddBlocksEvent event) {
        for (Map.Entry<ResourceLocation, List<ResourceLocation>> entry : BLOCK_ENTITY_TYPES_MAP.entrySet()) {
            event.modify(Objects.requireNonNull(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(entry.getKey())), entry.getValue().stream().map(BuiltInRegistries.BLOCK::get).toArray(Block[]::new));

            if (entry.getKey() == TURBINE_ENTITY_TYPE.get("dynamo").getId()) {
                for (ResourceLocation resourceLocation : entry.getValue()) {
                    TurbinePlacement.addRule(resourceLocation.toString(), PLACEMENT_RULE_MAP.get(resourceLocation), new ItemStack(BuiltInRegistries.BLOCK.get(resourceLocation)));
                }
            }
            if (entry.getKey() == FISSION_ENTITY_TYPE.get("heat_sink").getId() || entry.getKey() == FISSION_ENTITY_TYPE.get("coolant_heater").getId()) {
                for (ResourceLocation resourceLocation : entry.getValue()) {
                    FissionPlacement.addRule(resourceLocation.toString(), PLACEMENT_RULE_MAP.get(resourceLocation), new ItemStack(BuiltInRegistries.BLOCK.get(resourceLocation)));
                }
            }
        }

        BLOCK_ENTITY_TYPES_MAP.clear();
    }
}