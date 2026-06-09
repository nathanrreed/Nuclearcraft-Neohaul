package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.AlloyFurnace")
@Document("mods/nuclearcraft/recipe/manager/AlloyFurnaceRecipeManager")
public final class AlloyFurnace {
    private AlloyFurnace() {}

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          IIngredientWithAmount left,
                          IIngredientWithAmount right,
                          IItemStack output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        AlloyFurnaceRecipeManager.INSTANCE.addRecipe(name, left, right, output, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public static void addRecipe(String name,
                          IIngredientWithAmount left,
                          IIngredientWithAmount right,
                          CTChanceItemIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        AlloyFurnaceRecipeManager.INSTANCE.addRecipe(name, left, right, output, timeModifier, powerModifier, radiation);
    }
}
