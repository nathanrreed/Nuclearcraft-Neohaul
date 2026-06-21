package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.machine.DecayPoolHeatSourceRecipe;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.ItemLike;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.radiation.RadSources.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.FISSION_FLUID_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.MOLTEN_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_VOLUME;

public class DecayPoolHeatSourceProvider {
    public DecayPoolHeatSourceProvider(RecipeOutput recipeOutput) {
        addItemDecayRecipe(recipeOutput, FISSION_DUST_MAP.get("strontium_90"), DUST_MAP.get("zirconium"), STRONTIUM_90);
        addItemDecayRecipe(recipeOutput, FISSION_DUST_MAP.get("ruthenium_106"), DUST_MAP.get("palladium"), RUTHENIUM_106);
//        addItemDecayRecipe(recipeOutput, FISSION_DUST_MAP.get("caesium_137"), OreDictHelper.oreExists("dustBarium") ? "dustBarium" : "ingotBarium", CAESIUM_137); TODO
        addItemDecayRecipe(recipeOutput, FISSION_DUST_MAP.get("promethium_147"), ALLOY_MAP.get("samarium"), PROMETHIUM_147);
        addItemDecayRecipe(recipeOutput, FISSION_DUST_MAP.get("europium_155"), DUST_MAP.get("gadolinium"), EUROPIUM_155);

        addFluidDecayRecipe(recipeOutput, FISSION_FLUID_MAP.get("strontium_90"), MOLTEN_MAP.get("zirconium"), STRONTIUM_90, "decay_pool_heat_source_strontium_90");
        addFluidDecayRecipe(recipeOutput, FISSION_FLUID_MAP.get("ruthenium_106"), MOLTEN_MAP.get("palladium"), RUTHENIUM_106, "decay_pool_heat_source_ruthenium_106");
        addFluidDecayRecipe(recipeOutput, FISSION_FLUID_MAP.get("caesium_137"), MOLTEN_MAP.get("barium"), CAESIUM_137, "decay_pool_heat_source_caesium_137");
//        addFluidDecayRecipe(recipeOutput, FISSION_FLUID_MAP.get("promethium_147"), MOLTEN_MAP.get("samarium"), PROMETHIUM_147, "decay_pool_heat_source_promethium_147"); TODO
        addFluidDecayRecipe(recipeOutput, FISSION_FLUID_MAP.get("europium_155"), MOLTEN_MAP.get("gadolinium"), EUROPIUM_155, "decay_pool_heat_source_europium_155");
    }

    public void addItemDecayRecipe(RecipeOutput recipeOutput, ItemLike input, ItemLike output, double radiation) {
        addDecayRecipe(recipeOutput, SizedChanceItemIngredient.of(input, 1), SizedChanceFluidIngredient.EMPTY, SizedChanceItemIngredient.of(output, 1), SizedChanceFluidIngredient.EMPTY, radiation, "");
    }

    public void addFluidDecayRecipe(RecipeOutput recipeOutput, Fluids input, Fluids output, double radiation, String name) {
        addDecayRecipe(recipeOutput, SizedChanceItemIngredient.EMPTY, Fluids.sizedIngredient(input, INGOT_VOLUME), SizedChanceItemIngredient.EMPTY, Fluids.sizedIngredient(output, INGOT_VOLUME), radiation, name);
    }

    public void addDecayRecipe(RecipeOutput recipeOutput, SizedChanceItemIngredient itemInput, SizedChanceFluidIngredient fluidInput, SizedChanceItemIngredient itemOutput, SizedChanceFluidIngredient fluidOutput, double radiation, String name) {
        double mult = RecipeHelper.getDecayTimeMultiplier(radiation / 9D, 1E-6D);
        BasicRecipeBuilder<DecayPoolHeatSourceRecipe> builder = new BasicRecipeBuilder<>(new DecayPoolHeatSourceRecipe(itemInput, fluidInput, itemOutput, fluidOutput, NCMath.roundTo(20D * 12000D * mult, 20D), NCMath.roundTo(20D / mult, 5D)));
        if (name.isEmpty()) {
            builder.save(recipeOutput);
        } else {
            builder.save(recipeOutput, ncLoc(name));
        }
    }
}