package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.IngredientSorption;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.util.NCUtil;
import com.nred.nuclearcraft.util.StreamHelper;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.nred.nuclearcraft.recipe.RecipeHelper.matchFluidIngredient;
import static com.nred.nuclearcraft.recipe.RecipeHelper.matchIngredient;

public abstract class BasicRecipeHandler<RECIPE extends BasicRecipe> extends AbstractRecipeHandler<RECIPE> {
    public final String name;
    public final int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;

    private final List<Set<ResourceLocation>> validFluids = new ArrayList<>();

    public BasicRecipeHandler(@Nonnull String name, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize) {
        this.name = name;
        this.itemInputSize = itemInputSize;
        this.fluidInputSize = fluidInputSize;
        this.itemOutputSize = itemOutputSize;
        this.fluidOutputSize = fluidOutputSize;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getItemInputSize() {
        return itemInputSize;
    }

    public int getFluidInputSize() {
        return fluidInputSize;
    }

    public int getItemOutputSize() {
        return itemOutputSize;
    }

    public int getFluidOutputSize() {
        return fluidOutputSize;
    }

    @Override
    public void init(RecipeManager recipeManager) {
        super.init(recipeManager);
    }

    @Override
    public void postInit(RecipeManager recipeManager) {
        super.postInit(recipeManager);
        this.setValidFluids();
    }

    public void setValidFluids() {
        validFluids.clear();
    }

    public List<Set<ResourceLocation>> getValidFluids(RecipeManager recipeManager) {
        if (validFluids.isEmpty())
            validFluids.addAll(RecipeHelper.validFluids(this, recipeManager));
        return validFluids;
    }

    public Set<ResourceLocation> getValidFluids(Level level, int index) {
        return getValidFluids(NCUtil.getRecipeManager(level)).get(index);
    }

    public boolean isValidInput(ItemStack stack, Function<BasicRecipe, List<SizedChanceItemIngredient>> ingredientsFunction, RecipeManager recipeManager) {
        for (BasicRecipe recipe : getRecipeList(recipeManager)) {
            for (SizedChanceItemIngredient input : ingredientsFunction.apply(recipe)) {
                if (matchIngredient(input, stack, IngredientSorption.NEUTRAL).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidInput(FluidStack stack, Function<BasicRecipe, List<SizedChanceFluidIngredient>> ingredientsFunction, RecipeManager recipeManager) {
        for (BasicRecipe recipe : getRecipeList(recipeManager)) {
            for (SizedChanceFluidIngredient input : ingredientsFunction.apply(recipe)) {
                if (matchFluidIngredient(input, stack, IngredientSorption.NEUTRAL).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidItemInput(ItemStack stack, Level level) {
        return isValidInput(stack, BasicRecipe::getItemIngredients, level.getRecipeManager());
    }

    public boolean isValidFluidInput(FluidStack stack, Level level) {
        return isValidFluidInput(stack, level.getRecipeManager());
    }

    public boolean isValidFluidInput(FluidStack stack, RecipeManager recipeManager) {
        return isValidInput(stack, BasicRecipe::getFluidIngredients, recipeManager);
    }

    /**
     * Smart insertion - don't insert if stack is not valid for any possible recipes
     */
    public boolean isValidItemInput(RecipeManager recipeManager, ItemStack stack, int index, List<ItemStack> inputs, List<FluidStack> associatedInputs, RecipeInfo<? extends BasicRecipe> recipeInfo, Predicate<ItemStack> isEmptyFunction, Predicate<FluidStack> associatedIsEmptyFunction, Predicate<ItemStack> isEqualFunction, Function<BasicRecipe, List<SizedChanceItemIngredient>> ingredientsFunction, Function<BasicRecipe, List<SizedChanceFluidIngredient>> associatedIngredientsFunction) {
        List<ItemStack> otherInputs = inputsExcludingIndex(inputs, index);
        if ((otherInputs.stream().allMatch(isEmptyFunction) && associatedInputs.stream().allMatch(associatedIsEmptyFunction)) || isEqualFunction.test(inputs.get(index))) {
            return isValidInput(stack, ingredientsFunction, recipeManager);
        }

        if (recipeInfo == null) {
            ObjectSet<BasicRecipe> recipes = new ObjectOpenHashSet<>(getRecipeList(recipeManager));
            recipeLoop:
            for (BasicRecipe recipe : getRecipeList(recipeManager)) {
                List<SizedChanceItemIngredient> ingredients = ingredientsFunction.apply(recipe);
                List<SizedChanceFluidIngredient> associatedIngredients = associatedIngredientsFunction.apply(recipe);
                stackLoop:
                for (ItemStack inputStack : inputs) {
                    if (!isEmptyFunction.test(inputStack)) {
                        for (SizedChanceItemIngredient recipeInput : ingredients) {
                            if (matchIngredient(recipeInput, inputStack, IngredientSorption.NEUTRAL).matches()) {
                                continue stackLoop;
                            }
                        }
                        recipes.remove(recipe);
                        continue recipeLoop;
                    }
                }

                associatedStackLoop:
                for (FluidStack inputStack : associatedInputs) {
                    if (!associatedIsEmptyFunction.test(inputStack)) {
                        for (SizedChanceFluidIngredient recipeInput : associatedIngredients) {
                            if (matchFluidIngredient(recipeInput, inputStack, IngredientSorption.NEUTRAL).matches()) {
                                continue associatedStackLoop;
                            }
                        }
                        recipes.remove(recipe);
                        continue recipeLoop;
                    }
                }
            }

            for (BasicRecipe recipe : recipes) {
                if (isValidItemInputInternal(stack, otherInputs, ingredientsFunction.apply(recipe), isEmptyFunction)) {
                    return true;
                }
            }
            return false;
        } else {
            return isValidItemInputInternal(stack, otherInputs, ingredientsFunction.apply(recipeInfo.recipe), isEmptyFunction);
        }
    }

    public boolean isValidFluidInput(RecipeManager recipeManager, FluidStack stack, int index, List<FluidStack> inputs, List<ItemStack> associatedInputs, RecipeInfo<? extends BasicRecipe> recipeInfo, Predicate<FluidStack> isEmptyFunction, Predicate<ItemStack> associatedIsEmptyFunction, Predicate<FluidStack> isEqualFunction, Function<BasicRecipe, List<SizedChanceFluidIngredient>> ingredientsFunction, Function<BasicRecipe, List<SizedChanceItemIngredient>> associatedIngredientsFunction) {
        List<FluidStack> otherInputs = inputsExcludingIndex(inputs, index);
        if ((otherInputs.stream().allMatch(isEmptyFunction) && associatedInputs.stream().allMatch(associatedIsEmptyFunction)) || isEqualFunction.test(inputs.get(index))) {
            return isValidInput(stack, ingredientsFunction, recipeManager);
        }

        if (recipeInfo == null) {
            ObjectSet<BasicRecipe> recipes = new ObjectOpenHashSet<>(getRecipeList(recipeManager));
            recipeLoop:
            for (BasicRecipe recipe : getRecipeList(recipeManager)) {
                List<SizedChanceFluidIngredient> ingredients = ingredientsFunction.apply(recipe);
                List<SizedChanceItemIngredient> associatedIngredients = associatedIngredientsFunction.apply(recipe);
                stackLoop:
                for (FluidStack inputStack : inputs) {
                    if (!isEmptyFunction.test(inputStack)) {
                        for (SizedChanceFluidIngredient recipeInput : ingredients) {
                            if (matchFluidIngredient(recipeInput, inputStack, IngredientSorption.NEUTRAL).matches()) {
                                continue stackLoop;
                            }
                        }
                        recipes.remove(recipe);
                        continue recipeLoop;
                    }
                }

                associatedStackLoop:
                for (ItemStack inputStack : associatedInputs) {
                    if (!associatedIsEmptyFunction.test(inputStack)) {
                        for (SizedChanceItemIngredient recipeInput : associatedIngredients) {
                            if (matchIngredient(recipeInput, inputStack, IngredientSorption.NEUTRAL).matches()) {
                                continue associatedStackLoop;
                            }
                        }
                        recipes.remove(recipe);
                        continue recipeLoop;
                    }
                }
            }

            for (BasicRecipe recipe : recipes) {
                if (isValidFluidInputInternal(stack, otherInputs, ingredientsFunction.apply(recipe), isEmptyFunction)) {
                    return true;
                }
            }
            return false;
        } else {
            return isValidFluidInputInternal(stack, otherInputs, ingredientsFunction.apply(recipeInfo.recipe), isEmptyFunction);
        }
    }


    public boolean isValidItemInput(RecipeManager recipeManager, ItemStack stack, int slot, List<ItemStack> itemInputs, List<Tank> fluidInputs, RecipeInfo<? extends BasicRecipe> recipeInfo) {
        return isValidItemInput(recipeManager, stack, slot, itemInputs, StreamHelper.map(fluidInputs, Tank::getFluid), recipeInfo, ItemStack::isEmpty, x -> x == null || x.getAmount() <= 0, x -> ItemStack.isSameItemSameComponents(stack, x), BasicRecipe::getItemIngredients, BasicRecipe::getFluidIngredients);
    }

    public boolean isValidFluidInput(RecipeManager recipeManager, FluidStack stack, int tankNumber, List<Tank> fluidInputs, List<ItemStack> itemInputs, @Nullable RecipeInfo<? extends BasicRecipe> recipeInfo) {
        return isValidFluidInput(recipeManager, stack, tankNumber, StreamHelper.map(fluidInputs, Tank::getFluid), itemInputs, recipeInfo, x -> x == null || x.getAmount() <= 0, ItemStack::isEmpty, stack == null ? Objects::isNull : x -> (FluidStack.isSameFluidSameComponents(stack, x)), BasicRecipe::getFluidIngredients, BasicRecipe::getItemIngredients);
    }

    protected boolean isValidItemInputInternal(ItemStack stack, List<ItemStack> otherInputs, List<SizedChanceItemIngredient> ingredients, Predicate<ItemStack> isEmptyFunction) {
        for (SizedChanceItemIngredient input : ingredients) {
            if (matchIngredient(input, stack, IngredientSorption.NEUTRAL).matches()) {
                for (ItemStack other : otherInputs) {
                    if (!isEmptyFunction.test(other) && matchIngredient(input, other, IngredientSorption.NEUTRAL).matches()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected boolean isValidFluidInputInternal(FluidStack stack, List<FluidStack> otherInputs, List<SizedChanceFluidIngredient> ingredients, Predicate<FluidStack> isEmptyFunction) {
        for (SizedChanceFluidIngredient input : ingredients) {
            if (matchFluidIngredient(input, stack, IngredientSorption.NEUTRAL).matches()) {
                for (FluidStack other : otherInputs) {
                    if (!isEmptyFunction.test(other) && matchFluidIngredient(input, other, IngredientSorption.NEUTRAL).matches()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected static <T> List<T> inputsExcludingIndex(List<T> inputs, int index) {
        List<T> inputsExcludingIndex = new ArrayList<>(inputs);
        inputsExcludingIndex.remove(index);
        return inputsExcludingIndex;
    }
}