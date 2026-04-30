package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerInfo;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.util.NCMath;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
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

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
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
        for (int i = 0; i < recipe.itemIngredients.size(); i++) {
            ScreenPosition position = recipeViewerInfo.item_inputs().get(i);
            builder.addInputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemIngredients.get(i).ingredient());
        }

        for (int i = 0; i < recipe.fluidIngredients.size(); i++) {
            ScreenPosition position = recipeViewerInfo.fluid_inputs().get(i);
            builder.addInputSlot(position.x() + 1, position.y() + 1).addFluidStack(recipe.getFluidIngredients().get(i).getFluids()[0].getFluid(), recipe.getFluidIngredients().get(i).amount()).setFluidRenderer(1, false, 16, 16);
        }

        for (int i = 0; i < recipe.itemProducts.size(); i++) {
            ScreenPosition position = recipeViewerInfo.item_outputs().get(i);
            IRecipeSlotBuilder temp = builder.addOutputSlot(position.x() + 1, position.y() + 1).addIngredients(recipe.itemProducts.get(i).ingredient());
            SizedChanceItemIngredient ingredient = recipe.getItemProducts().get(i);
            temp.setSlotName("" + i).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable(MODID + ".recipe_viewer.chance_output", ingredient.minStackSize(), ingredient.count(), NCMath.decimalPlaces(ingredient.getMeanStackSize(), 2))));
        }

        int size = recipeViewerInfo.fluid_outputs().size() < 3 ? 24 : 16;
        for (int i = 0; i < recipe.fluidProducts.size(); i++) {
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
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(recipeViewerInfo.background(), 0, 0, recipeViewerInfo.rect().left(), recipeViewerInfo.rect().top(), recipeViewerInfo.rect().width(), recipeViewerInfo.rect().height());
        guiGraphics.blit(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 176, 3, (int) (((double) System.currentTimeMillis() / 100 / recipe.getProcessTimeMultiplier()) % 37), 38);

        if (new ScreenRectangle(recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2).containsPoint((int) mouseX, (int) mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, List.of(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.process_time", Component.literal(getTimeString(recipe.getBaseProcessTime(info.getDefaultProcessTime()))).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN), Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.process_power", Component.literal(getFEString(recipe.getBaseProcessPower(info.getDefaultProcessPower()), true)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE)), Optional.empty(), (int) mouseX, (int) mouseY);
        }
    }
}