package com.nred.nuclearcraft.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.DECAY_POOL_HEAT_SOURCE_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.DECAY_POOL_HEAT_SOURCE_RECIPE_TYPE;

public class DecayPoolHeatSourceRecipe extends BasicRecipe {
    private final double containerLifetime;
    private final double containerHeat;

    public DecayPoolHeatSourceRecipe(SizedChanceItemIngredient itemIngredient, SizedChanceFluidIngredient fluidIngredient, SizedChanceItemIngredient itemProduct, SizedChanceFluidIngredient fluidProduct, double containerLifetime, double containerHeat) {
        super(List.of(itemIngredient), List.of(fluidIngredient), List.of(itemProduct), List.of(fluidProduct));
        this.containerLifetime = containerLifetime;
        this.containerHeat = containerHeat;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return getItemProduct().getStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DECAY_POOL_HEAT_SOURCE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DECAY_POOL_HEAT_SOURCE_RECIPE_TYPE.get();
    }

    public double getDecayPoolContainerLifetime() {
        return containerLifetime;
    }

    public double getDecayPoolContainerHeat() {
        return containerHeat;
    }

    public static class Serializer implements RecipeSerializer<DecayPoolHeatSourceRecipe> {
        private static final MapCodec<DecayPoolHeatSourceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedChanceItemIngredient.FLAT_CODEC.fieldOf("itemInput").forGetter(DecayPoolHeatSourceRecipe::getItemIngredient),
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidInput").forGetter(DecayPoolHeatSourceRecipe::getFluidIngredient),
                        SizedChanceItemIngredient.FLAT_CODEC.fieldOf("itemProduct").forGetter(DecayPoolHeatSourceRecipe::getItemProduct),
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidProduct").forGetter(DecayPoolHeatSourceRecipe::getFluidProduct),
                        Codec.DOUBLE.fieldOf("timeModifier").forGetter(DecayPoolHeatSourceRecipe::getDecayPoolContainerLifetime),
                        Codec.DOUBLE.fieldOf("powerModifier").forGetter(DecayPoolHeatSourceRecipe::getDecayPoolContainerHeat)
                ).apply(inst, DecayPoolHeatSourceRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, DecayPoolHeatSourceRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedChanceItemIngredient.STREAM_CODEC, DecayPoolHeatSourceRecipe::getItemIngredient,
                SizedChanceFluidIngredient.STREAM_CODEC, DecayPoolHeatSourceRecipe::getFluidIngredient,
                SizedChanceItemIngredient.STREAM_CODEC, DecayPoolHeatSourceRecipe::getItemProduct,
                SizedChanceFluidIngredient.STREAM_CODEC, DecayPoolHeatSourceRecipe::getFluidProduct,
                ByteBufCodecs.DOUBLE, DecayPoolHeatSourceRecipe::getDecayPoolContainerLifetime,
                ByteBufCodecs.DOUBLE, DecayPoolHeatSourceRecipe::getDecayPoolContainerHeat,
                DecayPoolHeatSourceRecipe::new
        );

        @Override
        public MapCodec<DecayPoolHeatSourceRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DecayPoolHeatSourceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}