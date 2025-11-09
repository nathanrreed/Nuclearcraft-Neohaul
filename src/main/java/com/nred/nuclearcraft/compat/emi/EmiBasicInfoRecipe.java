package com.nred.nuclearcraft.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public record EmiBasicInfoRecipe(List<EmiIngredient> stacks, EmiRecipeCategory category, ResourceLocation id, Supplier<ClientTooltipComponent> tooltip) implements EmiRecipe {
    public EmiBasicInfoRecipe(List<EmiIngredient> stacks, EmiRecipeCategory category, ResourceLocation id) {
        this(stacks, category, id, null);
    }

    private static final int STACK_WIDTH = 6;
    private static final int MAX_STACKS = STACK_WIDTH * 3;

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return stacks;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public int getDisplayWidth() {
        return 144;
    }

    @Override
    public int getDisplayHeight() {
        return ((Math.min(stacks.size(), MAX_STACKS) - 1) / STACK_WIDTH + 1) * 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int stackCount = Math.min(stacks.size(), MAX_STACKS);
        int stackHeight = ((stackCount - 1) / STACK_WIDTH + 1);
        int xOff = 54 - (stackCount % STACK_WIDTH * 9);
        for (int i = 0; i < stackCount; i++) {
            int y = i / STACK_WIDTH * 18;
            int x = (i % STACK_WIDTH) * 18;
            if (y / 18 + 1 == stackHeight && stackCount % STACK_WIDTH != 0) {
                x += xOff;
            }
            if (i + 1 == stackCount && stacks.size() > stackCount) {
                widgets.addSlot(EmiIngredient.of(stacks.subList(i, stacks.size())), x + 18, y);
            } else {
                SlotWidget slot = widgets.addSlot(stacks.get(i), x + 18, y);
                if (tooltip != null) {
                    slot.appendTooltip(tooltip);
                }
            }
        }
    }
}