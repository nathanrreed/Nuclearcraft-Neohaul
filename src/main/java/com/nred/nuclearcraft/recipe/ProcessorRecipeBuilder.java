package com.nred.nuclearcraft.recipe;

import com.google.common.base.CaseFormat;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.info.Fluids;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ProcessorRecipeBuilder implements RecipeBuilder {
    private static final Logger log = LoggerFactory.getLogger(ProcessorRecipeBuilder.class);
    private final List<SizedChanceItemIngredient> itemInputs = new ArrayList<>();
    private final List<SizedChanceItemIngredient> itemResults = new ArrayList<>();
    private final List<SizedChanceFluidIngredient> fluidInputs = new ArrayList<>();
    private final List<SizedChanceFluidIngredient> fluidResults = new ArrayList<>();
    private final Class<? extends ProcessorRecipe> clazz;
    private final double timeModifier;
    private final double powerModifier;
    private final double radiation;
    protected Map<String, Criterion<?>> criteria = new HashMap<>();
    protected String group = null;

    public ProcessorRecipeBuilder(Class<? extends ProcessorRecipe> clazz, double timeModifier, double powerModifier, double radiation) {
        this.clazz = clazz;
        this.timeModifier = timeModifier;
        this.powerModifier = powerModifier;
        this.radiation = radiation;
    }

    public ProcessorRecipeBuilder(Class<? extends ProcessorRecipe> clazz, double timeModifier, double powerModifier) {
        this(clazz, timeModifier, powerModifier, 0.0);
    }

    public ProcessorRecipeBuilder addItemInput(ItemLike input, int amount) {
        itemInputs.add(SizedChanceItemIngredient.of(input, amount));
        return this;
    }

    public ProcessorRecipeBuilder addItemInput(TagKey<Item> input, int amount) {
        itemInputs.add(SizedChanceItemIngredient.of(input, amount));
        return this;
    }

    public ProcessorRecipeBuilder addItemInput(SizedChanceItemIngredient input) {
        itemInputs.add(input);
        return this;
    }

    public ProcessorRecipeBuilder addItemInput(Ingredient input, int amount) {
        itemInputs.add(new SizedChanceItemIngredient(input, amount));
        return this;
    }

    public ProcessorRecipeBuilder addItemInput(List<SizedChanceItemIngredient> input) {
        itemInputs.addAll(input);
        return this;
    }

    public ProcessorRecipeBuilder addItemResult(SizedChanceItemIngredient output) {
        itemResults.add(output);
        return this;
    }

    public ProcessorRecipeBuilder addItemResult(List<SizedChanceItemIngredient> output) {
        itemResults.addAll(output);
        return this;
    }

    public ProcessorRecipeBuilder addItemResult(ItemLike output, int count) {
        itemResults.add(new SizedChanceItemIngredient(Ingredient.of(output), count));
        return this;
    }

    public ProcessorRecipeBuilder addItemResult(ItemLike output, int chancePercent, int count) {
        itemResults.add(new SizedChanceItemIngredient(Ingredient.of(output), count, chancePercent, 0));
        return this;
    }

    public ProcessorRecipeBuilder addItemResult(TagKey<Item> output, int count) {
        itemResults.add(new SizedChanceItemIngredient(Ingredient.of(output), count));
        return this;
    }

    public ProcessorRecipeBuilder addFluidInput(Fluid input, int amount) {
        fluidInputs.add(SizedChanceFluidIngredient.of(input, amount));
        return this;
    }

    public ProcessorRecipeBuilder addFluidInput(TagKey<Fluid> input, int amount) {
        fluidInputs.add(SizedChanceFluidIngredient.of(input, amount));
        return this;
    }

    public ProcessorRecipeBuilder addFluidInput(Fluids input, int amount) {
        fluidInputs.add(Fluids.sizedIngredient(input, 100, amount));
        return this;
    }

    public ProcessorRecipeBuilder addFluidResult(Fluids output, int amount) {
        fluidResults.add(Fluids.sizedIngredient(output, 100, amount));
        return this;
    }

    public ProcessorRecipeBuilder addFluidResult(Fluids output, int chancePercent, int amount) {
        fluidResults.add(Fluids.sizedIngredient(output, chancePercent, amount));
        return this;
    }

    public ProcessorRecipeBuilder addFluidResult(Fluid output, int count) {
        fluidResults.add(new SizedChanceFluidIngredient(FluidIngredient.of(output), 100, count, 0));
        return this;
    }

    public ProcessorRecipeBuilder addFluidResult(TagKey<Fluid> output, int count) {
        fluidResults.add(new SizedChanceFluidIngredient(FluidIngredient.tag(output), 100, count, 0));
        return this;
    }

    @Override
    public ProcessorRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public ProcessorRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return itemResults.isEmpty() ? Items.AIR : itemResults.getFirst().getItemsRaw()[0].getItem();
    }

    public Fluid getFluidResult() {
        return fluidResults.getFirst().getFluidsRaw()[0].getFluid();
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        this.save(recipeOutput, getDefaultRecipeId());
    }

    @Override
    public void save(RecipeOutput recipeOutput, String id) {
        ResourceLocation resourcelocation = getDefaultRecipeId();
        ResourceLocation resourcelocation1 = ResourceLocation.parse(id);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + id + " should remove its 'save' argument as it is equal to default one");
        } else {
            this.save(recipeOutput, resourcelocation1);
        }
    }

    private ResourceLocation getDefaultRecipeId() {
        return itemResults.isEmpty() ? SimpleRecipeBuilder.getDefaultRecipeId(fluidInputs, fluidResults) : RecipeBuilder.getDefaultRecipeId(getResult());
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation key) {
        key = ncLoc(key.withPrefix(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName()).replace("recipe", "")).getPath());
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                .rewards(AdvancementRewards.Builder.recipe(key))
                .requirements(AdvancementRequirements.Strategy.OR);

        try {
            ProcessorRecipe recipe = clazz.getDeclaredConstructor(List.class, List.class, List.class, List.class, double.class, double.class, double.class).newInstance(this.itemInputs, this.itemResults, this.fluidInputs, this.fluidResults, this.timeModifier, this.powerModifier, this.radiation);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }
}
