package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.SupercoolerRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.SupercoolerRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/SupercoolerRecipeManager")
public class SupercoolerRecipeManager extends BasicNuclearRecipeManager<SupercoolerRecipe> {

    @ZenCodeGlobals.Global("nuclearSupercooler")
    public static final SupercoolerRecipeManager INSTANCE = new SupercoolerRecipeManager();

    public SupercoolerRecipeManager() {
        super("supercooler", SupercoolerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTFluidIngredient output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          IFluidStack output,
                          @ZenCodeType.OptionalInt(100) int outputChance,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output), new int[] { outputChance }, timeModifier, powerModifier, radiation);
    }
}
