package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.block.internal.fluid.Tank;
import com.nred.nuclearcraft.recipe.*;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public abstract class AbstractRecipeHandler<RECIPE extends BasicRecipe> {
    protected @Nonnull List<RECIPE> recipeList = new ArrayList<>();

    public abstract String getName();

    public static final IntList INVALID = new IntArrayList(new int[]{-1});

    public abstract List<RECIPE> getRecipeList();

    public @Nullable RecipeInfo<RECIPE> getRecipeInfoFromInputs(Level level, List<ItemStack> itemInputs, List<Tank> fluidInputs) { // TODO readd caching
        List<SizedIngredient> itemIngredients = itemInputs.stream().filter(itemStack -> !itemStack.isEmpty()).map(itemStack -> SizedIngredient.of(itemStack.getItem(), itemStack.getCount())).toList();
        List<SizedFluidIngredient> fluidIngredients = fluidInputs.stream().filter(tank -> !tank.isEmpty()).map(tank -> tank.isEmpty() ? new SizedFluidIngredient(FluidIngredient.empty(), 1) : SizedFluidIngredient.of(tank.getFluid())).toList();
        RECIPE recipe = getRecipeFromIngredients(level, itemIngredients, fluidIngredients);
        if (recipe == null)
            return null;
        return new RecipeInfo<>(recipe, RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, itemInputs, fluidInputs));
    }


    public static @Nullable BasicRecipe getRecipeFromIngredients(Level level, RecipeType<? extends BasicRecipe> recipeType, List<SizedIngredient> itemIngredients, List<SizedFluidIngredient> fluidIngredients) {
        List<? extends RecipeHolder<? extends BasicRecipe>> recipes = level.getRecipeManager().getRecipesFor(recipeType, new BasicRecipeInput(itemIngredients, fluidIngredients), level);
        assert recipes.size() <= 1; // Make sure there is no overlapping recipes
        return recipes.stream().findFirst().map(RecipeHolder::value).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public @Nullable RECIPE getRecipeFromIngredients(Level level, List<SizedIngredient> itemIngredients, List<SizedFluidIngredient> fluidIngredients) {
        return (RECIPE) getRecipeFromIngredients(level, (RecipeType<? extends BasicRecipe>) BuiltInRegistries.RECIPE_TYPE.get(ncLoc(getName())), itemIngredients, fluidIngredients);
    }

    public @Nullable RECIPE getRecipeFromProducts(List<SizedIngredient> itemProducts, List<SizedFluidIngredient> fluidProducts) {
        for (RECIPE recipe : recipeList) {
            if (RecipeHelper.matchIngredients(IngredientSorption.OUTPUT, recipe.getItemProducts(), recipe.getFluidProducts(), itemProducts, fluidProducts).isMatch) {
                return recipe;
            }
        }
        return null;
    }

    public void init(RecipeManager recipeManager) {
        recipeList = getRecipes(recipeManager);
    }

    @NotNull
    public List<RECIPE> getRecipes(@Nullable Level level) {
        RecipeManager recipeManager = null;
        if (level == null) {
            //Try to get a fallback level if we are in a context that may not have one
            //If we are on the client get the client's level, if we are on the server get the current server's level
            if (FMLEnvironment.dist.isClient()) {
                Level clientWorld = Minecraft.getInstance().level;
                if (clientWorld != null) {
                    recipeManager = clientWorld.getRecipeManager();
                }
            } else {
                MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
                if (currentServer != null) {
                    recipeManager = currentServer.getRecipeManager();
                }
            }
        } else {
            recipeManager = level.getRecipeManager();
        }
        if (recipeManager == null) {
            //If we failed, then return no recipes
            return Collections.emptyList();
        }
        return getRecipes(recipeManager);
    }

    @NotNull
    public List<RECIPE> getRecipes(@NotNull RecipeManager recipeManager) {
        if (recipeList.isEmpty()) {
            recipeList = recipeManager.getAllRecipesFor((RecipeType<RECIPE>) BuiltInRegistries.RECIPE_TYPE.get(ncLoc(getName()))).stream().map(RecipeHolder::value).toList();
        }
        return recipeList;
    }

//    public void postReload() {
//        postInit();
//        refreshCache();
//    }

//    public void refreshCache() {
//        recipeCache.clear();
//        fillHashCache();
//    }

//    protected abstract void fillHashCache();

//    protected boolean prepareMaterialListTuples(RECIPE recipe, List<Pair<List<ItemStack>, List<FluidStack>>> materialListTuples) {
//        List<List<ItemStack>> itemInputLists = StreamHelper.map(recipe.getItemIngredients(), IItemIngredient::getInputStackHashingList); TODO
//        List<List<FluidStack>> fluidInputLists = StreamHelper.map(recipe.getFluidIngredients(), IFluidIngredient::getInputStackHashingList);
//
//        int itemInputCount = itemInputLists.size(), fluidInputCount = fluidInputLists.size(), totalInputCount = itemInputCount + fluidInputCount;
//        int[] inputNumbers = new int[totalInputCount];
//
//        int[] maxNumbers = new int[totalInputCount];
//        for (int i = 0; i < itemInputCount; ++i) {
//            int maxNumber = itemInputLists.get(i).size() - 1;
//            if (maxNumber < 0) {
//                return false;
//            }
//            maxNumbers[i] = maxNumber;
//        }
//        for (int i = 0; i < fluidInputCount; ++i) {
//            int maxNumber = fluidInputLists.get(i).size() - 1;
//            if (maxNumber < 0) {
//                return false;
//            }
//            maxNumbers[i + itemInputCount] = maxNumber;
//        }
//
//        RecipeTupleGenerator.INSTANCE.generateMaterialListTuples(materialListTuples, maxNumbers, inputNumbers, itemInputLists, fluidInputLists, false);

//        return true;
//    }
//    public boolean isValidItemInput(ItemStack stack) {
//        for (RECIPE recipe : recipeList) {
//            for (IItemIngredient input : recipe.getItemIngredients()) {
//                if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean isValidFluidInput(FluidStack stack) {
//        for (RECIPE recipe : recipeList) {
//            for (IFluidIngredient input : recipe.getFluidIngredients()) {
//                if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean isValidItemOutput(ItemStack stack) {
//        for (RECIPE recipe : recipeList) {
//            for (IItemIngredient output : recipe.getItemProducts()) {
//                if (output.match(stack, IngredientSorption.OUTPUT).matches()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean isValidFluidOutput(FluidStack stack) {
//        for (RECIPE recipe : recipeList) {
//            for (IFluidIngredient output : recipe.getFluidProducts()) {
//                if (output.match(stack, IngredientSorption.OUTPUT).matches()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    // Recipe Ingredients TODO
//
//    public static OreIngredient oreStack(String oreType, int stackSize) {
//        if (!OreDictHelper.oreExists(oreType)) {
//            return null;
//        }
//        return new OreIngredient(oreType, stackSize);
//    }
//
//    public static FluidIngredient fluidStack(String fluidName, int stackSize) {
//        if (!FluidRegHelper.fluidExists(fluidName)) {
//            return null;
//        }
//        return new FluidIngredient(fluidName, stackSize);
//    }
//
//    public static List<OreIngredient> oreStackList(List<String> oreTypes, int stackSize) {
//        List<OreIngredient> oreStackList = new ArrayList<>();
//        for (String oreType : oreTypes) {
//            OreIngredient oreIngredient = oreStack(oreType, stackSize);
//            if (oreIngredient != null) {
//                oreStackList.add(oreIngredient);
//            }
//        }
//        return oreStackList;
//    }
//
//    public static List<FluidIngredient> fluidStackList(List<String> fluidNames, int stackSize) {
//        List<FluidIngredient> fluidStackList = new ArrayList<>();
//        for (String fluidName : fluidNames) {
//            FluidIngredient fluidIngredient = fluidStack(fluidName, stackSize);
//            if (fluidIngredient != null) {
//                fluidStackList.add(fluidIngredient);
//            }
//        }
//        return fluidStackList;
//    }
//
//    public static EmptyItemIngredient emptyItemStack() {
//        return new EmptyItemIngredient();
//    }
//
//    public static EmptyFluidIngredient emptyFluidStack() {
//        return new EmptyFluidIngredient();
//    }
//
//    public static ChanceItemIngredient chanceItemStack(ItemStack stack, int chancePercent) {
//        if (stack == null) {
//            return null;
//        }
//        return new ChanceItemIngredient(new ItemIngredient(stack), chancePercent);
//    }
//
//    public static ChanceItemIngredient chanceItemStack(ItemStack stack, int chancePercent, int minStackSize) {
//        if (stack == null) {
//            return null;
//        }
//        return new ChanceItemIngredient(new ItemIngredient(stack), chancePercent, minStackSize);
//    }
//
//    public static ChanceItemIngredient chanceOreStack(String oreType, int stackSize, int chancePercent) {
//        if (!OreDictHelper.oreExists(oreType)) {
//            return null;
//        }
//        return new ChanceItemIngredient(oreStack(oreType, stackSize), chancePercent);
//    }
//
//    public static ChanceItemIngredient chanceOreStack(String oreType, int stackSize, int chancePercent, int minStackSize) {
//        if (!OreDictHelper.oreExists(oreType)) {
//            return null;
//        }
//        return new ChanceItemIngredient(oreStack(oreType, stackSize), chancePercent, minStackSize);
//    }
//
//    public static ChanceFluidIngredient chanceFluidStack(String fluidName, int stackSize, int chancePercent, int stackDiff) {
//        if (!FluidRegHelper.fluidExists(fluidName)) {
//            return null;
//        }
//        return new ChanceFluidIngredient(fluidStack(fluidName, stackSize), chancePercent, stackDiff);
//    }
//
//    public static ChanceFluidIngredient chanceFluidStack(String fluidName, int stackSize, int chancePercent, int stackDiff, int minStackSize) {
//        if (!FluidRegHelper.fluidExists(fluidName)) {
//            return null;
//        }
//        return new ChanceFluidIngredient(fluidStack(fluidName, stackSize), chancePercent, stackDiff, minStackSize);
//    }
//
//    public static List<ChanceItemIngredient> chanceOreStackList(List<String> oreTypes, int stackSize, int chancePercent) {
//        List<ChanceItemIngredient> oreStackList = new ArrayList<>();
//        for (String oreType : oreTypes) {
//            ChanceItemIngredient chanceItemIngredient = chanceOreStack(oreType, stackSize, chancePercent);
//            if (chanceItemIngredient != null) {
//                oreStackList.add(chanceItemIngredient);
//            }
//        }
//        return oreStackList;
//    }
//
//    public static List<ChanceItemIngredient> chanceOreStackList(List<String> oreTypes, int stackSize, int chancePercent, int minStackSize) {
//        List<ChanceItemIngredient> oreStackList = new ArrayList<>();
//        for (String oreType : oreTypes) {
//            ChanceItemIngredient chanceItemIngredient = chanceOreStack(oreType, stackSize, chancePercent, minStackSize);
//            if (chanceItemIngredient != null) {
//                oreStackList.add(chanceItemIngredient);
//            }
//        }
//        return oreStackList;
//    }
//
//    public static List<ChanceFluidIngredient> chanceFluidStackList(List<String> fluidNames, int stackSize, int chancePercent, int stackDiff) {
//        List<ChanceFluidIngredient> fluidStackList = new ArrayList<>();
//        for (String fluidName : fluidNames) {
//            ChanceFluidIngredient chanceFluidIngredient = chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff);
//            if (chanceFluidIngredient != null) {
//                fluidStackList.add(chanceFluidIngredient);
//            }
//        }
//        return fluidStackList;
//    }
//
//    public static List<ChanceFluidIngredient> chanceFluidStackList(List<String> fluidNames, int stackSize, int chancePercent, int stackDiff, int minStackSize) {
//        List<ChanceFluidIngredient> fluidStackList = new ArrayList<>();
//        for (String fluidName : fluidNames) {
//            ChanceFluidIngredient chanceFluidIngredient = chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff, minStackSize);
//            if (chanceFluidIngredient != null) {
//                fluidStackList.add(chanceFluidIngredient);
//            }
//        }
//        return fluidStackList;
//    }
}
