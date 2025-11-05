package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;

import java.util.List;

public interface IRecipe {
    List<SizedChanceItemIngredient> getItemIngredients();

    List<SizedChanceFluidIngredient> getFluidIngredients();

    List<SizedChanceItemIngredient> getItemProducts();

    List<SizedChanceFluidIngredient> getFluidProducts();

    default SizedChanceItemIngredient getItemIngredient() {
        return getItemIngredients().getFirst();
    }

    default SizedChanceItemIngredient getItemProduct() {
        return getItemProducts().getFirst();
    }

    default SizedChanceFluidIngredient getFluidIngredient() {
        return getFluidIngredients().getFirst();
    }

    default SizedChanceFluidIngredient getFluidProduct() {
        return getFluidProducts().getFirst();
    }
}