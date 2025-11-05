package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.util.StreamCodecsHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_IRRADIATOR_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_IRRADIATOR_RECIPE_TYPE;

public class FissionIrradiatorRecipe extends BasicRecipe {
    private final long irradiatorFluxRequired;
    private final double irradiatorHeatPerFlux;
    private final double irradiatorProcessEfficiency;
    private final long irradiatorMinFluxPerTick;
    private final long irradiatorMaxFluxPerTick;
    private final double irradiatorBaseProcessRadiation;

    public FissionIrradiatorRecipe(SizedChanceItemIngredient input, SizedChanceItemIngredient product, long irradiatorFluxRequired, double irradiatorHeatPerFlux, double irradiatorProcessEfficiency, long irradiatorMinFluxPerTick, long irradiatorMaxFluxPerTick, double irradiatorBaseProcessRadiation) {
        super(List.of(input), List.of(), List.of(product), List.of());
        this.irradiatorFluxRequired = irradiatorFluxRequired;
        this.irradiatorHeatPerFlux = irradiatorHeatPerFlux;
        this.irradiatorProcessEfficiency = irradiatorProcessEfficiency;
        this.irradiatorMinFluxPerTick = irradiatorMinFluxPerTick;
        this.irradiatorMaxFluxPerTick = irradiatorMaxFluxPerTick;
        this.irradiatorBaseProcessRadiation = irradiatorBaseProcessRadiation;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FISSION_IRRADIATOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return FISSION_IRRADIATOR_RECIPE_TYPE.get();
    }

    public long getIrradiatorFluxRequired() {
        return irradiatorFluxRequired;
    }

    public double getIrradiatorHeatPerFlux() {
        return irradiatorHeatPerFlux;
    }

    public double getIrradiatorProcessEfficiency() {
        return irradiatorProcessEfficiency;
    }

    public long getIrradiatorMinFluxPerTick() {
        return irradiatorMinFluxPerTick;
    }

    public long getIrradiatorMaxFluxPerTick() {
        return irradiatorMaxFluxPerTick;
    }

    public double getIrradiatorBaseProcessRadiation() {
        return irradiatorBaseProcessRadiation;
    }

    public static class Serializer implements RecipeSerializer<FissionIrradiatorRecipe> {
        private static final MapCodec<FissionIrradiatorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedChanceItemIngredient.FLAT_CODEC.fieldOf("input").forGetter(FissionIrradiatorRecipe::getItemIngredient),
                        SizedChanceItemIngredient.FLAT_CODEC.fieldOf("product").forGetter(FissionIrradiatorRecipe::getItemProduct),
                        Codec.LONG.fieldOf("irradiatorFluxRequired").forGetter(FissionIrradiatorRecipe::getIrradiatorFluxRequired),
                        Codec.DOUBLE.fieldOf("irradiatorHeatPerFlux").forGetter(FissionIrradiatorRecipe::getIrradiatorHeatPerFlux),
                        Codec.DOUBLE.fieldOf("irradiatorProcessEfficiency").forGetter(FissionIrradiatorRecipe::getIrradiatorProcessEfficiency),
                        Codec.LONG.fieldOf("irradiatorMinFluxPerTick").forGetter(FissionIrradiatorRecipe::getIrradiatorMinFluxPerTick),
                        Codec.LONG.fieldOf("irradiatorMaxFluxPerTick").forGetter(FissionIrradiatorRecipe::getIrradiatorMaxFluxPerTick),
                        Codec.DOUBLE.fieldOf("irradiatorBaseProcessRadiation").forGetter(FissionIrradiatorRecipe::getIrradiatorBaseProcessRadiation)
                ).apply(inst, FissionIrradiatorRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, FissionIrradiatorRecipe> STREAM_CODEC = StreamCodecsHelper.composite(
                SizedChanceItemIngredient.STREAM_CODEC, FissionIrradiatorRecipe::getItemIngredient,
                SizedChanceItemIngredient.STREAM_CODEC, FissionIrradiatorRecipe::getItemProduct,
                ByteBufCodecs.VAR_LONG, FissionIrradiatorRecipe::getIrradiatorFluxRequired,
                ByteBufCodecs.DOUBLE, FissionIrradiatorRecipe::getIrradiatorHeatPerFlux,
                ByteBufCodecs.DOUBLE, FissionIrradiatorRecipe::getIrradiatorProcessEfficiency,
                ByteBufCodecs.VAR_LONG, FissionIrradiatorRecipe::getIrradiatorMinFluxPerTick,
                ByteBufCodecs.VAR_LONG, FissionIrradiatorRecipe::getIrradiatorMaxFluxPerTick,
                ByteBufCodecs.DOUBLE, FissionIrradiatorRecipe::getIrradiatorBaseProcessRadiation,
                FissionIrradiatorRecipe::new
        );

        @Override
        public MapCodec<FissionIrradiatorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FissionIrradiatorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}