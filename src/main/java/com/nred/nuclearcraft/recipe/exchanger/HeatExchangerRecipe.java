package com.nred.nuclearcraft.recipe.exchanger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.util.StreamCodecsHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.IntFunction;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.HEAT_EXCHANGER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.HEAT_EXCHANGER_RECIPE_TYPE;

public class HeatExchangerRecipe extends BasicRecipe {
    private final double heatDifference;
    private final int inputTemp;
    private final int outputTemp;
    private final boolean isHeating;
    private final int preferredFlowDir;
    private final double flowDirectionBonus;
    private final RecipeHeatingType recipeHeatingType;

    public HeatExchangerRecipe(SizedChanceFluidIngredient input, SizedChanceFluidIngredient output, double heatDifference, RecipeHeatingType recipeHeatingType, int inputTemp, int outputTemp, boolean isHeating, int preferredFlowDir, double flowDirectionBonus) {
        super(List.of(), List.of(input), List.of(), List.of(output));
        this.heatDifference = heatDifference;
        this.inputTemp = inputTemp;
        this.outputTemp = outputTemp;
        this.isHeating = isHeating;
        this.preferredFlowDir = preferredFlowDir;
        this.flowDirectionBonus = flowDirectionBonus;
        this.recipeHeatingType = recipeHeatingType;
    }

    public HeatExchangerRecipe(SizedChanceFluidIngredient input, SizedChanceFluidIngredient output, double heatDifference, int inputTemp, int outputTemp, boolean isHeating, int preferredFlowDir, double flowDirectionBonus) {
        this(input, output, heatDifference, RecipeHeatingType.OTHER, inputTemp, outputTemp, isHeating, preferredFlowDir, flowDirectionBonus);
    }

    public HeatExchangerRecipe(SizedChanceFluidIngredient input, SizedChanceFluidIngredient output, RecipeHeatingType recipeHeatingType, int inputTemp, int outputTemp, boolean isHeating, int preferredFlowDir, double flowDirectionBonus) {
        this(input, output, 0, recipeHeatingType, inputTemp, outputTemp, isHeating, preferredFlowDir, flowDirectionBonus);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HEAT_EXCHANGER_RECIPE_SERIALIZER.get();
    }

    @Override
    public net.minecraft.world.item.crafting.RecipeType<?> getType() {
        return HEAT_EXCHANGER_RECIPE_TYPE.get();
    }

    public enum RecipeHeatingType implements StringRepresentable {
        OTHER, HOT_GAS, EXHAUST_GAS, HOT_NAK;

