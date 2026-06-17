package com.nred.nuclearcraft.compat.crafttweaker.recipe_managers;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.compat.crafttweaker.ingredient.CTChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.IngotFormerRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.INGOT_FORMER_RECIPE_TYPE;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.recipe.manager.IngotFormerRecipeManager")
@Document("mods/nuclearcraft/recipe/manager/IngotFormerRecipeManager")
public class IngotFormerRecipeManager extends BasicNuclearRecipeManager<IngotFormerRecipe> {
    static final IngotFormerRecipeManager INSTANCE = new IngotFormerRecipeManager();

    public IngotFormerRecipeManager() {
        super("ingot_former", IngotFormerRecipe.class);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          IItemStack output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, compact(output), compact(input), null, timeModifier, powerModifier, radiation);
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          CTFluidIngredient input,
                          CTChanceItemIngredient output,
                          @ZenCodeType.OptionalDouble(1D) double timeModifier,
                          @ZenCodeType.OptionalDouble(1D) double powerModifier,
                          @ZenCodeType.OptionalDouble(0D) double radiation) {
        INSTANCE.addRecipeInternal(name, null, compact(output), compact(input), null, timeModifier, powerModifier, radiation);
    }

    @Override
    public RecipeType<IngotFormerRecipe> getRecipeType() {
        return INGOT_FORMER_RECIPE_TYPE.get();
    }
}