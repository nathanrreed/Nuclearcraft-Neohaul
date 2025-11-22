package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.FLAMMABLE_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.COMPOUND_MAP;

public class RadiationScrubberProvider {
    public RadiationScrubberProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder(SizedChanceItemIngredient.of(COMPOUND_MAP.get("borax"), 1), SizedChanceFluidIngredient.EMPTY, SizedChanceItemIngredient.of(COMPOUND_MAP.get("irradiated_borax"), 1), SizedChanceFluidIngredient.EMPTY, 12000, 200, 1.0).save(recipeOutput);
        new RecipeBuilder(SizedChanceItemIngredient.EMPTY, Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("radaway"), 250), SizedChanceItemIngredient.EMPTY, Fluids.sizedIngredient(FLAMMABLE_MAP.get("ethanol"), 250), 2400, 40, 5.0).save(recipeOutput, ncLoc("radiation_scrubber_from_radaway"));
        new RecipeBuilder(SizedChanceItemIngredient.EMPTY, Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("radaway_slow"), 250), SizedChanceItemIngredient.EMPTY, Fluids.sizedIngredient(FLAMMABLE_MAP.get("redstone_ethanol"), 250), 96000, 20, 0.25).save(recipeOutput, ncLoc("radiation_scrubber_from_radaway_slow"));
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final SizedChanceItemIngredient itemIngredient;
        private final SizedChanceFluidIngredient fluidIngredient;
        private final SizedChanceItemIngredient itemResult;
        private final SizedChanceFluidIngredient fluidResult;
        private final int time;
        private final int power;
        private final double efficiency;

        public RecipeBuilder(SizedChanceItemIngredient itemIngredient, SizedChanceFluidIngredient fluidIngredient, SizedChanceItemIngredient itemResult, SizedChanceFluidIngredient fluidResult, int time, int power, double efficiency) {
            super(itemResult == null ? ItemStack.EMPTY : itemResult.getStack());
            this.itemIngredient = itemIngredient;
            this.fluidIngredient = fluidIngredient;
            this.itemResult = itemResult;
            this.fluidResult = fluidResult;
            this.time = time;
            this.power = power;
            this.efficiency = efficiency;
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            RadiationScrubberRecipe recipe = new RadiationScrubberRecipe(itemIngredient, fluidIngredient, itemResult, fluidResult, time, power, efficiency);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}