package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.ChemicalReactor")
@Document("mods/nuclearcraft/recipe/manager/ChemicalReactorRecipeManager")
public final class ChemicalReactor {
    private ChemicalReactor() {}

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          CTFluidIngredient left,
                          CTFluidIngredient right,
                          CTFluidIngredient output1,
                          @ZenCodeType.Nullable CTFluidIngredient output2,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        ChemicalReactorRecipeManager.INSTANCE.addRecipe(name, left, right, output1, output2, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          CTFluidIngredient left,
                          CTFluidIngredient right,
                          CTChanceFluidIngredient output1,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output2,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        ChemicalReactorRecipeManager.INSTANCE.addRecipe(name, left, right, output1, output2, timeModifier, powerModifier, radiation);
    }
}
