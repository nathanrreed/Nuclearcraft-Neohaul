package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.processor.CentrifugeRecipe;
import com.nred.nuclearcraft.recipe.processor.FuelReprocessorRecipe;
import com.nred.nuclearcraft.recipe.processor.RockCrusherRecipe;
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
import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.probabilityUnpacker;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getTimeString;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

public class JeiProcessorCategory<T extends ProcessorRecipe> implements IRecipeCategory<T> {
    private final IGuiHelper helper;
    private final String type;
    private final RecipeType<T> TYPE;
    private final ResourceLocation UID;
    private final RecipeViewerInfo recipeViewerInfo;
    private static final Font font = Minecraft.getInstance().font;

    public JeiProcessorCategory(IGuiHelper helper, String type, Class<T> clazz) {
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
        boolean large = recipeViewerInfo.outputs().size() < 3;
        for (int i = 0; i < recipe.itemInputs.size(); i++) {
            ScreenPosition position = recipeViewerInfo.inputs().get(i);
            builder.addInputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemInputs.get(i).ingredient());
        }

        for (int i = 0; i < recipe.fluidInputs.size(); i++) {
            ScreenPosition position = recipeViewerInfo.inputs().get(i + recipe.itemInputs.size());
            builder.addInputSlot(position.x() + 1, position.y() + 1).addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidInputs.get(i).ingredient().getStacks()).toList());
        }

        for (int i = 0; i < recipe.itemResults.size(); i++) {
            ScreenPosition position = recipeViewerInfo.outputs().get(i);
            IRecipeSlotBuilder temp = builder.addOutputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemResults.get(i).ingredient());
            if (recipe instanceof RockCrusherRecipe) {
                temp.setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("jei.probability", probabilityUnpacker(recipe.itemResults.get(Integer.parseInt(recipeSlotView.getSlotName().get())).count()).first).withStyle(ChatFormatting.GOLD)));
            } else if (recipe instanceof FuelReprocessorRecipe && (i == 2 || i == 6)) {
                temp.setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("jei.probability", probabilityUnpacker(recipe.itemResults.get(Integer.parseInt(recipeSlotView.getSlotName().get())).count()).first).withStyle(ChatFormatting.GOLD)));
            }
        }

        for (int i = 0; i < recipe.fluidResults.size(); i++) {
            ScreenPosition position = recipeViewerInfo.outputs().get(i + recipe.itemResults.size());
            if (recipe instanceof CentrifugeRecipe && (i == 2 || i == 5)) {
                int amount = probabilityUnpacker(recipe.fluidResults.get(i).getFluids()[0].getAmount()).second;
                builder.addOutputSlot(position.x() + (large ? 1 : 0), position.y() + 1).addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidResults.get(i).ingredient().getStacks()).map(stack -> stack.copyWithAmount(amount)).toList()).setFluidRenderer(amount, false, large ? 24 : 16, large ? 24 : 16)
                        .setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("jei.probability", probabilityUnpacker(recipe.fluidResults.get(Integer.parseInt(recipeSlotView.getSlotName().get())).amount()).first).withStyle(ChatFormatting.GOLD)));
            } else {
                builder.addOutputSlot(position.x() + (large ? 1 : 0), position.y() + 1).addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidResults.get(i).ingredient().getStacks()).toList()).setFluidRenderer(recipe.fluidResults.get(i).amount(), false, large ? 24 : 16, large ? 24 : 16);
            }
        }
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(recipeViewerInfo.background(), 0, 0, recipeViewerInfo.rect().left(), recipeViewerInfo.rect().top(), recipeViewerInfo.rect().width(), recipeViewerInfo.rect().height());
        guiGraphics.blit(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 176, 3, (int) (((double) System.currentTimeMillis() / 100 / recipe.getTimeModifier()) % 37), 38);
        for (int i = 0; i < recipe.itemInputs.size(); i++) {
            ScreenPosition position = recipeViewerInfo.inputs().get(i);
            guiGraphics.renderItemDecorations(font, recipe.itemInputs.get(i).getItems()[0], position.x() + 1, position.y() + 1);
        }

        for (int i = 0; i < recipe.itemResults.size(); i++) {
            ScreenPosition position = recipeViewerInfo.outputs().get(i);
            if (recipe instanceof RockCrusherRecipe) {
                guiGraphics.renderItemDecorations(font, recipe.itemResults.get(i).getItems()[0].copyWithCount(probabilityUnpacker(recipe.itemResults.get(i).getItems()[0].getCount()).second), position.x() + 1, position.y() + 1);
            } else {
                guiGraphics.renderItemDecorations(font, recipe.itemResults.get(i).getItems()[0], position.x() + 1, position.y() + 1);
            }
        }

        if (new ScreenRectangle(recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2).containsPoint((int) mouseX, (int) mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, List.of(Component.translatable("tooltip.process_time", getTimeString(PROCESSOR_CONFIG_MAP.get(type).base_time() * recipe.getTimeModifier())), Component.translatable("tooltip.process_power", getFEString(PROCESSOR_CONFIG_MAP.get(type).base_power() * recipe.getPowerModifier(), true))), Optional.empty(), (int) mouseX, (int) mouseY);
        }
    }
}