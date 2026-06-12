package com.nred.nuclearcraft.compat.crafttweaker.utils;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public record CTProcessorRecipeWrapper(List<SizedChanceItemIngredient> itemInputs,
                                       List<SizedChanceItemIngredient> itemResults,
                                       List<SizedChanceFluidIngredient> fluidInputs,
                                       List<SizedChanceFluidIngredient> fluidResults) {

    public static CTProcessorRecipeWrapper create(@Nullable IIngredientWithAmount[] itemInputs,
                                                  @Nullable IIngredientWithAmount[] itemResults,
                                                  @Nullable int[] itemOutputChances,
                                                  @Nullable int[] itemOutputMinStackSizes,
                                                  @Nullable CTFluidIngredient[] fluidInputs,
                                                  @Nullable CTFluidIngredient[] fluidResults,
                                                  @Nullable int[] fluidOutputChances,
                                                  @Nullable int[] fluidOutputMinStackSizes) {

        List<SizedChanceItemIngredient> item_in = Collections.emptyList();
        List<SizedChanceItemIngredient> item_out = Collections.emptyList();
        List<SizedChanceFluidIngredient> fluid_in = Collections.emptyList();
        List<SizedChanceFluidIngredient> fluid_out = Collections.emptyList();

        validateArrayLengths("item results", itemResults, itemOutputChances, itemOutputMinStackSizes);
        validateArrayLengths("fluid results", fluidResults, fluidOutputChances, fluidOutputMinStackSizes);

        if (itemInputs != null) {
            item_in = new ObjectArrayList<>();
            for (IIngredientWithAmount item : itemInputs) {
                item_in.add(new SizedChanceItemIngredient(item.ingredient().asVanillaIngredient(), item.amount()));
            }
        }

        if (itemResults != null) {
            item_out = new ObjectArrayList<>();
            for (int i = 0; i < itemResults.length; i++) {
                IIngredientWithAmount item = itemResults[i];
                int chance = getOrDefault(itemOutputChances, i, 100);
                int minStackSize = getOrDefault(itemOutputMinStackSizes, i, 0);
                item_out.add(new SizedChanceItemIngredient(item.ingredient().asVanillaIngredient(), item.amount(), chance, minStackSize));
            }
        }

        if (fluidInputs != null) {
            fluid_in = new ObjectArrayList<>();
            for (CTFluidIngredient fluid : fluidInputs) {
                fluid_in.add(new SizedChanceFluidIngredient(toFluidIngredient(fluid), getFluidAmount(fluid)));
            }
        }

        if (fluidResults != null) {
            fluid_out = new ObjectArrayList<>();
            for (int i = 0; i < fluidResults.length; i++) {
                CTFluidIngredient fluid = fluidResults[i];
                int chance = getOrDefault(fluidOutputChances, i, 100);
                int minStackSize = getOrDefault(fluidOutputMinStackSizes, i, 0);
                fluid_out.add(new SizedChanceFluidIngredient(toFluidIngredient(fluid), getFluidAmount(fluid), chance, minStackSize));
            }
        }

        return new CTProcessorRecipeWrapper(item_in, item_out, fluid_in, fluid_out);
    }

    public static CTProcessorRecipeWrapper create(@Nullable IIngredientWithAmount[] inputs,
                                                  @Nullable Percentaged<IIngredientWithAmount>[] itemResult,
                                                  @Nullable IFluidStack[] fluidInputs,
                                                  @Nullable Percentaged<IFluidStack>[] fluidResult) {
        return create(
                inputs,
                unwrapItemResults(itemResult),
                unwrapChances(itemResult),
                null,
                wrapFluidInputs(fluidInputs),
                wrapFluidResults(fluidResult),
                unwrapFluidChances(fluidResult),
                null
        );
    }

    public <T extends Recipe<?>> void createRecipe(
            IRecipeManager<T> manager,
            String name,
            Function<CTProcessorRecipeWrapper, T> recipeCreator
    ) {
        if(itemInputs.isEmpty() && itemResults.isEmpty() && fluidInputs.isEmpty() && fluidResults.isEmpty()) return;

        CraftTweakerAPI.apply(
                new ActionAddRecipe<>(
                        manager,
                        manager.createHolder(
                                manager.fixRecipeId(name),
                                recipeCreator.apply(this)
                        )
                )
        );
    }

    private static void validateArrayLengths(String label, @Nullable Object[] results, @Nullable int[] chances, @Nullable int[] minStackSizes) {
        if (results == null) {
            if (chances != null || minStackSizes != null) {
                throw new IllegalArgumentException("Cannot set " + label + " metadata without " + label);
            }
            return;
        }

        if (chances != null && chances.length != results.length) {
            throw new IllegalArgumentException("Expected " + results.length + " " + label + " chances but got " + chances.length);
        }

        if (minStackSizes != null && minStackSizes.length != results.length) {
            throw new IllegalArgumentException("Expected " + results.length + " " + label + " min stack sizes but got " + minStackSizes.length);
        }
    }

    private static int getOrDefault(@Nullable int[] values, int index, int defaultValue) {
        return values == null ? defaultValue : values[index];
    }

    private static FluidIngredient toFluidIngredient(CTFluidIngredient ingredient) {
        return ingredient.mapTo(
                iFluidStack -> FluidIngredient.of(iFluidStack.<FluidStack>getImmutableInternal()),
                (fluidTagKey, amount) -> FluidIngredient.tag(fluidTagKey),
                stream -> CompoundFluidIngredient.of(stream.toList())
        );
    }

    private static int getFluidAmount(CTFluidIngredient ingredient) {
        return ingredient.mapTo(
                iFluidStack -> Math.toIntExact(iFluidStack.getAmount()),
                (fluidTagKey, amount) -> amount,
                stream -> stream.findFirst().orElse(0)
        );
    }

    @Nullable
    private static IIngredientWithAmount[] unwrapItemResults(@Nullable Percentaged<IIngredientWithAmount>[] itemResult) {
        if (itemResult == null) {
            return null;
        }

        IIngredientWithAmount[] data = new IIngredientWithAmount[itemResult.length];
        for (int i = 0; i < itemResult.length; i++) {
            data[i] = itemResult[i].getData();
        }
        return data;
    }

    @Nullable
    private static int[] unwrapChances(@Nullable Percentaged<IIngredientWithAmount>[] itemResult) {
        if (itemResult == null) {
            return null;
        }

        int[] chances = new int[itemResult.length];
        for (int i = 0; i < itemResult.length; i++) {
            chances[i] = (int) Math.floor(itemResult[i].getPercentage() * 100D);
        }
        return chances;
    }

    @Nullable
    private static CTFluidIngredient[] wrapFluidInputs(@Nullable IFluidStack[] fluidInputs) {
        if (fluidInputs == null) {
            return null;
        }

        CTFluidIngredient[] wrapped = new CTFluidIngredient[fluidInputs.length];
        for (int i = 0; i < fluidInputs.length; i++) {
            wrapped[i] = fluidInputs[i].asFluidIngredient();
        }
        return wrapped;
    }

    @Nullable
    private static CTFluidIngredient[] wrapFluidResults(@Nullable Percentaged<IFluidStack>[] fluidResult) {
        if (fluidResult == null) {
            return null;
        }

        CTFluidIngredient[] wrapped = new CTFluidIngredient[fluidResult.length];
        for (int i = 0; i < fluidResult.length; i++) {
            wrapped[i] = fluidResult[i].getData().asFluidIngredient();
        }
        return wrapped;
    }

    @Nullable
    private static int[] unwrapFluidChances(@Nullable Percentaged<IFluidStack>[] fluidResult) {
        if (fluidResult == null) {
            return null;
        }

        int[] chances = new int[fluidResult.length];
        for (int i = 0; i < fluidResult.length; i++) {
            chances[i] = (int) Math.floor(fluidResult[i].getPercentage() * 100D);
        }
        return chances;
    }
}
