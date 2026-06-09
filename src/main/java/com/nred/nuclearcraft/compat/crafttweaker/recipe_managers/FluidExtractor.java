package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.FluidExtractor")
@Document("mods/nuclearcraft/recipe/manager/FluidExtractorRecipeManager")
public final class FluidExtractor {
    private FluidExtractor() {}

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack itemOutput,
                          CTFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        FluidExtractorRecipeManager.INSTANCE.addRecipe(name, input, itemOutput, fluidOutput, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient itemOutput,
                          CTFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        FluidExtractorRecipeManager.INSTANCE.addRecipe(name, input, itemOutput, fluidOutput, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack itemOutput,
                          CTChanceFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        FluidExtractorRecipeManager.INSTANCE.addRecipe(name, input, itemOutput, fluidOutput, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient itemOutput,
                          CTChanceFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        FluidExtractorRecipeManager.INSTANCE.addRecipe(name, input, itemOutput, fluidOutput, timeModifier, powerModifier, radiation);
    }
}
