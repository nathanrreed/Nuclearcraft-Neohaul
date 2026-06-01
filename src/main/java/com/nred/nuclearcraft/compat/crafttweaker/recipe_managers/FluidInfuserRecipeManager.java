package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.FluidInfuserRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.FluidInfuserRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/FluidInfuserRecipeManager")
public class FluidInfuserRecipeManager extends BasicNuclearRecipeManager<FluidInfuserRecipe> {

    @ZenCodeGlobals.Global("nuclearFluidInfuser")
    public static final FluidInfuserRecipeManager INSTANCE = new FluidInfuserRecipeManager();

    public FluidInfuserRecipeManager() {
        super("fluid_infuser", FluidInfuserRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount itemInput,
                          CTFluidIngredient fluidInput,
                          IItemStack output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(itemInput), compact(output), compact(fluidInput), null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipeWithChance(String name,
                          IIngredientWithAmount itemInput,
                          CTFluidIngredient fluidInput,
                          Percentaged<IItemStack> output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, compact(itemInput), compact(output), compact(fluidInput), null, timeModifier, powerModifier, radiation);
    }
}
