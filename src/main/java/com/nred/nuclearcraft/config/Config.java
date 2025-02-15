package com.nred.nuclearcraft.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

@EventBusSubscriber(modid = NuclearcraftNeohaul.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC = build();
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static ModConfigSpec build() {
        BUILDER.comment("Collector Settings").push("collector");
        BUILDER.comment("Cobblestone Generator Settings").push("cobblestone_generator");
        BUILDER.defineInRange("capacity", 16, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("compact_capacity", 120, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("dense_capacity", 960, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.comment("Water Source Settings").push("water_source");
        BUILDER.defineInRange("capacity", 1200, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("compact_capacity", 9600, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("dense_capacity", 76800, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.comment("Nitrogen Collector Settings").push("nitrogen_collector");
        BUILDER.defineInRange("capacity", 600, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("compact_capacity", 4800, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("dense_capacity", 38400, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.pop();

        BUILDER.comment("Solar Power").push("solar");
        BUILDER.defineList("production", List.of(5, 20, 80, 320), null, element -> (element instanceof Integer integer && integer > 0));
        BUILDER.defineList("capacity", List.of(20, 80, 320, 1280), null, element -> (element instanceof Integer integer && integer > 0));
        BUILDER.pop();

        BUILDER.defineInRange("lithium_ion_cell_capacity", 8000000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("lithium_ion_cell_transfer_rate", 80000, 1, Integer.MAX_VALUE);

        BUILDER.comment("Processor Settings").push("processor");

        BUILDER.comment("Determine how the upgrades scale");
        BUILDER.define("energy_upgrade_power_law_processing_power", 1.0);
        BUILDER.define("energy_upgrade_multiplier_processing_power", 1.0);
        BUILDER.define("speed_upgrade_power_law_processing_time", 1.0);
        BUILDER.define("speed_upgrade_power_law_processing_power", 2.0);
        BUILDER.define("speed_upgrade_multiplier_processing_time", 1.0);
        BUILDER.define("speed_upgrade_multiplier_processing_power", 1.0);
        BUILDER.define("speed_upgrade_max_stack_size", 64);
        BUILDER.define("energy_upgrade_max_stack_size", 64);

        BUILDER.comment("Alloy Furnace").push("alloy_furnace");
        BUILDER.defineInRange("capacity", 64000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 400, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Assembler").push("assembler");
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 600, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Centrifuge").push("centrifuge");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 1200, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Chemical Reactor").push("chemical_reactor");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 800, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Crystallizer").push("crystallizer");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 1600, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Decay Hastener").push("decay_hastener");
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 800, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Electric Furnace").push("electric_furnace");
        BUILDER.defineInRange("base_power", 20, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 200, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Electrolyzer").push("electrolyzer");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 40, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 3200, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Fuel Reprocessor").push("fuel_reprocessor");
        BUILDER.defineInRange("base_power", 20, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 400, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Fluid Enricher").push("fluid_enricher");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 600, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Fluid Extractor").push("fluid_extractor");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 2400, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Fluid Infuser").push("fluid_infuser");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 600, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Fluid Mixer").push("fluid_mixer");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 20, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 600, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Ingot Former").push("ingot_former");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 400, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Manufactory").push("manufactory");
        BUILDER.defineInRange("base_power", 20, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 400, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Melter").push("melter");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 40, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 800, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Pressurizer").push("pressurizer");
        BUILDER.defineInRange("base_power", 40, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 600, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Rock Crusher").push("rock_crusher");
        BUILDER.defineInRange("base_power", 20, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 400, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Separator").push("separator");
        BUILDER.defineInRange("base_power", 10, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 800, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("Supercooler").push("supercooler");
        BUILDER.defineInRange("fluid_capacity", 16000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_power", 20, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 600, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.pop();

        return BUILDER.build();
    }

    public static Map<String, ProcessorConfig> PROCESSOR_CONFIG_MAP = new HashMap<>();
    public static List<Integer> SOLAR_CONFIG_PRODUCTION;
    public static List<Integer> SOLAR_CONFIG_CAPACITY;
    public static UpgradeConfig UPGRADES_CONFIG;
    public static int LITHIUM_ION_CELL_CAPACITY;
    public static int LITHIUM_ION_CELL_TRANSFER;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        CommentedConfig config = Objects.requireNonNull(event.getConfig().getLoadedConfig()).config();

        for (String processor : PROCESSOR_MAP.keySet()) {
            PROCESSOR_CONFIG_MAP.put(processor, new ProcessorConfig(config.getIntOrElse("processor." + processor + ".base_time", 0), config.getIntOrElse("processor." + processor + ".base_power", 0), config.getIntOrElse("processor." + processor + ".fluid_capacity", 0)));
        }

        SOLAR_CONFIG_PRODUCTION = config.get("solar.production");
        SOLAR_CONFIG_CAPACITY = config.get("solar.capacity");

        UPGRADES_CONFIG = new UpgradeConfig(config.getInt("processor.speed_upgrade_max_stack_size"), config.getInt("processor.energy_upgrade_max_stack_size"),
                config.get("processor.speed_upgrade_power_law_processing_power"), config.get("processor.speed_upgrade_power_law_processing_time"),
                config.get("processor.speed_upgrade_multiplier_processing_power"), config.get("processor.speed_upgrade_multiplier_processing_time"),
                config.get("processor.energy_upgrade_power_law_processing_power"), config.get("processor.energy_upgrade_multiplier_processing_power"));

        LITHIUM_ION_CELL_CAPACITY = config.getInt("lithium_ion_cell_capacity");
        LITHIUM_ION_CELL_TRANSFER = config.getInt("lithium_ion_cell_transfer_rate");
    }
}