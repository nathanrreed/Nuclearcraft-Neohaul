package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.RecipeViewer;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import com.nred.nuclearcraft.util.NCMath;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;

import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class JeiRecipeViewerCategory<RECIPE extends BasicRecipe> extends JeiBasicCategory<RECIPE> {
    private final Class<? extends RecipeViewer<RECIPE>> clazz;

    public JeiRecipeViewerCategory(IGuiHelper helper, String name, Class<? extends RecipeViewer<RECIPE>> clazz) {
        super(helper, name);
        this.clazz = clazz;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RECIPE recipe, IFocusGroup focuses) {
        if (recipe instanceof FissionCoolantHeaterRecipe coolantHeaterRecipe) { // Coolant Heaters need to be added separately
            ScreenPosition position = recipeViewerInfo.item_inputs().getFirst();
            builder.addInputSlot(position.x() + 1, position.y() + 1).addItemStack(coolantHeaterRecipe.getHeater());
        } else {
            for (int i = 0; i < recipe.itemIngredients.size(); i++) {
                ScreenPosition position = recipeViewerInfo.item_inputs().get(i);
                builder.addInputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemIngredients.get(i).ingredient());
            }
        }

        for (int i = 0; i < recipe.fluidIngredients.size(); i++) {
            if (recipe.getFluidIngredients().get(i).isEmpty()) continue; // Required for radiation scrubber
            ScreenPosition position = recipeViewerInfo.fluid_inputs().get(i);
            builder.addOutputSlot(position.x() + 1, position.y() + 1).addFluidStack(recipe.getFluidIngredients().get(i).getFluids()[0].getFluid(), recipe.getFluidIngredients().get(i).amount()).setFluidRenderer(recipe.getFluidIngredient().amount(), false, 16, 16);
        }

        for (int i = 0; i < recipe.itemProducts.size(); i++) {
            ScreenPosition position = recipeViewerInfo.item_outputs().get(i);
            IRecipeSlotBuilder temp = builder.addOutputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemProducts.get(i).ingredient());
            SizedChanceItemIngredient ingredient = recipe.getItemProducts().get(i);
            temp.setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.count(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2))));
        }

        int size = recipeViewerInfo.fluid_outputs().size() < 3 ? 24 : 16;
        for (int i = 0; i < recipe.fluidProducts.size(); i++) {
            if (recipe.getFluidProducts().get(i).isEmpty()) continue; // Required for radiation scrubber
            ScreenPosition position = recipeViewerInfo.fluid_outputs().get(i);
            SizedChanceFluidIngredient ingredient = recipe.getFluidProducts().get(i);
            if (ingredient.chancePercent() < 100) {
                builder.addOutputSlot(position.x() + 1, position.y() + 1).addFluidStack(ingredient.getFluids()[0].getFluid(), ingredient.amount()).setFluidRenderer(1, false, size, size)
                        .setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.amount(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2))));
            } else {
                builder.addOutputSlot(position.x() + 1, position.y() + 1).addFluidStack(ingredient.getFluids()[0].getFluid(), ingredient.amount()).setFluidRenderer(1, false, size, size);
            }
        }
    }

    @Override
    public void draw(RECIPE recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(recipeViewerInfo.background(), 0, 0, recipeViewerInfo.rect().left(), recipeViewerInfo.rect().top(), recipeViewerInfo.rect().width(), recipeViewerInfo.rect().height());

        RecipeViewer<RECIPE> recipeViewer;
        try {
            recipeViewer = clazz.getConstructor(recipe.getClass()).newInstance(recipe);
            if (recipe instanceof HeatExchangerRecipe hxRecipe && !hxRecipe.getHeatExchangerIsHeating()) { // Heat exchanger has different progress bar depending on if heating
                guiGraphics.blit(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 176, 19, (int) (((double) (System.currentTimeMillis() / 60) % recipeViewer.getProgressTime()) % 37), 16);
            } else {
                guiGraphics.blit(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 176, 3, (int) (((double) (System.currentTimeMillis() / 60) % recipeViewer.getProgressTime()) % 37), recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2);
            }

            if (new ScreenRectangle(recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2).containsPoint((int) mouseX, (int) mouseY)) {
                guiGraphics.renderTooltip(font, recipeViewer.progressTooltips((int) mouseX, (int) mouseY), Optional.empty(), (int) mouseX, (int) mouseY);
            }
        } catch (Exception ignored) {
        }
    }
}