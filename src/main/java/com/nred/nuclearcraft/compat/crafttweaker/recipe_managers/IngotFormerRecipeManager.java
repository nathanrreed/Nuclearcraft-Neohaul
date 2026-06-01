package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.IngotFormerRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.IngotFormerRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/IngotFormerRecipeManager")
public class IngotFormerRecipeManager extends BasicNuclearRecipeManager<IngotFormerRecipe> {

    @ZenCodeGlobals.Global("nuclearIngotFormer")
    public static final IngotFormerRecipeManager INSTANCE = new IngotFormerRecipeManager();

    public IngotFormerRecipeManager() {
        super("ingot_former", IngotFormerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          IItemStack output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, null, compact(output), compact(input), null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipeWithChance(String name,
                          CTFluidIngredient input,
                          Percentaged<IItemStack> output,
                          double timeModifier,
                          double powerModifier,
                          double radiation) {
        addRecipeInternal(name, null, compact(output), compact(input), null, timeModifier, powerModifier, radiation);
    }
}
