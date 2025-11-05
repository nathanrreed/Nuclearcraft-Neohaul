package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class PebbleFissionProvider {
    public PebbleFissionProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder(FUEL_THORIUM_MAP.get("tbu_tr"), DEPLETED_FUEL_THORIUM_MAP.get("tbu_tr"), 14400, 40, 1.25, 199, 0.04, false, RadSources.TBU_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_URANIUM_MAP.get("leu_233_tr"), DEPLETED_FUEL_URANIUM_MAP.get("leu_233_tr"), 2666, 216, 1.1, 66, 0.065, false, RadSources.LEU_233_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_URANIUM_MAP.get("heu_233_tr"), DEPLETED_FUEL_URANIUM_MAP.get("heu_233_tr"), 2666, 216 * 3, 1.1, 66 / 2, 0.065, false, RadSources.HEU_233_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_URANIUM_MAP.get("leu_235_tr"), DEPLETED_FUEL_URANIUM_MAP.get("leu_235_tr"), 4800, 120, 1, 87, 0.065, false, RadSources.LEU_235_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_URANIUM_MAP.get("heu_235_tr"), DEPLETED_FUEL_URANIUM_MAP.get("heu_235_tr"), 4800, 120 * 3, 1.05, 87 / 2, 0.065, false, RadSources.HEU_235_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_NEPTUNIUM_MAP.get("len_236_tr"), DEPLETED_FUEL_NEPTUNIUM_MAP.get("len_236_tr"), 1972, 292, 1.1, 60, 0.07, false, RadSources.LEN_236_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_NEPTUNIUM_MAP.get("hen_236_tr"), DEPLETED_FUEL_NEPTUNIUM_MAP.get("hen_236_tr"), 1972, 292 * 3, 1.15, 60 / 2, 0.07, false, RadSources.HEN_236_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("lep_239_tr"), DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_239_tr"), 4572, 126, 1.2, 84, 0.075, false, RadSources.LEP_239_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("hep_239_tr"), DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_239_tr"), 4572, 126 * 3, 1.25, 84 / 2, 0.075, false, RadSources.HEP_239_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("lep_241_tr"), DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_241_tr"), 3164, 182, 1.25, 71, 0.075, false, RadSources.LEP_241_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("hep_241_tr"), DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_241_tr"), 3164, 182 * 3, 1.3, 71 / 2, 0.075, false, RadSources.HEP_241_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_MIXED_MAP.get("mix_239_tr"), DEPLETED_FUEL_MIXED_MAP.get("mix_239_tr"), 4354, 132, 1.05, 80, 0.075, false, RadSources.MIX_239_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_MIXED_MAP.get("mix_241_tr"), DEPLETED_FUEL_MIXED_MAP.get("mix_241_tr"), 3014, 192, 1.15, 68, 0.075, false, RadSources.MIX_241_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_AMERICIUM_MAP.get("lea_242_tr"), DEPLETED_FUEL_AMERICIUM_MAP.get("lea_242_tr"), 1476, 390, 1.35, 55, 0.08, false, RadSources.LEA_242_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_AMERICIUM_MAP.get("hea_242_tr"), DEPLETED_FUEL_AMERICIUM_MAP.get("hea_242_tr"), 1476, 390 * 3, 1.4, 55 / 2, 0.08, false, RadSources.HEA_242_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_243_tr"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_243_tr"), 1500, 384, 1.45, 56, 0.085, false, RadSources.LECm_243_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_243_tr"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_243_tr"), 1500, 384 * 3, 1.5, 56 / 2, 0.085, false, RadSources.HECm_243_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_245_tr"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_245_tr"), 2420, 238, 1.5, 64, 0.085, false, RadSources.LECm_245_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_245_tr"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_245_tr"), 2420, 238 * 3, 1.55, 64 / 2, 0.085, false, RadSources.HECm_245_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_247_tr"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_247_tr"), 2150, 268, 1.55, 61, 0.085, false, RadSources.LECm_247_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_247_tr"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_247_tr"), 2150, 268 * 3, 1.6, 61 / 2, 0.085, false, RadSources.HECm_247_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_BERKELIUM_MAP.get("leb_248_tr"), DEPLETED_FUEL_BERKELIUM_MAP.get("leb_248_tr"), 2166, 266, 1.65, 62, 0.09, false, RadSources.LEB_248_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_BERKELIUM_MAP.get("heb_248_tr"), DEPLETED_FUEL_BERKELIUM_MAP.get("heb_248_tr"), 2166, 266 * 3, 1.7, 62 / 2, 0.09, false, RadSources.HEB_248_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("lecf_249_tr"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_249_tr"), 1066, 540, 1.75, 51, 0.1, true, RadSources.LECf_249_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("hecf_249_tr"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_249_tr"), 1066, 540 * 3, 1.8, 51 / 2, 0.1, true, RadSources.HECf_249_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("lecf_251_tr"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_251_tr"), 2000, 288, 1.8, 60, 0.1, true, RadSources.LECf_251_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("hecf_251_tr"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_251_tr"), 2000, 288 * 3, 1.85, 60 / 2, 0.1, true, RadSources.HECf_251_FISSION).save(recipeOutput);
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final ItemStack input;
        private final ItemStack output;
        private final int time;
        private final int heat;
        private final double efficiency;
        private final int criticality;
        private final double decayFactor;
        private final boolean selfPriming;
        private final double radiation;

        public RecipeBuilder(DeferredItem<Item> input, DeferredItem<Item> output, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
            super(output.toStack());
            this.input = input.toStack();
            this.output = output.toStack();
            this.time = time;
            this.heat = heat;
            this.efficiency = efficiency;
            this.criticality = criticality;
            this.decayFactor = decayFactor;
            this.selfPriming = selfPriming;
            this.radiation = radiation;
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            PebbleFissionRecipe recipe = new PebbleFissionRecipe(SizedChanceItemIngredient.of(this.input.getItem(), 1), SizedChanceItemIngredient.of(this.output.getItem(), 1), this.time, this.heat, this.efficiency, this.criticality, this.decayFactor, this.selfPriming, this.radiation);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}