package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.fission_heating_coolant_heat_mult;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_HEATING_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_HEATING_RECIPE_TYPE;

public class FissionHeatingRecipe extends BasicRecipe {
    private final int heatPerInputMB;
    private final boolean isCoolant;

    public FissionHeatingRecipe(SizedChanceFluidIngredient fluidIngredient, SizedChanceFluidIngredient fluidProduct, int heatPerInputMB, boolean isCoolant) {
        super(List.of(), List.of(fluidIngredient), List.of(), List.of(fluidProduct));
        this.heatPerInputMB = heatPerInputMB;
        this.isCoolant = isCoolant;
    }

    public int getFissionHeatingHeatPerInputMB() {
        return (int) (heatPerInputMB * (isCoolant ? fission_heating_coolant_heat_mult : 1));
    }

    public int getFissionHeatingHeatPerInputMBRaw() {
        return heatPerInputMB;
    }

    public boolean getIsCoolant() {
        return isCoolant;
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
                SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidIngredients").forGetter(FissionHeatingRecipe::getFluidIngredient),
                SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidProducts").forGetter(FissionHeatingRecipe::getFluidProduct),
                Codec.INT.fieldOf("heatPerInputMB").forGetter(FissionHeatingRecipe::getFissionHeatingHeatPerInputMBRaw),
                Codec.BOOL.fieldOf("isCoolant").forGetter(FissionHeatingRecipe::getIsCoolant)
        ).apply(inst, FissionHeatingRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, FissionHeatingRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedChanceFluidIngredient.STREAM_CODEC, FissionHeatingRecipe::getFluidIngredient,
                SizedChanceFluidIngredient.STREAM_CODEC, FissionHeatingRecipe::getFluidProduct,
                ByteBufCodecs.INT, FissionHeatingRecipe::getFissionHeatingHeatPerInputMBRaw,
                ByteBufCodecs.BOOL, FissionHeatingRecipe::getIsCoolant,
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