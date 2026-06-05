package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.FluidMixerRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.SaltMixer")
@Document("mods/nuclearcraft/recipe/manager/FluidMixerRecipeManager")
public class FluidMixerRecipeManager extends BasicNuclearRecipeManager<FluidMixerRecipe> {

    @ZenCodeGlobals.Global("mods.nuclearcraft.SaltMixer")
    public static final FluidMixerRecipeManager INSTANCE = new FluidMixerRecipeManager();

    public FluidMixerRecipeManager() {
        super("fluid_mixer", FluidMixerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient left,
                          CTFluidIngredient right,
                          CTFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(left, right), compact(output), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient left,
                          CTFluidIngredient right,
                          IFluidStack output,
                          @ZenCodeType.OptionalInt(100) int outputChance,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(left, right), compact(output), new int[] { outputChance }, timeModifier, powerModifier, radiation);
    }
}
