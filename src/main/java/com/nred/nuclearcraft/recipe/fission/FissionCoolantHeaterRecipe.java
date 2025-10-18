package com.nred.nuclearcraft.recipe.fission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.FISSION_COOLANT_HEATER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.COOLANT_HEATER_RECIPE_TYPE;

public class FissionCoolantHeaterRecipe extends BasicRecipe {
    private final ItemStack heater;
    private final int coolantHeaterCoolingRate;
    private final String coolantHeaterPlacementRule;

    public FissionCoolantHeaterRecipe(ItemStack heater, SizedFluidIngredient input, SizedFluidIngredient product, int coolantHeaterCoolingRate, String coolantHeaterPlacementRule) {
        super(List.of(), List.of(input), List.of(), List.of(product));
        this.heater = heater;
        this.coolantHeaterCoolingRate = coolantHeaterCoolingRate;
        this.coolantHeaterPlacementRule = coolantHeaterPlacementRule;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FISSION_COOLANT_HEATER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return COOLANT_HEATER_RECIPE_TYPE.get();
    }

    public int getCoolantHeaterCoolingRate() {
        return coolantHeaterCoolingRate;
    }

    public String getCoolantHeaterPlacementRule() {
        return coolantHeaterPlacementRule;
    }

    public ItemStack getHeater() {
        return this.heater;
    }

    public static class Serializer implements RecipeSerializer<FissionCoolantHeaterRecipe> {
        private static final MapCodec<FissionCoolantHeaterRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        ItemStack.CODEC.fieldOf("heater").forGetter(FissionCoolantHeaterRecipe::getHeater),
                        SizedFluidIngredient.FLAT_CODEC.fieldOf("input").forGetter(FissionCoolantHeaterRecipe::getFluidIngredient),
                        SizedFluidIngredient.FLAT_CODEC.fieldOf("product").forGetter(FissionCoolantHeaterRecipe::getFluidProduct),
                        Codec.INT.fieldOf("irradiatorFluxRequired").forGetter(FissionCoolantHeaterRecipe::getCoolantHeaterCoolingRate),
                        Codec.STRING.fieldOf("irradiatorHeatPerFlux").forGetter(FissionCoolantHeaterRecipe::getCoolantHeaterPlacementRule)
                ).apply(inst, FissionCoolantHeaterRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, FissionCoolantHeaterRecipe> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, FissionCoolantHeaterRecipe::getHeater,
                SizedFluidIngredient.STREAM_CODEC, FissionCoolantHeaterRecipe::getFluidIngredient,
                SizedFluidIngredient.STREAM_CODEC, FissionCoolantHeaterRecipe::getFluidProduct,
                ByteBufCodecs.INT, FissionCoolantHeaterRecipe::getCoolantHeaterCoolingRate,
                ByteBufCodecs.STRING_UTF8, FissionCoolantHeaterRecipe::getCoolantHeaterPlacementRule,
                FissionCoolantHeaterRecipe::new
        );

        @Override
        public MapCodec<FissionCoolantHeaterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FissionCoolantHeaterRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}