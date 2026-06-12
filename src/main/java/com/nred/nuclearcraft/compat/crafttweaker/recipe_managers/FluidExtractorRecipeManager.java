package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.FluidExtractorRecipe;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.FluidExtractorRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/FluidExtractorRecipeManager")
public class FluidExtractorRecipeManager extends BasicNuclearRecipeManager<FluidExtractorRecipe> {
    static final FluidExtractorRecipeManager INSTANCE = new FluidExtractorRecipeManager();

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
        INSTANCE.addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient itemOutput,
                          CTFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack itemOutput,
                          CTChanceFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient itemOutput,
                          CTChanceFluidIngredient fluidOutput,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), timeModifier, powerModifier, radiation);
    }
}

