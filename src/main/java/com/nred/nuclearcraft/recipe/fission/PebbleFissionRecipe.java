package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.util.StreamCodecsHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.PEBBLE_FISSION_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PEBBLE_FISSION_RECIPE_TYPE;

public class PebbleFissionRecipe extends ItemFissionRecipe {
    public PebbleFissionRecipe(SizedChanceItemIngredient ingredient, SizedChanceItemIngredient product, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
        super(ingredient, product, time, heat, efficiency, criticality, decayFactor, selfPriming, radiation);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PEBBLE_FISSION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PEBBLE_FISSION_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PebbleFissionRecipe> {
        public static MapCodec<PebbleFissionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedChanceItemIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(PebbleFissionRecipe::getItemIngredient),
                SizedChanceItemIngredient.FLAT_CODEC.fieldOf("product").forGetter(PebbleFissionRecipe::getItemProduct),
                Codec.INT.fieldOf("time").forGetter(PebbleFissionRecipe::getFissionFuelTimeRaw),
                Codec.INT.fieldOf("heat").forGetter(PebbleFissionRecipe::getFissionFuelHeatRaw),
                Codec.DOUBLE.fieldOf("efficiency").forGetter(PebbleFissionRecipe::getFissionFuelEfficiencyRaw),
                Codec.INT.fieldOf("criticality").forGetter(PebbleFissionRecipe::getFissionFuelCriticality),
                Codec.DOUBLE.fieldOf("decayFactor").forGetter(PebbleFissionRecipe::getFissionFuelDecayFactor),
                Codec.BOOL.fieldOf("selfPriming").forGetter(PebbleFissionRecipe::getFissionFuelSelfPriming),
                Codec.DOUBLE.fieldOf("radiation").forGetter(PebbleFissionRecipe::getFissionFuelRadiationRaw)
        ).apply(inst, PebbleFissionRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, PebbleFissionRecipe> STREAM_CODEC = StreamCodecsHelper.composite(
                SizedChanceItemIngredient.STREAM_CODEC, PebbleFissionRecipe::getItemIngredient,
                SizedChanceItemIngredient.STREAM_CODEC, PebbleFissionRecipe::getItemProduct,
                ByteBufCodecs.INT, PebbleFissionRecipe::getFissionFuelTimeRaw,
                ByteBufCodecs.INT, PebbleFissionRecipe::getFissionFuelHeatRaw,
                ByteBufCodecs.DOUBLE, PebbleFissionRecipe::getFissionFuelEfficiencyRaw,
                ByteBufCodecs.INT, PebbleFissionRecipe::getFissionFuelCriticality,
                ByteBufCodecs.DOUBLE, PebbleFissionRecipe::getFissionFuelDecayFactor,
                ByteBufCodecs.BOOL, PebbleFissionRecipe::getFissionFuelSelfPriming,
                ByteBufCodecs.DOUBLE, PebbleFissionRecipe::getFissionFuelRadiationRaw,
                PebbleFissionRecipe::new
        );

        @Override
        public MapCodec<PebbleFissionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PebbleFissionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}