        public static final StringRepresentable.StringRepresentableCodec<RecipeHeatingType> CODEC = StringRepresentable.fromEnum(RecipeHeatingType::values);
        private static final IntFunction<RecipeHeatingType> BY_ID = ByIdMap.continuous(RecipeHeatingType::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, RecipeHeatingType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

        public double getHeatExchangerHeatDifference(HeatExchangerRecipe recipe) {
            return switch (this) {
                case OTHER -> recipe.heatDifference;
                case HOT_GAS -> fission_cooler_coolant_heat_per_mb * heat_exchanger_gas_coolant_heat_mult;
                case EXHAUST_GAS -> 0.5D * fission_cooler_coolant_heat_per_mb * heat_exchanger_gas_coolant_heat_mult;
                case HOT_NAK -> fission_heater_coolant_heat_per_mb * heat_exchanger_nak_coolant_heat_mult;
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

    public double getHeatExchangerHeatDifference() {
        return this.recipeHeatingType.getHeatExchangerHeatDifference(this);
    }

    public double getHeatExchangerHeatDifferenceRaw() {
        return heatDifference;
    }

    public int getHeatExchangerInputTemperature() {
        return inputTemp;
    }

    public int getHeatExchangerOutputTemperature() {
        return outputTemp;
    }

    public boolean getHeatExchangerIsHeating() {
        int inputTemperature = getHeatExchangerInputTemperature(), outputTemperature = getHeatExchangerOutputTemperature();
        if (inputTemperature == outputTemperature) {
            return isHeating;
        } else {
            return inputTemperature < outputTemperature;
        }
    }

    public boolean getHeatExchangerIsHeatingRaw() {
        return isHeating;
    }

    public int getHeatExchangerPreferredFlowDirection() {
        return preferredFlowDir;
    }

    public double getHeatExchangerFlowDirectionBonus() {
        return flowDirectionBonus;
    }

    public double getHeatExchangerFlowDirectionMultiplier(Vec3 flowDir) {
        int preferredFlowDirection = getHeatExchangerPreferredFlowDirection();
        double flowDirectionBonus = getHeatExchangerFlowDirectionBonus();

		/*if (preferredFlowDirection == 0) {
			return Math.max(0D, 1D + flowDirectionBonus * Math.sqrt(flowDir.x * flowDir.x + flowDir.z * flowDir.z));
		}
		else if (preferredFlowDirection > 0) {
			return Math.max(0D, 1D + flowDirectionBonus * flowDir.y);
		}
		else {
			return Math.max(0D, 1D - flowDirectionBonus * flowDir.y);
		}*/

        double flowProjection = preferredFlowDirection == 0 ? Math.sqrt(flowDir.x * flowDir.x + flowDir.z * flowDir.z) : (preferredFlowDirection > 0 ? flowDir.y : -flowDir.y);
        return Math.max(0D, 1D + flowDirectionBonus * (flowProjection > 0.5D ? 1D : (flowProjection > -0.5D ? 0D : -1D)));
    }

    public static class Serializer implements RecipeSerializer<HeatExchangerRecipe> {
        private static final MapCodec<HeatExchangerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidIngredient").forGetter(HeatExchangerRecipe::getFluidIngredient),
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidProduct").forGetter(HeatExchangerRecipe::getFluidProduct),
                        Codec.DOUBLE.optionalFieldOf("heatDifference", 0.0).forGetter(HeatExchangerRecipe::getHeatExchangerHeatDifferenceRaw),
                        RecipeHeatingType.CODEC.fieldOf("recipeType").forGetter(HeatExchangerRecipe::getHeatingType),
                        Codec.INT.fieldOf("inputTemp").forGetter(HeatExchangerRecipe::getHeatExchangerInputTemperature),
                        Codec.INT.fieldOf("outputTemp").forGetter(HeatExchangerRecipe::getHeatExchangerOutputTemperature),
                        Codec.BOOL.fieldOf("isHeating").forGetter(HeatExchangerRecipe::getHeatExchangerIsHeatingRaw),
                        Codec.INT.fieldOf("preferredFlowDir").forGetter(HeatExchangerRecipe::getHeatExchangerPreferredFlowDirection),
                        Codec.DOUBLE.fieldOf("flowDirectionBonus").forGetter(HeatExchangerRecipe::getHeatExchangerFlowDirectionBonus)
                ).apply(inst, HeatExchangerRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, HeatExchangerRecipe> STREAM_CODEC = StreamCodecsHelper.composite(
                SizedChanceFluidIngredient.STREAM_CODEC, HeatExchangerRecipe::getFluidIngredient,
                SizedChanceFluidIngredient.STREAM_CODEC, HeatExchangerRecipe::getFluidProduct,
                ByteBufCodecs.DOUBLE, HeatExchangerRecipe::getHeatExchangerHeatDifferenceRaw,
                RecipeHeatingType.STREAM_CODEC, HeatExchangerRecipe::getHeatingType,
                ByteBufCodecs.INT, HeatExchangerRecipe::getHeatExchangerInputTemperature,
                ByteBufCodecs.INT, HeatExchangerRecipe::getHeatExchangerOutputTemperature,
                ByteBufCodecs.BOOL, HeatExchangerRecipe::getHeatExchangerIsHeatingRaw,
                ByteBufCodecs.INT, HeatExchangerRecipe::getHeatExchangerPreferredFlowDirection,
                ByteBufCodecs.DOUBLE, HeatExchangerRecipe::getHeatExchangerFlowDirectionBonus,
                HeatExchangerRecipe::new
        );

        @Override
        public MapCodec<HeatExchangerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HeatExchangerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}