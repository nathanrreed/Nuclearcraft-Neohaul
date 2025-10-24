package com.nred.nuclearcraft.config;

import com.google.common.primitives.Booleans;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = NuclearcraftNeohaul.MODID)
public class Config2 {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final String CATEGORY_WORLD_GEN = "world_gen";
    public static final String CATEGORY_PROCESSOR = "processor";
    public static final String CATEGORY_GENERATOR = "generator";
    public static final String CATEGORY_ENERGY_STORAGE = "energy_storage";
    public static final String CATEGORY_MACHINE = "machine";
    public static final String CATEGORY_FISSION = "fission";
    public static final String CATEGORY_FUSION = "fusion";
    public static final String CATEGORY_HEAT_EXCHANGER = "heat_exchanger";
    public static final String CATEGORY_TURBINE = "multiblock";
    public static final String CATEGORY_QUANTUM = "quantum";
    public static final String CATEGORY_TOOL = "tool";
    public static final String CATEGORY_ARMOR = "armor";
    public static final String CATEGORY_ENTITY = "entity";
    public static final String CATEGORY_RADIATION = "radiation";
    public static final String CATEGORY_REGISTRATION = "registration";
    public static final String CATEGORY_MISC = "misc";

    public static final String CATEGORY_OUTPUT = "output";

    protected static final Map<String, List<String>> PROPERTY_ORDER_MAP = new HashMap<>();

    protected static final boolean LIST = false, ARRAY = true;

    public static int[] ore_dims;
    public static boolean ore_dims_list_type;
    public static boolean[] ore_gen;
    public static int[] ore_size;
    public static int[] ore_rate;
    public static int[] ore_min_height;
    public static int[] ore_max_height;
    public static boolean[] ore_drops;
    public static boolean ore_hide_disabled;
    public static int[] ore_harvest_levels;

    public static double processor_time_multiplier; // Default: 1
    public static double processor_power_multiplier; // Default: 1
    public static int[] processor_time;
    public static int[] processor_power;
    public static double[] speed_upgrade_power_laws_fp;
    public static double[] speed_upgrade_multipliers_fp;
    public static double[] energy_upgrade_power_laws_fp;
    public static double[] energy_upgrade_multipliers_fp;
    public static int[] upgrade_stack_sizes;

    public static boolean enable_mek_gas;
    public static boolean[] enable_fluid_recipe_expansion;
    public static int machine_update_rate;
    public static double[] processor_passive_rate;
    public static boolean passive_push;
    public static double cobble_gen_power;
    public static boolean ore_processing;
    public static int[] manufactory_wood;
    public static boolean rock_crusher_alternate;

    public static boolean default_processor_recipes_global;
    public static boolean[] default_processor_recipes;
    //    public static boolean gtce_recipe_integration_global; TODO REMOVE
//    public static boolean[] gtce_recipe_integration;
//    public static boolean gtce_recipe_logging;
    public static boolean smart_processor_input;
    //    public static boolean factor_recipes; TODO ADD or REMOVE
    public static boolean processor_particles;

    public static int[] rtg_power;
    public static int[] solar_power;
//    public static double[] decay_lifetime;
//    public static double[] decay_power;

//    public static int[] battery_block_capacity;
//    public static int[] battery_block_max_transfer;
//    public static int[] battery_item_capacity;
//    public static int[] battery_item_max_transfer;

    public static int machine_min_size; // Default: 1
    public static int machine_max_size; // Default: 24
    public static double[] machine_diaphragm_efficiency;
    public static double[] machine_diaphragm_contact_factor;
    public static double[] machine_sieve_assembly_efficiency;

    public static int machine_electrolyzer_time;
    public static int machine_electrolyzer_power;
    public static String[] machine_cathode_efficiency;
    public static String[] machine_anode_efficiency;
    public static double machine_electrolyzer_sound_volume;

    public static int machine_distiller_time;
    public static int machine_distiller_power;
    public static double machine_distiller_sound_volume;

    public static int machine_infiltrator_time;
    public static int machine_infiltrator_power;
    public static String[] machine_infiltrator_pressure_fluid_efficiency;
    public static double machine_infiltrator_sound_volume;

    public static int fission_min_size; // Default: 1
    public static int fission_max_size; // Default: 24
    public static double fission_fuel_time_multiplier; // Default: 1
    public static double fission_fuel_heat_multiplier; // Default: 1
    public static double fission_fuel_efficiency_multiplier; // Default: 1
    public static double fission_fuel_radiation_multiplier; // Default: 1
    public static double[] fission_source_efficiency;
    public static int[] fission_sink_cooling_rate;
    public static String[] fission_sink_rule;
    public static int[] fission_heater_cooling_rate = new int[]{55, 50, 85, 80, 70, 105, 90, 100, 110, 115, 145, 65, 95, 200, 195, 75, 120, 60, 160, 130, 125, 150, 175, 170, 165, 180, 140, 135, 185, 190, 155, 205}; // TODO FIX / REMOVE
    public static String[] fission_heater_rule;
    //    public static int[] fission_moderator_flux_factor;
//    public static double[] fission_moderator_efficiency;
//    public static double[] fission_reflector_efficiency;
//    public static double[] fission_reflector_reflectivity;
    public static double[] fission_shield_heat_per_flux;
    public static double[] fission_shield_efficiency;
    //    public static double[] fission_irradiator_heat_per_flux;
//    public static double[] fission_irradiator_efficiency;
    public static int fission_cooling_efficiency_leniency;
    public static double[] fission_sparsity_penalty_params; // Multiplier, threshold
    public static double fission_heating_coolant_heat_mult;

    public static boolean fission_decay_mechanics;
    public static double[] fission_decay_build_up_times; // Decay heat, iodine, poison
    public static double[] fission_decay_lifetimes; // Decay heat, iodine, poison
    public static double[] fission_decay_equilibrium_factors; // Decay heat, iodine, poison
    public static double[] fission_decay_daughter_multipliers; // Iodine, poison
    public static double[] fission_decay_term_multipliers; // Exponential, linear

    public static boolean fission_overheat;
    public static double fission_meltdown_radiation_multiplier;
    public static boolean fission_heat_damage;
    public static int fission_neutron_reach;
    public static boolean[] fission_heat_dissipation;
    public static double fission_heat_dissipation_rate;
    public static double fission_emergency_cooling_multiplier;
    public static double fission_sound_volume;

//    public static int[] fission_thorium_fuel_time;
//    public static int[] fission_thorium_heat_generation;
//    public static double[] fission_thorium_efficiency;
//    public static int[] fission_thorium_criticality;
//    public static double[] fission_thorium_decay_factor;
//    public static boolean[] fission_thorium_self_priming;
//    public static double[] fission_thorium_radiation;

//    public static int[] fission_uranium_fuel_time;
//    public static int[] fission_uranium_heat_generation;
//    public static double[] fission_uranium_efficiency;
//    public static int[] fission_uranium_criticality;
//    public static double[] fission_uranium_decay_factor;
//    public static boolean[] fission_uranium_self_priming;
//    public static double[] fission_uranium_radiation;
//
//    public static int[] fission_neptunium_fuel_time;
//    public static int[] fission_neptunium_heat_generation;
//    public static double[] fission_neptunium_efficiency;
//    public static int[] fission_neptunium_criticality;
//    public static double[] fission_neptunium_decay_factor;
//    public static boolean[] fission_neptunium_self_priming;
//    public static double[] fission_neptunium_radiation;
//
//    public static int[] fission_plutonium_fuel_time;
//    public static int[] fission_plutonium_heat_generation;
//    public static double[] fission_plutonium_efficiency;
//    public static int[] fission_plutonium_criticality;
//    public static double[] fission_plutonium_decay_factor;
//    public static boolean[] fission_plutonium_self_priming;
//    public static double[] fission_plutonium_radiation;
//
//    public static int[] fission_mixed_fuel_time;
//    public static int[] fission_mixed_heat_generation;
//    public static double[] fission_mixed_efficiency;
//    public static int[] fission_mixed_criticality;
//    public static double[] fission_mixed_decay_factor;
//    public static boolean[] fission_mixed_self_priming;
//    public static double[] fission_mixed_radiation;
//
//    public static int[] fission_americium_fuel_time;
//    public static int[] fission_americium_heat_generation;
//    public static double[] fission_americium_efficiency;
//    public static int[] fission_americium_criticality;
//    public static double[] fission_americium_decay_factor;
//    public static boolean[] fission_americium_self_priming;
//    public static double[] fission_americium_radiation;
//
//    public static int[] fission_curium_fuel_time;
//    public static int[] fission_curium_heat_generation;
//    public static double[] fission_curium_efficiency;
//    public static int[] fission_curium_criticality;
//    public static double[] fission_curium_decay_factor;
//    public static boolean[] fission_curium_self_priming;
//    public static double[] fission_curium_radiation;
//
//    public static int[] fission_berkelium_fuel_time;
//    public static int[] fission_berkelium_heat_generation;
//    public static double[] fission_berkelium_efficiency;
//    public static int[] fission_berkelium_criticality;
//    public static double[] fission_berkelium_decay_factor;
//    public static boolean[] fission_berkelium_self_priming;
//    public static double[] fission_berkelium_radiation;
//
//    public static int[] fission_californium_fuel_time;
//    public static int[] fission_californium_heat_generation;
//    public static double[] fission_californium_efficiency;
//    public static int[] fission_californium_criticality;
//    public static double[] fission_californium_decay_factor;
//    public static boolean[] fission_californium_self_priming;
//    public static double[] fission_californium_radiation;

    public static double fusion_fuel_time_multiplier; // Default: 1
    public static double fusion_fuel_heat_multiplier; // Default: 1
    public static double fusion_fuel_efficiency_multiplier; // Default: 1
    public static double fusion_fuel_radiation_multiplier; // Default: 1
    public static boolean fusion_overheat;
    public static double fusion_meltdown_radiation_multiplier;
    public static int fusion_min_size; // Default: 1
    public static int fusion_max_size; // Default: 24
    public static int fusion_comparator_max_efficiency;
    public static double fusion_electromagnet_power;
    public static boolean fusion_plasma_craziness;
    public static double fusion_sound_volume;

    public static double[] fusion_fuel_time;
    public static double[] fusion_fuel_heat_generation;
    public static double[] fusion_fuel_optimal_temperature;
    public static double[] fusion_radiation;

    public static int heat_exchanger_min_size; // Default: 1
    public static int heat_exchanger_max_size; // Default: 24
    public static double[] heat_exchanger_heat_transfer_coefficient;
    public static double[] heat_exchanger_heat_retention_mult;
    public static double heat_exchanger_coolant_heat_mult;
    public static boolean heat_exchanger_lmtd;
    public static boolean heat_exchanger_alternate_hps_recipe;
    public static boolean heat_exchanger_alternate_exhaust_recipe;

    public static int turbine_min_size; // Default: 1
    public static int turbine_max_size; // Default: 24
    public static double[] turbine_blade_efficiency;
    public static double[] turbine_blade_expansion;
    public static double turbine_stator_expansion;
    public static double[] turbine_coil_conductivity;
    public static String[] turbine_coil_rule;
    public static String[] turbine_connector_rule;
    //    public static double[] turbine_power_per_mb;
//    public static double[] turbine_expansion_level;
    public static double turbine_spin_up_multiplier_global;
    //    public static double[] turbine_spin_up_multiplier;
    public static double turbine_spin_down_multiplier;
    public static int turbine_mb_per_blade;
    public static double[] turbine_throughput_leniency_params;
    public static double turbine_tension_throughput_factor;
    public static double turbine_tension_leniency;
    public static double turbine_power_bonus_multiplier;
    public static double turbine_sound_volume;
    public static double turbine_particles;
    public static double turbine_render_blade_width;
    public static double turbine_render_rotor_expansion;
    public static double turbine_render_rotor_speed;

    public static boolean quantum_dedicated_server;
    public static int quantum_max_qubits;
    public static int quantum_angle_precision;

    public static int[] tool_mining_level;
    public static int[] tool_durability;
    public static double[] tool_speed;
    public static double[] tool_attack_damage;
    public static int[] tool_enchantability;
    public static double[] tool_handle_modifier;

    public static int[] armor_durability;
    public static double[] armor_toughness;
    public static int[] armor_enchantability;
    public static int[] armor_boron;
    public static int[] armor_tough;
    public static int[] armor_hard_carbon;
    public static int[] armor_boron_nitride;
    public static int[] armor_hazmat;

    public static int entity_tracking_range;

    private static boolean radiation_enabled;
    public static boolean radiation_enabled_public;

    public static String[] radiation_immune_players;
    public static int radiation_world_chunks_per_tick;
    public static int radiation_player_tick_rate;
    public static String[] radiation_worlds;
    public static String[] radiation_biomes;
    public static String[] radiation_structures; // Mineshaft, Village, Fortress, Stronghold, Temple, Monument, EndCity, Mansion
    public static String[] radiation_world_limits;
    public static String[] radiation_biome_limits;
    public static int[] radiation_from_biomes_dims_blacklist;

    public static String[] radiation_ores;
    public static String[] radiation_items;
    public static String[] radiation_blocks;
    public static String[] radiation_fluids;
    public static String[] radiation_foods;
    public static String[] radiation_ores_blacklist;
    public static String[] radiation_items_blacklist;
    public static String[] radiation_blocks_blacklist;
    public static String[] radiation_fluids_blacklist;

    public static double max_player_rads;
    public static double radiation_player_decay_rate;
    public static String[] max_entity_rads;
    public static double radiation_entity_decay_rate;
    public static double radiation_spread_rate;
    public static double radiation_spread_gradient;
    public static double radiation_decay_rate;
    public static double radiation_lowest_rate;
    public static double radiation_chunk_limit;

    public static double[] radiation_sound_volumes;
    public static boolean radiation_check_blocks;
    public static int radiation_block_effect_max_rate;
    public static double radiation_rain_mult;
    public static double radiation_swim_mult;

    public static double radiation_feral_ghoul_attack;

    public static double radiation_radaway_amount;
    public static double radiation_radaway_slow_amount;
    public static double radiation_radaway_rate;
    public static double radiation_radaway_slow_rate;
    public static double radiation_poison_time;
    public static double radiation_radaway_cooldown;
    public static double radiation_rad_x_amount;
    public static double radiation_rad_x_lifetime;
    public static double radiation_rad_x_cooldown;
    public static double[] radiation_shielding_level;
    public static boolean radiation_tile_shielding;
    public static double radiation_scrubber_fraction;
    public static int radiation_scrubber_radius;
    public static boolean radiation_scrubber_non_linear;
    public static double[] radiation_scrubber_param;
    public static int[] radiation_scrubber_time;
    public static int[] radiation_scrubber_power;
    public static double[] radiation_scrubber_efficiency;
    public static double radiation_geiger_block_redstone;

    public static boolean radiation_shielding_default_recipes;
    public static String[] radiation_shielding_item_blacklist;
    public static String[] radiation_shielding_custom_stacks;
    public static String[] radiation_shielding_default_levels;

    public static boolean radiation_tile_entities;
    public static boolean radiation_hardcore_stacks;
    public static double radiation_hardcore_containers;
    public static boolean radiation_dropped_items;
    public static boolean radiation_death_persist;
    public static double radiation_death_persist_fraction;
    public static double radiation_death_immunity_time;

    public static String[] radiation_player_debuff_lists;
    public static String[] radiation_passive_debuff_lists;
    public static String[] radiation_mob_buff_lists;
    public static boolean radiation_player_rads_fatal;
    public static boolean radiation_passive_rads_fatal;
    public static boolean radiation_mob_rads_fatal;

    private static boolean radiation_horse_armor;
    public static boolean radiation_horse_armor_public;

    public static double radiation_hud_size;
    public static double radiation_hud_position;
    public static double[] radiation_hud_position_cartesian;
    public static boolean radiation_hud_text_outline;
    public static boolean radiation_require_counter;
    public static boolean radiation_chunk_boundaries;
    public static int radiation_unit_prefixes;

    public static double radiation_badge_durability;
    public static double radiation_badge_info_rate;

//    public static boolean[] register_processor;
//    public static boolean[] register_passive;
//    public static boolean[] register_battery;
    public static boolean register_quantum;
    public static boolean[] register_tool;
    public static boolean[] register_tic_tool;
    public static boolean[] register_armor;
    public static boolean[] register_conarm_armor;
    public static boolean[] register_entity;
    public static boolean register_fluid_blocks;
    public static boolean register_cofh_fluids;
    public static boolean register_tic_recipes;
    public static boolean register_projecte_emc;

