package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.FluidExtractorRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.Extractor")
@Document("mods/nuclearcraft/recipe/manager/FluidExtractorRecipeManager")
public class FluidExtractorRecipeManager extends BasicNuclearRecipeManager<FluidExtractorRecipe> {

    @ZenCodeGlobals.Global("mods.nuclearcraft.Extractor")
    public static final FluidExtractorRecipeManager INSTANCE = new FluidExtractorRecipeManager();

    public FluidExtractorRecipeManager() {
        super("fluid_extractor", FluidExtractorRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack itemOutput,
                          CTFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient itemOutput,
                          CTFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack itemOutput,
                          IFluidStack fluidOutput,
                          @ZenCodeType.OptionalInt(100) int fluidOutputChance,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), new int[] { fluidOutputChance }, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient itemOutput,
                          IFluidStack fluidOutput,
                          @ZenCodeType.OptionalInt(100) int fluidOutputChance,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), new int[] { fluidOutputChance }, timeModifier, powerModifier, radiation);
    }
}

