package com.nred.nuclearcraft.recipe.processor;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.ELECTRIC_FURNACE_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_TYPES;

public class ElectricFurnaceRecipe extends ProcessorRecipe {
    public ElectricFurnaceRecipe(List<SizedIngredient> itemInputs, List<SizedIngredient> itemResults, List<SizedFluidIngredient> fluidInputs, List<SizedFluidIngredient> fluidResults, double timeModifier, double powerModifier) {
        super(itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ELECTRIC_FURNACE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PROCESSOR_RECIPE_TYPES.get("electric_furnace").get();
    }
}