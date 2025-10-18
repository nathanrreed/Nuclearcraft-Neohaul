package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoolantHeaterRecipes extends BasicRecipeHandler<FissionCoolantHeaterRecipe> {
    public CoolantHeaterRecipes() {
        super("coolant_heater", 1, 1, 0, 1);
    }

    public @Nullable RecipeInfo<FissionCoolantHeaterRecipe> getRecipeInfoFromHeaterInputs(Level level, FissionCoolantHeaterType heaterType, List<Tank> fluidInputs) {
//        long hash = 31L * (heaterType + "_heater").hashCode() + RecipeHelper.hashMaterialsRaw(Collections.emptyList(), fluidInputs); TODO
        RecipeInfo<FissionCoolantHeaterRecipe> temp = NCRecipes.coolant_heater.getRecipeInfoFromInputs(level, List.of(), fluidInputs);
        if (temp != null && temp.recipe.getCoolantHeaterPlacementRule().equals(heaterType.getName() + "_heater")) {
            return temp;
        }
//        if (recipeCache.containsKey(hash)) {
//            ObjectSet<BasicRecipe> set = recipeCache.get(hash);
//            for (BasicRecipe recipe : set) {
//                if (recipe instanceof CoolantHeaterRecipe heaterRecipe) {
//                    RecipeMatchResult matchResult = heaterRecipe.matchHeaterInputs(heaterType, fluidInputs);
//                    if (matchResult.isMatch) {
//                        return new RecipeInfo<>(heaterRecipe, matchResult);
//                    }
//                }
//            }
//        }
        return null;
    }
}