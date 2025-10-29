package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Arrays;
import java.util.List;

import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

public class BasicRecipe implements IRecipe, Recipe<BasicRecipeInput> {
    public List<SizedIngredient> itemIngredients, itemProducts;
    public List<SizedFluidIngredient> fluidIngredients, fluidProducts;

    public BasicRecipe(List<SizedIngredient> itemIngredients, List<SizedFluidIngredient> fluidIngredients, List<SizedIngredient> itemProducts, List<SizedFluidIngredient> fluidProducts) {
        this.itemIngredients = itemIngredients;
        this.fluidIngredients = fluidIngredients;
        this.itemProducts = itemProducts;
        this.fluidProducts = fluidProducts;
    }

    @Override
    public List<SizedIngredient> getItemIngredients() {
        return itemIngredients;
    }

    @Override
    public List<SizedFluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    @Override
    public List<SizedIngredient> getItemProducts() {
        return itemProducts;
    }

    @Override
    public List<SizedFluidIngredient> getFluidProducts() {
        return fluidProducts;
    }

    public static MapCodec<BasicRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemIngredients").forGetter(BasicRecipe::getItemIngredients),
            SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidIngredients").forGetter(BasicRecipe::getFluidIngredients),
            SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemProducts").forGetter(BasicRecipe::getItemProducts),
            SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidProducts").forGetter(BasicRecipe::getFluidProducts)
    ).apply(inst, BasicRecipe::new));

    public static StreamCodec<RegistryFriendlyByteBuf, BasicRecipe> STREAM_CODEC = StreamCodec.composite(
            SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, BasicRecipe::getItemIngredients,
            SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, BasicRecipe::getFluidIngredients,
            SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, BasicRecipe::getItemProducts,
            SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, BasicRecipe::getFluidProducts,
            BasicRecipe::new
    );

    @Override
    public boolean matches(BasicRecipeInput input, Level level) {
        return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, input.itemIngredients(), input.fluidIngredients()).isMatch;
    }

    @Override
    public ItemStack assemble(BasicRecipeInput input, HolderLookup.Provider registries) {
        return itemProducts.isEmpty() ? ItemStack.EMPTY : Arrays.stream(itemProducts.getFirst().getItems()).findFirst().orElse(ItemStack.EMPTY).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return itemProducts.isEmpty() ? ItemStack.EMPTY : Arrays.stream(itemProducts.getFirst().getItems()).findFirst().orElse(ItemStack.EMPTY).copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        throw new RuntimeException("Basic Recipe Serializer Used!");
    }

    @Override
    public RecipeType<?> getType() {
        throw new RuntimeException("Basic Recipe Type Used!");
    }
}
