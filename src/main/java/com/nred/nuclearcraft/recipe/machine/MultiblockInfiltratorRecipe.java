package com.nred.nuclearcraft.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.MULTIBLOCK_INFILTRATOR_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MULTIBLOCK_INFILTRATOR_RECIPE_TYPE;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

public class MultiblockInfiltratorRecipe extends MultiblockMachine {
    private final double heatingFactor;

    public MultiblockInfiltratorRecipe(List<SizedChanceItemIngredient> itemInputs, List<SizedChanceFluidIngredient> fluidInputs, SizedChanceItemIngredient result, double timeModifier, double powerModifier, double heatingFactor) {
        super(itemInputs, fluidInputs, List.of(result), List.of(), timeModifier, powerModifier);
        this.heatingFactor = heatingFactor;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return getItemProduct().getStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MULTIBLOCK_INFILTRATOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MULTIBLOCK_INFILTRATOR_RECIPE_TYPE.get();
    }

    public double getInfiltratorHeatingFactor() {
        return heatingFactor;
    }

    public static class Serializer implements RecipeSerializer<MultiblockInfiltratorRecipe> {
        private static final MapCodec<MultiblockInfiltratorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedChanceItemIngredient.FLAT_CODEC.listOf().fieldOf("itemInputs").forGetter(MultiblockInfiltratorRecipe::getItemIngredients),
                        SizedChanceFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidInputs").forGetter(MultiblockInfiltratorRecipe::getFluidIngredients),
                        SizedChanceItemIngredient.FLAT_CODEC.fieldOf("itemResult").forGetter(MultiblockInfiltratorRecipe::getItemProduct),
                        Codec.DOUBLE.fieldOf("timeModifier").forGetter(MultiblockInfiltratorRecipe::getProcessTimeMultiplier),
                        Codec.DOUBLE.fieldOf("powerModifier").forGetter(MultiblockInfiltratorRecipe::getProcessPowerMultiplier),
                        Codec.DOUBLE.fieldOf("heatingFactor").forGetter(MultiblockInfiltratorRecipe::getInfiltratorHeatingFactor)
                ).apply(inst, MultiblockInfiltratorRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, MultiblockInfiltratorRecipe> STREAM_CODEC = StreamCodec.composite(
                SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, MultiblockInfiltratorRecipe::getItemIngredients,
                SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, MultiblockInfiltratorRecipe::getFluidIngredients,
                SizedChanceItemIngredient.STREAM_CODEC, MultiblockInfiltratorRecipe::getItemProduct,
                ByteBufCodecs.DOUBLE, MultiblockInfiltratorRecipe::getProcessTimeMultiplier,
                ByteBufCodecs.DOUBLE, MultiblockInfiltratorRecipe::getProcessPowerMultiplier,
                ByteBufCodecs.DOUBLE, MultiblockInfiltratorRecipe::getInfiltratorHeatingFactor,
                MultiblockInfiltratorRecipe::new
        );

        @Override
        public MapCodec<MultiblockInfiltratorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MultiblockInfiltratorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}