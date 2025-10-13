package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.block.internal.fluid.Tank;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.IngredientSorption;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.util.StreamHelper;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.nred.nuclearcraft.recipe.RecipeHelper.matchFluidIngredient;
import static com.nred.nuclearcraft.recipe.RecipeHelper.matchIngredient;

public abstract class BasicRecipeHandler<RECIPE extends BasicRecipe> extends AbstractRecipeHandler<RECIPE> {
    public final String name;
    public final int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;
//    public final boolean isShapeless;

//    public final int itemInputLastIndex, fluidInputLastIndex, itemOutputLastIndex, fluidOutputLastIndex;

    public final List<Set<ResourceKey<Fluid>>> validFluids = new ArrayList<>();

    public BasicRecipeHandler(@Nonnull String name, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize) {
        this.name = name;
        this.itemInputSize = itemInputSize;
        this.fluidInputSize = fluidInputSize;
        this.itemOutputSize = itemOutputSize;
        this.fluidOutputSize = fluidOutputSize;
//        this.isShapeless = isShapeless;
//        this.itemInputLastIndex = itemInputSize;
//        this.fluidInputLastIndex = itemInputSize + fluidInputSize;
//        this.itemOutputLastIndex = itemInputSize + fluidInputSize + itemOutputSize;
//        this.fluidOutputLastIndex = itemInputSize + fluidInputSize + itemOutputSize + fluidOutputSize;
//        addRecipes();
    }


//    @SuppressWarnings("unchecked")
//    public List<Set<ResourceKey<Fluid>>> validFluids(Level level) {
//        List<RECIPE> recipes = getRecipeList(level);
//        if (recipes.isEmpty()) // TODO remove
//            return List.of(Set.of());
//        return reorder(recipes.stream().map(a -> a.fluidIngredients.stream().toList()).toList()).stream().map(a -> a.stream().flatMap(b -> Arrays.stream(b.getFluids())).collect(Collectors.toSet()).stream().map(c -> BuiltInRegistries.FLUID.getResourceKey(c.getFluid())).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet())).toList();
//    }
//
//    public static <A extends Object> List<List<A>> reorder(List<List<A>> input) { // [[1, 2, 3], [9, 8]] -> [[1, 9],[2, 8],[3]]
//        int size = Collections.max(input, Comparator.comparingInt(List::size)).size();
//        ArrayList<ArrayList<A>> rtn = new ArrayList<>(size);
//        for (int i = 0; i < size; i++) {
//            rtn.add(new ArrayList<>(input.size()));
//        }
//
//        for (List<A> q : input) {
//            for (int i = 0; i < q.size(); i++) {
//                rtn.get(i).add(q.get(i));
//            }
//        }
//
//        return rtn.stream().map(a -> a.stream().toList()).toList();
//    }

//    @Override
//    public void addRecipe(Object... objects) {
//        BasicRecipe recipe = buildRecipe(objects);
//        addRecipe(factor_recipes ? factorRecipe(recipe) : recipe);
//    }

//    public BasicRecipe newRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts, List<Object> extras, boolean shapeless) {
//        return new BasicRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, shapeless);
//    }
//
//    @Override
//    public boolean isValidRecipe(BasicRecipe recipe) {
//        if (itemOutputSize == 0 && fluidOutputSize == 0) {
//            return true;
//        }
//
//        for (SizedIngredient ingredient : recipe.getItemIngredients()) {
//            if (!ingredient.ingredient().hasNoItems()) {
//                return true;
//            }
//        }
//
//        for (SizedFluidIngredient ingredient : recipe.getFluidIngredients()) {
//            if (!ingredient.ingredient().hasNoFluids()) {
//                return true;
//            }
//        }
//
//        for (SizedIngredient ingredient : recipe.getItemProducts()) {
//            if (!ingredient.ingredient().hasNoItems()) {
//                return true;
//            }
//        }
//
//        for (SizedFluidIngredient ingredient : recipe.getFluidProducts()) {
//            if (!ingredient.ingredient().hasNoFluids()) {
//                return true;
//            }
//        }
//
//        return false;
//    }

//    public void addGTCERecipes() { TODO
//        if (NCRecipes.hasGTCEIntegration(name)) {
//            for (BasicRecipe recipe : recipeList) {
//                GTCERecipeHelper.addGTCERecipe(name, recipe);
//            }
//        }
//    }

//    protected abstract List<Object> fixedExtras(List<Object> extras);
//
//    public static class ExtrasFixer {
//
//        protected final List<Object> extras;
//        public final List<Object> fixed = new ArrayList<>();
//
//        protected int currentIndex = 0;
//
//        public ExtrasFixer(List<Object> extras) {
//            this.extras = extras;
//        }
//
//        public <T> void add(Class<? extends T> clazz, T defaultValue) {
//            int index = currentIndex++;
//            Object extra;
//            fixed.add(index < extras.size() && (extra = tryCast(clazz, extras.get(index))) != null ? extra : defaultValue);
//        }
//
//        public static Object tryCast(Class<?> targetClass, Object value) {
//            if (value == null) {
//                return null;
//            }
//
//            if (value instanceof Byte byteValue) {
//                return castInt(targetClass, byteValue);
//            } else if (value instanceof Short shortValue) {
//                return castInt(targetClass, shortValue);
//            } else if (value instanceof Integer intValue) {
//                return castInt(targetClass, intValue);
//            } else if (value instanceof Long longValue) {
//                return castInt(targetClass, longValue);
//            } else if (value instanceof BigInteger bigIntValue) {
//                return castInt(targetClass, bigIntValue);
//            } else if (value instanceof Float floatValue) {
//                return castFloat(targetClass, floatValue);
//            } else if (value instanceof Double doubleValue) {
//                return castFloat(targetClass, doubleValue);
//            } else if (value instanceof BigDecimal bigDecimalValue) {
//                return castFloat(targetClass, bigDecimalValue);
//            }
//
//            return castDefault(targetClass, value);
//        }
//
//        private static <T extends Number> Number castInt(Class<?> targetClass, T value) {
//            if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
//                return value.byteValue();
//            } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
//                return value.shortValue();
//            } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
//                return value.intValue();
//            } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
//                return value.longValue();
//            } else {
//                return castDefault(targetClass, value);
//            }
//        }
//
//        private static <T extends Number> Number castFloat(Class<?> targetClass, T value) {
//            if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
//                return value.floatValue();
//            } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
//                return value.doubleValue();
//            } else {
//                return castDefault(targetClass, value);
//            }
//        }
//
//        private static <T> T castDefault(Class<?> targetClass, T value) {
//            return targetClass.isInstance(value) ? value : null;
//        }
//    }

//    protected BasicRecipe factorRecipe(BasicRecipe recipe) {
//        if (recipe == null) {
//            return null;
//        }
//        if (!recipe.getItemIngredients().isEmpty() || !recipe.getItemProducts().isEmpty()) {
//            return recipe;
//        }
//
//        IntList stackSizes = new IntArrayList();
//        for (IFluidIngredient ingredient : recipe.getFluidIngredients()) {
//            stackSizes.addAll(ingredient.getFactors());
//        }
//        for (IFluidIngredient ingredient : recipe.getFluidProducts()) {
//            stackSizes.addAll(ingredient.getFactors());
//        }
//        stackSizes.addAll(getExtraFactors(recipe.getExtras()));
//
//        int hcf = NCMath.hcf(stackSizes.toIntArray());
//        if (hcf == 1) {
//            return recipe;
//        }
//
//        UnaryOperator<List<IFluidIngredient>> factor = x -> StreamHelper.map(x, y -> y.getFactoredIngredient(hcf));
//
//        return newRecipe(recipe.getItemIngredients(), factor.apply(recipe.getFluidIngredients()), recipe.getItemProducts(), factor.apply(recipe.getFluidProducts()), getFactoredExtras(recipe.getExtras(), hcf), recipe.isShapeless());
//    }
//
//    protected IntList getExtraFactors(List<Object> extras) {
//        return new IntArrayList();
//    }
//
//    protected List<Object> getFactoredExtras(List<Object> extras, int factor) {
//        return extras;
//    }
//
//    @Nullable
//    public BasicRecipe buildRecipe(Object... objects) {
//        List<Object> itemInputs = new ArrayList<>(), fluidInputs = new ArrayList<>(), itemOutputs = new ArrayList<>(), fluidOutputs = new ArrayList<>(), extras = new ArrayList<>();
//        for (int i = 0; i < objects.length; ++i) {
//            Object object = objects[i];
//            if (i < itemInputLastIndex) {
//                itemInputs.add(object);
//            } else if (i < fluidInputLastIndex) {
//                fluidInputs.add(object);
//            } else if (i < itemOutputLastIndex) {
//                itemOutputs.add(object);
//            } else if (i < fluidOutputLastIndex) {
//                fluidOutputs.add(object);
//            } else {
//                extras.add(object);
//            }
//        }
//        return buildRecipe(itemInputs, fluidInputs, itemOutputs, fluidOutputs, extras, isShapeless);
//    }
//
//    protected static <T, V extends IIngredient<T>> V buildIngredientInternal(Object obj, Predicate<Object> isValidType, Function<Object, V> buildIngredient) {
//        if (obj != null && isValidType.test(obj)) {
//            V ingredient = buildIngredient.apply(obj);
//            if (ingredient != null) {
//                return ingredient;
//            }
//        }
//        return null;
//    }

//    protected static IItemIngredient buildItemInputIngredientInternal(Object obj) { TODO
//        return buildIngredientInternal(obj, AbstractRecipeHandler::isValidItemInputType, RecipeHelper::buildItemIngredient);
//    }
//
//    protected static IFluidIngredient buildFluidInputIngredientInternal(Object obj) {
//        return buildIngredientInternal(obj, AbstractRecipeHandler::isValidFluidInputType, RecipeHelper::buildFluidIngredient);
//    }
//
//    protected static IItemIngredient buildItemOutputIngredientInternal(Object obj) {
//        return buildIngredientInternal(obj, AbstractRecipeHandler::isValidItemOutputType, RecipeHelper::buildItemIngredient);
//    }
//
//    protected static IFluidIngredient buildFluidOutputIngredientInternal(Object obj) {
//        return buildIngredientInternal(obj, AbstractRecipeHandler::isValidFluidOutputType, RecipeHelper::buildFluidIngredient);
//    }

//    @Nullable TODO
//    public BasicRecipe buildRecipe(List<?> itemInputs, List<?> fluidInputs, List<?> itemOutputs, List<?> fluidOutputs, List<Object> extras, boolean shapeless) {
//        List<IItemIngredient> itemIngredients = new ArrayList<>(), itemProducts = new ArrayList<>();
//        List<IFluidIngredient> fluidIngredients = new ArrayList<>(), fluidProducts = new ArrayList<>();
//        for (Object obj : itemInputs) {
//            IItemIngredient input = buildItemInputIngredientInternal(obj);
//            if (input == null) {
//                return null;
//            }
//            itemIngredients.add(input);
//        }
//        for (Object obj : fluidInputs) {
//            IFluidIngredient input = buildFluidInputIngredientInternal(obj);
//            if (input == null) {
//                return null;
//            }
//            fluidIngredients.add(input);
//        }
//        for (Object obj : itemOutputs) {
//            IItemIngredient output = buildItemOutputIngredientInternal(obj);
//            if (output == null) {
//                return null;
//            }
//            itemProducts.add(output);
//        }
//        for (Object obj : fluidOutputs) {
//            IFluidIngredient output = buildFluidOutputIngredientInternal(obj);
//            if (output == null) {
//                return null;
//            }
//            fluidProducts.add(output);
//        }
//        if (!isValidRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts)) {
//            NCUtil.getLogger().info("A " + name + " recipe failed to be registered: " + RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
//        }
//        return newRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, fixedExtras(extras), shapeless);
//    }

//    public boolean isValidRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
//        return itemIngredients.size() == itemInputSize && fluidIngredients.size() == fluidInputSize && itemProducts.size() == itemOutputSize && fluidProducts.size() == fluidOutputSize;
//    }

