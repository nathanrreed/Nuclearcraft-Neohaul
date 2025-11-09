package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.handler.TileInfoHandler.TILE_CONTAINER_INFO_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getTimeString;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

public class JeiProcessorCategory<T extends ProcessorRecipe> implements IRecipeCategory<T> {
    private final IGuiHelper helper;
    private final String type;
    private final RecipeType<T> TYPE;
    private final ResourceLocation UID;
    private final RecipeViewerInfo recipeViewerInfo;
    private final BasicUpgradableProcessorMenuInfo<?, ?> info;
    private static final Font font = Minecraft.getInstance().font;


    public JeiProcessorCategory(IGuiHelper helper, String type, Class<T> clazz) {
        this.helper = helper;
        this.type = type;
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get(type);
        this.info = (BasicUpgradableProcessorMenuInfo<?, ?>) TILE_CONTAINER_INFO_MAP.get(type);

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
        boolean large = recipeViewerInfo.item_inputs().size() < 3;
        for (int i = 0; i < recipe.itemIngredients.size(); i++) {
            ScreenPosition position = recipeViewerInfo.item_inputs().get(i);
            builder.addInputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemIngredients.get(i).ingredient());
        }

        for (int i = 0; i < recipe.fluidIngredients.size(); i++) {
            ScreenPosition position = recipeViewerInfo.fluid_inputs().get(i);
            builder.addInputSlot(position.x() + 1, position.y() + 1).addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidIngredients.get(i).ingredient().getStacks()).toList());
        }

        for (int i = 0; i < recipe.itemProducts.size(); i++) {
            ScreenPosition position = recipeViewerInfo.item_outputs().get(i);
            IRecipeSlotBuilder temp = builder.addOutputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemProducts.get(i).ingredient());

//      TODO      temp.setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("jei.probability", probabilityUnpacker(recipe.itemProducts.get(Integer.parseInt(recipeSlotView.getSlotName().get())).count()).first).withStyle(ChatFormatting.GOLD)));
        }

        for (int i = 0; i < recipe.fluidProducts.size(); i++) {
            ScreenPosition position = recipeViewerInfo.fluid_outputs().get(i);
            int x = position.x() + (large ? 1 : 0);
            int amount = recipe.fluidProducts.get(i).amount();
            int chance = recipe.fluidProducts.get(i).chancePercent();
            if (chance < 100) {
                builder.addOutputSlot(x, position.y() + 1).addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidProducts.get(i).ingredient().getStacks()).map(stack -> stack.copyWithAmount(amount)).toList()).setFluidRenderer(amount, false, large ? 24 : 16, large ? 24 : 16)
                        .setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("jei.probability", chance / 100f).withStyle(ChatFormatting.GOLD)));
            } else {
                builder.addOutputSlot(x, position.y() + 1).addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidProducts.get(i).ingredient().getStacks()).toList()).setFluidRenderer(recipe.fluidProducts.get(i).amount(), false, large ? 24 : 16, large ? 24 : 16);
            }
        }
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(recipeViewerInfo.background(), 0, 0, recipeViewerInfo.rect().left(), recipeViewerInfo.rect().top(), recipeViewerInfo.rect().width(), recipeViewerInfo.rect().height());
        guiGraphics.blit(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 176, 3, (int) (((double) System.currentTimeMillis() / 100 / recipe.getProcessTimeMultiplier()) % 37), 38);
        for (int i = 0; i < recipe.itemIngredients.size(); i++) {
            ScreenPosition position = recipeViewerInfo.item_inputs().get(i);
            guiGraphics.renderItemDecorations(font, recipe.itemIngredients.get(i).getItems()[0], position.x() + 1, position.y() + 1);
        }

        for (int i = 0; i < recipe.itemProducts.size(); i++) {
            ScreenPosition position = recipeViewerInfo.item_outputs().get(i);
            guiGraphics.renderItemDecorations(font, recipe.itemProducts.get(i).getItems()[0], position.x() + 1, position.y() + 1);
        }

        if (new ScreenRectangle(recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2).containsPoint((int) mouseX, (int) mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, List.of(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.process_time", getTimeString(recipe.getBaseProcessTime(info.getDefaultProcessTime()))), Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.process_power", getFEString(recipe.getBaseProcessPower(info.getDefaultProcessPower()), true))), Optional.empty(), (int) mouseX, (int) mouseY);
        }
    }
}