package com.nred.nuclearcraft.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.MULTIBLOCK_DISTILLER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MULTIBLOCK_DISTILLER_RECIPE_TYPE;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;

public class MultiblockDistillerRecipe extends MultiblockMachine {
    public MultiblockDistillerRecipe(List<SizedChanceFluidIngredient> fluidInputs, List<SizedChanceFluidIngredient> fluidOutputs, double timeModifier, double powerModifier) {
        super(List.of(), fluidInputs, List.of(), fluidOutputs, timeModifier, powerModifier);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return getItemProduct().getStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MULTIBLOCK_DISTILLER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MULTIBLOCK_DISTILLER_RECIPE_TYPE.get();
    }

    public long getDistillerSieveTrayCount() {
        long liquidProductCount = getFluidProducts().stream().filter(x -> {
            FluidStack stack = x.getStack();
            Fluid fluid = stack == null ? null : stack.getFluid();
            return fluid != null && !fluid.getFluidType().isLighterThanAir();
        }).count();

        return Math.max(0L, liquidProductCount - 1L);
    }

    public static class Serializer implements RecipeSerializer<MultiblockDistillerRecipe> {
        private static final MapCodec<MultiblockDistillerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedChanceFluidIngredient.FLAT_CODEC.listOf().fieldOf("itemInputs").forGetter(MultiblockDistillerRecipe::getFluidIngredients),
                        SizedChanceFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidInputs").forGetter(MultiblockDistillerRecipe::getFluidProducts),
                        Codec.DOUBLE.fieldOf("timeModifier").forGetter(MultiblockDistillerRecipe::getProcessTimeMultiplier),
                        Codec.DOUBLE.fieldOf("powerModifier").forGetter(MultiblockDistillerRecipe::getProcessPowerMultiplier)
                ).apply(inst, MultiblockDistillerRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, MultiblockDistillerRecipe> STREAM_CODEC = StreamCodec.composite(
                SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, MultiblockDistillerRecipe::getFluidIngredients,
                SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, MultiblockDistillerRecipe::getFluidProducts,
                ByteBufCodecs.DOUBLE, MultiblockDistillerRecipe::getProcessTimeMultiplier,
                ByteBufCodecs.DOUBLE, MultiblockDistillerRecipe::getProcessPowerMultiplier,
                MultiblockDistillerRecipe::new
        );

        @Override
        public MapCodec<MultiblockDistillerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MultiblockDistillerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}