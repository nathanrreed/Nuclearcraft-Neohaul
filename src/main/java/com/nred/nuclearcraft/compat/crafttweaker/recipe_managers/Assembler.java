package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.Assembler")
@Document("mods/nuclearcraft/recipe/manager/AssemblerRecipeManager")
public final class Assembler {
    private Assembler() {}

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          IIngredientWithAmount input1,
                          @ZenCodeType.Nullable IIngredientWithAmount input2,
                          @ZenCodeType.Nullable IIngredientWithAmount input3,
                          @ZenCodeType.Nullable IIngredientWithAmount input4,
                          IItemStack output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        AssemblerRecipeManager.INSTANCE.addRecipe(name, input1, input2, input3, input4, output, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          IIngredientWithAmount input1,
                          @ZenCodeType.Nullable IIngredientWithAmount input2,
                          @ZenCodeType.Nullable IIngredientWithAmount input3,
                          @ZenCodeType.Nullable IIngredientWithAmount input4,
                          CTChanceItemIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        AssemblerRecipeManager.INSTANCE.addRecipe(name, input1, input2, input3, input4, output, timeModifier, powerModifier, radiation);
    }
}
