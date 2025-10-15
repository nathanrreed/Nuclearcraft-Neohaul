package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionIrradiatorRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_IRRADIATOR_CATEGORY;

public class EmiFissionIrradiatorRecipe extends RecipeViewerRecipe {
    private final FissionIrradiatorRecipe recipe;

    public EmiFissionIrradiatorRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, FissionIrradiatorRecipe recipe) {
        super(EMI_IRRADIATOR_CATEGORY, id, 0, 0);
        this.recipe = recipe;

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(2);

        list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_flux_required", Component.literal(UnitHelper.prefix(recipe.getIrradiatorFluxRequired(), 5, "N")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
        double heatPerFlux = recipe.getIrradiatorHeatPerFlux();
        if (heatPerFlux > 0D) {
            list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_heat_per_flux", Component.literal(UnitHelper.prefix(heatPerFlux, 5, "H/N")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
        }
        double efficiency = recipe.getIrradiatorProcessEfficiency();
        if (efficiency > 0D) {
            list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_process_efficiency", Component.literal(NCMath.pcDecimalPlaces(efficiency, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        long minFluxPerTick = recipe.getIrradiatorMinFluxPerTick(), maxFluxPerTick = recipe.getIrradiatorMaxFluxPerTick();
        if (minFluxPerTick > 0 || (maxFluxPerTick >= 0 && maxFluxPerTick < Long.MAX_VALUE)) {
            if (minFluxPerTick <= 0) {
                list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_valid_flux_maximum", Component.literal(minFluxPerTick + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
            } else if (maxFluxPerTick < 0 || maxFluxPerTick == Long.MAX_VALUE) {
                list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_valid_flux_minimum", Component.literal(maxFluxPerTick + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
            } else {
                list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_valid_flux_range", Component.literal(minFluxPerTick + " - " + maxFluxPerTick + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
            }
        }
//        double radiation = getIrradiatorBaseProcessRadiation() / RecipeStats.getFissionMaxModeratorLineFlux(); TODO add radiation
//        if (radiation > 0D) {
//            tooltip.add(TextFormatting.GOLD + RADIATION_PER_FLUX + " " + RadiationHelper.getRadiationTextColor(radiation) + UnitHelper.prefix(radiation, 3, "Rad/N"));
//            list.add(Component.translatable(MODID + ".recipe_viewer.radiation_per_flux", Component.literal(RadiationHelper.getRadiationTextColor(radiation) + UnitHelper.prefix(radiation, 3, "Rad/N")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GOLD));
//        }
        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(recipe.getIrradiatorFluxRequired() / 80);
    }
}