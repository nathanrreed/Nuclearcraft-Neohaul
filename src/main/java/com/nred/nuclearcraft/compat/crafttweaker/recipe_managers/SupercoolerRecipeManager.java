package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.processor.SupercoolerRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.SUPERCOOLER_RECIPE_TYPE;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.SupercoolerRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/SupercoolerRecipeManager")
public class SupercoolerRecipeManager extends BasicNuclearRecipeManager<SupercoolerRecipe> {
    static final SupercoolerRecipeManager INSTANCE = new SupercoolerRecipeManager();

    public SupercoolerRecipeManager() {
        super("supercooler", SupercoolerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTChanceFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, (IIngredientWithAmount[]) null, compact(input), compact(output), timeModifier, powerModifier, radiation);
    }

    @Override
    public RecipeType<SupercoolerRecipe> getRecipeType() {
        return SUPERCOOLER_RECIPE_TYPE.get();
    }
}