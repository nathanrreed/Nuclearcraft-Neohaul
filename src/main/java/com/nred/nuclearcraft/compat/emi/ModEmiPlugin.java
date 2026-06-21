package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.EmiRecipeViewerImpl.*;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.ElectricFurnaceRecipe;
import com.nred.nuclearcraft.recipe.processor.ProcessorRecipeDyn;
import com.nred.nuclearcraft.util.StreamHelper;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.CONDENSER_DISSIPATION_TOOLTIP;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.INFILTRATOR_PRESSURE_FLUID_TOOLTIP;
import static com.nred.nuclearcraft.helpers.Concat.fluidEntries;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.Names.PROCESSORS;
import static com.nred.nuclearcraft.multiblock.hx.CondenserLogic.getCondenserDissipationFluids;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    private static final Map<String, EmiStack> COLLECTOR_WORKSTATIONS = COLLECTOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> EmiStack.of(COLLECTOR_MAP.get(type))));
    public static final EmiRecipeCategory EMI_COLLECTOR_CATEGORY = new EmiRecipeCategory(ncLoc("collector"), COLLECTOR_WORKSTATIONS.get("cobblestone_generator"));

    private static final EmiStack TURBINE_WORKSTATION = EmiStack.of(TURBINE_MAP.get("turbine_controller"));
    public static final EmiRecipeCategory EMI_TURBINE_CATEGORY = new EmiRecipeCategory(ncLoc("turbine"), TURBINE_WORKSTATION);

    private static final EmiStack SOLID_FISSION_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller"));
    public static final EmiRecipeCategory EMI_SOLID_FISSION_CATEGORY = new EmiRecipeCategory(ncLoc("solid_fission"), SOLID_FISSION_WORKSTATION);

    private static final EmiStack PEBBLE_FISSION_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("pebble_bed_fission_controller"));
    public static final EmiRecipeCategory EMI_PEBBLE_FISSION_CATEGORY = new EmiRecipeCategory(ncLoc("pebble_fission"), PEBBLE_FISSION_WORKSTATION);

    private static final EmiStack SALT_FISSION_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("molten_salt_fission_controller"));
    public static final EmiRecipeCategory EMI_SALT_FISSION_CATEGORY = new EmiRecipeCategory(ncLoc("salt_fission"), SALT_FISSION_WORKSTATION);

    private static final EmiStack IRRADIATOR_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("fission_irradiator"));
    public static final EmiRecipeCategory EMI_IRRADIATOR_CATEGORY = new EmiRecipeCategory(ncLoc("fission_irradiator"), IRRADIATOR_WORKSTATION);

    private static final EmiStack VENT_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("fission_vent"));
    public static final EmiRecipeCategory EMI_VENT_CATEGORY = new EmiRecipeCategory(ncLoc("fission_heating"), VENT_WORKSTATION);

    private static final EmiStack EMERGENCY_COOLING_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("fission_vent"));
    public static final EmiRecipeCategory EMI_EMERGENCY_COOLING_CATEGORY = new EmiRecipeCategory(ncLoc("fission_emergency_cooling"), EMERGENCY_COOLING_WORKSTATION);

    private static final EmiStack SALT_COOLING_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"));
    public static final EmiRecipeCategory EMI_SALT_COOLING_CATEGORY = new EmiRecipeCategory(ncLoc("salt_cooling"), SALT_COOLING_WORKSTATION);

    private static final EmiStack PEBBLE_COOLER_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("oxygen_fission_gas_cooler"));
    public static final EmiRecipeCategory EMI_PEBBLE_COOLER_CATEGORY = new EmiRecipeCategory(ncLoc("gas_cooling"), PEBBLE_COOLER_WORKSTATION);

    private static final EmiStack HEAT_EXCHANGER_WORKSTATION = EmiStack.of(HX_MAP.get("heat_exchanger_controller"));
    public static final EmiRecipeCategory EMI_HEAT_EXCHANGER_CATEGORY = new EmiRecipeCategory(ncLoc("heat_exchanger"), HEAT_EXCHANGER_WORKSTATION);

    private static final EmiStack CONDENSER_WORKSTATION = EmiStack.of(HX_MAP.get("condenser_controller"));
    public static final EmiRecipeCategory EMI_CONDENSER_CATEGORY = new EmiRecipeCategory(ncLoc("condenser"), CONDENSER_WORKSTATION);

    private static final EmiStack CONDENSER_DISSIPATION_WORKSTATION = EmiStack.of(HX_MAP.get("heat_exchanger_inlet"));
    public static final EmiRecipeCategory EMI_CONDENSER_DISSIPATION_CATEGORY = new EmiRecipeCategory(ncLoc("condenser_dissipation"), CONDENSER_DISSIPATION_WORKSTATION);

    private static final EmiStack MULTIBLOCK_DISTILLER_WORKSTATION = EmiStack.of(MACHINE_MAP.get("distiller_controller"));
    public static final EmiRecipeCategory EMI_MULTIBLOCK_DISTILLER_CATEGORY = new EmiRecipeCategory(ncLoc("multiblock_distiller"), MULTIBLOCK_DISTILLER_WORKSTATION);

    private static final EmiStack MULTIBLOCK_ELECTROLYZER_WORKSTATION = EmiStack.of(MACHINE_MAP.get("electrolyzer_controller"));
    public static final EmiRecipeCategory EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY = new EmiRecipeCategory(ncLoc("multiblock_electrolyzer"), MULTIBLOCK_ELECTROLYZER_WORKSTATION);

    private static final EmiStack MULTIBLOCK_INFILTRATOR_WORKSTATION = EmiStack.of(MACHINE_MAP.get("infiltrator_controller"));
    public static final EmiRecipeCategory EMI_MULTIBLOCK_INFILTRATOR_CATEGORY = new EmiRecipeCategory(ncLoc("multiblock_infiltrator"), MULTIBLOCK_INFILTRATOR_WORKSTATION);

    private static final EmiStack MULTIBLOCK_DECAY_POOL_WORKSTATION = EmiStack.of(MACHINE_MAP.get("decay_pool_controller"));
    public static final EmiRecipeCategory EMI_MULTIBLOCK_DECAY_POOL_CATEGORY = new EmiRecipeCategory(ncLoc("multiblock_decay_pool"), MULTIBLOCK_DECAY_POOL_WORKSTATION);

    private static final EmiStack DECAY_POOL_HEAT_SOURCE_WORKSTATION = EmiStack.of(MACHINE_MAP.get("decay_pool_container"));
    public static final EmiRecipeCategory EMI_DECAY_POOL_HEAT_SOURCE_CATEGORY = new EmiRecipeCategory(ncLoc("decay_pool_heat_source"), DECAY_POOL_HEAT_SOURCE_WORKSTATION);

    private static final EmiStack DECAY_GENERATOR_WORKSTATION = EmiStack.of(DECAY_GENERATOR);
    public static final EmiRecipeCategory EMI_DECAY_GENERATOR_CATEGORY = new EmiRecipeCategory(ncLoc("decay_generator"), DECAY_GENERATOR_WORKSTATION);

    private static final EmiStack RADIATION_SCRUBBER_WORKSTATION = EmiStack.of(RADIATION_SCRUBBER);
    public static final EmiRecipeCategory EMI_RADIATION_SCRUBBER_CATEGORY = new EmiRecipeCategory(ncLoc("radiation_scrubber"), RADIATION_SCRUBBER_WORKSTATION);

    private static final Map<String, EmiStack> PROCESSOR_WORKSTATIONS = PROCESSORS.stream().collect(Collectors.toMap(Function.identity(), type -> EmiStack.of(PROCESSOR_MAP.get(type))));
    public static final Map<String, EmiRecipeCategory> EMI_PROCESSOR_CATEGORIES = PROCESSORS.stream().collect(Collectors.toMap(Function.identity(), type -> new EmiRecipeCategory(ncLoc(type), PROCESSOR_WORKSTATIONS.get(type))));
    public static final HashMap<String, EmiRecipeCategory> EMI_PROCESSOR_CATEGORIES_DYN = new HashMap<>();

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();

        for (Map.Entry<String, Fluids> entry : fluidEntries(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FUEL_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, SOUL_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            registry.addEmiStack(EmiStack.of(entry.getValue().bucket));
        }

        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, PROCESSOR_WORKSTATIONS.get("electric_furnace"));
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, EmiStack.of(NUCLEAR_FURNACE));

        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("alloy_furnace"), PROCESSOR_WORKSTATIONS.get("alloy_furnace"), ALLOY_FURNACE_RECIPE_TYPE.get(), EmiAlloyFurnaceRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("assembler"), PROCESSOR_WORKSTATIONS.get("assembler"), ASSEMBLER_RECIPE_TYPE.get(), EmiAssemblerRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("centrifuge"), PROCESSOR_WORKSTATIONS.get("centrifuge"), CENTRIFUGE_RECIPE_TYPE.get(), EmiCentrifugeRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("chemical_reactor"), PROCESSOR_WORKSTATIONS.get("chemical_reactor"), CHEMICAL_REACTOR_RECIPE_TYPE.get(), EmiChemicalReactorRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("crystallizer"), PROCESSOR_WORKSTATIONS.get("crystallizer"), CRYSTALLIZER_RECIPE_TYPE.get(), EmiCrystallizerRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("decay_hastener"), PROCESSOR_WORKSTATIONS.get("decay_hastener"), DECAY_HASTENER_RECIPE_TYPE.get(), EmiDecayHastenerRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("electrolyzer"), PROCESSOR_WORKSTATIONS.get("electrolyzer"), ELECTROLYZER_RECIPE_TYPE.get(), EmiElectrolyzerRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("fluid_enricher"), PROCESSOR_WORKSTATIONS.get("fluid_enricher"), FLUID_ENRICHER_RECIPE_TYPE.get(), EmiFluidEnricherRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("fluid_extractor"), PROCESSOR_WORKSTATIONS.get("fluid_extractor"), FLUID_EXTRACTOR_RECIPE_TYPE.get(), EmiFluidExtractorRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("fluid_infuser"), PROCESSOR_WORKSTATIONS.get("fluid_infuser"), FLUID_INFUSER_RECIPE_TYPE.get(), EmiFluidInfuserRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("fluid_mixer"), PROCESSOR_WORKSTATIONS.get("fluid_mixer"), FLUID_MIXER_RECIPE_TYPE.get(), EmiFluidMixerRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("fuel_reprocessor"), PROCESSOR_WORKSTATIONS.get("fuel_reprocessor"), FUEL_REPROCESSOR_RECIPE_TYPE.get(), EmiFuelReprocessorRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("ingot_former"), PROCESSOR_WORKSTATIONS.get("ingot_former"), INGOT_FORMER_RECIPE_TYPE.get(), EmiIngotFormerRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("manufactory"), PROCESSOR_WORKSTATIONS.get("manufactory"), MANUFACTORY_RECIPE_TYPE.get(), EmiManufactoryRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("melter"), PROCESSOR_WORKSTATIONS.get("melter"), MELTER_RECIPE_TYPE.get(), EmiMelterRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("pressurizer"), PROCESSOR_WORKSTATIONS.get("pressurizer"), PRESSURIZER_RECIPE_TYPE.get(), EmiPressurizerRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("rock_crusher"), PROCESSOR_WORKSTATIONS.get("rock_crusher"), ROCK_CRUSHER_RECIPE_TYPE.get(), EmiRockCrusherRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("separator"), PROCESSOR_WORKSTATIONS.get("separator"), SEPARATOR_RECIPE_TYPE.get(), EmiSeparatorRecipe::new);
        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("supercooler"), PROCESSOR_WORKSTATIONS.get("supercooler"), SUPERCOOLER_RECIPE_TYPE.get(), EmiSupercoolerRecipe::new);

        addCategory(registry, manager, EMI_PROCESSOR_CATEGORIES.get("electric_furnace"), PROCESSOR_WORKSTATIONS.get("electric_furnace"), ELECTRIC_FURNACE_RECIPE_TYPE.get(), EmiElectricFurnaceRecipe::new);
        for (RecipeHolder<SmeltingRecipe> recipe : manager.getAllRecipesFor(RecipeType.SMELTING).stream().sorted(Comparator.comparing(other -> other.id().getPath())).toList()) { // Take from vanilla
            registry.addRecipe(new EmiElectricFurnaceRecipe(recipe.id(), new ElectricFurnaceRecipe(recipe.value().getIngredients().stream().map(e -> new SizedChanceItemIngredient(e, 1)).toList(), List.of(SizedChanceItemIngredient.of(recipe.value().getResultItem(null))), List.of(), List.of(), 1, 1, 0)));
        }

        for (String name : PROCESSOR_RECIPE_DYN_TYPES.keySet()) {
            EMI_PROCESSOR_CATEGORIES_DYN.put(name, new EmiRecipeCategory(ResourceLocation.parse(name), EmiStack.of(PROCESSOR_MAP.get(name))));
            registry.addCategory(EMI_PROCESSOR_CATEGORIES_DYN.get(name));
            registry.addWorkstation(EMI_PROCESSOR_CATEGORIES_DYN.get(name), EmiStack.of(PROCESSOR_MAP.get(name)));
            for (RecipeHolder<ProcessorRecipeDyn> recipe : manager.getAllRecipesFor(PROCESSOR_RECIPE_DYN_TYPES.get(name).get())) {
                registry.addRecipe(new EmiProcessorRecipeDyn(recipe.id(), recipe.value(), name));
            }
        }

        addCategory(registry, manager, EMI_RADIATION_SCRUBBER_CATEGORY, RADIATION_SCRUBBER_WORKSTATION, RADIATION_SCRUBBER_RECIPE_TYPE.get(), EmiRadiationScrubberRecipe::new);
        addCategory(registry, manager, EMI_COLLECTOR_CATEGORY, COLLECTOR_WORKSTATIONS.values(), COLLECTOR_RECIPE_TYPE.get(), EmiCollectorRecipe::new);
        addCategory(registry, manager, EMI_DECAY_GENERATOR_CATEGORY, DECAY_GENERATOR_WORKSTATION, DECAY_GENERATOR_RECIPE_TYPE.get(), EmiDecayGeneratorRecipe::new);
        addCategory(registry, manager, EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY, MULTIBLOCK_ELECTROLYZER_WORKSTATION, MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE.get(), EmiMultiblockElectrolyzerRecipe::new);
        addCategory(registry, manager, EMI_MULTIBLOCK_DISTILLER_CATEGORY, MULTIBLOCK_DISTILLER_WORKSTATION, MULTIBLOCK_DISTILLER_RECIPE_TYPE.get(), EmiMultiblockDistillerRecipe::new);
        addCategory(registry, manager, EMI_MULTIBLOCK_INFILTRATOR_CATEGORY, MULTIBLOCK_INFILTRATOR_WORKSTATION, MULTIBLOCK_INFILTRATOR_RECIPE_TYPE.get(), EmiMultiblockInfiltratorRecipe::new);
        addCategory(registry, manager, EMI_MULTIBLOCK_DECAY_POOL_CATEGORY, MULTIBLOCK_DECAY_POOL_WORKSTATION, MULTIBLOCK_DECAY_POOL_RECIPE_TYPE.get(), EmiMultiblockDecayPoolRecipe::new);
        addCategory(registry, manager, EMI_DECAY_POOL_HEAT_SOURCE_CATEGORY, DECAY_POOL_HEAT_SOURCE_WORKSTATION, DECAY_POOL_HEAT_SOURCE_RECIPE_TYPE.get(), EmiDecayPoolHeatSourceRecipe::new);

        registry.addCategory(EMI_CONDENSER_DISSIPATION_CATEGORY);
        registry.addWorkstation(EMI_CONDENSER_DISSIPATION_CATEGORY, CONDENSER_DISSIPATION_WORKSTATION);
        for (FluidStack fluidStack : getCondenserDissipationFluids()) {
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(EmiStack.of(fluidStack.getFluid())), EMI_CONDENSER_DISSIPATION_CATEGORY, ncLoc("/condenser_dissipation/" + fluidStack.getDescriptionId()), () -> ClientTooltipComponent.create(CONDENSER_DISSIPATION_TOOLTIP.apply(fluidStack).getVisualOrderText())));
        }


        addCategory(registry, manager, EMI_IRRADIATOR_CATEGORY, IRRADIATOR_WORKSTATION, FISSION_IRRADIATOR_RECIPE_TYPE.get(), EmiFissionIrradiatorRecipe::new);
        addCategory(registry, manager, EMI_SOLID_FISSION_CATEGORY, List.of(SOLID_FISSION_WORKSTATION, EmiStack.of(FISSION_REACTOR_MAP.get("fission_fuel_cell"))), SOLID_FISSION_RECIPE_TYPE.get(), EmiSolidFissionRecipe::new);
        addCategory(registry, manager, EMI_SALT_FISSION_CATEGORY, List.of(SALT_FISSION_WORKSTATION, EmiStack.of(FISSION_REACTOR_MAP.get("fission_fuel_vessel"))), SALT_FISSION_RECIPE_TYPE.get(), EmiSaltFissionRecipe::new);
        addCategory(registry, manager, EMI_PEBBLE_FISSION_CATEGORY, List.of(PEBBLE_FISSION_WORKSTATION, EmiStack.of(FISSION_REACTOR_MAP.get("fission_fuel_chamber"))), PEBBLE_FISSION_RECIPE_TYPE.get(), EmiPebbleFissionRecipe::new);
        addCategory(registry, manager, EMI_VENT_CATEGORY, VENT_WORKSTATION, FISSION_HEATING_RECIPE_TYPE.get(), EmiFissionVentRecipe::new);
        addCategory(registry, manager, EMI_SALT_COOLING_CATEGORY, SALT_COOLING_WORKSTATION, COOLANT_HEATER_RECIPE_TYPE.get(), EmiSaltCoolingRecipe::new);
        addCategory(registry, manager, EMI_PEBBLE_COOLER_CATEGORY, PEBBLE_COOLER_WORKSTATION, COOLER_RECIPE_TYPE.get(), EmiGasCoolingRecipe::new);
        addCategory(registry, manager, EMI_EMERGENCY_COOLING_CATEGORY, EMERGENCY_COOLING_WORKSTATION, FISSION_EMERGENCY_COOLING_RECIPE_TYPE.get(), EmiFissionEmergencyCoolingRecipe::new);
        addCategory(registry, manager, EMI_HEAT_EXCHANGER_CATEGORY, Stream.concat(Stream.of(HEAT_EXCHANGER_WORKSTATION), HX_ENTITY_TYPE.get("tube").get().getValidBlocks().stream().map(EmiStack::of)).toList(), HEAT_EXCHANGER_RECIPE_TYPE.get(), EmiHeatExchangerRecipe::new);
        addCategory(registry, manager, EMI_CONDENSER_CATEGORY, Stream.concat(Stream.of(CONDENSER_WORKSTATION), HX_ENTITY_TYPE.get("tube").get().getValidBlocks().stream().map(EmiStack::of)).toList(), CONDENSER_RECIPE_TYPE.get(), EmiCondenserRecipe::new);
        addCategory(registry, manager, EMI_TURBINE_CATEGORY, StreamHelper.concatToList(Stream.of(TURBINE_MAP.get("turbine_controller")), TURBINE_ENTITY_TYPE.get("rotor_stator").get().getValidBlocks().stream(), TURBINE_ENTITY_TYPE.get("rotor_blade").get().getValidBlocks().stream()).stream().map(EmiStack::of).toList(), TURBINE_RECIPE_TYPE.get(), EmiTurbineRecipe::new);

        createBlockDataMapCategory(registry, MACHINE_DIAPHRAGM_DATA, "machine_diaphragm", MACHINE_MAP.get("sintered_steel_diaphragm"));
        createBlockDataMapCategory(registry, MACHINE_SIEVE_ASSEMBLY_DATA, "machine_sieve_assembly", MACHINE_MAP.get("steel_sieve_assembly"));
        createBlockDataMapCategory(registry, ELECTROLYZER_CATHODE_DATA, "electrolyzer_cathode", MACHINE_MAP.get("electrolyzer_cathode_terminal"));
        createBlockDataMapCategory(registry, ELECTROLYZER_ANODE_DATA, "electrolyzer_anode", MACHINE_MAP.get("electrolyzer_anode_terminal"));
        createBlockDataMapCategory(registry, FISSION_REFLECTOR_DATA, "fission_reflector", FISSION_REACTOR_MAP.get("beryllium_carbon_reflector"));
        createBlockDataMapCategory(registry, FISSION_MODERATOR_DATA, "fission_moderator", HEAVY_WATER_MODERATOR);
        createFluidDataMapCategory(registry, INFILTRATOR_PRESSURE_DATA, "infiltrator_pressure", MACHINE_MAP.get("infiltrator_pressure_chamber"), INFILTRATOR_PRESSURE_FLUID_TOOLTIP);

        registry.addRecipe(EmiWorldInteractionRecipe.builder().rightInput(EmiStack.of(CUSTOM_FLUID_MAP.get("corium").still.get(), 1000), false).leftInput(EmiStack.EMPTY).output(EmiStack.of(SOLIDIFIED_CORIUM.get())).build());
    }

    public static EmiIngredient getEmiFluidIngredient(SizedChanceFluidIngredient fluidIngredient) {
        return NeoForgeEmiIngredient.of(fluidIngredient.sized());
    }

    public static EmiIngredient getEmiItemIngredient(SizedChanceItemIngredient itemIngredient) {
        return NeoForgeEmiIngredient.of(itemIngredient.sized());
    }

    public static EmiStack getEmiFluidStack(SizedChanceFluidIngredient fluidIngredient) {
        return EmiStack.of(fluidIngredient.getStack().getFluid(), fluidIngredient.amount());
    }

    public static EmiStack getEmiItemStack(SizedChanceItemIngredient itemIngredient) {
        return EmiStack.of(itemIngredient.getStack());
    }

    private void addWorkstations(EmiRegistry registry, EmiRecipeCategory category, List<EmiStack> stacks) {
        stacks.forEach(e -> registry.addWorkstation(category, e));
    }

    private <R> void createBlockDataMapCategory(EmiRegistry registry, DataMapType<Block, R> dataMapType, String name, ItemLike icon) {
        createBlockDataMapCategory(registry, dataMapType, name, icon, null);
    }

    private <R> void createBlockDataMapCategory(EmiRegistry registry, DataMapType<Block, R> dataMapType, String name, ItemLike defaultIcon, Function<ItemStack, Component> tooltip) {
        createDataMapCategory(registry, BuiltInRegistries.BLOCK.getDataMap(dataMapType).entrySet(), name, EmiStack.of(defaultIcon), List.of(), (e) -> EmiStack.of(Objects.requireNonNull(BuiltInRegistries.BLOCK.get(e.getKey()))), tooltip);
    }

    private <R> void createFluidDataMapCategory(EmiRegistry registry, DataMapType<Fluid, R> dataMapType, String name, ItemLike icon) {
        createFluidDataMapCategory(registry, dataMapType, name, icon, null);
    }

    private <R> void createFluidDataMapCategory(EmiRegistry registry, DataMapType<Fluid, R> dataMapType, String name, ItemLike icon, Function<FluidStack, Component> tooltip) {
        createDataMapCategory(registry, BuiltInRegistries.FLUID.getDataMap(dataMapType).entrySet(), name, EmiStack.of(icon), List.of(EmiStack.of(icon)), (e) -> EmiStack.of(Objects.requireNonNull(BuiltInRegistries.FLUID.get(e.getKey()))), tooltip);
    }

    private <T, R, S> void createDataMapCategory(EmiRegistry registry, Set<Map.Entry<ResourceKey<T>, R>> entrySet, String name, EmiStack icon, List<EmiStack> workStations, Function<Map.Entry<ResourceKey<T>, R>, EmiStack> toEmiStack, Function<S, Component> tooltip) {
        if (entrySet.isEmpty()) return;
        EmiRecipeCategory category = new EmiRecipeCategory(ncLoc(name), icon);
        registry.addCategory(category);

        addWorkstations(registry, category, workStations);
        for (Map.Entry<ResourceKey<T>, R> entry : entrySet) {
            EmiStack stack = toEmiStack.apply(entry);
            S key = (S) (stack.getKey() instanceof Fluid fluid ? new FluidStack(fluid, 1000) : stack.getKey());
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(stack), category, ncLoc("/" + name + "/" + stack.getId().toString().replace(':', '_')), tooltip == null ? null : () -> ClientTooltipComponent.create(tooltip.apply(key).getVisualOrderText())));

            if (workStations.isEmpty()) {
                registry.addWorkstation(category, stack);
            }
        }
    }

    private static <T extends BasicRecipe> void addCategory(EmiRegistry registry, RecipeManager manager, EmiRecipeCategory category, EmiStack workstation, RecipeType<T> recipeType, BiFunction<ResourceLocation, T, EmiRecipe> emiRecipeFunc) {
        addCategory(registry, manager, category, List.of(workstation), recipeType, emiRecipeFunc);
    }

    private static <T extends BasicRecipe> void addCategory(EmiRegistry registry, RecipeManager manager, EmiRecipeCategory category, Collection<EmiStack> workstations, RecipeType<T> recipeType, BiFunction<ResourceLocation, T, EmiRecipe> emiRecipeFunc) {
        registry.addCategory(category);
        for (EmiStack workstation : workstations) {
            registry.addWorkstation(category, workstation);
        }
        for (RecipeHolder<T> recipe : manager.getAllRecipesFor(recipeType)) {
            registry.addRecipe(emiRecipeFunc.apply(recipe.id(), recipe.value()));
        }
    }

    private record SimpleRecipeHandler<T extends AbstractContainerMenu>(EmiRecipeCategory category) implements StandardRecipeHandler<T> {
        @Override
        public List<Slot> getInputSources(T handler) {
            return handler.slots;
        }

        @Override
        public List<Slot> getCraftingSlots(T handler) {
            return List.of();
        }

        @Override
        public boolean supportsRecipe(EmiRecipe recipe) {
            return (recipe.getCategory() == category);
        }
    }
}