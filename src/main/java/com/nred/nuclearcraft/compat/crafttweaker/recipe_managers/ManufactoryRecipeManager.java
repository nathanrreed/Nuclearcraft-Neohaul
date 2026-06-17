package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.ManufactoryRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.MANUFACTORY_RECIPE_TYPE;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.ManufactoryRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/ManufactoryRecipeManager")
public class ManufactoryRecipeManager extends BasicNuclearRecipeManager<ManufactoryRecipe> {
    static final ManufactoryRecipeManager INSTANCE = new ManufactoryRecipeManager();

    public ManufactoryRecipeManager() {
        super("manufactory", ManufactoryRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          IItemStack output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), compact(output), null, null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount input,
                          CTChanceItemIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(input), compact(output), null, null, timeModifier, powerModifier, radiation);
    }

    @Override
    public RecipeType<ManufactoryRecipe> getRecipeType() {
        return MANUFACTORY_RECIPE_TYPE.get();
    }
}