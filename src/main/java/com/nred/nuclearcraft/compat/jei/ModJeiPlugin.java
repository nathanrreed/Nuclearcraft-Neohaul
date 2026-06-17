package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.jei.JeiRecipeViewerImpl.*;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
import com.nred.nuclearcraft.recipe.processor.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import com.nred.nuclearcraft.util.StreamHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.CONDENSER_DISSIPATION_TOOLTIP;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.INFILTRATOR_PRESSURE_FLUID_TOOLTIP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.multiblock.hx.CondenserLogic.getCondenserDissipationFluids;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.*;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    public static IRecipeCategory<RecipeHolder<AlloyFurnaceRecipe>> JEI_ALLOY_FURNACE_CATEGORY;
    public static IRecipeCategory<RecipeHolder<AssemblerRecipe>> JEI_ASSEMBLER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<CentrifugeRecipe>> JEI_CENTRIFUGE_CATEGORY;
    public static IRecipeCategory<RecipeHolder<ChemicalReactorRecipe>> JEI_CHEMICAL_REACTOR_CATEGORY;
    public static IRecipeCategory<RecipeHolder<CrystallizerRecipe>> JEI_CRYSTALLIZER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<DecayHastenerRecipe>> JEI_DECAY_HASTENER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<ElectricFurnaceRecipe>> JEI_ELECTRIC_FURNACE_CATEGORY;
    public static IRecipeCategory<RecipeHolder<ElectrolyzerRecipe>> JEI_ELECTROLYZER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FluidEnricherRecipe>> JEI_FLUID_ENRICHER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FluidExtractorRecipe>> JEI_FLUID_EXTRACTOR_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FluidInfuserRecipe>> JEI_FLUID_INFUSER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FluidMixerRecipe>> JEI_FLUID_MIXER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FuelReprocessorRecipe>> JEI_FUEL_REPROCESSOR_CATEGORY;
    public static IRecipeCategory<RecipeHolder<IngotFormerRecipe>> JEI_INGOT_FORMER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<ManufactoryRecipe>> JEI_MANUFACTORY_CATEGORY;
    public static IRecipeCategory<RecipeHolder<MelterRecipe>> JEI_MELTER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<PressurizerRecipe>> JEI_PRESSURIZER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<RockCrusherRecipe>> JEI_ROCK_CRUSHER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<SeparatorRecipe>> JEI_SEPARATOR_CATEGORY;
    public static IRecipeCategory<RecipeHolder<SupercoolerRecipe>> JEI_SUPERCOOLER_CATEGORY;

    public static HashMap<String, IRecipeCategory<RecipeHolder<ProcessorRecipeDyn>>> JEI_PROCESSOR_CATEGORIES_DYN = new HashMap<>();

    public static IRecipeCategory<RecipeHolder<CollectorRecipe>> JEI_COLLECTOR_CATEGORY;
    public static IRecipeCategory<RecipeHolder<TurbineRecipe>> JEI_TURBINE_CATEGORY;
    public static IRecipeCategory<RecipeHolder<SolidFissionRecipe>> JEI_SOLID_FISSION_CATEGORY;
    public static IRecipeCategory<RecipeHolder<PebbleFissionRecipe>> JEI_PEBBLE_FISSION_CATEGORY;
    public static IRecipeCategory<RecipeHolder<SaltFissionRecipe>> JEI_SALT_FISSION_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FissionIrradiatorRecipe>> JEI_IRRADIATOR_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FissionHeatingRecipe>> JEI_VENT_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FissionEmergencyCoolingRecipe>> JEI_EMERGENCY_COOLING_CATEGORY;
    public static IRecipeCategory<RecipeHolder<FissionCoolantHeaterRecipe>> JEI_SALT_COOLING_CATEGORY;
    public static IRecipeCategory<RecipeHolder<PebbleFissionCoolerRecipe>> JEI_GAS_COOLING_CATEGORY;
    public static IRecipeCategory<RecipeHolder<HeatExchangerRecipe>> JEI_HEAT_EXCHANGER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<CondenserRecipe>> JEI_CONDENSER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<MultiblockDistillerRecipe>> JEI_MULTIBLOCK_DISTILLER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<MultiblockElectrolyzerRecipe>> JEI_MULTIBLOCK_ELECTROLYZER_CATEGORY;
    public static IRecipeCategory<RecipeHolder<MultiblockInfiltratorRecipe>> JEI_MULTIBLOCK_INFILTRATOR_CATEGORY;
    public static IRecipeCategory<RecipeHolder<DecayGeneratorRecipe>> JEI_DECAY_GENERATOR_CATEGORY;
    public static IRecipeCategory<RecipeHolder<RadiationScrubberRecipe>> JEI_RADIATION_SCRUBBER_CATEGORY;

    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_CONDENSER_DISSIPATION_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_INFILTRATOR_PRESSURE_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_MODERATOR_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_REFLECTOR_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_ELECTROLYZER_CATHODE_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_ELECTROLYZER_ANODE_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_DIAPHRAGM_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_SIEVE_ASSEMBLY_CATEGORY;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ncLoc("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

        JEI_ALLOY_FURNACE_CATEGORY = new JeiAlloyFurnaceCategory(helper);
        JEI_ASSEMBLER_CATEGORY = new JeiAssemblerCategory(helper);
        JEI_CENTRIFUGE_CATEGORY = new JeiCentrifugeCategory(helper);
        JEI_CHEMICAL_REACTOR_CATEGORY = new JeiChemicalReactorCategory(helper);
        JEI_CRYSTALLIZER_CATEGORY = new JeiCrystallizerCategory(helper);
        JEI_DECAY_HASTENER_CATEGORY = new JeiDecayHastenerCategory(helper);
        JEI_ELECTRIC_FURNACE_CATEGORY = new JeiElectricFurnaceCategory(helper);
        JEI_ELECTROLYZER_CATEGORY = new JeiElectrolyzerCategory(helper);
        JEI_FLUID_ENRICHER_CATEGORY = new JeiFluidEnricherCategory(helper);
        JEI_FLUID_EXTRACTOR_CATEGORY = new JeiFluidExtractorCategory(helper);
        JEI_FLUID_INFUSER_CATEGORY = new JeiFluidInfuserCategory(helper);
        JEI_FLUID_MIXER_CATEGORY = new JeiFluidMixerCategory(helper);
        JEI_FUEL_REPROCESSOR_CATEGORY = new JeiFuelReprocessorCategory(helper);
        JEI_INGOT_FORMER_CATEGORY = new JeiIngotFormerCategory(helper);
        JEI_MANUFACTORY_CATEGORY = new JeiManufactoryCategory(helper);
        JEI_MELTER_CATEGORY = new JeiMelterCategory(helper);
        JEI_PRESSURIZER_CATEGORY = new JeiPressurizerCategory(helper);
        JEI_ROCK_CRUSHER_CATEGORY = new JeiRockCrusherCategory(helper);
        JEI_SEPARATOR_CATEGORY = new JeiSeparatorCategory(helper);
        JEI_SUPERCOOLER_CATEGORY = new JeiSupercoolerCategory(helper);

        for (String name : PROCESSOR_RECIPE_DYN_TYPES.keySet()) {
            JEI_PROCESSOR_CATEGORIES_DYN.put(name, new JeiProcessorCategoryDyn(helper, name));
            registration.addRecipeCategories(JEI_PROCESSOR_CATEGORIES_DYN.get(name));
        }

        registration.addRecipeCategories(JEI_ALLOY_FURNACE_CATEGORY);
        registration.addRecipeCategories(JEI_ASSEMBLER_CATEGORY);
        registration.addRecipeCategories(JEI_CENTRIFUGE_CATEGORY);
        registration.addRecipeCategories(JEI_CHEMICAL_REACTOR_CATEGORY);
        registration.addRecipeCategories(JEI_CRYSTALLIZER_CATEGORY);
        registration.addRecipeCategories(JEI_DECAY_HASTENER_CATEGORY);
        registration.addRecipeCategories(JEI_ELECTRIC_FURNACE_CATEGORY);
        registration.addRecipeCategories(JEI_ELECTROLYZER_CATEGORY);
        registration.addRecipeCategories(JEI_FLUID_ENRICHER_CATEGORY);
        registration.addRecipeCategories(JEI_FLUID_EXTRACTOR_CATEGORY);
        registration.addRecipeCategories(JEI_FLUID_INFUSER_CATEGORY);
        registration.addRecipeCategories(JEI_FLUID_MIXER_CATEGORY);
        registration.addRecipeCategories(JEI_FUEL_REPROCESSOR_CATEGORY);
        registration.addRecipeCategories(JEI_INGOT_FORMER_CATEGORY);
        registration.addRecipeCategories(JEI_MANUFACTORY_CATEGORY);
        registration.addRecipeCategories(JEI_MELTER_CATEGORY);
        registration.addRecipeCategories(JEI_PRESSURIZER_CATEGORY);
        registration.addRecipeCategories(JEI_ROCK_CRUSHER_CATEGORY);
        registration.addRecipeCategories(JEI_SEPARATOR_CATEGORY);
        registration.addRecipeCategories(JEI_SUPERCOOLER_CATEGORY);

        JEI_COLLECTOR_CATEGORY = new JeiCollectorCategory(helper);
        registration.addRecipeCategories(JEI_COLLECTOR_CATEGORY);
        JEI_TURBINE_CATEGORY = new JeiTurbineCategory(helper);
        registration.addRecipeCategories(JEI_TURBINE_CATEGORY);
        JEI_SOLID_FISSION_CATEGORY = new JeiSolidFissionCategory(helper);
        registration.addRecipeCategories(JEI_SOLID_FISSION_CATEGORY);
        JEI_PEBBLE_FISSION_CATEGORY = new JeiPebbleFissionCategory(helper);
        registration.addRecipeCategories(JEI_PEBBLE_FISSION_CATEGORY);
        JEI_SALT_FISSION_CATEGORY = new JeiSaltFissionCategory(helper);
        registration.addRecipeCategories(JEI_SALT_FISSION_CATEGORY);
        JEI_IRRADIATOR_CATEGORY = new JeiFissionIrradiatorCategory(helper);
        registration.addRecipeCategories(JEI_IRRADIATOR_CATEGORY);
        JEI_VENT_CATEGORY = new JeiRecipeViewerImpl.JeiFissionVentCategory(helper);
        registration.addRecipeCategories(JEI_VENT_CATEGORY);
        JEI_EMERGENCY_COOLING_CATEGORY = new JeiFissionEmergencyCoolingCategory(helper);
        registration.addRecipeCategories(JEI_EMERGENCY_COOLING_CATEGORY);
        JEI_SALT_COOLING_CATEGORY = new JeiSaltCoolingCategory(helper);
        registration.addRecipeCategories(JEI_SALT_COOLING_CATEGORY);
        JEI_GAS_COOLING_CATEGORY = new JeiGasCoolingCategory(helper);
        registration.addRecipeCategories(JEI_GAS_COOLING_CATEGORY);
        JEI_MODERATOR_CATEGORY = new JeiBasicInfoCategory(helper, "fission_moderator", HEAVY_WATER_MODERATOR);
        registration.addRecipeCategories(JEI_MODERATOR_CATEGORY);
        JEI_REFLECTOR_CATEGORY = new JeiBasicInfoCategory(helper, "fission_reflector", FISSION_REACTOR_MAP.get("beryllium_carbon_reflector"));
        registration.addRecipeCategories(JEI_REFLECTOR_CATEGORY);
        JEI_HEAT_EXCHANGER_CATEGORY = new JeiHeatExchangerCategory(helper);
        registration.addRecipeCategories(JEI_HEAT_EXCHANGER_CATEGORY);
        JEI_CONDENSER_CATEGORY = new JeiCondenserCategory(helper);
        registration.addRecipeCategories(JEI_CONDENSER_CATEGORY);
        JEI_CONDENSER_DISSIPATION_CATEGORY = new JeiBasicInfoCategory(helper, "condenser_dissipation", HX_MAP.get("heat_exchanger_inlet"));
        registration.addRecipeCategories(JEI_CONDENSER_DISSIPATION_CATEGORY);
        JEI_MULTIBLOCK_DISTILLER_CATEGORY = new JeiMultiblockDistillerCategory(helper);
        registration.addRecipeCategories(JEI_MULTIBLOCK_DISTILLER_CATEGORY);
        JEI_MULTIBLOCK_ELECTROLYZER_CATEGORY = new JeiMultiblockElectrolyzerCategory(helper);
        registration.addRecipeCategories(JEI_MULTIBLOCK_ELECTROLYZER_CATEGORY);
        JEI_MULTIBLOCK_INFILTRATOR_CATEGORY = new JeiMultiblockInfiltratorCategory(helper);
        registration.addRecipeCategories(JEI_MULTIBLOCK_INFILTRATOR_CATEGORY);
        JEI_ELECTROLYZER_CATHODE_CATEGORY = new JeiBasicInfoCategory(helper, "electrolyzer_cathode", MACHINE_MAP.get("electrolyzer_cathode_terminal"));
        registration.addRecipeCategories(JEI_ELECTROLYZER_CATHODE_CATEGORY);
        JEI_ELECTROLYZER_ANODE_CATEGORY = new JeiBasicInfoCategory(helper, "electrolyzer_anode", MACHINE_MAP.get("electrolyzer_anode_terminal"));
        registration.addRecipeCategories(JEI_ELECTROLYZER_ANODE_CATEGORY);
        JEI_INFILTRATOR_PRESSURE_CATEGORY = new JeiBasicInfoCategory(helper, "infiltrator_pressure", MACHINE_MAP.get("infiltrator_pressure_chamber"));
        registration.addRecipeCategories(JEI_INFILTRATOR_PRESSURE_CATEGORY);
        JEI_DIAPHRAGM_CATEGORY = new JeiBasicInfoCategory(helper, "machine_diaphragm", MACHINE_MAP.get("sintered_steel_diaphragm"));
        registration.addRecipeCategories(JEI_DIAPHRAGM_CATEGORY);
        JEI_SIEVE_ASSEMBLY_CATEGORY = new JeiBasicInfoCategory(helper, "machine_sieve_assembly", MACHINE_MAP.get("steel_sieve_assembly"));
        registration.addRecipeCategories(JEI_SIEVE_ASSEMBLY_CATEGORY);
        JEI_DECAY_GENERATOR_CATEGORY = new JeiDecayGeneratorCategory(helper);
        registration.addRecipeCategories(JEI_DECAY_GENERATOR_CATEGORY);
        JEI_RADIATION_SCRUBBER_CATEGORY = new JeiRadiationScrubberCategory(helper);
        registration.addRecipeCategories(JEI_RADIATION_SCRUBBER_CATEGORY);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // TODO replace?
        registration.addRecipeCatalysts(JEI_ALLOY_FURNACE_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("alloy_furnace"));
        registration.addRecipeCatalysts(JEI_ASSEMBLER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("assembler"));
        registration.addRecipeCatalysts(JEI_CENTRIFUGE_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("centrifuge"));
        registration.addRecipeCatalysts(JEI_CHEMICAL_REACTOR_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("chemical_reactor"));
        registration.addRecipeCatalysts(JEI_CRYSTALLIZER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("crystallizer"));
        registration.addRecipeCatalysts(JEI_DECAY_HASTENER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("decay_hastener"));
        registration.addRecipeCatalysts(JEI_ELECTRIC_FURNACE_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("electric_furnace"));
        registration.addRecipeCatalysts(JEI_ELECTROLYZER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("electrolyzer"));
        registration.addRecipeCatalysts(JEI_FLUID_ENRICHER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("fluid_enricher"));
        registration.addRecipeCatalysts(JEI_FLUID_EXTRACTOR_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("fluid_extractor"));
        registration.addRecipeCatalysts(JEI_FLUID_INFUSER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("fluid_infuser"));
        registration.addRecipeCatalysts(JEI_FLUID_MIXER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("fluid_mixer"));
        registration.addRecipeCatalysts(JEI_FUEL_REPROCESSOR_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("fuel_reprocessor"));
        registration.addRecipeCatalysts(JEI_INGOT_FORMER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("ingot_former"));
        registration.addRecipeCatalysts(JEI_MANUFACTORY_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("manufactory"));
        registration.addRecipeCatalysts(JEI_MELTER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("melter"));
        registration.addRecipeCatalysts(JEI_PRESSURIZER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("pressurizer"));
        registration.addRecipeCatalysts(JEI_ROCK_CRUSHER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("rock_crusher"));
        registration.addRecipeCatalysts(JEI_SEPARATOR_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("separator"));
        registration.addRecipeCatalysts(JEI_SUPERCOOLER_CATEGORY.getRecipeType(), PROCESSOR_MAP.get("supercooler"));

        for (String name : PROCESSOR_RECIPE_DYN_TYPES.keySet()) {
            registration.addRecipeCatalysts(JEI_PROCESSOR_CATEGORIES_DYN.get(name).getRecipeType(), PROCESSOR_MAP.get(name));
        }

        registration.addRecipeCatalysts(RecipeTypes.SMELTING, VanillaTypes.ITEM_STACK, List.of(PROCESSOR_MAP.get("electric_furnace").toStack(), NUCLEAR_FURNACE.toStack()));

        registration.addRecipeCatalysts(JEI_COLLECTOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, COLLECTOR_MAP.values().stream().map(DeferredBlock::toStack).toList());
        registration.addRecipeCatalysts(JEI_TURBINE_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, StreamHelper.concatToList(Stream.of(TURBINE_MAP.get("turbine_controller")), TURBINE_ENTITY_TYPE.get("rotor_stator").get().getValidBlocks().stream(), TURBINE_ENTITY_TYPE.get("rotor_blade").get().getValidBlocks().stream()).stream().map(e -> e.asItem().getDefaultInstance()).toList());
        registration.addRecipeCatalysts(JEI_SOLID_FISSION_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller").toStack(), FISSION_REACTOR_MAP.get("fission_fuel_cell").toStack()));
        registration.addRecipeCatalysts(JEI_PEBBLE_FISSION_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("pebble_bed_fission_controller").toStack(), FISSION_REACTOR_MAP.get("fission_fuel_chamber").toStack()));
        registration.addRecipeCatalysts(JEI_SALT_FISSION_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("molten_salt_fission_controller").toStack(), FISSION_REACTOR_MAP.get("fission_fuel_vessel").toStack()));
        registration.addRecipeCatalysts(JEI_IRRADIATOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("fission_irradiator").toStack()));
        registration.addRecipeCatalysts(JEI_VENT_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("fission_vent").toStack()));
        registration.addRecipeCatalysts(JEI_EMERGENCY_COOLING_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("fission_vent").toStack()));
        registration.addRecipeCatalysts(JEI_SALT_COOLING_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, FISSION_ENTITY_TYPE.get("coolant_heater").get().getValidBlocks().stream().map(e -> e.asItem().getDefaultInstance()).toList());
        registration.addRecipeCatalysts(JEI_GAS_COOLING_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, FISSION_ENTITY_TYPE.get("cooler").get().getValidBlocks().stream().map(e -> e.asItem().getDefaultInstance()).toList());
        registration.addRecipeCatalysts(JEI_MODERATOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, BuiltInRegistries.BLOCK.getDataMap(FISSION_MODERATOR_DATA).keySet().stream().map(e -> Objects.requireNonNull(BuiltInRegistries.BLOCK.get(e)).asItem().getDefaultInstance()).toList());
        registration.addRecipeCatalysts(JEI_REFLECTOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, BuiltInRegistries.BLOCK.getDataMap(FISSION_REFLECTOR_DATA).keySet().stream().map(e -> Objects.requireNonNull(BuiltInRegistries.BLOCK.get(e)).asItem().getDefaultInstance()).toList());
        List<ItemStack> hx_tubes = HX_ENTITY_TYPE.get("tube").get().getValidBlocks().stream().map(e -> e.asItem().getDefaultInstance()).toList();

        registration.addRecipeCatalysts(JEI_HEAT_EXCHANGER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, Stream.concat(Stream.of(HX_MAP.get("heat_exchanger_controller").toStack()), hx_tubes.stream()).toList());
        registration.addRecipeCatalysts(JEI_CONDENSER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, Stream.concat(Stream.of(HX_MAP.get("condenser_controller").toStack()), hx_tubes.stream()).toList());
        registration.addRecipeCatalysts(JEI_MULTIBLOCK_DISTILLER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("distiller_controller").toStack()));
        registration.addRecipeCatalysts(JEI_CONDENSER_DISSIPATION_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(HX_MAP.get("heat_exchanger_inlet").toStack()));
        registration.addRecipeCatalysts(JEI_MULTIBLOCK_ELECTROLYZER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("electrolyzer_controller").toStack()));
        registration.addRecipeCatalysts(JEI_MULTIBLOCK_INFILTRATOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("infiltrator_controller").toStack()));
        registration.addRecipeCatalysts(JEI_ELECTROLYZER_CATHODE_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("electrolyzer_cathode_terminal").toStack()));
        registration.addRecipeCatalysts(JEI_ELECTROLYZER_ANODE_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("electrolyzer_anode_terminal").toStack()));
        registration.addRecipeCatalysts(JEI_INFILTRATOR_PRESSURE_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("infiltrator_pressure_chamber").toStack()));
        registration.addRecipeCatalysts(JEI_DIAPHRAGM_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, BuiltInRegistries.BLOCK.getDataMap(MACHINE_DIAPHRAGM_DATA).keySet().stream().map(e -> Objects.requireNonNull(BuiltInRegistries.BLOCK.get(e)).asItem().getDefaultInstance()).toList());
        registration.addRecipeCatalysts(JEI_SIEVE_ASSEMBLY_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, BuiltInRegistries.BLOCK.getDataMap(MACHINE_SIEVE_ASSEMBLY_DATA).keySet().stream().map(e -> Objects.requireNonNull(BuiltInRegistries.BLOCK.get(e)).asItem().getDefaultInstance()).toList());
        registration.addRecipeCatalysts(JEI_DECAY_GENERATOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(DECAY_GENERATOR.toStack()));
        registration.addRecipeCatalysts(JEI_RADIATION_SCRUBBER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(RADIATION_SCRUBBER.toStack()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(JEI_ALLOY_FURNACE_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(ALLOY_FURNACE_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_ASSEMBLER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(ASSEMBLER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_CENTRIFUGE_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(CENTRIFUGE_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_CHEMICAL_REACTOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(CHEMICAL_REACTOR_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_CRYSTALLIZER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(CRYSTALLIZER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_DECAY_HASTENER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(DECAY_HASTENER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_ELECTRIC_FURNACE_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(ELECTRIC_FURNACE_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_ELECTROLYZER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(ELECTROLYZER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_FLUID_ENRICHER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FLUID_ENRICHER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_FLUID_EXTRACTOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FLUID_EXTRACTOR_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_FLUID_INFUSER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FLUID_INFUSER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_FLUID_MIXER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FLUID_MIXER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_FUEL_REPROCESSOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FUEL_REPROCESSOR_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_INGOT_FORMER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(INGOT_FORMER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_MANUFACTORY_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(MANUFACTORY_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_MELTER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(MELTER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_PRESSURIZER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(PRESSURIZER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_ROCK_CRUSHER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(ROCK_CRUSHER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_SEPARATOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(SEPARATOR_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_SUPERCOOLER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(SUPERCOOLER_RECIPE_TYPE.get()).stream().toList());

        registration.addRecipes(JEI_ELECTRIC_FURNACE_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(RecipeType.SMELTING).stream().map(recipe -> new RecipeHolder<>(recipe.id(), new ElectricFurnaceRecipe(recipe.value().getIngredients().stream().map(e -> new SizedChanceItemIngredient(e, 1)).toList(), List.of(SizedChanceItemIngredient.of(recipe.value().getResultItem(null))), List.of(), List.of(), 1, 1, 0))).toList());

        for (String name : PROCESSOR_RECIPE_DYN_TYPES.keySet()) {
            registration.addRecipes(JEI_PROCESSOR_CATEGORIES_DYN.get(name).getRecipeType(), recipeManager.getAllRecipesFor(PROCESSOR_RECIPE_DYN_TYPES.get(name).get()).stream().toList());
        }

        registration.addRecipes(JEI_COLLECTOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(COLLECTOR_RECIPE_TYPE.get()).stream().sorted(Comparator.comparing(r -> r.value().getTypeName())).toList());
        registration.addRecipes(JEI_TURBINE_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(TURBINE_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_SOLID_FISSION_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(SOLID_FISSION_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_PEBBLE_FISSION_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(PEBBLE_FISSION_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_SALT_FISSION_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(SALT_FISSION_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_IRRADIATOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FISSION_IRRADIATOR_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_VENT_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FISSION_HEATING_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_EMERGENCY_COOLING_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FISSION_EMERGENCY_COOLING_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_SALT_COOLING_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(COOLANT_HEATER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_GAS_COOLING_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(COOLER_RECIPE_TYPE.get()).stream().toList());
        createItemDataMapCategory(registration, JEI_MODERATOR_CATEGORY, FISSION_MODERATOR_DATA);
        createItemDataMapCategory(registration, JEI_REFLECTOR_CATEGORY, FISSION_REFLECTOR_DATA);

        registration.addRecipes(JEI_HEAT_EXCHANGER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(HEAT_EXCHANGER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_CONDENSER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(CONDENSER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_CONDENSER_DISSIPATION_CATEGORY.getRecipeType(), getCondenserDissipationFluids().stream().map(e -> JeiBasicInfoRecipe.create(registration.getIngredientManager(), e, NeoForgeTypes.FLUID_STACK, CONDENSER_DISSIPATION_TOOLTIP)).toList());
        registration.addRecipes(JEI_MULTIBLOCK_DISTILLER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(MULTIBLOCK_DISTILLER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_MULTIBLOCK_ELECTROLYZER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_MULTIBLOCK_INFILTRATOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(MULTIBLOCK_INFILTRATOR_RECIPE_TYPE.get()).stream().toList());
        createItemDataMapCategory(registration, JEI_ELECTROLYZER_CATHODE_CATEGORY, ELECTROLYZER_CATHODE_DATA);
        createItemDataMapCategory(registration, JEI_ELECTROLYZER_ANODE_CATEGORY, ELECTROLYZER_ANODE_DATA);
        createFluidDataMapCategory(registration, JEI_INFILTRATOR_PRESSURE_CATEGORY, INFILTRATOR_PRESSURE_DATA, INFILTRATOR_PRESSURE_FLUID_TOOLTIP);
        createItemDataMapCategory(registration, JEI_DIAPHRAGM_CATEGORY, MACHINE_DIAPHRAGM_DATA);
        createItemDataMapCategory(registration, JEI_SIEVE_ASSEMBLY_CATEGORY, MACHINE_SIEVE_ASSEMBLY_DATA);

        registration.addRecipes(JEI_DECAY_GENERATOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(DECAY_GENERATOR_RECIPE_TYPE.get()).stream().toList());
        registration.addRecipes(JEI_RADIATION_SCRUBBER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(RADIATION_SCRUBBER_RECIPE_TYPE.get()).stream().toList());
    }

//    @Override TODO
//    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
//        for (var recipe_category : RECIPE_VIEWER_CATEGORY_INFO_MAP.entrySet()){
//            registration.addRecipeClickArea(GuiCrafting.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
//        }
//    }

    private <R> void createItemDataMapCategory(IRecipeRegistration registration, IRecipeCategory<IJeiBasicInfoRecipe> category, DataMapType<Block, R> dataMapType) {
        createItemDataMapCategory(registration, category, dataMapType, null);
    }

    private <R> void createItemDataMapCategory(IRecipeRegistration registration, IRecipeCategory<IJeiBasicInfoRecipe> category, DataMapType<Block, R> dataMapType, Function<ItemStack, Component> tooltip) {
        addDataMapRecipes(registration, category, BuiltInRegistries.BLOCK.getDataMap(dataMapType).entrySet(), e -> Objects.requireNonNull(BuiltInRegistries.BLOCK.get(e.getKey())).asItem().getDefaultInstance(), VanillaTypes.ITEM_STACK, tooltip);
    }

    private <R> void createFluidDataMapCategory(IRecipeRegistration registration, IRecipeCategory<IJeiBasicInfoRecipe> category, DataMapType<Fluid, R> dataMapType) {
        createFluidDataMapCategory(registration, category, dataMapType, null);
    }

    private <R> void createFluidDataMapCategory(IRecipeRegistration registration, IRecipeCategory<IJeiBasicInfoRecipe> category, DataMapType<Fluid, R> dataMapType, Function<FluidStack, Component> tooltip) {
        addDataMapRecipes(registration, category, BuiltInRegistries.FLUID.getDataMap(dataMapType).entrySet(), e -> new FluidStack(Objects.requireNonNull(BuiltInRegistries.FLUID.get(e.getKey())), 1000), NeoForgeTypes.FLUID_STACK, tooltip);
    }

    private <T, R, S> void addDataMapRecipes(IRecipeRegistration registration, IRecipeCategory<IJeiBasicInfoRecipe> category, Set<Map.Entry<ResourceKey<T>, R>> entrySet, Function<Map.Entry<ResourceKey<T>, R>, S> toStack, IIngredientType<S> type, Function<S, Component> tooltip) {
        registration.addRecipes(category.getRecipeType(), entrySet.stream().map(e -> JeiBasicInfoRecipe.create(registration.getIngredientManager(), toStack.apply(e), type, tooltip)).toList());
    }
}