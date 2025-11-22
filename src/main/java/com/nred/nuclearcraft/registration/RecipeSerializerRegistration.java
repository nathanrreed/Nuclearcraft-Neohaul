package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.recipe.*;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.*;
import com.nred.nuclearcraft.recipe.processor.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.registration.Registers.RECIPE_SERIALIZERS;

public class RecipeSerializerRegistration {
    public static final DeferredHolder<RecipeSerializer<?>, CollectorRecipe.Serializer> COLLECTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("collector_recipe", CollectorRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> ALLOY_FURNACE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("alloy_furnace_recipe", () -> new ProcessorRecipe.Serializer(AlloyFurnaceRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> ASSEMBLER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("assembler_recipe", () -> new ProcessorRecipe.Serializer(AssemblerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> CENTRIFUGE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("centrifuge_recipe", () -> new ProcessorRecipe.Serializer(CentrifugeRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> CHEMICAL_REACTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("chemical_reactor_recipe", () -> new ProcessorRecipe.Serializer(ChemicalReactorRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> CRYSTALLIZER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("crystallizer_recipe", () -> new ProcessorRecipe.Serializer(CrystallizerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> DECAY_HASTENER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("decay_hastener_recipe", () -> new ProcessorRecipe.Serializer(DecayHastenerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> ELECTRIC_FURNACE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("electric_furnace_recipe", () -> new ProcessorRecipe.Serializer(ElectricFurnaceRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> ELECTROLYZER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("electrolyzer_recipe", () -> new ProcessorRecipe.Serializer(ElectrolyzerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> FLUID_ENRICHER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("enricher_recipe", () -> new ProcessorRecipe.Serializer(FluidEnricherRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> FLUID_EXTRACTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("extractor_recipe", () -> new ProcessorRecipe.Serializer(FluidExtractorRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> FUEL_REPROCESSOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("fuel_reprocessor_recipe", () -> new ProcessorRecipe.Serializer(FuelReprocessorRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> FLUID_INFUSER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("infuser_recipe", () -> new ProcessorRecipe.Serializer(FluidInfuserRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> INGOT_FORMER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("ingot_former_recipe", () -> new ProcessorRecipe.Serializer(IngotFormerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> MANUFACTORY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("manufactory_recipe", () -> new ProcessorRecipe.Serializer(ManufactoryRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> MELTER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("melter_recipe", () -> new ProcessorRecipe.Serializer(MelterRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> PRESSURIZER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("pressurizer_recipe", () -> new ProcessorRecipe.Serializer(PressurizerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> ROCK_CRUSHER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("rock_crusher_recipe", () -> new ProcessorRecipe.Serializer(RockCrusherRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> FLUID_MIXER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("salt_mixer_recipe", () -> new ProcessorRecipe.Serializer(FluidMixerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> SEPARATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("separator_recipe", () -> new ProcessorRecipe.Serializer(SeparatorRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipe.Serializer> SUPERCOOLER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("supercooler_recipe", () -> new ProcessorRecipe.Serializer(SupercoolerRecipe.class));

    public static final DeferredHolder<RecipeSerializer<?>, TurbineRecipe.Serializer> TURBINE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("turbine_recipe", TurbineRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, FissionModeratorRecipe.Serializer> FISSION_MODERATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("moderator_recipe", FissionModeratorRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, FissionReflectorRecipe.Serializer> FISSION_REFLECTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("reflector_recipe", FissionReflectorRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, SolidFissionRecipe.Serializer> SOLID_FISSION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("solid_fission_recipe", SolidFissionRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, SaltFissionRecipe.Serializer> SALT_FISSION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("salt_fission_recipe", SaltFissionRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, PebbleFissionRecipe.Serializer> PEBBLE_FISSION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("pebble_fission_recipe", PebbleFissionRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, FissionHeatingRecipe.Serializer> FISSION_HEATING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("fission_heating_recipe", FissionHeatingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, FissionIrradiatorRecipe.Serializer> FISSION_IRRADIATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("fission_irradiator_recipe", FissionIrradiatorRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, FissionCoolantHeaterRecipe.Serializer> FISSION_COOLANT_HEATER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("fission_heater_recipe", FissionCoolantHeaterRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, FissionEmergencyCoolingRecipe.Serializer> FISSION_EMERGENCY_COOLING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("fission_emergency_cooling_recipe", FissionEmergencyCoolingRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, CondenserRecipe.Serializer> CONDENSER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("condenser_recipe", CondenserRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, HeatExchangerRecipe.Serializer> HEAT_EXCHANGER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("heat_exchanger_recipe", HeatExchangerRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, ElectrolyzerCathodeRecipe.Serializer> ELECTROLYZER_CATHODE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("electrolyzer_cathode_recipe", ElectrolyzerCathodeRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, ElectrolyzerAnodeRecipe.Serializer> ELECTROLYZER_ANODE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("electrolyzer_anode_recipe", ElectrolyzerAnodeRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MachineDiaphragmRecipe.Serializer> MACHINE_DIAPHRAGM_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("machine_diaphragm_recipe", MachineDiaphragmRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MachineSieveAssemblyRecipe.Serializer> MACHINE_SIEVE_ASSEMBLY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("machine_sieve_assembly_recipe", MachineSieveAssemblyRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, InfiltratorPressureFluidRecipe.Serializer> INFILTRATOR_PRESSURE_FLUID_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("infiltrator_pressure_fluid_recipe", InfiltratorPressureFluidRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MultiblockInfiltratorRecipe.Serializer> MULTIBLOCK_INFILTRATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("multiblock_infiltrator_recipe", MultiblockInfiltratorRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MultiblockDistillerRecipe.Serializer> MULTIBLOCK_DISTILLER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("multiblock_distiller_recipe", MultiblockDistillerRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MultiblockElectrolyzerRecipe.Serializer> MULTIBLOCK_ELECTROLYZER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("multiblock_electrolyzer_recipe", MultiblockElectrolyzerRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, ElectrolyzerElectrolyteRecipe.Serializer> ELECTROLYZER_ELECTROLYTE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("electrolyzer_electrolyte_recipe", ElectrolyzerElectrolyteRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, DecayGeneratorRecipe.Serializer> DECAY_GENERATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("decay_generator_recipe", DecayGeneratorRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RadiationScrubberRecipe.Serializer> RADIATION_SCRUBBER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("radiation_scrubber_recipe", RadiationScrubberRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RadiationBlockMutationRecipe.Serializer> RADIATION_BLOCK_MUTATION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("radiation_block_mutation_recipe", RadiationBlockMutationRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RadiationBlockPurificationRecipe.Serializer> RADIATION_BLOCK_PURIFICATION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("radiation_block_purification_recipe", RadiationBlockPurificationRecipe.Serializer::new);

    public static void init() {
    }
}