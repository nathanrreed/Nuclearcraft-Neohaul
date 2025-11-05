package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_CONDENSER_CATEGORY;

public class EmiCondenserRecipe extends RecipeViewerRecipe {
    private final CondenserRecipe recipe;

    public EmiCondenserRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, CondenserRecipe recipe) {
        super(EMI_CONDENSER_CATEGORY, id, 0, 0);
        this.recipe = recipe;

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(3);

        list.add(Component.translatable(MODID + ".recipe_viewer.condenser_fluid_temp_in", Component.literal(recipe.getCondenserInputTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
        list.add(Component.translatable(MODID + ".recipe_viewer.condenser_fluid_temp_out", Component.literal(recipe.getCondenserOutputTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable(MODID + ".recipe_viewer.condenser_cooling_required", Component.literal(UnitHelper.prefix(recipe.getCondenserCoolingRequired(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.BLUE));

        double flowDirectionBonus = recipe.getCondenserFlowDirectionBonus();
        if (flowDirectionBonus != 0D) {
            int preferredFlowDirection = recipe.getCondenserPreferredFlowDirection();
            list.add(Component.translatable(MODID + ".recipe_viewer." + (preferredFlowDirection == 0 ? "condenser_horizontal_bonus" : (preferredFlowDirection > 0 ? "condenser_upward_bonus" : "condenser_downward_bonus")), Component.literal(NCMath.pcDecimalPlaces(flowDirectionBonus, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(400D * recipe.getCondenserCoolingRequired());
    }
}