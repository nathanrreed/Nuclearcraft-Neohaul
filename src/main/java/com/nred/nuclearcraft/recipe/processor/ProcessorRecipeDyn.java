package com.nred.nuclearcraft.recipe.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.BlockEntityInfoHandler;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.util.StreamCodecsHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.PROCESSOR_RECIPE_SERIALIZERS_DYN;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_DYN_TYPES;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

public class ProcessorRecipeDyn extends ProcessorRecipe {
    private final String type;

    public ProcessorRecipeDyn(String type, List<SizedChanceItemIngredient> itemInputs, List<SizedChanceItemIngredient> itemResults, List<SizedChanceFluidIngredient> fluidInputs, List<SizedChanceFluidIngredient> fluidResults, double timeModifier, double powerModifier, double radiation) {
        super(itemInputs, itemResults, fluidInputs, fluidResults, timeModifier, powerModifier, radiation);
        this.type = type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PROCESSOR_RECIPE_SERIALIZERS_DYN.get(type).get();
    }

    @Override
    public RecipeType<?> getType() {
        return PROCESSOR_RECIPE_DYN_TYPES.get(type).get();
    }

    public static class Serializer implements RecipeSerializer<ProcessorRecipeDyn> {
        public final String name;

        public Serializer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public MapCodec<ProcessorRecipeDyn> codec() {
            var info = BlockEntityInfoHandler.getProcessorMenuInfo(name);
            return RecordCodecBuilder.mapCodec(inst ->
                    inst.group(
                            Codec.STRING.fieldOf("type").orElse(name).forGetter((recipe) -> recipe.type),
                            SizedChanceItemIngredient.FLAT_CODEC.listOf(0, info.itemInputSize).lenientOptionalFieldOf("itemIngredients", List.of()).forGetter(ProcessorRecipeDyn::getItemIngredients),
                            SizedChanceItemIngredient.FLAT_CODEC.listOf(0, info.itemOutputSize).lenientOptionalFieldOf("itemProducts", List.of()).forGetter(ProcessorRecipeDyn::getItemProducts),
                            SizedChanceFluidIngredient.FLAT_CODEC.listOf(0, info.fluidInputSize).lenientOptionalFieldOf("fluidIngredients", List.of()).forGetter(ProcessorRecipeDyn::getFluidIngredients),
                            SizedChanceFluidIngredient.FLAT_CODEC.listOf(0, info.fluidOutputSize).lenientOptionalFieldOf("fluidProducts", List.of()).forGetter(ProcessorRecipeDyn::getFluidProducts),
                            Codec.DOUBLE.fieldOf("timeModifier").forGetter(ProcessorRecipeDyn::getProcessTimeMultiplier),
                            Codec.DOUBLE.fieldOf("powerModifier").forGetter(ProcessorRecipeDyn::getProcessPowerMultiplier),
                            Codec.DOUBLE.fieldOf("radiation").forGetter(ProcessorRecipeDyn::getBaseProcessRadiation)
                    ).apply(inst, ProcessorRecipeDyn::new));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ProcessorRecipeDyn> streamCodec() {
            return StreamCodecsHelper.composite(
                    ByteBufCodecs.STRING_UTF8, (recipe) -> recipe.type,
                    SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipeDyn::getItemIngredients,
                    SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipeDyn::getItemProducts,
                    SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipeDyn::getFluidIngredients,
                    SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, ProcessorRecipeDyn::getFluidProducts,
                    ByteBufCodecs.DOUBLE, ProcessorRecipeDyn::getProcessTimeMultiplier,
                    ByteBufCodecs.DOUBLE, ProcessorRecipeDyn::getProcessPowerMultiplier,
                    ByteBufCodecs.DOUBLE, ProcessorRecipeDyn::getBaseProcessRadiation,
                    ProcessorRecipeDyn::new);
        }
    }
}