package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import com.nred.nuclearcraft.recipe.base_types.ItemToItemRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getTimeString;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

public class JeiItemToItemCategory<T extends ItemToItemRecipe> implements IRecipeCategory<T> {
    private final IGuiHelper helper;
    private final String type;
    private final RecipeType<T> TYPE;
    private final ResourceLocation UID;
    private final RecipeViewerInfo recipeViewerInfo;

    public JeiItemToItemCategory(IGuiHelper helper, String type, Class<T> clazz) {
        this.helper = helper;
        this.type = type;
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(type);

        UID = ncLoc(type);
        TYPE = new RecipeType<>(UID, clazz);
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return TYPE;
    }

    @Override
    public int getWidth() {
        return recipeViewerInfo.rect().width();
    }

    @Override
    public int getHeight() {
        return recipeViewerInfo.rect().height();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("emi.category." + MODID + "." + type);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(PROCESSOR_MAP.get(type).get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        int i = 0;
        for (ScreenPosition position : recipeViewerInfo.inputs()) {
            builder.addInputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemInputs.get(i++).ingredient());
        }

        i = 0;
        for (ScreenPosition position : recipeViewerInfo.outputs()) {
            builder.addOutputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemResults.get(i++).ingredient());
        }
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(recipeViewerInfo.background(), 0, 0, recipeViewerInfo.rect().left(), recipeViewerInfo.rect().top(), recipeViewerInfo.rect().width(), recipeViewerInfo.rect().height());
        guiGraphics.blit(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 176, 3, (int) (((double) System.currentTimeMillis() / 100 / recipe.getTimeModifier()) % 37), 38);
        if (new ScreenRectangle(recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, 38).containsPoint((int) mouseX, (int) mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, List.of(Component.translatable("tooltip.process_time", getTimeString(PROCESSOR_CONFIG_MAP.get(type).base_time() * recipe.getTimeModifier())), Component.translatable("tooltip.process_power", getFEString(PROCESSOR_CONFIG_MAP.get(type).base_power() * recipe.getPowerModifier(), true))), Optional.empty(), (int) mouseX, (int) mouseY);
        }
    }
}