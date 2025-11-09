package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_DECAY_GENERATOR_CATEGORY;

public class EmiDecayGeneratorRecipe extends RecipeViewerRecipe {
    private final DecayGeneratorRecipe recipe;

    public EmiDecayGeneratorRecipe(ResourceLocation id, DecayGeneratorRecipe recipe) {
        super(EMI_DECAY_GENERATOR_CATEGORY, id);
        this.recipe = recipe;

        this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
    }

    @Override
    public List<Component> progressTooltips(int x, int y) {
        ArrayList<Component> list = new ArrayList<>(2);

        list.add(Component.translatable(MODID + ".recipe_viewer.decay_gen_lifetime", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getDecayGeneratorLifetime(), 3, 1))).withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.GREEN));
        list.add(Component.translatable(MODID + ".recipe_viewer.decay_gen_power", Component.literal(UnitHelper.prefix(recipe.getDecayGeneratorPower(), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));


//        double radiation = recipe.getBaseProcessRadiation(); TODO
//        if (radiation > 0D) {
//            list.add(Component.translatable(MODID + ".tooltip.decay_gen_radiation", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
//        }

        return list;
    }

    @Override
    protected int getProgressTime() {
        return NCMath.toInt(20D * recipe.getDecayGeneratorLifetime());
    }
}