package com.nred.nuclearcraft.compat.jei;

import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Unmodifiable;

import java.util.function.Function;

public class JeiBasicInfoRecipe<T> implements IJeiBasicInfoRecipe {
    private final ITypedIngredient<T> ingredient;
    private Function<T, Component> stackTooltip;

    public static <T>IJeiBasicInfoRecipe create(IIngredientManager ingredientManager, T ingredient, IIngredientType<T> type, Function<T, Component> stackTooltip) {
        return new JeiBasicInfoRecipe<>(ingredientManager, ingredient, type, stackTooltip);
    }

    private JeiBasicInfoRecipe(IIngredientManager ingredientManager, T ingredient, IIngredientType<T> type, Function<T, Component> stackTooltip) {
        this(ingredientManager.createTypedIngredient(type, ingredient, true).get());
        this.stackTooltip = stackTooltip;
    }

    private JeiBasicInfoRecipe(ITypedIngredient<T> ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public @Unmodifiable ITypedIngredient<T> getIngredient() {
        return ingredient;
    }

    @Override
    public Component getTooltip() {
        if (stackTooltip != null) {
            return stackTooltip.apply(getIngredient().getIngredient());
        }
        return null;
    }
}