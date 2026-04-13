package com.nred.nuclearcraft.compat.jei;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class JeiBasicInfoCategory implements IRecipeCategory<IJeiBasicInfoRecipe> {
    protected static final Font font = Minecraft.getInstance().font;
    private final String name;
    private final ItemLike icon;
    private final IGuiHelper helper;
    public final RecipeType<IJeiBasicInfoRecipe> type;

    public JeiBasicInfoCategory(IGuiHelper helper, String name, ItemLike icon) {
        this.helper = helper;
        this.name = name;
        this.icon = icon;
        this.type = new RecipeType<>(ncLoc(name), IJeiBasicInfoRecipe.class);
    }

    @Override
    public RecipeType<IJeiBasicInfoRecipe> getRecipeType() {
        return type;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableItemLike(icon);
    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 18;
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiBasicInfoRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder inputSlotBuilder = builder.addInputSlot(1, 1);
        inputSlotBuilder.setBackground(helper.drawableBuilder(ncLoc("textures/gui/sprites/container/" + name + ".png"), 0, 0, 18, 18).setTextureSize(18, 18).build(), -1, -1);

        if (recipe.getTooltip() != null) {
            inputSlotBuilder.addRichTooltipCallback((slot, tooltipBuilder) -> tooltipBuilder.add(recipe.getTooltip()));
        }

        IIngredientAcceptor<?> outputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);

        ITypedIngredient<?> typedIngredient = recipe.getIngredient();
        inputSlotBuilder.addTypedIngredient(typedIngredient);
        outputSlotBuilder.addTypedIngredient(typedIngredient);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("emi.category." + MODID + "." + name);
    }
}