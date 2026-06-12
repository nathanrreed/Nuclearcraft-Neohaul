package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerInfo;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.COLLECTOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.COBBLE_GENERATOR_RECIPE_TYPE;

public class JeiCollectorCategory implements IRecipeCategory<RecipeHolder<CollectorRecipe>> {
    private static final ResourceLocation UID = ncLoc("collector");
    private static final RecipeType<RecipeHolder<CollectorRecipe>> TYPE = RecipeType.createFromVanilla(COBBLE_GENERATOR_RECIPE_TYPE.get());

    private final IGuiHelper helper;
    private RecipeViewerInfo recipeViewerInfo;
    private static final Font font = Minecraft.getInstance().font;

    public JeiCollectorCategory(IGuiHelper helper) {
        this.helper = helper;
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get("collector_fluid");
    }

    @Override
    public RecipeType<RecipeHolder<CollectorRecipe>> getRecipeType() {
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
        return Component.translatable("emi.category." + MODID + ".collector");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(COLLECTOR_MAP.get("cobblestone_generator").get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CollectorRecipe> holder, IFocusGroup focuses) {
        final CollectorRecipe recipe = holder.value();
        ScreenPosition position = recipeViewerInfo.item_inputs().getFirst();

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY).setPosition(position.x() + 1, position.y() + 1).addItemStack(recipe.getToastSymbol());

        if (!recipe.getItemProducts().isEmpty()) {
            this.recipeViewerInfo = RECIPE_VIEWER_MAP.get("collector_item");
            position = recipeViewerInfo.item_outputs().getFirst();
            builder.addOutputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.getItemProduct().ingredient());
        } else {
            this.recipeViewerInfo = RECIPE_VIEWER_MAP.get("collector_fluid");
            position = recipeViewerInfo.fluid_outputs().getFirst();
            builder.addOutputSlot(position.x() + 1, position.y() + 1).addFluidStack(recipe.getFluidProduct().getStack().getFluid(), recipe.getFluidProduct().amount()).setFluidRenderer(recipe.getFluidProduct().amount(), false, 24, 24);
        }
    }

    @Override
    public void draw(RecipeHolder<CollectorRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        final CollectorRecipe recipe = holder.value();
        guiGraphics.blit(recipeViewerInfo.background(), 0, 0, recipeViewerInfo.rect().left(), recipeViewerInfo.rect().top(), recipeViewerInfo.rect().width(), recipeViewerInfo.rect().height());
        guiGraphics.blit(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 176, 3, (int) (((double) System.currentTimeMillis() / 75) % 37), 38);

        boolean isItem = !recipe.getItemProducts().isEmpty();

        if (isItem) {
            ScreenPosition position = recipeViewerInfo.item_inputs().getFirst();
            guiGraphics.renderItemDecorations(font, recipe.getItemProduct().getStack(), position.x() + 1, position.y() + 1);
        }

        if (new ScreenRectangle(recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2).containsPoint((int) mouseX, (int) mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, List.of(Component.translatable(MODID + ".tooltip.production_rate", recipe.getCollectorProductionRate() + (isItem ? " C" : " mB"))), Optional.empty(), (int) mouseX, (int) mouseY);
        }
    }
}
