package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.processor.MelterRecipe;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.MelterRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/MelterRecipeManager")
public class MelterRecipeManager extends BasicNuclearRecipeManager<MelterRecipe> {
    static final MelterRecipeManager INSTANCE = new MelterRecipeManager();

    public MelterRecipeManager() {
        super("melter", MelterRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), (IIngredientWithAmount[]) null, null, compact(output), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), (IIngredientWithAmount[]) null, null, compact(output), timeModifier, powerModifier, radiation);
    }
}
