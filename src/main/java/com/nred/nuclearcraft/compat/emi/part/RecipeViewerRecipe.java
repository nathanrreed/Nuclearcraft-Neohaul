package com.nred.nuclearcraft.compat.emi.part;

import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;

public abstract class RecipeViewerRecipe extends BasicEmiRecipe {
    private final RecipeViewerInfo recipeViewerInfo;

    public RecipeViewerRecipe(EmiRecipeCategory category, ResourceLocation id, int width, int height) {
        super(category, id, width, height);
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(category.getId().getPath());
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

        for (int i = 0; i < inputs.size(); i++) {
            ScreenPosition position = recipeViewerInfo.inputs().get(i);
            widgets.addSlot(inputs.get(i), position.x(), position.y()).drawBack(false);
        }

        for (int i = 0; i < outputs.size(); i++) {
            ScreenPosition position = recipeViewerInfo.outputs().get(i);
            if (outputs.get(i).getKey() instanceof Fluid) {
                widgets.add(new TankWithAmount(outputs.get(i), position.x(), position.y(), 26, 26)).drawBack(false).recipeContext(this);
            } else {
                widgets.addSlot(outputs.get(i), position.x(), position.y()).drawBack(false).recipeContext(this);
            }
        }

        widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2, 176, 3, getProgressTime(), true, false, false).tooltipText(this::progressTooltips);
    }

    public abstract List<Component> progressTooltips(int x, int y);

    protected int getProgressTime() {
        return 1200;
    }
}