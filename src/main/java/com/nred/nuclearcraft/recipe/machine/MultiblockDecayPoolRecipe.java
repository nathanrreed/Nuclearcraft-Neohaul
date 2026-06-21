package com.nred.nuclearcraft.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.MULTIBLOCK_DECAY_POOL_RECIPE_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MULTIBLOCK_DECAY_POOL_RECIPE_TYPE;

public class MultiblockDecayPoolRecipe extends MultiblockMachine {
    public final int heatPerInputMB;

    public MultiblockDecayPoolRecipe(SizedChanceFluidIngredient fluidInput, SizedChanceFluidIngredient result, int heatPerInputMB) {
        super(List.of(), List.of(fluidInput), List.of(), List.of(result), 1, 1);
        this.heatPerInputMB = heatPerInputMB;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return getItemProduct().getStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MULTIBLOCK_DECAY_POOL_RECIPE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MULTIBLOCK_DECAY_POOL_RECIPE_TYPE.get();
    }

    public int getDecayPoolHeatPerInputMB() {
        return heatPerInputMB;
    }

    public static class Serializer implements RecipeSerializer<MultiblockDecayPoolRecipe> {
        private static final MapCodec<MultiblockDecayPoolRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidInput").forGetter(MultiblockDecayPoolRecipe::getFluidIngredient),
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("fluidProduct").forGetter(MultiblockDecayPoolRecipe::getFluidProduct),
                        Codec.INT.fieldOf("heatPerInputMB").forGetter(MultiblockDecayPoolRecipe::getDecayPoolHeatPerInputMB)
                ).apply(inst, MultiblockDecayPoolRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, MultiblockDecayPoolRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedChanceFluidIngredient.STREAM_CODEC, MultiblockDecayPoolRecipe::getFluidIngredient,
                SizedChanceFluidIngredient.STREAM_CODEC, MultiblockDecayPoolRecipe::getFluidProduct,
                ByteBufCodecs.INT, MultiblockDecayPoolRecipe::getDecayPoolHeatPerInputMB,
                MultiblockDecayPoolRecipe::new
        );

        @Override
        public MapCodec<MultiblockDecayPoolRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MultiblockDecayPoolRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}