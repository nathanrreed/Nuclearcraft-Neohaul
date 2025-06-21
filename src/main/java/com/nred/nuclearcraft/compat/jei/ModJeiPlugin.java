package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.collector.CollectorRecipe;
import com.nred.nuclearcraft.recipe.processor.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.COLLECTOR_MAP;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    public static Map<String, IRecipeCategory<? extends ProcessorRecipe>> PROCESSOR_CATEGORIES = Map.of();
    public static IRecipeCategory<CollectorRecipe> COLLECTOR_CATEGORY;

    @Override
    public ResourceLocation getPluginUid() {
        return ncLoc("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        PROCESSOR_CATEGORIES = makeCategories(registration);
        COLLECTOR_CATEGORY = new JeiCollectorCategory(helper);
        registration.addRecipeCategories(COLLECTOR_CATEGORY);

        for (IRecipeCategory<?> category : PROCESSOR_CATEGORIES.values()) {
            registration.addRecipeCategories(category);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (String type : PROCESSOR_CATEGORIES.keySet()) {
            registration.addRecipeCatalysts(PROCESSOR_CATEGORIES.get(type).getRecipeType(), PROCESSOR_MAP.get(type));
        }
        registration.addRecipeCatalysts(COLLECTOR_CATEGORY.getRecipeType(), VanillaTypes.ITEM_STACK, COLLECTOR_MAP.values().stream().map(DeferredBlock::toStack).toList());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        for (String type : PROCESSOR_CATEGORIES.keySet()) {
            registration.addRecipes((RecipeType<ProcessorRecipe>) PROCESSOR_CATEGORIES.get(type).getRecipeType(), recipeManager.getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(type).get()).stream().map(RecipeHolder::value).toList());
        }

        List.of(
                NITROGEN_COLLECTOR_RECIPE_TYPE, NITROGEN_COLLECTOR_COMPACT_RECIPE_TYPE, NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE,
                WATER_SOURCE_RECIPE_TYPE, WATER_SOURCE_COMPACT_RECIPE_TYPE, WATER_SOURCE_DENSE_RECIPE_TYPE,
                COBBLE_GENERATOR_RECIPE_TYPE, COBBLE_GENERATOR_COMPACT_RECIPE_TYPE, COBBLE_GENERATOR_DENSE_RECIPE_TYPE
        ).forEach(i -> {
            registration.addRecipes(COLLECTOR_CATEGORY.getRecipeType(), recipeManager.getAllRecipesFor(i.get()).stream().map(RecipeHolder::value).toList());
        });
    }

    private static Map<String, IRecipeCategory<? extends ProcessorRecipe>> makeCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        Map<String, IRecipeCategory<? extends ProcessorRecipe>> map = new HashMap<>();
        map.put("alloy_furnace", new JeiProcessorCategory<>(helper, "alloy_furnace", AlloyFurnaceRecipe.class));
        map.put("assembler", new JeiProcessorCategory<>(helper, "assembler", AssemblerRecipe.class));
        map.put("centrifuge", new JeiProcessorCategory<>(helper, "centrifuge", CentrifugeRecipe.class));
        map.put("chemical_reactor", new JeiProcessorCategory<>(helper, "chemical_reactor", ChemicalReactorRecipe.class));
        map.put("crystallizer", new JeiProcessorCategory<>(helper, "crystallizer", CrystallizerRecipe.class));
        map.put("decay_hastener", new JeiProcessorCategory<>(helper, "decay_hastener", DecayHastenerRecipe.class));
        map.put("electric_furnace", new JeiProcessorCategory<>(helper, "electric_furnace", ElectricFurnaceRecipe.class));
        map.put("electrolyzer", new JeiProcessorCategory<>(helper, "electrolyzer", ElectrolyzerRecipe.class));
        map.put("fluid_enricher", new JeiProcessorCategory<>(helper, "fluid_enricher", FluidEnricherRecipe.class));
        map.put("fluid_extractor", new JeiProcessorCategory<>(helper, "fluid_extractor", FluidExtractorRecipe.class));
        map.put("fluid_infuser", new JeiProcessorCategory<>(helper, "fluid_infuser", FluidInfuserRecipe.class));
        map.put("fluid_mixer", new JeiProcessorCategory<>(helper, "fluid_mixer", FluidMixerRecipe.class));
        map.put("fuel_reprocessor", new JeiProcessorCategory<>(helper, "fuel_reprocessor", FuelReprocessorRecipe.class));
        map.put("ingot_former", new JeiProcessorCategory<>(helper, "ingot_former", IngotFormerRecipe.class));
        map.put("manufactory", new JeiProcessorCategory<>(helper, "manufactory", ManufactoryRecipe.class));
        map.put("melter", new JeiProcessorCategory<>(helper, "melter", MelterRecipe.class));
        map.put("pressurizer", new JeiProcessorCategory<>(helper, "pressurizer", PressurizerRecipe.class));
        map.put("rock_crusher", new JeiProcessorCategory<>(helper, "rock_crusher", RockCrusherRecipe.class));
        map.put("separator", new JeiProcessorCategory<>(helper, "separator", SeparatorRecipe.class));
        map.put("supercooler", new JeiProcessorCategory<>(helper, "supercooler", SupercoolerRecipe.class));
        return map;
    }
}