package com.nred.nuclearcraft.helpers;

import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeHelpers {
    public static List<@NotNull SizedChanceItemIngredient> ingotDusts(String input1, int count1, String input2, int count2) {
        return List.of(tags(List.of(tag(Tags.Items.DUSTS, input1), tag(Tags.Items.INGOTS, input1)), count1), tags(List.of(tag(Tags.Items.DUSTS, input2), tag(Tags.Items.INGOTS, input2)), count2));
    }

    public static @NotNull SizedChanceItemIngredient ingotDust(String input, int count) {
        return tags(List.of(tag(Tags.Items.DUSTS, input), tag(Tags.Items.INGOTS, input)), count);
    }

    public static @NotNull SizedChanceItemIngredient gemDust(String input, int count) {
        return tags(List.of(tag(Tags.Items.DUSTS, input), tag(Tags.Items.GEMS, input)), count);
    }

    public static SizedChanceItemIngredient tags(List<TagKey<Item>> tags, int count) {
        return new SizedChanceItemIngredient(Ingredient.fromValues(tags.stream().map(Ingredient.TagValue::new)), count);
    }

    public static TagKey<Item> tag(TagKey<Item> tag, String name) {
        return ItemTags.create(tag.location().withSuffix("/" + name));
    }

    public static RecipeOutput tagExists(RecipeOutput recipeOutput, TagKey<Item> tag) {
        return recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tag)));
    }

    @SafeVarargs
    public static RecipeOutput tagsExists(RecipeOutput recipeOutput, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            recipeOutput = recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tag)));
        }
        return recipeOutput;
    }

    public static TagKey<Block> blockTag(TagKey<Block> tag, String name) {
        return BlockTags.create(tag.location().withSuffix("/" + name));
    }

    public static TagKey<Fluid> fluidTag(TagKey<Fluid> tag, String name) {
        return FluidTags.create(tag.location().withSuffix("/" + name));
    }

    public static List<EmiIngredient> removeBarriers(List<EmiIngredient> itemInputs) {
        ArrayList<EmiIngredient> list = new ArrayList<>();
        for (EmiIngredient ingredient : itemInputs) {
            List<EmiStack> stacks = ingredient.getEmiStacks().stream().filter(emiStack -> !emiStack.getItemStack().is(Items.BARRIER)).map(emiStack -> emiStack.setAmount(ingredient.getAmount())).toList();
            if (!stacks.isEmpty()) {
                list.add(EmiIngredient.of(stacks));
            }
        }
        return list;
    }
}
