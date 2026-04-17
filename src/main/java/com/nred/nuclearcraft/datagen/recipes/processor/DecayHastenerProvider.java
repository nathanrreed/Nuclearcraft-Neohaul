package com.nred.nuclearcraft.datagen.recipes.processor;

import com.google.common.collect.Sets;
import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.processor.DecayHastenerRecipe;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class DecayHastenerProvider {
    public DecayHastenerProvider(RecipeOutput recipeOutput) {
        addIsotopes(recipeOutput, INGOT_MAP, "thorium", DUST_MAP, "lead", RadSources.THORIUM);
//        addIsotopes(recipeOutput, FISSION_DUST_MAP, "bismuth", DUST_MAP, "thallium", RadSources.BISMUTH);

        addIsotopes(recipeOutput, FISSION_DUST_MAP, "radium", DUST_MAP, "lead", RadSources.RADIUM);
        addIsotopes(recipeOutput, FISSION_DUST_MAP, "polonium", DUST_MAP, "lead", RadSources.POLONIUM);

        addIsotopes(recipeOutput, FISSION_DUST_MAP, "tbp", PELLET_THORIUM_MAP, "tbu", RadSources.TBP);
        addIsotopes(recipeOutput, FISSION_DUST_MAP, "protactinium_233", URANIUM_MAP, "233", RadSources.PROTACTINIUM_233);

        addIsotopes(recipeOutput, FISSION_DUST_MAP, "strontium_90", DUST_MAP, "zirconium", RadSources.STRONTIUM_90);
        addIsotopes(recipeOutput, FISSION_DUST_MAP, "ruthenium_106", DUST_MAP, "palladium", RadSources.RUTHENIUM_106);
//        addIsotopes(recipeOutput, FISSION_DUST_MAP, "caesium_137", DUST_MAP, "barium", RadSources.CAESIUM_137);
//        addIsotopes(recipeOutput, FISSION_DUST_MAP, "promethium_147", DUST_MAP, "neodymium", RadSources.PROMETHIUM_147);
//        addIsotopes(recipeOutput, FISSION_DUST_MAP, "europium_155", DUST_MAP, "gadolinium", RadSources.EUROPIUM_155);

        addIsotopes(recipeOutput, URANIUM_MAP, "233", FISSION_DUST_MAP, "bismuth", RadSources.URANIUM_233);
        addIsotopes(recipeOutput, URANIUM_MAP, "235", DUST_MAP, "lead", RadSources.URANIUM_235);
        addIsotopes(recipeOutput, URANIUM_MAP, "238", FISSION_DUST_MAP, "radium", RadSources.URANIUM_238);

        addIsotopes(recipeOutput, NEPTUNIUM_MAP, "236", DUST_MAP, "thorium", RadSources.NEPTUNIUM_236);
        addIsotopes(recipeOutput, NEPTUNIUM_MAP, "237", URANIUM_MAP, "233", RadSources.NEPTUNIUM_237);

        addIsotopes(recipeOutput, PLUTONIUM_MAP, "238", DUST_MAP, "lead", RadSources.PLUTONIUM_238);
        addIsotopes(recipeOutput, PLUTONIUM_MAP, "239", URANIUM_MAP, "235", RadSources.PLUTONIUM_239);
        addIsotopes(recipeOutput, PLUTONIUM_MAP, "241", NEPTUNIUM_MAP, "237", RadSources.PLUTONIUM_241);
        addIsotopes(recipeOutput, PLUTONIUM_MAP, "242", URANIUM_MAP, "238", RadSources.PLUTONIUM_242);

        addIsotopes(recipeOutput, AMERICIUM_MAP, "241", NEPTUNIUM_MAP, "237", RadSources.AMERICIUM_241);
        addIsotopes(recipeOutput, AMERICIUM_MAP, "242", DUST_MAP, "lead", RadSources.AMERICIUM_242);
        addIsotopes(recipeOutput, AMERICIUM_MAP, "243", PLUTONIUM_MAP, "239", RadSources.AMERICIUM_243);

        addIsotopes(recipeOutput, CURIUM_MAP, "243", PLUTONIUM_MAP, "239", RadSources.CURIUM_243);
        addIsotopes(recipeOutput, CURIUM_MAP, "245", PLUTONIUM_MAP, "241", RadSources.CURIUM_245);
        addIsotopes(recipeOutput, CURIUM_MAP, "246", PLUTONIUM_MAP, "242", RadSources.CURIUM_246);
        addIsotopes(recipeOutput, CURIUM_MAP, "247", AMERICIUM_MAP, "243", RadSources.CURIUM_247);

        addIsotopes(recipeOutput, BERKELIUM_MAP, "247", AMERICIUM_MAP, "243", RadSources.BERKELIUM_247);
        addIsotopes(recipeOutput, BERKELIUM_MAP, "248", DUST_MAP, "thorium", RadSources.BERKELIUM_248);

        addIsotopes(recipeOutput, CALIFORNIUM_MAP, "249", CURIUM_MAP, "245", RadSources.CALIFORNIUM_249);
        addIsotopes(recipeOutput, CALIFORNIUM_MAP, "250", CURIUM_MAP, "246", RadSources.CALIFORNIUM_250);
        addIsotopes(recipeOutput, CALIFORNIUM_MAP, "251", CURIUM_MAP, "247", RadSources.CALIFORNIUM_251);
        addIsotopes(recipeOutput, CALIFORNIUM_MAP, "252", DUST_MAP, "thorium", RadSources.CALIFORNIUM_252);
    }

    private static final Set<String> NON_FISSION = Sets.newHashSet("thorium", "lead", "bismuth", "thallium", "radium", "polonium", "tbp", "zirconium", "palladium", "barium", "neodymium", "gadolinium");

    private void addIsotopes(RecipeOutput recipeOutput, Map<String, DeferredItem<Item>> inputMap, String input, Map<String, DeferredItem<Item>> outputMap, String output, double radiation) {
        double timeMult = NCMath.roundTo(RecipeHelper.getDecayTimeMultiplier(1E-6D, radiation, 3.16E-7D), 5D / 800);

        if (NON_FISSION.contains(output)) {
            Stream<DeferredItem<Item>> inputs = Stream.of(inputMap.getOrDefault(input, null), inputMap.getOrDefault(input + "_ox", null), inputMap.getOrDefault(input + "_ni", null));
            new ProcessorRecipeBuilder(DecayHastenerRecipe.class, timeMult, 1.0, radiation).addItemInput(Ingredient.of(inputs.filter(Objects::nonNull).toArray(ItemLike[]::new)), 1).addItemResult(outputMap.get(output), 1).save(recipeOutput, output + "_from_" + input);
        } else {
            for (String suffix : List.of("", "_c", "_ox", "_ni", "_za")) {
                if (!inputMap.containsKey(input + suffix) || !outputMap.containsKey(output + suffix)) continue;
                new ProcessorRecipeBuilder(DecayHastenerRecipe.class, timeMult, 1.0, radiation).addItemInput(inputMap.get(input + suffix), 1).addItemResult(outputMap.get(output + suffix), 1).save(recipeOutput, outputMap.get(output + suffix).getId().getPath() + "_from_" + inputMap.get(input + suffix).getId().getPath());
            }
        }
    }
}