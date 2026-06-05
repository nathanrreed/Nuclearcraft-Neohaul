package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.FluidEnricherRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.Enricher")
@Document("mods/nuclearcraft/recipe/manager/FluidEnricherRecipeManager")
public class FluidEnricherRecipeManager extends BasicNuclearRecipeManager<FluidEnricherRecipe> {

    @ZenCodeGlobals.Global("mods.nuclearcraft.Enricher")
    public static final FluidEnricherRecipeManager INSTANCE = new FluidEnricherRecipeManager();

    public FluidEnricherRecipeManager() {
        super("fluid_enricher", FluidEnricherRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount itemInput,
                          CTFluidIngredient fluidInput,
                          CTFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(itemInput), (IIngredientWithAmount[]) null, compact(fluidInput), compact(output), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount itemInput,
                          CTFluidIngredient fluidInput,
                          IFluidStack output,
                          @ZenCodeType.OptionalInt(100) int outputChance,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(itemInput), (IIngredientWithAmount[]) null, compact(fluidInput), compact(output), new int[] { outputChance }, timeModifier, powerModifier, radiation);
    }
}
