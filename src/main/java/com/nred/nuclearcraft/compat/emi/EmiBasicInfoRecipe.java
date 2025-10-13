package com.nred.nuclearcraft.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record EmiBasicInfoRecipe(List<EmiIngredient> stacks, EmiRecipeCategory category, ResourceLocation id) implements EmiRecipe {
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
        return stacks.stream().flatMap(ing -> ing.getEmiStacks().stream()).toList();
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
                widgets.addSlot(stacks.get(i), x + 18, y);
            }
        }
    }
}