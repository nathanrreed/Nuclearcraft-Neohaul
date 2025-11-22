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
import com.nred.nuclearcraft.recipe.machine.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidEntries;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.multiblock.hx.CondenserLogic.getCondenserDissipationFluids;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
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

    public static final EmiRecipeCategory EMI_MODERATOR_CATEGORY = new EmiRecipeCategory(ncLoc("fission_moderator"), EmiStack.of(HEAVY_WATER_MODERATOR));
    public static final EmiRecipeCategory EMI_REFLECTOR_CATEGORY = new EmiRecipeCategory(ncLoc("fission_reflector"), EmiStack.of(FISSION_REACTOR_MAP.get("beryllium_carbon_reflector")));

    private static final EmiStack HEAT_EXCHANGER_WORKSTATION = EmiStack.of(HX_MAP.get("heat_exchanger_controller"));
    public static final EmiRecipeCategory EMI_HEAT_EXCHANGER_CATEGORY = new EmiRecipeCategory(ncLoc("heat_exchanger"), HEAT_EXCHANGER_WORKSTATION);

    private static final EmiStack CONDENSER_WORKSTATION = EmiStack.of(HX_MAP.get("condenser_controller"));
    public static final EmiRecipeCategory EMI_CONDENSER_CATEGORY = new EmiRecipeCategory(ncLoc("condenser"), CONDENSER_WORKSTATION);

    private static final EmiStack CONDENSER_DISSIPATION_WORKSTATION = EmiStack.of(HX_MAP.get("heat_exchanger_inlet"));
    public static final EmiRecipeCategory EMI_CONDENSER_DISSIPATION_CATEGORY = new EmiRecipeCategory(ncLoc("condenser_dissipation"), CONDENSER_DISSIPATION_WORKSTATION);

    private static final EmiStack MULTIBLOCK_DISTILLER_WORKSTATION = EmiStack.of(MACHINE_MAP.get("distiller_controller"));
    public static final EmiRecipeCategory EMI_MULTIBLOCK_DISTILLER_CATEGORY = new EmiRecipeCategory(ncLoc("multiblock_distiller"), MULTIBLOCK_DISTILLER_WORKSTATION);

    private static final EmiStack DECAY_GENERATOR_WORKSTATION = EmiStack.of(DECAY_GENERATOR);
    public static final EmiRecipeCategory EMI_DECAY_GENERATOR_CATEGORY = new EmiRecipeCategory(ncLoc("decay_generator"), DECAY_GENERATOR_WORKSTATION);

    private static final EmiStack MULTIBLOCK_ELECTROLYZER_WORKSTATION = EmiStack.of(MACHINE_MAP.get("electrolyzer_controller"));
    public static final EmiRecipeCategory EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY = new EmiRecipeCategory(ncLoc("multiblock_electrolyzer"), MULTIBLOCK_ELECTROLYZER_WORKSTATION);

    private static final EmiStack MULTIBLOCK_INFILTRATOR_WORKSTATION = EmiStack.of(MACHINE_MAP.get("infiltrator_controller"));
    public static final EmiRecipeCategory EMI_MULTIBLOCK_INFILTRATOR_CATEGORY = new EmiRecipeCategory(ncLoc("multiblock_infiltrator"), MULTIBLOCK_INFILTRATOR_WORKSTATION);

    private static final EmiStack ELECTROLYZER_CATHODE_WORKSTATION = EmiStack.of(MACHINE_MAP.get("electrolyzer_cathode_terminal"));
    public static final EmiRecipeCategory EMI_ELECTROLYZER_CATHODE_CATEGORY = new EmiRecipeCategory(ncLoc("electrolyzer_cathode"), ELECTROLYZER_CATHODE_WORKSTATION);

    private static final EmiStack ELECTROLYZER_ANODE_WORKSTATION = EmiStack.of(MACHINE_MAP.get("electrolyzer_anode_terminal"));
    public static final EmiRecipeCategory EMI_ELECTROLYZER_ANODE_CATEGORY = new EmiRecipeCategory(ncLoc("electrolyzer_anode"), ELECTROLYZER_ANODE_WORKSTATION);

    private static final EmiStack INFILTRATOR_PRESSURE_WORKSTATION = EmiStack.of(MACHINE_MAP.get("infiltrator_pressure_chamber"));
    public static final EmiRecipeCategory EMI_INFILTRATOR_PRESSURE_CATEGORY = new EmiRecipeCategory(ncLoc("infiltrator_pressure"), INFILTRATOR_PRESSURE_WORKSTATION);

    private static final EmiStack DIAPHRAGM_WORKSTATION = EmiStack.of(MACHINE_MAP.get("sintered_steel_diaphragm"));
    public static final EmiRecipeCategory EMI_DIAPHRAGM_CATEGORY = new EmiRecipeCategory(ncLoc("diaphragm"), DIAPHRAGM_WORKSTATION);

    private static final EmiStack SIEVE_ASSEMBLY_WORKSTATION = EmiStack.of(MACHINE_MAP.get("steel_sieve_assembly"));
    public static final EmiRecipeCategory EMI_SIEVE_ASSEMBLY_CATEGORY = new EmiRecipeCategory(ncLoc("sieve_assembly"), SIEVE_ASSEMBLY_WORKSTATION);

    private static final EmiStack RADIATION_SCRUBBER_WORKSTATION = EmiStack.of(RADIATION_SCRUBBER);
    public static final EmiRecipeCategory EMI_RADIATION_SCRUBBER_CATEGORY = new EmiRecipeCategory(ncLoc("radiation_scrubber"), RADIATION_SCRUBBER_WORKSTATION);

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();

        for (Map.Entry<String, Fluids> entry : fluidEntries(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FUEL_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            registry.addEmiStack(EmiStack.of(entry.getValue().bucket));
        }

        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, PROCESSOR_WORKSTATIONS.get("electric_furnace"));
        for (String type : EMI_PROCESSOR_CATEGORIES.keySet()) {
            registry.addCategory(EMI_PROCESSOR_CATEGORIES.get(type));
            registry.addRecipeHandler((MenuType<? extends ProcessorMenu<?, ?, ?>>) PROCESSOR_MENU_TYPES.get(type).get(), new StandardRecipeHandler() {
                @Override
                public List<Slot> getInputSources(AbstractContainerMenu handler) {
                    return handler.slots;
                }

                @Override
                public List<Slot> getCraftingSlots(AbstractContainerMenu handler) {
                    return handler.slots; //TODO
                }

                @Override
                public boolean supportsRecipe(EmiRecipe recipe) {
                    return (recipe.getCategory() == EMI_PROCESSOR_CATEGORIES.get(type));
                }
            });

            registry.addWorkstation(EMI_PROCESSOR_CATEGORIES.get(type), PROCESSOR_WORKSTATIONS.get(type));

            if (type.equals("electric_furnace")) {
                for (RecipeHolder<SmeltingRecipe> recipe : manager.getAllRecipesFor(RecipeType.SMELTING).stream().sorted(Comparator.comparing(other -> other.id().getPath())).toList()) { // Take from vanilla
                    registry.addRecipe(new EmiProcessorRecipe(type, EMI_PROCESSOR_CATEGORIES.get(type), recipe.id(), recipe.value().getIngredients().stream().map(EmiIngredient::of).toList(), List.of(EmiIngredient.of(Ingredient.of(recipe.value().getResultItem(null)))), List.of(), List.of(), 1, 1));
                }
            } else {
                for (RecipeHolder<? extends ProcessorRecipe> recipe : manager.getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(type).get()).stream().sorted(Comparator.comparing(other -> other.id().getPath())).toList()) {
                    EmiProcessorRecipe temp = new EmiProcessorRecipe(type, EMI_PROCESSOR_CATEGORIES.get(type), recipe.id(), recipe.value().itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList(), recipe.value().itemProducts.stream().map(ModEmiPlugin::getEmiItemIngredient).toList(), recipe.value().fluidIngredients.stream().map(s -> NeoForgeEmiIngredient.of(s.sized()).setChance(s.chancePercent() / 100f)).toList(), recipe.value().fluidProducts.stream().map(s -> NeoForgeEmiIngredient.of(s.sized()).setChance(s.chancePercent() / 100f)).toList(), recipe.value().getProcessTimeMultiplier(), recipe.value().getProcessPowerMultiplier());
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

        registry.addCategory(EMI_DIAPHRAGM_CATEGORY);
        addWorkstations(registry, EMI_DIAPHRAGM_CATEGORY, List.of(DIAPHRAGM_WORKSTATION, EmiStack.of(MACHINE_MAP.get("polyethersulfone_diaphragm")), EmiStack.of(MACHINE_MAP.get("zirfon_diaphragm"))));
        for (RecipeHolder<MachineDiaphragmRecipe> recipe : manager.getAllRecipesFor(MACHINE_DIAPHRAGM_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(EmiIngredient.of(recipe.value().block())), EMI_DIAPHRAGM_CATEGORY, ncLoc("diaphragm")));
        }

        registry.addCategory(EMI_SIEVE_ASSEMBLY_CATEGORY);
        addWorkstations(registry, EMI_SIEVE_ASSEMBLY_CATEGORY, List.of(SIEVE_ASSEMBLY_WORKSTATION, EmiStack.of(MACHINE_MAP.get("polytetrafluoroethene_sieve_assembly")), EmiStack.of(MACHINE_MAP.get("hastelloy_sieve_assembly"))));
        for (RecipeHolder<MachineSieveAssemblyRecipe> recipe : manager.getAllRecipesFor(MACHINE_SIEVE_ASSEMBLY_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(EmiIngredient.of(recipe.value().block())), EMI_SIEVE_ASSEMBLY_CATEGORY, ncLoc("sieve_assembly")));
        }

        registry.addCategory(EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY);
        registry.addWorkstation(EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY, MULTIBLOCK_ELECTROLYZER_WORKSTATION);
        registry.addRecipeHandler(ELECTROLYZER_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY));
        for (RecipeHolder<MultiblockElectrolyzerRecipe> recipe : manager.getAllRecipesFor(MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiMultiblockElectrolyzerRecipe(recipe.id(), recipe.value(), manager.getAllRecipesFor(ELECTROLYZER_ELECTROLYTE_RECIPE_TYPE.get()).stream().filter(r -> r.value().getElectrolyteGroup().equals(recipe.value().getElectrolyteGroup())).map(RecipeHolder::value).toList()));
        }

        registry.addCategory(EMI_ELECTROLYZER_CATHODE_CATEGORY);
        registry.addWorkstation(EMI_ELECTROLYZER_CATHODE_CATEGORY, ELECTROLYZER_CATHODE_WORKSTATION);
        for (RecipeHolder<ElectrolyzerCathodeRecipe> recipe : manager.getAllRecipesFor(ELECTROLYZER_CATHODE_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(EmiIngredient.of(recipe.value().block())), EMI_ELECTROLYZER_CATHODE_CATEGORY, ncLoc("electrolyzer_cathode")));
        }

        registry.addCategory(EMI_ELECTROLYZER_ANODE_CATEGORY);
        registry.addWorkstation(EMI_ELECTROLYZER_ANODE_CATEGORY, ELECTROLYZER_ANODE_WORKSTATION);
        for (RecipeHolder<ElectrolyzerAnodeRecipe> recipe : manager.getAllRecipesFor(ELECTROLYZER_ANODE_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(EmiIngredient.of(recipe.value().block())), EMI_ELECTROLYZER_ANODE_CATEGORY, ncLoc("electrolyzer_anode")));
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

        registry.addCategory(EMI_INFILTRATOR_PRESSURE_CATEGORY);
        registry.addWorkstation(EMI_INFILTRATOR_PRESSURE_CATEGORY, INFILTRATOR_PRESSURE_WORKSTATION);
        for (RecipeHolder<InfiltratorPressureFluidRecipe> recipe : manager.getAllRecipesFor(INFILTRATOR_PRESSURE_FLUID_RECIPE_TYPE.get())) {
            if (recipe.value().gas().hasNoFluids()) continue;
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(NeoForgeEmiIngredient.of(recipe.value().gas())), EMI_INFILTRATOR_PRESSURE_CATEGORY, ncLoc("infiltrator_pressure"), () -> ClientTooltipComponent.create(Component.translatable(MODID + ".recipe_viewer.infiltrator_pressure_fluid_efficiency", Component.literal(NCMath.pcDecimalPlaces(recipe.value().getInfiltratorPressureFluidEfficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE).getVisualOrderText())));
        }

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

        registry.addCategory(EMI_MODERATOR_CATEGORY);
        registry.addRecipe(new EmiBasicInfoRecipe(manager.getAllRecipesFor(FISSION_MODERATOR_RECIPE_TYPE.get()).stream().map(i -> EmiIngredient.of(i.value().moderator())).toList(), EMI_MODERATOR_CATEGORY, ncLoc("moderators")));
        registry.addCategory(EMI_REFLECTOR_CATEGORY);
        registry.addRecipe(new EmiBasicInfoRecipe(manager.getAllRecipesFor(FISSION_REFLECTOR_RECIPE_TYPE.get()).stream().map(i -> EmiIngredient.of(i.value().reflector())).toList(), EMI_REFLECTOR_CATEGORY, ncLoc("reflectors")));

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
            registry.addRecipe(new EmiBasicInfoRecipe(List.of(NeoForgeEmiIngredient.of(SizedFluidIngredient.of(fluidStack))), EMI_CONDENSER_DISSIPATION_CATEGORY, ncLoc("condenser_dissipation"), () -> ClientTooltipComponent.create(Component.translatable(MODID + ".recipe_viewer.condenser_dissipation_fluid_temp", Component.literal(fluidStack.getFluidType().getTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA).getVisualOrderText())));
        }

        registry.addCategory(EMI_TURBINE_CATEGORY);
        registry.addWorkstation(EMI_TURBINE_CATEGORY, TURBINE_WORKSTATION);
        registry.addRecipeHandler(TURBINE_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_TURBINE_CATEGORY));
        for (RecipeHolder<TurbineRecipe> recipe : manager.getAllRecipesFor(TURBINE_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiTurbineRecipe(recipe.id(), getEmiFluidIngredient(recipe.value().getFluidIngredient()), getEmiFluidIngredient(recipe.value().getFluidProduct()), recipe.value().getTurbinePowerPerMB(), recipe.value().getTurbineExpansionLevel(), recipe.value().getTurbineSpinUpMultiplier()));
        }

        //  TODO add corium registry.addRecipe(EmiWorldInteractionRecipe.builder()..build());
    }

    public static EmiIngredient getEmiFluidIngredient(SizedChanceFluidIngredient fluidIngredient) {
        return NeoForgeEmiIngredient.of(fluidIngredient.sized()).setChance(fluidIngredient.chancePercent() / 100f);
    }

    public static EmiIngredient getEmiItemIngredient(SizedChanceItemIngredient itemIngredient) {
        return NeoForgeEmiIngredient.of(itemIngredient.sized()).setChance(itemIngredient.chancePercent() / 100f);
    }

    public static EmiStack getEmiFluidStack(SizedChanceFluidIngredient fluidIngredient) {
        return EmiStack.of(fluidIngredient.getStack().getFluid(), fluidIngredient.amount()).setChance(fluidIngredient.chancePercent() / 100f);
    }

    public static EmiStack getEmiItemStack(SizedChanceItemIngredient itemIngredient) {
        return EmiStack.of(itemIngredient.getStack()).setChance(itemIngredient.chancePercent() / 100f);
    }

    private void addWorkstations(EmiRegistry registry, EmiRecipeCategory category, List<EmiStack> stacks) {
        for (EmiStack stack : stacks) {
            registry.addWorkstation(category, EmiIngredient.of(List.of(stack)));
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