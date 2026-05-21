package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.IntFunction;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_HEATING_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_HEATING_RECIPE_TYPE;

public class FissionHeatingRecipe extends BasicRecipe {
    private final int heatPerInputMB;
    private final RecipeHeatingType recipeHeatingType;

    public FissionHeatingRecipe(SizedChanceFluidIngredient fluidIngredient, SizedChanceFluidIngredient fluidProduct, RecipeHeatingType recipeHeatingType, int heatPerInputMB) {
        super(List.of(), List.of(fluidIngredient), List.of(), List.of(fluidProduct));
        this.heatPerInputMB = heatPerInputMB;
        this.recipeHeatingType = recipeHeatingType;
    }

    public FissionHeatingRecipe(SizedChanceFluidIngredient fluidIngredient, SizedChanceFluidIngredient fluidProduct, int heatPerInputMB) {
        this(fluidIngredient, fluidProduct, RecipeHeatingType.OTHER, heatPerInputMB);
    }

    public FissionHeatingRecipe(SizedChanceFluidIngredient fluidIngredient, SizedChanceFluidIngredient fluidProduct, RecipeHeatingType recipeHeatingType) {
        this(fluidIngredient, fluidProduct, recipeHeatingType, 0);
    }

    public enum RecipeHeatingType implements StringRepresentable {
        OTHER, GAS, NAK;

        public static final StringRepresentable.StringRepresentableCodec<RecipeHeatingType> CODEC = StringRepresentable.fromEnum(RecipeHeatingType::values);
        private static final IntFunction<RecipeHeatingType> BY_ID = ByIdMap.continuous(RecipeHeatingType::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, RecipeHeatingType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

        public int getFissionHeatingHeatPerInputMB(FissionHeatingRecipe recipe) {
            return switch (this) {
                case OTHER -> recipe.heatPerInputMB;
                case GAS -> (int) (fission_cooler_coolant_heat_per_mb * fission_heating_gas_coolant_heat_mult);
                case NAK -> (int) (fission_heater_coolant_heat_per_mb * fission_heating_nak_coolant_heat_mult);
            };

        }

        @Override
        public @NonNull String getSerializedName() {
            return name().toLowerCase();
        }
    }

    public RecipeHeatingType getHeatingType() {
        return recipeHeatingType;
    }

    public int getFissionHeatingHeatPerInputMB() {
        return this.recipeHeatingType.getFissionHeatingHeatPerInputMB(this);
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
                SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidIngredient").forGetter(FissionHeatingRecipe::getFluidIngredient),
                SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidProduct").forGetter(FissionHeatingRecipe::getFluidProduct),
                RecipeHeatingType.CODEC.fieldOf("recipeType").forGetter(FissionHeatingRecipe::getHeatingType),
                Codec.INT.optionalFieldOf("heatPerInputMB", 0).forGetter(FissionHeatingRecipe::getFissionHeatingHeatPerInputMBRaw)
        ).apply(inst, FissionHeatingRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, FissionHeatingRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedChanceFluidIngredient.STREAM_CODEC, FissionHeatingRecipe::getFluidIngredient,
                SizedChanceFluidIngredient.STREAM_CODEC, FissionHeatingRecipe::getFluidProduct,
                RecipeHeatingType.STREAM_CODEC, FissionHeatingRecipe::getHeatingType,
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