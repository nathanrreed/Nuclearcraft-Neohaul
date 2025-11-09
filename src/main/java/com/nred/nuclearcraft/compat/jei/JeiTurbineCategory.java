package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.compat.common.RecipeViewerInfo;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import com.nred.nuclearcraft.util.NCMath;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.common.RecipeViewerInfoMap.RECIPE_VIEWER_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.TURBINE_MAP;

public class JeiTurbineCategory implements IRecipeCategory<TurbineRecipe> {
    private static final ResourceLocation UID = ncLoc("multiblock");
    private static final RecipeType<TurbineRecipe> TYPE = new RecipeType<>(UID, TurbineRecipe.class);

    private final IGuiHelper helper;
    private RecipeViewerInfo recipeViewerInfo;
    private static final Font font = Minecraft.getInstance().font;

    public JeiTurbineCategory(IGuiHelper helper) {
        this.helper = helper;
        this.recipeViewerInfo = RECIPE_VIEWER_MAP.get("turbine");
    }

    @Override
    public RecipeType<TurbineRecipe> getRecipeType() {
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
        return Component.translatable("emi.category." + MODID + ".turbine");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TURBINE_MAP.get("turbine_controller").get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TurbineRecipe recipe, IFocusGroup focuses) {
        ScreenPosition position = recipeViewerInfo.fluid_inputs().getFirst();
        builder.addOutputSlot(position.x(), position.y()).addFluidStack(recipe.getFluidIngredient().getFluids()[0].getFluid(), recipe.getFluidIngredient().amount()).setFluidRenderer(recipe.getFluidIngredient().amount(), false, 18, 18);

        position = recipeViewerInfo.fluid_outputs().getFirst();
        builder.addOutputSlot(position.x() + 1, position.y() + 1).addFluidStack(recipe.getFluidIngredient().getFluids()[0].getFluid(), recipe.getFluidIngredient().amount()).setFluidRenderer(recipe.getFluidIngredient().amount(), false, 24, 24);
    }

    @Override
    public void draw(TurbineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(recipeViewerInfo.background(), 0, 0, recipeViewerInfo.rect().left(), recipeViewerInfo.rect().top(), recipeViewerInfo.rect().width(), recipeViewerInfo.rect().height());
        guiGraphics.blit(recipeViewerInfo.background(), recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 176, 3, (int) (((double) System.currentTimeMillis() / 75) % 37), 38);

        if (new ScreenRectangle(recipeViewerInfo.progress().x(), recipeViewerInfo.progress().y(), 37, recipeViewerInfo.rect().height() - recipeViewerInfo.progress().y() * 2).containsPoint((int) mouseX, (int) mouseY)) {
            ArrayList<Component> list = new ArrayList<>(2);
            list.addAll(List.of(
                    Component.translatable(NuclearcraftNeohaul.MODID + ".recipe_viewer.turbine_energy_density", Component.literal(NCMath.decimalPlaces(recipe.getTurbinePowerPerMB(), 2) + " RF/mB").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE),
                    Component.translatable(NuclearcraftNeohaul.MODID + ".recipe_viewer.turbine_expansion", Component.literal(NCMath.pcDecimalPlaces(recipe.getTurbineExpansionLevel(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY)));
            if (recipe.getTurbineSpinUpMultiplier() != 1.0)
                list.add(Component.translatable(NuclearcraftNeohaul.MODID + ".recipe_viewer.turbine_spin_up_multiplier", Component.literal(NCMath.pcDecimalPlaces(recipe.getTurbineSpinUpMultiplier(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));

            guiGraphics.renderTooltip(Minecraft.getInstance().font, list, Optional.empty(), (int) mouseX, (int) mouseY);
        }
    }
}