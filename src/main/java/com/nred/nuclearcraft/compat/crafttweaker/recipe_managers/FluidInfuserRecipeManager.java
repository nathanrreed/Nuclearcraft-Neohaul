package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.FluidInfuserRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FLUID_INFUSER_RECIPE_TYPE;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.FluidInfuserRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/FluidInfuserRecipeManager")
public class FluidInfuserRecipeManager extends BasicNuclearRecipeManager<FluidInfuserRecipe> {
    static final FluidInfuserRecipeManager INSTANCE = new FluidInfuserRecipeManager();

    public FluidInfuserRecipeManager() {
        super("fluid_infuser", FluidInfuserRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount itemInput,
                          CTFluidIngredient fluidInput,
                          IItemStack output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(itemInput), compact(output), compact(fluidInput), null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount itemInput,
                          CTFluidIngredient fluidInput,
                          CTChanceItemIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(itemInput), compact(output), compact(fluidInput), null, timeModifier, powerModifier, radiation);
    }

    @Override
    public RecipeType<FluidInfuserRecipe> getRecipeType() {
        return FLUID_INFUSER_RECIPE_TYPE.get();
    }
}