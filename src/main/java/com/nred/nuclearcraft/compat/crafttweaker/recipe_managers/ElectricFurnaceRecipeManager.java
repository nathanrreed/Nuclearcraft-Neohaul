package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.ElectricFurnaceRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.ElectricFurnaceRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/ElectricFurnaceRecipeManager")
public class ElectricFurnaceRecipeManager extends BasicNuclearRecipeManager<ElectricFurnaceRecipe> {

    @ZenCodeGlobals.Global("nuclearElectricFurnace")
    public static final ElectricFurnaceRecipeManager INSTANCE = new ElectricFurnaceRecipeManager();

    public ElectricFurnaceRecipeManager() {
        super("electric_furnace", ElectricFurnaceRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), compact(output), null, null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipeWithChance(String name,
                          IIngredientWithAmount input,
                          Percentaged<IItemStack> output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), compact(output), null, null, timeModifier, powerModifier, radiation);
    }
}
