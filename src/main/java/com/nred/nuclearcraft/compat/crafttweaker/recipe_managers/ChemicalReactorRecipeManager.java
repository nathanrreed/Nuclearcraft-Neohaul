package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.ChemicalReactorRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.ChemicalReactor")
@Document("mods/nuclearcraft/recipe/manager/ChemicalReactorRecipeManager")
public class ChemicalReactorRecipeManager extends BasicNuclearRecipeManager<ChemicalReactorRecipe> {

    @ZenCodeGlobals.Global("mods.nuclearcraft.ChemicalReactor")
    public static final ChemicalReactorRecipeManager INSTANCE = new ChemicalReactorRecipeManager();

    public ChemicalReactorRecipeManager() {
        super("chemical_reactor", ChemicalReactorRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient left,
                          CTFluidIngredient right,
                          CTFluidIngredient output1,
                          @ZenCodeType.Nullable CTFluidIngredient output2,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(left, right), compact(output1, output2), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient left,
                          CTFluidIngredient right,
                          IFluidStack output1,
                          @ZenCodeType.OptionalInt(100) int output1Chance,
                          @ZenCodeType.Nullable IFluidStack output2,
                          @ZenCodeType.OptionalInt(100) int output2Chance,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        IFluidStack[] outputs = compact(output1, output2);
        int[] chances = outputChances(outputs.length, output1Chance, output2Chance);
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(left, right), outputs, chances, timeModifier, powerModifier, radiation);
    }

    private static int[] outputChances(int length, int... values) {
        int[] chances = new int[length];
        System.arraycopy(values, 0, chances, 0, length);
        return chances;
    }
}
