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
import java.util.Optional;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.RADIATION_SCRUBBER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.RADIATION_SCRUBBER_RECIPE_TYPE;

public class RadiationScrubberRecipe extends BasicRecipe {
    private final long time;
    private final long power;
    private final double efficiency;

    public RadiationScrubberRecipe(SizedChanceItemIngredient itemIngredient, SizedChanceFluidIngredient fluidIngredient, SizedChanceItemIngredient itemProduct, SizedChanceFluidIngredient fluidProduct, long time, long power, double efficiency) {
        super(itemIngredient == null ? List.of() : List.of(itemIngredient), fluidIngredient == null ? List.of() : List.of(fluidIngredient), itemProduct == null ? List.of() : List.of(itemProduct), fluidProduct == null ? List.of() : List.of(fluidProduct));
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
                SizedChanceItemIngredient.FLAT_CODEC.lenientOptionalFieldOf("itemIngredient").forGetter(e -> Optional.ofNullable(e.getItemIngredients().isEmpty() ? null : e.getItemIngredient())),
                SizedChanceFluidIngredient.FLAT_CODEC.lenientOptionalFieldOf("fluidIngredient").forGetter(e -> Optional.ofNullable(e.getFluidIngredients().isEmpty() ? null : e.getFluidIngredient())),
                SizedChanceItemIngredient.FLAT_CODEC.lenientOptionalFieldOf("itemProduct").forGetter(e -> Optional.ofNullable(e.getItemProducts().isEmpty() ? null : e.getItemProduct())),
                SizedChanceFluidIngredient.FLAT_CODEC.lenientOptionalFieldOf("fluidProduct").forGetter(e -> Optional.ofNullable(e.getFluidProducts().isEmpty() ? null : e.getFluidProduct())),
                Codec.LONG.fieldOf("time").forGetter(RadiationScrubberRecipe::getScrubberProcessTime),
                Codec.LONG.fieldOf("power").forGetter(RadiationScrubberRecipe::getScrubberProcessPower),
                Codec.DOUBLE.fieldOf("efficiency").forGetter(RadiationScrubberRecipe::getScrubberProcessEfficiency)
        ).apply(inst, (itemIngredient, fluidIngredient, itemProduct, fluidProduct, time, power, efficiency) ->
                new RadiationScrubberRecipe(itemIngredient.orElse(null), fluidIngredient.orElse(null), itemProduct.orElse(null), fluidProduct.orElse(null), time, power, efficiency)));

        public static StreamCodec<RegistryFriendlyByteBuf, RadiationScrubberRecipe> STREAM_CODEC = NeoForgeStreamCodecs.composite(
                SizedChanceItemIngredient.STREAM_CODEC, e -> e.getItemIngredients().isEmpty() ? SizedChanceItemIngredient.EMPTY : e.getItemIngredient(),
                SizedChanceFluidIngredient.STREAM_CODEC, e -> e.getFluidIngredients().isEmpty() ? SizedChanceFluidIngredient.EMPTY : e.getFluidIngredient(),
                SizedChanceItemIngredient.STREAM_CODEC, e -> e.getItemProducts().isEmpty() ? SizedChanceItemIngredient.EMPTY : e.getItemProduct(),
                SizedChanceFluidIngredient.STREAM_CODEC, e -> e.getFluidProducts().isEmpty() ? SizedChanceFluidIngredient.EMPTY : e.getFluidProduct(),
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