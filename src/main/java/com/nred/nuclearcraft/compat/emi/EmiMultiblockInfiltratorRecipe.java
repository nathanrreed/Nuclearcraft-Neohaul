package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_MULTIBLOCK_INFILTRATOR_CATEGORY;
import static com.nred.nuclearcraft.config.NCConfig.machine_infiltrator_power;
import static com.nred.nuclearcraft.config.NCConfig.machine_infiltrator_time;

public class EmiMultiblockInfiltratorRecipe extends RecipeViewerRecipe {
    private final MultiblockInfiltratorRecipe recipe;

    public EmiMultiblockInfiltratorRecipe(ResourceLocation id, MultiblockInfiltratorRecipe recipe) {
        super(EMI_MULTIBLOCK_INFILTRATOR_CATEGORY, id);
        this.recipe = recipe;

        this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        this.inputs.addAll(recipe.fluidIngredients.stream().map(ModEmiPlugin::getEmiFluidIngredient).toList());
        this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
        this.outputs.addAll(recipe.fluidProducts.stream().map(ModEmiPlugin::getEmiFluidStack).toList());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(2);

        list.add(Component.translatable(MODID + ".tooltip.process_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getBaseProcessTime(machine_infiltrator_time), 3)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
        list.add(Component.translatable(MODID + ".tooltip.process_power", Component.literal(UnitHelper.prefix(recipe.getBaseProcessPower(machine_infiltrator_power), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));

        double heatingBonus = recipe.getInfiltratorHeatingFactor();
        if (heatingBonus != 0D) {
            list.add(Component.translatable(MODID + ".recipe_viewer.infiltrator_heating_factor", Component.literal(NCMath.pcDecimalPlaces(heatingBonus, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
        }

//        double radiation = recipe.getBaseProcessRadiation(); TODO
//        if (radiation > 0D) {
//            list.add(Component.translatable(MODID + ".tooltip.base_process_radiation", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
//        }

        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(4D * recipe.getBaseProcessTime(machine_infiltrator_time));
    }
}