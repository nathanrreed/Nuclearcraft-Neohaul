package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.SizedItemIngredient;
import com.nred.nuclearcraft.recipe.base_types.ItemToItemRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.AlloyFurnaceRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.datagen.ModRecipeProvider.tag;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class AlloyFurnaceRecipeProvider {
    public AlloyFurnaceRecipeProvider(RecipeOutput recipeOutput) {
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(ingotDusts("copper", 3, "tin", 1)).addResult(ALLOY_MAP.get("bronze"), 4).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(List.of(ingotDust("iron", 1), new SizedItemIngredient(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(tag(Tags.Items.DUSTS, "coal")), new Ingredient.TagValue(tag(Tags.Items.DUSTS, "graphite")), new Ingredient.TagValue(tag(Tags.Items.INGOTS, "graphite")), new Ingredient.ItemValue(new ItemStack(Items.COAL)))), 2))).addResult(ALLOY_MAP.get("steel"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1.5).addInput(ingotDusts("steel", 1, "boron", 1)).addResult(ALLOY_MAP.get("ferroboron"), 2).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1.5).addInput(List.of(SizedItemIngredient.of(ALLOY_MAP.get("ferroboron"), 1), ingotDust("lithium", 1))).addResult(ALLOY_MAP.get("tough"), 2).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 2).addInput(List.of(ingotDust("graphite", 1), gemDust("diamond", 1))).addResult(ALLOY_MAP.get("hard_carbon"), 2).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(ingotDusts("magnesium", 1, "boron", 2)).addResult(ALLOY_MAP.get("magnesium_diboride"), 3).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1).addInput(ingotDusts("lithium", 1, "manganese_dioxide", 1)).addResult(ALLOY_MAP.get("lithium_manganese_dioxide"), 2).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 0.5).addInput(ingotDusts("copper", 3, "silver", 1)).addResult(ALLOY_MAP.get("shibuichi"), 4).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 0.5).addInput(ingotDusts("tin", 3, "silver", 1)).addResult(ALLOY_MAP.get("tin_silver"), 4).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 2, 2).addInput(List.of(SizedItemIngredient.of(ALLOY_MAP.get("tough"), 1), SizedItemIngredient.of(ALLOY_MAP.get("hard_carbon"), 1))).addResult(ALLOY_MAP.get("extreme"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1.5).addInput(List.of(SizedItemIngredient.of(ALLOY_MAP.get("extreme"), 1), SizedItemIngredient.of(GEM_MAP.get("boron_arsenide"), 1))).addResult(ALLOY_MAP.get("thermoconducting"), 2).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 4, 1).addInput(ingotDusts("zirconium", 7, "tin", 1)).addResult(ALLOY_MAP.get("zircaloy"), 8).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 2, 2).addInput(List.of(SizedItemIngredient.of(tag(Tags.Items.GEMS, "silicon"), 1), ingotDust("graphite", 1))).addResult(ALLOY_MAP.get("silicon_carbide"), 2).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 8, 2).addInput(List.of(ingotDust("iron", 15), new SizedItemIngredient(Ingredient.of(COMPOUND_MAP.get("c_mn_blend")), 1))).addResult(ALLOY_MAP.get("hsla_steel"), 16).save(recipeOutput);

        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(List.of(SizedItemIngredient.of(URANIUM_MAP.get("233"), 1), ingotDust("zirconium", 1))).addResult(URANIUM_MAP.get("233_c"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(List.of(SizedItemIngredient.of(URANIUM_MAP.get("233"), 1), ingotDust("graphite", 1))).addResult(URANIUM_MAP.get("233_za"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(List.of(SizedItemIngredient.of(URANIUM_MAP.get("235"), 1), ingotDust("zirconium", 1))).addResult(URANIUM_MAP.get("235_za"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(List.of(SizedItemIngredient.of(URANIUM_MAP.get("235"), 1), ingotDust("graphite", 1))).addResult(URANIUM_MAP.get("235_c"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(List.of(SizedItemIngredient.of(URANIUM_MAP.get("238"), 1), ingotDust("zirconium", 1))).addResult(URANIUM_MAP.get("238_za"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addInput(List.of(SizedItemIngredient.of(URANIUM_MAP.get("238"), 1), ingotDust("graphite", 1))).addResult(URANIUM_MAP.get("238_c"), 1).save(recipeOutput);

        // TODO Neptunium, Plutonium, Americium, Curium, Berkelium, Californium, Pellets
    }

    private List<@NotNull SizedItemIngredient> ingotDusts(String input1, int count1, String input2, int count2) {
        return List.of(SizedItemIngredient.tags(List.of(tag(Tags.Items.DUSTS, input1), tag(Tags.Items.INGOTS, input1)), count1), SizedItemIngredient.tags(List.of(tag(Tags.Items.DUSTS, input2), tag(Tags.Items.INGOTS, input2)), count2));
    }

    private @NotNull SizedItemIngredient ingotDust(String input, int count) {
        return SizedItemIngredient.tags(List.of(tag(Tags.Items.DUSTS, input), tag(Tags.Items.INGOTS, input)), count);
    }

    private @NotNull SizedItemIngredient gemDust(String input, int count) {
        return SizedItemIngredient.tags(List.of(tag(Tags.Items.DUSTS, input), tag(Tags.Items.GEMS, input)), count);
    }
}