package com.nred.nuclearcraft.recipe.base_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.SizedItemIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class ItemToItemSerializer implements RecipeSerializer<ItemToItemRecipe> {
    private final Class<? extends ItemToItemRecipe> clazz;

    public ItemToItemSerializer(Class<? extends ItemToItemRecipe> clazz) {
        this.clazz = clazz;
    }

    @Override
    public MapCodec<ItemToItemRecipe> codec() {
        return RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedItemIngredient.FLAT_CODEC.listOf().fieldOf("itemInputs").forGetter(ItemToItemRecipe::getItemInputs),
                        SizedItemIngredient.FLAT_CODEC.listOf().fieldOf("itemResults").forGetter(ItemToItemRecipe::getItemResults),
                        Codec.DOUBLE.fieldOf("timeModifier").forGetter(ItemToItemRecipe::getTimeModifier),
                        Codec.DOUBLE.fieldOf("powerModifier").forGetter(ItemToItemRecipe::getPowerModifier)
                ).apply(inst, ((sizedItemIngredients, sizedItemIngredients2, aDouble, aDouble2) -> {
                    try {
                        return clazz.getDeclaredConstructor(List.class, List.class, double.class, double.class).newInstance(sizedItemIngredients, sizedItemIngredients2, aDouble, aDouble2);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })));
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ItemToItemRecipe> streamCodec() {
        return StreamCodec.composite(
                SizedItemIngredient.LIST_STREAM_CODEC, ItemToItemRecipe::getItemInputs,
                SizedItemIngredient.LIST_STREAM_CODEC, ItemToItemRecipe::getItemResults,
                ByteBufCodecs.DOUBLE, ItemToItemRecipe::getTimeModifier,
                ByteBufCodecs.DOUBLE, ItemToItemRecipe::getPowerModifier,
                ((sizedItemIngredients, sizedItemIngredients2, aDouble, aDouble2) -> {
                    try {
                        return clazz.getDeclaredConstructor(List.class, List.class, double.class, double.class).newInstance(sizedItemIngredients, sizedItemIngredients2, aDouble, aDouble2);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }
}


