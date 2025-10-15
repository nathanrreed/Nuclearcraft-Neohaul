package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionEmergencyCoolingRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_EMERGENCY_COOLING_CATEGORY;

public class EmiFissionEmergencyCoolingRecipe extends RecipeViewerRecipe {
    private final FissionEmergencyCoolingRecipe recipe;

    public EmiFissionEmergencyCoolingRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, FissionEmergencyCoolingRecipe recipe) {
        super(EMI_EMERGENCY_COOLING_CATEGORY, id, 0, 0);
        this.recipe = recipe;

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(1);
        list.add(Component.translatable(MODID + ".recipe_viewer.fission_emergency_cooling_heating_required", Component.literal(UnitHelper.prefix(recipe.getEmergencyCoolingHeatPerInputMB(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(640.0 / recipe.getEmergencyCoolingHeatPerInputMB());
    }
}