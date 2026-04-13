package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerInfo;
import com.nred.nuclearcraft.compat.emi.part.TankWithAmount;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.handler.TileInfoHandler.TILE_CONTAINER_INFO_MAP;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.removeBarriers;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getTimeString;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

public class EmiProcessorRecipe extends BasicEmiRecipe {
    private final RecipeViewerInfo recipeViewerInfo;
    private final double time;
    private final double power;
    private final ProcessorRecipe recipe;

    public EmiProcessorRecipe(String type, EmiRecipeCategory category, ResourceLocation id, List<EmiIngredient> itemInputs, List<EmiIngredient> itemResults, List<EmiIngredient> fluidInputs, List<EmiIngredient> fluidResults, ProcessorRecipe recipe) {
        super(category, id, 0, 0);
        this.recipe = recipe;
        this.inputs.addAll(removeBarriers(itemInputs));
        this.inputs.addAll(fluidInputs);
        this.outputs.addAll(itemResults.stream().map(EmiIngredient::getEmiStacks).flatMap(Collection::stream).toList());
        this.outputs.addAll(fluidResults.stream().map(EmiIngredient::getEmiStacks).flatMap(Collection::stream).toList());

        this.catalysts.add(EmiIngredient.of(Ingredient.of(PROCESSOR_MAP.get(type))));
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(type);
        BasicUpgradableProcessorMenuInfo<?, ?> info = (BasicUpgradableProcessorMenuInfo<?, ?>) TILE_CONTAINER_INFO_MAP.get(type);
        this.time = info.getDefaultProcessTime() * recipe.getProcessTimeMultiplier();
        this.power = info.getDefaultProcessPower() * recipe.getProcessPowerMultiplier();
    }

    public EmiProcessorRecipe(String type, EmiRecipeCategory category, ResourceLocation id, List<EmiIngredient> itemInputs, List<EmiIngredient> itemResults, double timeModifier, double powerModifier) {
        super(category, id, 0, 0);
        this.recipe = null;
        this.inputs.addAll(removeBarriers(itemInputs));
        this.outputs.addAll(itemResults.stream().map(EmiIngredient::getEmiStacks).flatMap(Collection::stream).toList());

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
                SizedChanceFluidIngredient ingredient = recipe != null ? recipe.getFluidProducts().get(f) : SizedChanceFluidIngredient.EMPTY;
                ScreenPosition position = recipeViewerInfo.fluid_outputs().get(f++);
                SlotWidget slot = widgets.add(new TankWithAmount(output, position.x(), position.y(), size, size)).drawBack(false).recipeContext(this);
                if (ingredient.chancePercent() != 100) {
                    slot.appendTooltip(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.amount(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2)));
                }
            } else {
                SizedChanceItemIngredient ingredient = recipe != null ? recipe.getItemProducts().get(i) : SizedChanceItemIngredient.EMPTY;
                ScreenPosition position = recipeViewerInfo.item_outputs().get(i++);
                SlotWidget slot = widgets.addSlot(output, position.x(), position.y()).drawBack(false).recipeContext(this);
                if (ingredient.chancePercent() != 100) {
                    slot.appendTooltip(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.count(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2)));
                }
            }
        }

        widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2, 176, 3, (int) (time * 10.0), true, false, false).tooltipText(List.of(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.process_time", Component.literal(getTimeString(time)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN), Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.process_power", Component.literal(getFEString(power, true)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE)));
    }
}