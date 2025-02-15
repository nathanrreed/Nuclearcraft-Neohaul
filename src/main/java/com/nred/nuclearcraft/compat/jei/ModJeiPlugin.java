package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.recipe.base_types.ItemToItemRecipe;
import com.nred.nuclearcraft.recipe.processor.AlloyFurnaceRecipe;
import com.nred.nuclearcraft.recipe.processor.ManufactoryRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.HashMap;
import java.util.Map;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_TYPES;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    public static Map<String, IRecipeCategory<? extends ItemToItemRecipe>> PROCESSOR_CATEGORIES = Map.of();

    @Override
    public ResourceLocation getPluginUid() {
        return ncLoc("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        PROCESSOR_CATEGORIES = makeCategories(registration);
        for (IRecipeCategory<?> category : PROCESSOR_CATEGORIES.values()) {
            registration.addRecipeCategories(category);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (String type : PROCESSOR_CATEGORIES.keySet()) {
            registration.addRecipeCatalysts(PROCESSOR_CATEGORIES.get(type).getRecipeType(), PROCESSOR_MAP.get(type));
        }
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
//        registration.addRecipeTransferHandler(InfuserMenu::class.java, ModMenuTypes.INFUSER_MENU.get(), InfuserCategory.TYPE, 0, 3, 3, 36)
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        for (String type : PROCESSOR_CATEGORIES.keySet()) {
            registration.addRecipes((RecipeType<ItemToItemRecipe>) PROCESSOR_CATEGORIES.get(type).getRecipeType(), recipeManager.getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(type).get()).stream().map(RecipeHolder::value).toList());
        }
    }

    private static Map<String, IRecipeCategory<? extends ItemToItemRecipe>> makeCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        Map<String, IRecipeCategory<? extends ItemToItemRecipe>> map = new HashMap<>();
        map.put("alloy_furnace", new JeiItemToItemCategory<>(helper, "alloy_furnace", AlloyFurnaceRecipe.class));
        map.put("manufactory", new JeiItemToItemCategory<>(helper, "manufactory", ManufactoryRecipe.class));
        return map;
    }
}