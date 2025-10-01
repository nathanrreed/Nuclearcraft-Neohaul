package com.nred.nuclearcraft.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nred.nuclearcraft.util.NCMath;

import java.util.*;

import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

@EventBusSubscriber(modid = NuclearcraftNeohaul.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC = build();
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static boolean positive(Object element) {
        return element instanceof Integer integer && integer > 0;
    }

    private static boolean range(Object element, int min, int max) {
        return element instanceof Integer integer && integer > min && integer < max;
    }

    private static boolean range(Object element, double min, double max) {
        return element instanceof Double num && num > min && num < max;
    }

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
        BUILDER.defineList("production", List.of(5, 20, 80, 320), null, Config::positive);
        BUILDER.defineList("capacity", List.of(20, 80, 320, 1280), null, Config::positive);
        BUILDER.pop();

        BUILDER.defineInRange("lithium_ion_cell_capacity", 8000000, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("lithium_ion_cell_transfer_rate", 80000, 1, Integer.MAX_VALUE);

        BUILDER.comment("Batteries and Voltaic Piles").push("battery");
        BUILDER.defineList("battery_capacity", List.of(32000000, 128000000, 512000000, 2048000000), null, Config::positive);
        BUILDER.defineList("voltaic_pile_capacity", List.of(1600000, 6400000, 25600000, 102400000), null, Config::positive);
        BUILDER.pop();

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
        BUILDER.defineInRange("base_power", 5, 1, Integer.MAX_VALUE);
        BUILDER.defineInRange("base_time", 400, 1, Integer.MAX_VALUE);
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

        BUILDER.comment("Fission Fuel Info").push("fission_fuel_info");
        BUILDER.defineList("thorium_fuel_time", List.of(14400, 14400, 18000, 11520, 18000), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("thorium_heat_generation", List.of(40, 40, 32, 50, 32), null, e -> range(e, 0, 32767));
        BUILDER.defineList("thorium_efficiency", List.of(1.25, 1.25, 1.25, 1.25, 1.25), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("thorium_criticality", List.of(199, 234, 293, 199, 234), null, e -> range(e, 0, 32767));
        BUILDER.defineList("thorium_decay_factor", List.of(0.04, 0.04, 0.04, 0.04, 0.04), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("thorium_self_priming", List.of(false, false, false, false, false), null, e -> e instanceof Boolean);

        BUILDER.defineList("uranium_fuel_time", List.of(2666, 2666, 3348, 2134, 3348, 2666, 2666, 3348, 2134, 3348, 4800, 4800, 6000, 3840, 6000, 4800, 4800, 6000, 3840, 6000), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("uranium_heat_generation", List.of(216, 216, 172, 270, 172, 216 * 3, 216 * 3, 172 * 3, 270 * 3, 172 * 3, 120, 120, 96, 150, 96, 120 * 3, 120 * 3, 96 * 3, 150 * 3, 96 * 3), null, e -> range(e, 0, 32767));
        BUILDER.defineList("uranium_efficiency", List.of(1.1, 1.1, 1.1, 1.1, 1.1, 1.15, 1.15, 1.15, 1.15, 1.15, 1.0, 1.0, 1.0, 1.0, 1.0, 1.05, 1.05, 1.05, 1.05, 1.05), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("uranium_criticality", List.of(66, 78, 98, 66, 78, 66 / 2, 78 / 2, 98 / 2, 66 / 2, 78 / 2, 87, 102, 128, 87, 102, 87 / 2, 102 / 2, 128 / 2, 87 / 2, 102 / 2), null, e -> range(e, 0, 32767));
        BUILDER.defineList("uranium_decay_factor", Collections.nCopies(20, 0.065), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("uranium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false), null, e -> e instanceof Boolean);

        BUILDER.defineList("neptunium_fuel_time", List.of(1972, 1972, 2462, 1574, 2462, 1972, 1972, 2462, 1574, 2462), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("neptunium_heat_generation", List.of(292, 292, 234, 366, 234, 292 * 3, 292 * 3, 234 * 3, 366 * 3, 234 * 3), null, e -> range(e, 0, 32767));
        BUILDER.defineList("neptunium_efficiency", List.of(1.1, 1.1, 1.1, 1.1, 1.1, 1.15, 1.15, 1.15, 1.15, 1.15), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("neptunium_criticality", List.of(60, 70, 88, 60, 70, 60 / 2, 70 / 2, 88 / 2, 60 / 2, 70 / 2), null, e -> range(e, 0, 32767));
        BUILDER.defineList("neptunium_decay_factor", Collections.nCopies(10, 0.07), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("neptunium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false), null, e -> e instanceof Boolean);

        BUILDER.defineList("plutonium_fuel_time", List.of(4572, 4572, 5760, 3646, 5760, 4572, 4572, 5760, 3646, 5760, 3164, 3164, 3946, 2526, 3946, 3164, 3164, 3946, 2526, 3946), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("plutonium_heat_generation", List.of(126, 126, 100, 158, 100, 126 * 3, 126 * 3, 100 * 3, 158 * 3, 100 * 3, 182, 182, 146, 228, 146, 182 * 3, 182 * 3, 146 * 3, 228 * 3, 146 * 3), null, e -> range(e, 0, 32767));
        BUILDER.defineList("plutonium_efficiency", List.of(1.2, 1.2, 1.2, 1.2, 1.2, 1.25, 1.25, 1.25, 1.25, 1.25, 1.25, 1.25, 1.25, 1.25, 1.25, 1.3, 1.3, 1.3, 1.3, 1.3), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("plutonium_criticality", List.of(84, 99, 124, 84, 99, 84 / 2, 99 / 2, 124 / 2, 84 / 2, 99 / 2, 71, 84, 105, 71, 84, 71 / 2, 84 / 2, 105 / 2, 71 / 2, 84 / 2), null, e -> range(e, 0, 32767));
        BUILDER.defineList("plutonium_decay_factor", Collections.nCopies(20, 0.075), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("plutonium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false), null, e -> e instanceof Boolean);

        BUILDER.defineList("mixed_fuel_time", List.of(4354, 4354, 5486, 3472, 5486, 3014, 3014, 3758, 2406, 3758), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("mixed_heat_generation", List.of(132, 132, 106, 166, 106, 192, 192, 154, 240, 154), null, e -> range(e, 0, 32767));
        BUILDER.defineList("mixed_efficiency", List.of(1.05, 1.05, 1.05, 1.05, 1.05, 1.15, 1.15, 1.15, 1.15, 1.15), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("mixed_criticality", List.of(80, 94, 118, 80, 94, 68, 80, 100, 68, 80), null, e -> range(e, 0, 32767));
        BUILDER.defineList("mixed_decay_factor", Collections.nCopies(10, 0.075), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("mixed_self_priming", List.of(false, false, false, false, false, false, false, false, false, false), null, e -> e instanceof Boolean);

        BUILDER.defineList("americium_fuel_time", List.of(1476, 1476, 1846, 1180, 1846, 1476, 1476, 1846, 1180, 1846), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("americium_heat_generation", List.of(390, 390, 312, 488, 312, 390 * 3, 390 * 3, 312 * 3, 488 * 3, 312 * 3), null, e -> range(e, 0, 32767));
        BUILDER.defineList("americium_efficiency", List.of(1.35, 1.35, 1.35, 1.35, 1.35, 1.4, 1.4, 1.4, 1.4, 1.4), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("americium_criticality", List.of(55, 65, 81, 55, 65, 55 / 2, 65 / 2, 81 / 2, 55 / 2, 65 / 2), null, e -> range(e, 0, 32767));
        BUILDER.defineList("americium_decay_factor", Collections.nCopies(10, 0.08), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("americium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false), null, e -> e instanceof Boolean);

        BUILDER.defineList("curium_fuel_time", List.of(1500, 1500, 1870, 1200, 1870, 1500, 1500, 1870, 1200, 1870, 2420, 2420, 3032, 1932, 3032, 2420, 2420, 3032, 1932, 3032, 2150, 2150, 2692, 1714, 2692, 2150, 2150, 2692, 1714, 2692), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("curium_heat_generation", List.of(384, 384, 308, 480, 308, 384 * 3, 384 * 3, 308 * 3, 480 * 3, 308 * 3, 238, 238, 190, 298, 190, 238 * 3, 238 * 3, 190 * 3, 298 * 3, 190 * 3, 268, 268, 214, 336, 214, 268 * 3, 268 * 3, 214 * 3, 336 * 3, 214 * 3), null, e -> range(e, 0, 32767));
        BUILDER.defineList("curium_efficiency", List.of(1.45, 1.45, 1.45, 1.45, 1.45, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.55, 1.55, 1.55, 1.55, 1.55, 1.55, 1.55, 1.55, 1.55, 1.55, 1.6, 1.6, 1.6, 1.6, 1.6), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("curium_criticality", List.of(56, 66, 83, 56, 66, 56 / 2, 66 / 2, 83 / 2, 56 / 2, 66 / 2, 64, 75, 94, 64, 75, 64 / 2, 75 / 2, 94 / 2, 64 / 2, 75 / 2, 61, 72, 90, 61, 72, 61 / 2, 72 / 2, 90 / 2, 61 / 2, 72 / 2), null, e -> range(e, 0, 32767));
        BUILDER.defineList("curium_decay_factor", Collections.nCopies(30, 0.085), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("curium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false), null, e -> e instanceof Boolean);

        BUILDER.defineList("berkelium_fuel_time", List.of(2166, 2166, 2716, 1734, 2716, 2166, 2166, 2716, 1734, 2716), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("berkelium_heat_generation", List.of(266, 266, 212, 332, 212, 266 * 3, 266 * 3, 212 * 3, 332 * 3, 212 * 3), null, e -> range(e, 0, 32767));
        BUILDER.defineList("berkelium_efficiency", List.of(1.65, 1.65, 1.65, 1.65, 1.65, 1.7, 1.7, 1.7, 1.7, 1.7), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("berkelium_criticality", List.of(62, 73, 91, 62, 73, 62 / 2, 73 / 2, 91 / 2, 62 / 2, 73 / 2), null, e -> range(e, 0, 32767));
        BUILDER.defineList("berkelium_decay_factor", Collections.nCopies(10, 0.09), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("berkelium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false), null, e -> e instanceof Boolean);

        BUILDER.defineList("californium_fuel_time", List.of(1066, 1066, 1334, 852, 1334, 1066, 1066, 1334, 852, 1334, 2000, 2000, 2504, 1600, 2504, 2000, 2000, 2504, 1600, 2504), null, e -> range(e, 1, Integer.MAX_VALUE));
        BUILDER.defineList("californium_heat_generation", List.of(540, 540, 432, 676, 432, 540 * 3, 540 * 3, 432 * 3, 676 * 3, 432 * 3, 288, 288, 230, 360, 230, 288 * 3, 288 * 3, 230 * 3, 360 * 3, 230 * 3), null, e -> range(e, 0, 32767));
        BUILDER.defineList("californium_efficiency", List.of(1.75, 1.75, 1.75, 1.75, 1.75, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.85, 1.85, 1.85, 1.85, 1.85), null, e -> range(e, 0.0, 32767.0));
        BUILDER.defineList("californium_criticality", List.of(51, 60, 75, 51, 60, 51 / 2, 60 / 2, 75 / 2, 51 / 2, 60 / 2, 60, 71, 89, 60, 71, 60 / 2, 71 / 2, 89 / 2, 60 / 2, 71 / 2), null, e -> range(e, 0, 32767));
        BUILDER.defineList("californium_decay_factor", Collections.nCopies(20, 0.1), null, e -> range(e, 0.0, 1.0));
        BUILDER.defineList("californium_self_priming", List.of(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true), null, e -> e instanceof Boolean);
        BUILDER.pop();

        BUILDER.comment("Turbine").push("multiblock");
        BUILDER.defineInRange("turbine_min_size", 1, 1, 255);
        BUILDER.defineInRange("turbine_max_size", 24, 1, 255);
        BUILDER.defineList("turbine_blade_efficiency", List.of(1D, 1.1D, 1.2D), null, e -> range(e, 0.01D, 15D));
        BUILDER.defineList("turbine_blade_expansion", List.of(1.4D, 1.6D, 1.8D), null, e -> range(e, 1D, 15D));
        BUILDER.defineInRange("turbine_stator_expansion", 0.75D, 0.01D, 1D);
        BUILDER.defineList("turbine_coil_conductivity", List.of(0.88D, 0.9D, 1D, 1.04D, 1.06D, 1.12D), null, e -> range(e, 0.01D, 15D));
        BUILDER.defineList("turbine_coil_rule", List.of("one bearing || one connector", "one magnesium coil", "two magnesium coils", "one aluminum coil", "one beryllium coil", "one gold coil && one copper coil"), null, e -> true);
        BUILDER.defineList("turbine_connector_rule", List.of("one of any coil"), null, e -> true);
        BUILDER.defineList("turbine_power_per_mb", List.of(16D, 4D, 4D), null, e -> range(e, 0D, 255D));
        BUILDER.defineList("turbine_expansion_level", List.of(4D, 2D, 2D), null, e -> range(e, 1D, 255D));
        BUILDER.defineInRange("turbine_spin_up_multiplier_global", 1D, 0D, 255D);
        BUILDER.defineList("turbine_spin_up_multiplier", List.of(1D, 1D, 1D), null, e -> range(e, 0D, 255D));
        BUILDER.defineInRange("turbine_spin_down_multiplier", 1D, 0.01D, 255D);
        BUILDER.defineInRange("turbine_mb_per_blade", 100, 1, 32767);
        BUILDER.defineList("turbine_throughput_leniency_params", List.of(0.5D, 0.75D), null, e -> range(e, 0D, 1D));
        BUILDER.defineInRange("turbine_tension_throughput_factor", 2D, 1D, 255D);
        BUILDER.defineInRange("turbine_tension_leniency", 0.05D, 0D, 1D);
        BUILDER.defineInRange("turbine_power_bonus_multiplier", 1D, 0D, 255D);
        BUILDER.defineInRange("turbine_sound_volume", 1D, 0D, 15D);
        BUILDER.defineInRange("turbine_particles", 0.025D, 0D, 1D);
        BUILDER.defineInRange("turbine_render_blade_width", NCMath.SQRT2, 0.01D, 4D);
        BUILDER.defineInRange("turbine_render_rotor_expansion", 4D, 1D, 15D);
        BUILDER.defineInRange("turbine_render_rotor_speed", 1D, 0D, 15D);

        BUILDER.pop();

        return BUILDER.build();
    }

    public static Map<String, ProcessorConfig> PROCESSOR_CONFIG_MAP = new HashMap<>();
    public static Map<String, FuelConfig> FUEL_CONFIG_MAP = new HashMap<>();
    public static List<Integer> SOLAR_CONFIG_PRODUCTION;
    public static List<Integer> SOLAR_CONFIG_CAPACITY;
    public static List<Integer> BATTERY_CONFIG_CAPACITY;
    public static List<Integer> VOLTAIC_PILE_CAPACITY;
    public static UpgradeConfig UPGRADES_CONFIG;
    public static int LITHIUM_ION_CELL_CAPACITY;
    public static int LITHIUM_ION_CELL_TRANSFER;

    public static int turbine_min_size;
    public static int turbine_max_size;
    public static List<Double> turbine_blade_efficiency;
    public static List<Double> turbine_blade_expansion;
    public static double turbine_stator_expansion;
    public static List<Double> turbine_coil_conductivity;
    public static List<String> turbine_coil_rule;
    public static List<String> turbine_connector_rule;
    public static List<Double> turbine_power_per_mb;
    public static List<Double> turbine_expansion_level;
    public static double turbine_spin_up_multiplier_global;
    public static List<Double> turbine_spin_up_multiplier;
    public static double turbine_spin_down_multiplier;
    public static int turbine_mb_per_blade;
    public static List<Double> turbine_throughput_leniency_params;
    public static double turbine_tension_throughput_factor;
    public static double turbine_tension_leniency;
    public static double turbine_power_bonus_multiplier;
    public static double turbine_sound_volume;
    public static double turbine_particles;
    public static double turbine_render_blade_width;
    public static double turbine_render_rotor_expansion;
    public static double turbine_render_rotor_speed;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        CommentedConfig config = Objects.requireNonNull(event.getConfig().getLoadedConfig()).config();

        for (String processor : PROCESSOR_MAP.keySet()) {
            PROCESSOR_CONFIG_MAP.put(processor, new ProcessorConfig(config.getIntOrElse("processor." + processor + ".base_time", 0), config.getIntOrElse("processor." + processor + ".base_power", 0), config.getIntOrElse("processor." + processor + ".fluid_capacity", 0)));
        }
        for (String type : List.of("thorium", "uranium", "neptunium", "plutonium", "mixed", "americium", "curium", "berkelium", "californium")) {
            FUEL_CONFIG_MAP.put(type, new FuelConfig(config.get("fission_fuel_info." + type + "_fuel_time"), config.get("fission_fuel_info." + type + "_heat_generation"), config.get("fission_fuel_info." + type + "_efficiency"), config.get("fission_fuel_info." + type + "_criticality"), config.get("fission_fuel_info." + type + "_decay_factor"), config.get("fission_fuel_info." + type + "_fuel_time")));
        }

        SOLAR_CONFIG_PRODUCTION = config.get("solar.production");
        SOLAR_CONFIG_CAPACITY = config.get("solar.capacity");
        BATTERY_CONFIG_CAPACITY = config.get("battery.battery_capacity");
        VOLTAIC_PILE_CAPACITY = config.get("battery.voltaic_pile_capacity");

        UPGRADES_CONFIG = new UpgradeConfig(config.getInt("processor.speed_upgrade_max_stack_size"), config.getInt("processor.energy_upgrade_max_stack_size"),
                config.get("processor.speed_upgrade_power_law_processing_power"), config.get("processor.speed_upgrade_power_law_processing_time"),
                config.get("processor.speed_upgrade_multiplier_processing_power"), config.get("processor.speed_upgrade_multiplier_processing_time"),
                config.get("processor.energy_upgrade_power_law_processing_power"), config.get("processor.energy_upgrade_multiplier_processing_power"));

        LITHIUM_ION_CELL_CAPACITY = config.getInt("lithium_ion_cell_capacity");
        LITHIUM_ION_CELL_TRANSFER = config.getInt("lithium_ion_cell_transfer_rate");

        turbine_min_size = config.getInt("multiblock.turbine_min_size");
        turbine_max_size = config.getInt("multiblock.turbine_max_size");
        turbine_blade_efficiency = config.get("multiblock.turbine_blade_efficiency");
        turbine_blade_expansion = config.get("multiblock.turbine_blade_expansion");
        turbine_stator_expansion = config.get("multiblock.turbine_stator_expansion");
        turbine_coil_conductivity = config.get("multiblock.turbine_coil_conductivity");
        turbine_coil_rule = config.get("multiblock.turbine_coil_rule");
        turbine_connector_rule = config.get("multiblock.turbine_connector_rule");
        turbine_power_per_mb = config.get("multiblock.turbine_power_per_mb");
        turbine_expansion_level = config.get("multiblock.turbine_expansion_level");
        turbine_spin_up_multiplier_global = config.get("multiblock.turbine_spin_up_multiplier_global");
        turbine_spin_up_multiplier = config.get("multiblock.turbine_spin_up_multiplier");
        turbine_spin_down_multiplier = config.get("multiblock.turbine_spin_down_multiplier");
        turbine_mb_per_blade = config.get("multiblock.turbine_mb_per_blade");
        turbine_throughput_leniency_params = config.get("multiblock.turbine_throughput_leniency_params");
        turbine_tension_throughput_factor = config.get("multiblock.turbine_tension_throughput_factor");
        turbine_tension_leniency = config.get("multiblock.turbine_tension_leniency");
        turbine_power_bonus_multiplier = config.get("multiblock.turbine_power_bonus_multiplier");
        turbine_sound_volume = config.get("multiblock.turbine_sound_volume");
        turbine_particles = config.get("multiblock.turbine_particles");
        turbine_render_blade_width = config.get("multiblock.turbine_render_blade_width");
        turbine_render_rotor_expansion = config.get("multiblock.turbine_render_rotor_expansion");
    }
}