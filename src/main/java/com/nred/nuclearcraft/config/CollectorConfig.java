package com.nred.nuclearcraft.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.Map;
import java.util.Objects;

@EventBusSubscriber(modid = NuclearcraftNeohaul.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class CollectorConfig {
    public static Map<MACHINE_LEVEL, Integer> cobblestoneCapacity;
    public static Map<MACHINE_LEVEL, Integer> waterCapacity;
    public static Map<MACHINE_LEVEL, Integer> nitrogenCapacity;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        CommentedConfig config = Objects.requireNonNull(event.getConfig().getLoadedConfig()).config();

        cobblestoneCapacity = Map.of(
                MACHINE_LEVEL.BASE, config.getInt("collector.cobblestone_generator.capacity"),
                MACHINE_LEVEL.COMPACT, config.getInt("collector.cobblestone_generator.compact_capacity"),
                MACHINE_LEVEL.DENSE, config.getInt("collector.cobblestone_generator.dense_capacity"));
        waterCapacity = Map.of(
                MACHINE_LEVEL.BASE, config.getInt("collector.water_source.capacity"),
                MACHINE_LEVEL.COMPACT, config.getInt("collector.water_source.compact_capacity"),
                MACHINE_LEVEL.DENSE, config.getInt("collector.water_source.dense_capacity")
        );
        nitrogenCapacity = Map.of(
                MACHINE_LEVEL.BASE, config.getInt("collector.nitrogen_collector.capacity"),
                MACHINE_LEVEL.COMPACT, config.getInt("collector.nitrogen_collector.compact_capacity"),
                MACHINE_LEVEL.DENSE, config.getInt("collector.nitrogen_collector.dense_capacity")
        );
    }
}