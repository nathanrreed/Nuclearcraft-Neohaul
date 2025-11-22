package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.recipe.*;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.Registers.RECIPE_TYPES;

public class RecipeTypeRegistration {
    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(final String identifier) {
        return RECIPE_TYPES.register(identifier, registryName -> new RecipeType<>() {
            @Override
            public String toString() {
                return registryName.toString();
            }
        });
    }

    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> COBBLE_GENERATOR_RECIPE_TYPE = register("cobblestone_generator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> COBBLE_GENERATOR_COMPACT_RECIPE_TYPE = register("cobblestone_generator_compact");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> COBBLE_GENERATOR_DENSE_RECIPE_TYPE = register("cobblestone_generator_dense");

    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> WATER_SOURCE_RECIPE_TYPE = register("water_source");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> WATER_SOURCE_COMPACT_RECIPE_TYPE = register("water_source_compact");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> WATER_SOURCE_DENSE_RECIPE_TYPE = register("water_source_dense");

    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> NITROGEN_COLLECTOR_RECIPE_TYPE = register("nitrogen_collector");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> NITROGEN_COLLECTOR_COMPACT_RECIPE_TYPE = register("nitrogen_collector_compact");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE = register("nitrogen_collector_dense");

    public static final Map<String, DeferredHolder<RecipeType<?>, RecipeType<ProcessorRecipe>>> PROCESSOR_RECIPE_TYPES = PROCESSOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), RecipeTypeRegistration::register));

    public static final DeferredHolder<RecipeType<?>, RecipeType<TurbineRecipe>> TURBINE_RECIPE_TYPE = register("turbine");

    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionModeratorRecipe>> FISSION_MODERATOR_RECIPE_TYPE = register("fission_moderator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionReflectorRecipe>> FISSION_REFLECTOR_RECIPE_TYPE = register("fission_reflector");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionIrradiatorRecipe>> FISSION_IRRADIATOR_RECIPE_TYPE = register("fission_irradiator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionCoolantHeaterRecipe>> COOLANT_HEATER_RECIPE_TYPE = register("coolant_heater");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionHeatingRecipe>> FISSION_HEATING_RECIPE_TYPE = register("fission_heating");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionEmergencyCoolingRecipe>> FISSION_EMERGENCY_COOLING_RECIPE_TYPE = register("fission_emergency_cooling");
    public static final DeferredHolder<RecipeType<?>, RecipeType<SolidFissionRecipe>> SOLID_FISSION_RECIPE_TYPE = register("solid_fission");
    public static final DeferredHolder<RecipeType<?>, RecipeType<SaltFissionRecipe>> SALT_FISSION_RECIPE_TYPE = register("salt_fission");
    public static final DeferredHolder<RecipeType<?>, RecipeType<PebbleFissionRecipe>> PEBBLE_FISSION_RECIPE_TYPE = register("pebble_fission");

    public static final DeferredHolder<RecipeType<?>, RecipeType<HeatExchangerRecipe>> HEAT_EXCHANGER_RECIPE_TYPE = register("heat_exchanger");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CondenserRecipe>> CONDENSER_RECIPE_TYPE = register("condenser");

    public static final DeferredHolder<RecipeType<?>, RecipeType<ElectrolyzerCathodeRecipe>> ELECTROLYZER_CATHODE_RECIPE_TYPE = register("electrolyzer_cathode");
    public static final DeferredHolder<RecipeType<?>, RecipeType<ElectrolyzerAnodeRecipe>> ELECTROLYZER_ANODE_RECIPE_TYPE = register("electrolyzer_anode");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MachineDiaphragmRecipe>> MACHINE_DIAPHRAGM_RECIPE_TYPE = register("machine_diaphragm");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MachineSieveAssemblyRecipe>> MACHINE_SIEVE_ASSEMBLY_RECIPE_TYPE = register("machine_sieve_assembly");
    public static final DeferredHolder<RecipeType<?>, RecipeType<InfiltratorPressureFluidRecipe>> INFILTRATOR_PRESSURE_FLUID_RECIPE_TYPE = register("infiltrator_pressure_fluid");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MultiblockInfiltratorRecipe>> MULTIBLOCK_INFILTRATOR_RECIPE_TYPE = register("multiblock_infiltrator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MultiblockDistillerRecipe>> MULTIBLOCK_DISTILLER_RECIPE_TYPE = register("multiblock_distiller");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MultiblockElectrolyzerRecipe>> MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE = register("multiblock_electrolyzer");
    public static final DeferredHolder<RecipeType<?>, RecipeType<ElectrolyzerElectrolyteRecipe>> ELECTROLYZER_ELECTROLYTE_RECIPE_TYPE = register("electrolyzer_electrolyte");

    public static final DeferredHolder<RecipeType<?>, RecipeType<DecayGeneratorRecipe>> DECAY_GENERATOR_RECIPE_TYPE = register("decay_generator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RadiationScrubberRecipe>> RADIATION_SCRUBBER_RECIPE_TYPE = register("radiation_scrubber");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RadiationBlockMutationRecipe>> RADIATION_BLOCK_MUTATION_RECIPE_TYPE = register("radiation_block_mutation");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RadiationBlockPurificationRecipe>> RADIATION_BLOCK_PURIFICATION_RECIPE_TYPE = register("radiation_block_purification");

    public static void init() {
    }
}