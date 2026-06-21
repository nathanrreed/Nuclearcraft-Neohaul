package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.recipe.*;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.*;
import com.nred.nuclearcraft.recipe.processor.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.nred.nuclearcraft.registration.Registers.RECIPE_TYPES;

public class RecipeTypeRegistration {
    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(final String identifier) {
        return RECIPE_TYPES.register(identifier, RecipeType::simple);
    }

    public static final DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> COLLECTOR_RECIPE_TYPE = register("collector");

public static final DeferredHolder<RecipeType<?>, RecipeType<AlloyFurnaceRecipe>>    ALLOY_FURNACE_RECIPE_TYPE    = register("alloy_furnace");
public static final DeferredHolder<RecipeType<?>, RecipeType<AssemblerRecipe>>       ASSEMBLER_RECIPE_TYPE        = register("assembler");
public static final DeferredHolder<RecipeType<?>, RecipeType<CentrifugeRecipe>>      CENTRIFUGE_RECIPE_TYPE       = register("centrifuge");
public static final DeferredHolder<RecipeType<?>, RecipeType<ChemicalReactorRecipe>> CHEMICAL_REACTOR_RECIPE_TYPE = register("chemical_reactor");
public static final DeferredHolder<RecipeType<?>, RecipeType<CrystallizerRecipe>>    CRYSTALLIZER_RECIPE_TYPE     = register("crystallizer");
public static final DeferredHolder<RecipeType<?>, RecipeType<DecayHastenerRecipe>>   DECAY_HASTENER_RECIPE_TYPE   = register("decay_hastener");
public static final DeferredHolder<RecipeType<?>, RecipeType<ElectricFurnaceRecipe>> ELECTRIC_FURNACE_RECIPE_TYPE = register("electric_furnace");
public static final DeferredHolder<RecipeType<?>, RecipeType<ElectrolyzerRecipe>>    ELECTROLYZER_RECIPE_TYPE     = register("electrolyzer");
public static final DeferredHolder<RecipeType<?>, RecipeType<FluidEnricherRecipe>>   FLUID_ENRICHER_RECIPE_TYPE   = register("fluid_enricher");
public static final DeferredHolder<RecipeType<?>, RecipeType<FluidExtractorRecipe>>  FLUID_EXTRACTOR_RECIPE_TYPE  = register("fluid_extractor");
public static final DeferredHolder<RecipeType<?>, RecipeType<FluidInfuserRecipe>>    FLUID_INFUSER_RECIPE_TYPE    = register("fluid_infuser");
public static final DeferredHolder<RecipeType<?>, RecipeType<FluidMixerRecipe>>      FLUID_MIXER_RECIPE_TYPE      = register("fluid_mixer");
public static final DeferredHolder<RecipeType<?>, RecipeType<FuelReprocessorRecipe>> FUEL_REPROCESSOR_RECIPE_TYPE = register("fuel_reprocessor");
public static final DeferredHolder<RecipeType<?>, RecipeType<IngotFormerRecipe>>     INGOT_FORMER_RECIPE_TYPE     = register("ingot_former");
public static final DeferredHolder<RecipeType<?>, RecipeType<ManufactoryRecipe>>     MANUFACTORY_RECIPE_TYPE      = register("manufactory");
public static final DeferredHolder<RecipeType<?>, RecipeType<MelterRecipe>>          MELTER_RECIPE_TYPE           = register("melter");
public static final DeferredHolder<RecipeType<?>, RecipeType<PressurizerRecipe>>     PRESSURIZER_RECIPE_TYPE      = register("pressurizer");
public static final DeferredHolder<RecipeType<?>, RecipeType<RockCrusherRecipe>>     ROCK_CRUSHER_RECIPE_TYPE     = register("rock_crusher");
public static final DeferredHolder<RecipeType<?>, RecipeType<SeparatorRecipe>>       SEPARATOR_RECIPE_TYPE        = register("separator");
public static final DeferredHolder<RecipeType<?>, RecipeType<SupercoolerRecipe>>     SUPERCOOLER_RECIPE_TYPE      = register("supercooler");

    public static final HashMap<String, DeferredHolder<RecipeType<?>, RecipeType<ProcessorRecipeDyn>>> _PROCESSOR_RECIPE_DYN_TYPES = new HashMap<>();
    public static final Map<String, DeferredHolder<RecipeType<?>, RecipeType<ProcessorRecipeDyn>>> PROCESSOR_RECIPE_DYN_TYPES = Collections.synchronizedMap(_PROCESSOR_RECIPE_DYN_TYPES);

    public static final DeferredHolder<RecipeType<?>, RecipeType<TurbineRecipe>> TURBINE_RECIPE_TYPE = register("turbine");

    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionIrradiatorRecipe>> FISSION_IRRADIATOR_RECIPE_TYPE = register("fission_irradiator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionCoolantHeaterRecipe>> COOLANT_HEATER_RECIPE_TYPE = register("coolant_heater");
    public static final DeferredHolder<RecipeType<?>, RecipeType<PebbleFissionCoolerRecipe>> COOLER_RECIPE_TYPE = register("gas_cooler");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionHeatingRecipe>> FISSION_HEATING_RECIPE_TYPE = register("fission_heating");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FissionEmergencyCoolingRecipe>> FISSION_EMERGENCY_COOLING_RECIPE_TYPE = register("fission_emergency_cooling");
    public static final DeferredHolder<RecipeType<?>, RecipeType<SolidFissionRecipe>> SOLID_FISSION_RECIPE_TYPE = register("solid_fission");
    public static final DeferredHolder<RecipeType<?>, RecipeType<SaltFissionRecipe>> SALT_FISSION_RECIPE_TYPE = register("salt_fission");
    public static final DeferredHolder<RecipeType<?>, RecipeType<PebbleFissionRecipe>> PEBBLE_FISSION_RECIPE_TYPE = register("pebble_fission");

    public static final DeferredHolder<RecipeType<?>, RecipeType<HeatExchangerRecipe>> HEAT_EXCHANGER_RECIPE_TYPE = register("heat_exchanger");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CondenserRecipe>> CONDENSER_RECIPE_TYPE = register("condenser");

    public static final DeferredHolder<RecipeType<?>, RecipeType<MultiblockInfiltratorRecipe>> MULTIBLOCK_INFILTRATOR_RECIPE_TYPE = register("multiblock_infiltrator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MultiblockDistillerRecipe>> MULTIBLOCK_DISTILLER_RECIPE_TYPE = register("multiblock_distiller");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MultiblockElectrolyzerRecipe>> MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE = register("multiblock_electrolyzer");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MultiblockDecayPoolRecipe>> MULTIBLOCK_DECAY_POOL_RECIPE_TYPE = register("multiblock_decay_pool");
    public static final DeferredHolder<RecipeType<?>, RecipeType<DecayPoolHeatSourceRecipe>> DECAY_POOL_HEAT_SOURCE_RECIPE_TYPE = register("decay_pool_heat_source");

    public static final DeferredHolder<RecipeType<?>, RecipeType<DecayGeneratorRecipe>> DECAY_GENERATOR_RECIPE_TYPE = register("decay_generator");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RadiationScrubberRecipe>> RADIATION_SCRUBBER_RECIPE_TYPE = register("radiation_scrubber");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RadiationBlockMutationRecipe>> RADIATION_BLOCK_MUTATION_RECIPE_TYPE = register("radiation_block_mutation");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RadiationBlockPurificationRecipe>> RADIATION_BLOCK_PURIFICATION_RECIPE_TYPE = register("radiation_block_purification");

    public static void init() {
    }
}