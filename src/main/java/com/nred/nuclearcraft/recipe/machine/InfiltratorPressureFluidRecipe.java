package com.nred.nuclearcraft.recipe.machine;

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
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.INFILTRATOR_PRESSURE_FLUID_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.INFILTRATOR_PRESSURE_FLUID_RECIPE_TYPE;

public class InfiltratorPressureFluidRecipe extends BasicRecipe {
    private final FluidIngredient gas;
    private final double efficiency;

    public InfiltratorPressureFluidRecipe(FluidIngredient gas, double efficiency) {
        super(List.of(), List.of(new SizedChanceFluidIngredient(gas, 1)), List.of(), List.of());
        this.gas = gas;
        this.efficiency = efficiency;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return INFILTRATOR_PRESSURE_FLUID_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return INFILTRATOR_PRESSURE_FLUID_RECIPE_TYPE.get();
    }

    public double getInfiltratorPressureFluidEfficiency() {
        return efficiency;
    }

    public FluidIngredient gas() {
        return gas;
    }

    public static class Serializer implements RecipeSerializer<InfiltratorPressureFluidRecipe> {
        private static final MapCodec<InfiltratorPressureFluidRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        FluidIngredient.CODEC.fieldOf("gas").forGetter(InfiltratorPressureFluidRecipe::gas),
                        Codec.DOUBLE.fieldOf("efficiency").forGetter(InfiltratorPressureFluidRecipe::getInfiltratorPressureFluidEfficiency)
                ).apply(inst, InfiltratorPressureFluidRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, InfiltratorPressureFluidRecipe> STREAM_CODEC = StreamCodec.composite(
                FluidIngredient.STREAM_CODEC, InfiltratorPressureFluidRecipe::gas,
                ByteBufCodecs.DOUBLE, InfiltratorPressureFluidRecipe::getInfiltratorPressureFluidEfficiency,
                InfiltratorPressureFluidRecipe::new
        );

        @Override
        public MapCodec<InfiltratorPressureFluidRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfiltratorPressureFluidRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}