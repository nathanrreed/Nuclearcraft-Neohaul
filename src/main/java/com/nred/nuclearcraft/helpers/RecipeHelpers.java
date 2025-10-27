package com.nred.nuclearcraft.helpers;

import com.ibm.icu.impl.Pair;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeHelpers {
    public static List<@NotNull SizedIngredient> ingotDusts(String input1, int count1, String input2, int count2) {
        return List.of(tags(List.of(tag(Tags.Items.DUSTS, input1), tag(Tags.Items.INGOTS, input1)), count1), tags(List.of(tag(Tags.Items.DUSTS, input2), tag(Tags.Items.INGOTS, input2)), count2));
    }

    public static @NotNull SizedIngredient ingotDust(String input, int count) {
        return tags(List.of(tag(Tags.Items.DUSTS, input), tag(Tags.Items.INGOTS, input)), count);
    }

    public static @NotNull SizedIngredient gemDust(String input, int count) {
        return tags(List.of(tag(Tags.Items.DUSTS, input), tag(Tags.Items.GEMS, input)), count);
    }

    public static SizedIngredient tags(List<TagKey<Item>> tags, int count) {
        return new SizedIngredient(Ingredient.fromValues(tags.stream().map(Ingredient.TagValue::new)), count);
    }

    public static TagKey<Item> tag(TagKey<Item> tag, String name) {
        return ItemTags.create(tag.location().withSuffix("/" + name));
    }

    public static int probabilityPacker(int probability, int amount) {
        return (probability << 16) | (amount & 0xFFFF);
    }

    public static Pair<Short, Short> probabilityUnpacker(int packed) {
        short probability = (short) (packed >> 16);
        short amount = (short) packed;

        return Pair.of(probability, amount);
    }

    public static List<EmiIngredient> removeBarriers(List<EmiIngredient> itemInputs) {
        ArrayList<EmiIngredient> list = new ArrayList<>();
        for (EmiIngredient ingredient : itemInputs) {
            List<EmiStack> stacks = ingredient.getEmiStacks().stream().filter(emiStack -> !emiStack.getItemStack().is(Items.BARRIER)).toList();
            if (!stacks.isEmpty()) {
                list.add(EmiIngredient.of(stacks));
            }
        }
        return list;
    }
}
