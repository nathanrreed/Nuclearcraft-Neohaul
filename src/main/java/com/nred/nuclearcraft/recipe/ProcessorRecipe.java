package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

public abstract class ProcessorRecipe extends BasicRecipe {
    private final double timeModifier;
    private final double powerModifier;
    private final double radiation;

    public ProcessorRecipe(List<SizedIngredient> itemInputs, List<SizedIngredient> itemResults, List<SizedFluidIngredient> fluidInputs, List<SizedFluidIngredient> fluidResults, double timeModifier, double powerModifier) {
        super(itemInputs, fluidInputs, itemResults, fluidResults);
        this.timeModifier = timeModifier;
        this.powerModifier = powerModifier;
        this.radiation = 0; // TODO
    }

    public double getProcessTimeMultiplier() {
        return timeModifier;
    }

    public double getProcessPowerMultiplier() {
        return powerModifier;
    }


    public double getBaseProcessTime(double defaultProcessTime) {
        return getProcessTimeMultiplier() * defaultProcessTime;
    }

    public double getBaseProcessPower(double defaultProcessPower) {
        return getProcessPowerMultiplier() * defaultProcessPower;
    }

    public double getBaseProcessRadiation() {
        return radiation;
    }

    @Override
    public ItemStack getToastSymbol() {
        return PROCESSOR_MAP.get(ResourceLocation.parse(getType().toString()).getPath()).toStack();
    }

    public static class Serializer implements RecipeSerializer<ProcessorRecipe> {
        private final Class<? extends ProcessorRecipe> clazz;

        public Serializer(Class<? extends ProcessorRecipe> clazz) {
            this.clazz = clazz;
        }

        @Override
        public MapCodec<ProcessorRecipe> codec() {
            return RecordCodecBuilder.mapCodec(inst ->
                    inst.group(
                            SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemInputs").forGetter(ProcessorRecipe::getItemIngredients),
                            SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemResults").forGetter(ProcessorRecipe::getItemProducts),
                            SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidInputs").forGetter(ProcessorRecipe::getFluidIngredients),
                            SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidResults").forGetter(ProcessorRecipe::getFluidProducts),
                            Codec.DOUBLE.fieldOf("timeModifier").forGetter(ProcessorRecipe::getProcessTimeMultiplier),
                            Codec.DOUBLE.fieldOf("powerModifier").forGetter(ProcessorRecipe::getProcessPowerMultiplier)
                    ).apply(inst, ((itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier) -> {
                        try {
                            return clazz.getDeclaredConstructor(List.class, List.class, List.class, List.class, double.class, double.class).newInstance(itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ProcessorRecipe> streamCodec() {
            return StreamCodec.composite(
                    SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipe::getItemIngredients,
                    SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipe::getItemProducts,
                    SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipe::getFluidIngredients,
                    SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipe::getFluidProducts,
                    ByteBufCodecs.DOUBLE, ProcessorRecipe::getProcessTimeMultiplier,
                    ByteBufCodecs.DOUBLE, ProcessorRecipe::getProcessPowerMultiplier,
                    ((itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier) -> {
                        try {
                            return clazz.getDeclaredConstructor(List.class, List.class, List.class, List.class, double.class, double.class).newInstance(itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
            );
        }
    }
}