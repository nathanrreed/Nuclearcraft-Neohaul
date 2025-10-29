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

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_REFLECTOR_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_REFLECTOR_RECIPE_TYPE;

public class FissionReflectorRecipe extends BasicRecipe {
    private final Ingredient reflector;
    private final double efficiency;
    private final double reflectivity;

    public FissionReflectorRecipe(Ingredient reflector, double efficiency, double reflectivity) {
        super(List.of(new SizedIngredient(reflector, 1)), List.of(), List.of(), List.of());
        this.reflector = reflector;
        this.efficiency = efficiency;
        this.reflectivity = reflectivity;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FISSION_REFLECTOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return FISSION_REFLECTOR_RECIPE_TYPE.get();
    }

    public double getFissionReflectorReflectivity() {
        return reflectivity;
    }

    public double getFissionReflectorEfficiency() {
        return efficiency;
    }

    public Ingredient reflector() {
        return reflector;
    }

    public static class Serializer implements RecipeSerializer<FissionReflectorRecipe> {
        private static final MapCodec<FissionReflectorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Ingredient.CODEC.fieldOf("block").forGetter(FissionReflectorRecipe::reflector),
                        Codec.DOUBLE.fieldOf("efficiency").forGetter(FissionReflectorRecipe::getFissionReflectorEfficiency),
                        Codec.DOUBLE.fieldOf("reflector").forGetter(FissionReflectorRecipe::getFissionReflectorReflectivity)
                ).apply(inst, FissionReflectorRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, FissionReflectorRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, FissionReflectorRecipe::reflector,
                ByteBufCodecs.DOUBLE, FissionReflectorRecipe::getFissionReflectorEfficiency,
                ByteBufCodecs.DOUBLE, FissionReflectorRecipe::getFissionReflectorReflectivity,
                FissionReflectorRecipe::new
        );

        @Override
        public MapCodec<FissionReflectorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FissionReflectorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}