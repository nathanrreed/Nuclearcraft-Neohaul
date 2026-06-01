package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.AlloyFurnaceRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.AlloyFurnaceRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/AlloyFurnaceRecipeManager")
public class AlloyFurnaceRecipeManager extends BasicNuclearRecipeManager<AlloyFurnaceRecipe> {

    @ZenCodeGlobals.Global("nuclearAlloyFurnace")
    public static final AlloyFurnaceRecipeManager INSTANCE = new AlloyFurnaceRecipeManager();

    public AlloyFurnaceRecipeManager() {
        super("alloy_furnace", AlloyFurnaceRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount left,
                          IIngredientWithAmount right,
                          IItemStack output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(left, right), compact(output), null, null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipeWithChance(String name,
                          IIngredientWithAmount left,
                          IIngredientWithAmount right,
                          Percentaged<IItemStack> output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(left, right), compact(output), null, null, timeModifier, powerModifier, radiation);
    }
}
