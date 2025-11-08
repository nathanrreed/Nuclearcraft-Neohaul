package com.nred.nuclearcraft.recipe;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.util.CollectionHelper;
import com.nred.nuclearcraft.util.StackHelper;
import com.nred.nuclearcraft.util.StreamHelper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.*;

public class RecipeHelper {
    public static ItemStack fixItemStack(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof ItemStack stack) {
            ItemStack copy = stack.copy();
            if (copy.getCount() <= 0) {
                copy.setCount(1);
            }
            return copy;
        } else if (object instanceof Item item) {
            return new ItemStack(item, 1);
        } else if (object instanceof Block block) {
            return new ItemStack(block, 1);
        } else {
            throw new RuntimeException(String.format("Invalid ItemStack: %s", object));
        }
    }

    public static FluidStack fixFluidStack(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof FluidStack stack) {
            FluidStack copy = stack.copy();
            if (copy.getAmount() <= 0) {
                copy.setAmount(1000);
            }
            return copy;
        } else if (object instanceof Fluid fluid) {
            return new FluidStack(fluid, 1000);
        } else {
            throw new RuntimeException(String.format("Invalid FluidStack: %s", object));
        }
    }

    public static List<ItemStack[]> getItemInputLists(List<SizedChanceItemIngredient> itemIngredientList) {
        return StreamHelper.map(itemIngredientList, SizedChanceItemIngredient::getItems);
    }

    public static List<FluidStack[]> getFluidInputLists(List<SizedFluidIngredient> fluidIngredientList) {
        return StreamHelper.map(fluidIngredientList, SizedFluidIngredient::getFluids);
    }

    public static List<ItemStack[]> getItemOutputLists(List<SizedChanceItemIngredient> itemIngredientList) {
        return StreamHelper.map(itemIngredientList, SizedChanceItemIngredient::getItems);
    }

    public static List<FluidStack[]> getFluidOutputLists(List<SizedFluidIngredient> fluidIngredientList) {
        return StreamHelper.map(fluidIngredientList, SizedFluidIngredient::getFluids);
    }

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
            return new IngredientMatchResult(type.checkStackSize(sizedIngredient.count(), itemstack.getCount()), 0);
        } else if (object instanceof SizedChanceItemIngredient ingredient && matchIngredient(sizedIngredient, ingredient.getStack(), type).matches()) {
            return new IngredientMatchResult(type.checkStackSize(sizedIngredient.count(), ingredient.count()), 0);
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
            return new IngredientMatchResult(type.checkStackSize(sizedIngredient.amount(), fluidstack.getAmount()), 0);
        } else if (object instanceof SizedChanceFluidIngredient ingredient && matchFluidIngredient(sizedIngredient, ingredient.getStack(), type).matches()) {
            return new IngredientMatchResult(type.checkStackSize(sizedIngredient.amount(), ingredient.amount()), 0);
        }
        return IngredientMatchResult.FAIL;
    }

    public static RecipeMatchResult matchIngredients(IngredientSorption sorption, List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients, List<?> items, List<?> fluids) {
        int itemCount = items.size(), fluidCount = fluids.size();
        if (itemIngredients.size() != itemCount || fluidIngredients.size() != fluidCount) {
            return RecipeMatchResult.FAIL;
        }

        IntList itemIngredientNumbers = new IntArrayList(new int[itemCount]);
        IntList fluidIngredientNumbers = new IntArrayList(new int[fluidCount]);
        IntList itemInputOrder = CollectionHelper.increasingList(itemCount);
        IntList fluidInputOrder = CollectionHelper.increasingList(fluidCount);

        List<SizedChanceItemIngredient> itemIngredientsRemaining = new ArrayList<>(itemIngredients);
        itemInputs:
        for (int i = 0; i < itemCount; ++i) {
            Object item = items.get(i);
            for (int j = 0; j < itemCount; ++j) {
                SizedChanceItemIngredient itemIngredient = itemIngredientsRemaining.get(j);
                if (itemIngredient == null) {
                    continue;
                }
                IngredientMatchResult matchResult = matchIngredient(itemIngredient, item, sorption);
                if (matchResult.matches()) {
                    itemIngredientsRemaining.set(j, null);
                    itemIngredientNumbers.set(i, matchResult.getIngredientNumber());
                    itemInputOrder.set(i, j);
                    continue itemInputs;
                }
            }
            return RecipeMatchResult.FAIL;
        }
        List<SizedChanceFluidIngredient> fluidIngredientsRemaining = new ArrayList<>(fluidIngredients);
        fluidInputs:
        for (int i = 0; i < fluidCount; ++i) {
            Object fluid = fluids.get(i);
            if (fluid instanceof Tank tank) {
                fluid = tank.getFluid();
            }
            for (int j = 0; j < fluidCount; ++j) {
                SizedChanceFluidIngredient fluidIngredient = fluidIngredientsRemaining.get(j);
                if (fluidIngredient == null) {
                    continue;
                }
                IngredientMatchResult matchResult = matchFluidIngredient(fluidIngredient, fluid, sorption);
                if (matchResult.matches()) {
                    fluidIngredientsRemaining.set(j, null);
                    fluidIngredientNumbers.set(i, matchResult.getIngredientNumber());
                    fluidInputOrder.set(i, j);
                    continue fluidInputs;
                }
            }
            return RecipeMatchResult.FAIL;
        }
        return new RecipeMatchResult(true, itemIngredientNumbers, fluidIngredientNumbers, itemInputOrder, fluidInputOrder);
    }

    public static List<Set<ResourceLocation>> validFluids(BasicRecipeHandler recipeHandler) {
        return validFluids(recipeHandler, Collections.emptySet());
    }

    public static List<Set<ResourceLocation>> validFluids(BasicRecipeHandler recipeHandler, Set<ResourceLocation> exceptions) {
        Set<ResourceLocation> fluidNameSet = new ObjectOpenHashSet<>();
        for (Map.Entry<ResourceKey<Fluid>, Fluid> entry : BuiltInRegistries.FLUID.entrySet()) {
            ResourceLocation fluidKey = entry.getKey().location();
            if (recipeHandler.isValidFluidInput(new FluidStack(entry.getValue(), 1000)) && !exceptions.contains(fluidKey)) {
                fluidNameSet.add(fluidKey);
            }
        }

        List<Set<ResourceLocation>> allowedFluidSets = new ArrayList<>();
        for (int i = 0; i < recipeHandler.fluidInputSize; ++i) {
            allowedFluidSets.add(fluidNameSet);
        }
        for (int i = recipeHandler.fluidInputSize; i < recipeHandler.fluidInputSize + recipeHandler.fluidOutputSize; ++i) {
            allowedFluidSets.add(null);
        }

        return allowedFluidSets;
    }

    public static long hashMaterials(List<ItemStack> items, List<FluidStack> fluids) {
        long hash = 1L;
        for (ItemStack stack : items) {
            hash = 31L * hash + (stack == null || stack.isEmpty() ? 0L : pack(stack));
        }
        for (FluidStack stack : fluids) {
            hash = 31L * hash + (stack == null ? 0L : BuiltInRegistries.FLUID.getId(stack.getFluid()));
        }
        return hash;
    }

    public static int pack(ItemStack stack) {
        return BuiltInRegistries.ITEM.getId(stack.getItem());
    }

    public static BasicRecipe blockRecipe(BasicRecipeHandler<? extends BasicRecipe> recipeHandler, Level level, BlockPos pos) {
        return blockRecipe(recipeHandler, level, level.getBlockState(pos));
    }

    public static BasicRecipe blockRecipe(BasicRecipeHandler<? extends BasicRecipe> recipeHandler, Level level, BlockState blockState) {
        RecipeInfo<? extends BasicRecipe> recipeInfo = recipeHandler.getRecipeInfoFromInputs(level, Lists.newArrayList(StackHelper.blockStateToStack(blockState)), Collections.emptyList());
        return recipeInfo == null ? null : recipeInfo.recipe;
    }
}