    public static boolean give_guidebook;
    public static boolean single_creative_tab;
    public static boolean ctrl_info;

    public static boolean rare_drops;
    public static boolean dungeon_loot;

    public static boolean wasteland_biome;
    public static int wasteland_biome_weight;

    public static boolean wasteland_dimension_gen;
    public static int wasteland_dimension;

    public static int mushroom_spread_rate;
    public static boolean mushroom_gen;
    public static int mushroom_gen_size;
    public static int mushroom_gen_rate;

    public static ResourceLocation[] corium_solidification;
    public static boolean corium_solidification_list_type;

    public static boolean ore_dict_raw_material_recipes;
//    public static boolean ore_dict_priority_bool;
//    public static String[] ore_dict_priority;
    public static boolean hwyla_enabled;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void onLoad(final ModConfigEvent event) {
        loadConfig();
    }

    public static void loadConfig() {
        ore_dims = syncInts(ORE_DIMS, LIST);
        ore_dims_list_type = ORE_DIMS_LIST_TYPE.getAsBoolean();
        ore_gen = syncBooleans(ORE_GEN, ARRAY);
        ore_size = syncInts(ORE_SIZE, ARRAY);
        ore_rate = syncInts(ORE_RATE, ARRAY);
        ore_min_height = syncInts(ORE_MIN_HEIGHT, ARRAY);
        ore_max_height = syncInts(ORE_MAX_HEIGHT, ARRAY);
        ore_drops = syncBooleans(ORE_DROPS, ARRAY);
        ore_hide_disabled = ORE_HIDE_DISABLED.getAsBoolean();
        ore_harvest_levels = syncInts(ORE_HARVEST_LEVELS, ARRAY);

        wasteland_biome = WASTELAND_BIOME.getAsBoolean();
        wasteland_biome_weight = WASTELAND_BIOME_WEIGHT.getAsInt();
        wasteland_dimension_gen = WASTELAND_DIMENSION_GEN.getAsBoolean();
        wasteland_dimension = WASTELAND_DIMENSION.getAsInt();
        mushroom_spread_rate = MUSHROOM_SPREAD_RATE.getAsInt();
        mushroom_gen = MUSHROOM_GEN.getAsBoolean();
        mushroom_gen_size = MUSHROOM_GEN_SIZE.getAsInt();
        mushroom_gen_rate = MUSHROOM_GEN_RATE.getAsInt();

        processor_time_multiplier = PROCESSOR_TIME_MULTIPLIER.getAsDouble();
        processor_power_multiplier = PROCESSOR_POWER_MULTIPLIER.getAsDouble();
        processor_time = syncInts(PROCESSOR_TIME, ARRAY);
        processor_power = syncInts(PROCESSOR_POWER, ARRAY);
        speed_upgrade_power_laws_fp = syncDoubles(SPEED_UPGRADE_POWER_LAWS_FP, ARRAY);
        speed_upgrade_multipliers_fp = syncDoubles(SPEED_UPGRADE_MULTIPLIERS_FP, ARRAY);
        energy_upgrade_power_laws_fp = syncDoubles(ENERGY_UPGRADE_POWER_LAWS_FP, ARRAY);
        energy_upgrade_multipliers_fp = syncDoubles(ENERGY_UPGRADE_MULTIPLIERS_FP, ARRAY);
        upgrade_stack_sizes = syncInts(UPGRADE_STACK_SIZES, ARRAY);

        enable_mek_gas = ENABLE_MEK_GAS.getAsBoolean();
        enable_fluid_recipe_expansion = syncBooleans(ENABLE_FLUID_RECIPE_EXPANSION, ARRAY);
        machine_update_rate = MACHINE_UPDATE_RATE.getAsInt();
        processor_passive_rate = syncDoubles(PROCESSOR_PASSIVE_RATE, ARRAY);
        passive_push = PASSIVE_PUSH.getAsBoolean();
        cobble_gen_power = COBBLE_GEN_POWER.getAsDouble();
        ore_processing = ORE_PROCESSING.getAsBoolean();
        manufactory_wood = syncInts(MANUFACTORY_WOOD, ARRAY);
        rock_crusher_alternate = ROCK_CRUSHER_ALTERNATE.getAsBoolean();

        default_processor_recipes_global = DEFAULT_PROCESSOR_RECIPES_GLOBAL.getAsBoolean();
        default_processor_recipes = syncBooleans(DEFAULT_PROCESSOR_RECIPES, ARRAY);
//        gtce_recipe_integration_global = GTCE_RECIPE_INTEGRATION_GLOBAL.getAsBoolean();
//        gtce_recipe_integration = syncBooleans(GTCE_RECIPE_INTEGRATION, ARRAY);
//        gtce_recipe_logging = GTCE_RECIPE_LOGGING.getAsBoolean();
        smart_processor_input = SMART_PROCESSOR_INPUT.getAsBoolean();
//        factor_recipes = FACTOR_RECIPES.getAsBoolean();
        processor_particles = PROCESSOR_PARTICLES.getAsBoolean();

        rtg_power = syncInts(RTG_POWER, ARRAY);
        solar_power = syncInts(SOLAR_POWER, ARRAY);
//        decay_lifetime = syncDoubles(DECAY_LIFETIME, ARRAY);
//        decay_power = syncDoubles(DECAY_POWER, ARRAY);

//        battery_block_capacity = syncInts(BATTERY_BLOCK_CAPACITY, ARRAY);
//        battery_block_max_transfer = syncInts(BATTERY_BLOCK_MAX_TRANSFER, ARRAY);
//        battery_block_energy_tier = syncInts(BATTERY_BLOCK_ENERGY_TIER, ARRAY);
//        battery_item_capacity = syncInts(BATTERY_ITEM_CAPACITY, ARRAY);
//        battery_item_max_transfer = syncInts(BATTERY_ITEM_MAX_TRANSFER, ARRAY);
//        battery_item_energy_tier = syncInts(BATTERY_ITEM_ENERGY_TIER, ARRAY);

        machine_min_size = MACHINE_MIN_SIZE.getAsInt();
        machine_max_size = MACHINE_MAX_SIZE.getAsInt();

        machine_electrolyzer_time = MACHINE_ELECTROLYZER_TIME.getAsInt();
        machine_electrolyzer_power = MACHINE_ELECTROLYZER_POWER.getAsInt();
        machine_cathode_efficiency = syncStrings(MACHINE_CATHODE_EFFICIENCY, LIST);
        machine_anode_efficiency = syncStrings(MACHINE_ANODE_EFFICIENCY, LIST);
        machine_electrolyzer_sound_volume = MACHINE_ELECTROLYZER_SOUND_VOLUME.getAsDouble();

        machine_distiller_time = MACHINE_DISTILLER_TIME.getAsInt();
        machine_distiller_power = MACHINE_DISTILLER_POWER.getAsInt();
        machine_distiller_sound_volume = MACHINE_DISTILLER_SOUND_VOLUME.getAsDouble();

        machine_infiltrator_time = MACHINE_INFILTRATOR_TIME.getAsInt();
        machine_infiltrator_power = MACHINE_INFILTRATOR_POWER.getAsInt();
        machine_infiltrator_pressure_fluid_efficiency = syncStrings(MACHINE_INFILTRATOR_PRESSURE_FLUID_EFFICIENCY, LIST);
        machine_infiltrator_sound_volume = MACHINE_INFILTRATOR_SOUND_VOLUME.getAsDouble();

        machine_diaphragm_efficiency = syncDoubles(MACHINE_DIAPHRAGM_EFFICIENCY, ARRAY);
        machine_diaphragm_contact_factor = syncDoubles(MACHINE_DIAPHRAGM_CONTACT_FACTOR, ARRAY);
        machine_sieve_assembly_efficiency = syncDoubles(MACHINE_SIEVE_ASSEMBLY_EFFICIENCY, ARRAY);

        fission_min_size = FISSION_MIN_SIZE.getAsInt();
        fission_max_size = FISSION_MAX_SIZE.getAsInt();
        fission_fuel_time_multiplier = FISSION_FUEL_TIME_MULTIPLIER.getAsDouble();
        fission_fuel_heat_multiplier = FISSION_FUEL_HEAT_MULTIPLIER.getAsDouble();
        fission_fuel_efficiency_multiplier = FISSION_FUEL_EFFICIENCY_MULTIPLIER.getAsDouble();
        fission_fuel_radiation_multiplier = FISSION_FUEL_RADIATION_MULTIPLIER.getAsDouble();
        fission_source_efficiency = syncDoubles(FISSION_SOURCE_EFFICIENCY, ARRAY);
        fission_sink_cooling_rate = syncInts(FISSION_SINK_COOLING_RATE, ARRAY);
        fission_sink_rule = syncStrings(FISSION_SINK_RULE, ARRAY);
        fission_heater_cooling_rate = syncInts(FISSION_HEATER_COOLING_RATE, ARRAY);
        fission_heater_rule = syncStrings(FISSION_HEATER_RULE, ARRAY);
//        fission_moderator_flux_factor = syncInts(FISSION_MODERATOR_FLUX_FACTOR, ARRAY);
//        fission_moderator_efficiency = syncDoubles(FISSION_MODERATOR_EFFICIENCY, ARRAY);
//        fission_reflector_efficiency = syncDoubles(FISSION_REFLECTOR_EFFICIENCY, ARRAY);
//        fission_reflector_reflectivity = syncDoubles(FISSION_REFLECTOR_REFLECTIVITY, ARRAY);
        fission_shield_heat_per_flux = syncDoubles(FISSION_SHIELD_HEAT_PER_FLUX, ARRAY);
        fission_shield_efficiency = syncDoubles(FISSION_SHIELD_EFFICIENCY, ARRAY);
//        fission_irradiator_heat_per_flux = syncDoubles(FISSION_IRRADIATOR_HEAT_PER_FLUX, ARRAY);
//        fission_irradiator_efficiency = syncDoubles(FISSION_IRRADIATOR_EFFICIENCY, ARRAY);
        fission_cooling_efficiency_leniency = FISSION_COOLING_EFFICIENCY_LENIENCY.getAsInt();
        fission_sparsity_penalty_params = syncDoubles(FISSION_SPARSITY_PENALTY_PARAMS, ARRAY);
        fission_heating_coolant_heat_mult = FISSION_HEATING_COOLANT_HEAT_MULT.getAsDouble();

        fission_decay_mechanics = FISSION_DECAY_MECHANICS.getAsBoolean();
        fission_decay_build_up_times = syncDoubles(FISSION_DECAY_BUILD_UP_TIMES, ARRAY);
        fission_decay_lifetimes = syncDoubles(FISSION_DECAY_LIFETIMES, ARRAY);
        fission_decay_equilibrium_factors = syncDoubles(FISSION_DECAY_EQUILIBRIUM_FACTORS, ARRAY);
        fission_decay_daughter_multipliers = syncDoubles(FISSION_DECAY_DAUGHTER_MULTIPLIERS, ARRAY);
        fission_decay_term_multipliers = syncDoubles(FISSION_DECAY_TERM_MULTIPLIERS, ARRAY);
        fission_overheat = FISSION_OVERHEAT.getAsBoolean();
        fission_meltdown_radiation_multiplier = FISSION_MELTDOWN_RADIATION_MULTIPLIER.getAsDouble();
        fission_heat_damage = FISSION_HEAT_DAMAGE.getAsBoolean();
        fission_neutron_reach = FISSION_NEUTRON_REACH.getAsInt();
        fission_heat_dissipation = syncBooleans(FISSION_HEAT_DISSIPATION, ARRAY);
        fission_heat_dissipation_rate = FISSION_HEAT_DISSIPATION_RATE.getAsDouble();
        fission_emergency_cooling_multiplier = FISSION_EMERGENCY_COOLING_MULTIPLIER.getAsDouble();
        fission_sound_volume = FISSION_SOUND_VOLUME.getAsDouble();

//        fission_thorium_fuel_time = syncInts(FISSION_THORIUM_FUEL_TIME, ARRAY);
//        fission_thorium_heat_generation = syncInts(FISSION_THORIUM_HEAT_GENERATION, ARRAY);
//        fission_thorium_efficiency = syncDoubles(FISSION_THORIUM_EFFICIENCY, ARRAY);
//        fission_thorium_criticality = syncInts(FISSION_THORIUM_CRITICALITY, ARRAY);
//        fission_thorium_decay_factor = syncDoubles(FISSION_THORIUM_DECAY_FACTOR, ARRAY);
//        fission_thorium_self_priming = syncBooleans(FISSION_THORIUM_SELF_PRIMING, ARRAY);
//        fission_thorium_radiation = syncDoubles(FISSION_THORIUM_RADIATION, ARRAY);

//        fission_uranium_fuel_time = syncInts(FISSION_URANIUM_FUEL_TIME, ARRAY);
//        fission_uranium_heat_generation = syncInts(FISSION_URANIUM_HEAT_GENERATION, ARRAY);
//        fission_uranium_efficiency = syncDoubles(FISSION_URANIUM_EFFICIENCY, ARRAY);
//        fission_uranium_criticality = syncInts(FISSION_URANIUM_CRITICALITY, ARRAY);
//        fission_uranium_decay_factor = syncDoubles(FISSION_URANIUM_DECAY_FACTOR, ARRAY);
//        fission_uranium_self_priming = syncBooleans(FISSION_URANIUM_SELF_PRIMING, ARRAY);
////        fission_uranium_radiation = syncDoubles(FISSION_URANIUM_RADIATION, ARRAY);
//
//        fission_neptunium_fuel_time = syncInts(FISSION_NEPTUNIUM_FUEL_TIME, ARRAY);
//        fission_neptunium_heat_generation = syncInts(FISSION_NEPTUNIUM_HEAT_GENERATION, ARRAY);
//        fission_neptunium_efficiency = syncDoubles(FISSION_NEPTUNIUM_EFFICIENCY, ARRAY);
//        fission_neptunium_criticality = syncInts(FISSION_NEPTUNIUM_CRITICALITY, ARRAY);
//        fission_neptunium_decay_factor = syncDoubles(FISSION_NEPTUNIUM_DECAY_FACTOR, ARRAY);
//        fission_neptunium_self_priming = syncBooleans(FISSION_NEPTUNIUM_SELF_PRIMING, ARRAY);
////        fission_neptunium_radiation = syncDoubles(FISSION_NEPTUNIUM_RADIATION, ARRAY);
//
//        fission_plutonium_fuel_time = syncInts(FISSION_PLUTONIUM_FUEL_TIME, ARRAY);
//        fission_plutonium_heat_generation = syncInts(FISSION_PLUTONIUM_HEAT_GENERATION, ARRAY);
//        fission_plutonium_efficiency = syncDoubles(FISSION_PLUTONIUM_EFFICIENCY, ARRAY);
//        fission_plutonium_criticality = syncInts(FISSION_PLUTONIUM_CRITICALITY, ARRAY);
//        fission_plutonium_decay_factor = syncDoubles(FISSION_PLUTONIUM_DECAY_FACTOR, ARRAY);
//        fission_plutonium_self_priming = syncBooleans(FISSION_PLUTONIUM_SELF_PRIMING, ARRAY);
////        fission_plutonium_radiation = syncDoubles(FISSION_PLUTONIUM_RADIATION, ARRAY);
//
//        fission_mixed_fuel_time = syncInts(FISSION_MIXED_FUEL_TIME, ARRAY);
//        fission_mixed_heat_generation = syncInts(FISSION_MIXED_HEAT_GENERATION, ARRAY);
//        fission_mixed_efficiency = syncDoubles(FISSION_MIXED_EFFICIENCY, ARRAY);
//        fission_mixed_criticality = syncInts(FISSION_MIXED_CRITICALITY, ARRAY);
//        fission_mixed_decay_factor = syncDoubles(FISSION_MIXED_DECAY_FACTOR, ARRAY);
//        fission_mixed_self_priming = syncBooleans(FISSION_MIXED_SELF_PRIMING, ARRAY);
////        fission_mixed_radiation = syncDoubles(FISSION_MIXED_RADIATION, ARRAY);
//
//        fission_americium_fuel_time = syncInts(FISSION_AMERICIUM_FUEL_TIME, ARRAY);
//        fission_americium_heat_generation = syncInts(FISSION_AMERICIUM_HEAT_GENERATION, ARRAY);
//        fission_americium_efficiency = syncDoubles(FISSION_AMERICIUM_EFFICIENCY, ARRAY);
//        fission_americium_criticality = syncInts(FISSION_AMERICIUM_CRITICALITY, ARRAY);
//        fission_americium_decay_factor = syncDoubles(FISSION_AMERICIUM_DECAY_FACTOR, ARRAY);
//        fission_americium_self_priming = syncBooleans(FISSION_AMERICIUM_SELF_PRIMING, ARRAY);
////        fission_americium_radiation = syncDoubles(FISSION_AMERICIUM_RADIATION, ARRAY);
//
//        fission_curium_fuel_time = syncInts(FISSION_CURIUM_FUEL_TIME, ARRAY);
//        fission_curium_heat_generation = syncInts(FISSION_CURIUM_HEAT_GENERATION, ARRAY);
//        fission_curium_efficiency = syncDoubles(FISSION_CURIUM_EFFICIENCY, ARRAY);
//        fission_curium_criticality = syncInts(FISSION_CURIUM_CRITICALITY, ARRAY);
//        fission_curium_decay_factor = syncDoubles(FISSION_CURIUM_DECAY_FACTOR, ARRAY);
//        fission_curium_self_priming = syncBooleans(FISSION_CURIUM_SELF_PRIMING, ARRAY);
////        fission_curium_radiation = syncDoubles(FISSION_CURIUM_RADIATION, ARRAY);
//
//        fission_berkelium_fuel_time = syncInts(FISSION_BERKELIUM_FUEL_TIME, ARRAY);
//        fission_berkelium_heat_generation = syncInts(FISSION_BERKELIUM_HEAT_GENERATION, ARRAY);
//        fission_berkelium_efficiency = syncDoubles(FISSION_BERKELIUM_EFFICIENCY, ARRAY);
//        fission_berkelium_criticality = syncInts(FISSION_BERKELIUM_CRITICALITY, ARRAY);
//        fission_berkelium_decay_factor = syncDoubles(FISSION_BERKELIUM_DECAY_FACTOR, ARRAY);
//        fission_berkelium_self_priming = syncBooleans(FISSION_BERKELIUM_SELF_PRIMING, ARRAY);
////        fission_berkelium_radiation = syncDoubles(FISSION_BERKELIUM_RADIATION, ARRAY);
//
//        fission_californium_fuel_time = syncInts(FISSION_CALIFORNIUM_FUEL_TIME, ARRAY);
//        fission_californium_heat_generation = syncInts(FISSION_CALIFORNIUM_HEAT_GENERATION, ARRAY);
//        fission_californium_efficiency = syncDoubles(FISSION_CALIFORNIUM_EFFICIENCY, ARRAY);
//        fission_californium_criticality = syncInts(FISSION_CALIFORNIUM_CRITICALITY, ARRAY);
//        fission_californium_decay_factor = syncDoubles(FISSION_CALIFORNIUM_DECAY_FACTOR, ARRAY);
//        fission_californium_self_priming = syncBooleans(FISSION_CALIFORNIUM_SELF_PRIMING, ARRAY);
////        fission_californium_radiation = syncDoubles(FISSION_CALIFORNIUM_RADIATION, ARRAY);

        fusion_fuel_time_multiplier = FUSION_FUEL_TIME_MULTIPLIER.getAsDouble();
        fusion_fuel_heat_multiplier = FUSION_FUEL_HEAT_MULTIPLIER.getAsDouble();
        fusion_fuel_efficiency_multiplier = FUSION_FUEL_EFFICIENCY_MULTIPLIER.getAsDouble();
        fusion_fuel_radiation_multiplier = FUSION_FUEL_RADIATION_MULTIPLIER.getAsDouble();
        fusion_overheat = FUSION_OVERHEAT.getAsBoolean();
        fusion_meltdown_radiation_multiplier = FUSION_MELTDOWN_RADIATION_MULTIPLIER.getAsDouble();
        fusion_min_size = FUSION_MIN_SIZE.getAsInt();
        fusion_max_size = FUSION_MAX_SIZE.getAsInt();
        fusion_comparator_max_efficiency = FUSION_COMPARATOR_MAX_EFFICIENCY.getAsInt();
        fusion_electromagnet_power = FUSION_ELECTROMAGNET_POWER.getAsDouble();
        fusion_plasma_craziness = FUSION_PLASMA_CRAZINESS.getAsBoolean();
        fusion_sound_volume = FUSION_SOUND_VOLUME.getAsDouble();

        fusion_fuel_time = syncDoubles(FUSION_FUEL_TIME, ARRAY);
        fusion_fuel_heat_generation = syncDoubles(FUSION_FUEL_HEAT_GENERATION, ARRAY);
        // TODO: multiply by R
        fusion_fuel_optimal_temperature = syncDoubles(FUSION_FUEL_OPTIMAL_TEMPERATURE, ARRAY);
//        fusion_radiation = syncDoubles(FUSION_RADIATION, ARRAY);

        heat_exchanger_min_size = HEAT_EXCHANGER_MIN_SIZE.getAsInt();
        heat_exchanger_max_size = HEAT_EXCHANGER_MAX_SIZE.getAsInt();
        heat_exchanger_heat_transfer_coefficient = syncDoubles(HEAT_EXCHANGER_HEAT_TRANSFER_COEFFICIENT, ARRAY);
        heat_exchanger_heat_retention_mult = syncDoubles(HEAT_EXCHANGER_HEAT_RETENTION_MULT, ARRAY);
        heat_exchanger_coolant_heat_mult = HEAT_EXCHANGER_COOLANT_HEAT_MULT.getAsDouble();
        heat_exchanger_lmtd = HEAT_EXCHANGER_LMTD.getAsBoolean();
        heat_exchanger_alternate_hps_recipe = HEAT_EXCHANGER_ALTERNATE_HPS_RECIPE.getAsBoolean();
        heat_exchanger_alternate_exhaust_recipe = HEAT_EXCHANGER_ALTERNATE_EXHAUST_RECIPE.getAsBoolean();

        turbine_min_size = TURBINE_MIN_SIZE.getAsInt();
        turbine_max_size = TURBINE_MAX_SIZE.getAsInt();
        turbine_blade_efficiency = syncDoubles(TURBINE_BLADE_EFFICIENCY, ARRAY);
        turbine_blade_expansion = syncDoubles(TURBINE_BLADE_EXPANSION, ARRAY);
        turbine_stator_expansion = TURBINE_STATOR_EXPANSION.getAsDouble();
        turbine_coil_conductivity = syncDoubles(TURBINE_COIL_CONDUCTIVITY, ARRAY);
        turbine_coil_rule = syncStrings(TURBINE_COIL_RULE, ARRAY);
        turbine_connector_rule = syncStrings(TURBINE_CONNECTOR_RULE, ARRAY);
//        turbine_power_per_mb = syncDoubles(TURBINE_POWER_PER_MB, ARRAY);
//        turbine_expansion_level = syncDoubles(TURBINE_EXPANSION_LEVEL, ARRAY);
        turbine_spin_up_multiplier_global = TURBINE_SPIN_UP_MULTIPLIER_GLOBAL.getAsDouble();
//        turbine_spin_up_multiplier = syncDoubles(TURBINE_SPIN_UP_MULTIPLIER, ARRAY);
        turbine_spin_down_multiplier = TURBINE_SPIN_DOWN_MULTIPLIER.getAsDouble();
        turbine_mb_per_blade = TURBINE_MB_PER_BLADE.getAsInt();
        turbine_throughput_leniency_params = syncDoubles(TURBINE_THROUGHPUT_LENIENCY_PARAMS, ARRAY);
        turbine_tension_throughput_factor = TURBINE_TENSION_THROUGHPUT_FACTOR.getAsDouble();
        turbine_tension_leniency = TURBINE_TENSION_LENIENCY.getAsDouble();
        turbine_power_bonus_multiplier = TURBINE_POWER_BONUS_MULTIPLIER.getAsDouble();
        turbine_sound_volume = TURBINE_SOUND_VOLUME.getAsDouble();
        turbine_particles = TURBINE_PARTICLES.getAsDouble();
        turbine_render_blade_width = TURBINE_RENDER_BLADE_WIDTH.getAsDouble();
        turbine_render_rotor_expansion = TURBINE_RENDER_ROTOR_EXPANSION.getAsDouble();
        turbine_render_rotor_speed = TURBINE_RENDER_ROTOR_SPEED.getAsDouble();

        quantum_dedicated_server = QUANTUM_DEDICATED_SERVER.getAsBoolean();
        quantum_max_qubits = QUANTUM_MAX_QUBITS.getAsInt();
        quantum_angle_precision = QUANTUM_ANGLE_PRECISION.getAsInt();

        tool_mining_level = syncInts(TOOL_MINING_LEVEL, ARRAY);
        tool_durability = syncInts(TOOL_DURABILITY, ARRAY);
        tool_speed = syncDoubles(TOOL_SPEED, ARRAY);
        tool_attack_damage = syncDoubles(TOOL_ATTACK_DAMAGE, ARRAY);
        tool_enchantability = syncInts(TOOL_ENCHANTABILITY, ARRAY);
        tool_handle_modifier = syncDoubles(TOOL_HANDLE_MODIFIER, ARRAY);

        armor_durability = syncInts(ARMOR_DURABILITY, ARRAY);
        armor_toughness = syncDoubles(ARMOR_TOUGHNESS, ARRAY);
        armor_enchantability = syncInts(ARMOR_ENCHANTABILITY, ARRAY);
        armor_boron = syncInts(ARMOR_BORON, ARRAY);
        armor_tough = syncInts(ARMOR_TOUGH, ARRAY);
        armor_hard_carbon = syncInts(ARMOR_HARD_CARBON, ARRAY);
        armor_boron_nitride = syncInts(ARMOR_BORON_NITRIDE, ARRAY);
        armor_hazmat = syncInts(ARMOR_HAZMAT, ARRAY);

        entity_tracking_range = ENTITY_TRACKING_RANGE.getAsInt();

        radiation_enabled = RADIATION_ENABLED.getAsBoolean();

        radiation_immune_players = syncStrings(RADIATION_IMMUNE_PLAYERS, LIST);
        radiation_world_chunks_per_tick = RADIATION_WORLD_CHUNKS_PER_TICK.getAsInt();
        radiation_player_tick_rate = RADIATION_PLAYER_TICK_RATE.getAsInt();
        radiation_worlds = syncStrings(RADIATION_WORLDS, LIST);
        radiation_biomes = syncStrings(RADIATION_BIOMES, LIST);
        radiation_structures = syncStrings(RADIATION_STRUCTURES, LIST);
        radiation_world_limits = syncStrings(RADIATION_WORLD_LIMITS, LIST);
        radiation_biome_limits = syncStrings(RADIATION_BIOME_LIMITS, LIST);
        radiation_from_biomes_dims_blacklist = syncInts(RADIATION_FROM_BIOMES_DIMS_BLACKLIST, LIST);

//        radiation_ores = syncStrings(RADIATION_ORES, LIST);
        radiation_items = syncStrings(RADIATION_ITEMS, LIST);
        radiation_blocks = syncStrings(RADIATION_BLOCKS, LIST);
        radiation_fluids = syncStrings(RADIATION_FLUIDS, LIST);
        radiation_foods = syncStrings(RADIATION_FOODS, LIST);
        radiation_ores_blacklist = syncStrings(RADIATION_ORES_BLACKLIST, LIST);
        radiation_items_blacklist = syncStrings(RADIATION_ITEMS_BLACKLIST, LIST);
        radiation_blocks_blacklist = syncStrings(RADIATION_BLOCKS_BLACKLIST, LIST);
        radiation_fluids_blacklist = syncStrings(RADIATION_FLUIDS_BLACKLIST, LIST);

        max_player_rads = MAX_PLAYER_RADS.getAsDouble();
        radiation_player_decay_rate = RADIATION_PLAYER_DECAY_RATE.getAsDouble();
        max_entity_rads = syncStrings(MAX_ENTITY_RADS, LIST);
        radiation_entity_decay_rate = RADIATION_ENTITY_DECAY_RATE.getAsDouble();
        radiation_spread_rate = RADIATION_SPREAD_RATE.getAsDouble();
        radiation_spread_gradient = RADIATION_SPREAD_GRADIENT.getAsDouble();
        radiation_decay_rate = RADIATION_DECAY_RATE.getAsDouble();
        radiation_lowest_rate = RADIATION_LOWEST_RATE.getAsDouble();
        radiation_chunk_limit = RADIATION_CHUNK_LIMIT.getAsDouble();

        radiation_sound_volumes = syncDoubles(RADIATION_SOUND_VOLUMES, ARRAY);
        radiation_check_blocks = RADIATION_CHECK_BLOCKS.getAsBoolean();
        radiation_block_effect_max_rate = RADIATION_BLOCK_EFFECT_MAX_RATE.getAsInt();
        radiation_rain_mult = RADIATION_RAIN_MULT.getAsDouble();
        radiation_swim_mult = RADIATION_SWIM_MULT.getAsDouble();

//        radiation_feral_ghoul_attack = RADIATION_FERAL_GHOUL_ATTACK.getAsDouble();

        radiation_radaway_amount = RADIATION_RADAWAY_AMOUNT.getAsDouble();
        radiation_radaway_slow_amount = RADIATION_RADAWAY_SLOW_AMOUNT.getAsDouble();
        radiation_radaway_rate = RADIATION_RADAWAY_RATE.getAsDouble();
        radiation_radaway_slow_rate = RADIATION_RADAWAY_SLOW_RATE.getAsDouble();
        radiation_poison_time = RADIATION_POISON_TIME.getAsDouble();
        radiation_radaway_cooldown = RADIATION_RADAWAY_COOLDOWN.getAsDouble();
        radiation_rad_x_amount = RADIATION_RAD_X_AMOUNT.getAsDouble();
        radiation_rad_x_lifetime = RADIATION_RAD_X_LIFETIME.getAsDouble();
        radiation_rad_x_cooldown = RADIATION_RAD_X_COOLDOWN.getAsDouble();
        radiation_shielding_level = syncDoubles(RADIATION_SHIELDING_LEVEL, ARRAY);
        radiation_tile_shielding = RADIATION_TILE_SHIELDING.getAsBoolean();
        radiation_scrubber_fraction = RADIATION_SCRUBBER_FRACTION.getAsDouble();
        radiation_scrubber_radius = RADIATION_SCRUBBER_RADIUS.getAsInt();
        radiation_scrubber_non_linear = RADIATION_SCRUBBER_NON_LINEAR.getAsBoolean();
        radiation_scrubber_param = syncDoubles(RADIATION_SCRUBBER_PARAM, ARRAY);
        radiation_scrubber_time = syncInts(RADIATION_SCRUBBER_TIME, ARRAY);
        radiation_scrubber_power = syncInts(RADIATION_SCRUBBER_POWER, ARRAY);
        radiation_scrubber_efficiency = syncDoubles(RADIATION_SCRUBBER_EFFICIENCY, ARRAY);
        radiation_geiger_block_redstone = RADIATION_GEIGER_BLOCK_REDSTONE.getAsDouble();

        radiation_shielding_default_recipes = RADIATION_SHIELDING_DEFAULT_RECIPES.getAsBoolean();
        radiation_shielding_item_blacklist = syncStrings(RADIATION_SHIELDING_ITEM_BLACKLIST, LIST);
        radiation_shielding_custom_stacks = syncStrings(RADIATION_SHIELDING_CUSTOM_STACKS, LIST);
        radiation_shielding_default_levels = syncStrings(RADIATION_SHIELDING_DEFAULT_LEVELS, LIST);

        radiation_tile_entities = RADIATION_TILE_ENTITIES.getAsBoolean();
        radiation_hardcore_stacks = RADIATION_HARDCORE_STACKS.getAsBoolean();
        radiation_hardcore_containers = RADIATION_HARDCORE_CONTAINERS.getAsDouble();
        radiation_dropped_items = RADIATION_DROPPED_ITEMS.getAsBoolean();
        radiation_death_persist = RADIATION_DEATH_PERSIST.getAsBoolean();
        radiation_death_persist_fraction = RADIATION_DEATH_PERSIST_FRACTION.getAsDouble();
        radiation_death_immunity_time = RADIATION_DEATH_IMMUNITY_TIME.getAsDouble();

        radiation_player_debuff_lists = syncStrings(RADIATION_PLAYER_DEBUFF_LISTS, LIST);
        radiation_passive_debuff_lists = syncStrings(RADIATION_PASSIVE_DEBUFF_LISTS, LIST);
        radiation_mob_buff_lists = syncStrings(RADIATION_MOB_BUFF_LISTS, LIST);
        radiation_player_rads_fatal = RADIATION_PLAYER_RADS_FATAL.getAsBoolean();
        radiation_passive_rads_fatal = RADIATION_PASSIVE_RADS_FATAL.getAsBoolean();
        radiation_mob_rads_fatal = RADIATION_MOB_RADS_FATAL.getAsBoolean();

        radiation_horse_armor = RADIATION_HORSE_ARMOR.getAsBoolean();

        radiation_hud_size = RADIATION_HUD_SIZE.getAsDouble();
        radiation_hud_position = RADIATION_HUD_POSITION.getAsDouble();
        radiation_hud_position_cartesian = syncDoubles(RADIATION_HUD_POSITION_CARTESIAN, LIST);
        radiation_hud_text_outline = RADIATION_HUD_TEXT_OUTLINE.getAsBoolean();
        radiation_require_counter = RADIATION_REQUIRE_COUNTER.getAsBoolean();
        radiation_chunk_boundaries = RADIATION_CHUNK_BOUNDARIES.getAsBoolean();
        radiation_unit_prefixes = RADIATION_UNIT_PREFIXES.getAsInt();

        radiation_badge_durability = RADIATION_BADGE_DURABILITY.getAsDouble();
        radiation_badge_info_rate = RADIATION_BADGE_INFO_RATE.getAsDouble();

//        register_processor = syncBooleans(REGISTER_PROCESSOR, ARRAY);
//        register_passive = syncBooleans(REGISTER_PASSIVE, ARRAY);
//        register_battery = syncBooleans(REGISTER_BATTERY, ARRAY);
        register_quantum = REGISTER_QUANTUM.getAsBoolean();
        register_tool = syncBooleans(REGISTER_TOOL, ARRAY);
        register_tic_tool = syncBooleans(REGISTER_TIC_TOOL, ARRAY);
        register_armor = syncBooleans(REGISTER_ARMOR, ARRAY);
        register_conarm_armor = syncBooleans(REGISTER_CONARM_ARMOR, ARRAY);
        register_entity = syncBooleans(REGISTER_ENTITY, ARRAY);
        register_fluid_blocks = REGISTER_FLUID_BLOCKS.getAsBoolean();
        register_cofh_fluids = REGISTER_COFH_FLUIDS.getAsBoolean();
        register_tic_recipes = REGISTER_TIC_RECIPES.getAsBoolean();
        register_projecte_emc = REGISTER_PROJECTE_EMC.getAsBoolean();

        give_guidebook = GIVE_GUIDEBOOK.getAsBoolean();
        single_creative_tab = SINGLE_CREATIVE_TAB.getAsBoolean();
        ctrl_info = CTRL_INFO.getAsBoolean();
        rare_drops = RARE_DROPS.getAsBoolean();
        dungeon_loot = DUNGEON_LOOT.getAsBoolean();
        corium_solidification = syncResourceLocations(CORIUM_SOLIDIFICATION, LIST);
        corium_solidification_list_type = CORIUM_SOLIDIFICATION_LIST_TYPE.getAsBoolean();
//        ore_dict_raw_material_recipes = ORE_DICT_RAW_MATERIAL_RECIPES.getAsBoolean();
//        ore_dict_priority_bool = ORE_DICT_PRIORITY_BOOL.getAsBoolean();
//        ore_dict_priority = syncStrings(ORE_DICT_PRIORITY, LIST);
        hwyla_enabled = HWYLA_ENABLED.getAsBoolean();

        radiation_enabled_public = radiation_enabled;
        radiation_horse_armor_public = radiation_horse_armor;
    }

    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ORE_DIMS = add(CATEGORY_WORLD_GEN, "ore_dims", List.of(0, 2, -6, -100, 4598, -9999, -11325), Integer.MIN_VALUE, Integer.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue ORE_DIMS_LIST_TYPE = add(CATEGORY_WORLD_GEN, "ore_dims_list_type", false);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> ORE_GEN = add(CATEGORY_WORLD_GEN, "ore_gen", List.of(true, true, true, true, true, true, true, true), ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ORE_SIZE = add(CATEGORY_WORLD_GEN, "ore_size", List.of(6, 6, 6, 3, 3, 4, 4, 5), 1, Integer.MAX_VALUE, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ORE_RATE = add(CATEGORY_WORLD_GEN, "ore_rate", List.of(5, 4, 6, 3, 6, 6, 6, 4), 1, Integer.MAX_VALUE, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ORE_MIN_HEIGHT = add(CATEGORY_WORLD_GEN, "ore_min_height", List.of(0, 0, 0, 0, 0, 0, 0, 0), -63, 255, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ORE_MAX_HEIGHT = add(CATEGORY_WORLD_GEN, "ore_max_height", List.of(48, 40, 36, 32, 32, 28, 28, 24), 1, 255, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> ORE_DROPS = add(CATEGORY_WORLD_GEN, "ore_drops", List.of(false, false, false, false, false, false, false), ARRAY);
    private static final ModConfigSpec.BooleanValue ORE_HIDE_DISABLED = add(CATEGORY_WORLD_GEN, "ore_hide_disabled", false);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ORE_HARVEST_LEVELS = add(CATEGORY_WORLD_GEN, "ore_harvest_levels", List.of(1, 1, 1, 2, 2, 2, 2, 2), 0, 15, ARRAY);

    private static final ModConfigSpec.BooleanValue WASTELAND_BIOME = add(CATEGORY_WORLD_GEN, "wasteland_biome", true);
    private static final ModConfigSpec.IntValue WASTELAND_BIOME_WEIGHT = add(CATEGORY_WORLD_GEN, "wasteland_biome_weight", 5, 0, 255);
    private static final ModConfigSpec.BooleanValue WASTELAND_DIMENSION_GEN = add(CATEGORY_WORLD_GEN, "wasteland_dimension_gen", true);
    private static final ModConfigSpec.IntValue WASTELAND_DIMENSION = add(CATEGORY_WORLD_GEN, "wasteland_dimension", 4598, Integer.MIN_VALUE, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue MUSHROOM_SPREAD_RATE = add(CATEGORY_WORLD_GEN, "mushroom_spread_rate", 16, 0, 511);
    private static final ModConfigSpec.BooleanValue MUSHROOM_GEN = add(CATEGORY_WORLD_GEN, "mushroom_gen", true);
    private static final ModConfigSpec.IntValue MUSHROOM_GEN_SIZE = add(CATEGORY_WORLD_GEN, "mushroom_gen_size", 32, 0, 511);
    private static final ModConfigSpec.IntValue MUSHROOM_GEN_RATE = add(CATEGORY_WORLD_GEN, "mushroom_gen_rate", 64, 0, 511);

    private static final ModConfigSpec.DoubleValue PROCESSOR_TIME_MULTIPLIER = add(CATEGORY_PROCESSOR, "processor_time_multiplier", 1D, 0.001D, 255D);
    private static final ModConfigSpec.DoubleValue PROCESSOR_POWER_MULTIPLIER = add(CATEGORY_PROCESSOR, "processor_power_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> PROCESSOR_TIME = add(CATEGORY_PROCESSOR, "processor_time", List.of(400, 800, 800, 400, 400, 600, 800, 600, 3200, 600, 400, 600, 800, 600, 1600, 600, 2400, 1200, 400, 200), 1, 128000, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> PROCESSOR_POWER = add(CATEGORY_PROCESSOR, "processor_power", List.of(20, 10, 10, 20, 10, 10, 40, 20, 40, 10, 0, 40, 10, 20, 10, 10, 10, 10, 20, 20), 0, 16000, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> SPEED_UPGRADE_POWER_LAWS_FP = add(CATEGORY_PROCESSOR, "speed_upgrade_power_laws_fp", List.of(1D, 2D, 2D, 1D), 1D, 15D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> SPEED_UPGRADE_MULTIPLIERS_FP = add(CATEGORY_PROCESSOR, "speed_upgrade_multipliers_fp", List.of(1D, 1D, 1D, 1D), 0D, 15D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> ENERGY_UPGRADE_POWER_LAWS_FP = add(CATEGORY_PROCESSOR, "energy_upgrade_power_laws_fp", List.of(1D, 1D), 1D, 15D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> ENERGY_UPGRADE_MULTIPLIERS_FP = add(CATEGORY_PROCESSOR, "energy_upgrade_multipliers_fp", List.of(1D, 1D), 0D, 15D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> UPGRADE_STACK_SIZES = add(CATEGORY_PROCESSOR, "upgrade_stack_sizes", List.of(64, 64), 1, 64, ARRAY);

    private static final ModConfigSpec.IntValue RF_PER_EU = add(CATEGORY_PROCESSOR, "rf_per_eu", 16, 1, 65536);
    private static final ModConfigSpec.BooleanValue ENABLE_GTCE_EU = add(CATEGORY_PROCESSOR, "enable_gtce_eu", true);
    private static final ModConfigSpec.BooleanValue ENABLE_MEK_GAS = add(CATEGORY_PROCESSOR, "enable_mek_gas", true);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> ENABLE_FLUID_RECIPE_EXPANSION = add(CATEGORY_PROCESSOR, "enable_fluid_recipe_expansion", List.of(true, true), ARRAY);
    private static final ModConfigSpec.IntValue MACHINE_UPDATE_RATE = add(CATEGORY_PROCESSOR, "machine_update_rate", 20, 1, 1200);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> PROCESSOR_PASSIVE_RATE = add(CATEGORY_PROCESSOR, "processor_passive_rate", List.of(0.125, 10.0, 5.0), 0D, 4000D, ARRAY);
    private static final ModConfigSpec.BooleanValue PASSIVE_PUSH = add(CATEGORY_PROCESSOR, "passive_push", true);
    private static final ModConfigSpec.DoubleValue COBBLE_GEN_POWER = add(CATEGORY_PROCESSOR, "cobble_gen_power", 0D, 0D, 255D);
    private static final ModConfigSpec.BooleanValue ORE_PROCESSING = add(CATEGORY_PROCESSOR, "ore_processing", true);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> MANUFACTORY_WOOD = add(CATEGORY_PROCESSOR, "manufactory_wood", List.of(6, 4), 1, 64, ARRAY);
    private static final ModConfigSpec.BooleanValue ROCK_CRUSHER_ALTERNATE = add(CATEGORY_PROCESSOR, "rock_crusher_alternate", false);

    private static final ModConfigSpec.BooleanValue DEFAULT_PROCESSOR_RECIPES_GLOBAL = add(CATEGORY_PROCESSOR, "default_processor_recipes_global", true);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> DEFAULT_PROCESSOR_RECIPES = add(CATEGORY_PROCESSOR, "default_processor_recipes", List.of(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true), ARRAY);
    // TODO   private static final ModConfigSpec.BooleanValue GTCE_RECIPE_INTEGRATION_GLOBAL = add(CATEGORY_PROCESSOR, "gtce_recipe_integration_global", true);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> GTCE_RECIPE_INTEGRATION = add(CATEGORY_PROCESSOR, "gtce_recipe_integration", List.of(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true), ARRAY);
//    private static final ModConfigSpec.BooleanValue GTCE_RECIPE_LOGGING = add(CATEGORY_PROCESSOR, "gtce_recipe_logging", false);
    private static final ModConfigSpec.BooleanValue SMART_PROCESSOR_INPUT = add(CATEGORY_PROCESSOR, "smart_processor_input", true);
    //    private static final ModConfigSpec.BooleanValue FACTOR_RECIPES = add(CATEGORY_PROCESSOR, "factor_recipes", false);
    private static final ModConfigSpec.BooleanValue PROCESSOR_PARTICLES = add(CATEGORY_PROCESSOR, "processor_particles", true);

    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> RTG_POWER = add(CATEGORY_GENERATOR, "rtg_power", List.of(1, 40, 10, 200), 1, Integer.MAX_VALUE, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> SOLAR_POWER = add(CATEGORY_GENERATOR, "solar_power", List.of(5, 20, 80, 320), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> DECAY_LIFETIME = add(CATEGORY_GENERATOR, "decay_lifetime", List.of(12000D / 0.75D, 12000D / 1.2D, 1200D, 12000D / 2.2D, 12000D / 3D, 12000D / 18D, 12000D / 28D, 12000D / 80D, 12000D / 1000D), 1D, 16777215D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> DECAY_POWER = add(CATEGORY_GENERATOR, "decay_power", List.of(0.75D, 1.2D, 1D, 2.2D, 3D, 18D, 28D, 80D, 1000D), 0D, 32767D, ARRAY);

//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> BATTERY_BLOCK_CAPACITY = add(CATEGORY_ENERGY_STORAGE, "battery_block_capacity", List.of(1600000, 6400000, 25600000, 102400000, 32000000, 128000000, 512000000, 2048000000), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> BATTERY_BLOCK_MAX_TRANSFER = add(CATEGORY_ENERGY_STORAGE, "battery_block_max_transfer", List.of(16000, 64000, 256000, 1024000, 320000, 1280000, 5120000, 20480000), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> BATTERY_BLOCK_ENERGY_TIER = add(CATEGORY_ENERGY_STORAGE, "battery_block_energy_tier", List.of(1, 2, 3, 4, 3, 4, 5, 6), 1, 10, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> BATTERY_ITEM_CAPACITY = add(CATEGORY_ENERGY_STORAGE, "battery_item_capacity", List.of(8000000), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> BATTERY_ITEM_MAX_TRANSFER = add(CATEGORY_ENERGY_STORAGE, "battery_item_max_transfer", List.of(80000), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> BATTERY_ITEM_ENERGY_TIER = add(CATEGORY_ENERGY_STORAGE, "battery_item_energy_tier", List.of(3), 1, 10, ARRAY);

    private static final ModConfigSpec.IntValue MACHINE_MIN_SIZE = add(CATEGORY_MACHINE, "machine_min_size", 1, 1, 255);
    private static final ModConfigSpec.IntValue MACHINE_MAX_SIZE = add(CATEGORY_MACHINE, "machine_max_size", 24, 3, 255);

    private static final ModConfigSpec.IntValue MACHINE_ELECTROLYZER_TIME = add(CATEGORY_MACHINE, "machine_electrolyzer_time", 20, 1, 128000);
    private static final ModConfigSpec.IntValue MACHINE_ELECTROLYZER_POWER = add(CATEGORY_MACHINE, "machine_electrolyzer_power", 320, 1, 128000);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> MACHINE_CATHODE_EFFICIENCY = addString(CATEGORY_MACHINE, "machine_cathode_efficiency", List.of("Iron@0.6", "Nickel@0.7", "Molybdenum@0.8", "Cobalt@0.9", "Platinum@1.0", "Palladium@1.0"), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> MACHINE_ANODE_EFFICIENCY = addString(CATEGORY_MACHINE, "machine_anode_efficiency", List.of("CopperOxide@0.6", "TinOxide@0.6", "NickelOxide@0.7", "CobaltOxide@0.8", "RutheniumOxide@0.9", "IridiumOxide@1.0"), LIST);
    private static final ModConfigSpec.DoubleValue MACHINE_ELECTROLYZER_SOUND_VOLUME = add(CATEGORY_MACHINE, "machine_electrolyzer_sound_volume", 1D, 0D, 15D);

    private static final ModConfigSpec.IntValue MACHINE_DISTILLER_TIME = add(CATEGORY_MACHINE, "machine_distiller_time", 800, 1, 128000);
    private static final ModConfigSpec.IntValue MACHINE_DISTILLER_POWER = add(CATEGORY_MACHINE, "machine_distiller_power", 10, 1, 128000);
    private static final ModConfigSpec.DoubleValue MACHINE_DISTILLER_SOUND_VOLUME = add(CATEGORY_MACHINE, "machine_distiller_sound_volume", 1D, 0D, 15D);

    private static final ModConfigSpec.IntValue MACHINE_INFILTRATOR_TIME = add(CATEGORY_MACHINE, "machine_infiltrator_time", 1600, 1, 128000);
    private static final ModConfigSpec.IntValue MACHINE_INFILTRATOR_POWER = add(CATEGORY_MACHINE, "machine_infiltrator_power", 320, 1, 128000);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> MACHINE_INFILTRATOR_PRESSURE_FLUID_EFFICIENCY = addString(CATEGORY_MACHINE, "machine_infiltrator_pressure_fluid_efficiency", List.of("nitrogen@0.8", "argon@0.9", "neon@0.9", "helium@1.0"), LIST);
    private static final ModConfigSpec.DoubleValue MACHINE_INFILTRATOR_SOUND_VOLUME = add(CATEGORY_MACHINE, "machine_infiltrator_sound_volume", 1D, 0D, 15D);

    private static final ModConfigSpec.ConfigValue<List<? extends Double>> MACHINE_DIAPHRAGM_EFFICIENCY = add(CATEGORY_FISSION, "machine_diaphragm_efficiency", List.of(0.8D, 0.9D, 1D), 0D, 255D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> MACHINE_DIAPHRAGM_CONTACT_FACTOR = add(CATEGORY_FISSION, "machine_diaphragm_contact_factor", List.of(1D, 1.5D, 2D), 0D, 255D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> MACHINE_SIEVE_ASSEMBLY_EFFICIENCY = add(CATEGORY_FISSION, "machine_sieve_assembly_efficiency", List.of(0.8D, 0.9D, 1D), 0D, 255D, ARRAY);

    private static final ModConfigSpec.IntValue FISSION_MIN_SIZE = add(CATEGORY_FISSION, "fission_min_size", 1, 1, 255);
    private static final ModConfigSpec.IntValue FISSION_MAX_SIZE = add(CATEGORY_FISSION, "fission_max_size", 24, 3, 255);
    private static final ModConfigSpec.DoubleValue FISSION_FUEL_TIME_MULTIPLIER = add(CATEGORY_FISSION, "fission_fuel_time_multiplier", 1D, 0.001D, 255D);
    private static final ModConfigSpec.DoubleValue FISSION_FUEL_HEAT_MULTIPLIER = add(CATEGORY_FISSION, "fission_fuel_heat_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.DoubleValue FISSION_FUEL_EFFICIENCY_MULTIPLIER = add(CATEGORY_FISSION, "fission_fuel_efficiency_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.DoubleValue FISSION_FUEL_RADIATION_MULTIPLIER = add(CATEGORY_FISSION, "fission_fuel_radiation_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_SOURCE_EFFICIENCY = add(CATEGORY_FISSION, "fission_source_efficiency", List.of(0.9D, 0.95D, 1D), 0D, 255D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_SINK_COOLING_RATE = add(CATEGORY_FISSION, "fission_sink_cooling_rate", List.of(55, 50, 85, 80, 70, 105, 90, 100, 110, 115, 145, 65, 95, 200, 195, 75, 120, 60, 160, 130, 125, 150, 175, 170, 165, 180, 140, 135, 185, 190, 155, 205), 0, 32767, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> FISSION_SINK_RULE = addString(CATEGORY_FISSION, "fission_sink_rule", List.of("one cell", "one moderator", "one cell && one moderator", "one redstone sink", "two axial glowstone sinks", "one obsidian sink", "two moderators", "one cell && one casing", "exactly two iron sinks", "two water sinks", "exactly one water sink && two lead sinks", "one reflector", "one reflector && one iron sink", "one cell && one gold sink", "one moderator && one prismarine sink", "one water sink", "two axial lapis sinks", "one iron sink", "exactly one quartz sink && one casing", "exactly two axial lead sinks && one casing", "exactly one moderator && one casing", "two cells", "one quartz sink && one lapis sink", "two glowstone sinks && one tin sink", "one gold sink && one prismarine sink", "one redstone sink && one end_stone sink", "one end_stone sink && one copper sink", "two axial reflectors", "two copper sinks && one purpur sink", "exactly two redstone sinks", "three moderators", "three cells"), ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_HEATER_COOLING_RATE = add(CATEGORY_FISSION, "fission_heater_cooling_rate", List.of(55, 50, 85, 80, 70, 105, 90, 100, 110, 115, 145, 65, 95, 200, 195, 75, 120, 60, 160, 130, 125, 150, 175, 170, 165, 180, 140, 135, 185, 190, 155, 205), 0, 32767, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> FISSION_HEATER_RULE = addString(CATEGORY_FISSION, "fission_heater_rule", List.of("one vessel", "one moderator", "one vessel && one moderator", "one redstone heater", "two axial glowstone heaters", "one obsidian heater", "two moderators", "one vessel && one casing", "exactly two iron heaters", "two standard heaters", "exactly one standard heater && two lead heaters", "one reflector", "one reflector && one iron heater", "one vessel && one gold heater", "one moderator && one prismarine heater", "one standard heater", "two axial lapis heaters", "one iron heater", "exactly one quartz heater && one casing", "exactly two axial lead heaters && one casing", "exactly one moderator && one casing", "two vessels", "one quartz heater && one lapis heater", "two glowstone heaters && one tin heater", "one gold heater && one prismarine heater", "one redstone heater && one end_stone heater", "one end_stone heater && one copper heater", "two axial reflectors", "two copper heaters && one purpur heater", "exactly two redstone heaters", "three moderators", "three vessels"), ARRAY);
    //    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_MODERATOR_FLUX_FACTOR = add(CATEGORY_FISSION, "fission_moderator_flux_factor", List.of(10, 22, 36), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_MODERATOR_EFFICIENCY = add(CATEGORY_FISSION, "fission_moderator_efficiency", List.of(1.1D, 1.05D, 1D), 0D, 255D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_REFLECTOR_EFFICIENCY = add(CATEGORY_FISSION, "fission_reflector_efficiency", List.of(0.5D, 0.25D), 0D, 255D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_REFLECTOR_REFLECTIVITY = add(CATEGORY_FISSION, "fission_reflector_reflectivity", List.of(1D, 0.5D), 0D, 1D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_SHIELD_HEAT_PER_FLUX = add(CATEGORY_FISSION, "fission_shield_heat_per_flux", List.of(5D), 0D, 32767D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_SHIELD_EFFICIENCY = add(CATEGORY_FISSION, "fission_shield_efficiency", List.of(0.5D), 0D, 255D, ARRAY);
    //    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_IRRADIATOR_HEAT_PER_FLUX = add(CATEGORY_FISSION, "fission_irradiator_heat_per_flux", List.of(0D, 0D, 0D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_IRRADIATOR_EFFICIENCY = add(CATEGORY_FISSION, "fission_irradiator_efficiency", List.of(0D, 0D, 0.5D), 0D, 32767D, ARRAY);
    private static final ModConfigSpec.IntValue FISSION_COOLING_EFFICIENCY_LENIENCY = add(CATEGORY_FISSION, "fission_cooling_efficiency_leniency", 10, 0, 32767);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_SPARSITY_PENALTY_PARAMS = add(CATEGORY_FISSION, "fission_sparsity_penalty_params", List.of(0.5D, 0.75D), 0D, 1D, ARRAY);
    private static final ModConfigSpec.DoubleValue FISSION_HEATING_COOLANT_HEAT_MULT = add(CATEGORY_FISSION, "fission_heating_coolant_heat_mult", 2D, 0.001D, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue FISSION_DECAY_MECHANICS = add(CATEGORY_FISSION, "fission_decay_mechanics", false);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_DECAY_BUILD_UP_TIMES = add(CATEGORY_FISSION, "fission_decay_build_up_times", List.of(24000D, 24000D, 24000D), 0D, Integer.MAX_VALUE, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_DECAY_LIFETIMES = add(CATEGORY_FISSION, "fission_decay_lifetimes", List.of(6000D, 8000D, 12000D), 0D, Integer.MAX_VALUE, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_DECAY_EQUILIBRIUM_FACTORS = add(CATEGORY_FISSION, "fission_decay_equilibrium_factors", List.of(1D, 5D, 1D), 0D, 255D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_DECAY_DAUGHTER_MULTIPLIERS = add(CATEGORY_FISSION, "fission_decay_daughter_multipliers", List.of(5D, 5D), 0D, 255D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_DECAY_TERM_MULTIPLIERS = add(CATEGORY_FISSION, "fission_decay_term_multipliers", List.of(0.95D, 0.05D), 0D, 1D, ARRAY);
    private static final ModConfigSpec.BooleanValue FISSION_OVERHEAT = add(CATEGORY_FISSION, "fission_overheat", true);
    private static final ModConfigSpec.DoubleValue FISSION_MELTDOWN_RADIATION_MULTIPLIER = add(CATEGORY_FISSION, "fission_meltdown_radiation_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.BooleanValue FISSION_HEAT_DAMAGE = add(CATEGORY_FISSION, "fission_heat_damage", false);
    private static final ModConfigSpec.IntValue FISSION_NEUTRON_REACH = add(CATEGORY_FISSION, "fission_neutron_reach", 4, 1, 255);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_HEAT_DISSIPATION = add(CATEGORY_FISSION, "fission_heat_dissipation", List.of(true, false), ARRAY);
    private static final ModConfigSpec.DoubleValue FISSION_HEAT_DISSIPATION_RATE = add(CATEGORY_FISSION, "fission_heat_dissipation_rate", 0D, 1D / 292032000D, 1D);
    private static final ModConfigSpec.DoubleValue FISSION_EMERGENCY_COOLING_MULTIPLIER = add(CATEGORY_FISSION, "fission_emergency_cooling_multiplier", 1D, 0.001D, 255D);
    private static final ModConfigSpec.DoubleValue FISSION_SOUND_VOLUME = add(CATEGORY_FISSION, "fission_sound_volume", 1D, 0D, 15D);

//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_THORIUM_FUEL_TIME = add(CATEGORY_FISSION, "fission_thorium_fuel_time", List.of(14400, 14400, 18000, 11520, 18000), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_THORIUM_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_thorium_heat_generation", List.of(40, 40, 32, 50, 32), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_THORIUM_EFFICIENCY = add(CATEGORY_FISSION, "fission_thorium_efficiency", List.of(1.25D, 1.25D, 1.25D, 1.25D, 1.25D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_THORIUM_CRITICALITY = add(CATEGORY_FISSION, "fission_thorium_criticality", List.of(199, 234, 293, 199, 234), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_THORIUM_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_thorium_decay_factor", Arrays.stream(arrayCopies(5, 0.04D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_THORIUM_SELF_PRIMING = add(CATEGORY_FISSION, "fission_thorium_self_priming", List.of(false, false, false, false, false), ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_THORIUM_RADIATION = add(CATEGORY_FISSION, "fission_thorium_radiation", List.of(RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION), 0D, 1000D, ARRAY);
//
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_URANIUM_FUEL_TIME = add(CATEGORY_FISSION, "fission_uranium_fuel_time", List.of(2666, 2666, 3348, 2134, 3348, 2666, 2666, 3348, 2134, 3348, 4800, 4800, 6000, 3840, 6000, 4800, 4800, 6000, 3840, 6000), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_URANIUM_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_uranium_heat_generation", List.of(216, 216, 172, 270, 172, 216 * 3, 216 * 3, 172 * 3, 270 * 3, 172 * 3, 120, 120, 96, 150, 96, 120 * 3, 120 * 3, 96 * 3, 150 * 3, 96 * 3), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_URANIUM_EFFICIENCY = add(CATEGORY_FISSION, "fission_uranium_efficiency", List.of(1.1D, 1.1D, 1.1D, 1.1D, 1.1D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D, 1D, 1D, 1D, 1D, 1D, 1.05D, 1.05D, 1.05D, 1.05D, 1.05D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_URANIUM_CRITICALITY = add(CATEGORY_FISSION, "fission_uranium_criticality", List.of(66, 78, 98, 66, 78, 66 / 2, 78 / 2, 98 / 2, 66 / 2, 78 / 2, 87, 102, 128, 87, 102, 87 / 2, 102 / 2, 128 / 2, 87 / 2, 102 / 2), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_URANIUM_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_uranium_decay_factor", Arrays.stream(arrayCopies(20, 0.065D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_URANIUM_SELF_PRIMING = add(CATEGORY_FISSION, "fission_uranium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false), ARRAY);
////    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_URANIUM_RADIATION = add(CATEGORY_FISSION, "fission_uranium_radiation", List.of(RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION), 0D, 1000D, ARRAY);
//
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_NEPTUNIUM_FUEL_TIME = add(CATEGORY_FISSION, "fission_neptunium_fuel_time", List.of(1972, 1972, 2462, 1574, 2462, 1972, 1972, 2462, 1574, 2462), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_NEPTUNIUM_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_neptunium_heat_generation", List.of(292, 292, 234, 366, 234, 292 * 3, 292 * 3, 234 * 3, 366 * 3, 234 * 3), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_NEPTUNIUM_EFFICIENCY = add(CATEGORY_FISSION, "fission_neptunium_efficiency", List.of(1.1D, 1.1D, 1.1D, 1.1D, 1.1D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_NEPTUNIUM_CRITICALITY = add(CATEGORY_FISSION, "fission_neptunium_criticality", List.of(60, 70, 88, 60, 70, 60 / 2, 70 / 2, 88 / 2, 60 / 2, 70 / 2), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_NEPTUNIUM_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_neptunium_decay_factor", Arrays.stream(arrayCopies(10, 0.07D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_NEPTUNIUM_SELF_PRIMING = add(CATEGORY_FISSION, "fission_neptunium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false), ARRAY);
////    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_NEPTUNIUM_RADIATION = add(CATEGORY_FISSION, "fission_neptunium_radiation", List.of(RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION), 0D, 1000D, ARRAY);
//
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_PLUTONIUM_FUEL_TIME = add(CATEGORY_FISSION, "fission_plutonium_fuel_time", List.of(4572, 4572, 5760, 3646, 5760, 4572, 4572, 5760, 3646, 5760, 3164, 3164, 3946, 2526, 3946, 3164, 3164, 3946, 2526, 3946), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_PLUTONIUM_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_plutonium_heat_generation", List.of(126, 126, 100, 158, 100, 126 * 3, 126 * 3, 100 * 3, 158 * 3, 100 * 3, 182, 182, 146, 228, 146, 182 * 3, 182 * 3, 146 * 3, 228 * 3, 146 * 3), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_PLUTONIUM_EFFICIENCY = add(CATEGORY_FISSION, "fission_plutonium_efficiency", List.of(1.2D, 1.2D, 1.2D, 1.2D, 1.2D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.3D, 1.3D, 1.3D, 1.3D, 1.3D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_PLUTONIUM_CRITICALITY = add(CATEGORY_FISSION, "fission_plutonium_criticality", List.of(84, 99, 124, 84, 99, 84 / 2, 99 / 2, 124 / 2, 84 / 2, 99 / 2, 71, 84, 105, 71, 84, 71 / 2, 84 / 2, 105 / 2, 71 / 2, 84 / 2), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_PLUTONIUM_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_plutonium_decay_factor", Arrays.stream(arrayCopies(20, 0.075D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_PLUTONIUM_SELF_PRIMING = add(CATEGORY_FISSION, "fission_plutonium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false), ARRAY);
////    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_PLUTONIUM_RADIATION = add(CATEGORY_FISSION, "fission_plutonium_radiation", List.of(RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION), 0D, 1000D, ARRAY);
//
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_MIXED_FUEL_TIME = add(CATEGORY_FISSION, "fission_mixed_fuel_time", List.of(4354, 4354, 5486, 3472, 5486, 3014, 3014, 3758, 2406, 3758), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_MIXED_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_mixed_heat_generation", List.of(132, 132, 106, 166, 106, 192, 192, 154, 240, 154), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_MIXED_EFFICIENCY = add(CATEGORY_FISSION, "fission_mixed_efficiency", List.of(1.05D, 1.05D, 1.05D, 1.05D, 1.05D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_MIXED_CRITICALITY = add(CATEGORY_FISSION, "fission_mixed_criticality", List.of(80, 94, 118, 80, 94, 68, 80, 100, 68, 80), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_MIXED_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_mixed_decay_factor", Arrays.stream(arrayCopies(10, 0.075D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_MIXED_SELF_PRIMING = add(CATEGORY_FISSION, "fission_mixed_self_priming", List.of(false, false, false, false, false, false, false, false, false, false), ARRAY);
////    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_MIXED_RADIATION = add(CATEGORY_FISSION, "fission_mixed_radiation", List.of(RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION), 0D, 1000D, ARRAY);
//
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_AMERICIUM_FUEL_TIME = add(CATEGORY_FISSION, "fission_americium_fuel_time", List.of(1476, 1476, 1846, 1180, 1846, 1476, 1476, 1846, 1180, 1846), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_AMERICIUM_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_americium_heat_generation", List.of(390, 390, 312, 488, 312, 390 * 3, 390 * 3, 312 * 3, 488 * 3, 312 * 3), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_AMERICIUM_EFFICIENCY = add(CATEGORY_FISSION, "fission_americium_efficiency", List.of(1.35D, 1.35D, 1.35D, 1.35D, 1.35D, 1.4D, 1.4D, 1.4D, 1.4D, 1.4D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_AMERICIUM_CRITICALITY = add(CATEGORY_FISSION, "fission_americium_criticality", List.of(55, 65, 81, 55, 65, 55 / 2, 65 / 2, 81 / 2, 55 / 2, 65 / 2), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_AMERICIUM_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_americium_decay_factor", Arrays.stream(arrayCopies(10, 0.08D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_AMERICIUM_SELF_PRIMING = add(CATEGORY_FISSION, "fission_americium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false), ARRAY);
////    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_AMERICIUM_RADIATION = add(CATEGORY_FISSION, "fission_americium_radiation", List.of(RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION), 0D, 1000D, ARRAY);
//
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_CURIUM_FUEL_TIME = add(CATEGORY_FISSION, "fission_curium_fuel_time", List.of(1500, 1500, 1870, 1200, 1870, 1500, 1500, 1870, 1200, 1870, 2420, 2420, 3032, 1932, 3032, 2420, 2420, 3032, 1932, 3032, 2150, 2150, 2692, 1714, 2692, 2150, 2150, 2692, 1714, 2692), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_CURIUM_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_curium_heat_generation", List.of(384, 384, 308, 480, 308, 384 * 3, 384 * 3, 308 * 3, 480 * 3, 308 * 3, 238, 238, 190, 298, 190, 238 * 3, 238 * 3, 190 * 3, 298 * 3, 190 * 3, 268, 268, 214, 336, 214, 268 * 3, 268 * 3, 214 * 3, 336 * 3, 214 * 3), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_CURIUM_EFFICIENCY = add(CATEGORY_FISSION, "fission_curium_efficiency", List.of(1.45D, 1.45D, 1.45D, 1.45D, 1.45D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.6D, 1.6D, 1.6D, 1.6D, 1.6D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_CURIUM_CRITICALITY = add(CATEGORY_FISSION, "fission_curium_criticality", List.of(56, 66, 83, 56, 66, 56 / 2, 66 / 2, 83 / 2, 56 / 2, 66 / 2, 64, 75, 94, 64, 75, 64 / 2, 75 / 2, 94 / 2, 64 / 2, 75 / 2, 61, 72, 90, 61, 72, 61 / 2, 72 / 2, 90 / 2, 61 / 2, 72 / 2), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_CURIUM_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_curium_decay_factor", Arrays.stream(arrayCopies(30, 0.085D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_CURIUM_SELF_PRIMING = add(CATEGORY_FISSION, "fission_curium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false), ARRAY);
////    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_CURIUM_RADIATION = add(CATEGORY_FISSION, "fission_curium_radiation", List.of(RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION), 0D, 1000D, ARRAY);
//
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_BERKELIUM_FUEL_TIME = add(CATEGORY_FISSION, "fission_berkelium_fuel_time", List.of(2166, 2166, 2716, 1734, 2716, 2166, 2166, 2716, 1734, 2716), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_BERKELIUM_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_berkelium_heat_generation", List.of(266, 266, 212, 332, 212, 266 * 3, 266 * 3, 212 * 3, 332 * 3, 212 * 3), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_BERKELIUM_EFFICIENCY = add(CATEGORY_FISSION, "fission_berkelium_efficiency", List.of(1.65D, 1.65D, 1.65D, 1.65D, 1.65D, 1.7D, 1.7D, 1.7D, 1.7D, 1.7D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_BERKELIUM_CRITICALITY = add(CATEGORY_FISSION, "fission_berkelium_criticality", List.of(62, 73, 91, 62, 73, 62 / 2, 73 / 2, 91 / 2, 62 / 2, 73 / 2), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_BERKELIUM_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_berkelium_decay_factor", Arrays.stream(arrayCopies(10, 0.09D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_BERKELIUM_SELF_PRIMING = add(CATEGORY_FISSION, "fission_berkelium_self_priming", List.of(false, false, false, false, false, false, false, false, false, false), ARRAY);
////    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_BERKELIUM_RADIATION = add(CATEGORY_FISSION, "fission_berkelium_radiation", List.of(RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION), 0D, 1000D, ARRAY);
//
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_CALIFORNIUM_FUEL_TIME = add(CATEGORY_FISSION, "fission_californium_fuel_time", List.of(1066, 1066, 1334, 852, 1334, 1066, 1066, 1334, 852, 1334, 2000, 2000, 2504, 1600, 2504, 2000, 2000, 2504, 1600, 2504), 1, Integer.MAX_VALUE, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_CALIFORNIUM_HEAT_GENERATION = add(CATEGORY_FISSION, "fission_californium_heat_generation", List.of(540, 540, 432, 676, 432, 540 * 3, 540 * 3, 432 * 3, 676 * 3, 432 * 3, 288, 288, 230, 360, 230, 288 * 3, 288 * 3, 230 * 3, 360 * 3, 230 * 3), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_CALIFORNIUM_EFFICIENCY = add(CATEGORY_FISSION, "fission_californium_efficiency", List.of(1.75D, 1.75D, 1.75D, 1.75D, 1.75D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.85D, 1.85D, 1.85D, 1.85D, 1.85D), 0D, 32767D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FISSION_CALIFORNIUM_CRITICALITY = add(CATEGORY_FISSION, "fission_californium_criticality", List.of(51, 60, 75, 51, 60, 51 / 2, 60 / 2, 75 / 2, 51 / 2, 60 / 2, 60, 71, 89, 60, 71, 60 / 2, 71 / 2, 89 / 2, 60 / 2, 71 / 2), 0, 32767, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_CALIFORNIUM_DECAY_FACTOR = add(CATEGORY_FISSION, "fission_californium_decay_factor", Arrays.stream(arrayCopies(20, 0.1D)).boxed().toList(), 0D, 1D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> FISSION_CALIFORNIUM_SELF_PRIMING = add(CATEGORY_FISSION, "fission_californium_self_priming", List.of(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true), ARRAY);
    /// /    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FISSION_CALIFORNIUM_RADIATION = add(CATEGORY_FISSION, "fission_californium_radiation", List.of(RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION), 0D, 1000D, ARRAY);

    private static final ModConfigSpec.DoubleValue FUSION_FUEL_TIME_MULTIPLIER = add(CATEGORY_FUSION, "fusion_fuel_time_multiplier", 1D, 0.001D, 255D);
    private static final ModConfigSpec.DoubleValue FUSION_FUEL_HEAT_MULTIPLIER = add(CATEGORY_FUSION, "fusion_fuel_heat_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.DoubleValue FUSION_FUEL_EFFICIENCY_MULTIPLIER = add(CATEGORY_FUSION, "fusion_fuel_efficiency_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.DoubleValue FUSION_FUEL_RADIATION_MULTIPLIER = add(CATEGORY_FUSION, "fusion_fuel_radiation_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.BooleanValue FUSION_OVERHEAT = add(CATEGORY_FUSION, "fusion_overheat", true);
    private static final ModConfigSpec.DoubleValue FUSION_MELTDOWN_RADIATION_MULTIPLIER = add(CATEGORY_FUSION, "fusion_meltdown_radiation_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.IntValue FUSION_MIN_SIZE = add(CATEGORY_FUSION, "fusion_min_size", 1, 1, 255);
    private static final ModConfigSpec.IntValue FUSION_MAX_SIZE = add(CATEGORY_FUSION, "fusion_max_size", 24, 1, 255);
    private static final ModConfigSpec.IntValue FUSION_COMPARATOR_MAX_EFFICIENCY = add(CATEGORY_FUSION, "fusion_comparator_max_efficiency", 90, 1, 100);
    private static final ModConfigSpec.DoubleValue FUSION_ELECTROMAGNET_POWER = add(CATEGORY_FUSION, "fusion_electromagnet_power", 250D, 0D, Integer.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue FUSION_PLASMA_CRAZINESS = add(CATEGORY_FUSION, "fusion_plasma_craziness", true);
    private static final ModConfigSpec.DoubleValue FUSION_SOUND_VOLUME = add(CATEGORY_FUSION, "fusion_sound_volume", 1D, 0D, 15D);

    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FUSION_FUEL_TIME = add(CATEGORY_FUSION, "fusion_fuel_time", List.of(100D, 150D, 200D, 200D, 350D, 400D, 600D, 200D, 250D, 250D, 400D, 450D, 650D, 300D, 300D, 450D, 500D, 700D, 300D, 450D, 500D, 700D, 600D, 650D, 850D, 700D, 900D, 1100D), 1D, 32767D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FUSION_FUEL_HEAT_GENERATION = add(CATEGORY_FUSION, "fusion_fuel_heat_generation", List.of(44200D, 112300D, 30D, 303600D, 35100D, 133000D, 44400D, 50700D, 172600D, 225200D, 171600D, 85900D, 26100D, 90100D, 109900D, 91500D, 43500D, 700D, 131500D, 115100D, 72700D, 14000D, 106800D, 55200D, 15700D, 22900D, 45D, 5D), 0D, Integer.MAX_VALUE, ARRAY);
    // TODO: multiply by R
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FUSION_FUEL_OPTIMAL_TEMPERATURE = add(CATEGORY_FUSION, "fusion_fuel_optimal_temperature", List.of(3635D, 1022D, 4964D, 2740D, 5972D, 4161D, 13432D, 949D, 670D, 2160D, 3954D, 4131D, 13853D, 736D, 2137D, 4079D, 4522D, 27254D, 5420D, 7800D, 7937D, 24266D, 11268D, 11927D, 30399D, 13630D, 166414D, 293984D), 500D, 20000D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FUSION_RADIATION = add(CATEGORY_FUSION, "fusion_radiation", List.of(RadSources.FUSION / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.TRITIUM + RadSources.NEUTRON) / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.TRITIUM) / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.TRITIUM / 2D + RadSources.NEUTRON / 2D) / 64D, (RadSources.FUSION + RadSources.TRITIUM + RadSources.NEUTRON) / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.TRITIUM + 2 * RadSources.NEUTRON) / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D), 0D, 1000D, ARRAY);

    private static final ModConfigSpec.IntValue HEAT_EXCHANGER_MIN_SIZE = add(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_min_size", 1, 1, 255);
    private static final ModConfigSpec.IntValue HEAT_EXCHANGER_MAX_SIZE = add(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_max_size", 24, 2, 255);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> HEAT_EXCHANGER_HEAT_TRANSFER_COEFFICIENT = add(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_heat_transfer_coefficient", List.of(16D, 24D, 32D), 0.001D, Integer.MAX_VALUE, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> HEAT_EXCHANGER_HEAT_RETENTION_MULT = add(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_heat_retention_mult", List.of(0.9D, 0.95D, 1D), 0.01D, 1D, ARRAY);
    private static final ModConfigSpec.DoubleValue HEAT_EXCHANGER_COOLANT_HEAT_MULT = add(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_coolant_heat_mult", 4D, 0.001D, Integer.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue HEAT_EXCHANGER_LMTD = add(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_lmtd", false);
    private static final ModConfigSpec.BooleanValue HEAT_EXCHANGER_ALTERNATE_HPS_RECIPE = add(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_alternate_hps_recipe", false);
    private static final ModConfigSpec.BooleanValue HEAT_EXCHANGER_ALTERNATE_EXHAUST_RECIPE = add(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_alternate_exhaust_recipe", false);

    private static final ModConfigSpec.IntValue TURBINE_MIN_SIZE = add(CATEGORY_TURBINE, "turbine_min_size", 1, 1, 255);
    private static final ModConfigSpec.IntValue TURBINE_MAX_SIZE = add(CATEGORY_TURBINE, "turbine_max_size", 24, 3, 255);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TURBINE_BLADE_EFFICIENCY = add(CATEGORY_TURBINE, "turbine_blade_efficiency", List.of(1D, 1.1D, 1.2D), 0.01D, 15D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TURBINE_BLADE_EXPANSION = add(CATEGORY_TURBINE, "turbine_blade_expansion", List.of(1.4D, 1.6D, 1.8D), 1D, 15D, ARRAY);
    private static final ModConfigSpec.DoubleValue TURBINE_STATOR_EXPANSION = add(CATEGORY_TURBINE, "turbine_stator_expansion", 0.75D, 0.01D, 1D);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TURBINE_COIL_CONDUCTIVITY = add(CATEGORY_TURBINE, "turbine_coil_conductivity", List.of(0.88D, 0.9D, 1D, 1.04D, 1.06D, 1.12D), 0.01D, 15D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> TURBINE_COIL_RULE = addString(CATEGORY_TURBINE, "turbine_coil_rule", List.of("one bearing || one connector", "one magnesium coil", "two magnesium coils", "one aluminum coil", "one beryllium coil", "one gold coil && one copper coil"), ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> TURBINE_CONNECTOR_RULE = addString(CATEGORY_TURBINE, "turbine_connector_rule", List.of("one of any coil"), ARRAY);
    //    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TURBINE_POWER_PER_MB = add(CATEGORY_TURBINE, "turbine_power_per_mb", List.of(16D, 4D, 4D), 0D, 255D, ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TURBINE_EXPANSION_LEVEL = add(CATEGORY_TURBINE, "turbine_expansion_level", List.of(4D, 2D, 2D), 1D, 255D, ARRAY);
    private static final ModConfigSpec.DoubleValue TURBINE_SPIN_UP_MULTIPLIER_GLOBAL = add(CATEGORY_TURBINE, "turbine_spin_up_multiplier_global", 1D, 0D, 255D);
    //    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TURBINE_SPIN_UP_MULTIPLIER = add(CATEGORY_TURBINE, "turbine_spin_up_multiplier", List.of(1D, 1D, 1D), 0D, 255D, ARRAY);
    private static final ModConfigSpec.DoubleValue TURBINE_SPIN_DOWN_MULTIPLIER = add(CATEGORY_TURBINE, "turbine_spin_down_multiplier", 1D, 0.01D, 255D);
    private static final ModConfigSpec.IntValue TURBINE_MB_PER_BLADE = add(CATEGORY_TURBINE, "turbine_mb_per_blade", 100, 1, 32767);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TURBINE_THROUGHPUT_LENIENCY_PARAMS = add(CATEGORY_TURBINE, "turbine_throughput_leniency_params", List.of(0.5D, 0.75D), 0D, 1D, ARRAY);
    private static final ModConfigSpec.DoubleValue TURBINE_TENSION_THROUGHPUT_FACTOR = add(CATEGORY_TURBINE, "turbine_tension_throughput_factor", 2D, 1D, 255D);
    private static final ModConfigSpec.DoubleValue TURBINE_TENSION_LENIENCY = add(CATEGORY_TURBINE, "turbine_tension_leniency", 0.05D, 0D, 1D);
    private static final ModConfigSpec.DoubleValue TURBINE_POWER_BONUS_MULTIPLIER = add(CATEGORY_TURBINE, "turbine_power_bonus_multiplier", 1D, 0D, 255D);
    private static final ModConfigSpec.DoubleValue TURBINE_SOUND_VOLUME = add(CATEGORY_TURBINE, "turbine_sound_volume", 1D, 0D, 15D);
    private static final ModConfigSpec.DoubleValue TURBINE_PARTICLES = add(CATEGORY_TURBINE, "turbine_particles", 0.025D, 0D, 1D);
    private static final ModConfigSpec.DoubleValue TURBINE_RENDER_BLADE_WIDTH = add(CATEGORY_TURBINE, "turbine_render_blade_width", NCMath.SQRT2, 0.01D, 4D);
    private static final ModConfigSpec.DoubleValue TURBINE_RENDER_ROTOR_EXPANSION = add(CATEGORY_TURBINE, "turbine_render_rotor_expansion", 4D, 1D, 15D);
    private static final ModConfigSpec.DoubleValue TURBINE_RENDER_ROTOR_SPEED = add(CATEGORY_TURBINE, "turbine_render_rotor_speed", 1D, 0D, 15D);

    private static final ModConfigSpec.BooleanValue QUANTUM_DEDICATED_SERVER = add(CATEGORY_QUANTUM, "quantum_dedicated_server", false);
    private static final ModConfigSpec.IntValue QUANTUM_MAX_QUBITS = add(CATEGORY_QUANTUM, "quantum_max_qubits", 16, 1, 24);
    private static final ModConfigSpec.IntValue QUANTUM_ANGLE_PRECISION = add(CATEGORY_QUANTUM, "quantum_angle_precision", 16, 4, 1024);

    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> TOOL_MINING_LEVEL = add(CATEGORY_TOOL, "tool_mining_level", List.of(2, 2, 3, 3, 3, 3, 4, 4), 0, 15, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> TOOL_DURABILITY = add(CATEGORY_TOOL, "tool_durability", List.of(547, 547 * 5, 929, 929 * 5, 1245, 1245 * 5, 1928, 1928 * 5), 1, 32767, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TOOL_SPEED = add(CATEGORY_TOOL, "tool_speed", List.of(8D, 8D, 10D, 10D, 11D, 11D, 12D, 12D), 1D, 255D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TOOL_ATTACK_DAMAGE = add(CATEGORY_TOOL, "tool_attack_damage", List.of(2.5D, 2.5D, 3D, 3D, 3D, 3D, 3.5D, 3.5D), 0D, 255D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> TOOL_ENCHANTABILITY = add(CATEGORY_TOOL, "tool_enchantability", List.of(6, 6, 15, 15, 12, 12, 20, 20), 1, 255, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> TOOL_HANDLE_MODIFIER = add(CATEGORY_TOOL, "tool_handle_modifier", List.of(0.85D, 1.1D, 1D, 0.75D), 0.01D, 10D, ARRAY);

    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ARMOR_DURABILITY = add(CATEGORY_ARMOR, "armor_durability", List.of(22, 30, 34, 42, 0), 1, 127, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> ARMOR_TOUGHNESS = add(CATEGORY_ARMOR, "armor_toughness", List.of(1D, 2D, 1D, 2D, 0D), 0D, 8D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ARMOR_ENCHANTABILITY = add(CATEGORY_ARMOR, "armor_enchantability", List.of(6, 15, 12, 20, 5), 1, 255, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ARMOR_BORON = add(CATEGORY_ARMOR, "armor_boron", List.of(2, 5, 7, 3), 1, 25, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ARMOR_TOUGH = add(CATEGORY_ARMOR, "armor_tough", List.of(3, 6, 7, 3), 1, 25, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ARMOR_HARD_CARBON = add(CATEGORY_ARMOR, "armor_hard_carbon", List.of(3, 5, 7, 3), 1, 25, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ARMOR_BORON_NITRIDE = add(CATEGORY_ARMOR, "armor_boron_nitride", List.of(3, 6, 8, 3), 1, 25, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> ARMOR_HAZMAT = add(CATEGORY_ARMOR, "armor_hazmat", List.of(3, 6, 7, 3), 1, 25, ARRAY);

    private static final ModConfigSpec.IntValue ENTITY_TRACKING_RANGE = add(CATEGORY_ENTITY, "entity_tracking_range", 64, 1, 255);

    private static final ModConfigSpec.BooleanValue RADIATION_ENABLED = add(CATEGORY_RADIATION, "radiation_enabled", true);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_IMMUNE_PLAYERS = addString(CATEGORY_RADIATION, "radiation_immune_players", List.of(), LIST);
    private static final ModConfigSpec.IntValue RADIATION_WORLD_CHUNKS_PER_TICK = add(CATEGORY_RADIATION, "radiation_world_chunks_per_tick", 5, 1, 400);
    private static final ModConfigSpec.IntValue RADIATION_PLAYER_TICK_RATE = add(CATEGORY_RADIATION, "radiation_player_tick_rate", 5, 1, 400);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_WORLDS = addString(CATEGORY_RADIATION, "radiation_worlds", List.of("4598_2.25"), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_BIOMES = addString(CATEGORY_RADIATION, "radiation_biomes", List.of("nuclearcraft:nuclear_wasteland_0.25"), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_STRUCTURES = addString(CATEGORY_RADIATION, "radiation_structures", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_WORLD_LIMITS = addString(CATEGORY_RADIATION, "radiation_world_limits", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_BIOME_LIMITS = addString(CATEGORY_RADIATION, "radiation_biome_limits", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> RADIATION_FROM_BIOMES_DIMS_BLACKLIST = add(CATEGORY_RADIATION, "radiation_from_biomes_dims_blacklist", List.of(144), Integer.MIN_VALUE, Integer.MAX_VALUE, LIST);

    //    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_ORES = addString(CATEGORY_RADIATION, "radiation_ores", List.of("depletedFuelIC2U_" + (RadSources.URANIUM_238 * 4D + RadSources.PLUTONIUM_239 / 9D), "depletedFuelIC2MOX_" + RadSources.PLUTONIUM_239 * 28D / 9D), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_ITEMS = addString(CATEGORY_RADIATION, "radiation_items", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_BLOCKS = addString(CATEGORY_RADIATION, "radiation_blocks", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_FLUIDS = addString(CATEGORY_RADIATION, "radiation_fluids", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_FOODS = addString(CATEGORY_RADIATION, "radiation_foods", List.of("minecraft:golden_apple:0_-20_0.1", "minecraft:golden_apple:1_-100_0.5", "minecraft:golden_carrot:0_-4_0", "minecraft:spider_eye:0_0_0.5", "minecraft:poisonous_potato:0_0_0.5", "minecraft:fish:3_0_2", "minecraft:rabbit_stew:0_0_0.1", "minecraft:chorus_fruit:0_0_-0.25", "minecraft:beetroot:0_0_0.25", "minecraft:beetroot_soup:0_0_1.5", "quark:golden_frog_leg:0_-4_0"), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_ORES_BLACKLIST = addString(CATEGORY_RADIATION, "radiation_ores_blacklist", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_ITEMS_BLACKLIST = addString(CATEGORY_RADIATION, "radiation_items_blacklist", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_BLOCKS_BLACKLIST = addString(CATEGORY_RADIATION, "radiation_blocks_blacklist", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_FLUIDS_BLACKLIST = addString(CATEGORY_RADIATION, "radiation_fluids_blacklist", List.of(), LIST);

    private static final ModConfigSpec.DoubleValue MAX_PLAYER_RADS = add(CATEGORY_RADIATION, "max_player_rads", 1000D, 1D, 1000000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_PLAYER_DECAY_RATE = add(CATEGORY_RADIATION, "radiation_player_decay_rate", 0.0000005D, 0D, 1D);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> MAX_ENTITY_RADS = addString(CATEGORY_RADIATION, "max_entity_rads", List.of(), LIST);
    private static final ModConfigSpec.DoubleValue RADIATION_ENTITY_DECAY_RATE = add(CATEGORY_RADIATION, "radiation_entity_decay_rate", 0.001D, 0D, 1D);
    private static final ModConfigSpec.DoubleValue RADIATION_SPREAD_RATE = add(CATEGORY_RADIATION, "radiation_spread_rate", 0.1D, 0D, 1D);
    private static final ModConfigSpec.DoubleValue RADIATION_SPREAD_GRADIENT = add(CATEGORY_RADIATION, "radiation_spread_gradient", 0.5D, 1D, 1000000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_DECAY_RATE = add(CATEGORY_RADIATION, "radiation_decay_rate", 0.001D, 0D, 1D);
    private static final ModConfigSpec.DoubleValue RADIATION_LOWEST_RATE = add(CATEGORY_RADIATION, "radiation_lowest_rate", 0.000000000000001D, 0D, 1D);
    private static final ModConfigSpec.DoubleValue RADIATION_CHUNK_LIMIT = add(CATEGORY_RADIATION, "radiation_chunk_limit", -1D, -1D, Double.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<List<? extends Double>> RADIATION_SOUND_VOLUMES = add(CATEGORY_RADIATION, "radiation_sound_volumes", List.of(1D, 1D, 1D, 1D, 1D, 1D, 1D, 1D), 0D, 15D, ARRAY);
    private static final ModConfigSpec.BooleanValue RADIATION_CHECK_BLOCKS = add(CATEGORY_RADIATION, "radiation_check_blocks", true);
    private static final ModConfigSpec.IntValue RADIATION_BLOCK_EFFECT_MAX_RATE = add(CATEGORY_RADIATION, "radiation_block_effect_max_rate", 0, 0, 15);
    private static final ModConfigSpec.DoubleValue RADIATION_RAIN_MULT = add(CATEGORY_RADIATION, "radiation_rain_mult", 1D, 0.000001D, 1000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_SWIM_MULT = add(CATEGORY_RADIATION, "radiation_swim_mult", 2D, 0.000001D, 1000000D);

//    private static final ModConfigSpec.DoubleValue RADIATION_FERAL_GHOUL_ATTACK = add(CATEGORY_RADIATION, "radiation_feral_ghoul_attack", RadSources.CAESIUM_137, 0.000001D, 1000000D);

    private static final ModConfigSpec.DoubleValue RADIATION_RADAWAY_AMOUNT = add(CATEGORY_RADIATION, "radiation_radaway_amount", 300D, 0.001D, 1000000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_RADAWAY_SLOW_AMOUNT = add(CATEGORY_RADIATION, "radiation_radaway_slow_amount", 300D, 0.001D, 1000000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_RADAWAY_RATE = add(CATEGORY_RADIATION, "radiation_radaway_rate", 5D, 0.001D, 1000000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_RADAWAY_SLOW_RATE = add(CATEGORY_RADIATION, "radiation_radaway_slow_rate", 0.025D, 0.00001D, 10000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_POISON_TIME = add(CATEGORY_RADIATION, "radiation_poison_time", 60D, 1D, 1000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_RADAWAY_COOLDOWN = add(CATEGORY_RADIATION, "radiation_radaway_cooldown", 0D, 0D, 100000D);
    private static final ModConfigSpec.DoubleValue RADIATION_RAD_X_AMOUNT = add(CATEGORY_RADIATION, "radiation_rad_x_amount", 25D, 0.001D, 1000000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_RAD_X_LIFETIME = add(CATEGORY_RADIATION, "radiation_rad_x_lifetime", 12000D, 20D, 1000000000D);
    private static final ModConfigSpec.DoubleValue RADIATION_RAD_X_COOLDOWN = add(CATEGORY_RADIATION, "radiation_rad_x_cooldown", 0D, 0D, 100000D);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> RADIATION_SHIELDING_LEVEL = add(CATEGORY_RADIATION, "radiation_shielding_level", List.of(0.01D, 0.1D, 1D), 0.000000000000000001D, 1000D, ARRAY);
    private static final ModConfigSpec.BooleanValue RADIATION_TILE_SHIELDING = add(CATEGORY_RADIATION, "radiation_tile_shielding", true);
    private static final ModConfigSpec.DoubleValue RADIATION_SCRUBBER_FRACTION = add(CATEGORY_RADIATION, "radiation_scrubber_fraction", 0.125D, 0.001D, 1D);
    private static final ModConfigSpec.IntValue RADIATION_SCRUBBER_RADIUS = add(CATEGORY_RADIATION, "radiation_scrubber_radius", 4, 1, 10);
    private static final ModConfigSpec.BooleanValue RADIATION_SCRUBBER_NON_LINEAR = add(CATEGORY_RADIATION, "radiation_scrubber_non_linear", true);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> RADIATION_SCRUBBER_PARAM = add(CATEGORY_RADIATION, "radiation_scrubber_param", List.of(2.14280951676725D, 3D, 4D, 2D), 1D, 15D, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> RADIATION_SCRUBBER_TIME = add(CATEGORY_RADIATION, "radiation_scrubber_time", List.of(12000, 2400, 96000), 1, Integer.MAX_VALUE, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> RADIATION_SCRUBBER_POWER = add(CATEGORY_RADIATION, "radiation_scrubber_power", List.of(200, 40, 20), 0, Integer.MAX_VALUE, ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> RADIATION_SCRUBBER_EFFICIENCY = add(CATEGORY_RADIATION, "radiation_scrubber_efficiency", List.of(1D, 5D, 0.25D), 0D, 255D, ARRAY);
    private static final ModConfigSpec.DoubleValue RADIATION_GEIGER_BLOCK_REDSTONE = add(CATEGORY_RADIATION, "radiation_geiger_block_redstone", 3D, -127D, 127D);

    private static final ModConfigSpec.BooleanValue RADIATION_SHIELDING_DEFAULT_RECIPES = add(CATEGORY_RADIATION, "radiation_shielding_default_recipes", true);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_SHIELDING_ITEM_BLACKLIST = addString(CATEGORY_RADIATION, "radiation_shielding_item_blacklist", List.of("nuclearcraft:helm_hazmat", "nuclearcraft:chest_hazmat", "nuclearcraft:legs_hazmat", "nuclearcraft:boots_hazmat", "extraplanets:tier1_space_suit_helmet", "extraplanets:tier1_space_suit_chest", "extraplanets:tier1_space_suit_jetpack_chest", "extraplanets:tier1_space_suit_leggings", "extraplanets:tier1_space_suit_boots", "extraplanets:tier1_space_suit_gravity_boots", "extraplanets:tier2_space_suit_helmet", "extraplanets:tier2_space_suit_chest", "extraplanets:tier2_space_suit_jetpack_chest", "extraplanets:tier2_space_suit_leggings", "extraplanets:tier2_space_suit_boots", "extraplanets:tier2_space_suit_gravity_boots", "extraplanets:tier3_space_suit_helmet", "extraplanets:tier3_space_suit_chest", "extraplanets:tier3_space_suit_jetpack_chest", "extraplanets:tier3_space_suit_leggings", "extraplanets:tier3_space_suit_boots", "extraplanets:tier3_space_suit_gravity_boots", "extraplanets:tier4_space_suit_helmet", "extraplanets:tier4_space_suit_chest", "extraplanets:tier4_space_suit_jetpack_chest", "extraplanets:tier4_space_suit_leggings", "extraplanets:tier4_space_suit_boots", "extraplanets:tier4_space_suit_gravity_boots"), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_SHIELDING_CUSTOM_STACKS = addString(CATEGORY_RADIATION, "radiation_shielding_custom_stacks", List.of(), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_SHIELDING_DEFAULT_LEVELS = addString(CATEGORY_RADIATION, "radiation_shielding_default_levels", List.of("nuclearcraft:helm_hazmat_2.0", "nuclearcraft:chest_hazmat_3.0", "nuclearcraft:legs_hazmat_2.0", "nuclearcraft:boots_hazmat_2.0", "gravisuite:gravichestplate_3.0", "gravisuit:gravisuit_3.0", "gravisuit:nucleargravisuit_3.0", "extraplanets:tier1_space_suit_helmet_1.0", "extraplanets:tier1_space_suit_chest_1.5", "extraplanets:tier1_space_suit_jetpack_chest_1.5", "extraplanets:tier1_space_suit_leggings_1.0", "extraplanets:tier1_space_suit_boots_1.0", "extraplanets:tier1_space_suit_gravity_boots_1.0", "extraplanets:tier2_space_suit_helmet_1.3", "extraplanets:tier2_space_suit_chest_1.95", "extraplanets:tier2_space_suit_jetpack_chest_1.95", "extraplanets:tier2_space_suit_leggings_1.3", "extraplanets:tier2_space_suit_boots_1.3", "extraplanets:tier2_space_suit_gravity_boots_1.3", "extraplanets:tier3_space_suit_helmet_1.6", "extraplanets:tier3_space_suit_chest_2.4", "extraplanets:tier3_space_suit_jetpack_chest_2.4", "extraplanets:tier3_space_suit_leggings_1.6", "extraplanets:tier3_space_suit_boots_1.6", "extraplanets:tier3_space_suit_gravity_boots_1.6", "extraplanets:tier4_space_suit_helmet_2.0", "extraplanets:tier4_space_suit_chest_3.0", "extraplanets:tier4_space_suit_jetpack_chest_3.0", "extraplanets:tier4_space_suit_leggings_2.0", "extraplanets:tier4_space_suit_boots_2.0", "extraplanets:tier4_space_suit_gravity_boots_2.0"), LIST);

    private static final ModConfigSpec.BooleanValue RADIATION_TILE_ENTITIES = add(CATEGORY_RADIATION, "radiation_tile_entities", true);
    private static final ModConfigSpec.BooleanValue RADIATION_HARDCORE_STACKS = add(CATEGORY_RADIATION, "radiation_hardcore_stacks", true);
    private static final ModConfigSpec.DoubleValue RADIATION_HARDCORE_CONTAINERS = add(CATEGORY_RADIATION, "radiation_hardcore_containers", 0D, 0D, 1D);
    private static final ModConfigSpec.BooleanValue RADIATION_DROPPED_ITEMS = add(CATEGORY_RADIATION, "radiation_dropped_items", true);
    private static final ModConfigSpec.BooleanValue RADIATION_DEATH_PERSIST = add(CATEGORY_RADIATION, "radiation_death_persist", true);
    private static final ModConfigSpec.DoubleValue RADIATION_DEATH_PERSIST_FRACTION = add(CATEGORY_RADIATION, "radiation_death_persist_fraction", 0.75D, 0D, 1D);
    private static final ModConfigSpec.DoubleValue RADIATION_DEATH_IMMUNITY_TIME = add(CATEGORY_RADIATION, "radiation_death_immunity_time", 90D, 0D, 3600D);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_PLAYER_DEBUFF_LISTS = addString(CATEGORY_RADIATION, "radiation_player_debuff_lists", List.of("40.0_minecraft:weakness@1", "55.0_minecraft:weakness@1,minecraft:mining_fatigue@1", "70.0_minecraft:weakness@2,minecraft:mining_fatigue@1,minecraft:hunger@1", "80.0_minecraft:weakness@2,minecraft:mining_fatigue@2,minecraft:hunger@1,minecraft:poison@1", "90.0_minecraft:weakness@3,minecraft:mining_fatigue@3,minecraft:hunger@2,minecraft:poison@1,minecraft:wither@1"), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_PASSIVE_DEBUFF_LISTS = addString(CATEGORY_RADIATION, "radiation_passive_debuff_lists", List.of("40.0_minecraft:weakness@1", "55.0_minecraft:weakness@1,minecraft:mining_fatigue@1", "70.0_minecraft:weakness@2,minecraft:mining_fatigue@1,minecraft:hunger@1", "80.0_minecraft:weakness@2,minecraft:mining_fatigue@2,minecraft:hunger@1,minecraft:poison@1", "90.0_minecraft:weakness@3,minecraft:mining_fatigue@3,minecraft:hunger@2,minecraft:poison@1,minecraft:wither@1"), LIST);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RADIATION_MOB_BUFF_LISTS = addString(CATEGORY_RADIATION, "radiation_mob_buff_lists", List.of("40.0_minecraft:speed@1", "55.0_minecraft:speed@1,minecraft:strength@1", "70.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1", "80.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1,minecraft:absorption@1", "90.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1,minecraft:absorption@1,minecraft:regeneration@1"), LIST);
    private static final ModConfigSpec.BooleanValue RADIATION_PLAYER_RADS_FATAL = add(CATEGORY_RADIATION, "radiation_player_rads_fatal", true);
    private static final ModConfigSpec.BooleanValue RADIATION_PASSIVE_RADS_FATAL = add(CATEGORY_RADIATION, "radiation_passive_rads_fatal", true);
    private static final ModConfigSpec.BooleanValue RADIATION_MOB_RADS_FATAL = add(CATEGORY_RADIATION, "radiation_mob_rads_fatal", true);

    private static final ModConfigSpec.BooleanValue RADIATION_HORSE_ARMOR = add(CATEGORY_RADIATION, "radiation_horse_armor", false);

    private static final ModConfigSpec.DoubleValue RADIATION_HUD_SIZE = add(CATEGORY_RADIATION, "radiation_hud_size", 1D, 0.1D, 10D);
    private static final ModConfigSpec.DoubleValue RADIATION_HUD_POSITION = add(CATEGORY_RADIATION, "radiation_hud_position", 225D, 0D, 360D);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> RADIATION_HUD_POSITION_CARTESIAN = add(CATEGORY_RADIATION, "radiation_hud_position_cartesian", List.of(), 0D, 1D, LIST);
    private static final ModConfigSpec.BooleanValue RADIATION_HUD_TEXT_OUTLINE = add(CATEGORY_RADIATION, "radiation_hud_text_outline", false);
    private static final ModConfigSpec.BooleanValue RADIATION_REQUIRE_COUNTER = add(CATEGORY_RADIATION, "radiation_require_counter", true);
    private static final ModConfigSpec.BooleanValue RADIATION_CHUNK_BOUNDARIES = add(CATEGORY_RADIATION, "radiation_chunk_boundaries", true);
    private static final ModConfigSpec.IntValue RADIATION_UNIT_PREFIXES = add(CATEGORY_RADIATION, "radiation_unit_prefixes", 0, 0, 15);

    private static final ModConfigSpec.DoubleValue RADIATION_BADGE_DURABILITY = add(CATEGORY_RADIATION, "radiation_badge_durability", 250D, 0.000000000000000001D, 1000D);
    private static final ModConfigSpec.DoubleValue RADIATION_BADGE_INFO_RATE = add(CATEGORY_RADIATION, "radiation_badge_info_rate", 0.1D, 0.000000000000000001D, 1D);

//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> REGISTER_PROCESSOR = add(CATEGORY_REGISTRATION, "register_processor", List.of(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true), ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> REGISTER_PASSIVE = add(CATEGORY_REGISTRATION, "register_passive", List.of(true, true, true), ARRAY);
//    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> REGISTER_BATTERY = add(CATEGORY_REGISTRATION, "register_battery", List.of(true, true), ARRAY);
    private static final ModConfigSpec.BooleanValue REGISTER_QUANTUM = add(CATEGORY_REGISTRATION, "register_quantum", true);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> REGISTER_TOOL = add(CATEGORY_REGISTRATION, "register_tool", List.of(true, true, true, true), ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> REGISTER_TIC_TOOL = add(CATEGORY_REGISTRATION, "register_tic_tool", List.of(true, true, true, true, true, true, true, true), ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> REGISTER_ARMOR = add(CATEGORY_REGISTRATION, "register_armor", List.of(true, true, true, true), ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> REGISTER_CONARM_ARMOR = add(CATEGORY_REGISTRATION, "register_conarm_armor", List.of(true, true, true, true, true, true, true, true), ARRAY);
    private static final ModConfigSpec.ConfigValue<List<? extends Boolean>> REGISTER_ENTITY = add(CATEGORY_REGISTRATION, "register_entity", List.of(true), ARRAY);
    private static final ModConfigSpec.BooleanValue REGISTER_FLUID_BLOCKS = add(CATEGORY_REGISTRATION, "register_fluid_blocks", false);
    private static final ModConfigSpec.BooleanValue REGISTER_COFH_FLUIDS = add(CATEGORY_REGISTRATION, "register_cofh_fluids", false);
    private static final ModConfigSpec.BooleanValue REGISTER_TIC_RECIPES = add(CATEGORY_REGISTRATION, "register_tic_recipes", true);
    private static final ModConfigSpec.BooleanValue REGISTER_PROJECTE_EMC = add(CATEGORY_REGISTRATION, "register_projecte_emc", true);

    private static final ModConfigSpec.BooleanValue GIVE_GUIDEBOOK = add(CATEGORY_MISC, "give_guidebook", true);
    private static final ModConfigSpec.BooleanValue SINGLE_CREATIVE_TAB = add(CATEGORY_MISC, "single_creative_tab", false);
    private static final ModConfigSpec.BooleanValue CTRL_INFO = add(CATEGORY_MISC, "ctrl_info", false);
    private static final ModConfigSpec.BooleanValue RARE_DROPS = add(CATEGORY_MISC, "rare_drops", false);
    private static final ModConfigSpec.BooleanValue DUNGEON_LOOT = add(CATEGORY_MISC, "dungeon_loot", false);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> CORIUM_SOLIDIFICATION = addString(CATEGORY_MISC, "corium_solidification", List.of("minecraft:overworld", "minecraft:the_end"), LIST); // TODO 2, -6, -100, 4598, -9999, -11325
    private static final ModConfigSpec.BooleanValue CORIUM_SOLIDIFICATION_LIST_TYPE = add(CATEGORY_MISC, "corium_solidification_list_type", false);
//    private static final ModConfigSpec.BooleanValue ORE_DICT_RAW_MATERIAL_RECIPES = add(CATEGORY_MISC, "ore_dict_raw_material_recipes", false);
//    private static final ModConfigSpec.BooleanValue ORE_DICT_PRIORITY_BOOL = add(CATEGORY_MISC, "ore_dict_priority_bool", true);
//    private static final ModConfigSpec.ConfigValue<List<? extends String>> ORE_DICT_PRIORITY = addString(CATEGORY_MISC, "ore_dict_priority", List.of("minecraft", "thermalfoundation", "techreborn", "nuclearcraft", "immersiveengineering", "mekanism", "appliedenergistics2", "refinedstorage", "actuallyadditions", "libvulpes", "advancedrocketry", "thaumcraft", "biomesoplenty"), LIST);
    private static final ModConfigSpec.BooleanValue HWYLA_ENABLED = add(CATEGORY_MISC, "hwyla_enabled", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int[] syncInts(ModConfigSpec.ConfigValue<List<? extends Integer>> property, boolean fixedArray) {
        return fixedArray ? readIntegerArray(property) : property.get().stream().mapToInt(Integer::intValue).toArray();
    }

    public static boolean[] syncBooleans(ModConfigSpec.ConfigValue<List<? extends Boolean>> property, boolean fixedArray) {
        return fixedArray ? readBooleanArray(property) : Booleans.toArray(property.get().stream().map(Boolean::booleanValue).toList());
    }

    public static double[] syncDoubles(ModConfigSpec.ConfigValue<List<? extends Double>> property, boolean fixedArray) {
        return fixedArray ? readDoubleArray(property) : property.get().stream().mapToDouble(Double::doubleValue).toArray();
    }

    public static String[] syncStrings(ModConfigSpec.ConfigValue<List<? extends String>> property, boolean fixedArray) {
        return fixedArray ? readStringArray(property) : property.get().toArray(String[]::new);
    }
    public static ResourceLocation[] syncResourceLocations(ModConfigSpec.ConfigValue<List<? extends String>> property, boolean fixedArray) {
        return (fixedArray ? Arrays.stream(readStringArray(property)) : property.get().stream()).map(ResourceLocation::parse).toArray(ResourceLocation[]::new);
    }

    public static ModConfigSpec.ConfigValue<Integer> add(String category, String name, int defaultValue) {
        return BUILDER.translation("gui.nc.config." + name).define(List.of(category, name), defaultValue);
    }

    public static ModConfigSpec.IntValue add(String category, String name, int defaultValue, int minValue, int maxValue) {
        return BUILDER.translation("gui.nc.config." + name).defineInRange(List.of(category, name), defaultValue, minValue, maxValue);
    }

    public static ModConfigSpec.BooleanValue add(String category, String name, boolean defaultValue) {
        return BUILDER.translation("gui.nc.config." + name).define(List.of(category, name), defaultValue);
    }

    public static ModConfigSpec.ConfigValue<Double> add(String category, String name, double defaultValue) {
        return BUILDER.translation("gui.nc.config." + name).define(List.of(category, name), defaultValue);
    }

    public static ModConfigSpec.DoubleValue add(String category, String name, double defaultValue, double minValue, double maxValue) {
        return BUILDER.translation("gui.nc.config." + name).defineInRange(List.of(category, name), defaultValue, minValue, maxValue);
    }

    public static ModConfigSpec.ConfigValue<String> add(String category, String name, String defaultValue) {
        return BUILDER.translation("gui.nc.config." + name).define(List.of(category, name), defaultValue);
    }

    public static ModConfigSpec.ConfigValue<List<? extends Integer>> add(String category, String name, List<Integer> defaultValue, int minValue, int maxValue) {
        return BUILDER.translation("gui.nc.config." + name).defineList(List.of(category, name), defaultValue, () -> 0, e -> e instanceof Integer);
    }

    public static ModConfigSpec.ConfigValue<List<? extends Integer>> add(String category, String name, List<Integer> defaultValue, int minValue, int maxValue, boolean fixedArray) {
        return BUILDER.translation("gui.nc.config." + name).defineList(List.of(category, name), defaultValue, fixedArray ? null : () -> 0, e -> range(e, minValue, maxValue));
    }

    public static ModConfigSpec.ConfigValue<List<? extends Boolean>> add(String category, String name, List<Boolean> defaultValue, boolean fixedArray) {
        return BUILDER.translation("gui.nc.config." + name).defineList(List.of(category, name), defaultValue, fixedArray ? null : () -> true, e -> e instanceof Boolean);
    }

    public static ModConfigSpec.ConfigValue<List<? extends String>> addString(String category, String name, List<String> defaultValue, boolean fixedArray) {
        return BUILDER.translation("gui.nc.config." + name).defineList(List.of(category, name), defaultValue, fixedArray ? null : () -> "", e -> e instanceof String);
    }

    public static ModConfigSpec.ConfigValue<List<? extends Double>> add(String category, String name, List<Double> defaultValue, double minValue, double maxValue, boolean fixedArray) {
        return BUILDER.translation("gui.nc.config." + name).defineList(List.of(category, name), defaultValue, fixedArray ? null : () -> 0.0, e -> range(e, minValue, maxValue));
    }

    public static int[] readIntegerArray(ModConfigSpec.ConfigValue<List<? extends Integer>> property) {
        int[] currentList = property.get().stream().mapToInt(Integer::intValue).toArray();
        int currentLength = currentList.length;
        int defaultLength = property.getDefault().size();

        if (currentLength == defaultLength) {
            return currentList;
        }
        int[] newList = new int[defaultLength];
        if (currentLength > defaultLength) {
            System.arraycopy(currentList, 0, newList, 0, defaultLength);
        } else {
            System.arraycopy(currentList, 0, newList, 0, currentLength);
            property.set(property.getDefault());
            int[] defaultList = property.get().stream().mapToInt(Integer::intValue).toArray();
            System.arraycopy(defaultList, currentLength, newList, currentLength, defaultLength - currentLength);
        }
        return newList;
    }

    public static boolean[] readBooleanArray(ModConfigSpec.ConfigValue<List<? extends Boolean>> property) {
        boolean[] currentList = Booleans.toArray(property.get().stream().map(Boolean::booleanValue).toList());
        int currentLength = currentList.length;
        int defaultLength = property.getDefault().size();

        if (currentLength == defaultLength) {
            return currentList;
        }
        boolean[] newList = new boolean[defaultLength];
        if (currentLength > defaultLength) {
            System.arraycopy(currentList, 0, newList, 0, defaultLength);
        } else {
            System.arraycopy(currentList, 0, newList, 0, currentLength);
            property.set(property.getDefault());
            boolean[] defaultList = Booleans.toArray(property.get().stream().map(Boolean::booleanValue).toList());
            System.arraycopy(defaultList, currentLength, newList, currentLength, defaultLength - currentLength);
        }
        return newList;
    }

    public static double[] readDoubleArray(ModConfigSpec.ConfigValue<List<? extends Double>> property) {
        double[] currentList = property.get().stream().mapToDouble(Double::doubleValue).toArray();
        int currentLength = currentList.length;
        int defaultLength = property.getDefault().size();

        if (currentLength == defaultLength) {
            return currentList;
        }
        double[] newList = new double[defaultLength];
        if (currentLength > defaultLength) {
            System.arraycopy(currentList, 0, newList, 0, defaultLength);
        } else {
            System.arraycopy(currentList, 0, newList, 0, currentLength);
            property.set(property.getDefault());
            double[] defaultList = property.get().stream().mapToDouble(Double::doubleValue).toArray();
            System.arraycopy(defaultList, currentLength, newList, currentLength, defaultLength - currentLength);
        }
        return newList;
    }

    public static String[] readStringArray(ModConfigSpec.ConfigValue<List<? extends String>> property) {
        String[] currentList = property.get().toArray(String[]::new);
        int currentLength = currentList.length;
        int defaultLength = property.getDefault().size();

        if (currentLength == defaultLength) {
            return currentList;
        }
        String[] newList = new String[defaultLength];
        if (currentLength > defaultLength) {
            System.arraycopy(currentList, 0, newList, 0, defaultLength);
        } else {
            System.arraycopy(currentList, 0, newList, 0, currentLength);
            property.set(property.getDefault());
            String[] defaultList = property.get().toArray(String[]::new);
            System.arraycopy(defaultList, currentLength, newList, currentLength, defaultLength - currentLength);
        }
        return newList;
    }

    private static boolean range(Object element, int min, int max) {
        return element instanceof Integer integer && integer >= min && integer <= max;
    }

    private static boolean range(Object element, double min, double max) {
        return element instanceof Double num && num >= min && num <= max;
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

//    public static void setCategoryPropertyOrders(Configuration config) {
//        for (Map.Entry<String, List<String>> entry : PROPERTY_ORDER_MAP.entrySet()) {
//            config.setCategoryPropertyOrder(entry.getKey(), entry.getValue());
//        }
//    }
//
//    protected static class ServerConfigEventHandler {
//
//        protected ServerConfigEventHandler() {
//        }
//
//        @SubscribeEvent
//        public void configOnWorldLoad(PlayerEvent.PlayerLoggedInEvent event) {
//            new ConfigUpdatePacket(radiation_enabled, radiation_horse_armor).sendTo(event.getEntity());
//        }
//    }
//
//    public static void onConfigPacket(ConfigUpdatePacket message) {
//        if (!radiation_enabled_public && message.radiation_enabled) {
//            String unloc = "message.nuclearcraft.radiation_config_info" + (ModCheck.jeiLoaded() ? "_jei" : "");
//            Minecraft.getInstance().player.sendSystemMessage(Component.translatable(unloc).withStyle(ChatFormatting.GOLD));
//        }
//        radiation_enabled_public = message.radiation_enabled;
//        radiation_horse_armor_public = message.radiation_horse_armor;
//    }

}