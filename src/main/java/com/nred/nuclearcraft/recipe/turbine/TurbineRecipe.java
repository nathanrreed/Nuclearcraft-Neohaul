package com.nred.nuclearcraft.recipe.turbine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.turbine_spin_up_multiplier_global;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.TURBINE_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.TURBINE_RECIPE_TYPE;

public final class TurbineRecipe extends BasicRecipe {
    private final double power_per_mb;
    private final double expansion_level;
    private final double spin_up_multiplier;
    private final String particle;
    private final double particle_speed_mult;

    public TurbineRecipe(SizedFluidIngredient fluidIngredient, SizedFluidIngredient fluidProduct, double power_per_mb, double expansion_level, double spin_up_multiplier, String particle, double particle_speed_mult) {
        super(List.of(), List.of(fluidIngredient), List.of(), List.of(fluidProduct));
        this.power_per_mb = power_per_mb;
        this.expansion_level = expansion_level;
        this.spin_up_multiplier = spin_up_multiplier;
        this.particle = particle;
        this.particle_speed_mult = particle_speed_mult;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TURBINE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TURBINE_RECIPE_TYPE.get();
    }

    public double getTurbineSpinUpMultiplier() {
        return turbine_spin_up_multiplier_global * spin_up_multiplier;
    }

    public double getTurbinePowerPerMB() {
        return power_per_mb;
    }

    public double getTurbineExpansionLevel() {
        return expansion_level;
    }

    public double getTurbineSpinUpMultiplierRaw() {
        return spin_up_multiplier;
    }

    public String getTurbineParticleEffect() {
        return particle;
    }

    public double getTurbineParticleSpeedMultiplier() {
        return particle_speed_mult;
    }

    public static class Serializer implements RecipeSerializer<TurbineRecipe> {
        private static final MapCodec<TurbineRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedFluidIngredient.FLAT_CODEC.fieldOf("fluidIngredients").forGetter(TurbineRecipe::getFluidIngredient),
                        SizedFluidIngredient.FLAT_CODEC.fieldOf("fluidProducts").forGetter(TurbineRecipe::getFluidProduct),
                        Codec.DOUBLE.fieldOf("power_per_mb").forGetter(TurbineRecipe::getTurbinePowerPerMB),
                        Codec.DOUBLE.fieldOf("expansion_level").forGetter(TurbineRecipe::getTurbineExpansionLevel),
                        Codec.DOUBLE.fieldOf("spin_up_multiplier").forGetter(TurbineRecipe::getTurbineSpinUpMultiplierRaw),
                        Codec.STRING.fieldOf("particle").forGetter(TurbineRecipe::getTurbineParticleEffect),
                        Codec.DOUBLE.fieldOf("particle_speed_mult").forGetter(TurbineRecipe::getTurbineParticleSpeedMultiplier)
                ).apply(inst, TurbineRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, TurbineRecipe> STREAM_CODEC = NeoForgeStreamCodecs.composite(
                SizedFluidIngredient.STREAM_CODEC, TurbineRecipe::getFluidIngredient,
                SizedFluidIngredient.STREAM_CODEC, TurbineRecipe::getFluidProduct,
                ByteBufCodecs.DOUBLE, TurbineRecipe::getTurbinePowerPerMB,
                ByteBufCodecs.DOUBLE, TurbineRecipe::getTurbineExpansionLevel,
                ByteBufCodecs.DOUBLE, TurbineRecipe::getTurbineSpinUpMultiplierRaw,
                ByteBufCodecs.STRING_UTF8, TurbineRecipe::getTurbineParticleEffect,
                ByteBufCodecs.DOUBLE, TurbineRecipe::getTurbineParticleSpeedMultiplier,
                TurbineRecipe::new
        );

        @Override
        public MapCodec<TurbineRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TurbineRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}