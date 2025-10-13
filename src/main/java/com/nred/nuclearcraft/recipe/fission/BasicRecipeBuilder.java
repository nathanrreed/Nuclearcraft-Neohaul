package com.nred.nuclearcraft.recipe.fission;

import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class BasicRecipeBuilder extends SimpleRecipeBuilder {
    private final BasicRecipe recipe;

    public BasicRecipeBuilder(BasicRecipe recipe) {
        super(recipe.getItemProducts().stream().findFirst().map(i -> Arrays.stream(i.getItems()).findFirst()).orElse(Optional.of(ItemStack.EMPTY)).get());
        this.recipe = recipe;
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
        return recipe.itemIngredients.isEmpty() ? getDefaultRecipeId(recipe.fluidIngredients, recipe.fluidProducts) : RecipeBuilder.getDefaultRecipeId(getResult());
    }

    public static ResourceLocation getDefaultRecipeId(List<SizedFluidIngredient> inputs, List<SizedFluidIngredient> outputs) {
        return ncLoc((outputs.stream().map(ProcessorRecipeBuilder::getKey).reduce("", (string, fluid) -> string + "_" + fluid) + "_from_" + inputs.stream().map(ProcessorRecipeBuilder::getKey).reduce("", (string, fluid) -> string + "_" + fluid)).replaceAll("__", "_").replaceFirst("^_", ""));
    }

    public static ResourceLocation getDefaultRecipeId(SizedFluidIngredient input, SizedFluidIngredient output) {
        return ncLoc((getKey(output) + "_from_" + getKey(input)).replaceAll("__", "_").replaceFirst("^_", ""));
    }

    public static String getKey(SizedFluidIngredient ingredient) {
        try {
            return BuiltInRegistries.FLUID.getKey(ingredient.getFluids()[0].getFluid()).getPath();
        } catch (ArrayIndexOutOfBoundsException e) {
            if (ingredient.ingredient() instanceof TagFluidIngredient tagged)
                return tagged.tag().location().getPath();
            throw new RuntimeException("no ingredients");
        }
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