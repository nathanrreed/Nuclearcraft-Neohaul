package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import com.nred.nuclearcraft.util.UnitHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_SALT_COOLING_CATEGORY;

public class EmiSaltCoolingRecipe extends RecipeViewerRecipe {
    private final FissionCoolantHeaterRecipe recipe;

    public EmiSaltCoolingRecipe(ResourceLocation id, EmiIngredient fluidInput, EmiIngredient fluidResult, FissionCoolantHeaterRecipe recipe) {
        super(EMI_SALT_COOLING_CATEGORY, id, 0, 0);
        this.recipe = recipe;

        this.inputs.add(EmiStack.of(recipe.getHeater()));
        this.inputs.add(fluidInput);
        this.outputs.addAll(fluidResult.getEmiStacks());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(2);

        list.add(Component.translatable(MODID + ".recipe_viewer.coolant_heater_rate", Component.literal(UnitHelper.prefix(recipe.getCoolantHeaterCoolingRate(), 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.BLUE));

        String coolantHeaterInfo = FissionPlacement.TOOLTIP_MAP.getOrDefault(recipe.getCoolantHeaterPlacementRule(), "");
        if (!coolantHeaterInfo.isEmpty()) {
            list.add(Component.literal(coolantHeaterInfo).withStyle(ChatFormatting.AQUA));
        }
        return list;
    }

    @Override
    protected int getProgressTime() {
        return 2000;
    }
}