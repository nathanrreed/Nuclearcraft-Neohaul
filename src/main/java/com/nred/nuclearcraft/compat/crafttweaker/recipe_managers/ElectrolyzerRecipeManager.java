package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.processor.ElectrolyzerRecipe;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.ElectrolyzerRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/ElectrolyzerRecipeManager")
public class ElectrolyzerRecipeManager extends BasicNuclearRecipeManager<ElectrolyzerRecipe> {
    static final ElectrolyzerRecipeManager INSTANCE = new ElectrolyzerRecipeManager();

    public ElectrolyzerRecipeManager() {
        super("electrolyzer", ElectrolyzerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTFluidIngredient output1,
                          @ZenCodeType.Nullable CTFluidIngredient output2,
                          @ZenCodeType.Nullable CTFluidIngredient output3,
                          @ZenCodeType.Nullable CTFluidIngredient output4,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output1, output2, output3, output4), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTChanceFluidIngredient output1,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output2,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output3,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output4,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output1, output2, output3, output4), timeModifier, powerModifier, radiation);
    }
}
