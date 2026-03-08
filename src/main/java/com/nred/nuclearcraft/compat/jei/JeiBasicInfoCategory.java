package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.machine.InfiltratorPressureFluidRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.CONDENSER_DISSIPATION_TOOLTIP;
import static com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.INFILTRATOR_PRESSURE_FLUID_TOOLTIP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class JeiBasicInfoCategory<RECIPE extends BasicRecipe> extends JeiBasicCategory<RECIPE> {
    protected static final Font font = Minecraft.getInstance().font;
    private final ItemLike icon;
    private final Class<RECIPE> clazz;

    public JeiBasicInfoCategory(IGuiHelper helper, String name, ItemLike icon, Class<RECIPE> clazz) {
        super(helper, name);
        this.icon = icon;
        this.clazz = clazz;
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
    public void setRecipe(IRecipeLayoutBuilder builder, RECIPE recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder inputSlotBuilder = builder.addInputSlot(1, 1).setStandardSlotBackground();
        IIngredientAcceptor<?> outputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);

        for (SizedChanceItemIngredient typedIngredient : recipe.getItemIngredients()) {
            inputSlotBuilder.addIngredients(typedIngredient.ingredient());
            outputSlotBuilder.addIngredients(typedIngredient.ingredient());
        }
        for (SizedChanceFluidIngredient typedIngredient : recipe.getFluidIngredients()) {
            FluidStack fluid = Arrays.stream(typedIngredient.getFluidsRaw()).findFirst().orElse(FluidStack.EMPTY);
            if (fluid.isEmpty()) continue;
            IRecipeSlotBuilder slot = inputSlotBuilder.addFluidStack(fluid.getFluid());
            if (name.equals("condenser_dissipation")) {
                slot.addRichTooltipCallback((recipeSlotView, tooltips) -> tooltips.add(CONDENSER_DISSIPATION_TOOLTIP.apply(fluid)));
            } else if (name.equals("infiltrator_pressure")) {
                slot.addRichTooltipCallback((recipeSlotView, tooltips) -> tooltips.add(INFILTRATOR_PRESSURE_FLUID_TOOLTIP.apply((InfiltratorPressureFluidRecipe) recipe)));
            }
            outputSlotBuilder.addFluidStack(fluid.getFluid());
        }
    }

    @Override
    public RecipeType<RECIPE> getRecipeType() {
        return new RecipeType<>(ncLoc(name), clazz);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("emi.category." + MODID + "." + name);
    }
}