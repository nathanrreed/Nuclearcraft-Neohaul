package com.nred.nuclearcraft.util;

import com.mojang.datafixers.util.Either;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.RotaryRecipe;
import mekanism.api.recipes.vanilla_input.RotaryRecipeInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;

import static mekanism.api.recipes.MekanismRecipeTypes.NAME_ROTARY;

public class ChemicalHelper {
    public static FluidStack getFluidFromGas(ChemicalStack chemicalStack, Level level) {
        if (chemicalStack.isEmpty() || level == null) {
            return FluidStack.EMPTY;
        }
        Optional<RecipeHolder<RotaryRecipe>> temp = level.getRecipeManager().getRecipeFor((RecipeType<RotaryRecipe>) BuiltInRegistries.RECIPE_TYPE.get(NAME_ROTARY), new RotaryRecipeInput(Either.right(chemicalStack)), level);
        return temp.map(holder -> holder.value().getFluidOutput(chemicalStack).copyWithAmount((int) ((holder.value().getChemicalInput().getNeededAmount(chemicalStack) / holder.value().getFluidOutput(chemicalStack).getAmount()) * chemicalStack.getAmount()))).orElse(FluidStack.EMPTY);
    }

    public static ChemicalStack getGasFromFluid(FluidStack fluidStack, Level level) {
        if (fluidStack.isEmpty() || level == null) {
            return ChemicalStack.EMPTY;
        }
        Optional<RecipeHolder<RotaryRecipe>> temp = level.getRecipeManager().getRecipeFor((RecipeType<RotaryRecipe>) BuiltInRegistries.RECIPE_TYPE.get(NAME_ROTARY), new RotaryRecipeInput(Either.left(fluidStack)), level);
        return temp.map(holder -> holder.value().getChemicalOutput(fluidStack).copyWithAmount((holder.value().getFluidInput().getNeededAmount(fluidStack) / holder.value().getChemicalOutput(fluidStack).getAmount()) * fluidStack.getAmount())).orElse(ChemicalStack.EMPTY);
    }
}