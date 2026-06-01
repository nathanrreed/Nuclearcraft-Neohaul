package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.ElectrolyzerRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.ElectrolyzerRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/ElectrolyzerRecipeManager")
public class ElectrolyzerRecipeManager extends BasicNuclearRecipeManager<ElectrolyzerRecipe> {

    @ZenCodeGlobals.Global("nuclearElectrolyzer")
    public static final ElectrolyzerRecipeManager INSTANCE = new ElectrolyzerRecipeManager();

    public ElectrolyzerRecipeManager() {
        super("electrolyzer", ElectrolyzerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTFluidIngredient output1,
                          @ZenCodeType.Nullable CTFluidIngredient output2,
                          @ZenCodeType.Nullable CTFluidIngredient output3,
                          @ZenCodeType.Nullable CTFluidIngredient output4,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output1, output2, output3, output4), timeModifier, powerModifier, radiation);
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
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        IFluidStack[] outputs = compact(output1, output2, output3, output4);
        int[] chances = outputChances(outputs.length, output1Chance, output2Chance, output3Chance, output4Chance);
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), outputs, chances, timeModifier, powerModifier, radiation);
    }

    private static int[] outputChances(int length, int... values) {
        int[] chances = new int[length];
        System.arraycopy(values, 0, chances, 0, length);
        return chances;
    }
}
