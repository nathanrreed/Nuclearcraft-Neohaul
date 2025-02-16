package com.nred.nuclearcraft.recipe.base_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ProcessorRecipeSerializer implements RecipeSerializer<ProcessorRecipe> {
    private final Class<? extends ProcessorRecipe> clazz;

    public ProcessorRecipeSerializer(Class<? extends ProcessorRecipe> clazz) {
        this.clazz = clazz;
    }

    @Override
    public MapCodec<ProcessorRecipe> codec() {
        return RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemInputs").forGetter(ProcessorRecipe::getItemInputs),
                        SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemResults").forGetter(ProcessorRecipe::getItemResults),
                        SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidInputs").forGetter(ProcessorRecipe::getFluidInputs),
                        SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidResults").forGetter(ProcessorRecipe::getFluidResults),
                        Codec.DOUBLE.fieldOf("timeModifier").forGetter(ProcessorRecipe::getTimeModifier),
                        Codec.DOUBLE.fieldOf("powerModifier").forGetter(ProcessorRecipe::getPowerModifier)
                ).apply(inst, ((itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier) -> {
                    try {
                        return clazz.getDeclaredConstructor(List.class, List.class, List.class, List.class, double.class, double.class).newInstance(itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })));
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ProcessorRecipe> streamCodec() {
        return StreamCodec.composite(
                ITEM_LIST_STREAM_CODEC, ProcessorRecipe::getItemInputs,
                ITEM_LIST_STREAM_CODEC, ProcessorRecipe::getItemResults,
                FLUID_LIST_STREAM_CODEC, ProcessorRecipe::getFluidInputs,
                FLUID_LIST_STREAM_CODEC, ProcessorRecipe::getFluidResults,
                ByteBufCodecs.DOUBLE, ProcessorRecipe::getTimeModifier,
                ByteBufCodecs.DOUBLE, ProcessorRecipe::getPowerModifier,
                ((itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier) -> {
                    try {
                        return clazz.getDeclaredConstructor(List.class, List.class, List.class, List.class, double.class, double.class).newInstance(itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, List<SizedFluidIngredient>> FLUID_LIST_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull List<SizedFluidIngredient> decode(RegistryFriendlyByteBuf buffer) {
            return Arrays.stream(buffer.readArray(SizedFluidIngredient[]::new, buf -> SizedFluidIngredient.STREAM_CODEC.decode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE)))).toList();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, List<SizedFluidIngredient> value) {
            buffer.writeArray(value.toArray(), (buf, ing) -> SizedFluidIngredient.STREAM_CODEC.encode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE), ((SizedFluidIngredient) ing)));
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, List<SizedIngredient>> ITEM_LIST_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull List<SizedIngredient> decode(RegistryFriendlyByteBuf buffer) {
            return Arrays.stream(buffer.readArray(SizedIngredient[]::new, buf -> SizedIngredient.STREAM_CODEC.decode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE)))).toList();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, List<SizedIngredient> value) {
            buffer.writeArray(value.toArray(), (buf, ing) -> SizedIngredient.STREAM_CODEC.encode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE), ((SizedIngredient) ing)));
        }
    };
}


