package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.MelterRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.MelterRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/MelterRecipeManager")
public class MelterRecipeManager extends BasicNuclearRecipeManager<MelterRecipe> {

    @ZenCodeGlobals.Global("nuclearMelter")
    public static final MelterRecipeManager INSTANCE = new MelterRecipeManager();

    public MelterRecipeManager() {
        super("melter", MelterRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTFluidIngredient output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), (IIngredientWithAmount[]) null, null, compact(output), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IFluidStack output,
                          @ZenCodeType.OptionalInt(100) int outputChance,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(input), (IIngredientWithAmount[]) null, null, compact(output), new int[] { outputChance }, timeModifier, powerModifier, radiation);
    }
}
