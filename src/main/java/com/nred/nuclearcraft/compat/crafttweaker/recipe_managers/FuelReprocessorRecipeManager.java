package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.FuelReprocessorRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.FuelReprocessor")
@Document("mods/nuclearcraft/recipe/manager/FuelReprocessorRecipeManager")
public class FuelReprocessorRecipeManager extends BasicNuclearRecipeManager<FuelReprocessorRecipe> {

    @ZenCodeGlobals.Global("mods.nuclearcraft.FuelReprocessor")
    public static final FuelReprocessorRecipeManager INSTANCE = new FuelReprocessorRecipeManager();

    public FuelReprocessorRecipeManager() {
        super("fuel_reprocessor", FuelReprocessorRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack output1,
                          @ZenCodeType.Nullable IItemStack output2,
                          @ZenCodeType.Nullable IItemStack output3,
                          @ZenCodeType.Nullable IItemStack output4,
                          @ZenCodeType.Nullable IItemStack output5,
                          @ZenCodeType.Nullable IItemStack output6,
                          @ZenCodeType.Nullable IItemStack output7,
                          @ZenCodeType.Nullable IItemStack output8,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(input), compact(output1, output2, output3, output4, output5, output6, output7, output8), null, null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient output1,
                          @ZenCodeType.Nullable CTChanceItemIngredient output2,
                          @ZenCodeType.Nullable CTChanceItemIngredient output3,
                          @ZenCodeType.Nullable CTChanceItemIngredient output4,
                          @ZenCodeType.Nullable CTChanceItemIngredient output5,
                          @ZenCodeType.Nullable CTChanceItemIngredient output6,
                          @ZenCodeType.Nullable CTChanceItemIngredient output7,
                          @ZenCodeType.Nullable CTChanceItemIngredient output8,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(input), compact(output1, output2, output3, output4, output5, output6, output7, output8), null, null, timeModifier, powerModifier, radiation);
    }
}

