package com.nred.nuclearcraft.recipe.fission;

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

import java.util.List;

import static com.nred.nuclearcraft.config.Config2.fission_heating_coolant_heat_mult;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_HEATING_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_HEATING_RECIPE_TYPE;

public class FissionHeatingRecipe extends BasicRecipe {
    private final int heatPerInputMB;

    public FissionHeatingRecipe(SizedFluidIngredient fluidIngredient, SizedFluidIngredient fluidProduct, int heatPerInputMB) {
        super(List.of(), List.of(fluidIngredient), List.of(), List.of(fluidProduct));
        this.heatPerInputMB = heatPerInputMB;
    }

    public int getFissionHeatingHeatPerInputMB() {
        return (int) (heatPerInputMB * fission_heating_coolant_heat_mult);
    }

    public int getFissionHeatingHeatPerInputMBRaw() {
        return heatPerInputMB;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FISSION_HEATING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return FISSION_HEATING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<FissionHeatingRecipe> {
        public static MapCodec<FissionHeatingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedFluidIngredient.FLAT_CODEC.fieldOf("fluidIngredients").forGetter(FissionHeatingRecipe::getFluidIngredient),
                SizedFluidIngredient.FLAT_CODEC.fieldOf("fluidProducts").forGetter(FissionHeatingRecipe::getFluidProduct),
                Codec.INT.fieldOf("heatPerInputMB").forGetter(FissionHeatingRecipe::getFissionHeatingHeatPerInputMBRaw)
        ).apply(inst, FissionHeatingRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, FissionHeatingRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedFluidIngredient.STREAM_CODEC, FissionHeatingRecipe::getFluidIngredient,
                SizedFluidIngredient.STREAM_CODEC, FissionHeatingRecipe::getFluidProduct,
                ByteBufCodecs.INT, FissionHeatingRecipe::getFissionHeatingHeatPerInputMBRaw,
                FissionHeatingRecipe::new
        );

        @Override
        public MapCodec<FissionHeatingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FissionHeatingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}