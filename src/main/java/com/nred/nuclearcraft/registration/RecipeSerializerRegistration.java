package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeSerializer;
import com.nred.nuclearcraft.recipe.collector.CollectorSerializer;
import com.nred.nuclearcraft.recipe.processor.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.registration.Registers.RECIPE_SERIALIZERS;

public class RecipeSerializerRegistration {
    public static final DeferredHolder<RecipeSerializer<?>, CollectorSerializer> COLLECTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("collector_recipe", CollectorSerializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> ALLOY_FURNACE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("alloy_furnace_recipe", () -> new ProcessorRecipeSerializer(AlloyFurnaceRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> ASSEMBLER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("assembler_recipe", () -> new ProcessorRecipeSerializer(AssemblerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> CENTRIFUGE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("centrifuge_recipe", () -> new ProcessorRecipeSerializer(CentrifugeRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> CHEMICAL_REACTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("chemical_reactor_recipe", () -> new ProcessorRecipeSerializer(ChemicalReactorRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> CRYSTALLIZER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("crystallizer_recipe", () -> new ProcessorRecipeSerializer(CrystallizerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> DECAY_HASTENER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("decay_hastener_recipe", () -> new ProcessorRecipeSerializer(DecayHastenerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> ELECTRIC_FURNACE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("electric_furnace_recipe", () -> new ProcessorRecipeSerializer(ElectricFurnaceRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> ELECTROLYZER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("electrolyzer_recipe", () -> new ProcessorRecipeSerializer(ElectrolyzerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> FLUID_ENRICHER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("enricher_recipe", () -> new ProcessorRecipeSerializer(FluidEnricherRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> FLUID_EXTRACTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("extractor_recipe", () -> new ProcessorRecipeSerializer(FluidExtractorRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> FUEL_REPROCESSOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("fuel_reprocessor_recipe", () -> new ProcessorRecipeSerializer(FuelReprocessorRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> FLUID_INFUSER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("infuser_recipe", () -> new ProcessorRecipeSerializer(FluidInfuserRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> INGOT_FORMER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("ingot_former_recipe", () -> new ProcessorRecipeSerializer(IngotFormerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> MANUFACTORY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("manufactory_recipe", () -> new ProcessorRecipeSerializer(ManufactoryRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> MELTER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("melter_recipe", () -> new ProcessorRecipeSerializer(MelterRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> PRESSURIZER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("pressurizer_recipe", () -> new ProcessorRecipeSerializer(PressurizerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> ROCK_CRUSHER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("rock_crusher_recipe", () -> new ProcessorRecipeSerializer(RockCrusherRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> FLUID_MIXER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("salt_mixer_recipe", () -> new ProcessorRecipeSerializer(FluidMixerRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> SEPARATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("separator_recipe", () -> new ProcessorRecipeSerializer(SeparatorRecipe.class));
    public static final DeferredHolder<RecipeSerializer<?>, ProcessorRecipeSerializer> SUPERCOOLER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("supercooler_recipe", () -> new ProcessorRecipeSerializer(SupercoolerRecipe.class));

    public static final DeferredHolder<RecipeSerializer<?>, TurbineRecipeSerializer> TURBINE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("turbine_recipe", TurbineRecipeSerializer::new);


    public static void init() {
    }
}