package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionHeatingRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_VENT_CATEGORY;

public class EmiFissionVentRecipe extends RecipeViewerRecipe {
    private final FissionHeatingRecipe recipe;

    public EmiFissionVentRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, FissionHeatingRecipe recipe) {
        super(EMI_VENT_CATEGORY, id, 0, 0);
        this.recipe = recipe;

        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(1);
        list.add(Component.translatable(MODID + ".recipe_viewer.fission_heating_required", Component.literal(UnitHelper.prefix(recipe.getFissionHeatingHeatPerInputMB(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(recipe.getFissionHeatingHeatPerInputMB() * 4.0);
    }
}