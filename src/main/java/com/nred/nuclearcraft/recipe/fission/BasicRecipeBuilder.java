package com.nred.nuclearcraft.recipe.fission;

import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Optional;

public class BasicRecipeBuilder<T extends BasicRecipe> extends SimpleRecipeBuilder {
    private final T recipe;

    public BasicRecipeBuilder(T recipe) {
        super(recipe.getItemProducts().stream().findFirst().map(i -> Arrays.stream(i.getItems()).findFirst()).orElse(Optional.of(ItemStack.EMPTY)).get());
        this.recipe = recipe;
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        this.save(recipeOutput, getDefaultRecipeId());
    }

    @Override
    public void save(RecipeOutput recipeOutput, String append) {
        this.save(recipeOutput, getDefaultRecipeId(append));
    }

    private ResourceLocation getDefaultRecipeId(String append) {
        return recipe.itemIngredients.isEmpty() ? getDefaultRecipeId(recipe.fluidIngredients, recipe.fluidProducts, append) : RecipeBuilder.getDefaultRecipeId(getResult());
    }

    private ResourceLocation getDefaultRecipeId() {
        return getDefaultRecipeId("");
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation key) {
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                .rewards(AdvancementRewards.Builder.recipe(key))
                .requirements(AdvancementRequirements.Strategy.OR);

        output.accept(key, this.recipe, advancement.build(key.withPrefix("recipes/")));
    }
}