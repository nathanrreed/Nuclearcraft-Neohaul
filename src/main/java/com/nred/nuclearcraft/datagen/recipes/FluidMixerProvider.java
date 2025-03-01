package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidMixerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

import static com.nred.nuclearcraft.helpers.FissionConstants.FISSION_FUEL_FLUIDS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class FluidMixerProvider {
    public FluidMixerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("lif"), 144).addFluidInput(MOLTEN_MAP.get("bef2"), 72).addFluidResult(MOLTEN_MAP.get("flibe"), 72).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("sodium"), 72).addFluidInput(MOLTEN_MAP.get("potassium"), 288).addFluidResult(COOLANT_MAP.get("nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 4 / 3.0, 1).addFluidInput(MOLTEN_MAP.get("boron_11"), 144).addFluidInput(MOLTEN_MAP.get("boron_10"), 48).addFluidResult(MOLTEN_MAP.get("boron"), 192).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 10 / 9.0, 1).addFluidInput(MOLTEN_MAP.get("lithium_7"), 72).addFluidInput(MOLTEN_MAP.get("lithium_6"), 16).addFluidResult(MOLTEN_MAP.get("lithium"), 160).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("steel"), 72).addFluidInput(MOLTEN_MAP.get("boron"), 72).addFluidResult(MOLTEN_MAP.get("ferroboron"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("ferroboron"), 72).addFluidInput(MOLTEN_MAP.get("lithium"), 72).addFluidResult(MOLTEN_MAP.get("tough"), 144).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lead_platinum"), 144).addFluidInput(CUSTOM_FLUID.get("ender"), 250).addFluidResult(MOLTEN_MAP.get("enderium"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 0.5).addFluidInput(CUSTOM_FLUID.get("radaway"), 250).addFluidInput(MOLTEN_MAP.get("redstone"), 200).addFluidResult(CUSTOM_FLUID.get("radaway_slow"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 0.5).addFluidInput(FLAMMABLE_MAP.get("ethanol"), 250).addFluidInput(MOLTEN_MAP.get("redstone"), 200).addFluidResult(FLAMMABLE_MAP.get("redstone_ethanol"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID.get("ice"), 1000).addFluidInput(FLAMMABLE_MAP.get("ethanol"), 250).addFluidResult(CUSTOM_FLUID.get("slurry_ice"), 1000).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID.get("slurry_ice"), 1000).addFluidInput(CUSTOM_FLUID.get("cryotheum"), 250).addFluidResult(CUSTOM_FLUID.get("emergency_coolant"), 1000).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(CHOCOLATE_MAP.get("chocolate_liquor"), 72).addFluidInput(CHOCOLATE_MAP.get("cocoa_butter"), 72).addFluidResult(CHOCOLATE_MAP.get("unsweetened_chocolate"), 144).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("unsweetened_chocolate"), 144).addFluidInput(SUGAR_MAP.get("sugar"), 72).addFluidResult(CHOCOLATE_MAP.get("dark_chocolate"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("dark_chocolate"), 144).addFluidInput(NeoForgeMod.MILK.get(), 250).addFluidResult(CHOCOLATE_MAP.get("milk_chocolate"), 288).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 0.5).addFluidInput(SUGAR_MAP.get("gelatin"), 72).addFluidInput(Fluids.WATER, 250).addFluidResult(SUGAR_MAP.get("hydrated_gelatin"), 72).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 0.5).addFluidInput(SUGAR_MAP.get("hydrated_gelatin"), 144).addFluidInput(SUGAR_MAP.get("sugar"), 72).addFluidResult(SUGAR_MAP.get("marshmallow"), 144).save(recipeOutput);

        for (String name : List.of("iron", "gold", "prismarine", "copper", "tin", "lead", "boron", "lithium", "magnesium", "manganese", "aluminum", "silver", "enderium")) {
            new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get(name), 144).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get(name + "_nak"), 144).save(recipeOutput);
        }
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("redstone"), 200).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("redstone_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("quartz"), 1332).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("quartz_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("obsidian"), 360).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("obsidian_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("nether_brick"), 360).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("nether_brick_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("glowstone"), 500).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("glowstone_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("lapis"), 1332).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("lapis_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("slime"), 288).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("slime_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("end_stone"), 360).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("end_stone_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("purpur"), 360).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("purpur_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("diamond"), 666).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("diamond_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("emerald"), 666).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("emerald_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("fluorite"), 1332).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("fluorite_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("villiaumite"), 1332).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("villiaumite_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("carobbiite"), 1332).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("carobbiite_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(HOT_GAS_MAP.get("arsenic"), 1332).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("arsenic_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(CUSTOM_FLUID.get("liquid_nitrogen"), 250).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("liquid_nitrogen_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(CUSTOM_FLUID.get("liquid_helium"), 250).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("liquid_helium_nak"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(CUSTOM_FLUID.get("cryotheum"), 250).addFluidInput(COOLANT_MAP.get("nak"), 144).addFluidResult(COOLANT_MAP.get("cryotheum_nak"), 144).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("uranium_238"), 128).addFluidInput(FISSION_FUEL_MAP.get("uranium_233"), 16).addFluidResult(FISSION_FUEL_MAP.get("leu_233"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("leu_233"), 48).addFluidInput(FISSION_FUEL_MAP.get("uranium_233"), 16).addFluidResult(FISSION_FUEL_MAP.get("heu_233"), 64).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("uranium_238"), 128).addFluidInput(FISSION_FUEL_MAP.get("uranium_235"), 16).addFluidResult(FISSION_FUEL_MAP.get("leu_235"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("leu_235"), 48).addFluidInput(FISSION_FUEL_MAP.get("uranium_235"), 16).addFluidResult(FISSION_FUEL_MAP.get("heu_235"), 64).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("neptunium_237"), 128).addFluidInput(FISSION_FUEL_MAP.get("neptunium_236"), 16).addFluidResult(FISSION_FUEL_MAP.get("len_236"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("len_236"), 48).addFluidInput(FISSION_FUEL_MAP.get("neptunium_236"), 16).addFluidResult(FISSION_FUEL_MAP.get("hen_236"), 64).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("plutonium_242"), 128).addFluidInput(FISSION_FUEL_MAP.get("plutonium_239"), 16).addFluidResult(FISSION_FUEL_MAP.get("lep_239"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lep_239"), 48).addFluidInput(FISSION_FUEL_MAP.get("plutonium_239"), 16).addFluidResult(FISSION_FUEL_MAP.get("hep_239"), 64).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("plutonium_242"), 128).addFluidInput(FISSION_FUEL_MAP.get("plutonium_241"), 16).addFluidResult(FISSION_FUEL_MAP.get("lep_241"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lep_241"), 48).addFluidInput(FISSION_FUEL_MAP.get("plutonium_241"), 16).addFluidResult(FISSION_FUEL_MAP.get("hep_241"), 64).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("americium_243"), 128).addFluidInput(FISSION_FUEL_MAP.get("americium_242"), 16).addFluidResult(FISSION_FUEL_MAP.get("lea_242"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lea_242"), 48).addFluidInput(FISSION_FUEL_MAP.get("americium_242"), 16).addFluidResult(FISSION_FUEL_MAP.get("hea_242"), 64).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("curium_246"), 128).addFluidInput(FISSION_FUEL_MAP.get("curium_243"), 16).addFluidResult(FISSION_FUEL_MAP.get("lecm_243"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecm_243"), 48).addFluidInput(FISSION_FUEL_MAP.get("curium_243"), 16).addFluidResult(FISSION_FUEL_MAP.get("hecm_243"), 64).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("curium_246"), 128).addFluidInput(FISSION_FUEL_MAP.get("curium_245"), 16).addFluidResult(FISSION_FUEL_MAP.get("lecm_245"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecm_245"), 48).addFluidInput(FISSION_FUEL_MAP.get("curium_245"), 16).addFluidResult(FISSION_FUEL_MAP.get("hecm_245"), 64).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("curium_246"), 128).addFluidInput(FISSION_FUEL_MAP.get("curium_247"), 16).addFluidResult(FISSION_FUEL_MAP.get("lecm_247"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecm_247"), 48).addFluidInput(FISSION_FUEL_MAP.get("curium_247"), 16).addFluidResult(FISSION_FUEL_MAP.get("hecm_247"), 64).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("berkelium_247"), 128).addFluidInput(FISSION_FUEL_MAP.get("berkelium_248"), 16).addFluidResult(FISSION_FUEL_MAP.get("leb_248"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("leb_248"), 48).addFluidInput(FISSION_FUEL_MAP.get("berkelium_248"), 16).addFluidResult(FISSION_FUEL_MAP.get("heb_248"), 64).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("californium_252"), 128).addFluidInput(FISSION_FUEL_MAP.get("californium_249"), 16).addFluidResult(FISSION_FUEL_MAP.get("lecf_249"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecf_249"), 48).addFluidInput(FISSION_FUEL_MAP.get("californium_249"), 16).addFluidResult(FISSION_FUEL_MAP.get("hecf_249"), 64).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("californium_252"), 128).addFluidInput(FISSION_FUEL_MAP.get("californium_251"), 16).addFluidResult(FISSION_FUEL_MAP.get("lecf_251"), 144).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecf_251"), 48).addFluidInput(FISSION_FUEL_MAP.get("californium_251"), 16).addFluidResult(FISSION_FUEL_MAP.get("hecf_251"), 64).save(recipeOutput);

        for (String name : FISSION_FUEL_FLUIDS) {
            new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get(name + "_fluoride"), 72).addFluidInput(MOLTEN_MAP.get("flibe"), 72).addFluidResult(FISSION_FUEL_MAP.get(name + "_fluoride_flibe"), 72).save(recipeOutput);
        }
    }
}