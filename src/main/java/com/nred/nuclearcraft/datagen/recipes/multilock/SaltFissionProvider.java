package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.SaltFissionRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import static com.nred.nuclearcraft.info.Fluids.sizedIngredient;
import static com.nred.nuclearcraft.registration.FluidRegistration.FISSION_FUEL_MAP;

public class SaltFissionProvider {
    public SaltFissionProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder("tbu", 18000, 32, 1.25, 234, 0.04, false, RadSources.TBU_FISSION).save(recipeOutput);

        new RecipeBuilder("leu_233", 3348, 172, 1.1, 78, 0.065, false, RadSources.LEU_233_FISSION).save(recipeOutput);
        new RecipeBuilder("heu_233", 3348, 172 * 3, 1.15, 78 / 2, 0.065, false, RadSources.HEU_233_FISSION).save(recipeOutput);
        new RecipeBuilder("leu_235", 6000, 96, 1, 102, 0.065, false, RadSources.LEU_235_FISSION).save(recipeOutput);
        new RecipeBuilder("heu_235", 6000, 96 * 3, 1.05, 102 / 2, 0.065, false, RadSources.HEU_235_FISSION).save(recipeOutput);

        new RecipeBuilder("len_236", 2462, 234, 1.1, 70, 0.07, false, RadSources.LEN_236_FISSION).save(recipeOutput);
        new RecipeBuilder("hen_236", 2462, 234 * 3, 1.15, 70 / 2, 0.07, false, RadSources.HEN_236_FISSION).save(recipeOutput);

        new RecipeBuilder("lep_239", 5760, 100, 1.2, 99, 0.075, false, RadSources.LEP_239_FISSION).save(recipeOutput);
        new RecipeBuilder("hep_239", 5760, 100 * 3, 1.25, 99 / 2, 0.075, false, RadSources.HEP_239_FISSION).save(recipeOutput);
        new RecipeBuilder("lep_241", 3946, 146, 1.25, 84, 0.075, false, RadSources.LEP_241_FISSION).save(recipeOutput);
        new RecipeBuilder("hep_241", 3946, 146 * 3, 1.3, 84 / 2, 0.075, false, RadSources.HEP_241_FISSION).save(recipeOutput);

        new RecipeBuilder("mix_239", 5486, 106, 1.05, 94, 0.075, false, RadSources.MIX_239_FISSION).save(recipeOutput);
        new RecipeBuilder("mix_241", 3758, 154, 1.15, 80, 0.075, false, RadSources.MIX_241_FISSION).save(recipeOutput);

        new RecipeBuilder("lea_242", 1846, 312, 1.35, 65, 0.08, false, RadSources.LEA_242_FISSION).save(recipeOutput);
        new RecipeBuilder("hea_242", 1846, 312 * 3, 1.4, 65 / 2, 0.08, false, RadSources.HEA_242_FISSION).save(recipeOutput);

        new RecipeBuilder("lecm_243", 1870, 308, 1.45, 66, 0.085, false, RadSources.LECm_243_FISSION).save(recipeOutput);
        new RecipeBuilder("hecm_243", 1870, 308 * 3, 1.5, 66 / 2, 0.085, false, RadSources.HECm_243_FISSION).save(recipeOutput);
        new RecipeBuilder("lecm_245", 3032, 190, 1.5, 75, 0.085, false, RadSources.LECm_245_FISSION).save(recipeOutput);
        new RecipeBuilder("hecm_245", 3032, 190 * 3, 1.55, 75 / 2, 0.085, false, RadSources.HECm_245_FISSION).save(recipeOutput);
        new RecipeBuilder("lecm_247", 2692, 214, 1.55, 72, 0.085, false, RadSources.LECm_247_FISSION).save(recipeOutput);
        new RecipeBuilder("hecm_247", 2692, 214 * 3, 1.6, 72 / 2, 0.085, false, RadSources.HECm_247_FISSION).save(recipeOutput);

        new RecipeBuilder("leb_248", 2716, 212, 1.65, 73, 0.09, false, RadSources.LEB_248_FISSION).save(recipeOutput);
        new RecipeBuilder("heb_248", 2716, 212 * 3, 1.7, 73 / 2, 0.09, false, RadSources.HEB_248_FISSION).save(recipeOutput);

        new RecipeBuilder("lecf_249", 1334, 432, 1.75, 60, 0.1, true, RadSources.LECf_249_FISSION).save(recipeOutput);
        new RecipeBuilder("hecf_249", 1334, 432 * 3, 1.8, 60 / 2, 0.1, true, RadSources.HECf_249_FISSION).save(recipeOutput);
        new RecipeBuilder("lecf_251", 2504, 230, 1.8, 71, 0.1, true, RadSources.LECf_251_FISSION).save(recipeOutput);
        new RecipeBuilder("hecf_251", 2504, 230 * 3, 1.85, 71 / 2, 0.1, true, RadSources.HECf_251_FISSION).save(recipeOutput);
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final SizedFluidIngredient input;
        private final SizedFluidIngredient output;
        private final int time;
        private final int heat;
        private final double efficiency;
        private final int criticality;
        private final double decayFactor;
        private final boolean selfPriming;
        private final double radiation;

        public RecipeBuilder(String fuelType, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
            super(ItemStack.EMPTY);
            this.input = sizedIngredient(FISSION_FUEL_MAP.get(fuelType + "_fluoride_flibe"), 1);
            this.output = sizedIngredient(FISSION_FUEL_MAP.get("depleted_" + fuelType + "_fluoride_flibe"), 1);
            this.time = time;
            this.heat = heat;
            this.efficiency = efficiency;
            this.criticality = criticality;
            this.decayFactor = decayFactor;
            this.selfPriming = selfPriming;
            this.radiation = radiation;
        }

        @Override
        public void save(RecipeOutput recipeOutput) {
            save(recipeOutput, getDefaultRecipeId(input, output));
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            SaltFissionRecipe recipe = new SaltFissionRecipe(this.input, this.output, this.time, this.heat, this.efficiency, this.criticality, this.decayFactor, this.selfPriming, this.radiation);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}