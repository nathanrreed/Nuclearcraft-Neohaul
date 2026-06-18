package com.nred.nuclearcraft.compat.guideme;

import com.nred.nuclearcraft.NCInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.SaltFissionRecipe;
import com.nred.nuclearcraft.util.InfoHelper;
import guideme.document.interaction.GuideTooltip;
import guideme.siteexport.ResourceExporter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class FluidTooltip implements GuideTooltip {
    private final FluidStack stack;

    public FluidTooltip(FluidStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getIcon() {
        return stack.getFluidType().getBucket(stack);
    }

    @Override
    public List<ClientTooltipComponent> getLines() {
        Tank tank = new Tank(1000, null);
        tank.setFluid(stack);

        ArrayList<Component> tooltips = new ArrayList<>(1);
        tooltips.add(stack.getFluid().getFluidType().getDescription());
        Function<BasicRecipeHandler<?>, BasicRecipe> fluidRecipe = x -> {
            RecipeInfo<? extends BasicRecipe> recipeInfo = x.getRecipeInfoFromInputs(Minecraft.getInstance().level, Collections.emptyList(), List.of(tank));
            return recipeInfo == null ? null : recipeInfo.recipe;
        };
        SaltFissionRecipe recipe = (SaltFissionRecipe) fluidRecipe.apply(NCRecipes.salt_fission);
        if (recipe != null) {
            InfoHelper.infoFull(tooltips, new ChatFormatting[]{ChatFormatting.UNDERLINE, ChatFormatting.GREEN, ChatFormatting.YELLOW, ChatFormatting.LIGHT_PURPLE, ChatFormatting.RED, ChatFormatting.AQUA, ChatFormatting.GRAY, ChatFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo(recipe));
        }

        return tooltips.stream().map(e -> ClientTooltipComponent.create(e.getVisualOrderText())).toList();
    }

    @Override
    public void exportResources(ResourceExporter exporter) {
        exporter.referenceFluid(stack.getFluid());
    }
}