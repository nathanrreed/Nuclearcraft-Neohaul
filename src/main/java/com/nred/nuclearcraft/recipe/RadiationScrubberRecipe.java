package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.RADIATION_SCRUBBER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.RADIATION_SCRUBBER_RECIPE_TYPE;

public class RadiationScrubberRecipe extends BasicRecipe {
    private final long time;
    private final long power;
    private final double efficiency;

    public RadiationScrubberRecipe(SizedChanceItemIngredient itemIngredients, SizedChanceFluidIngredient fluidIngredients, SizedChanceItemIngredient itemProducts, SizedChanceFluidIngredient fluidProducts, long time, long power, double efficiency) {
        super(List.of(itemIngredients), List.of(fluidIngredients), List.of(itemProducts), List.of(fluidProducts));
        this.time = time;
        this.power = power;
        this.efficiency = efficiency;
    }

    public long getScrubberProcessTime() {
        return time;
    }

    public long getScrubberProcessPower() {
        return power;
    }

    public double getScrubberProcessEfficiency() {
        return efficiency;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RADIATION_SCRUBBER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RADIATION_SCRUBBER_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<RadiationScrubberRecipe> {
        public static MapCodec<RadiationScrubberRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedChanceItemIngredient.FLAT_CODEC.fieldOf("itemIngredient").forGetter(RadiationScrubberRecipe::getItemIngredient),
                SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidIngredient").forGetter(RadiationScrubberRecipe::getFluidIngredient),
                SizedChanceItemIngredient.FLAT_CODEC.fieldOf("itemProduct").forGetter(RadiationScrubberRecipe::getItemProduct),
                SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidProduct").forGetter(RadiationScrubberRecipe::getFluidProduct),
                Codec.LONG.fieldOf("time").forGetter(RadiationScrubberRecipe::getScrubberProcessTime),
                Codec.LONG.fieldOf("power").forGetter(RadiationScrubberRecipe::getScrubberProcessPower),
                Codec.DOUBLE.fieldOf("efficiency").forGetter(RadiationScrubberRecipe::getScrubberProcessEfficiency)
        ).apply(inst, RadiationScrubberRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, RadiationScrubberRecipe> STREAM_CODEC = NeoForgeStreamCodecs.composite(
                SizedChanceItemIngredient.STREAM_CODEC, RadiationScrubberRecipe::getItemIngredient,
                SizedChanceFluidIngredient.STREAM_CODEC, RadiationScrubberRecipe::getFluidIngredient,
                SizedChanceItemIngredient.STREAM_CODEC, RadiationScrubberRecipe::getItemProduct,
                SizedChanceFluidIngredient.STREAM_CODEC, RadiationScrubberRecipe::getFluidProduct,
                ByteBufCodecs.VAR_LONG, RadiationScrubberRecipe::getScrubberProcessTime,
                ByteBufCodecs.VAR_LONG, RadiationScrubberRecipe::getScrubberProcessPower,
                ByteBufCodecs.DOUBLE, RadiationScrubberRecipe::getScrubberProcessEfficiency,
                RadiationScrubberRecipe::new
        );

        @Override
        public MapCodec<RadiationScrubberRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RadiationScrubberRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}