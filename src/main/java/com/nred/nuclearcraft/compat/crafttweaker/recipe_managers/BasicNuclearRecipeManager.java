package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.compat.crafttweaker.utils.CTProcessorRecipeWrapper;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_TYPES;

public abstract class BasicNuclearRecipeManager<T extends ProcessorRecipe> implements IRecipeManager<T> {
    private final String processorName;
    private final Class<T> recipeClass;

    protected BasicNuclearRecipeManager(String processorName, Class<T> recipeClass) {
        this.processorName = processorName;
        this.recipeClass = recipeClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeType<T> getRecipeType() {
        return (RecipeType<T>) PROCESSOR_RECIPE_TYPES.get(processorName).get();
    }

    protected final void addRecipeInternal(String name,
                                           @Nullable IIngredientWithAmount[] itemInputs,
                                           @Nullable IIngredientWithAmount[] itemOutputs,
                                           @Nullable CTFluidIngredient[] fluidInputs,
                                           @Nullable CTFluidIngredient[] fluidOutputs,
                                           double timeModifier,
                                           double powerModifier,
                                           double radiation) {
        addRecipeInternal(name, itemInputs, itemOutputs, null, null, fluidInputs, fluidOutputs, null, null, timeModifier, powerModifier, radiation);
    }

    protected final void addRecipeInternal(String name,
                                           @Nullable IIngredientWithAmount[] itemInputs,
                                           @Nullable Object[] itemOutputs,
                                           @Nullable CTFluidIngredient[] fluidInputs,
                                           @Nullable CTFluidIngredient[] fluidOutputs,
                                           double timeModifier,
                                           double powerModifier,
                                           double radiation) {
        addRecipeInternal(
                name,
                itemInputs,
                unwrapItemOutputs(itemOutputs),
                unwrapItemOutputChances(itemOutputs),
                unwrapItemOutputMinStackSizes(itemOutputs),
                fluidInputs,
                fluidOutputs,
                null,
                null,
                timeModifier,
                powerModifier,
                radiation
        );
    }

    protected final void addRecipeInternal(String name,
                                           @Nullable IIngredientWithAmount[] itemInputs,
                                           @Nullable IIngredientWithAmount[] itemOutputs,
                                           @Nullable CTFluidIngredient[] fluidInputs,
                                           @Nullable IFluidStack[] fluidOutputs,
                                           @Nullable int[] fluidOutputChances,
                                           double timeModifier,
                                           double powerModifier,
                                           double radiation) {
        addRecipeInternal(
                name,
                itemInputs,
                itemOutputs,
                null,
                null,
                fluidInputs,
                asFluidIngredients(fluidOutputs),
                fluidOutputChances,
                null,
                timeModifier,
                powerModifier,
                radiation
        );
    }

    protected final void addRecipeInternal(String name,
                                           @Nullable IIngredientWithAmount[] itemInputs,
                                           @Nullable Object[] itemOutputs,
                                           @Nullable CTFluidIngredient[] fluidInputs,
                                           @Nullable IFluidStack[] fluidOutputs,
                                           @Nullable int[] fluidOutputChances,
                                           double timeModifier,
                                           double powerModifier,
                                           double radiation) {
        addRecipeInternal(
                name,
                itemInputs,
                unwrapItemOutputs(itemOutputs),
                unwrapItemOutputChances(itemOutputs),
                unwrapItemOutputMinStackSizes(itemOutputs),
                fluidInputs,
                asFluidIngredients(fluidOutputs),
                fluidOutputChances,
                null,
                timeModifier,
                powerModifier,
                radiation
        );
    }

    protected final void addRecipeInternal(String name,
                                           @Nullable IIngredientWithAmount[] itemInputs,
                                           @Nullable IIngredientWithAmount[] itemOutputs,
                                           @Nullable int[] itemOutputChances,
                                           @Nullable int[] itemOutputMinStackSizes,
                                           @Nullable CTFluidIngredient[] fluidInputs,
                                           @Nullable CTFluidIngredient[] fluidOutputs,
                                           @Nullable int[] fluidOutputChances,
                                           @Nullable int[] fluidOutputMinStackSizes,
                                           double timeModifier,
                                           double powerModifier,
                                           double radiation) {
        CTProcessorRecipeWrapper.create(
                itemInputs,
                itemOutputs,
                itemOutputChances,
                itemOutputMinStackSizes,
                fluidInputs,
                fluidOutputs,
                fluidOutputChances,
                fluidOutputMinStackSizes
        ).createRecipe(this, name, wrapper -> createRecipe(wrapper, timeModifier, powerModifier, radiation));
    }

    protected final T createRecipe(CTProcessorRecipeWrapper wrapper,
                                   double timeModifier,
                                   double powerModifier,
                                   double radiation) {
        try {
            return recipeClass.getDeclaredConstructor(List.class, List.class, List.class, List.class, double.class, double.class, double.class)
                    .newInstance(wrapper.itemInputs(), wrapper.itemResults(), wrapper.fluidInputs(), wrapper.fluidResults(), timeModifier, powerModifier, radiation);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create processor recipe for '" + processorName + "'", e);
        }
    }

    @SafeVarargs
    protected static <T> T[] compact(T... values) {
        return Arrays.stream(values)
                .filter(Objects::nonNull)
                .toArray(length -> Arrays.copyOf(values, length));
    }

    @Nullable
    private static IIngredientWithAmount[] unwrapItemOutputs(@Nullable Object[] itemOutputs) {
        if (itemOutputs == null) {
            return null;
        }

        IIngredientWithAmount[] outputs = new IIngredientWithAmount[itemOutputs.length];
        for (int i = 0; i < itemOutputs.length; i++) {
            outputs[i] = parseItemOutput(itemOutputs[i]).ingredient();
        }
        return outputs;
    }

    @Nullable
    private static int[] unwrapItemOutputChances(@Nullable Object[] itemOutputs) {
        if (itemOutputs == null) {
            return null;
        }

        int[] chances = new int[itemOutputs.length];
        for (int i = 0; i < itemOutputs.length; i++) {
            chances[i] = parseItemOutput(itemOutputs[i]).chancePercent();
        }
        return chances;
    }

    @Nullable
    private static int[] unwrapItemOutputMinStackSizes(@Nullable Object[] itemOutputs) {
        if (itemOutputs == null) {
            return null;
        }

        int[] minStackSizes = new int[itemOutputs.length];
        for (int i = 0; i < itemOutputs.length; i++) {
            minStackSizes[i] = parseItemOutput(itemOutputs[i]).minStackSize();
        }
        return minStackSizes;
    }

    private static ParsedItemOutput parseItemOutput(Object output) {
        if (output instanceof CTChanceItemIngredient chanceItemIngredient) {
            return new ParsedItemOutput(
                    chanceItemIngredient.getInternalIngredient(),
                    chanceItemIngredient.getChancePercent(),
                    chanceItemIngredient.getMinStackSize()
            );
        }
        if (output instanceof IIngredientWithAmount ingredient) {
            return new ParsedItemOutput(ingredient, 100, 0);
        }

        throw new IllegalArgumentException("Unsupported item output type: " + output.getClass().getName());
    }

    @Nullable
    private static CTFluidIngredient[] asFluidIngredients(@Nullable IFluidStack[] fluidOutputs) {
        if (fluidOutputs == null) {
            return null;
        }

        CTFluidIngredient[] outputs = new CTFluidIngredient[fluidOutputs.length];
        for (int i = 0; i < fluidOutputs.length; i++) {
            outputs[i] = fluidOutputs[i].asFluidIngredient();
        }
        return outputs;
    }

    private record ParsedItemOutput(IIngredientWithAmount ingredient, int chancePercent, int minStackSize) {
    }
}
