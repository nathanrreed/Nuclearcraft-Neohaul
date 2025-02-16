package com.nred.nuclearcraft.recipe.base_types;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public abstract class ProcessorRecipe implements Recipe<ProcessorRecipeInput> {
    public final List<SizedIngredient> itemInputs;
    public final List<SizedIngredient> itemResults;
    public final List<SizedFluidIngredient> fluidInputs;
    public final List<SizedFluidIngredient> fluidResults;
    private final double timeModifier;
    private final double powerModifier;

    public ProcessorRecipe(List<SizedIngredient> itemInputs, List<SizedIngredient> itemResults, List<SizedFluidIngredient> fluidInputs, List<SizedFluidIngredient> fluidResults, double timeModifier, double powerModifier) {
        this.itemInputs = itemInputs;
        this.itemResults = itemResults;
        this.fluidInputs = fluidInputs;
        this.fluidResults = fluidResults;
        this.timeModifier = timeModifier;
        this.powerModifier = powerModifier;
    }

    @Override
    public boolean matches(ProcessorRecipeInput input, Level level) {
        for (SizedIngredient itemInput : itemInputs) {
            if (!testSizedIngredient(itemInput, input.stacks())) {
                return false;
            }
        }
        for (SizedFluidIngredient fluidInput : fluidInputs) {
            if (!testSizedFluidIngredient(fluidInput, input.fluids())) {
                return false;
            }
        }

        return true;
    }

    public static boolean testSizedFluidIngredient(SizedFluidIngredient ingredient, List<FluidStack> stacks) {
        return stacks.stream().anyMatch(stack -> ingredient.test(stack) && stack.getAmount() >= ingredient.amount());
    }

    public static boolean testSizedIngredient(SizedIngredient ingredient, List<ItemStack> stacks) {
        return stacks.stream().anyMatch(stack -> ingredient.test(stack) && stack.getCount() >= ingredient.count());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(ProcessorRecipeInput input, HolderLookup.Provider registries) {
        return itemResults.getFirst().getItems()[0];
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return itemResults.getFirst().getItems()[0];
    }

    public FluidStack getResultFluid(HolderLookup.Provider registries) {
        return fluidResults.getFirst().getFluids()[0];
    }

    public List<SizedIngredient> getItemInputs() {
        return itemInputs;
    }

    public List<SizedIngredient> getItemResults() {
        return itemResults;
    }

    public List<SizedFluidIngredient> getFluidInputs() {
        return fluidInputs;
    }

    public List<SizedFluidIngredient> getFluidResults() {
        return fluidResults;
    }

    public double getTimeModifier() {
        return timeModifier;
    }

    public double getPowerModifier() {
        return powerModifier;
    }
}