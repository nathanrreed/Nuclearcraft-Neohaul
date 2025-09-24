package com.nred.nuclearcraft.recipe.turbine;

import com.nred.nuclearcraft.recipe.base_types.FluidRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import static com.nred.nuclearcraft.config.Config2.turbine_spin_up_multiplier_global;
import static com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe.testSizedFluidIngredient;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.TURBINE_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.TURBINE_RECIPE_TYPE;

public record TurbineRecipe(SizedFluidIngredient fluidInput, SizedFluidIngredient fluidResult, double power_per_mb, double expansion_level, double spin_up_multiplier, ParticleOptions particle, double particle_speed_mult) implements Recipe<FluidRecipeInput> {

    @Override
    public boolean matches(FluidRecipeInput input, Level level) {
        return testSizedFluidIngredient(fluidInput, input.fluids());
    }

    @Override
    public ItemStack assemble(FluidRecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TURBINE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TURBINE_RECIPE_TYPE.get();
    }

    public double get_spin_up_multiplier() {
        return turbine_spin_up_multiplier_global * spin_up_multiplier;
    }
}