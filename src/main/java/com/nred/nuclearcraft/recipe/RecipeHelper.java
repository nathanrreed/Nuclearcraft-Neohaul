package com.nred.nuclearcraft.recipe;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.util.CollectionHelper;
import com.nred.nuclearcraft.util.StackHelper;
import com.nred.nuclearcraft.util.StreamHelper;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;

public class RecipeHelper {

    @Nullable
    public static ItemStack getItemStackFromIngredientList(List<SizedChanceItemIngredient> itemIngredientList, int pos) {
        if (pos < itemIngredientList.size()) {
            return itemIngredientList.get(pos).getStack();
        }
        return null;
    }

    public static IngredientMatchResult matchIngredient(SizedChanceItemIngredient sizedIngredient, Object object, IngredientSorption type) {
        if (object instanceof ItemStack itemstack) {
            if (!sizedIngredient.test(itemstack)) {
                return IngredientMatchResult.FAIL;
            }
            return new IngredientMatchResult(type.checkStackSize(sizedIngredient.count(), itemstack.getCount()));
        } else if (object instanceof SizedChanceItemIngredient ingredient && matchIngredient(sizedIngredient, ingredient.getStack(), type).matches()) {
            return new IngredientMatchResult(type.checkStackSize(sizedIngredient.count(), ingredient.count()));
        }
        return IngredientMatchResult.FAIL;
    }

    public static IngredientMatchResult matchFluidIngredient(SizedChanceFluidIngredient sizedIngredient, Object object, IngredientSorption type) {
        if (object instanceof Tank tank) {
            object = tank.getFluid();
        }
        if (object instanceof FluidStack fluidstack) {
            if (!sizedIngredient.test(fluidstack)) {
                return IngredientMatchResult.FAIL;
            }
            return new IngredientMatchResult(type.checkStackSize(sizedIngredient.amount(), fluidstack.getAmount()));
        } else if (object instanceof SizedChanceFluidIngredient ingredient && matchFluidIngredient(sizedIngredient, ingredient.getStack(), type).matches()) {
            return new IngredientMatchResult(type.checkStackSize(sizedIngredient.amount(), ingredient.amount()));
        }
        return IngredientMatchResult.FAIL;
    }

    public static RecipeMatchResult matchIngredients(IngredientSorption sorption, List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients, List<SizedChanceItemIngredient> items, List<SizedChanceFluidIngredient> fluids) {
        int itemCount = items.size(), fluidCount = fluids.size();
        if (itemIngredients.size() != itemCount || fluidIngredients.size() != fluidCount) {
            // Try filtering out empty
            items = items.stream().filter(itemIngredient -> !itemIngredient.isEmpty()).toList();
            fluids = fluids.stream().filter(fluidIngredient -> !fluidIngredient.isEmpty()).toList();
            itemCount = items.size();
            fluidCount = fluids.size();
            if (itemIngredients.size() != itemCount || fluidIngredients.size() != fluidCount) {
                return RecipeMatchResult.FAIL;
            }
        }

        IntList itemInputOrder = CollectionHelper.increasingList(itemCount);
        IntList fluidInputOrder = CollectionHelper.increasingList(fluidCount);

        List<SizedChanceItemIngredient> itemIngredientsRemaining = new ArrayList<>(itemIngredients);
        itemInputs:
        for (int i = 0; i < itemCount; ++i) {
            SizedChanceItemIngredient item = items.get(i);
            for (int j = 0; j < itemCount; ++j) {
                SizedChanceItemIngredient itemIngredient = itemIngredientsRemaining.get(j);
                if (itemIngredient == null) {
                    continue;
                }
                IngredientMatchResult matchResult = matchIngredient(itemIngredient, item, sorption);
                if (matchResult.matches()) {
                    itemIngredientsRemaining.set(j, null);
                    itemInputOrder.set(i, j);
                    continue itemInputs;
                }
            }
            return RecipeMatchResult.FAIL;
        }
        List<SizedChanceFluidIngredient> fluidIngredientsRemaining = new ArrayList<>(fluidIngredients);
        fluidInputs:
        for (int i = 0; i < fluidCount; ++i) {
            SizedChanceFluidIngredient fluid = fluids.get(i);
            for (int j = 0; j < fluidCount; ++j) {
                SizedChanceFluidIngredient fluidIngredient = fluidIngredientsRemaining.get(j);
                if (fluidIngredient == null) {
                    continue;
                }
                IngredientMatchResult matchResult = matchFluidIngredient(fluidIngredient, fluid, sorption);
                if (matchResult.matches()) {
                    fluidIngredientsRemaining.set(j, null);
                    fluidInputOrder.set(i, j);
                    continue fluidInputs;
                }
            }
            return RecipeMatchResult.FAIL;
        }
        return new RecipeMatchResult(true, itemInputOrder, fluidInputOrder);
    }

    public static List<Set<ResourceLocation>> validFluids(IRecipeHandler recipeHandler, RecipeManager recipeManager) {
        return validFluids(recipeHandler, Collections.emptySet(), recipeManager);
    }

