package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.RockCrusherRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.qmdLoaded;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.qmdNotLoaded;
import static com.nred.nuclearcraft.registration.BlockRegistration.MATERIAL_BLOCK_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class RockCrusherProvider {
    public RockCrusherProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(RockCrusherRecipe.class, 1, 1).addItemInput(Ingredient.of(Items.GRANITE, Items.POLISHED_GRANITE), 1).addItemResult(GEM_DUST_MAP.get("rhodochrosite"), 2, 40).addItemResult(GEM_DUST_MAP.get("sulfur"), 2, 30).addItemResult(GEM_DUST_MAP.get("villiaumite"), 1, 35).save(recipeOutput);
        new ProcessorRecipeBuilder(RockCrusherRecipe.class, 1, 1).addItemInput(Ingredient.of(Items.DIORITE, Items.POLISHED_DIORITE), 1).addItemResult(DUST_MAP.get("zirconium"), 2, 50).addItemResult(GEM_DUST_MAP.get("fluorite"), 2, 45).addItemResult(GEM_DUST_MAP.get("carobbiite"), 1, 35).save(recipeOutput);
        new ProcessorRecipeBuilder(RockCrusherRecipe.class, 1, 1).addItemInput(Ingredient.of(Items.ANDESITE, Items.POLISHED_ANDESITE), 1).addItemResult(DUST_MAP.get("beryllium"), 2, 40).addItemResult(COMPOUND_MAP.get("alugentum"), 2, 30).addItemResult(GEM_DUST_MAP.get("arsenic"), 1, 30).save(recipeOutput);

        new ProcessorRecipeBuilder(RockCrusherRecipe.class, 1, 1).addItemInput(MATERIAL_BLOCK_MAP.get("soulless_sandstone"), 1).addItemResult(GEM_DUST_MAP.get("barite"), 1, 80).addItemResult(GEM_DUST_MAP.get("coal"), 1, 60).addItemResult(GEM_DUST_MAP.get("dysprholminite"), 1, 40).save(qmdLoaded(recipeOutput), ncLoc("soulless_sandstone_qmd"));
        new ProcessorRecipeBuilder(RockCrusherRecipe.class, 1, 1).addItemInput(MATERIAL_BLOCK_MAP.get("soulless_sandstone"), 1).addItemResult(GEM_DUST_MAP.get("barite"), 1, 80).addItemResult(GEM_DUST_MAP.get("nichromite"), 1, 60).addItemResult(GEM_DUST_MAP.get("dysprholminite"), 1, 40).save(qmdNotLoaded(recipeOutput));
    }
}