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
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.MULTIBLOCK_ELECTROLYZER_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

public class MultiblockElectrolyzerRecipe extends MultiblockMachine {
    private final String group;

    public MultiblockElectrolyzerRecipe(List<SizedChanceItemIngredient> itemInputs, List<SizedChanceFluidIngredient> fluidInputs, List<SizedChanceItemIngredient> itemResults, List<SizedChanceFluidIngredient> fluidResults, double timeModifier, double powerModifier, String group) {
        super(itemInputs, fluidInputs, itemResults, fluidInputs, timeModifier, powerModifier);
        this.group = group;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return getItemProduct().getStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MULTIBLOCK_ELECTROLYZER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE.get();
    }

    public String getElectrolyteGroup() {
        return group;
    }

    public static class Serializer implements RecipeSerializer<MultiblockElectrolyzerRecipe> {
        private static final MapCodec<MultiblockElectrolyzerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedChanceItemIngredient.FLAT_CODEC.listOf().fieldOf("itemInputs").forGetter(MultiblockElectrolyzerRecipe::getItemIngredients),
                        SizedChanceFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidInputs").forGetter(MultiblockElectrolyzerRecipe::getFluidIngredients),
                        SizedChanceItemIngredient.FLAT_CODEC.listOf().fieldOf("itemProducts").forGetter(MultiblockElectrolyzerRecipe::getItemProducts),
                        SizedChanceFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidProducts").forGetter(MultiblockElectrolyzerRecipe::getFluidProducts),
                        Codec.DOUBLE.fieldOf("timeModifier").forGetter(MultiblockElectrolyzerRecipe::getProcessTimeMultiplier),
                        Codec.DOUBLE.fieldOf("powerModifier").forGetter(MultiblockElectrolyzerRecipe::getProcessPowerMultiplier),
                        Codec.STRING.fieldOf("name").forGetter(MultiblockElectrolyzerRecipe::getElectrolyteGroup)
                ).apply(inst, MultiblockElectrolyzerRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, MultiblockElectrolyzerRecipe> STREAM_CODEC = NeoForgeStreamCodecs.composite(
                SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, MultiblockElectrolyzerRecipe::getItemIngredients,
                SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, MultiblockElectrolyzerRecipe::getFluidIngredients,
                SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, MultiblockElectrolyzerRecipe::getItemProducts,
                SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, MultiblockElectrolyzerRecipe::getFluidProducts,
                ByteBufCodecs.DOUBLE, MultiblockElectrolyzerRecipe::getProcessTimeMultiplier,
                ByteBufCodecs.DOUBLE, MultiblockElectrolyzerRecipe::getProcessPowerMultiplier,
                ByteBufCodecs.STRING_UTF8, MultiblockElectrolyzerRecipe::getElectrolyteGroup,
                MultiblockElectrolyzerRecipe::new
        );

        @Override
        public MapCodec<MultiblockElectrolyzerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MultiblockElectrolyzerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}