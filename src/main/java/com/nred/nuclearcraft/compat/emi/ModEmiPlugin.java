package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.menu.ProcessorMenu;
import com.nred.nuclearcraft.recipe.base_types.ItemToItemRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.MenuRegistration.PROCESSOR_MENU_TYPES;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_TYPES;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    public static final Map<String, EmiStack> PROCESSOR_WORKSTATIONS = makeWorkstations();
    public static final Map<String, EmiRecipeCategory> PROCESSOR_CATEGORIES = makeCategories();

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();

        for (String type : PROCESSOR_CATEGORIES.keySet()) {
            registry.addCategory(PROCESSOR_CATEGORIES.get(type));
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
                    return (recipe.getCategory() == PROCESSOR_CATEGORIES.get(type));
                }
            });
            registry.addWorkstation(PROCESSOR_CATEGORIES.get(type), PROCESSOR_WORKSTATIONS.get(type));

            for (RecipeHolder<? extends ItemToItemRecipe> recipe : manager.getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(type).get())) { // TODO Add way to get type
                registry.addRecipe(new EmiItemToItemRecipe(type, PROCESSOR_CATEGORIES.get(type), PROCESSOR_MAP.get(type), recipe.id(), recipe.value().itemInputs.stream().map(sizedItemIngredient -> EmiIngredient.of(sizedItemIngredient.ingredient(), sizedItemIngredient.count())).toList(), recipe.value().itemResults.stream().map(sizedItemIngredient -> EmiIngredient.of(sizedItemIngredient.ingredient(), sizedItemIngredient.count())).toList(), recipe.value().getTimeModifier(), recipe.value().getPowerModifier()));
            }
        }
    }

    private static Map<String, EmiRecipeCategory> makeCategories() {
        Map<String, EmiRecipeCategory> map = new HashMap<>();
        map.put("alloy_furnace", new EmiRecipeCategory(ncLoc("alloy_furnace"), PROCESSOR_WORKSTATIONS.get("alloy_furnace")));
        map.put("manufactory", new EmiRecipeCategory(ncLoc("manufactory"), PROCESSOR_WORKSTATIONS.get("manufactory")));
        return map;
    }

    private static Map<String, EmiStack> makeWorkstations() {
        Map<String, EmiStack> map = new HashMap<>();
        map.put("alloy_furnace", EmiStack.of(PROCESSOR_MAP.get("alloy_furnace")));
        map.put("manufactory", EmiStack.of(PROCESSOR_MAP.get("manufactory")));
        return map;
    }
}