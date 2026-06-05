package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.CentrifugeRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.Centrifuge")
@Document("mods/nuclearcraft/recipe/manager/CentrifugeRecipeManager")
public class CentrifugeRecipeManager extends BasicNuclearRecipeManager<CentrifugeRecipe> {

    @ZenCodeGlobals.Global("mods.nuclearcraft.Centrifuge")
    public static final CentrifugeRecipeManager INSTANCE = new CentrifugeRecipeManager();

    public CentrifugeRecipeManager() {
        super("centrifuge", CentrifugeRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTFluidIngredient output1,
                          @ZenCodeType.Nullable CTFluidIngredient output2,
                          @ZenCodeType.Nullable CTFluidIngredient output3,
                          @ZenCodeType.Nullable CTFluidIngredient output4,
                          @ZenCodeType.Nullable CTFluidIngredient output5,
                          @ZenCodeType.Nullable CTFluidIngredient output6,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output1, output2, output3, output4, output5, output6), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          IFluidStack output1,
                          @ZenCodeType.OptionalInt(100) int output1Chance,
                          @ZenCodeType.Nullable IFluidStack output2,
                          @ZenCodeType.OptionalInt(100) int output2Chance,
                          @ZenCodeType.Nullable IFluidStack output3,
                          @ZenCodeType.OptionalInt(100) int output3Chance,
                          @ZenCodeType.Nullable IFluidStack output4,
                          @ZenCodeType.OptionalInt(100) int output4Chance,
                          @ZenCodeType.Nullable IFluidStack output5,
                          @ZenCodeType.OptionalInt(100) int output5Chance,
                          @ZenCodeType.Nullable IFluidStack output6,
                          @ZenCodeType.OptionalInt(100) int output6Chance,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        IFluidStack[] outputs = compact(output1, output2, output3, output4, output5, output6);
        int[] chances = outputChances(outputs.length, output1Chance, output2Chance, output3Chance, output4Chance, output5Chance, output6Chance);
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), outputs, chances, timeModifier, powerModifier, radiation);
    }

    private static int[] outputChances(int length, int... values) {
        int[] chances = new int[length];
        System.arraycopy(values, 0, chances, 0, length);
        return chances;
    }
}
