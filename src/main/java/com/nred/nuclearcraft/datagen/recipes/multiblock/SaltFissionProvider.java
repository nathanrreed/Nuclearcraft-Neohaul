package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.SaltFissionRecipe;
import net.minecraft.data.recipes.RecipeOutput;

public class SaltFissionProvider {
    public SaltFissionProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new SaltFissionRecipe("tbu", 18000, 32, 1.25, 234, 0.04, false, RadSources.TBU_FISSION)).save(recipeOutput);

        new BasicRecipeBuilder<>(new SaltFissionRecipe("leu_233", 3348, 172, 1.1, 78, 0.065, false, RadSources.LEU_233_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("heu_233", 3348, 172 * 3, 1.15, 78 / 2, 0.065, false, RadSources.HEU_233_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("leu_235", 6000, 96, 1, 102, 0.065, false, RadSources.LEU_235_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("heu_235", 6000, 96 * 3, 1.05, 102 / 2, 0.065, false, RadSources.HEU_235_FISSION)).save(recipeOutput);

        new BasicRecipeBuilder<>(new SaltFissionRecipe("len_236", 2462, 234, 1.1, 70, 0.07, false, RadSources.LEN_236_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hen_236", 2462, 234 * 3, 1.15, 70 / 2, 0.07, false, RadSources.HEN_236_FISSION)).save(recipeOutput);

        new BasicRecipeBuilder<>(new SaltFissionRecipe("lep_239", 5760, 100, 1.2, 99, 0.075, false, RadSources.LEP_239_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hep_239", 5760, 100 * 3, 1.25, 99 / 2, 0.075, false, RadSources.HEP_239_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("lep_241", 3946, 146, 1.25, 84, 0.075, false, RadSources.LEP_241_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hep_241", 3946, 146 * 3, 1.3, 84 / 2, 0.075, false, RadSources.HEP_241_FISSION)).save(recipeOutput);

        new BasicRecipeBuilder<>(new SaltFissionRecipe("mix_239", 5486, 106, 1.05, 94, 0.075, false, RadSources.MIX_239_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("mix_241", 3758, 154, 1.15, 80, 0.075, false, RadSources.MIX_241_FISSION)).save(recipeOutput);

        new BasicRecipeBuilder<>(new SaltFissionRecipe("lea_242", 1846, 312, 1.35, 65, 0.08, false, RadSources.LEA_242_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hea_242", 1846, 312 * 3, 1.4, 65 / 2, 0.08, false, RadSources.HEA_242_FISSION)).save(recipeOutput);

        new BasicRecipeBuilder<>(new SaltFissionRecipe("lecm_243", 1870, 308, 1.45, 66, 0.085, false, RadSources.LECm_243_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hecm_243", 1870, 308 * 3, 1.5, 66 / 2, 0.085, false, RadSources.HECm_243_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("lecm_245", 3032, 190, 1.5, 75, 0.085, false, RadSources.LECm_245_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hecm_245", 3032, 190 * 3, 1.55, 75 / 2, 0.085, false, RadSources.HECm_245_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("lecm_247", 2692, 214, 1.55, 72, 0.085, false, RadSources.LECm_247_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hecm_247", 2692, 214 * 3, 1.6, 72 / 2, 0.085, false, RadSources.HECm_247_FISSION)).save(recipeOutput);

        new BasicRecipeBuilder<>(new SaltFissionRecipe("leb_248", 2716, 212, 1.65, 73, 0.09, false, RadSources.LEB_248_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("heb_248", 2716, 212 * 3, 1.7, 73 / 2, 0.09, false, RadSources.HEB_248_FISSION)).save(recipeOutput);

        new BasicRecipeBuilder<>(new SaltFissionRecipe("lecf_249", 1334, 432, 1.75, 60, 0.1, true, RadSources.LECf_249_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hecf_249", 1334, 432 * 3, 1.8, 60 / 2, 0.1, true, RadSources.HECf_249_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("lecf_251", 2504, 230, 1.8, 71, 0.1, true, RadSources.LECf_251_FISSION)).save(recipeOutput);
        new BasicRecipeBuilder<>(new SaltFissionRecipe("hecf_251", 2504, 230 * 3, 1.85, 71 / 2, 0.1, true, RadSources.HECf_251_FISSION)).save(recipeOutput);
    }
}