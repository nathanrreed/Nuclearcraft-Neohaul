package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.ManufactoryRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.ManufactoryRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/ManufactoryRecipeManager")
public class ManufactoryRecipeManager extends BasicNuclearRecipeManager<ManufactoryRecipe> {

    @ZenCodeGlobals.Global("nuclearManufactory")
    public static final ManufactoryRecipeManager INSTANCE = new ManufactoryRecipeManager();

    public ManufactoryRecipeManager() {
        super("manufactory", ManufactoryRecipe.class);
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
