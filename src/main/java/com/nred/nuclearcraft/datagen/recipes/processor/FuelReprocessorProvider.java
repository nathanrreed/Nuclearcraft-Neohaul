package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FuelReprocessorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.List;

import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class FuelReprocessorProvider {
    public FuelReprocessorProvider(RecipeOutput recipeOutput) {
        List<String> types = List.of("_c", "_ox", "_ni", "_za");
        for (String type : types) {
            ProcessorRecipeBuilder temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_THORIUM_MAP.get("tbu" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("233" + type), 1).addItemResult(URANIUM_MAP.get("238" + type), 5).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 25, 1).addItemResult(NEPTUNIUM_MAP.get("236" + type), 1).addItemResult(NEPTUNIUM_MAP.get("237" + type), 1).addItemResult(FISSION_DUST_MAP.get("caesium_137"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "tbu" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_URANIUM_MAP.get("leu_233" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("238" + type), 5).addItemResult(PLUTONIUM_MAP.get("241" + type), 1).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 25, 1).addItemResult(PLUTONIUM_MAP.get("242" + type), 1).addItemResult(AMERICIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("caesium_137"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "leu_233" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_URANIUM_MAP.get("heu_233" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("235" + type), 1).addItemResult(URANIUM_MAP.get("238" + type), 2).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 75, 1).addItemResult(PLUTONIUM_MAP.get("242" + type), 3).addItemResult(AMERICIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("caesium_137"), 75, 1);
            addOptionals(temp, type, true).save(recipeOutput, "heu_233" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_URANIUM_MAP.get("leu_235" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("238" + type), 4).addItemResult(PLUTONIUM_MAP.get("239" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 25, 1).addItemResult(PLUTONIUM_MAP.get("242" + type), 1).addItemResult(AMERICIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("caesium_137"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "leu_235" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_URANIUM_MAP.get("heu_235" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("238" + type), 3).addItemResult(NEPTUNIUM_MAP.get("236" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 75, 1).addItemResult(PLUTONIUM_MAP.get("242" + type), 2).addItemResult(AMERICIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("caesium_137"), 75, 1);
            addOptionals(temp, type, true).save(recipeOutput, "heu_235" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_NEPTUNIUM_MAP.get("len_236" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("238" + type), 4).addItemResult(NEPTUNIUM_MAP.get("237" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 25, 1).addItemResult(PLUTONIUM_MAP.get("241" + type), 1).addItemResult(PLUTONIUM_MAP.get("242" + type), 2).addItemResult(FISSION_DUST_MAP.get("caesium_137"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "len_236" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_NEPTUNIUM_MAP.get("hen_236" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("238" + type), 4).addItemResult(PLUTONIUM_MAP.get("238" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 75, 1).addItemResult(PLUTONIUM_MAP.get("241" + type), 1).addItemResult(PLUTONIUM_MAP.get("242" + type), 1).addItemResult(FISSION_DUST_MAP.get("caesium_137"), 75, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hen_236" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_239" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(PLUTONIUM_MAP.get("242" + type), 5).addItemResult(AMERICIUM_MAP.get("242" + type), 1).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 25, 1).addItemResult(AMERICIUM_MAP.get("243" + type), 1).addItemResult(CURIUM_MAP.get("246" + type), 2).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "lep_239" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_239" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(PLUTONIUM_MAP.get("241" + type), 1).addItemResult(AMERICIUM_MAP.get("242" + type), 1).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 75, 1).addItemResult(AMERICIUM_MAP.get("243" + type), 4).addItemResult(CURIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 75, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hep_239" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_PLUTONIUM_MAP.get("lep_241" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(PLUTONIUM_MAP.get("242" + type), 5).addItemResult(AMERICIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 25, 1).addItemResult(CURIUM_MAP.get("246" + type), 1).addItemResult(BERKELIUM_MAP.get("247" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "lep_241" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_PLUTONIUM_MAP.get("hep_241" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(AMERICIUM_MAP.get("241" + type), 1).addItemResult(AMERICIUM_MAP.get("242" + type), 1).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 75, 1).addItemResult(AMERICIUM_MAP.get("243" + type), 3).addItemResult(CURIUM_MAP.get("246" + type), 2).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 75, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hep_241" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_MIXED_MAP.get("mix_239" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("238" + type), 4).addItemResult(PLUTONIUM_MAP.get("241" + type), 1).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 25, 1).addItemResult(PLUTONIUM_MAP.get("242" + type), 2).addItemResult(AMERICIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "mix_239" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_MIXED_MAP.get("mix_241" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(URANIUM_MAP.get("238" + type), 3).addItemResult(PLUTONIUM_MAP.get("241" + type), 1).addItemResult(FISSION_DUST_MAP.get("strontium_90"), 25, 1).addItemResult(PLUTONIUM_MAP.get("242" + type), 3).addItemResult(AMERICIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 25, 1);
            addOptionals(temp, type, true).save(recipeOutput, "mix_241" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_AMERICIUM_MAP.get("lea_242" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(AMERICIUM_MAP.get("243" + type), 3).addItemResult(CURIUM_MAP.get("245" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 25, 1).addItemResult(CURIUM_MAP.get("246" + type), 3).addItemResult(BERKELIUM_MAP.get("248" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "lea_242" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_AMERICIUM_MAP.get("hea_242" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(AMERICIUM_MAP.get("243" + type), 3).addItemResult(CURIUM_MAP.get("243" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 75, 1).addItemResult(CURIUM_MAP.get("246" + type), 2).addItemResult(BERKELIUM_MAP.get("247" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 75, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hea_242" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CURIUM_MAP.get("lecm_243" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CURIUM_MAP.get("246" + type), 4).addItemResult(CURIUM_MAP.get("247" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 25, 1).addItemResult(BERKELIUM_MAP.get("247" + type), 2).addItemResult(BERKELIUM_MAP.get("248" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 25, 1);
            addOptionals(temp, type, false).save(recipeOutput, "lecm_243" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CURIUM_MAP.get("hecm_243" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CURIUM_MAP.get("245" + type), 1).addItemResult(CURIUM_MAP.get("246" + type), 3).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 75, 1).addItemResult(BERKELIUM_MAP.get("247" + type), 2).addItemResult(BERKELIUM_MAP.get("248" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 75, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hecm_243" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CURIUM_MAP.get("lecm_245" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CURIUM_MAP.get("246" + type), 4).addItemResult(CURIUM_MAP.get("247" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 30, 1).addItemResult(BERKELIUM_MAP.get("247" + type), 2).addItemResult(CALIFORNIUM_MAP.get("249" + type), 1).addItemResult(FISSION_DUST_MAP.get("europium_155"), 20, 1);
            addOptionals(temp, type, false).save(recipeOutput, "lecm_245" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CURIUM_MAP.get("hecm_245" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CURIUM_MAP.get("246" + type), 3).addItemResult(CURIUM_MAP.get("247" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 90, 1).addItemResult(BERKELIUM_MAP.get("247" + type), 2).addItemResult(CALIFORNIUM_MAP.get("249" + type), 1).addItemResult(FISSION_DUST_MAP.get("europium_155"), 60, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hecm_245" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CURIUM_MAP.get("lecm_247" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CURIUM_MAP.get("246" + type), 5).addItemResult(BERKELIUM_MAP.get("247" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 30, 1).addItemResult(BERKELIUM_MAP.get("248" + type), 1).addItemResult(CALIFORNIUM_MAP.get("249" + type), 1).addItemResult(FISSION_DUST_MAP.get("europium_155"), 20, 1);
            addOptionals(temp, type, false).save(recipeOutput, "lecm_247" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CURIUM_MAP.get("hecm_247" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(BERKELIUM_MAP.get("247" + type), 4).addItemResult(BERKELIUM_MAP.get("248" + type), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 90, 1).addItemResult(CALIFORNIUM_MAP.get("249" + type), 1).addItemResult(CALIFORNIUM_MAP.get("251" + type), 1).addItemResult(FISSION_DUST_MAP.get("europium_155"), 60, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hecm_247" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_BERKELIUM_MAP.get("leb_248" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(BERKELIUM_MAP.get("247" + type), 5).addItemResult(BERKELIUM_MAP.get("248" + type), 1).addItemResult(FISSION_DUST_MAP.get("ruthenium_106"), 30, 1).addItemResult(CALIFORNIUM_MAP.get("249" + type), 1).addItemResult(CALIFORNIUM_MAP.get("251" + type), 1).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 20, 1);
            addOptionals(temp, type, false).save(recipeOutput, "leb_248" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_BERKELIUM_MAP.get("heb_248" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(BERKELIUM_MAP.get("248" + type), 1).addItemResult(CALIFORNIUM_MAP.get("249" + type), 1).addItemResult(FISSION_DUST_MAP.get("ruthenium_106"), 90, 1).addItemResult(CALIFORNIUM_MAP.get("251" + type), 2).addItemResult(CALIFORNIUM_MAP.get("252" + type), 3).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 60, 1);
            addOptionals(temp, type, true).save(recipeOutput, "heb_248" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_249" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(FISSION_DUST_MAP.get("ruthenium_106"), 30, 1).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 20, 1);
            addOptionals(temp, type, false).save(recipeOutput, "lecf_249" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_249" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CALIFORNIUM_MAP.get("250" + type), 1).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(FISSION_DUST_MAP.get("ruthenium_106"), 90, 1).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(FISSION_DUST_MAP.get("promethium_147"), 60, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hecf_249" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CALIFORNIUM_MAP.get("lecf_251" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(FISSION_DUST_MAP.get("ruthenium_106"), 30, 1).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(FISSION_DUST_MAP.get("europium_155"), 20, 1);
            addOptionals(temp, type, false).save(recipeOutput, "lecf_251" + type);

            temp = new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_CALIFORNIUM_MAP.get("hecf_251" + (type.equals("_c") ? "_tr" : type)), 9).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(FISSION_DUST_MAP.get("ruthenium_106"), 90, 1).addItemResult(CALIFORNIUM_MAP.get("252" + type), 2).addItemResult(CALIFORNIUM_MAP.get("252" + type), 1).addItemResult(FISSION_DUST_MAP.get("europium_155"), 60, 1);
            addOptionals(temp, type, true).save(recipeOutput, "hecf_251" + type);
        }
    }

    private ProcessorRecipeBuilder addOptionals(ProcessorRecipeBuilder temp, String type, boolean extra) {
        if (type.equals("_c")) {
            temp.addItemResult(DUST_MAP.get("graphite"), extra ? 4 : 3);
            temp.addItemResult(ALLOY_MAP.get("silicon_carbide"), 1);
        } else if (type.equals("_za")) {
            temp.addItemResult(DUST_MAP.get("zirconium"), extra ? 2 : 1);
        }

        return temp;
    }
}