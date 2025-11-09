package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_TURBINE_CATEGORY;

public class EmiTurbineRecipe extends RecipeViewerRecipe {
    private final double expansion_level;
    private final double power_per_mb;
    private final double spin_up_multiplier;

    public EmiTurbineRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, double power_per_mb, double expansion_level, double spin_up_multiplier) {
        super(EMI_TURBINE_CATEGORY, id);
        this.expansion_level = expansion_level;
        this.power_per_mb = power_per_mb;
        this.spin_up_multiplier = spin_up_multiplier;

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(2);
        list.addAll(List.of(
                Component.translatable(MODID + ".recipe_viewer.turbine_energy_density", Component.literal(NCMath.decimalPlaces(power_per_mb, 2) + " RF/mB").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE),
                Component.translatable(MODID + ".recipe_viewer.turbine_expansion", Component.literal(NCMath.pcDecimalPlaces(expansion_level, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY)));
        if (spin_up_multiplier != 1.0)
            list.add(Component.translatable(MODID + ".recipe_viewer.turbine_spin_up_multiplier", Component.literal(NCMath.pcDecimalPlaces(spin_up_multiplier, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
        return list;
    }
}