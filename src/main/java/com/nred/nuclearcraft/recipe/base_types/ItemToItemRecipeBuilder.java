package com.nred.nuclearcraft.recipe.base_types;

import com.google.common.base.CaseFormat;
import com.nred.nuclearcraft.recipe.SizedItemIngredient;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemToItemRecipeBuilder implements RecipeBuilder {

    private static final Logger log = LoggerFactory.getLogger(ItemToItemRecipeBuilder.class);
    private final List<SizedItemIngredient> itemInputs = new ArrayList<>();
    private final List<SizedItemIngredient> itemResults = new ArrayList<>();
    private final Class<? extends ItemToItemRecipe> clazz;
    private final double timeModifier;
    private final double powerModifier;
    protected Map<String, Criterion<?>> criteria = new HashMap<>();
    protected String group = null;

    public ItemToItemRecipeBuilder(Class<? extends ItemToItemRecipe> clazz, double timeModifier, double powerModifier) {
        this.clazz = clazz;
        this.timeModifier = timeModifier;
        this.powerModifier = powerModifier;
    }

    public ItemToItemRecipeBuilder addInput(ItemLike input, int amount) {
        itemInputs.add(SizedItemIngredient.of(input, amount));
        return this;
    }

    public ItemToItemRecipeBuilder addInput(TagKey<Item> input, int amount) {
        itemInputs.add(SizedItemIngredient.of(input, amount));
        return this;
    }

    public ItemToItemRecipeBuilder addInput(SizedItemIngredient input) {
        itemInputs.add(input);
        return this;
    }

    public ItemToItemRecipeBuilder addInput(List<SizedItemIngredient> input) {
        itemInputs.addAll(input);
        return this;
    }

    public ItemToItemRecipeBuilder addResult(SizedItemIngredient output) {
        itemResults.add(output);
        return this;
    }

    public ItemToItemRecipeBuilder addResult(List<SizedItemIngredient> output) {
        itemResults.addAll(output);
        return this;
    }

    public ItemToItemRecipeBuilder addResult(ItemLike output, int count) {
        itemResults.add(new SizedItemIngredient(Ingredient.of(output), count));
        return this;
    }

    public ItemToItemRecipeBuilder addResult(TagKey<Item> output, int count) {
        itemResults.add(new SizedItemIngredient(Ingredient.of(output), count));
        return this;
    }

    @Override
    public ItemToItemRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public ItemToItemRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return itemResults.getFirst().getItems()[0].getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation key) {
        key = key.withPrefix(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName()) + "_");
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                .rewards(AdvancementRewards.Builder.recipe(key))
                .requirements(AdvancementRequirements.Strategy.OR);

        try {
            ItemToItemRecipe recipe = clazz.getDeclaredConstructor(List.class, List.class, double.class, double.class).newInstance(this.itemInputs, this.itemResults, this.timeModifier, this.powerModifier);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }
}