    public static List<Set<ResourceLocation>> validFluids(IRecipeHandler recipeHandler, Set<ResourceLocation> exceptions, RecipeManager recipeManager) {
        Set<ResourceLocation> fluidNameSet = new ObjectOpenHashSet<>();
        for (Map.Entry<ResourceKey<Fluid>, Fluid> entry : BuiltInRegistries.FLUID.entrySet()) { // TODO do this better
            ResourceLocation fluidKey = entry.getKey().location();
            if (recipeHandler.isValidFluidInput(new FluidStack(entry.getValue(), 1000), recipeManager) && !exceptions.contains(fluidKey)) {
                fluidNameSet.add(fluidKey);
            }
        }

        List<Set<ResourceLocation>> allowedFluidSets = new ArrayList<>();
        for (int i = 0; i < recipeHandler.getFluidInputSize(); ++i) {
            allowedFluidSets.add(fluidNameSet);
        }
        for (int i = recipeHandler.getFluidInputSize(); i < recipeHandler.getFluidInputSize() + recipeHandler.getFluidOutputSize(); ++i) {
            allowedFluidSets.add(null);
        }

        return allowedFluidSets;
    }

    public static long hashMaterials(List<ItemStack> items, List<FluidStack> fluids) { // TODO use
        long hash = 1L;
        for (ItemStack stack : items) {
            hash = 31L * hash + (stack == null || stack.isEmpty() ? 0L : pack(stack));
        }
        for (FluidStack stack : fluids) {
            hash = 31L * hash + (stack == null ? 0L : pack(stack));
        }
        return hash;
    }

    public static int pack(ItemStack stack) {
        return BuiltInRegistries.ITEM.getId(stack.getItem());
    }

    public static int pack(Item item) {
        return BuiltInRegistries.ITEM.getId(item);
    }

    public static int pack(FluidStack stack) {
        return BuiltInRegistries.FLUID.getId(stack.getFluid());
    }

    public static ItemStack unpack(int id) {
        return new ItemStack(BuiltInRegistries.ITEM.byId(id));
    }

    public static BasicRecipe blockRecipe(BasicRecipeHandler<? extends BasicRecipe> recipeHandler, Level level, BlockPos pos) {
        return blockRecipe(recipeHandler, level, level.getBlockState(pos));
    }

    public static BasicRecipe blockRecipe(BasicRecipeHandler<? extends BasicRecipe> recipeHandler, Level level, BlockState blockState) {
        RecipeInfo<? extends BasicRecipe> recipeInfo = recipeHandler.getRecipeInfoFromInputs(level, Lists.newArrayList(StackHelper.blockStateToStack(blockState)), Collections.emptyList());
        return recipeInfo == null ? null : recipeInfo.recipe;
    }

    public static double getDecayTimeMultiplier(double radiation, double scale) {
        return radiation > scale ? (Math.log(2D) / Math.log1p(radiation / scale)) : (Math.log1p(scale / radiation) / Math.log(2D));
    }

    public static List<List<ItemStack>> getItemInputLists(List<SizedChanceItemIngredient> itemIngredientList) {
        return StreamHelper.map(itemIngredientList, (e) -> Arrays.stream(e.getItemsRaw()).toList());
    }

    public static List<List<FluidStack>> getFluidInputLists(List<SizedChanceFluidIngredient> fluidIngredientList) {
        return StreamHelper.map(fluidIngredientList, (e) -> Arrays.stream(e.getFluidsRaw()).toList());
    }

    public static List<List<ItemStack>> getItemOutputLists(List<SizedChanceItemIngredient> itemIngredientList) {
        return StreamHelper.map(itemIngredientList, (e) -> Arrays.stream(e.getItemsRaw()).toList());
    }

    public static List<List<FluidStack>> getFluidOutputLists(List<SizedChanceFluidIngredient> fluidIngredientList) {
        return StreamHelper.map(fluidIngredientList, (e) -> Arrays.stream(e.getFluidsRaw()).toList());
    }

    @Nullable
    public static ItemStack getItemStackFromProductList(List<SizedChanceItemIngredient> itemProductList, int pos) {
        if (pos < itemProductList.size()) {
            return itemProductList.get(pos).getStack();
        }
        return null;
    }

    @Nullable
    public static FluidStack getFluidStackFromProductList(List<SizedChanceFluidIngredient> fluidProductList, int pos) {
        if (pos < fluidProductList.size()) {
            return fluidProductList.get(pos).getStack();
        }
        return null;
    }

    public static BlockState getBlockStateFromIngredientList(List<SizedChanceItemIngredient> itemIngredientList, int pos) {
        ItemStack stack = RecipeHelper.getItemStackFromIngredientList(itemIngredientList, pos);
        return stack == null ? null : StackHelper.getBlockStateFromStack(stack);
    }

    public static BlockState getBlockStateFromProductList(List<SizedChanceItemIngredient> itemProductList, int pos) {
        ItemStack stack = RecipeHelper.getItemStackFromProductList(itemProductList, pos);
        return stack == null ? null : StackHelper.getBlockStateFromStack(stack);
    }
}