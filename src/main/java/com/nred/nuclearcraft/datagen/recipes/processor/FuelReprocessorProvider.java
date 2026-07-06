package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.FuelReprocessorRecipe;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class FuelReprocessorProvider {
    public FuelReprocessorProvider(RecipeOutput recipeOutput) {
        addReprocessingRecipes(recipeOutput, "tbu", DEPLETED_FUEL_THORIUM_MAP, "Uranium233", 1, "Uranium238", 5, "Neptunium236", 1, "Neptunium237", 1, "strontium_90", "caesium_137", 0.5D, 50);

        addReprocessingRecipes(recipeOutput, "leu_233", DEPLETED_FUEL_URANIUM_MAP, "Uranium238", 5, "Plutonium241", 1, "Plutonium242", 1, "Americium243", 1, "strontium_90", "caesium_137", 0.5D, 50);
        addReprocessingRecipes(recipeOutput, "heu_233", DEPLETED_FUEL_URANIUM_MAP, "Uranium235", 1, "Uranium238", 2, "Plutonium242", 3, "Americium243", 1, "strontium_90", "caesium_137", 1.5D, 50);
        addReprocessingRecipes(recipeOutput, "leu_235", DEPLETED_FUEL_URANIUM_MAP, "Uranium238", 4, "Plutonium239", 1, "Plutonium242", 2, "Americium243", 1, "molybdenum", "caesium_137", 0.5D, 50);
        addReprocessingRecipes(recipeOutput, "heu_235", DEPLETED_FUEL_URANIUM_MAP, "Uranium238", 3, "Neptunium236", 1, "Plutonium242", 2, "Americium243", 1, "molybdenum", "caesium_137", 1.5D, 50);

        addReprocessingRecipes(recipeOutput, "len_236", DEPLETED_FUEL_NEPTUNIUM_MAP, "Uranium238", 4, "Neptunium237", 1, "Plutonium241", 1, "Plutonium242", 2, "molybdenum", "caesium_137", 0.5D, 50);
        addReprocessingRecipes(recipeOutput, "hen_236", DEPLETED_FUEL_NEPTUNIUM_MAP, "Uranium238", 4, "Plutonium238", 1, "Plutonium241", 1, "Plutonium242", 1, "molybdenum", "caesium_137", 1.5D, 50);

        addReprocessingRecipes(recipeOutput, "lep_239", DEPLETED_FUEL_PLUTONIUM_MAP, "Plutonium242", 5, "Americium242", 1, "Americium243", 1, "Curium246", 1, "strontium_90", "promethium_147", 0.5D, 50);
        addReprocessingRecipes(recipeOutput, "hep_239", DEPLETED_FUEL_PLUTONIUM_MAP, "Plutonium241", 1, "Americium242", 1, "Americium243", 4, "Curium243", 1, "strontium_90", "promethium_147", 1.5D, 50);
        addReprocessingRecipes(recipeOutput, "lep_241", DEPLETED_FUEL_PLUTONIUM_MAP, "Plutonium242", 5, "Americium243", 1, "Curium246", 1, "Berkelium247", 1, "strontium_90", "promethium_147", 0.5D, 50);
        addReprocessingRecipes(recipeOutput, "hep_241", DEPLETED_FUEL_PLUTONIUM_MAP, "Americium241", 1, "Americium242", 1, "Americium243", 3, "Curium246", 2, "strontium_90", "promethium_147", 1.5D, 50);

        addReprocessingRecipes(recipeOutput, "mix_239", DEPLETED_FUEL_MIXED_MAP, "Uranium238", 4, "Plutonium241", 1, "Plutonium242", 2, "Americium243", 1, "strontium_90", "promethium_147", 0.5D, 50);
        addReprocessingRecipes(recipeOutput, "mix_241", DEPLETED_FUEL_MIXED_MAP, "Uranium238", 3, "Plutonium241", 1, "Plutonium242", 3, "Americium243", 1, "strontium_90", "promethium_147", 0.5D, 50);

        addReprocessingRecipes(recipeOutput, "lea_242", DEPLETED_FUEL_AMERICIUM_MAP, "Americium243", 3, "Curium245", 1, "Curium246", 3, "Berkelium248", 1, "molybdenum", "promethium_147", 0.5D, 50);
        addReprocessingRecipes(recipeOutput, "hea_242", DEPLETED_FUEL_AMERICIUM_MAP, "Americium243", 3, "Curium243", 1, "Curium246", 2, "Berkelium247", 1, "molybdenum", "promethium_147", 1.5D, 50);

        addReprocessingRecipes(recipeOutput, "lecm_243", DEPLETED_FUEL_CURIUM_MAP, "Curium246", 4, "Curium247", 1, "Berkelium247", 2, "Berkelium248", 1, "molybdenum", "promethium_147", 0.5D, 50);
        addReprocessingRecipes(recipeOutput, "hecm_243", DEPLETED_FUEL_CURIUM_MAP, "Curium245", 1, "Curium246", 3, "Berkelium247", 2, "Berkelium248", 1, "molybdenum", "promethium_147", 1.5D, 50);
        addReprocessingRecipes(recipeOutput, "lecm_245", DEPLETED_FUEL_CURIUM_MAP, "Curium246", 4, "Curium247", 1, "Berkelium247", 2, "Californium249", 1, "molybdenum", "europium_155", 0.5D, 60);
        addReprocessingRecipes(recipeOutput, "hecm_245", DEPLETED_FUEL_CURIUM_MAP, "Curium246", 3, "Curium247", 1, "Berkelium247", 2, "Californium249", 1, "molybdenum", "europium_155", 1.5D, 60);
        addReprocessingRecipes(recipeOutput, "lecm_247", DEPLETED_FUEL_CURIUM_MAP, "Curium246", 5, "Berkelium247", 1, "Berkelium248", 1, "Californium249", 1, "molybdenum", "europium_155", 0.5D, 60);
        addReprocessingRecipes(recipeOutput, "hecm_247", DEPLETED_FUEL_CURIUM_MAP, "Berkelium247", 4, "Berkelium248", 1, "Californium249", 1, "Californium251", 1, "molybdenum", "europium_155", 1.5D, 60);

        addReprocessingRecipes(recipeOutput, "leb_248", DEPLETED_FUEL_BERKELIUM_MAP, "Berkelium247", 5, "Berkelium248", 1, "Californium249", 1, "Californium251", 1, "ruthenium_106", "promethium_147", 0.5D, 60);
        addReprocessingRecipes(recipeOutput, "heb_248", DEPLETED_FUEL_BERKELIUM_MAP, "Berkelium248", 1, "Californium249", 1, "Californium251", 2, "Californium252", 3, "ruthenium_106", "promethium_147", 1.5D, 60);

        addReprocessingRecipes(recipeOutput, "lecf_249", DEPLETED_FUEL_CALIFORNIUM_MAP, "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 2, "ruthenium_106", "promethium_147", 0.5D, 60);
        addReprocessingRecipes(recipeOutput, "hecf_249", DEPLETED_FUEL_CALIFORNIUM_MAP, "Californium250", 1, "Californium252", 2, "Californium252", 2, "Californium252", 2, "ruthenium_106", "promethium_147", 1.5D, 60);
        addReprocessingRecipes(recipeOutput, "lecf_251", DEPLETED_FUEL_CALIFORNIUM_MAP, "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 2, "ruthenium_106", "europium_155", 0.5D, 60);
        addReprocessingRecipes(recipeOutput, "hecf_251", DEPLETED_FUEL_CALIFORNIUM_MAP, "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 1, "ruthenium_106", "europium_155", 1.5D, 60);
    }

    public static void addReprocessingRecipes(RecipeOutput recipeOutput, String fuel, Map<String, DeferredItem<Item>> depleted_map, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4, String waste1, String waste2, double m, int r) {
        int extraReturn = 9 - n1 - n2 - n3 - n4;
        new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(depleted_map.get(fuel + "_tr"), 9).addItemResult(get(out1 + "_c", n1)).addItemResult(get(out2 + "_c", n2)).addItemResult(wasteStack(waste1, m * r)).addItemResult(DUST_MAP.get("graphite"), extraReturn + 2).addItemResult(get(out3 + "_c", n3)).addItemResult(get(out4 + "_c", n4)).addItemResult(wasteStack(waste2, m * (100 - r))).addItemResult(ALLOY_MAP.get("silicon_carbide"), 1).save(recipeOutput, ncLoc(fuel + "_tr"));
        new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(depleted_map.get(fuel + "_ox"), 9).addItemResult(get(out1 + "_ox", n1)).addItemResult(get(out2 + "_ox", n2)).addItemResult(wasteStack(waste1, m * r)).addItemResult(get(out3 + "_ox", n3)).addItemResult(get(out4 + "_ox", n4)).addItemResult(wasteStack(waste2, m * (100 - r))).save(recipeOutput, ncLoc(fuel + "_ox"));
        new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(depleted_map.get(fuel + "_ni"), 9).addItemResult(get(out1 + "_ni", n1)).addItemResult(get(out2 + "_ni", n2)).addItemResult(wasteStack(waste1, m * r)).addItemResult(get(out3 + "_ni", n3)).addItemResult(get(out4 + "_ni", n4)).addItemResult(wasteStack(waste2, m * (100 - r))).save(recipeOutput, ncLoc(fuel + "_ni"));
        new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(depleted_map.get(fuel + "_za"), 9).addItemResult(get(out1 + "_za", n1)).addItemResult(get(out2 + "_za", n2)).addItemResult(wasteStack(waste1, m * r)).addItemResult(DUST_MAP.get("zirconium"), extraReturn).addItemResult(get(out3 + "_za", n3)).addItemResult(get(out4 + "_za", n4)).addItemResult(wasteStack(waste2, m * (100 - r))).save(recipeOutput, ncLoc(fuel + "_za"));
    }

    private static SizedChanceItemIngredient wasteStack(String waste, double chancePercent) {
        return SizedChanceItemIngredient.of(FISSION_DUST_MAP.get(waste), 2, NCMath.toInt(chancePercent));
    }

    private static SizedChanceItemIngredient get(String name, int size) {
        String type = name.replaceFirst("^\\D+", "");
        name = name.replaceFirst("\\d.*", "");
        return SizedChanceItemIngredient.of(switch (name) {
            case "Americium" -> AMERICIUM_MAP.get(type);
            case "Berkelium" -> BERKELIUM_MAP.get(type);
            case "Boron" -> BORON_MAP.get(type);
            case "Californium" -> CALIFORNIUM_MAP.get(type);
            case "Curium" -> CURIUM_MAP.get(type);
            case "Neptunium" -> NEPTUNIUM_MAP.get(type);
            case "Plutonium" -> PLUTONIUM_MAP.get(type);
            case "Uranium" -> URANIUM_MAP.get(type);
            default -> throw new IllegalStateException("Unexpected value: " + name);
        }, size);
    }
}