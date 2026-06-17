package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.processor.FluidEnricherRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FLUID_ENRICHER_RECIPE_TYPE;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.FluidEnricherRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/FluidEnricherRecipeManager")
public class FluidEnricherRecipeManager extends BasicNuclearRecipeManager<FluidEnricherRecipe> {
    static final FluidEnricherRecipeManager INSTANCE = new FluidEnricherRecipeManager();

    public FluidEnricherRecipeManager() {
        super("fluid_enricher", FluidEnricherRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount itemInput,
                          CTFluidIngredient fluidInput,
                          CTFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(itemInput), (IIngredientWithAmount[]) null, compact(fluidInput), compact(output), timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredientWithAmount itemInput,
                          CTFluidIngredient fluidInput,
                          CTChanceFluidIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, compact(itemInput), (IIngredientWithAmount[]) null, compact(fluidInput), compact(output), timeModifier, powerModifier, radiation);
    }

    @Override
    public RecipeType<FluidEnricherRecipe> getRecipeType() {
        return FLUID_ENRICHER_RECIPE_TYPE.get();
    }
}