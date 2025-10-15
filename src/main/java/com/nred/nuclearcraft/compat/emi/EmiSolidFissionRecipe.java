package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.fission.SolidFissionRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_SOLID_FISSION_CATEGORY;
import static com.nred.nuclearcraft.config.Config2.fission_decay_mechanics;

public class EmiSolidFissionRecipe extends RecipeViewerRecipe {
    private final SolidFissionRecipe recipe;

    public EmiSolidFissionRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, SolidFissionRecipe recipe) {
        super(EMI_SOLID_FISSION_CATEGORY, id, 0, 0);
        this.recipe = recipe;

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(4);

        list.add(Component.translatable(MODID + ".info.fission_fuel.base_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getFissionFuelTime(), 3)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
        list.add(Component.translatable(MODID + ".info.fission_fuel.base_heat", Component.literal(UnitHelper.prefix(recipe.getFissionFuelHeat(), 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
        list.add(Component.translatable(MODID + ".info.fission_fuel.base_efficiency", Component.literal(NCMath.pcDecimalPlaces(recipe.getFissionFuelEfficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
        list.add(Component.translatable(MODID + ".info.fission_fuel.criticality", Component.literal(recipe.getFissionFuelCriticality() + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));

        if (fission_decay_mechanics) {
            list.add(Component.translatable(MODID + ".info.fission_fuel.decay_factor", Component.literal(NCMath.pcDecimalPlaces(recipe.getFissionFuelDecayFactor(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
        }
        if (recipe.getFissionFuelSelfPriming()) {
            list.add(Component.translatable(MODID + ".info.fission_fuel.self_priming").withStyle(ChatFormatting.DARK_AQUA));
        }
//        double radiation = getFissionFuelRadiation();  TODO add radiation
//        if (radiation > 0D) {
//            list.add(Component.translatable(MODID + ".recipe_viewer.fission_fuel.radiation_per_flux", RadiationHelper.radsColoredPrefix(radiation, true))).withStyle(ChatFormatting.GOLD);
//        }
        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(recipe.getFissionFuelTime() * 16.0);
    }
}