package com.nred.nuclearcraft.recipe.exchanger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.util.StreamCodecsHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.HEAT_EXCHANGER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.HEAT_EXCHANGER_RECIPE_TYPE;

public class HeatExchangerRecipe extends BasicRecipe {
    private final double heatDifference;
    private final int inputTemp;
    private final int outputTemp;
    private final boolean isHeating;
    private final int preferredFlowDir;
    private final double flowDirectionBonus;

    public HeatExchangerRecipe(SizedFluidIngredient input, SizedFluidIngredient output, double heatDifference, int inputTemp, int outputTemp, boolean isHeating, int preferredFlowDir, double flowDirectionBonus) {
        super(List.of(), List.of(input), List.of(), List.of(output));
        this.heatDifference = heatDifference;
        this.inputTemp = inputTemp;
        this.outputTemp = outputTemp;
        this.isHeating = isHeating;
        this.preferredFlowDir = preferredFlowDir;
        this.flowDirectionBonus = flowDirectionBonus;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HEAT_EXCHANGER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return HEAT_EXCHANGER_RECIPE_TYPE.get();
    }

    public double getHeatExchangerHeatDifference() {
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
                        SizedFluidIngredient.FLAT_CODEC.fieldOf("input").forGetter(HeatExchangerRecipe::getFluidIngredient),
                        SizedFluidIngredient.FLAT_CODEC.fieldOf("output").forGetter(HeatExchangerRecipe::getFluidProduct),
                        Codec.DOUBLE.fieldOf("heatDifference").forGetter(HeatExchangerRecipe::getHeatExchangerHeatDifference),
                        Codec.INT.fieldOf("inputTemp").forGetter(HeatExchangerRecipe::getHeatExchangerInputTemperature),
                        Codec.INT.fieldOf("outputTemp").forGetter(HeatExchangerRecipe::getHeatExchangerOutputTemperature),
                        Codec.BOOL.fieldOf("isHeating").forGetter(HeatExchangerRecipe::getHeatExchangerIsHeatingRaw),
                        Codec.INT.fieldOf("preferredFlowDir").forGetter(HeatExchangerRecipe::getHeatExchangerPreferredFlowDirection),
                        Codec.DOUBLE.fieldOf("flowDirectionBonus").forGetter(HeatExchangerRecipe::getHeatExchangerFlowDirectionBonus)
                ).apply(inst, HeatExchangerRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, HeatExchangerRecipe> STREAM_CODEC = StreamCodecsHelper.composite(
                SizedFluidIngredient.STREAM_CODEC, HeatExchangerRecipe::getFluidIngredient,
                SizedFluidIngredient.STREAM_CODEC, HeatExchangerRecipe::getFluidProduct,
                ByteBufCodecs.DOUBLE, HeatExchangerRecipe::getHeatExchangerHeatDifference,
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