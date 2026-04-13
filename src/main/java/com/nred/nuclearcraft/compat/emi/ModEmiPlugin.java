package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.menu.processor.ProcessorMenu;
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
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
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
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.CONDENSER_DISSIPATION_TOOLTIP;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.INFILTRATOR_PRESSURE_FLUID_TOOLTIP;
import static com.nred.nuclearcraft.helpers.Concat.fluidEntries;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.multiblock.hx.CondenserLogic.getCondenserDissipationFluids;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.MenuRegistration.*;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    private static final Map<String, EmiStack> PROCESSOR_WORKSTATIONS = PROCESSOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> EmiStack.of(PROCESSOR_MAP.get(type))));
    public static final Map<String, EmiRecipeCategory> EMI_PROCESSOR_CATEGORIES = PROCESSOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> new EmiRecipeCategory(ncLoc(type), PROCESSOR_WORKSTATIONS.get(type))));

    private static final Map<String, EmiStack> COLLECTOR_WORKSTATIONS = COLLECTOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> EmiStack.of(COLLECTOR_MAP.get(type))));
    public static final EmiRecipeCategory EMI_COLLECTOR_CATEGORY = new EmiRecipeCategory(ncLoc("collector"), COLLECTOR_WORKSTATIONS.get("cobblestone_generator"));

    private static final EmiStack TURBINE_WORKSTATION = EmiStack.of(TURBINE_MAP.get("turbine_controller"));
    public static final EmiRecipeCategory EMI_TURBINE_CATEGORY = new EmiRecipeCategory(ncLoc("turbine"), TURBINE_WORKSTATION);

    private static final EmiStack SOLID_FISSION_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller"));
    public static final EmiRecipeCategory EMI_SOLID_FISSION_CATEGORY = new EmiRecipeCategory(ncLoc("solid_fission"), SOLID_FISSION_WORKSTATION);

    private static final EmiStack SALT_FISSION_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("molten_salt_fission_controller"));
    public static final EmiRecipeCategory EMI_SALT_FISSION_CATEGORY = new EmiRecipeCategory(ncLoc("salt_fission"), SALT_FISSION_WORKSTATION);

    private static final EmiStack IRRADIATOR_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("fission_irradiator"));
    public static final EmiRecipeCategory EMI_IRRADIATOR_CATEGORY = new EmiRecipeCategory(ncLoc("fission_irradiator"), IRRADIATOR_WORKSTATION);

    private static final EmiStack VENT_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("fission_vent"));
    public static final EmiRecipeCategory EMI_VENT_CATEGORY = new EmiRecipeCategory(ncLoc("fission_vent"), VENT_WORKSTATION);

    private static final EmiStack EMERGENCY_COOLING_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("fission_vent"));
    public static final EmiRecipeCategory EMI_EMERGENCY_COOLING_CATEGORY = new EmiRecipeCategory(ncLoc("fission_emergency_cooling"), EMERGENCY_COOLING_WORKSTATION);

    private static final EmiStack SALT_COOLING_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"));
    public static final EmiRecipeCategory EMI_SALT_COOLING_CATEGORY = new EmiRecipeCategory(ncLoc("salt_cooling"), SALT_COOLING_WORKSTATION);

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

    private static final EmiStack DECAY_GENERATOR_WORKSTATION = EmiStack.of(DECAY_GENERATOR);
    public static final EmiRecipeCategory EMI_DECAY_GENERATOR_CATEGORY = new EmiRecipeCategory(ncLoc("decay_generator"), DECAY_GENERATOR_WORKSTATION);

    private static final EmiStack RADIATION_SCRUBBER_WORKSTATION = EmiStack.of(RADIATION_SCRUBBER);
    public static final EmiRecipeCategory EMI_RADIATION_SCRUBBER_CATEGORY = new EmiRecipeCategory(ncLoc("radiation_scrubber"), RADIATION_SCRUBBER_WORKSTATION);

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();

        for (Map.Entry<String, Fluids> entry : fluidEntries(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FUEL_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            registry.addEmiStack(EmiStack.of(entry.getValue().bucket));
        }

        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, PROCESSOR_WORKSTATIONS.get("electric_furnace"));
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, EmiStack.of(NUCLEAR_FURNACE));
        for (String type : EMI_PROCESSOR_CATEGORIES.keySet()) {
            registry.addCategory(EMI_PROCESSOR_CATEGORIES.get(type));
            registry.addRecipeHandler((MenuType<? extends ProcessorMenu<?, ?, ?>>) PROCESSOR_MENU_TYPES.get(type).get(), new StandardRecipeHandler() {
                @Override
                public List<Slot> getInputSources(AbstractContainerMenu handler) {
                    return handler.slots;
                }

                @Override
                public List<Slot> getCraftingSlots(AbstractContainerMenu handler) {
                    return handler.slots;
                }

                @Override
                public boolean supportsRecipe(EmiRecipe recipe) {
                    return (recipe.getCategory() == EMI_PROCESSOR_CATEGORIES.get(type));
                }
            });

            registry.addWorkstation(EMI_PROCESSOR_CATEGORIES.get(type), PROCESSOR_WORKSTATIONS.get(type));

            if (type.equals("electric_furnace")) {
                for (RecipeHolder<SmeltingRecipe> recipe : manager.getAllRecipesFor(RecipeType.SMELTING).stream().sorted(Comparator.comparing(other -> other.id().getPath())).toList()) { // Take from vanilla
                    registry.addRecipe(new EmiProcessorRecipe(type, EMI_PROCESSOR_CATEGORIES.get(type), recipe.id(), recipe.value().getIngredients().stream().map(EmiIngredient::of).toList(), List.of(EmiIngredient.of(Ingredient.of(recipe.value().getResultItem(null)))), 1, 1));
                }
            } else {
                for (RecipeHolder<? extends ProcessorRecipe> recipe : manager.getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(type).get()).stream().sorted(Comparator.comparing(other -> other.id().getPath())).toList()) {
                    EmiProcessorRecipe temp = new EmiProcessorRecipe(type, EMI_PROCESSOR_CATEGORIES.get(type), recipe.id(), recipe.value().itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList(), recipe.value().itemProducts.stream().map(ModEmiPlugin::getEmiItemIngredient).toList(), recipe.value().fluidIngredients.stream().map(s -> NeoForgeEmiIngredient.of(s.sized())).toList(), recipe.value().fluidProducts.stream().map(s -> NeoForgeEmiIngredient.of(s.sized())).toList(), recipe.value());
                    if (!temp.getInputs().isEmpty()) {
                        registry.addRecipe(temp);
                    }
                }
            }
        }

        registry.addCategory(EMI_RADIATION_SCRUBBER_CATEGORY);
        registry.addWorkstation(EMI_RADIATION_SCRUBBER_CATEGORY, RADIATION_SCRUBBER_WORKSTATION);
        for (RecipeHolder<RadiationScrubberRecipe> recipe : manager.getAllRecipesFor(RADIATION_SCRUBBER_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiRadiationScrubberRecipe(recipe.id(), recipe.value()));
        }

        registry.addCategory(EMI_COLLECTOR_CATEGORY);
        registry.addRecipeHandler(null, new SimpleRecipeHandler<InventoryMenu>(EMI_COLLECTOR_CATEGORY));

        for (String type : COLLECTOR_WORKSTATIONS.keySet()) {
            registry.addWorkstation(EMI_COLLECTOR_CATEGORY, COLLECTOR_WORKSTATIONS.get(type));
        }
        List.of(
                Pair.of(NITROGEN_COLLECTOR_RECIPE_TYPE, "nitrogen_collector"), Pair.of(NITROGEN_COLLECTOR_COMPACT_RECIPE_TYPE, "nitrogen_collector_compact"), Pair.of(NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE, "nitrogen_collector_dense"),
                Pair.of(WATER_SOURCE_RECIPE_TYPE, "water_source"), Pair.of(WATER_SOURCE_COMPACT_RECIPE_TYPE, "water_source_compact"), Pair.of(WATER_SOURCE_DENSE_RECIPE_TYPE, "water_source_dense"),
                Pair.of(COBBLE_GENERATOR_RECIPE_TYPE, "cobblestone_generator"), Pair.of(COBBLE_GENERATOR_COMPACT_RECIPE_TYPE, "cobblestone_generator_compact"), Pair.of(COBBLE_GENERATOR_DENSE_RECIPE_TYPE, "cobblestone_generator_dense")
        ).forEach(i -> {
            for (RecipeHolder<? extends CollectorRecipe> recipe : manager.getAllRecipesFor(i.first().get())) {
                registry.addRecipe(new EmiCollectorRecipe(recipe.id(), COLLECTOR_MAP.get(i.second()), recipe.value().getItemProducts().stream().map(ModEmiPlugin::getEmiItemIngredient).toList(), recipe.value().getFluidProducts().stream().map(ModEmiPlugin::getEmiFluidIngredient).toList(), recipe.value().getCollectorProductionRate()));
            }
        });

        registry.addCategory(EMI_DECAY_GENERATOR_CATEGORY);
        registry.addWorkstation(EMI_DECAY_GENERATOR_CATEGORY, DECAY_GENERATOR_WORKSTATION);
        for (RecipeHolder<DecayGeneratorRecipe> recipe : manager.getAllRecipesFor(DECAY_GENERATOR_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiDecayGeneratorRecipe(recipe.id(), recipe.value()));
        }

        createItemDataMapCategory(registry, MACHINE_DIAPHRAGM_DATA, "machine_diaphragm", MACHINE_MAP.get("sintered_steel_diaphragm"));
        createItemDataMapCategory(registry, MACHINE_SIEVE_ASSEMBLY_DATA, "machine_sieve_assembly", MACHINE_MAP.get("steel_sieve_assembly"));
        createItemDataMapCategory(registry, ELECTROLYZER_CATHODE_DATA, "electrolyzer_cathode", MACHINE_MAP.get("electrolyzer_cathode_terminal"));
        createItemDataMapCategory(registry, ELECTROLYZER_ANODE_DATA, "electrolyzer_anode", MACHINE_MAP.get("electrolyzer_anode_terminal"));

        registry.addCategory(EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY);
        registry.addWorkstation(EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY, MULTIBLOCK_ELECTROLYZER_WORKSTATION);
        registry.addRecipeHandler(ELECTROLYZER_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY));
        for (RecipeHolder<MultiblockElectrolyzerRecipe> recipe : manager.getAllRecipesFor(MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiMultiblockElectrolyzerRecipe(recipe.id(), recipe.value()));
        }

        registry.addCategory(EMI_MULTIBLOCK_DISTILLER_CATEGORY);
        registry.addWorkstation(EMI_MULTIBLOCK_DISTILLER_CATEGORY, MULTIBLOCK_DISTILLER_WORKSTATION);
        registry.addRecipeHandler(DISTILLER_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_MULTIBLOCK_DISTILLER_CATEGORY));
        for (RecipeHolder<MultiblockDistillerRecipe> recipe : manager.getAllRecipesFor(MULTIBLOCK_DISTILLER_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiMultiblockDistillerRecipe(recipe.id(), recipe.value()));
        }
        registry.addCategory(EMI_MULTIBLOCK_INFILTRATOR_CATEGORY);
        registry.addWorkstation(EMI_MULTIBLOCK_INFILTRATOR_CATEGORY, MULTIBLOCK_INFILTRATOR_WORKSTATION);
        registry.addRecipeHandler(INFILTRATOR_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_MULTIBLOCK_INFILTRATOR_CATEGORY));
        for (RecipeHolder<MultiblockInfiltratorRecipe> recipe : manager.getAllRecipesFor(MULTIBLOCK_INFILTRATOR_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiMultiblockInfiltratorRecipe(recipe.id(), recipe.value()));
        }

        createFluidDataMapCategory(registry, INFILTRATOR_PRESSURE_DATA, "infiltrator_pressure", MACHINE_MAP.get("infiltrator_pressure_chamber"), INFILTRATOR_PRESSURE_FLUID_TOOLTIP);

        registry.addCategory(EMI_IRRADIATOR_CATEGORY);
        registry.addWorkstation(EMI_IRRADIATOR_CATEGORY, IRRADIATOR_WORKSTATION);
        registry.addRecipeHandler(FISSION_IRRADIATOR_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_IRRADIATOR_CATEGORY));
        for (RecipeHolder<FissionIrradiatorRecipe> recipe : manager.getAllRecipesFor(FISSION_IRRADIATOR_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiFissionIrradiatorRecipe(recipe.id(), getEmiItemIngredient(recipe.value().getItemIngredient()), getEmiItemIngredient(recipe.value().getItemProduct()), recipe.value()));
        }

        registry.addCategory(EMI_SOLID_FISSION_CATEGORY);
        addWorkstations(registry, EMI_SOLID_FISSION_CATEGORY, List.of(SOLID_FISSION_WORKSTATION, EmiStack.of(FISSION_REACTOR_MAP.get("fission_fuel_cell"))));
        registry.addRecipeHandler(SOLID_FISSION_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_SOLID_FISSION_CATEGORY));
        for (RecipeHolder<SolidFissionRecipe> recipe : manager.getAllRecipesFor(SOLID_FISSION_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiSolidFissionRecipe(recipe.id(), getEmiItemIngredient(recipe.value().getItemIngredient()), getEmiItemIngredient(recipe.value().getItemProduct()), recipe.value()));
        }

        registry.addCategory(EMI_SALT_FISSION_CATEGORY);
        addWorkstations(registry, EMI_SALT_FISSION_CATEGORY, List.of(SALT_FISSION_WORKSTATION, EmiStack.of(FISSION_REACTOR_MAP.get("fission_fuel_vessel"))));
        registry.addRecipeHandler(SALT_FISSION_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_SALT_FISSION_CATEGORY));
        for (RecipeHolder<SaltFissionRecipe> recipe : manager.getAllRecipesFor(SALT_FISSION_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiSaltFissionRecipe(recipe.id(), getEmiFluidIngredient(recipe.value().getFluidIngredient()), getEmiFluidIngredient(recipe.value().getFluidProduct()), recipe.value()));
        }

        createItemDataMapCategory(registry, FISSION_MODERATOR_DATA, "fission_moderator", HEAVY_WATER_MODERATOR);
        createItemDataMapCategory(registry, FISSION_REFLECTOR_DATA, "fission_reflector", FISSION_REACTOR_MAP.get("beryllium_carbon_reflector"));

        registry.addCategory(EMI_VENT_CATEGORY);
        registry.addWorkstation(EMI_VENT_CATEGORY, VENT_WORKSTATION);
        registry.addRecipeHandler(null, new SimpleRecipeHandler<>(EMI_VENT_CATEGORY));
        for (RecipeHolder<FissionHeatingRecipe> recipe : manager.getAllRecipesFor(FISSION_HEATING_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiFissionVentRecipe(recipe.id(), getEmiFluidIngredient(recipe.value().getFluidIngredient()), getEmiFluidIngredient(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addCategory(EMI_SALT_COOLING_CATEGORY);
        addWorkstations(registry, EMI_SALT_COOLING_CATEGORY, Stream.concat(COOLANTS.stream().map(part -> EmiStack.of(FISSION_REACTOR_MAP.get(part + "_fission_coolant_heater"))), Stream.of(SOLID_FISSION_WORKSTATION)).toList());
        registry.addRecipeHandler(FISSION_SALT_HEATER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_SALT_COOLING_CATEGORY));
        for (RecipeHolder<FissionCoolantHeaterRecipe> recipe : manager.getAllRecipesFor(COOLANT_HEATER_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiSaltCoolingRecipe(recipe.id(), getEmiFluidIngredient(recipe.value().getFluidIngredient()), getEmiFluidIngredient(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addCategory(EMI_EMERGENCY_COOLING_CATEGORY);
        registry.addWorkstation(EMI_EMERGENCY_COOLING_CATEGORY, EMERGENCY_COOLING_WORKSTATION);
        registry.addRecipeHandler(null, new SimpleRecipeHandler<>(EMI_EMERGENCY_COOLING_CATEGORY));
        for (RecipeHolder<FissionEmergencyCoolingRecipe> recipe : manager.getAllRecipesFor(FISSION_EMERGENCY_COOLING_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiFissionEmergencyCoolingRecipe(recipe.id(), getEmiFluidIngredient(recipe.value().getFluidIngredient()), getEmiFluidIngredient(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addCategory(EMI_HEAT_EXCHANGER_CATEGORY);
        addWorkstations(registry, EMI_HEAT_EXCHANGER_CATEGORY, List.of(HEAT_EXCHANGER_WORKSTATION, EmiStack.of(HX_MAP.get("copper_heat_exchanger_tube")), EmiStack.of(HX_MAP.get("hard_carbon_heat_exchanger_tube")), EmiStack.of(HX_MAP.get("thermoconducting_alloy_heat_exchanger_tube"))));
        for (RecipeHolder<HeatExchangerRecipe> recipe : manager.getAllRecipesFor(HEAT_EXCHANGER_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiHeatExchangerRecipe(recipe.id(), getEmiFluidIngredient(recipe.value().getFluidIngredient()), getEmiFluidIngredient(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addCategory(EMI_CONDENSER_CATEGORY);
        addWorkstations(registry, EMI_CONDENSER_CATEGORY, List.of(CONDENSER_WORKSTATION, EmiStack.of(HX_MAP.get("copper_heat_exchanger_tube")), EmiStack.of(HX_MAP.get("hard_carbon_heat_exchanger_tube")), EmiStack.of(HX_MAP.get("thermoconducting_alloy_heat_exchanger_tube"))));
        for (RecipeHolder<CondenserRecipe> recipe : manager.getAllRecipesFor(CONDENSER_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiCondenserRecipe(recipe.id(), getEmiFluidIngredient(recipe.value().getFluidIngredient()), getEmiFluidIngredient(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addCategory(EMI_CONDENSER_DISSIPATION_CATEGORY);
        registry.addWorkstation(EMI_CONDENSER_DISSIPATION_CATEGORY, CONDENSER_DISSIPATION_WORKSTATION);
        for (FluidStack fluidStack : getCondenserDissipationFluids()) {
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(EmiStack.of(fluidStack.getFluid())), EMI_CONDENSER_DISSIPATION_CATEGORY, ncLoc("/condenser_dissipation/" + fluidStack.getDescriptionId()), () -> ClientTooltipComponent.create(CONDENSER_DISSIPATION_TOOLTIP.apply(fluidStack).getVisualOrderText())));
        }

        registry.addCategory(EMI_TURBINE_CATEGORY);
        registry.addWorkstation(EMI_TURBINE_CATEGORY, TURBINE_WORKSTATION);
        registry.addRecipeHandler(TURBINE_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_TURBINE_CATEGORY));
        for (RecipeHolder<TurbineRecipe> recipe : manager.getAllRecipesFor(TURBINE_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiTurbineRecipe(recipe.id(), getEmiFluidIngredient(recipe.value().getFluidIngredient()), getEmiFluidIngredient(recipe.value().getFluidProduct()), recipe.value()));
        }

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

    private <R> void createItemDataMapCategory(EmiRegistry registry, DataMapType<Item, R> dataMapType, String name, ItemLike icon) {
        createItemDataMapCategory(registry, dataMapType, name, icon, null);
    }

    private <R> void createItemDataMapCategory(EmiRegistry registry, DataMapType<Item, R> dataMapType, String name, ItemLike defaultIcon, Function<ItemStack, Component> tooltip) {
        createDataMapCategory(registry, BuiltInRegistries.ITEM.getDataMap(dataMapType).entrySet(), name, EmiStack.of(defaultIcon), List.of(), (e) -> EmiStack.of(Objects.requireNonNull(BuiltInRegistries.ITEM.get(e.getKey()))), tooltip);
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
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(stack), category, ncLoc("/" + name + "/" + stack.getId().toString().replace(':', '_')), tooltip == null ? null : () -> ClientTooltipComponent.create(tooltip.apply((S) stack.getKey()).getVisualOrderText())));

            if (workStations.isEmpty()) {
                registry.addWorkstation(category, stack);
            }
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