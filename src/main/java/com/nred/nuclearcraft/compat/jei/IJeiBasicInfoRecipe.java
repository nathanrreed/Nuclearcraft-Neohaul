package com.nred.nuclearcraft.compat.jei;

import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiBasicInfoRecipe {
    @Unmodifiable
    ITypedIngredient<?> getIngredient();

    Component getTooltip();
}