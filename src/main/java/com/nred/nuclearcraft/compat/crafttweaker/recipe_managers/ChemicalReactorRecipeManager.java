package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.processor.ChemicalReactorRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.CHEMICAL_REACTOR_RECIPE_TYPE;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.ChemicalReactorRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/ChemicalReactorRecipeManager")
public class ChemicalReactorRecipeManager extends BasicNuclearRecipeManager<ChemicalReactorRecipe> {
    static final ChemicalReactorRecipeManager INSTANCE = new ChemicalReactorRecipeManager();

    public ChemicalReactorRecipeManager() {
        super("chemical_reactor", ChemicalReactorRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient left,
                          CTFluidIngredient right,
                          CTFluidIngredient output1,
                          @ZenCodeType.Nullable CTFluidIngredient output2,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(left, right), compact(output1, output2), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient left,
                          CTFluidIngredient right,
                          CTChanceFluidIngredient output1,
                          @ZenCodeType.Nullable CTChanceFluidIngredient output2,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(left, right), compact(output1, output2), timeModifier, powerModifier, radiation);
    }

    @Override
    public RecipeType<ChemicalReactorRecipe> getRecipeType() {
        return CHEMICAL_REACTOR_RECIPE_TYPE.get();
    }
}