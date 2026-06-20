package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidMixerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.List;

import static com.nred.nuclearcraft.helpers.FissionConstants.FISSION_FUEL_FLUIDS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.util.FluidStackHelper.*;

public class FluidMixerProvider {
    public FluidMixerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("lif"), INGOT_VOLUME).addFluidInput(MOLTEN_MAP.get("bef2"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("flibe"), INGOT_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("sodium"), INGOT_VOLUME / 2).addFluidInput(MOLTEN_MAP.get("potassium"), INGOT_VOLUME * 2).addFluidResult(COOLANT_MAP.get("nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 4 / 3.0, 1).addFluidInput(MOLTEN_MAP.get("boron_11"), INGOT_VOLUME).addFluidInput(MOLTEN_MAP.get("boron_10"), NUGGET_VOLUME * 3).addFluidResult(MOLTEN_MAP.get("boron"), NUGGET_VOLUME * 12).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 10 / 9.0, 1).addFluidInput(MOLTEN_MAP.get("lithium_7"), INGOT_VOLUME).addFluidInput(MOLTEN_MAP.get("lithium_6"), NUGGET_VOLUME).addFluidResult(MOLTEN_MAP.get("lithium"), NUGGET_VOLUME * 10).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("steel"), INGOT_VOLUME / 2).addFluidInput(MOLTEN_MAP.get("boron"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("ferroboron"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("ferroboron"), INGOT_VOLUME / 2).addFluidInput(MOLTEN_MAP.get("lithium"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("tough"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("nickel"), INGOT_VOLUME / 2).addFluidInput(MOLTEN_MAP.get("chromium"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("nichrome"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("nichrome"), INGOT_VOLUME * 3 / 4).addFluidInput(FISSION_FLUID_MAP.get("molybdenum"), INGOT_VOLUME / 4).addFluidResult(MOLTEN_MAP.get("hastelloy"), INGOT_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lead_platinum"), INGOT_VOLUME).addFluidInput(CUSTOM_FLUID_MAP.get("ender"), EUM_DUST_VOLUME).addFluidResult(MOLTEN_MAP.get("enderium"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 0.5).addFluidInput(CUSTOM_FLUID_MAP.get("radaway"), BUCKET_VOLUME / 4).addFluidInput(MOLTEN_MAP.get("redstone"), REDSTONE_DUST_VOLUME * 2).addFluidResult(CUSTOM_FLUID_MAP.get("radaway_slow"), BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 0.5).addFluidInput(FLAMMABLE_MAP.get("ethanol"), BUCKET_VOLUME / 4).addFluidInput(MOLTEN_MAP.get("redstone"), REDSTONE_DUST_VOLUME * 2).addFluidResult(FLAMMABLE_MAP.get("redstone_ethanol"), BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID_MAP.get("ice"), BUCKET_VOLUME).addFluidInput(FLAMMABLE_MAP.get("ethanol"), BUCKET_VOLUME / 4).addFluidResult(CUSTOM_FLUID_MAP.get("slurry_ice"), BUCKET_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID_MAP.get("slurry_ice"), BUCKET_VOLUME).addFluidInput(CUSTOM_FLUID_MAP.get("cryotheum"), EUM_DUST_VOLUME).addFluidResult(CUSTOM_FLUID_MAP.get("emergency_coolant"), BUCKET_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("chocolate_liquor"), INGOT_VOLUME / 2).addFluidInput(CHOCOLATE_MAP.get("cocoa_butter"), INGOT_VOLUME / 2).addFluidResult(CHOCOLATE_MAP.get("unsweetened_chocolate"), INGOT_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("unsweetened_chocolate"), INGOT_VOLUME).addFluidInput(SUGAR_MAP.get("sugar"), INGOT_VOLUME / 2).addFluidResult(CHOCOLATE_MAP.get("dark_chocolate"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("dark_chocolate"), INGOT_VOLUME).addFluidInput(NeoForgeMod.MILK.get(), BUCKET_VOLUME / 4).addFluidResult(CHOCOLATE_MAP.get("milk_chocolate"), INGOT_VOLUME * 2).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 0.5).addFluidInput(SUGAR_MAP.get("gelatin"), INGOT_VOLUME / 2).addFluidInput(Fluids.WATER, BUCKET_VOLUME / 4).addFluidResult(SUGAR_MAP.get("hydrated_gelatin"), INGOT_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 0.5).addFluidInput(SUGAR_MAP.get("hydrated_gelatin"), INGOT_VOLUME).addFluidInput(SUGAR_MAP.get("sugar"), INGOT_VOLUME / 2).addFluidResult(SUGAR_MAP.get("marshmallow"), INGOT_VOLUME).save(recipeOutput);

        for (String name : List.of("iron", "gold", "prismarine", "copper", "tin", "lead", "boron", "lithium", "magnesium", "manganese", "aluminum", "silver", "enderium")) {
            new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get(name), INGOT_VOLUME).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get(name + "_nak"), INGOT_VOLUME).save(recipeOutput);
        }
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("redstone"), REDSTONE_DUST_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("redstone_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("quartz"), GEM_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("quartz_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("obsidian"), SEARED_MATERIAL_VOLUME * 5).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("obsidian_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("nether_brick"), SEARED_MATERIAL_VOLUME * 5).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("nether_brick_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("glowstone"), GLOWSTONE_DUST_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("glowstone_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("lapis"), GEM_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("lapis_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("slime"), INGOT_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("slime_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("end_stone"), SEARED_MATERIAL_VOLUME * 5).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("end_stone_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("purpur"), SEARED_MATERIAL_VOLUME * 5).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("purpur_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("diamond"), GEM_VOLUME).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("diamond_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("emerald"), GEM_VOLUME).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("emerald_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("fluorite"), GEM_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("fluorite_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("villiaumite"), GEM_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("villiaumite_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("carobbiite"), GEM_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("carobbiite_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(HOT_GAS_MAP.get("arsenic"), GEM_VOLUME * 2).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("arsenic_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_nitrogen"), BUCKET_VOLUME / 4).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("liquid_nitrogen_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_helium"), BUCKET_VOLUME / 4).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("liquid_helium_nak"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(CUSTOM_FLUID_MAP.get("cryotheum"), EUM_DUST_VOLUME).addFluidInput(COOLANT_MAP.get("nak"), INGOT_VOLUME).addFluidResult(COOLANT_MAP.get("cryotheum_nak"), INGOT_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("uranium_238"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("uranium_233"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("leu_233"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("leu_233"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("uranium_233"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("heu_233"), NUGGET_VOLUME * 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("uranium_238"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("uranium_235"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("leu_235"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("leu_235"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("uranium_235"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("heu_235"), NUGGET_VOLUME * 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("neptunium_237"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("neptunium_236"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("len_236"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("len_236"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("neptunium_236"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hen_236"), NUGGET_VOLUME * 4).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("plutonium_242"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("plutonium_239"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("lep_239"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lep_239"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("plutonium_239"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hep_239"), NUGGET_VOLUME * 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("plutonium_242"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("plutonium_241"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("lep_241"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lep_241"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("plutonium_241"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hep_241"), NUGGET_VOLUME * 4).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("americium_243"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("americium_242"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("lea_242"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lea_242"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("americium_242"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hea_242"), NUGGET_VOLUME * 4).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("curium_246"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("curium_243"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("lecm_243"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecm_243"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("curium_243"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hecm_243"), NUGGET_VOLUME * 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("curium_246"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("curium_245"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("lecm_245"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecm_245"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("curium_245"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hecm_245"), NUGGET_VOLUME * 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("curium_246"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("curium_247"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("lecm_247"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecm_247"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("curium_247"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hecm_247"), NUGGET_VOLUME * 4).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("berkelium_247"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("berkelium_248"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("leb_248"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("leb_248"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("berkelium_248"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("heb_248"), NUGGET_VOLUME * 4).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("californium_252"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("californium_249"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("lecf_249"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecf_249"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("californium_249"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hecf_249"), NUGGET_VOLUME * 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("californium_252"), NUGGET_VOLUME * 8).addFluidInput(FISSION_FUEL_MAP.get("californium_251"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("lecf_251"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("lecf_251"), NUGGET_VOLUME * 3).addFluidInput(FISSION_FUEL_MAP.get("californium_251"), NUGGET_VOLUME).addFluidResult(FISSION_FUEL_MAP.get("hecf_251"), NUGGET_VOLUME * 4).save(recipeOutput);

        for (String name : FISSION_FUEL_FLUIDS) {
            new ProcessorRecipeBuilder(FluidMixerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get(name + "_fluoride"), INGOT_VOLUME / 2).addFluidInput(MOLTEN_MAP.get("flibe"), INGOT_VOLUME / 2).addFluidResult(FISSION_FUEL_MAP.get(name + "_fluoride_flibe"), INGOT_VOLUME / 2).save(recipeOutput);
        }
    }
}