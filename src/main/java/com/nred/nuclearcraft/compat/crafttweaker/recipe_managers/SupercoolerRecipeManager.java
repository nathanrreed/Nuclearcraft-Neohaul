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
@ZenCodeType.Name("mods.nuclearcraft.Supercooler")
@Document("mods/nuclearcraft/recipe/manager/SupercoolerRecipeManager")
public class SupercoolerRecipeManager extends BasicNuclearRecipeManager<SupercoolerRecipe> {

    @ZenCodeGlobals.Global("mods.nuclearcraft.Supercooler")
    public static final SupercoolerRecipeManager INSTANCE = new SupercoolerRecipeManager();

    public SupercoolerRecipeManager() {
        super("supercooler", SupercoolerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          IFluidStack output,
                          @ZenCodeType.OptionalInt(100) int outputChance,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output), new int[] { outputChance }, timeModifier, powerModifier, radiation);
    }
}
