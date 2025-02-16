package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.menu.ProcessorMenu;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe;
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
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.MenuRegistration.PROCESSOR_MENU_TYPES;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_TYPES;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    private static final Map<String, EmiStack> PROCESSOR_WORKSTATIONS = PROCESSOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> EmiStack.of(PROCESSOR_MAP.get(type))));
    public static final Map<String, EmiRecipeCategory> EMI_PROCESSOR_CATEGORIES = PROCESSOR_MAP.keySet().stream().collect(Collectors.toMap(Function.identity(), type -> new EmiRecipeCategory(ncLoc(type), PROCESSOR_WORKSTATIONS.get(type))));

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();

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
                for (RecipeHolder<SmeltingRecipe> recipe : manager.getAllRecipesFor(RecipeType.SMELTING)) { // Take from vanilla
                    registry.addRecipe(new EmiProcessorRecipe(type, EMI_PROCESSOR_CATEGORIES.get(type), recipe.id(), recipe.value().getIngredients().stream().map(EmiIngredient::of).toList(), List.of(EmiIngredient.of(Ingredient.of(recipe.value().getResultItem(null)))), List.of(), List.of(), 1, 1));
                }
            } else {
                for (RecipeHolder<? extends ProcessorRecipe> recipe : manager.getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(type).get())) {
                    registry.addRecipe(new EmiProcessorRecipe(type, EMI_PROCESSOR_CATEGORIES.get(type), recipe.id(), recipe.value().itemInputs.stream().map(NeoForgeEmiIngredient::of).toList(), recipe.value().itemResults.stream().map(NeoForgeEmiIngredient::of).toList(), recipe.value().fluidInputs.stream().map(NeoForgeEmiIngredient::of).toList(), recipe.value().fluidResults.stream().map(NeoForgeEmiIngredient::of).toList(), recipe.value().getTimeModifier(), recipe.value().getPowerModifier()));
                }
            }
        }
    }
}