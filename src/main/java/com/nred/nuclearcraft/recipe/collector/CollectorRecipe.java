package com.nred.nuclearcraft.recipe.collector;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.COLLECTOR_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

record CollectorInput() implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 0;
    }
}

public record CollectorRecipe(ItemStack itemResult, FluidStack fluidResult, MACHINE_LEVEL level, double rate) implements Recipe<CollectorInput> {
    @Override
    public boolean matches(CollectorInput input, Level level) {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(CollectorInput input, HolderLookup.Provider registries) {
        return itemResult.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return itemResult.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return COLLECTOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        if (this.fluidResult.isEmpty()) {
            return switch (this.level) {
                case BASE -> COBBLE_GENERATOR_RECIPE_TYPE.get();
                case COMPACT -> COBBLE_GENERATOR_COMPACT_RECIPE_TYPE.get();
                case DENSE -> COBBLE_GENERATOR_DENSE_RECIPE_TYPE.get();
            };
        } else if (fluidResult.is(Fluids.WATER)) {
            return switch (this.level) {
                case BASE -> WATER_SOURCE_RECIPE_TYPE.get();
                case COMPACT -> WATER_SOURCE_COMPACT_RECIPE_TYPE.get();
                case DENSE -> WATER_SOURCE_DENSE_RECIPE_TYPE.get();
            };
        } else {
            return switch (this.level) {
                case BASE -> NITROGEN_COLLECTOR_RECIPE_TYPE.get();
                case COMPACT -> NITROGEN_COLLECTOR_COMPACT_RECIPE_TYPE.get();
                case DENSE -> NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE.get();
            };
        }
    }
}