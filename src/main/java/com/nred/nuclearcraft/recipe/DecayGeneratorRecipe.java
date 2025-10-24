package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.DECAY_GENERATOR_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.DECAY_GENERATOR_RECIPE_TYPE;

public class DecayGeneratorRecipe extends BasicRecipe {
    private final double lifetime;
    private final double power;
    private final double radiation;

    public DecayGeneratorRecipe(SizedIngredient ingredient, SizedIngredient product, double lifetime, double power, double radiation) {
        super(List.of(ingredient), List.of(), List.of(product), List.of());
        this.lifetime = lifetime;
        this.power = power;
        this.radiation = radiation;
    }

    public double getDecayGeneratorLifetime() {
        return lifetime;
    }

    public double getDecayGeneratorPower() {
        return power;
    }

    public double getDecayGeneratorRadiation() {
        return radiation;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DECAY_GENERATOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DECAY_GENERATOR_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<DecayGeneratorRecipe> {
        public static MapCodec<DecayGeneratorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(DecayGeneratorRecipe::getItemIngredient),
                SizedIngredient.FLAT_CODEC.fieldOf("product").forGetter(DecayGeneratorRecipe::getItemProduct),
                Codec.DOUBLE.fieldOf("efficiency").forGetter(DecayGeneratorRecipe::getDecayGeneratorLifetime),
                Codec.DOUBLE.fieldOf("decayFactor").forGetter(DecayGeneratorRecipe::getDecayGeneratorPower),
                Codec.DOUBLE.fieldOf("radiation").forGetter(DecayGeneratorRecipe::getDecayGeneratorRadiation)
        ).apply(inst, DecayGeneratorRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, DecayGeneratorRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedIngredient.STREAM_CODEC, DecayGeneratorRecipe::getItemIngredient,
                SizedIngredient.STREAM_CODEC, DecayGeneratorRecipe::getItemProduct,
                ByteBufCodecs.DOUBLE, DecayGeneratorRecipe::getDecayGeneratorLifetime,
                ByteBufCodecs.DOUBLE, DecayGeneratorRecipe::getDecayGeneratorPower,
                ByteBufCodecs.DOUBLE, DecayGeneratorRecipe::getDecayGeneratorRadiation,
                DecayGeneratorRecipe::new
        );

        @Override
        public MapCodec<DecayGeneratorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DecayGeneratorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}