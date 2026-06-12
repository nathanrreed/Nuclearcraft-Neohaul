package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.ElectricFurnaceRecipe;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.ElectricFurnaceRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/ElectricFurnaceRecipeManager")
public class ElectricFurnaceRecipeManager extends BasicNuclearRecipeManager<ElectricFurnaceRecipe> {
    static final ElectricFurnaceRecipeManager INSTANCE = new ElectricFurnaceRecipeManager();

    public ElectricFurnaceRecipeManager() {
        super("electric_furnace", ElectricFurnaceRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), compact(output), null, null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), compact(output), null, null, timeModifier, powerModifier, radiation);
    }
}

