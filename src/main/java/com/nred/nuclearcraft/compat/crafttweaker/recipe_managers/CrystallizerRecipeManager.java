package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.processor.CrystallizerRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.CrystallizerRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/CrystallizerRecipeManager")
public class CrystallizerRecipeManager extends BasicNuclearRecipeManager<CrystallizerRecipe> {

    @ZenCodeGlobals.Global("nuclearCrystallizer")
    public static final CrystallizerRecipeManager INSTANCE = new CrystallizerRecipeManager();

    public CrystallizerRecipeManager() {
        super("crystallizer", CrystallizerRecipe.class);
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
