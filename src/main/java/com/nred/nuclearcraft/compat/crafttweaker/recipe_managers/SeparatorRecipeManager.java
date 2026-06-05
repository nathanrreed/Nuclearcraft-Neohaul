package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.SeparatorRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.Separator")
@Document("mods/nuclearcraft/recipe/manager/SeparatorRecipeManager")
public class SeparatorRecipeManager extends BasicNuclearRecipeManager<SeparatorRecipe> {

    @ZenCodeGlobals.Global("mods.nuclearcraft.Separator")
    public static final SeparatorRecipeManager INSTANCE = new SeparatorRecipeManager();

    public SeparatorRecipeManager() {
        super("separator", SeparatorRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack output1,
                          @ZenCodeType.Nullable IItemStack output2,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(input), compact(output1, output2), null, null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient output1,
                          @ZenCodeType.Nullable CTChanceItemIngredient output2,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, compact(input), compact(output1, output2), null, null, timeModifier, powerModifier, radiation);
    }
}

