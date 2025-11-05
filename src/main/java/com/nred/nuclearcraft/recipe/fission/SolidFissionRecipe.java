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

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.SOLID_FISSION_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.SOLID_FISSION_RECIPE_TYPE;

public class SolidFissionRecipe extends ItemFissionRecipe {
    public SolidFissionRecipe(SizedChanceItemIngredient ingredient, SizedChanceItemIngredient product, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
        super(ingredient, product, time, heat, efficiency, criticality, decayFactor, selfPriming, radiation);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SOLID_FISSION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SOLID_FISSION_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<SolidFissionRecipe> {
        public static MapCodec<SolidFissionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedChanceItemIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(SolidFissionRecipe::getItemIngredient),
                SizedChanceItemIngredient.FLAT_CODEC.fieldOf("product").forGetter(SolidFissionRecipe::getItemProduct),
                Codec.INT.fieldOf("time").forGetter(SolidFissionRecipe::getFissionFuelTimeRaw),
                Codec.INT.fieldOf("heat").forGetter(SolidFissionRecipe::getFissionFuelHeatRaw),
                Codec.DOUBLE.fieldOf("efficiency").forGetter(SolidFissionRecipe::getFissionFuelEfficiencyRaw),
                Codec.INT.fieldOf("criticality").forGetter(SolidFissionRecipe::getFissionFuelCriticality),
                Codec.DOUBLE.fieldOf("decayFactor").forGetter(SolidFissionRecipe::getFissionFuelDecayFactor),
                Codec.BOOL.fieldOf("selfPriming").forGetter(SolidFissionRecipe::getFissionFuelSelfPriming),
                Codec.DOUBLE.fieldOf("radiation").forGetter(SolidFissionRecipe::getFissionFuelRadiationRaw)
        ).apply(inst, SolidFissionRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, SolidFissionRecipe> STREAM_CODEC = StreamCodecsHelper.composite(
                SizedChanceItemIngredient.STREAM_CODEC, SolidFissionRecipe::getItemIngredient,
                SizedChanceItemIngredient.STREAM_CODEC, SolidFissionRecipe::getItemProduct,
                ByteBufCodecs.INT, SolidFissionRecipe::getFissionFuelTimeRaw,
                ByteBufCodecs.INT, SolidFissionRecipe::getFissionFuelHeatRaw,
                ByteBufCodecs.DOUBLE, SolidFissionRecipe::getFissionFuelEfficiencyRaw,
                ByteBufCodecs.INT, SolidFissionRecipe::getFissionFuelCriticality,
                ByteBufCodecs.DOUBLE, SolidFissionRecipe::getFissionFuelDecayFactor,
                ByteBufCodecs.BOOL, SolidFissionRecipe::getFissionFuelSelfPriming,
                ByteBufCodecs.DOUBLE, SolidFissionRecipe::getFissionFuelRadiationRaw,
                SolidFissionRecipe::new
        );

        @Override
        public MapCodec<SolidFissionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SolidFissionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}