package com.nred.nuclearcraft.compat.recipe_viewer.info.builder;

import com.nred.nuclearcraft.compat.recipe_viewer.info.RecipeViewerSimpleCategoryInfo;
import com.nred.nuclearcraft.handler.MenuInfoBuilder;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class RecipeViewerSimpleCategoryInfoBuilder<RECIPE extends BasicRecipe> extends MenuInfoBuilder<RecipeViewerSimpleCategoryInfoBuilder<RECIPE>> {
    public final List<? extends ItemLike> crafters;

    public RecipeViewerSimpleCategoryInfoBuilder(String name, List<? extends ItemLike> crafters) {
        super(name);

        this.crafters = crafters;
    }

    public RecipeViewerSimpleCategoryInfo<RECIPE> buildCategoryInfo() {
        return new RecipeViewerSimpleCategoryInfo<>(this);
    }
}