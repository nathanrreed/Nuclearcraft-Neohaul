package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.RADIATION_BLOCK_PURIFICATION_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.RADIATION_BLOCK_PURIFICATION_RECIPE_TYPE;

public class RadiationBlockPurificationRecipe extends BasicRecipe {
    private final double purificationThreshold;

    public RadiationBlockPurificationRecipe(SizedChanceItemIngredient itemIngredient, SizedChanceItemIngredient itemProduct, double purificationThreshold) {
        super(List.of(itemIngredient), List.of(), List.of(itemProduct), List.of());
        this.purificationThreshold = purificationThreshold;
    }

    public double getPurificationThreshold() {
        return purificationThreshold;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RADIATION_BLOCK_PURIFICATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RADIATION_BLOCK_PURIFICATION_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<RadiationBlockPurificationRecipe> {
        public static MapCodec<RadiationBlockPurificationRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedChanceItemIngredient.FLAT_CODEC.fieldOf("itemIngredient").forGetter(RadiationBlockPurificationRecipe::getItemIngredient),
                SizedChanceItemIngredient.FLAT_CODEC.fieldOf("itemProduct").forGetter(RadiationBlockPurificationRecipe::getItemProduct),
                Codec.DOUBLE.fieldOf("purificationThreshold").forGetter(RadiationBlockPurificationRecipe::getPurificationThreshold)
        ).apply(inst, RadiationBlockPurificationRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, RadiationBlockPurificationRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedChanceItemIngredient.STREAM_CODEC, RadiationBlockPurificationRecipe::getItemIngredient,
                SizedChanceItemIngredient.STREAM_CODEC, RadiationBlockPurificationRecipe::getItemProduct,
                ByteBufCodecs.DOUBLE, RadiationBlockPurificationRecipe::getPurificationThreshold,
                RadiationBlockPurificationRecipe::new
        );

        @Override
        public MapCodec<RadiationBlockPurificationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RadiationBlockPurificationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}