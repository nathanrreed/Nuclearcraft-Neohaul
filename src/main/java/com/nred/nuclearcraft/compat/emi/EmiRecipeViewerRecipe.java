package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.TankWithAmount;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.RecipeViewer;
import com.nred.nuclearcraft.compat.recipe_viewer.info.RecipeViewerCategoryInfo;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.handler.BlockEntityInfoHandler.RECIPE_VIEWER_CATEGORY_INFO_MAP;

public abstract class EmiRecipeViewerRecipe extends BasicEmiRecipe {
    public final RecipeViewerCategoryInfo categoryInfo;
    private final RecipeViewer<?> recipeViewer;

    public EmiRecipeViewerRecipe(String name, EmiRecipeCategory category, ResourceLocation id, RecipeViewer<? extends BasicRecipe> recipeViewer) {
        super(category, id, 0, 0);
        this.recipeViewer = recipeViewer;
        this.categoryInfo = RECIPE_VIEWER_CATEGORY_INFO_MAP.get(name);

        BasicRecipe recipe = recipeViewer.recipe;

        if (!recipe.itemIngredients.isEmpty() && recipe.getItemIngredient().count() > 0) {
            this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        }
        if (!recipe.fluidIngredients.isEmpty() && recipe.getFluidIngredient().amount() > 0) {
            this.inputs.addAll(recipe.fluidIngredients.stream().map(ModEmiPlugin::getEmiFluidIngredient).toList());
        }
        if (!recipe.itemProducts.isEmpty() && recipe.getItemProduct().count() > 0) {
            this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
        }
        if (!recipe.fluidProducts.isEmpty() && recipe.getFluidProduct().amount() > 0) {
            this.outputs.addAll(recipe.fluidProducts.stream().map(ModEmiPlugin::getEmiFluidStack).toList());
        }
    }

    @Override
    public int getDisplayWidth() {
        return categoryInfo.getRecipeViewerBackgroundW();
    }

    @Override
    public int getDisplayHeight() {
        return categoryInfo.getRecipeViewerBackgroundH();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int backgroundX = categoryInfo.getRecipeViewerBackgroundX();
        int backgroundY = categoryInfo.getRecipeViewerBackgroundY();

        ResourceLocation texture = ResourceLocation.parse(categoryInfo.getRecipeViewerTexture());

        widgets.addTexture(texture, 0, 0, categoryInfo.getRecipeViewerBackgroundW(), categoryInfo.getRecipeViewerBackgroundH(), categoryInfo.getRecipeViewerBackgroundX(), categoryInfo.getRecipeViewerBackgroundY());

        int i = 0, f = 0;
        List<int[]> itemInputStackXY = categoryInfo.getItemInputStackXY();
        List<int[]> fluidInputStackXYWH = categoryInfo.getFluidInputGuiXYWH();
        for (EmiIngredient input : inputs) {
            if (input.getEmiStacks().getFirst().getKey() instanceof Fluid) {
                int[] stackXYWH = fluidInputStackXYWH.get(f++);
                widgets.add(new TankWithAmount(input, stackXYWH[0] - backgroundX - 1, stackXYWH[1] - backgroundY - 1)).drawBack(false);
            } else {
                int[] stackXY = itemInputStackXY.get(i++);
                widgets.addSlot(input, stackXY[0] - backgroundX - 1, stackXY[1] - backgroundY - 1).drawBack(false);
            }
        }

        i = 0;
        f = 0;
        List<int[]> itemOutputStackXY = categoryInfo.getItemOutputStackXY();
        List<int[]> fluidOutputStackXYWH = categoryInfo.getFluidOutputGuiXYWH();
        for (EmiStack output : outputs) {
            if (output.getKey() instanceof Fluid) {
                SizedChanceFluidIngredient ingredient = recipeViewer.recipe.getFluidProducts().get(f);
                int[] stackXYWH = fluidOutputStackXYWH.get(f++);
                SlotWidget slot = widgets.add(new TankWithAmount(output, stackXYWH[0] - backgroundX - 1, stackXYWH[1] - backgroundY - 1, stackXYWH[2] + 2, stackXYWH[3] + 2)).drawBack(false).recipeContext(this);
                if (ingredient.chancePercent() != 100) {
                    slot.appendTooltip(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.amount(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2)));
                }
            } else {
                SizedChanceItemIngredient ingredient = recipeViewer.recipe.getItemProducts().get(i);
                int[] stackXY = itemOutputStackXY.get(i++);
                SlotWidget slot = widgets.addSlot(output, stackXY[0] - backgroundX - 1, stackXY[1] - backgroundY - 1).drawBack(false).recipeContext(this);
                if (ingredient.chancePercent() != 100) {
                    slot.appendTooltip(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.count(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2)));
                }
            }
        }

        widgets.addAnimatedTexture(texture, categoryInfo.getProgressBarGuiX() - categoryInfo.getRecipeViewerBackgroundX(), categoryInfo.getProgressBarGuiY() - categoryInfo.getRecipeViewerBackgroundY(), categoryInfo.getProgressBarGuiW(), categoryInfo.getProgressBarGuiH(), categoryInfo.getProgressBarGuiU(), categoryInfo.getProgressBarGuiV(), getProgressTime(), true, false, false).tooltipText(this::progressTooltips);
    }

    public List<Component> progressTooltips(int x, int y) {
        return recipeViewer.progressTooltips(x, y);
    }

    protected int getProgressTime() {
        return Mth.clamp(recipeViewer.getProgressArrowTime() * 50, 500, 10000);
    }
}