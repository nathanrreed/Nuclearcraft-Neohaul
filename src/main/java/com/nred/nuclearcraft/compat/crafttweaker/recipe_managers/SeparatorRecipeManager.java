package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.SeparatorRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.SeparatorRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/SeparatorRecipeManager")
public class SeparatorRecipeManager extends BasicNuclearRecipeManager<SeparatorRecipe> {

    @ZenCodeGlobals.Global("nuclearSeparator")
    public static final SeparatorRecipeManager INSTANCE = new SeparatorRecipeManager();

    public SeparatorRecipeManager() {
        super("separator", SeparatorRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack output1,
                          @ZenCodeType.Nullable IItemStack output2,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), compact(output1, output2), null, null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipeWithChance(String name,
                          IIngredientWithAmount input,
                          Percentaged<IItemStack> output1,
                          @ZenCodeType.Nullable Percentaged<IItemStack> output2,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), compact(output1, output2), null, null, timeModifier, powerModifier, radiation);
    }
}
