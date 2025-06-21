package com.nred.nuclearcraft.recipe.turbine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public class TurbineRecipeSerializer implements RecipeSerializer<TurbineRecipe> {

    private static final MapCodec<TurbineRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    SizedFluidIngredient.FLAT_CODEC.fieldOf("fluidInput").forGetter(TurbineRecipe::fluidInput),
                    SizedFluidIngredient.FLAT_CODEC.fieldOf("fluidResult").forGetter(TurbineRecipe::fluidResult),
                    Codec.DOUBLE.fieldOf("power_per_mb").forGetter(TurbineRecipe::power_per_mb),
                    Codec.DOUBLE.fieldOf("expansion_level").forGetter(TurbineRecipe::expansion_level),
                    Codec.DOUBLE.fieldOf("spin_up_multiplier").forGetter(TurbineRecipe::spin_up_multiplier),
                    ParticleTypes.CODEC.fieldOf("particle").forGetter(TurbineRecipe::particle),
                    Codec.DOUBLE.fieldOf("particle_speed_mult").forGetter(TurbineRecipe::particle_speed_mult)
            ).apply(inst, TurbineRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, TurbineRecipe> STREAM_CODEC = NeoForgeStreamCodecs.composite(
            SizedFluidIngredient.STREAM_CODEC, TurbineRecipe::fluidInput,
            SizedFluidIngredient.STREAM_CODEC, TurbineRecipe::fluidResult,
            ByteBufCodecs.DOUBLE, TurbineRecipe::power_per_mb,
            ByteBufCodecs.DOUBLE, TurbineRecipe::expansion_level,
            ByteBufCodecs.DOUBLE, TurbineRecipe::spin_up_multiplier,
            ParticleTypes.STREAM_CODEC, TurbineRecipe::particle,
            ByteBufCodecs.DOUBLE, TurbineRecipe::particle_speed_mult,
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


