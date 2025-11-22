package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.RADIATION_BLOCK_MUTATION_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.RADIATION_BLOCK_MUTATION_RECIPE_TYPE;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

public class RadiationBlockMutationRecipe extends BasicRecipe {
    private final Ingredient itemProduct;
    private final double mutationThreshold;

    public RadiationBlockMutationRecipe(List<SizedChanceItemIngredient> itemIngredients, Ingredient itemProduct, double mutationThreshold) {
        super(itemIngredients, List.of(), List.of(), List.of());
        this.itemProduct = itemProduct;
        this.mutationThreshold = mutationThreshold;
    }

    public double getBlockMutationThreshold() {
        return mutationThreshold;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RADIATION_BLOCK_MUTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RADIATION_BLOCK_MUTATION_RECIPE_TYPE.get();
    }

    @Override
    public SizedChanceItemIngredient getItemProduct() {
        return new SizedChanceItemIngredient(getItemIngredientProduct(), 1);
    }

    public Ingredient getItemIngredientProduct() {
        return itemProduct;
    }

    public static class Serializer implements RecipeSerializer<RadiationBlockMutationRecipe> {
        public static MapCodec<RadiationBlockMutationRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedChanceItemIngredient.FLAT_CODEC.listOf().fieldOf("itemIngredient").forGetter(RadiationBlockMutationRecipe::getItemIngredients),
                Ingredient.CODEC.fieldOf("itemProduct").forGetter(RadiationBlockMutationRecipe::getItemIngredientProduct),
                Codec.DOUBLE.fieldOf("mutationThreshold").forGetter(RadiationBlockMutationRecipe::getBlockMutationThreshold)
        ).apply(inst, RadiationBlockMutationRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, RadiationBlockMutationRecipe> STREAM_CODEC = StreamCodec.composite(
                SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, RadiationBlockMutationRecipe::getItemIngredients,
                Ingredient.CONTENTS_STREAM_CODEC, RadiationBlockMutationRecipe::getItemIngredientProduct,
                ByteBufCodecs.DOUBLE, RadiationBlockMutationRecipe::getBlockMutationThreshold,
                RadiationBlockMutationRecipe::new
        );

        @Override
        public MapCodec<RadiationBlockMutationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RadiationBlockMutationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}