    @Override
    public String getName() {
        return name;
    }

//    public static BasicRecipeHandler get(String name) { TODO
//        return NCRecipes.getHandler(name);
//    }

//    @SuppressWarnings("unchecked") TODO
//    public List<RECIPE> getRecipeList(Level level) {
//        return (List<RECIPE>) level.getRecipeManager().getAllRecipesFor((RecipeType<? extends BasicRecipe>) BuiltInRegistries.RECIPE_TYPE.get(ncLoc(getName()))).stream().map(RecipeHolder::value).toList();
//    }

    @Override
    public List<RECIPE> getRecipeList() {
        return recipeList;
    }

    public int getItemInputSize() {
        return itemInputSize;
    }

    public int getFluidInputSize() {
        return fluidInputSize;
    }

    public int getItemOutputSize() {
        return itemOutputSize;
    }

    public int getFluidOutputSize() {
        return fluidOutputSize;
    }

    @Override
    public void init(RecipeManager recipeManager) {
        super.init(recipeManager);

        this.setValidFluids(recipeManager);
    }

    public void clearRecipes() {
        validFluids.clear();
    }

    protected void setValidFluids(RecipeManager recipeManager) {
        if (recipeList.isEmpty())
            getRecipes(recipeManager);
        clearRecipes();
        validFluids.addAll(RecipeHelper.validFluids(this));
    }

