package com.nred.nuclearcraft.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.ELECTROLYZER_CATHODE_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.ELECTROLYZER_CATHODE_RECIPE_TYPE;

public class ElectrolyzerCathodeRecipe extends BasicRecipe {
    private final Ingredient block;
    private final double efficiency;

    public ElectrolyzerCathodeRecipe(Ingredient block, double efficiency) {
        super(List.of(new SizedChanceItemIngredient(block, 1)), List.of(), List.of(), List.of());
        this.block = block;
        this.efficiency = efficiency;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ELECTROLYZER_CATHODE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ELECTROLYZER_CATHODE_RECIPE_TYPE.get();
    }

    public double getElectrolyzerElectrodeEfficiency() {
        return efficiency;
    }

    public Ingredient block() {
        return block;
    }

    public static class Serializer implements RecipeSerializer<ElectrolyzerCathodeRecipe> {
        private static final MapCodec<ElectrolyzerCathodeRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Ingredient.CODEC.fieldOf("block").forGetter(ElectrolyzerCathodeRecipe::block),
                        Codec.DOUBLE.fieldOf("efficiency").forGetter(ElectrolyzerCathodeRecipe::getElectrolyzerElectrodeEfficiency)
                ).apply(inst, ElectrolyzerCathodeRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, ElectrolyzerCathodeRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, ElectrolyzerCathodeRecipe::block,
                ByteBufCodecs.DOUBLE, ElectrolyzerCathodeRecipe::getElectrolyzerElectrodeEfficiency,
                ElectrolyzerCathodeRecipe::new
        );

        @Override
        public MapCodec<ElectrolyzerCathodeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ElectrolyzerCathodeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}