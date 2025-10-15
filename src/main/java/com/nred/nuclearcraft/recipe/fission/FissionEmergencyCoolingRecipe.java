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

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_EMERGENCY_COOLING_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_EMERGENCY_COOLING_RECIPE_TYPE;

public class FissionEmergencyCoolingRecipe extends BasicRecipe {
    private final double cooling_multiplier;

    public FissionEmergencyCoolingRecipe(SizedFluidIngredient fluidIngredient, SizedFluidIngredient fluidProduct, double cooling_multiplier) {
        super(List.of(), List.of(fluidIngredient), List.of(), List.of(fluidProduct));
        this.cooling_multiplier = cooling_multiplier;
    }

    public double getEmergencyCoolingHeatPerInputMB() {
        return cooling_multiplier;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FISSION_EMERGENCY_COOLING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return FISSION_EMERGENCY_COOLING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<FissionEmergencyCoolingRecipe> {
        public static MapCodec<FissionEmergencyCoolingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedFluidIngredient.FLAT_CODEC.fieldOf("fluidIngredients").forGetter(FissionEmergencyCoolingRecipe::getFluidIngredient),
                SizedFluidIngredient.FLAT_CODEC.fieldOf("fluidProducts").forGetter(FissionEmergencyCoolingRecipe::getFluidProduct),
                Codec.DOUBLE.fieldOf("cooling_multiplier").forGetter(FissionEmergencyCoolingRecipe::getEmergencyCoolingHeatPerInputMB)
        ).apply(inst, FissionEmergencyCoolingRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, FissionEmergencyCoolingRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedFluidIngredient.STREAM_CODEC, FissionEmergencyCoolingRecipe::getFluidIngredient,
                SizedFluidIngredient.STREAM_CODEC, FissionEmergencyCoolingRecipe::getFluidProduct,
                ByteBufCodecs.DOUBLE, FissionEmergencyCoolingRecipe::getEmergencyCoolingHeatPerInputMB,
                FissionEmergencyCoolingRecipe::new
        );

        @Override
        public MapCodec<FissionEmergencyCoolingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FissionEmergencyCoolingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}