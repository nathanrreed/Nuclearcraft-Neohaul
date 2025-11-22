package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_RADIATION_SCRUBBER_CATEGORY;

public class EmiRadiationScrubberRecipe extends RecipeViewerRecipe {
    private final RadiationScrubberRecipe recipe;

    public EmiRadiationScrubberRecipe(ResourceLocation id, RadiationScrubberRecipe recipe) {
        super(EMI_RADIATION_SCRUBBER_CATEGORY, id);
        this.recipe = recipe;

        if (!recipe.getItemIngredients().isEmpty())
            this.inputs.add(EmiIngredient.of(recipe.getItemIngredient().ingredient(), recipe.getItemIngredient().count()));
        if (!recipe.getFluidIngredients().isEmpty())
            this.inputs.add(NeoForgeEmiIngredient.of(recipe.getFluidIngredient().sized()));
        if (!recipe.getItemProducts().isEmpty())
            this.outputs.add(EmiStack.of(recipe.getItemProduct().getStack()));
        if (!recipe.getFluidProducts().isEmpty())
            this.outputs.add(NeoForgeEmiStack.of(recipe.getFluidProduct().getStack()));
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(3);
        list.add(Component.translatable(MODID + ".recipe_viewer.scrubber_process_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getScrubberProcessTime(), 3)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
        list.add(Component.translatable(MODID + ".recipe_viewer.scrubber_process_power", Component.literal(UnitHelper.prefix(recipe.getScrubberProcessPower(), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
        list.add(Component.translatable(MODID + ".recipe_viewer.scrubber_process_efficiency", Component.literal(NCMath.pcDecimalPlaces(recipe.getScrubberProcessEfficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(recipe.getScrubberProcessTime() * 1.2);
    }
}