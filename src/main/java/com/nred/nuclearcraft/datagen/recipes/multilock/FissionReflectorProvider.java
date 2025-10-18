package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.multiblock.fisson.FissionReflectorType;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionReflectorRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;

public class FissionReflectorProvider {
    public FissionReflectorProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder(Ingredient.of(FISSION_REACTOR_MAP.get("beryllium_carbon_reflector")), FissionReflectorType.BERYLLIUM_CARBON.getEfficiency(), FissionReflectorType.BERYLLIUM_CARBON.getReflectivity()).save(recipeOutput, MODID + ":beryllium_carbon_reflector_recipe");
        new RecipeBuilder(Ingredient.of(FISSION_REACTOR_MAP.get("lead_steel_reflector")), FissionReflectorType.LEAD_STEEL.getEfficiency(), FissionReflectorType.LEAD_STEEL.getReflectivity()).save(recipeOutput, MODID + ":lead_steel_reflector_recipe");
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final Ingredient moderator;
        private final double efficiency;
        private final double reflectivity;

        public RecipeBuilder(Ingredient moderator, double efficiency, double reflectivity) {
            super(ItemStack.EMPTY);
            this.moderator = moderator;
            this.reflectivity = reflectivity;
            this.efficiency = efficiency;
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            FissionReflectorRecipe recipe = new FissionReflectorRecipe(this.moderator, this.efficiency, this.reflectivity);

            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}