package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.processor.CentrifugeRecipe;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.CentrifugeRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/CentrifugeRecipeManager")
public class CentrifugeRecipeManager extends BasicNuclearRecipeManager<CentrifugeRecipe> {
    static final CentrifugeRecipeManager INSTANCE = new CentrifugeRecipeManager();

    public CentrifugeRecipeManager() {
        super("centrifuge", CentrifugeRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTFluidIngredient output1,
                          @ZenCodeType.Nullable CTFluidIngredient output2,
                          @ZenCodeType.Nullable CTFluidIngredient output3,
                          @ZenCodeType.Nullable CTFluidIngredient output4,
                          @ZenCodeType.Nullable CTFluidIngredient output5,
                          @ZenCodeType.Nullable CTFluidIngredient output6,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output1, output2, output3, output4, output5, output6), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTChanceFluidIngredient output1,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output2,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output3,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output4,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output5,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output6,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output1, output2, output3, output4, output5, output6), timeModifier, powerModifier, radiation);
    }
}
