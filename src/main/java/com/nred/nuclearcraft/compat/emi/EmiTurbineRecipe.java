package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_TURBINE_CATEGORY;

public class EmiTurbineRecipe extends BasicEmiRecipe {
    private final RecipeViewerInfo recipeViewerInfo;
    private final double expansion_level;
    private final double power_per_mb;
    private final double spin_up_multiplier;

    public EmiTurbineRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, double power_per_mb, double expansion_level, double spin_up_multiplier) {
        super(EMI_TURBINE_CATEGORY, id, 0, 0);
        this.expansion_level = expansion_level;
        this.power_per_mb = power_per_mb;
        this.spin_up_multiplier = spin_up_multiplier;

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());

        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get("turbine");
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
            widgets.add(new TankWithAmount(inputs.get(i), position.x(), position.y(), 18, 18)).drawBack(false);
        }

        for (int i = 0; i < outputs.size(); i++) {
            ScreenPosition position = recipeViewerInfo.outputs().get(i);
            widgets.add(new TankWithAmount(outputs.get(i), position.x(), position.y(), 26, 26)).drawBack(false).recipeContext(this);
        }

        widgets.addAnimatedTexture(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2, 176, 4, 600, true, false, false).tooltipText((x, y) -> {
            ArrayList<Component> list = new ArrayList<>(2);
            list.addAll(List.of(
                    Component.translatable(MODID + ".recipe_viewer.turbine_energy_density", Component.literal(NCMath.decimalPlaces(power_per_mb, 2) + " RF/mB").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE),
                    Component.translatable(MODID + ".recipe_viewer.turbine_expansion", Component.literal(NCMath.pcDecimalPlaces(expansion_level, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY)));
            if (spin_up_multiplier != 1.0)
                list.add(Component.translatable(MODID + ".recipe_viewer.turbine_spin_up_multiplier", Component.literal(NCMath.pcDecimalPlaces(spin_up_multiplier, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
            return list;
        });
    }

    private class TankWithAmount extends TankWidget {
        public TankWithAmount(EmiIngredient stack, int x, int y, int w, int h) {
            super(stack, x, y, w, h, stack.getAmount());
        }

        @Override
        protected void addSlotTooltip(List<ClientTooltipComponent> list) {
            if (stack.getAmount() == 1) {
                list.add(EmiTooltipComponents.getAmount(stack));
            }
            super.addSlotTooltip(list);
        }
    }
}