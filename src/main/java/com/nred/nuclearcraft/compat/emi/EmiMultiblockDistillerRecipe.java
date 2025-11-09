package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_MULTIBLOCK_DISTILLER_CATEGORY;
import static com.nred.nuclearcraft.config.NCConfig.machine_distiller_power;
import static com.nred.nuclearcraft.config.NCConfig.machine_distiller_time;

public class EmiMultiblockDistillerRecipe extends RecipeViewerRecipe {
    private final MultiblockDistillerRecipe recipe;

    public EmiMultiblockDistillerRecipe(ResourceLocation id, MultiblockDistillerRecipe recipe) {
        super(EMI_MULTIBLOCK_DISTILLER_CATEGORY, id);
        this.recipe = recipe;

        this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        this.inputs.addAll(recipe.fluidIngredients.stream().map(ModEmiPlugin::getEmiFluidIngredient).toList());
        this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
        this.outputs.addAll(recipe.fluidProducts.stream().map(ModEmiPlugin::getEmiFluidStack).toList());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(3);

        list.add(Component.translatable(MODID + ".tooltip.process_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getBaseProcessTime(machine_distiller_time), 3))).withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.GREEN));
        list.add(Component.translatable(MODID + ".tooltip.process_power", Component.literal(UnitHelper.prefix(recipe.getBaseProcessPower(machine_distiller_power), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
        list.add(Component.translatable(MODID + ".recipe_viewer.distiller_sieve_tray_count", Component.literal(recipe.getDistillerSieveTrayCount() + "").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));

//        double radiation = recipe.getBaseProcessRadiation(); TODO
//        if (radiation > 0D) {
//            list.add(Component.translatable(MODID + ".tooltip.base_process_radiation", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
//        }

        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(20D * recipe.getBaseProcessTime(machine_distiller_time));
    }
}