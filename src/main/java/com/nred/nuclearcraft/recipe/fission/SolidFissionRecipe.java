package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.StreamCodecsHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

import static com.nred.nuclearcraft.config.Config2.*;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.SOLID_FISSION_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.SOLID_FISSION_RECIPE_TYPE;

public class SolidFissionRecipe extends BasicRecipe {
    private final int time;
    private final int heat;
    private final double efficiency;
    private final int criticality;
    private final double decayFactor;
    private final boolean selfPriming;
    private final double radiation;

    public SolidFissionRecipe(SizedIngredient ingredient, SizedIngredient product, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
        super(List.of(ingredient), List.of(), List.of(product), List.of());
        this.time = time;
        this.heat = heat;
        this.efficiency = efficiency;
        this.criticality = criticality;
        this.decayFactor = decayFactor;
        this.selfPriming = selfPriming;
        this.radiation = radiation;
    }

    public int getFissionFuelTimeRaw() {
        return NCMath.toInt(time);
    }

    public int getFissionFuelTime() {
        return NCMath.toInt(fission_fuel_time_multiplier * time);
    }

    public int getFissionFuelHeatRaw() {
        return NCMath.toInt(heat);
    }

    public int getFissionFuelHeat() {
        return NCMath.toInt(fission_fuel_heat_multiplier * heat);
    }

    public double getFissionFuelEfficiencyRaw() {
        return efficiency;
    }

    public double getFissionFuelEfficiency() {
        return fission_fuel_efficiency_multiplier * efficiency;
    }

    public int getFissionFuelCriticality() {
        return criticality;
    }

    public double getFissionFuelDecayFactor() {
        return decayFactor;
    }

    public boolean getFissionFuelSelfPriming() {
        return selfPriming;
    }

    public double getFissionFuelRadiationRaw() {
        return radiation;
    }

    public double getFissionFuelRadiation() {
        return fission_fuel_radiation_multiplier * radiation;
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
                SizedIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(SolidFissionRecipe::getItemIngredient),
                SizedIngredient.FLAT_CODEC.fieldOf("product").forGetter(SolidFissionRecipe::getItemProduct),
                Codec.INT.fieldOf("time").forGetter(SolidFissionRecipe::getFissionFuelTimeRaw),
                Codec.INT.fieldOf("heat").forGetter(SolidFissionRecipe::getFissionFuelHeatRaw),
                Codec.DOUBLE.fieldOf("efficiency").forGetter(SolidFissionRecipe::getFissionFuelEfficiencyRaw),
                Codec.INT.fieldOf("criticality").forGetter(SolidFissionRecipe::getFissionFuelCriticality),
                Codec.DOUBLE.fieldOf("decayFactor").forGetter(SolidFissionRecipe::getFissionFuelDecayFactor),
                Codec.BOOL.fieldOf("selfPriming").forGetter(SolidFissionRecipe::getFissionFuelSelfPriming),
                Codec.DOUBLE.fieldOf("radiation").forGetter(SolidFissionRecipe::getFissionFuelRadiationRaw)
        ).apply(inst, SolidFissionRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, SolidFissionRecipe> STREAM_CODEC = StreamCodecsHelper.composite(
                SizedIngredient.STREAM_CODEC, SolidFissionRecipe::getItemIngredient,
                SizedIngredient.STREAM_CODEC, SolidFissionRecipe::getItemProduct,
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