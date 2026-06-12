package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.Supercooler")
@Document("mods/nuclearcraft/recipe/manager/SupercoolerRecipeManager")
public final class Supercooler {
    private Supercooler() {}

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          CTFluidIngredient input,
                          CTFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        SupercoolerRecipeManager.INSTANCE.addRecipe(name, input, output, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          CTFluidIngredient input,
                          CTChanceFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        SupercoolerRecipeManager.INSTANCE.addRecipe(name, input, output, timeModifier, powerModifier, radiation);
    }
}
