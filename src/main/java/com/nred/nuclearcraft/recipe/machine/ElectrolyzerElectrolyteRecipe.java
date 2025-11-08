package com.nred.nuclearcraft.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.ELECTROLYZER_ELECTROLYTE_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.ELECTROLYZER_ELECTROLYTE_RECIPE_TYPE;

public class ElectrolyzerElectrolyteRecipe extends BasicRecipe {
    private final double efficiency;
    private final String group;

    public ElectrolyzerElectrolyteRecipe(SizedChanceFluidIngredient electrolyte, double efficiency, String group) {
        super(List.of(), List.of(electrolyte), List.of(), List.of());
        this.efficiency = efficiency;
        this.group = group;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ELECTROLYZER_ELECTROLYTE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ELECTROLYZER_ELECTROLYTE_RECIPE_TYPE.get();
    }

    public double getElectrolyzerElectrolyteEfficiency() {
        return efficiency;
    }

    public String getElectrolyteGroup() {
        return group;
    }

    public static class Serializer implements RecipeSerializer<ElectrolyzerElectrolyteRecipe> {
        private static final MapCodec<ElectrolyzerElectrolyteRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        SizedChanceFluidIngredient.FLAT_CODEC.fieldOf("electrolyte").forGetter(ElectrolyzerElectrolyteRecipe::getFluidIngredient),
                        Codec.DOUBLE.fieldOf("efficiency").forGetter(ElectrolyzerElectrolyteRecipe::getElectrolyzerElectrolyteEfficiency),
                        Codec.STRING.fieldOf("group").forGetter(ElectrolyzerElectrolyteRecipe::getElectrolyteGroup)
                ).apply(inst, ElectrolyzerElectrolyteRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, ElectrolyzerElectrolyteRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedChanceFluidIngredient.STREAM_CODEC, ElectrolyzerElectrolyteRecipe::getFluidIngredient,
                ByteBufCodecs.DOUBLE, ElectrolyzerElectrolyteRecipe::getElectrolyzerElectrolyteEfficiency,
                ByteBufCodecs.STRING_UTF8, ElectrolyzerElectrolyteRecipe::getElectrolyteGroup,
                ElectrolyzerElectrolyteRecipe::new
        );

        @Override
        public MapCodec<ElectrolyzerElectrolyteRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ElectrolyzerElectrolyteRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}