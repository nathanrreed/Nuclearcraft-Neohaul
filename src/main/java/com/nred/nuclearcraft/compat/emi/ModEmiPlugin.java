package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.menu.processor.ProcessorMenu;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.collector.CollectorRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.helpers.Concat.fluidEntries;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.Names.FISSION_HEAT_PARTS;
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

    private static final EmiStack SALT_COOLING_WORKSTATION = EmiStack.of(FISSION_REACTOR_MAP.get("standard_heater"));
    public static final EmiRecipeCategory EMI_SALT_COOLING_CATEGORY = new EmiRecipeCategory(ncLoc("salt_cooling"), SALT_COOLING_WORKSTATION);

    public static final EmiRecipeCategory EMI_MODERATOR_CATEGORY = new EmiRecipeCategory(ncLoc("fission_moderator"), EmiStack.of(HEAVY_WATER_MODERATOR));
    public static final EmiRecipeCategory EMI_REFLECTOR_CATEGORY = new EmiRecipeCategory(ncLoc("fission_reflector"), EmiStack.of(FISSION_REACTOR_MAP.get("beryllium_carbon_reflector")));

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();

        for (Map.Entry<String, Fluids> entry : fluidEntries(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FUEL_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            registry.addEmiStack(EmiStack.of(entry.getValue().bucket));
        }

        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, PROCESSOR_WORKSTATIONS.get("electric_furnace"));
        for (String type : EMI_PROCESSOR_CATEGORIES.keySet()) {
            registry.addCategory(EMI_PROCESSOR_CATEGORIES.get(type));
            registry.addRecipeHandler((MenuType<ProcessorMenu>) PROCESSOR_MENU_TYPES.get(type).get(), new StandardRecipeHandler<>() {
                @Override
                public List<Slot> getInputSources(ProcessorMenu handler) {
                    return handler.slots;
                }

                @Override
                public List<Slot> getCraftingSlots(ProcessorMenu handler) {
                    return handler.ITEM_INPUTS;
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
                    EmiProcessorRecipe temp = new EmiProcessorRecipe(type, EMI_PROCESSOR_CATEGORIES.get(type), recipe.id(), recipe.value().itemInputs.stream().map(NeoForgeEmiIngredient::of).toList(), recipe.value().itemResults.stream().map(NeoForgeEmiIngredient::of).toList(), recipe.value().fluidInputs.stream().map(NeoForgeEmiIngredient::of).toList(), recipe.value().fluidResults.stream().map(NeoForgeEmiIngredient::of).toList(), recipe.value().getTimeModifier(), recipe.value().getPowerModifier());
                    if (!temp.getInputs().isEmpty()) {
                        registry.addRecipe(temp);
                    }
                }
            }
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
                registry.addRecipe(new EmiCollectorRecipe(recipe.id(), COLLECTOR_MAP.get(i.second()), recipe.value().itemResult().isEmpty() ? List.of() : List.of(NeoForgeEmiIngredient.of(SizedIngredient.of(recipe.value().itemResult().getItem(), recipe.value().itemResult().getCount()))), recipe.value().fluidResult().isEmpty() ? List.of() : List.of(NeoForgeEmiIngredient.of(SizedFluidIngredient.of(recipe.value().fluidResult()))), recipe.value().rate()));
            }
        });


        registry.addCategory(EMI_TURBINE_CATEGORY);
        registry.addWorkstation(EMI_TURBINE_CATEGORY, TURBINE_WORKSTATION);
        registry.addRecipeHandler(TURBINE_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_TURBINE_CATEGORY));
        for (RecipeHolder<TurbineRecipe> recipe : manager.getAllRecipesFor(TURBINE_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiTurbineRecipe(recipe.id(), NeoForgeEmiIngredient.of(recipe.value().getFluidIngredient()), NeoForgeEmiIngredient.of(recipe.value().getFluidProduct()), recipe.value().getTurbinePowerPerMB(), recipe.value().getTurbineExpansionLevel(), recipe.value().getTurbineSpinUpMultiplier()));
        }


        registry.addCategory(EMI_SOLID_FISSION_CATEGORY);
        registry.addWorkstation(EMI_SOLID_FISSION_CATEGORY, EmiIngredient.of(List.of(SOLID_FISSION_WORKSTATION, EmiStack.of(FISSION_REACTOR_MAP.get("fission_cell")))));
        registry.addRecipeHandler(SOLID_FISSION_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_SOLID_FISSION_CATEGORY));
        for (RecipeHolder<SolidFissionRecipe> recipe : manager.getAllRecipesFor(SOLID_FISSION_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiSolidFissionRecipe(recipe.id(), NeoForgeEmiIngredient.of(recipe.value().getItemIngredient()), NeoForgeEmiIngredient.of(recipe.value().getItemProduct()), recipe.value()));
        }

        registry.addCategory(EMI_SALT_FISSION_CATEGORY);
        registry.addWorkstation(EMI_SALT_FISSION_CATEGORY, EmiIngredient.of(List.of(SALT_FISSION_WORKSTATION, EmiStack.of(FISSION_REACTOR_MAP.get("fission_vessel")))));
        registry.addRecipeHandler(SALT_FISSION_CONTROLLER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_SALT_FISSION_CATEGORY));
        for (RecipeHolder<SaltFissionRecipe> recipe : manager.getAllRecipesFor(SALT_FISSION_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiSaltFissionRecipe(recipe.id(), NeoForgeEmiIngredient.of(recipe.value().getFluidIngredient()), NeoForgeEmiIngredient.of(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addCategory(EMI_IRRADIATOR_CATEGORY);
        registry.addWorkstation(EMI_IRRADIATOR_CATEGORY, IRRADIATOR_WORKSTATION);
        registry.addRecipeHandler(FISSION_IRRADIATOR_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_IRRADIATOR_CATEGORY));
        for (RecipeHolder<FissionIrradiatorRecipe> recipe : manager.getAllRecipesFor(FISSION_IRRADIATOR_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiFissionIrradiatorRecipe(recipe.id(), NeoForgeEmiIngredient.of(recipe.value().getItemIngredient()), NeoForgeEmiIngredient.of(recipe.value().getItemProduct()), recipe.value()));
        }

        registry.addCategory(EMI_VENT_CATEGORY);
        registry.addWorkstation(EMI_VENT_CATEGORY, VENT_WORKSTATION);
        registry.addRecipeHandler(null, new SimpleRecipeHandler<>(EMI_VENT_CATEGORY));

        for (RecipeHolder<FissionHeatingRecipe> recipe : manager.getAllRecipesFor(FISSION_HEATING_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiFissionVentRecipe(recipe.id(), NeoForgeEmiIngredient.of(recipe.value().getFluidIngredient()), NeoForgeEmiIngredient.of(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addCategory(EMI_EMERGENCY_COOLING_CATEGORY);
        registry.addWorkstation(EMI_EMERGENCY_COOLING_CATEGORY, EMERGENCY_COOLING_WORKSTATION);
        registry.addRecipeHandler(null, new SimpleRecipeHandler<>(EMI_EMERGENCY_COOLING_CATEGORY));

        for (RecipeHolder<FissionEmergencyCoolingRecipe> recipe : manager.getAllRecipesFor(FISSION_EMERGENCY_COOLING_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiFissionEmergencyCoolingRecipe(recipe.id(), NeoForgeEmiIngredient.of(recipe.value().getFluidIngredient()), NeoForgeEmiIngredient.of(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addCategory(EMI_SALT_COOLING_CATEGORY);
        registry.addWorkstation(EMI_SALT_COOLING_CATEGORY, EmiIngredient.of(Stream.concat(FISSION_HEAT_PARTS.stream().map(part -> EmiStack.of(FISSION_REACTOR_MAP.get(part + "_heater"))), Stream.of(SOLID_FISSION_WORKSTATION)).toList()));
        registry.addRecipeHandler(FISSION_SALT_HEATER_MENU_TYPE.get(), new SimpleRecipeHandler<>(EMI_SALT_COOLING_CATEGORY));
        for (RecipeHolder<FissionCoolantHeaterRecipe> recipe : manager.getAllRecipesFor(FISSION_COOLANT_HEATER_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiSaltCoolingRecipe(recipe.id(), NeoForgeEmiIngredient.of(recipe.value().getFluidIngredient()), NeoForgeEmiIngredient.of(recipe.value().getFluidProduct()), recipe.value()));
        }

        registry.addRecipe(new EmiBasicInfoRecipe(manager.getAllRecipesFor(FISSION_MODERATOR_RECIPE_TYPE.get()).stream().map(i -> EmiIngredient.of(i.value().moderator())).toList(), EMI_MODERATOR_CATEGORY, ncLoc("moderators")));
        registry.addRecipe(new EmiBasicInfoRecipe(manager.getAllRecipesFor(FISSION_REFLECTOR_RECIPE_TYPE.get()).stream().map(i -> EmiIngredient.of(i.value().reflector())).toList(), EMI_REFLECTOR_CATEGORY, ncLoc("reflectors")));
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