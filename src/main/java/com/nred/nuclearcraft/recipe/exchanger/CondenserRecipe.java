package com.nred.nuclearcraft.recipe.exchanger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.CONDENSER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.CONDENSER_RECIPE_TYPE;

public class CondenserRecipe extends BasicRecipe {
    private final double coolingRequired;
    private final int inputTemp;
    private final int outputTemp;
    private final int preferredFlowDir;
    private final double flowDirectionBonus;

    public CondenserRecipe(SizedFluidIngredient input, SizedFluidIngredient output, double coolingRequired, int inputTemp, int outputTemp, int preferredFlowDir, double flowDirectionBonus) {
        super(List.of(), List.of(input), List.of(), List.of(output));
        this.coolingRequired = coolingRequired;
        this.inputTemp = inputTemp;
        this.outputTemp = outputTemp;
        this.preferredFlowDir = preferredFlowDir;
        this.flowDirectionBonus = flowDirectionBonus;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CONDENSER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CONDENSER_RECIPE_TYPE.get();
    }

    public double getCondenserCoolingRequired() {
        return coolingRequired;
    }

    public int getCondenserInputTemperature() {
        return inputTemp;
    }

    public int getCondenserOutputTemperature() {
        return outputTemp;
    }

    public int getCondenserPreferredFlowDirection() {
        return preferredFlowDir;
    }

    public double getCondenserFlowDirectionBonus() {
        return flowDirectionBonus;
    }

    public double getCondenserFlowDirectionMultiplier(Vec3 flowDir) {
        int preferredFlowDirection = getCondenserPreferredFlowDirection();
        double flowDirectionBonus = getCondenserFlowDirectionBonus();
        double flowProjection = preferredFlowDirection == 0 ? Math.sqrt(flowDir.x * flowDir.x + flowDir.z * flowDir.z) : (preferredFlowDirection > 0 ? flowDir.y : -flowDir.y);
        return Math.max(0D, 1D + flowDirectionBonus * (flowProjection > 0.5D ? 1D : (flowProjection > -0.5D ? 0D : -1D)));
    }

    public static class Serializer implements RecipeSerializer<CondenserRecipe> {
        private static final MapCodec<CondenserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedFluidIngredient.FLAT_CODEC.fieldOf("input").forGetter(CondenserRecipe::getFluidIngredient),
                        SizedFluidIngredient.FLAT_CODEC.fieldOf("output").forGetter(CondenserRecipe::getFluidProduct),
                        Codec.DOUBLE.fieldOf("coolingRequired").forGetter(CondenserRecipe::getCondenserCoolingRequired),
                        Codec.INT.fieldOf("inputTemp").forGetter(CondenserRecipe::getCondenserInputTemperature),
                        Codec.INT.fieldOf("outputTemp").forGetter(CondenserRecipe::getCondenserOutputTemperature),
                        Codec.INT.fieldOf("preferredFlowDir").forGetter(CondenserRecipe::getCondenserPreferredFlowDirection),
                        Codec.DOUBLE.fieldOf("flowDirectionBonus").forGetter(CondenserRecipe::getCondenserFlowDirectionBonus)
                ).apply(inst, CondenserRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, CondenserRecipe> STREAM_CODEC = NeoForgeStreamCodecs.composite(
                SizedFluidIngredient.STREAM_CODEC, CondenserRecipe::getFluidIngredient,
                SizedFluidIngredient.STREAM_CODEC, CondenserRecipe::getFluidProduct,
                ByteBufCodecs.DOUBLE, CondenserRecipe::getCondenserCoolingRequired,
                ByteBufCodecs.INT, CondenserRecipe::getCondenserInputTemperature,
                ByteBufCodecs.INT, CondenserRecipe::getCondenserOutputTemperature,
                ByteBufCodecs.INT, CondenserRecipe::getCondenserPreferredFlowDirection,
                ByteBufCodecs.DOUBLE, CondenserRecipe::getCondenserFlowDirectionBonus,
                CondenserRecipe::new
        );

        @Override
        public MapCodec<CondenserRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CondenserRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}