package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.multiblock.fisson.pebble.FissionCoolerType;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_COOLER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.COOLER_RECIPE_TYPE;

public class PebbleFissionCoolerRecipe extends BasicRecipe {
    private final ItemStack cooler;
    private final String coolerPlacementRule;

    public PebbleFissionCoolerRecipe(ItemStack cooler, SizedChanceFluidIngredient input, SizedChanceFluidIngredient product, String coolerPlacementRule) {
        super(List.of(), List.of(input), List.of(), List.of(product));
        this.cooler = cooler;
        this.coolerPlacementRule = coolerPlacementRule;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FISSION_COOLER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return COOLER_RECIPE_TYPE.get();
    }

    public int getFissionCoolingRate() {
        return FissionCoolerType.getType(coolerPlacementRule).getCoolingRate();
    }

    public String getFissionCoolingPlacementRule() {
        return coolerPlacementRule;
    }

    public ItemStack getCooler() {
        return this.cooler;
    }

    public static class Serializer implements RecipeSerializer<PebbleFissionCoolerRecipe> {
        private static final MapCodec<PebbleFissionCoolerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        ItemStack.CODEC.fieldOf("cooler").forGetter(PebbleFissionCoolerRecipe::getCooler),
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidIngredient").forGetter(PebbleFissionCoolerRecipe::getFluidIngredient),
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidProduct").forGetter(PebbleFissionCoolerRecipe::getFluidProduct),
                        Codec.STRING.fieldOf("placementRule").forGetter(PebbleFissionCoolerRecipe::getFissionCoolingPlacementRule)
                ).apply(inst, PebbleFissionCoolerRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, PebbleFissionCoolerRecipe> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, PebbleFissionCoolerRecipe::getCooler,
                SizedChanceFluidIngredient.STREAM_CODEC, PebbleFissionCoolerRecipe::getFluidIngredient,
                SizedChanceFluidIngredient.STREAM_CODEC, PebbleFissionCoolerRecipe::getFluidProduct,
                ByteBufCodecs.STRING_UTF8, PebbleFissionCoolerRecipe::getFissionCoolingPlacementRule,
                PebbleFissionCoolerRecipe::new
        );

        @Override
        public MapCodec<PebbleFissionCoolerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PebbleFissionCoolerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}