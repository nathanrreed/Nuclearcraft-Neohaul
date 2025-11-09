package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.List;

import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_COLLECTOR_CATEGORY;

public class EmiCollectorRecipe extends BasicEmiRecipe {
    private final RecipeViewerInfo recipeViewerInfo;
    private final String rate;
    private final boolean isItem;

    public EmiCollectorRecipe(ResourceLocation id, ItemLike catalyst, List<EmiIngredient> itemResults, List<EmiIngredient> fluidResults, String rate) {
        super(EMI_COLLECTOR_CATEGORY, id, 0, 0);
        this.rate = rate;
        this.isItem = !itemResults.isEmpty();

        this.outputs.addAll(itemResults.stream().map(EmiIngredient::getEmiStacks).flatMap(Collection::stream).toList());

        this.outputs.addAll(fluidResults.stream().map(EmiIngredient::getEmiStacks).flatMap(Collection::stream).toList());

        this.catalysts.add(EmiIngredient.of(Ingredient.of(catalyst)));
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(isItem ? "collector_item" : "collector_fluid");
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

        for (int i = 0; i < catalysts.size(); i++) {
            ScreenPosition position = recipeViewerInfo.item_inputs().get(i);
            widgets.addSlot(catalysts.get(i), position.x(), position.y()).drawBack(false);
        }

        int i = 0, f = 0;
        for (EmiStack output : outputs) {
            if (output.getKey() instanceof Fluid) {
                ScreenPosition position = recipeViewerInfo.fluid_outputs().get(f++);
                widgets.addTank(output, position.x(), position.y(), 26, 26, (int) output.getAmount()).drawBack(false).recipeContext(this);
            } else {
                ScreenPosition position = recipeViewerInfo.item_outputs().get(i++);
                widgets.addSlot(output, position.x(), position.y()).drawBack(false).recipeContext(this);
            }
        }

        widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2, 176, 4, 600, true, false, false).tooltipText(List.of(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.production_rate", Component.literal(rate).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN)));
    }
}