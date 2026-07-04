package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.info.NCFluid;
import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.processor.CentrifugeRecipe;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.List;

import static com.nred.nuclearcraft.helpers.FissionConstants.FISSION_FUEL_FLUIDS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.util.FluidStackHelper.*;

public class CentrifugeProvider {
    public CentrifugeProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 4 / 3.0, 1).addFluidInput(MOLTEN_MAP.get("boron"), NUGGET_VOLUME * 12).addFluidResult(MOLTEN_MAP.get("boron_11"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("boron_10"), NUGGET_VOLUME * 3).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 10 / 9.0, 1).addFluidInput(MOLTEN_MAP.get("lithium"), NUGGET_VOLUME * 10).addFluidResult(MOLTEN_MAP.get("lithium_7"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("lithium_6"), NUGGET_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 1, 0.5).addFluidInput(FLAMMABLE_MAP.get("redstone_ethanol"), BUCKET_VOLUME / 2).addFluidResult(FLAMMABLE_MAP.get("ethanol"), BUCKET_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("redstone"), REDSTONE_DUST_VOLUME * 4).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 0.5).addFluidInput(MOLTEN_MAP.get("bacro_nio"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("nickel_oxide"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("bacro"), INGOT_VOLUME / 2).save(recipeOutput);

        for (String name : List.of("iron", "gold", "prismarine", "copper", "tin", "lead", "boron", "lithium", "magnesium", "manganese", "aluminum", "silver", "enderium")) {
            new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get(name + "_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get(name), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        }
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("redstone_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("redstone"), REDSTONE_DUST_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("quartz_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("quartz"), GEM_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("obsidian_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("obsidian"), SEARED_MATERIAL_VOLUME * 5).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("nether_brick_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("nether_brick"), SEARED_MATERIAL_VOLUME * 5).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("glowstone_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("glowstone"), BUCKET_VOLUME / 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("lapis_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("lapis"), GEM_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("slime_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("slime"), INGOT_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("end_stone_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("end_stone"), SEARED_MATERIAL_VOLUME * 5).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("purpur_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("purpur"), SEARED_MATERIAL_VOLUME * 5).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("diamond_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("diamond"), GEM_VOLUME).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("emerald_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("emerald"), GEM_VOLUME).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("fluorite_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("fluorite"), GEM_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("villiaumite_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("villiaumite"), GEM_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("carobbiite_nak"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("carobbiite"), GEM_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("arsenic_nak"), INGOT_VOLUME).addFluidResult(HOT_GAS_MAP.get("arsenic"), GEM_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("liquid_nitrogen_nak"), INGOT_VOLUME).addFluidResult(CUSTOM_FLUID_MAP.get("liquid_nitrogen"), BUCKET_VOLUME / 4).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("liquid_helium_nak"), INGOT_VOLUME).addFluidResult(CUSTOM_FLUID_MAP.get("liquid_helium"), BUCKET_VOLUME / 4).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(COOLANT_MAP.get("cryotheum_nak"), INGOT_VOLUME).addFluidResult(CUSTOM_FLUID_MAP.get("cryotheum"), BUCKET_VOLUME / 4).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);

        // Fission Materials

        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 10 / 9.0, 1).addFluidInput(MOLTEN_MAP.get("uranium"), NUGGET_VOLUME * 10).addFluidResult(FISSION_FUEL_MAP.get("uranium_238"), INGOT_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("uranium_235"), NUGGET_VOLUME).save(recipeOutput);

        addFissionFuelIsotopeRecipes(recipeOutput, "u", "uranium", 238, 233, 235);
        addFissionFuelIsotopeRecipes(recipeOutput, "n", "neptunium", 237, 236);
        addFissionFuelIsotopeRecipes(recipeOutput, "p", "plutonium", 242, 239, 241);
        for (int fissile : new int[]{239, 241}) {
            new ProcessorRecipeBuilder(CentrifugeRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("mix_" + fissile), INGOT_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("uranium_238"), NUGGET_VOLUME * 8).addFluidResult(FISSION_FUEL_MAP.get("plutonium_" + fissile), NUGGET_VOLUME).save(recipeOutput);
        }
        addFissionFuelIsotopeRecipes(recipeOutput, "a", "americium", 243, 242);
        addFissionFuelIsotopeRecipes(recipeOutput, "cm", "curium", 246, 243, 245, 247);
        addFissionFuelIsotopeRecipes(recipeOutput, "b", "berkelium", 247, 248);
        addFissionFuelIsotopeRecipes(recipeOutput, "cf", "californium", 252, 249, 251);

        for (String name : FISSION_FUEL_FLUIDS) {
            new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get(name + "_fluoride_flibe"), INGOT_VOLUME / 2).addFluidResult(FISSION_FUEL_MAP.get(name + "_fluoride"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("flibe"), INGOT_VOLUME / 2).save(recipeOutput);
            new ProcessorRecipeBuilder(CentrifugeRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("depleted_" + name + "_fluoride_flibe"), INGOT_VOLUME / 2).addFluidResult(FISSION_FUEL_MAP.get("depleted_" + name + "_fluoride"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("flibe"), INGOT_VOLUME / 2).save(recipeOutput);
        }

        // Fuel Reprocessing

        addReprocessingRecipe(recipeOutput, "tbu", "uranium_233", 1, "uranium_238", 5, "neptunium_236", 1, "neptunium_237", 1, "strontium_90", "caesium_137", 0.5D, 50);

        addReprocessingRecipe(recipeOutput, "leu_233", "uranium_238", 5, "plutonium_241", 1, "plutonium_242", 1, "americium_243", 1, "strontium_90", "caesium_137", 0.5D, 50);
        addReprocessingRecipe(recipeOutput, "heu_233", "uranium_235", 1, "uranium_238", 2, "plutonium_242", 3, "americium_243", 1, "strontium_90", "caesium_137", 1.5D, 50);
        addReprocessingRecipe(recipeOutput, "leu_235", "uranium_238", 4, "plutonium_239", 1, "plutonium_242", 2, "americium_243", 1, "molybdenum", "caesium_137", 0.5D, 50);
        addReprocessingRecipe(recipeOutput, "heu_235", "uranium_238", 3, "neptunium_236", 1, "plutonium_242", 2, "americium_243", 1, "molybdenum", "caesium_137", 1.5D, 50);

        addReprocessingRecipe(recipeOutput, "len_236", "uranium_238", 4, "neptunium_237", 1, "plutonium_241", 1, "plutonium_242", 2, "molybdenum", "caesium_137", 0.5D, 50);
        addReprocessingRecipe(recipeOutput, "hen_236", "uranium_238", 4, "plutonium_238", 1, "plutonium_241", 1, "plutonium_242", 1, "molybdenum", "caesium_137", 1.5D, 50);

        addReprocessingRecipe(recipeOutput, "lep_239", "plutonium_242", 5, "americium_242", 1, "americium_243", 1, "curium_246", 1, "strontium_90", "promethium_147", 0.5D, 50);
        addReprocessingRecipe(recipeOutput, "hep_239", "plutonium_241", 1, "americium_242", 1, "americium_243", 4, "curium_243", 1, "strontium_90", "promethium_147", 1.5D, 50);
        addReprocessingRecipe(recipeOutput, "lep_241", "plutonium_242", 5, "americium_243", 1, "curium_246", 1, "berkelium_247", 1, "strontium_90", "promethium_147", 0.5D, 50);
        addReprocessingRecipe(recipeOutput, "hep_241", "americium_241", 1, "americium_242", 1, "americium_243", 3, "curium_246", 2, "strontium_90", "promethium_147", 1.5D, 50);

        addReprocessingRecipe(recipeOutput, "mix_239", "uranium_238", 4, "plutonium_241", 1, "plutonium_242", 2, "americium_243", 1, "strontium_90", "promethium_147", 0.5D, 50);
        addReprocessingRecipe(recipeOutput, "mix_241", "uranium_238", 3, "plutonium_241", 1, "plutonium_242", 3, "americium_243", 1, "strontium_90", "promethium_147", 0.5D, 50);

        addReprocessingRecipe(recipeOutput, "lea_242", "americium_243", 3, "curium_245", 1, "curium_246", 3, "berkelium_248", 1, "molybdenum", "promethium_147", 0.5D, 50);
        addReprocessingRecipe(recipeOutput, "hea_242", "americium_243", 3, "curium_243", 1, "curium_246", 2, "berkelium_247", 1, "molybdenum", "promethium_147", 1.5D, 50);

        addReprocessingRecipe(recipeOutput, "lecm_243", "curium_246", 4, "curium_247", 1, "berkelium_247", 2, "berkelium_248", 1, "molybdenum", "promethium_147", 0.5D, 50);
        addReprocessingRecipe(recipeOutput, "hecm_243", "curium_245", 1, "curium_246", 3, "berkelium_247", 2, "berkelium_248", 1, "molybdenum", "promethium_147", 1.5D, 50);
        addReprocessingRecipe(recipeOutput, "lecm_245", "curium_246", 4, "curium_247", 1, "berkelium_247", 2, "californium_249", 1, "molybdenum", "europium_155", 0.5D, 60);
        addReprocessingRecipe(recipeOutput, "hecm_245", "curium_246", 3, "curium_247", 1, "berkelium_247", 2, "californium_249", 1, "molybdenum", "europium_155", 1.5D, 60);
        addReprocessingRecipe(recipeOutput, "lecm_247", "curium_246", 5, "berkelium_247", 1, "berkelium_248", 1, "californium_249", 1, "molybdenum", "europium_155", 0.5D, 60);
        addReprocessingRecipe(recipeOutput, "hecm_247", "berkelium_247", 4, "berkelium_248", 1, "californium_249", 1, "californium_251", 1, "molybdenum", "europium_155", 1.5D, 60);

        addReprocessingRecipe(recipeOutput, "leb_248", "berkelium_247", 5, "berkelium_248", 1, "californium_249", 1, "californium_251", 1, "ruthenium_106", "promethium_147", 0.5D, 60);
        addReprocessingRecipe(recipeOutput, "heb_248", "berkelium_248", 1, "californium_249", 1, "californium_251", 2, "californium_252", 3, "ruthenium_106", "promethium_147", 1.5D, 60);

        addReprocessingRecipe(recipeOutput, "lecf_249", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 2, "ruthenium_106", "promethium_147", 0.5D, 60);
        addReprocessingRecipe(recipeOutput, "hecf_249", "californium_250", 1, "californium_252", 2, "californium_252", 2, "californium_252", 2, "ruthenium_106", "promethium_147", 1.5D, 60);
        addReprocessingRecipe(recipeOutput, "lecf_251", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 2, "ruthenium_106", "europium_155", 0.5D, 60);
        addReprocessingRecipe(recipeOutput, "hecf_251", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 1, "ruthenium_106", "europium_155", 1.5D, 60);
    }

    public void addFissionFuelIsotopeRecipes(RecipeOutput recipeOutput, String suffix, String element, int fertile, int... fissiles) {
        for (int fissile : fissiles) {
            new ProcessorRecipeBuilder(CentrifugeRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("le" + suffix + "_" + fissile), INGOT_VOLUME).addFluidResult(FISSION_FUEL_MAP.get(element + "_" + fertile), NUGGET_VOLUME * 8).addFluidResult(FISSION_FUEL_MAP.get(element + "_" + fissile), NUGGET_VOLUME).save(recipeOutput);
            new ProcessorRecipeBuilder(CentrifugeRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("he" + suffix + "_" + fissile), INGOT_VOLUME).addFluidResult(FISSION_FUEL_MAP.get(element + "_" + fertile), NUGGET_VOLUME * 6).addFluidResult(FISSION_FUEL_MAP.get(element + "_" + fissile), NUGGET_VOLUME * 3).save(recipeOutput);
        }
    }

    public void addReprocessingRecipe(RecipeOutput recipeOutput, String fuel, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4, String waste1, String waste2, double w, int r) {
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("depleted_" + fuel), INGOT_VOLUME).addFluidResult(FISSION_FUEL_MAP.get(out1), NUGGET_VOLUME * n1).addFluidResult(FISSION_FUEL_MAP.get(out2), NUGGET_VOLUME * n2).addFluidResult(wasteStack(waste1, w * r)).addFluidResult(FISSION_FUEL_MAP.get(out3), NUGGET_VOLUME * n3).addFluidResult(FISSION_FUEL_MAP.get(out4), NUGGET_VOLUME * n4).addFluidResult(wasteStack(waste2, w * (100 - r))).save(recipeOutput);
    }

    public SizedChanceFluidIngredient wasteStack(String waste, double chancePercent) {
        return NCFluid.sizedIngredient(FISSION_FLUID_MAP.get(waste), 2 * NUGGET_VOLUME, NCMath.toInt(chancePercent), 0, NUGGET_VOLUME);
    }

}