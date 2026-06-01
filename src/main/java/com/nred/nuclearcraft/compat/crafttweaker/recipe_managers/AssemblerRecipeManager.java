package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.AssemblerRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.AssemblerRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/AssemblerRecipeManager")
public class AssemblerRecipeManager extends BasicNuclearRecipeManager<AssemblerRecipe> {

    @ZenCodeGlobals.Global("nuclearAssembler")
    public static final AssemblerRecipeManager INSTANCE = new AssemblerRecipeManager();

    public AssemblerRecipeManager() {
        super("assembler", AssemblerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipeWithChance(String name,
                          IIngredientWithAmount input1,
                          @ZenCodeType.Nullable IIngredientWithAmount input2,
                          @ZenCodeType.Nullable IIngredientWithAmount input3,
                          @ZenCodeType.Nullable IIngredientWithAmount input4,
                          IItemStack output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input1, input2, input3, input4), compact(output), null, null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input1,
                          @ZenCodeType.Nullable IIngredientWithAmount input2,
                          @ZenCodeType.Nullable IIngredientWithAmount input3,
                          @ZenCodeType.Nullable IIngredientWithAmount input4,
                          Percentaged<IItemStack> output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input1, input2, input3, input4), compact(output), null, null, timeModifier, powerModifier, radiation);
    }
}
