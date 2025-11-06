package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_HEAT_EXCHANGER_CATEGORY;

public class EmiHeatExchangerRecipe extends RecipeViewerRecipe {
    private final HeatExchangerRecipe recipe;

    public EmiHeatExchangerRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, HeatExchangerRecipe recipe) {
        super(EMI_HEAT_EXCHANGER_CATEGORY, id, 0, 0);
        this.recipe = recipe;

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(3);

        boolean heating = recipe.getHeatExchangerIsHeating();
        list.add(Component.translatable(MODID + ".recipe_viewer.exchanger_fluid_temp_in", Component.literal(recipe.getHeatExchangerInputTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle((heating ? ChatFormatting.AQUA : ChatFormatting.RED)));
        list.add(Component.translatable(MODID + ".recipe_viewer.exchanger_fluid_temp_out", Component.literal(recipe.getHeatExchangerOutputTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle((heating ? ChatFormatting.RED : ChatFormatting.AQUA)));
        list.add(Component.translatable(MODID + ".recipe_viewer." + (heating ? "exchanger_heating_required" : "exchanger_cooling_required"), Component.literal(UnitHelper.prefix(recipe.getHeatExchangerHeatDifference(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle((heating ? ChatFormatting.GOLD : ChatFormatting.BLUE)));

        double flowDirectionBonus = recipe.getHeatExchangerFlowDirectionBonus();
        if (flowDirectionBonus != 0D) {
            int preferredFlowDirection = recipe.getHeatExchangerPreferredFlowDirection();
            list.add(Component.translatable(MODID + ".recipe_viewer." + (preferredFlowDirection == 0 ? "exchanger_horizontal_bonus" : (preferredFlowDirection > 0 ? "exchanger_upward_bonus" : "exchanger_downward_bonus")), Component.literal(NCMath.pcDecimalPlaces(flowDirectionBonus, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(400D * recipe.getHeatExchangerHeatDifference());
    }
}