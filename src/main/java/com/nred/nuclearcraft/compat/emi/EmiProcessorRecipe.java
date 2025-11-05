package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.List;

import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.handler.TileInfoHandler.TILE_CONTAINER_INFO_MAP;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.removeBarriers;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getTimeString;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

public class EmiProcessorRecipe extends BasicEmiRecipe {
    private final RecipeViewerInfo recipeViewerInfo;
    private final double time;
    private final double power;

    public EmiProcessorRecipe(String type, EmiRecipeCategory category, ResourceLocation id, List<EmiIngredient> itemInputs, List<EmiIngredient> itemResults, List<EmiIngredient> fluidInputs, List<EmiIngredient> fluidResults, double timeModifier, double powerModifier) {
        super(category, id, 0, 0);
        this.inputs.addAll(removeBarriers(itemInputs));
        this.inputs.addAll(fluidInputs);
        this.outputs.addAll(itemResults.stream().map(EmiIngredient::getEmiStacks).flatMap(Collection::stream).toList());
        this.outputs.addAll(fluidResults.stream().map(EmiIngredient::getEmiStacks).flatMap(Collection::stream).toList());

        this.catalysts.add(EmiIngredient.of(Ingredient.of(PROCESSOR_MAP.get(type))));
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(type);
        BasicUpgradableProcessorMenuInfo<?, ?> info = (BasicUpgradableProcessorMenuInfo<?, ?>) TILE_CONTAINER_INFO_MAP.get(type);
        this.time = info.getDefaultProcessTime() * timeModifier;
        this.power = info.getDefaultProcessPower() * powerModifier;
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

        boolean large = recipeViewerInfo.outputs().size() < 3;
        for (int i = 0; i < outputs.size(); i++) {
            ScreenPosition position = recipeViewerInfo.outputs().get(i);
            if (outputs.get(i).getKey() instanceof Fluid) {
                widgets.addTank(outputs.get(i), position.x(), position.y(), large ? 26 : 18, large ? 26 : 18, (int) outputs.get(i).getAmount()).drawBack(false).recipeContext(this);
            } else {
                widgets.addSlot(outputs.get(i), position.x(), position.y()).drawBack(false).recipeContext(this);
            }
        }

        widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2, 176, 3, (int) (time * 10.0), true, false, false).tooltipText(List.of(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.process_time", getTimeString(time)), Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.process_power", getFEString(power, true))));
    }
}