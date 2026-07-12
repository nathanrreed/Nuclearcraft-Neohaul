package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.RecipeViewer;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.util.NCMath;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class JeiRecipeViewerCategory<RECIPE extends BasicRecipe> extends JeiBasicCategory<RecipeHolder<RECIPE>> {
    private final Class<? extends RecipeViewer<RECIPE>> clazz;
    private final RecipeType<RecipeHolder<RECIPE>> recipeType;
    protected final int arrowX, arrowY;
    private boolean hasArrow = false;
    private ITickTimer timer = null;

    public JeiRecipeViewerCategory(IGuiHelper helper, String name, Class<? extends RecipeViewer<RECIPE>> clazz, net.minecraft.world.item.crafting.RecipeType<RECIPE> recipeType) {
        super(helper, name);
        this.clazz = clazz;
        this.recipeType = RecipeType.createFromVanilla(recipeType);

        if (categoryInfo.getProgressBarGuiW() > 0 && categoryInfo.getProgressBarGuiH() > 0) {
            hasArrow = true;
        }

        this.arrowX = categoryInfo.getProgressBarGuiX() - categoryInfo.getRecipeViewerBackgroundX();
        this.arrowY = categoryInfo.getProgressBarGuiY() - categoryInfo.getRecipeViewerBackgroundY();
    }

    protected <T> T getEnumerationElement(List<T> list, long millisPerElement) {
        return list.get((int) ((System.currentTimeMillis() / millisPerElement) % list.size()));
    }

    @Override
    public RecipeType<RecipeHolder<RECIPE>> getRecipeType() {
        return recipeType;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(categoryInfo.crafters.getFirst()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RECIPE> holder, IFocusGroup focuses) {
        final RECIPE recipe = holder.value();

        int backgroundX = categoryInfo.getRecipeViewerBackgroundX();
        int backgroundY = categoryInfo.getRecipeViewerBackgroundY();

        List<int[]> itemInputStackXY = categoryInfo.getItemInputStackXY();
        for (int i = 0; i < recipe.itemIngredients.size(); i++) {
            int[] stackXY = itemInputStackXY.get(i);
            builder.addInputSlot(stackXY[0] - backgroundX, stackXY[1] - backgroundY).addItemStacks(Arrays.stream(recipe.itemIngredients.get(i).getItemsRaw()).toList());
        }

        List<int[]> fluidInputStackXYWH = categoryInfo.getFluidInputGuiXYWH();
        for (int i = 0; i < recipe.fluidIngredients.size(); i++) {
            if (recipe.getFluidIngredients().get(i).isEmpty()) continue; // Required for radiation scrubber
            int[] stackXYWH = fluidInputStackXYWH.get(i);
            builder.addInputSlot(stackXYWH[0] - backgroundX, stackXYWH[1] - backgroundY).addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.fluidIngredients.get(i).getFluidsRaw())).setFluidRenderer(1, false, stackXYWH[2], stackXYWH[3]);
        }

        List<int[]> itemOutputStackXY = categoryInfo.getItemOutputStackXY();
        if (recipe.isSpecial()) {
            int[] stackXY = itemOutputStackXY.getFirst();
            builder.addOutputSlot(stackXY[0] - backgroundX, stackXY[1] - backgroundY).addItemStack(recipe.getResultItem(null));
        } else {
            for (int i = 0; i < recipe.itemProducts.size(); i++) {
                int[] stackXY = itemOutputStackXY.get(i);
                SizedChanceItemIngredient ingredient = recipe.getItemProducts().get(i);
                IRecipeSlotBuilder temp = builder.addOutputSlot(stackXY[0] - backgroundX, stackXY[1] - backgroundY).addItemStacks(Arrays.asList(ingredient.getItemsRaw()));
                if (ingredient.chancePercent() < 100) {
                    temp.setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.count(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2))));
                }
            }
        }

        List<int[]> fluidOutputStackXYWH = categoryInfo.getFluidOutputGuiXYWH();
        for (int i = 0; i < recipe.fluidProducts.size(); i++) {
            if (recipe.getFluidProducts().get(i).isEmpty()) continue; // Required for radiation scrubber
            int[] stackXYWH = fluidOutputStackXYWH.get(i);
            SizedChanceFluidIngredient ingredient = recipe.getFluidProducts().get(i);
            IRecipeSlotBuilder temp = builder.addOutputSlot(stackXYWH[0] - backgroundX, stackXYWH[1] - backgroundY).addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(ingredient.getFluidsRaw())).setFluidRenderer(1, false, stackXYWH[2], stackXYWH[3]);
            if (ingredient.chancePercent() < 100) {
                temp.setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.amount(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2))));
            }
        }
    }

    @Override
    public void draw(RecipeHolder<RECIPE> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        final RECIPE recipe = holder.value();

        ResourceLocation texture = ResourceLocation.parse(categoryInfo.getRecipeViewerTexture());
        guiGraphics.blit(texture, 0, 0, categoryInfo.getRecipeViewerBackgroundX(), categoryInfo.getRecipeViewerBackgroundY(), categoryInfo.getRecipeViewerBackgroundW(), categoryInfo.getRecipeViewerBackgroundH());

        RecipeViewer<RECIPE> recipeViewer;
        try {
            try {
                recipeViewer = clazz.getConstructor(recipe.getClass()).newInstance(recipe);
            } catch (Exception ignored) {
                recipeViewer = clazz.getConstructor(recipe.getClass(), String.class).newInstance(recipe, name); // Custom Processors
            }
            if (categoryInfo.getProgressBarGuiW() > 0 && categoryInfo.getProgressBarGuiH() > 0) {
                if (hasArrow) {
                    int[] progressUVWH = recipeViewer.getProgressArrowUVWH(categoryInfo.getProgressBarGuiU(), categoryInfo.getProgressBarGuiV(), categoryInfo.getProgressBarGuiW(), categoryInfo.getProgressBarGuiH());
                    if (timer == null) {
                        timer = helper.createTickTimer(Math.max(progressUVWH[2], recipeViewer.getProgressArrowTime() / 6), progressUVWH[2], false);
                    }
                    guiGraphics.blit(texture, arrowX, arrowY, progressUVWH[0], progressUVWH[1], timer.getValue(), progressUVWH[3]);

                    if (new ScreenRectangle(arrowX, arrowY, categoryInfo.getRecipeViewerTooltipW(), categoryInfo.getRecipeViewerTooltipH()).containsPoint((int) mouseX, (int) mouseY)) {
                        guiGraphics.renderTooltip(font, recipeViewer.progressTooltips((int) mouseX, (int) mouseY), Optional.empty(), (int) mouseX, (int) mouseY);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
