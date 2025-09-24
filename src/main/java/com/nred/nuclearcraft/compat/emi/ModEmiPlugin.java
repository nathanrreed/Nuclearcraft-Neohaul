package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.menu.processor.ProcessorMenu;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.collector.CollectorRecipe;
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

import static com.nred.nuclearcraft.helpers.Concat.fluidEntries;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.MenuRegistration.PROCESSOR_MENU_TYPES;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    private static final Map<String, EmiStack> PROCESSOR_WORKSTATIONS = PROCESSOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> EmiStack.of(PROCESSOR_MAP.get(type))));
    public static final Map<String, EmiRecipeCategory> EMI_PROCESSOR_CATEGORIES = PROCESSOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> new EmiRecipeCategory(ncLoc(type), PROCESSOR_WORKSTATIONS.get(type))));

    private static final Map<String, EmiStack> COLLECTOR_WORKSTATIONS = COLLECTOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> EmiStack.of(COLLECTOR_MAP.get(type))));
    public static final EmiRecipeCategory EMI_COLLECTOR_CATEGORY = new EmiRecipeCategory(ncLoc("collector"), COLLECTOR_WORKSTATIONS.get("cobblestone_generator"));

    private static final EmiStack TURBINE_WORKSTATION = EmiStack.of(TURBINE_MAP.get("turbine_controller"));
    public static final EmiRecipeCategory EMI_TURBINE_CATEGORY = new EmiRecipeCategory(ncLoc("turbine"), TURBINE_WORKSTATION);

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();

        for (Map.Entry<String, Fluids> entry : fluidEntries(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
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
        registry.addRecipeHandler(null, new StandardRecipeHandler<InventoryMenu>() {
            @Override
            public List<Slot> getInputSources(InventoryMenu handler) {
                return handler.slots;
            }

            @Override
            public List<Slot> getCraftingSlots(InventoryMenu handler) {
                return List.of();
            }

            @Override
            public boolean supportsRecipe(EmiRecipe recipe) {
                return (recipe.getCategory() == EMI_COLLECTOR_CATEGORY);
            }
        });

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
        registry.addRecipeHandler(null, new StandardRecipeHandler<InventoryMenu>() {
            @Override
            public List<Slot> getInputSources(InventoryMenu handler) {
                return handler.slots;
            }

            @Override
            public List<Slot> getCraftingSlots(InventoryMenu handler) {
                return List.of();
            }

            @Override
            public boolean supportsRecipe(EmiRecipe recipe) {
                return (recipe.getCategory() == EMI_TURBINE_CATEGORY);
            }
        });

        for (RecipeHolder<TurbineRecipe> recipe : manager.getAllRecipesFor(TURBINE_RECIPE_TYPE.get())) {
            registry.addRecipe(new EmiTurbineRecipe(recipe.id(), NeoForgeEmiIngredient.of(recipe.value().fluidInput()),NeoForgeEmiIngredient.of(recipe.value().fluidResult()), recipe.value().power_per_mb(), recipe.value().expansion_level(), recipe.value().get_spin_up_multiplier()));
        }
    }
}