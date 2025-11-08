package com.nred.nuclearcraft.recipe.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.MACHINE_DIAPHRAGM_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MACHINE_DIAPHRAGM_RECIPE_TYPE;

public class MachineDiaphragmRecipe extends BasicRecipe {
    private final Ingredient block;
    private final double efficiency;
    private final double contact_factor;

    public MachineDiaphragmRecipe(Ingredient block, double efficiency, double contact_factor) {
        super(List.of(new SizedChanceItemIngredient(block, 1)), List.of(), List.of(), List.of());
        this.block = block;
        this.efficiency = efficiency;
        this.contact_factor = contact_factor;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MACHINE_DIAPHRAGM_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MACHINE_DIAPHRAGM_RECIPE_TYPE.get();
    }

    public double getMachineDiaphragmEfficiency() {
        return efficiency;
    }

    public double getMachineDiaphragmContactFactor() {
        return contact_factor;
    }

    public Ingredient block() {
        return block;
    }

    public static class Serializer implements RecipeSerializer<MachineDiaphragmRecipe> {
        private static final MapCodec<MachineDiaphragmRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Ingredient.CODEC.fieldOf("block").forGetter(MachineDiaphragmRecipe::block),
                        Codec.DOUBLE.fieldOf("efficiency").forGetter(MachineDiaphragmRecipe::getMachineDiaphragmEfficiency),
                        Codec.DOUBLE.fieldOf("contact_factor").forGetter(MachineDiaphragmRecipe::getMachineDiaphragmContactFactor)
                ).apply(inst, MachineDiaphragmRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, MachineDiaphragmRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, MachineDiaphragmRecipe::block,
                ByteBufCodecs.DOUBLE, MachineDiaphragmRecipe::getMachineDiaphragmEfficiency,
                ByteBufCodecs.DOUBLE, MachineDiaphragmRecipe::getMachineDiaphragmContactFactor,
                MachineDiaphragmRecipe::new
        );

        @Override
        public MapCodec<MachineDiaphragmRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MachineDiaphragmRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}