    public boolean isValidInput(ItemStack stack, Function<BasicRecipe, List<SizedIngredient>> ingredientsFunction) {
        for (BasicRecipe recipe : recipeList) {
            for (SizedIngredient input : ingredientsFunction.apply(recipe)) {
                if (matchIngredient(input, stack, IngredientSorption.NEUTRAL).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidInput(FluidStack stack, Function<BasicRecipe, List<SizedFluidIngredient>> ingredientsFunction) {
        for (BasicRecipe recipe : recipeList) {
            for (SizedFluidIngredient input : ingredientsFunction.apply(recipe)) {
                if (matchFluidIngredient(input, stack, IngredientSorption.NEUTRAL).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidItemInput(ItemStack stack) {
        return isValidInput(stack, BasicRecipe::getItemIngredients);
    }

    public boolean isValidFluidInput(FluidStack stack) {
        return isValidInput(stack, BasicRecipe::getFluidIngredients);
    }

    /**
     * Smart insertion - don't insert if stack is not valid for any possible recipes
     */
    public boolean isValidItemInput(ItemStack stack, int index, List<ItemStack> inputs, List<FluidStack> associatedInputs, RecipeInfo<BasicRecipe> recipeInfo, int inputSize, int associatedInputSize, Predicate<ItemStack> isEmptyFunction, Predicate<FluidStack> associatedIsEmptyFunction, Predicate<ItemStack> isEqualFunction, Function<BasicRecipe, List<SizedIngredient>> ingredientsFunction, Function<BasicRecipe, List<SizedFluidIngredient>> associatedIngredientsFunction) {
        List<ItemStack> otherInputs = inputsExcludingIndex(inputs, index);
        if ((otherInputs.stream().allMatch(isEmptyFunction) && associatedInputs.stream().allMatch(associatedIsEmptyFunction)) || isEqualFunction.test(inputs.get(index))) {
            return isValidInput(stack, ingredientsFunction);
        }

        if (recipeInfo == null) {
            ObjectSet<BasicRecipe> recipes = new ObjectOpenHashSet<>(recipeList);
            recipeLoop:
            for (BasicRecipe recipe : recipeList) {
                List<SizedIngredient> ingredients = ingredientsFunction.apply(recipe);
                List<SizedFluidIngredient> associatedIngredients = associatedIngredientsFunction.apply(recipe);
                stackLoop:
                for (ItemStack inputStack : inputs) {
                    if (!isEmptyFunction.test(inputStack)) {
                        for (SizedIngredient recipeInput : ingredients) {
                            if (matchIngredient(recipeInput, inputStack, IngredientSorption.NEUTRAL).matches()) {
                                continue stackLoop;
                            }
                        }
                        recipes.remove(recipe);
                        continue recipeLoop;
                    }
                }

                associatedStackLoop:
                for (FluidStack inputStack : associatedInputs) {
                    if (!associatedIsEmptyFunction.test(inputStack)) {
                        for (SizedFluidIngredient recipeInput : associatedIngredients) {
                            if (matchFluidIngredient(recipeInput, inputStack, IngredientSorption.NEUTRAL).matches()) {
                                continue associatedStackLoop;
                            }
                        }
                        recipes.remove(recipe);
                        continue recipeLoop;
                    }
                }
            }

            for (BasicRecipe recipe : recipes) {
                if (isValidItemInputInternal(stack, otherInputs, ingredientsFunction.apply(recipe), isEmptyFunction)) {
                    return true;
                }
            }
            return false;
        } else {
            return isValidItemInputInternal(stack, otherInputs, ingredientsFunction.apply(recipeInfo.recipe), isEmptyFunction);
        }
    }

    public boolean isValidFluidInput(FluidStack stack, int index, List<FluidStack> inputs, List<ItemStack> associatedInputs, RecipeInfo<BasicRecipe> recipeInfo, int inputSize, int associatedInputSize, Predicate<FluidStack> isEmptyFunction, Predicate<ItemStack> associatedIsEmptyFunction, Predicate<FluidStack> isEqualFunction, Function<BasicRecipe, List<SizedFluidIngredient>> ingredientsFunction, Function<BasicRecipe, List<SizedIngredient>> associatedIngredientsFunction) {
        List<FluidStack> otherInputs = inputsExcludingIndex(inputs, index);
        if ((otherInputs.stream().allMatch(isEmptyFunction) && associatedInputs.stream().allMatch(associatedIsEmptyFunction)) || isEqualFunction.test(inputs.get(index))) {
            return isValidInput(stack, ingredientsFunction);
        }

        if (recipeInfo == null) {
            ObjectSet<BasicRecipe> recipes = new ObjectOpenHashSet<>(recipeList);
            recipeLoop:
            for (BasicRecipe recipe : recipeList) {
                List<SizedFluidIngredient> ingredients = ingredientsFunction.apply(recipe);
                List<SizedIngredient> associatedIngredients = associatedIngredientsFunction.apply(recipe);
                stackLoop:
                for (FluidStack inputStack : inputs) {
                    if (!isEmptyFunction.test(inputStack)) {
                        for (SizedFluidIngredient recipeInput : ingredients) {
                            if (matchFluidIngredient(recipeInput, inputStack, IngredientSorption.NEUTRAL).matches()) {
                                continue stackLoop;
                            }
                        }
                        recipes.remove(recipe);
                        continue recipeLoop;
                    }
                }

                associatedStackLoop:
                for (ItemStack inputStack : associatedInputs) {
                    if (!associatedIsEmptyFunction.test(inputStack)) {
                        for (SizedIngredient recipeInput : associatedIngredients) {
                            if (matchIngredient(recipeInput, inputStack, IngredientSorption.NEUTRAL).matches()) {
                                continue associatedStackLoop;
                            }
                        }
                        recipes.remove(recipe);
                        continue recipeLoop;
                    }
                }

            }

            for (BasicRecipe recipe : recipes) {
                if (isValidFluidInputInternal(stack, otherInputs, ingredientsFunction.apply(recipe), isEmptyFunction)) {
                    return true;
                }
            }
            return false;
        } else {
            return isValidFluidInputInternal(stack, otherInputs, ingredientsFunction.apply(recipeInfo.recipe), isEmptyFunction);
        }
    }


    public boolean isValidItemInput(ItemStack stack, int slot, List<ItemStack> itemInputs, List<Tank> fluidInputs, RecipeInfo<BasicRecipe> recipeInfo) {
        return isValidItemInput(stack, slot, itemInputs, StreamHelper.map(fluidInputs, Tank::getFluid), recipeInfo, itemInputSize, fluidInputSize, ItemStack::isEmpty, x -> x == null || x.getAmount() <= 0, x -> ItemStack.isSameItemSameComponents(stack, x), BasicRecipe::getItemIngredients, BasicRecipe::getFluidIngredients);
    }

    public boolean isValidFluidInput(FluidStack stack, int tankNumber, List<Tank> fluidInputs, List<ItemStack> itemInputs, RecipeInfo<BasicRecipe> recipeInfo) {
        return isValidFluidInput(stack, tankNumber, StreamHelper.map(fluidInputs, Tank::getFluid), itemInputs, recipeInfo, fluidInputSize, itemInputSize, x -> x == null || x.getAmount() <= 0, ItemStack::isEmpty, stack == null ? Objects::isNull : x -> (FluidStack.isSameFluidSameComponents(stack, x)), BasicRecipe::getFluidIngredients, BasicRecipe::getItemIngredients);
    }

    protected boolean isValidItemInputInternal(ItemStack stack, List<ItemStack> otherInputs, List<SizedIngredient> ingredients, Predicate<ItemStack> isEmptyFunction) {
        for (SizedIngredient input : ingredients) {
            if (matchIngredient(input, stack, IngredientSorption.NEUTRAL).matches()) {
                for (ItemStack other : otherInputs) {
                    if (!isEmptyFunction.test(other) && matchIngredient(input, other, IngredientSorption.NEUTRAL).matches()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected boolean isValidFluidInputInternal(FluidStack stack, List<FluidStack> otherInputs, List<SizedFluidIngredient> ingredients, Predicate<FluidStack> isEmptyFunction) {
        for (SizedFluidIngredient input : ingredients) {
            if (matchFluidIngredient(input, stack, IngredientSorption.NEUTRAL).matches()) {
                for (FluidStack other : otherInputs) {
                    if (!isEmptyFunction.test(other) && matchFluidIngredient(input, other, IngredientSorption.NEUTRAL).matches()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected static <T> List<T> inputsExcludingIndex(List<T> inputs, int index) {
        List<T> inputsExcludingIndex = new ArrayList<>(inputs);
        inputsExcludingIndex.remove(index);
        return inputsExcludingIndex;
    }
}