package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.StreamCodecsHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.SALT_FISSION_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.SALT_FISSION_RECIPE_TYPE;

public class SaltFissionRecipe extends BasicRecipe {
    private final int time;
    private final int heat;
    private final double efficiency;
    private final int criticality;
    private final double decayFactor;
    private final boolean selfPriming;
    private final double radiation;

    public SaltFissionRecipe(SizedChanceFluidIngredient ingredient, SizedChanceFluidIngredient product, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
        super(List.of(), List.of(ingredient), List.of(), List.of(product));
        this.time = time;
        this.heat = heat;
        this.efficiency = efficiency;
        this.criticality = criticality;
        this.decayFactor = decayFactor;
        this.selfPriming = selfPriming;
        this.radiation = radiation;
    }

    public int getSaltFissionFuelTimeRaw() {
        return NCMath.toInt(time);
    }

    public int getSaltFissionFuelTime() {
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
        return SALT_FISSION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SALT_FISSION_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<SaltFissionRecipe> {
        public static MapCodec<SaltFissionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(SaltFissionRecipe::getFluidIngredient),
                SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("product").forGetter(SaltFissionRecipe::getFluidProduct),
                Codec.INT.fieldOf("time").forGetter(SaltFissionRecipe::getSaltFissionFuelTimeRaw),
                Codec.INT.fieldOf("heat").forGetter(SaltFissionRecipe::getFissionFuelHeatRaw),
                Codec.DOUBLE.fieldOf("efficiency").forGetter(SaltFissionRecipe::getFissionFuelEfficiencyRaw),
                Codec.INT.fieldOf("criticality").forGetter(SaltFissionRecipe::getFissionFuelCriticality),
                Codec.DOUBLE.fieldOf("decayFactor").forGetter(SaltFissionRecipe::getFissionFuelDecayFactor),
                Codec.BOOL.fieldOf("selfPriming").forGetter(SaltFissionRecipe::getFissionFuelSelfPriming),
                Codec.DOUBLE.fieldOf("radiation").forGetter(SaltFissionRecipe::getFissionFuelRadiationRaw)
        ).apply(inst, SaltFissionRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, SaltFissionRecipe> STREAM_CODEC = StreamCodecsHelper.composite(
                SizedChanceFluidIngredient.STREAM_CODEC, SaltFissionRecipe::getFluidIngredient,
                SizedChanceFluidIngredient.STREAM_CODEC, SaltFissionRecipe::getFluidProduct,
                ByteBufCodecs.INT, SaltFissionRecipe::getSaltFissionFuelTimeRaw,
                ByteBufCodecs.INT, SaltFissionRecipe::getFissionFuelHeatRaw,
                ByteBufCodecs.DOUBLE, SaltFissionRecipe::getFissionFuelEfficiencyRaw,
                ByteBufCodecs.INT, SaltFissionRecipe::getFissionFuelCriticality,
                ByteBufCodecs.DOUBLE, SaltFissionRecipe::getFissionFuelDecayFactor,
                ByteBufCodecs.BOOL, SaltFissionRecipe::getFissionFuelSelfPriming,
                ByteBufCodecs.DOUBLE, SaltFissionRecipe::getFissionFuelRadiationRaw,
                SaltFissionRecipe::new
        );

        @Override
        public MapCodec<SaltFissionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SaltFissionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}