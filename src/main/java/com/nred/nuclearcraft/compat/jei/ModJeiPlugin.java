package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.recipe.CollectorRecipe;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
import com.nred.nuclearcraft.recipe.processor.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
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
import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.multiblock.hx.CondenserLogic.getCondenserDissipationFluids;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.*;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    public static Map<String, IRecipeCategory<? extends ProcessorRecipe>> JEI_PROCESSOR_CATEGORIES = Map.of();
    public static IRecipeCategory<CollectorRecipe> JEI_COLLECTOR_CATEGORY;
    public static IRecipeCategory<TurbineRecipe> JEI_TURBINE_CATEGORY;
    public static IRecipeCategory<SolidFissionRecipe> JEI_SOLID_FISSION_CATEGORY;
    public static IRecipeCategory<SaltFissionRecipe> JEI_SALT_FISSION_CATEGORY;
    public static IRecipeCategory<FissionIrradiatorRecipe> JEI_IRRADIATOR_CATEGORY;
    public static IRecipeCategory<FissionHeatingRecipe> JEI_VENT_CATEGORY;
    public static IRecipeCategory<FissionEmergencyCoolingRecipe> JEI_EMERGENCY_COOLING_CATEGORY;
    public static IRecipeCategory<FissionCoolantHeaterRecipe> JEI_SALT_COOLING_CATEGORY;
    public static IRecipeCategory<HeatExchangerRecipe> JEI_HEAT_EXCHANGER_CATEGORY;
    public static IRecipeCategory<CondenserRecipe> JEI_CONDENSER_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_CONDENSER_DISSIPATION_CATEGORY;
    public static IRecipeCategory<MultiblockDistillerRecipe> JEI_MULTIBLOCK_DISTILLER_CATEGORY;
    public static IRecipeCategory<MultiblockElectrolyzerRecipe> JEI_MULTIBLOCK_ELECTROLYZER_CATEGORY;
    public static IRecipeCategory<MultiblockInfiltratorRecipe> JEI_MULTIBLOCK_INFILTRATOR_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_INFILTRATOR_PRESSURE_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_MODERATOR_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_REFLECTOR_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_ELECTROLYZER_CATHODE_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_ELECTROLYZER_ANODE_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_DIAPHRAGM_CATEGORY;
    public static IRecipeCategory<IJeiBasicInfoRecipe> JEI_SIEVE_ASSEMBLY_CATEGORY;
    public static IRecipeCategory<DecayGeneratorRecipe> JEI_DECAY_GENERATOR_CATEGORY;
    public static IRecipeCategory<RadiationScrubberRecipe> JEI_RADIATION_SCRUBBER_CATEGORY;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ncLoc("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        JEI_PROCESSOR_CATEGORIES = makeCategories(registration);
        for (IRecipeCategory<?> category : JEI_PROCESSOR_CATEGORIES.values()) {
            registration.addRecipeCategories(category);
        }

        JEI_COLLECTOR_CATEGORY = new JeiCollectorCategory(helper);
        registration.addRecipeCategories(JEI_COLLECTOR_CATEGORY);
        JEI_TURBINE_CATEGORY = new JeiTurbineCategory(helper);
        registration.addRecipeCategories(JEI_TURBINE_CATEGORY);
        JEI_SOLID_FISSION_CATEGORY = new JeiSolidFissionCategory(helper);
        registration.addRecipeCategories(JEI_SOLID_FISSION_CATEGORY);
        JEI_SALT_FISSION_CATEGORY = new JeiSaltFissionCategory(helper);
        registration.addRecipeCategories(JEI_SALT_FISSION_CATEGORY);
        JEI_IRRADIATOR_CATEGORY = new JeiFissionIrradiatorCategory(helper);
        registration.addRecipeCategories(JEI_IRRADIATOR_CATEGORY);
        JEI_VENT_CATEGORY = new JeiFissionVentCategory(helper);
        registration.addRecipeCategories(JEI_VENT_CATEGORY);
        JEI_EMERGENCY_COOLING_CATEGORY = new JeiFissionEmergencyCoolingCategory(helper);
        registration.addRecipeCategories(JEI_EMERGENCY_COOLING_CATEGORY);
        JEI_SALT_COOLING_CATEGORY = new JeiSaltCoolingCategory(helper);
        registration.addRecipeCategories(JEI_SALT_COOLING_CATEGORY);
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
        for (String type : JEI_PROCESSOR_CATEGORIES.keySet()) {
            registration.addRecipeCatalysts(JEI_PROCESSOR_CATEGORIES.get(type).getRecipeType(), PROCESSOR_MAP.get(type));
        }
        registration.addRecipeCatalysts(RecipeTypes.SMELTING, VanillaTypes.ITEM_STACK, List.of(PROCESSOR_MAP.get("electric_furnace").toStack(), NUCLEAR_FURNACE.toStack()));

        registration.addRecipeCatalysts(JEI_COLLECTOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, COLLECTOR_MAP.values().stream().map(DeferredBlock::toStack).toList());
        registration.addRecipeCatalysts(JEI_TURBINE_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(TURBINE_MAP.get("turbine_controller").toStack()));
        registration.addRecipeCatalysts(JEI_SOLID_FISSION_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller").toStack()));
        registration.addRecipeCatalysts(JEI_SALT_FISSION_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("molten_salt_fission_controller").toStack()));
        registration.addRecipeCatalysts(JEI_IRRADIATOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("fission_irradiator").toStack()));
        registration.addRecipeCatalysts(JEI_VENT_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("fission_vent").toStack()));
        registration.addRecipeCatalysts(JEI_EMERGENCY_COOLING_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("fission_vent").toStack()));
        registration.addRecipeCatalysts(JEI_SALT_COOLING_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, Stream.concat(COOLANTS.stream().map(part -> FISSION_REACTOR_MAP.get(part + "_fission_coolant_heater").toStack()), Stream.of(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller").toStack())).toList());
        registration.addRecipeCatalysts(JEI_MODERATOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(HEAVY_WATER_MODERATOR.toStack(), INGOT_BLOCK_MAP.get("beryllium").toStack(), INGOT_BLOCK_MAP.get("graphite").toStack()));
        registration.addRecipeCatalysts(JEI_REFLECTOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(FISSION_REACTOR_MAP.get("beryllium_carbon_reflector").toStack(), FISSION_REACTOR_MAP.get("lead_steel_reflector").toStack()));
        registration.addRecipeCatalysts(JEI_HEAT_EXCHANGER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(HX_MAP.get("heat_exchanger_controller").toStack(), HX_MAP.get("copper_heat_exchanger_tube").toStack(), HX_MAP.get("hard_carbon_heat_exchanger_tube").toStack(), HX_MAP.get("thermoconducting_alloy_heat_exchanger_tube").toStack()));
        registration.addRecipeCatalysts(JEI_CONDENSER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(HX_MAP.get("condenser_controller").toStack(), HX_MAP.get("copper_heat_exchanger_tube").toStack(), HX_MAP.get("hard_carbon_heat_exchanger_tube").toStack(), HX_MAP.get("thermoconducting_alloy_heat_exchanger_tube").toStack()));
        registration.addRecipeCatalysts(JEI_MULTIBLOCK_DISTILLER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("distiller_controller").toStack()));
        registration.addRecipeCatalysts(JEI_CONDENSER_DISSIPATION_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(HX_MAP.get("heat_exchanger_inlet").toStack()));
        registration.addRecipeCatalysts(JEI_MULTIBLOCK_ELECTROLYZER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("electrolyzer_controller").toStack()));
        registration.addRecipeCatalysts(JEI_MULTIBLOCK_INFILTRATOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("infiltrator_controller").toStack()));
        registration.addRecipeCatalysts(JEI_ELECTROLYZER_CATHODE_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("electrolyzer_cathode_terminal").toStack()));
        registration.addRecipeCatalysts(JEI_ELECTROLYZER_ANODE_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("electrolyzer_anode_terminal").toStack()));
        registration.addRecipeCatalysts(JEI_INFILTRATOR_PRESSURE_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("infiltrator_pressure_chamber").toStack()));
        registration.addRecipeCatalysts(JEI_DIAPHRAGM_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("sintered_steel_diaphragm").toStack(), MACHINE_MAP.get("polyethersulfone_diaphragm").toStack(), MACHINE_MAP.get("zirfon_diaphragm").toStack()));
        registration.addRecipeCatalysts(JEI_SIEVE_ASSEMBLY_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(MACHINE_MAP.get("steel_sieve_assembly").toStack(), MACHINE_MAP.get("polytetrafluoroethene_sieve_assembly").toStack(), MACHINE_MAP.get("hastelloy_sieve_assembly").toStack()));
        registration.addRecipeCatalysts(JEI_DECAY_GENERATOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(DECAY_GENERATOR.toStack()));
        registration.addRecipeCatalysts(JEI_RADIATION_SCRUBBER_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, List.of(RADIATION_SCRUBBER.toStack()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        for (String type : JEI_PROCESSOR_CATEGORIES.keySet()) {
            registration.addRecipes((RecipeType<ProcessorRecipe>) JEI_PROCESSOR_CATEGORIES.get(type).getRecipeType(), recipeManager.getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(type).get()).stream().map(RecipeHolder::value).toList());
        }

        List.of(
                NITROGEN_COLLECTOR_RECIPE_TYPE, NITROGEN_COLLECTOR_COMPACT_RECIPE_TYPE, NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE,
                WATER_SOURCE_RECIPE_TYPE, WATER_SOURCE_COMPACT_RECIPE_TYPE, WATER_SOURCE_DENSE_RECIPE_TYPE,
                COBBLE_GENERATOR_RECIPE_TYPE, COBBLE_GENERATOR_COMPACT_RECIPE_TYPE, COBBLE_GENERATOR_DENSE_RECIPE_TYPE
        ).forEach(i -> {
            registration.addRecipes(JEI_COLLECTOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(i.get()).stream().map(RecipeHolder::value).toList());
        });

        registration.addRecipes(JEI_TURBINE_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(TURBINE_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_SOLID_FISSION_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(SOLID_FISSION_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_SALT_FISSION_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(SALT_FISSION_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_IRRADIATOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FISSION_IRRADIATOR_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_VENT_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FISSION_HEATING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_EMERGENCY_COOLING_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(FISSION_EMERGENCY_COOLING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_SALT_COOLING_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(COOLANT_HEATER_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        createItemDataMapCategory(registration, JEI_MODERATOR_CATEGORY, FISSION_MODERATOR_DATA);
        createItemDataMapCategory(registration, JEI_REFLECTOR_CATEGORY, FISSION_REFLECTOR_DATA);

        registration.addRecipes(JEI_HEAT_EXCHANGER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(HEAT_EXCHANGER_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_CONDENSER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(CONDENSER_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_CONDENSER_DISSIPATION_CATEGORY.getRecipeType(), getCondenserDissipationFluids().stream().map(e -> JeiBasicInfoRecipe.create(registration.getIngredientManager(), e, NeoForgeTypes.FLUID_STACK, CONDENSER_DISSIPATION_TOOLTIP)).toList());
        registration.addRecipes(JEI_MULTIBLOCK_DISTILLER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(MULTIBLOCK_DISTILLER_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_MULTIBLOCK_ELECTROLYZER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_MULTIBLOCK_INFILTRATOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(MULTIBLOCK_INFILTRATOR_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        createItemDataMapCategory(registration, JEI_ELECTROLYZER_CATHODE_CATEGORY, ELECTROLYZER_CATHODE_DATA);
        createItemDataMapCategory(registration, JEI_ELECTROLYZER_ANODE_CATEGORY, ELECTROLYZER_ANODE_DATA);
        createFluidDataMapCategory(registration, JEI_INFILTRATOR_PRESSURE_CATEGORY, INFILTRATOR_PRESSURE_DATA, INFILTRATOR_PRESSURE_FLUID_TOOLTIP);
        createItemDataMapCategory(registration, JEI_DIAPHRAGM_CATEGORY, MACHINE_DIAPHRAGM_DATA);
        createItemDataMapCategory(registration, JEI_SIEVE_ASSEMBLY_CATEGORY, MACHINE_SIEVE_ASSEMBLY_DATA);

        registration.addRecipes(JEI_DECAY_GENERATOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(DECAY_GENERATOR_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(JEI_RADIATION_SCRUBBER_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(RADIATION_SCRUBBER_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
    }


    private <R> void createItemDataMapCategory(IRecipeRegistration registration, IRecipeCategory<IJeiBasicInfoRecipe> category, DataMapType<Item, R> dataMapType) {
        createItemDataMapCategory(registration, category, dataMapType, null);
    }

    private <R> void createItemDataMapCategory(IRecipeRegistration registration, IRecipeCategory<IJeiBasicInfoRecipe> category, DataMapType<Item, R> dataMapType, Function<ItemStack, Component> tooltip) {
        addDataMapRecipes(registration, category, BuiltInRegistries.ITEM.getDataMap(dataMapType).entrySet(), e -> Objects.requireNonNull(BuiltInRegistries.ITEM.get(e.getKey())).getDefaultInstance(), VanillaTypes.ITEM_STACK, tooltip);
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

    private static Map<String, IRecipeCategory<? extends ProcessorRecipe>> makeCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        Map<String, IRecipeCategory<? extends ProcessorRecipe>> map = new HashMap<>();
        map.put("alloy_furnace", new JeiProcessorCategory<>(helper, "alloy_furnace", AlloyFurnaceRecipe.class));
        map.put("assembler", new JeiProcessorCategory<>(helper, "assembler", AssemblerRecipe.class));
        map.put("centrifuge", new JeiProcessorCategory<>(helper, "centrifuge", CentrifugeRecipe.class));
        map.put("chemical_reactor", new JeiProcessorCategory<>(helper, "chemical_reactor", ChemicalReactorRecipe.class));
        map.put("crystallizer", new JeiProcessorCategory<>(helper, "crystallizer", CrystallizerRecipe.class));
        map.put("decay_hastener", new JeiProcessorCategory<>(helper, "decay_hastener", DecayHastenerRecipe.class));
        map.put("electric_furnace", new JeiProcessorCategory<>(helper, "electric_furnace", ElectricFurnaceRecipe.class));
        map.put("electrolyzer", new JeiProcessorCategory<>(helper, "electrolyzer", ElectrolyzerRecipe.class));
        map.put("fluid_enricher", new JeiProcessorCategory<>(helper, "fluid_enricher", FluidEnricherRecipe.class));
        map.put("fluid_extractor", new JeiProcessorCategory<>(helper, "fluid_extractor", FluidExtractorRecipe.class));
        map.put("fluid_infuser", new JeiProcessorCategory<>(helper, "fluid_infuser", FluidInfuserRecipe.class));
        map.put("fluid_mixer", new JeiProcessorCategory<>(helper, "fluid_mixer", FluidMixerRecipe.class));
        map.put("fuel_reprocessor", new JeiProcessorCategory<>(helper, "fuel_reprocessor", FuelReprocessorRecipe.class));
        map.put("ingot_former", new JeiProcessorCategory<>(helper, "ingot_former", IngotFormerRecipe.class));
        map.put("manufactory", new JeiProcessorCategory<>(helper, "manufactory", ManufactoryRecipe.class));
        map.put("melter", new JeiProcessorCategory<>(helper, "melter", MelterRecipe.class));
        map.put("pressurizer", new JeiProcessorCategory<>(helper, "pressurizer", PressurizerRecipe.class));
        map.put("rock_crusher", new JeiProcessorCategory<>(helper, "rock_crusher", RockCrusherRecipe.class));
        map.put("separator", new JeiProcessorCategory<>(helper, "separator", SeparatorRecipe.class));
        map.put("supercooler", new JeiProcessorCategory<>(helper, "supercooler", SupercoolerRecipe.class));
        return map;
    }
}