package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.recipe.*;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public abstract class AbstractRecipeHandler<RECIPE extends BasicRecipe> {
    protected List<RECIPE> recipeList = Collections.emptyList();

    public abstract String getName();

    public static final IntList INVALID = new IntArrayList(new int[]{-1});

    public List<RECIPE> getRecipeList(@NotNull RecipeManager recipeManager) {
        if (recipeList.isEmpty())
            setRecipes(recipeManager);
        return recipeList;
    }

    public @Nullable RecipeInfo<RECIPE> getRecipeInfoFromInputs(Level level, List<ItemStack> itemInputs, List<Tank> fluidInputs) { // TODO readd caching
        List<SizedChanceItemIngredient> itemIngredients = itemInputs.stream().map(itemStack -> SizedChanceItemIngredient.of(itemStack.getItem(), itemStack.getCount())).toList(); // Tries without filtering
        List<SizedChanceFluidIngredient> fluidIngredients = fluidInputs.stream().map(tank -> tank.isEmpty() ? SizedChanceFluidIngredient.EMPTY : SizedChanceFluidIngredient.of(tank.getFluid())).toList();
        RECIPE recipe = getRecipeFromIngredients(level, itemIngredients, fluidIngredients);
        if (recipe == null) { // Retries after filtering out empty ingredients
            itemIngredients = itemInputs.stream().filter(itemStack -> !itemStack.isEmpty()).map(itemStack -> SizedChanceItemIngredient.of(itemStack.getItem(), itemStack.getCount())).toList();
            fluidIngredients = fluidInputs.stream().filter(tank -> !tank.isEmpty()).map(tank -> tank.isEmpty() ? new SizedChanceFluidIngredient(FluidIngredient.empty(), 1) : SizedChanceFluidIngredient.of(tank.getFluid())).toList();
            recipe = getRecipeFromIngredients(level, itemIngredients, fluidIngredients);
        }

        if (recipe == null)
            return null;

        return new RecipeInfo<>(recipe, RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, itemInputs, fluidInputs));
    }

    public static @Nullable BasicRecipe getRecipeFromIngredients(Level level, RecipeType<? extends BasicRecipe> recipeType, List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients) {
        List<? extends RecipeHolder<? extends BasicRecipe>> recipes = level.getRecipeManager().getRecipesFor(recipeType, new BasicRecipeInput(itemIngredients, fluidIngredients), level);
        assert recipes.size() <= 1; // Make sure there is no overlapping recipes
        return recipes.stream().findFirst().map(RecipeHolder::value).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public @Nullable RECIPE getRecipeFromIngredients(Level level, List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients) {
        return (RECIPE) getRecipeFromIngredients(level, (RecipeType<? extends BasicRecipe>) BuiltInRegistries.RECIPE_TYPE.get(ncLoc(getName())), itemIngredients, fluidIngredients);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public List<RECIPE> setRecipes(@NotNull RecipeManager recipeManager) {
        if (recipeList.isEmpty()) {
            recipeList = recipeManager.getAllRecipesFor((RecipeType<RECIPE>) BuiltInRegistries.RECIPE_TYPE.get(ncLoc(getName()))).stream().map(RecipeHolder::value).toList();
        }
        return recipeList;
    }

    public void init(RecipeManager recipeManager) {
        setRecipes(recipeManager);
    }

    public void postInit(RecipeManager recipeManager) {
    }

    public void postReload(RecipeManager recipeManager) {
        recipeList = Collections.emptyList(); // Void the list
        postInit(recipeManager);
    }
}