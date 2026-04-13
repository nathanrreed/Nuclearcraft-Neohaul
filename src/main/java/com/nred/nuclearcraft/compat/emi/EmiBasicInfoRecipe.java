package com.nred.nuclearcraft.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public record EmiBasicInfoRecipe(List<? extends EmiIngredient> stacks, EmiRecipeCategory category, ResourceLocation id, Supplier<ClientTooltipComponent> tooltip) implements EmiRecipe { // See dev.emi.emi.api.recipe.EmiInfoRecipe
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
        return (List<EmiIngredient>) stacks;
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
        return (Math.min(stacks.size(), STACK_WIDTH)) * 18; // Was 144
    }

    @Override
    public int getDisplayHeight() {
        return ((Math.min(stacks.size(), MAX_STACKS) - 1) / STACK_WIDTH + 1) * 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int stackCount = Math.min(stacks.size(), MAX_STACKS);
        for (int i = 0; i < stackCount; i++) {
            int y = i / STACK_WIDTH * 18;
            int x = (i % STACK_WIDTH) * 18;
            if (i + 1 == stackCount && stacks.size() > stackCount) {
                widgets.addSlot(EmiIngredient.of(stacks.subList(i, stacks.size())), x, y);
            } else {
                widgets.add(new SlotWidget(stacks.get(i), x, y) {

                    @Override
                    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
                        List<ClientTooltipComponent> list = super.getTooltip(mouseX, mouseY);
                        if (tooltip != null) {
                            list.add(1, tooltip.get());
                        }
                        return list;
                    }

                    @Override
                    public void drawBackground(GuiGraphics draw, int mouseX, int mouseY, float delta) {
                        Bounds bounds = getBounds();
                        int width = bounds.width();
                        int height = bounds.height();
                        draw.blitSprite(textureId, bounds.x(), bounds.y(), width, height);
                    }
                }.backgroundTexture(category.getId().withPrefix("container/"), 0, 0));
            }
        }
    }
}