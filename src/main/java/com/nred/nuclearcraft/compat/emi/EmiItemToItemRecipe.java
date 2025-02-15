package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.List;

import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getTimeString;

public class EmiItemToItemRecipe extends BasicEmiRecipe {
    private final RecipeViewerInfo recipeViewerInfo;
    private final double time;
    private final double power;

    public EmiItemToItemRecipe(String type, EmiRecipeCategory category, ItemLike catalyst, ResourceLocation id, List<EmiIngredient> itemInputs, List<EmiIngredient> itemResults, double timeModifier, double powerModifier) {
        super(category, id, 0, 0);
        this.inputs.addAll(itemInputs);
        this.outputs.addAll(itemResults.stream().map(EmiIngredient::getEmiStacks).flatMap(Collection::stream).toList());
        this.catalysts.add(EmiIngredient.of(Ingredient.of(catalyst)));
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(type);

        this.time = PROCESSOR_CONFIG_MAP.get(type).base_time() * timeModifier;
        this.power = PROCESSOR_CONFIG_MAP.get(type).base_power() * powerModifier;
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

        int i = 0;
        for (ScreenPosition position : recipeViewerInfo.inputs()) {
            widgets.addSlot(inputs.get(i++), position.x(), position.y()).drawBack(false);
        }

        i = 0;
        for (ScreenPosition position : recipeViewerInfo.outputs()) {
            widgets.addSlot(outputs.get(i++), position.x(), position.y()).drawBack(false);
        }

        widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, 38, 176, 3, (int) (time * 10.0), true, false, false).tooltipText(List.of(Component.translatable("tooltip.process_time", getTimeString(time)), Component.translatable("tooltip.process_power", getFEString(power, true))));
    }
}