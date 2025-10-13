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

import java.util.List;

import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

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
                SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipe::getItemInputs,
                SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipe::getItemResults,
                SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipe::getFluidInputs,
                SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipe::getFluidResults,
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
}


