package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public class CollectorSerializer implements RecipeSerializer<CollectorRecipe> {
    private static final MapCodec<CollectorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    ItemStack.OPTIONAL_CODEC.fieldOf("itemResult").forGetter(CollectorRecipe::itemResult),
                    FluidStack.OPTIONAL_CODEC.fieldOf("fluidResult").forGetter(CollectorRecipe::fluidResult),
                    Codec.STRING.fieldOf("level").xmap(MACHINE_LEVEL::valueOf, Enum::name).forGetter(CollectorRecipe::level),
                    Codec.DOUBLE.fieldOf("rate").forGetter(CollectorRecipe::rate)
            ).apply(inst, CollectorRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, CollectorRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, CollectorRecipe::itemResult,
            FluidStack.OPTIONAL_STREAM_CODEC, CollectorRecipe::fluidResult,
            NeoForgeStreamCodecs.enumCodec(MACHINE_LEVEL.class), CollectorRecipe::level,
            ByteBufCodecs.DOUBLE, CollectorRecipe::rate,
            CollectorRecipe::new
    );

    @Override
    public MapCodec<CollectorRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CollectorRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}


