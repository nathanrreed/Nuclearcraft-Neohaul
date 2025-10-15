package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.SolidFissionRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class SolidFissionProvider {
    public SolidFissionProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder(FUEL_THORIUM_MAP.get("tbu_ox"), DEPLETED_FUEL_THORIUM_MAP.get("tbu_ox"), 14400, 40, 1.25, 234, 0.04, false, RadSources.TBU_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_THORIUM_MAP.get("tbu_ni"), DEPLETED_FUEL_THORIUM_MAP.get("tbu_ni"), 18000, 32, 1.25, 293, 0.04, false, RadSources.TBU_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_THORIUM_MAP.get("tbu_za"), DEPLETED_FUEL_THORIUM_MAP.get("tbu_za"), 11520, 50, 1.25, 199, 0.04, false, RadSources.TBU_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_URANIUM_MAP.get("leu_233_ox"), DEPLETED_FUEL_URANIUM_MAP.get("leu_233_ox"), 2666, 216, 1.1, 78, 0.065, false, RadSources.LEU_233_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_URANIUM_MAP.get("leu_233_ni"), DEPLETED_FUEL_URANIUM_MAP.get("leu_233_ni"), 3348, 172, 1.1, 98, 0.065, false, RadSources.LEU_233_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_URANIUM_MAP.get("leu_233_za"), DEPLETED_FUEL_URANIUM_MAP.get("leu_233_za"), 2134, 270, 1.1, 66, 0.065, false, RadSources.LEU_233_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_URANIUM_MAP.get("heu_233_ox"), DEPLETED_FUEL_URANIUM_MAP.get("heu_233_ox"), 2666, 216 * 3, 1.1, 78 / 2, 0.065, false, RadSources.HEU_233_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_URANIUM_MAP.get("heu_233_ni"), DEPLETED_FUEL_URANIUM_MAP.get("heu_233_ni"), 3348, 172 * 3, 1.1, 98 / 2, 0.065, false, RadSources.HEU_233_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_URANIUM_MAP.get("heu_233_za"), DEPLETED_FUEL_URANIUM_MAP.get("heu_233_za"), 2134, 270 * 3, 1.1, 66 / 2, 0.065, false, RadSources.HEU_233_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_URANIUM_MAP.get("leu_235_ox"), DEPLETED_FUEL_URANIUM_MAP.get("leu_235_ox"), 4800, 120, 1, 102, 0.065, false, RadSources.LEU_235_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_URANIUM_MAP.get("leu_235_ni"), DEPLETED_FUEL_URANIUM_MAP.get("leu_235_ni"), 6000, 96, 1, 128, 0.065, false, RadSources.LEU_235_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_URANIUM_MAP.get("leu_235_za"), DEPLETED_FUEL_URANIUM_MAP.get("leu_235_za"), 3840, 150, 1, 87, 0.065, false, RadSources.LEU_235_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_URANIUM_MAP.get("heu_235_ox"), DEPLETED_FUEL_URANIUM_MAP.get("heu_235_ox"), 4800, 120 * 3, 1.05, 102 / 2, 0.065, false, RadSources.HEU_235_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_URANIUM_MAP.get("heu_235_ni"), DEPLETED_FUEL_URANIUM_MAP.get("heu_235_ni"), 6000, 96 * 3, 1.05, 128 / 2, 0.065, false, RadSources.HEU_235_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_URANIUM_MAP.get("heu_235_za"), DEPLETED_FUEL_URANIUM_MAP.get("heu_235_za"), 3840, 150 * 3, 1.05, 87 / 2, 0.065, false, RadSources.HEU_235_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_NEPTUNIUM_MAP.get("len_236_ox"), DEPLETED_FUEL_NEPTUNIUM_MAP.get("len_236_ox"), 1972, 292, 1.1, 70, 0.07, false, RadSources.LEN_236_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_NEPTUNIUM_MAP.get("len_236_ni"), DEPLETED_FUEL_NEPTUNIUM_MAP.get("len_236_ni"), 2462, 234, 1.1, 88, 0.07, false, RadSources.LEN_236_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_NEPTUNIUM_MAP.get("len_236_za"), DEPLETED_FUEL_NEPTUNIUM_MAP.get("len_236_za"), 1574, 366, 1.1, 60, 0.07, false, RadSources.LEN_236_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_NEPTUNIUM_MAP.get("hen_236_ox"), DEPLETED_FUEL_NEPTUNIUM_MAP.get("hen_236_ox"), 1972, 292 * 3, 1.15, 70 / 2, 0.07, false, RadSources.HEN_236_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_NEPTUNIUM_MAP.get("hen_236_ni"), DEPLETED_FUEL_NEPTUNIUM_MAP.get("hen_236_ni"), 2462, 234 * 3, 1.15, 88 / 2, 0.07, false, RadSources.HEN_236_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_NEPTUNIUM_MAP.get("hen_236_za"), DEPLETED_FUEL_NEPTUNIUM_MAP.get("hen_236_za"), 1574, 366 * 3, 1.15, 60 / 2, 0.07, false, RadSources.HEN_236_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("lep_239_ox"), DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_239_ox"), 4572, 126, 1.2, 99, 0.075, false, RadSources.LEP_239_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("lep_239_ni"), DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_239_ni"), 5760, 100, 1.2, 124, 0.075, false, RadSources.LEP_239_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("lep_239_za"), DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_239_za"), 3646, 158, 1.2, 84, 0.075, false, RadSources.LEP_239_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("hep_239_ox"), DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_239_ox"), 4572, 126 * 3, 1.25, 99 / 2, 0.075, false, RadSources.HEP_239_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("hep_239_ni"), DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_239_ni"), 5760, 100 * 3, 1.25, 124 / 2, 0.075, false, RadSources.HEP_239_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("hep_239_za"), DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_239_za"), 3646, 158 * 3, 1.25, 84 / 2, 0.075, false, RadSources.HEP_239_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("lep_241_ox"), DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_241_ox"), 3164, 182, 1.25, 84, 0.075, false, RadSources.LEP_241_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("lep_241_ni"), DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_241_ni"), 3946, 146, 1.25, 105, 0.075, false, RadSources.LEP_241_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("lep_241_za"), DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_241_za"), 2526, 228, 1.25, 71, 0.075, false, RadSources.LEP_241_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("hep_241_ox"), DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_241_ox"), 3164, 182 * 3, 1.3, 84 / 2, 0.075, false, RadSources.HEP_241_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("hep_241_ni"), DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_241_ni"), 3946, 146 * 3, 1.3, 105 / 2, 0.075, false, RadSources.HEP_241_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_PLUTONIUM_MAP.get("hep_241_za"), DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_241_za"), 2526, 228 * 3, 1.3, 71 / 2, 0.075, false, RadSources.HEP_241_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_MIXED_MAP.get("mix_239_ox"), DEPLETED_FUEL_MIXED_MAP.get("mix_239_ox"), 4354, 132, 1.05, 94, 0.075, false, RadSources.MIX_239_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_MIXED_MAP.get("mix_239_ni"), DEPLETED_FUEL_MIXED_MAP.get("mix_239_ni"), 5486, 106, 1.05, 118, 0.075, false, RadSources.MIX_239_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_MIXED_MAP.get("mix_239_za"), DEPLETED_FUEL_MIXED_MAP.get("mix_239_za"), 3472, 166, 1.05, 80, 0.075, false, RadSources.MIX_239_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_MIXED_MAP.get("mix_241_ox"), DEPLETED_FUEL_MIXED_MAP.get("mix_241_ox"), 3014, 192, 1.15, 80, 0.075, false, RadSources.MIX_241_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_MIXED_MAP.get("mix_241_ni"), DEPLETED_FUEL_MIXED_MAP.get("mix_241_ni"), 3758, 154, 1.15, 100, 0.075, false, RadSources.MIX_241_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_MIXED_MAP.get("mix_241_za"), DEPLETED_FUEL_MIXED_MAP.get("mix_241_za"), 2406, 240, 1.15, 68, 0.075, false, RadSources.MIX_241_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_AMERICIUM_MAP.get("lea_242_ox"), DEPLETED_FUEL_AMERICIUM_MAP.get("lea_242_ox"), 1476, 390, 1.35, 65, 0.08, false, RadSources.LEA_242_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_AMERICIUM_MAP.get("lea_242_ni"), DEPLETED_FUEL_AMERICIUM_MAP.get("lea_242_ni"), 1846, 312, 1.35, 81, 0.08, false, RadSources.LEA_242_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_AMERICIUM_MAP.get("lea_242_za"), DEPLETED_FUEL_AMERICIUM_MAP.get("lea_242_za"), 1180, 488, 1.35, 55, 0.08, false, RadSources.LEA_242_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_AMERICIUM_MAP.get("hea_242_ox"), DEPLETED_FUEL_AMERICIUM_MAP.get("hea_242_ox"), 1476, 390 * 3, 1.4, 65 / 2, 0.08, false, RadSources.HEA_242_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_AMERICIUM_MAP.get("hea_242_ni"), DEPLETED_FUEL_AMERICIUM_MAP.get("hea_242_ni"), 1846, 312 * 3, 1.4, 81 / 2, 0.08, false, RadSources.HEA_242_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_AMERICIUM_MAP.get("hea_242_za"), DEPLETED_FUEL_AMERICIUM_MAP.get("hea_242_za"), 1180, 488 * 3, 1.4, 55 / 2, 0.08, false, RadSources.HEA_242_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_243_ox"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_243_ox"), 1500, 384, 1.45, 66, 0.085, false, RadSources.LECm_243_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_243_ni"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_243_ni"), 1870, 308, 1.45, 83, 0.085, false, RadSources.LECm_243_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_243_za"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_243_za"), 1200, 480, 1.45, 56, 0.085, false, RadSources.LECm_243_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_243_ox"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_243_ox"), 1500, 384 * 3, 1.5, 66 / 2, 0.085, false, RadSources.HECm_243_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_243_ni"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_243_ni"), 1870, 308 * 3, 1.5, 83 / 2, 0.085, false, RadSources.HECm_243_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_243_za"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_243_za"), 1200, 480 * 3, 1.5, 56 / 2, 0.085, false, RadSources.HECm_243_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_245_ox"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_245_ox"), 2420, 238, 1.5, 75, 0.085, false, RadSources.LECm_245_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_245_ni"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_245_ni"), 3032, 190, 1.5, 94, 0.085, false, RadSources.LECm_245_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_245_za"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_245_za"), 1932, 298, 1.5, 64, 0.085, false, RadSources.LECm_245_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_245_ox"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_245_ox"), 2420, 238 * 3, 1.55, 75 / 2, 0.085, false, RadSources.HECm_245_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_245_ni"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_245_ni"), 3032, 190 * 3, 1.55, 94 / 2, 0.085, false, RadSources.HECm_245_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_245_za"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_245_za"), 1932, 298 * 3, 1.55, 64 / 2, 0.085, false, RadSources.HECm_245_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_247_ox"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_247_ox"), 2150, 268, 1.55, 72, 0.085, false, RadSources.LECm_247_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_247_ni"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_247_ni"), 2692, 214, 1.55, 90, 0.085, false, RadSources.LECm_247_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("lecm_247_za"), DEPLETED_FUEL_CURIUM_MAP.get("lecm_247_za"), 1714, 336, 1.55, 61, 0.085, false, RadSources.LECm_247_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_247_ox"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_247_ox"), 2150, 268 * 3, 1.6, 72 / 2, 0.085, false, RadSources.HECm_247_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_247_ni"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_247_ni"), 2692, 214 * 3, 1.6, 90 / 2, 0.085, false, RadSources.HECm_247_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CURIUM_MAP.get("hecm_247_za"), DEPLETED_FUEL_CURIUM_MAP.get("hecm_247_za"), 1714, 336 * 3, 1.6, 61 / 2, 0.085, false, RadSources.HECm_247_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_BERKELIUM_MAP.get("leb_248_ox"), DEPLETED_FUEL_BERKELIUM_MAP.get("leb_248_ox"), 2166, 266, 1.65, 73, 0.09, false, RadSources.LEB_248_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_BERKELIUM_MAP.get("leb_248_ni"), DEPLETED_FUEL_BERKELIUM_MAP.get("leb_248_ni"), 2716, 212, 1.65, 91, 0.09, false, RadSources.LEB_248_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_BERKELIUM_MAP.get("leb_248_za"), DEPLETED_FUEL_BERKELIUM_MAP.get("leb_248_za"), 1734, 332, 1.65, 62, 0.09, false, RadSources.LEB_248_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_BERKELIUM_MAP.get("heb_248_ox"), DEPLETED_FUEL_BERKELIUM_MAP.get("heb_248_ox"), 2166, 266 * 3, 1.7, 73 / 2, 0.09, false, RadSources.HEB_248_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_BERKELIUM_MAP.get("heb_248_ni"), DEPLETED_FUEL_BERKELIUM_MAP.get("heb_248_ni"), 2716, 212 * 3, 1.7, 91 / 2, 0.09, false, RadSources.HEB_248_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_BERKELIUM_MAP.get("heb_248_za"), DEPLETED_FUEL_BERKELIUM_MAP.get("heb_248_za"), 1734, 332 * 3, 1.7, 62 / 2, 0.09, false, RadSources.HEB_248_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("lecf_249_ox"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_249_ox"), 1066, 540, 1.75, 60, 0.1, true, RadSources.LECf_249_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("lecf_249_ni"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_249_ni"), 1334, 432, 1.75, 75, 0.1, true, RadSources.LECf_249_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("lecf_249_za"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_249_za"), 852, 676, 1.75, 51, 0.1, true, RadSources.LECf_249_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("hecf_249_ox"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_249_ox"), 1066, 540 * 3, 1.8, 60 / 2, 0.1, true, RadSources.HECf_249_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("hecf_249_ni"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_249_ni"), 1334, 432 * 3, 1.8, 75 / 2, 0.1, true, RadSources.HECf_249_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("hecf_249_za"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_249_za"), 852, 676 * 3, 1.8, 51 / 2, 0.1, true, RadSources.HECf_249_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("lecf_251_ox"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_251_ox"), 2000, 288, 1.8, 71, 0.1, true, RadSources.LECf_251_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("lecf_251_ni"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_251_ni"), 2504, 230, 1.8, 89, 0.1, true, RadSources.LECf_251_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("lecf_251_za"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_251_za"), 1600, 360, 1.8, 60, 0.1, true, RadSources.LECf_251_FISSION).save(recipeOutput);

        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("hecf_251_ox"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_251_ox"), 2000, 288 * 3, 1.85, 71 / 2, 0.1, true, RadSources.HECf_251_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("hecf_251_ni"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_251_ni"), 2504, 230 * 3, 1.85, 89 / 2, 0.1, true, RadSources.HECf_251_FISSION).save(recipeOutput);
        new RecipeBuilder(FUEL_CALIFORNIUM_MAP.get("hecf_251_za"), DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_251_za"), 1600, 360 * 3, 1.85, 60 / 2, 0.1, true, RadSources.HECf_251_FISSION).save(recipeOutput);

//    TODO   addRecipe(getYelloriumIngredient(), "ingotCyanite", NCMath.toInt(fission_uranium_fuel_time[11] * 0.5D * 9D / 8D), NCMath.toInt(fission_uranium_heat_generation[11] * 8D / 9D), fission_uranium_efficiency[11], fission_uranium_criticality[11], fission_uranium_decay_factor[11], false, fission_uranium_radiation[11] * 8D / 9D);
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

            SolidFissionRecipe recipe = new SolidFissionRecipe(SizedIngredient.of(this.input.getItem(), 1), SizedIngredient.of(this.output.getItem(), 1), this.time, this.heat, this.efficiency, this.criticality, this.decayFactor, this.selfPriming, this.radiation);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}