package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.FluidExtractorRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.FluidExtractorRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/FluidExtractorRecipeManager")
public class FluidExtractorRecipeManager extends BasicNuclearRecipeManager<FluidExtractorRecipe> {

    @ZenCodeGlobals.Global("nuclearFluidExtractor")
    public static final FluidExtractorRecipeManager INSTANCE = new FluidExtractorRecipeManager();

    public FluidExtractorRecipeManager() {
        super("fluid_extractor", FluidExtractorRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack itemOutput,
                          CTFluidIngredient fluidOutput,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipeWithChance(String name,
                          IIngredientWithAmount input,
                          Percentaged<IItemStack> itemOutput,
                          CTFluidIngredient fluidOutput,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack itemOutput,
                          IFluidStack fluidOutput,
                          @ZenCodeType.OptionalInt(100) int fluidOutputChance,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), new int[] { fluidOutputChance }, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipeWithChance(String name,
                          IIngredientWithAmount input,
                          Percentaged<IItemStack> itemOutput,
                          IFluidStack fluidOutput,
                          @ZenCodeType.OptionalInt(100) int fluidOutputChance,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), compact(itemOutput), null, compact(fluidOutput), new int[] { fluidOutputChance }, timeModifier, powerModifier, radiation);
    }
}
