package com.nred.nuclearcraft.compat.emi.part;

import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;

public abstract class RecipeViewerRecipe extends BasicEmiRecipe {
    private final RecipeViewerInfo recipeViewerInfo;

    public RecipeViewerRecipe(EmiRecipeCategory category, ResourceLocation id) {
        super(category, id, 0, 0);
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
                ScreenPosition position = recipeViewerInfo.fluid_outputs().get(f++);
                widgets.add(new TankWithAmount(output, position.x(), position.y(), size, size)).drawBack(false).recipeContext(this);
            } else {
                ScreenPosition position = recipeViewerInfo.item_outputs().get(i++);
                widgets.addSlot(output, position.x(), position.y()).drawBack(false).recipeContext(this);
            }
        }

        widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2, 176, 3, getProgressTime(), true, false, false).tooltipText(this::progressTooltips);
    }

    public abstract List<Component> progressTooltips(int x, int y);

    protected int getProgressTime() {
        return 1200;
    }
}