//package com.nred.nuclearcraft.recipe.processor;
//
//import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.*;
//import net.minecraft.world.level.Level;
//
//import java.util.List;
//
//import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.COLLECTOR_RECIPE_SERIALIZER;
//import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;
//
//record ItemToItemsInput() implements RecipeInput {
//    @Override
//    public ItemStack getItem(int index) {
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    public int size() {
//        return 0;
//    }
//}
//
//public record ItemToItemsRecipe(ItemStack itemInput, List<Ingredient> itemResults) implements Recipe<ItemToItemsInput> {
//    @Override
//    public boolean matches(ItemToItemsInput input, Level level) {
//        return false;
//    }
//
//    @Override
//    public boolean isSpecial() {
//        return true;
//    }
//
//    @Override
//    public ItemStack assemble(ItemToItemsInput input, HolderLookup.Provider registries) {
//        return itemResults.getFirst().copy();
//    }
//
//    @Override
//    public boolean canCraftInDimensions(int width, int height) {
//        return true;
//    }
//
//    @Override
//    public ItemStack getResultItem(HolderLookup.Provider registries) {
//        return itemResult.copy();
//    }
//
//    @Override
//    public RecipeSerializer<?> getSerializer() {
//        return COLLECTOR_RECIPE_SERIALIZER.get();
//    }
//
//    @Override
//    public RecipeType<?> getType() {
//       return NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE.get();
//    }
//}