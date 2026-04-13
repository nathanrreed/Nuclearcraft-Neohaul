package com.nred.nuclearcraft.compat.emi.part;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerInfo;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.RecipeViewer;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;

public abstract class RecipeViewerRecipe extends BasicEmiRecipe {
    private final RecipeViewerInfo recipeViewerInfo;
    private final RecipeViewer<?> recipeViewer;

    public RecipeViewerRecipe(EmiRecipeCategory category, ResourceLocation id, RecipeViewer<?> recipeViewer) {
        super(category, id, 0, 0);
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(category.getId().getPath());
        this.recipeViewer = recipeViewer;
    }

    @Override
    public int getDisplayWidth() {
        return recipeViewerInfo.rect().width();
    }

    @Override
    public int getDisplayHeight() {
        return recipeViewerInfo.rect().height();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(recipeViewerInfo.background(), 0, 0, recipeViewerInfo.rect().width(), recipeViewerInfo.rect().height(), recipeViewerInfo.rect().left(), recipeViewerInfo.rect().top());

        int i = 0, f = 0;
        for (EmiIngredient input : inputs) {
            if (input.getEmiStacks().getFirst().getKey() instanceof Fluid) {
                ScreenPosition position = recipeViewerInfo.fluid_inputs().get(f++);
                widgets.add(new TankWithAmount(input, position.x(), position.y())).drawBack(false);
            } else {
                ScreenPosition position = recipeViewerInfo.item_inputs().get(i++);
                widgets.addSlot(input, position.x(), position.y()).drawBack(false);
            }
        }

        i = 0;
        f = 0;
        int size = recipeViewerInfo.fluid_outputs().size() < 3 ? 26 : 18;
        for (EmiStack output : outputs) {
            if (output.getKey() instanceof Fluid) {
                SizedChanceFluidIngredient ingredient = recipeViewer.recipe.getFluidProducts().get(f);
                ScreenPosition position = recipeViewerInfo.fluid_outputs().get(f++);
                SlotWidget slot = widgets.add(new TankWithAmount(output, position.x(), position.y(), size, size)).drawBack(false).recipeContext(this);
                if (ingredient.chancePercent() != 100) {
                    slot.appendTooltip(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.amount(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2)));
                }
            } else {
                SizedChanceItemIngredient ingredient = recipeViewer.recipe.getItemProducts().get(i);
                ScreenPosition position = recipeViewerInfo.item_outputs().get(i++);
                SlotWidget slot = widgets.addSlot(output, position.x(), position.y()).drawBack(false).recipeContext(this);
                if (ingredient.chancePercent() != 100) {
                    slot.appendTooltip(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.count(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2)));
                }
            }
        }

        if (recipeViewer.recipe instanceof HeatExchangerRecipe hxRecipe && !hxRecipe.getHeatExchangerIsHeating()) {
            widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, 16, 176, 19, getProgressTime(), true, false, false).tooltipText(this::progressTooltips);
        } else {
            widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2, 176, 3, getProgressTime(), true, false, false).tooltipText(this::progressTooltips);
        }
    }

    public List<Component> progressTooltips(int x, int y) {
        return recipeViewer.progressTooltips(x, y);
    }

    protected int getProgressTime() {
        return recipeViewer.getProgressTime();
    }
}