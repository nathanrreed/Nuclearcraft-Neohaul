package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_MODERATOR_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_MODERATOR_RECIPE_TYPE;

public class FissionModeratorRecipe extends BasicRecipe {
    private final Ingredient moderator;
    private final int fluxFactor;
    private final double efficiency;

    public FissionModeratorRecipe(Ingredient moderator, int fluxFactor, double efficiency) {
        super(List.of(new SizedIngredient(moderator, 1)), List.of(), List.of(), List.of());
        this.moderator = moderator;
        this.fluxFactor = fluxFactor;
        this.efficiency = efficiency;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FISSION_MODERATOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return FISSION_MODERATOR_RECIPE_TYPE.get();
    }

    public int getFissionModeratorFluxFactor() {
        return fluxFactor;
    }

    public double getFissionModeratorEfficiency() {
        return efficiency;
    }

    public Ingredient moderator() {
        return moderator;
    }

    public static class Serializer implements RecipeSerializer<FissionModeratorRecipe> {
        private static final MapCodec<FissionModeratorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Ingredient.CODEC.fieldOf("moderator").forGetter(FissionModeratorRecipe::moderator),
                        Codec.INT.fieldOf("fluxFactor").forGetter(FissionModeratorRecipe::getFissionModeratorFluxFactor),
                        Codec.DOUBLE.fieldOf("efficiency").forGetter(FissionModeratorRecipe::getFissionModeratorEfficiency)
                ).apply(inst, FissionModeratorRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, FissionModeratorRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, FissionModeratorRecipe::moderator,
                ByteBufCodecs.INT, FissionModeratorRecipe::getFissionModeratorFluxFactor,
                ByteBufCodecs.DOUBLE, FissionModeratorRecipe::getFissionModeratorEfficiency,
                FissionModeratorRecipe::new
        );

        @Override
        public MapCodec<FissionModeratorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FissionModeratorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}