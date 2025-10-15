package com.nred.nuclearcraft.recipe;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public abstract class SimpleRecipeBuilder implements RecipeBuilder {
    protected ItemStack result;
    protected Map<String, Criterion<?>> criteria = new HashMap<>();
    protected String group = null;

    public SimpleRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    @Override
    public SimpleRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public SimpleRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    public static ResourceLocation getDefaultRecipeId(List<SizedFluidIngredient> inputs, List<SizedFluidIngredient> outputs, String append) {
        return ncLoc((outputs.stream().map(SimpleRecipeBuilder::getKey).reduce("", (string, fluid) -> string + "_" + fluid) + "_from_" + inputs.stream().map(SimpleRecipeBuilder::getKey).reduce("", (string, fluid) -> string + "_" + fluid)).replaceAll("__", "_").replaceFirst("^_", "") + append);
    }

    public static ResourceLocation getDefaultRecipeId(List<SizedFluidIngredient> inputs, List<SizedFluidIngredient> outputs) {
        return getDefaultRecipeId(inputs, outputs, "");
    }

    public static ResourceLocation getDefaultRecipeId(SizedFluidIngredient input, SizedFluidIngredient output) {
        return getDefaultRecipeId(input, output, "");
    }

    public static ResourceLocation getDefaultRecipeId(SizedFluidIngredient input, SizedFluidIngredient output, String append) {
        return ncLoc((getKey(output) + "_from_" + getKey(input)).replaceAll("__", "_").replaceFirst("^_", "") + append);
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
}