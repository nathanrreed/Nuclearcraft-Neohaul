package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.NCInfo;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import com.nred.nuclearcraft.multiblock.turbine.TurbinePartType;
import com.nred.nuclearcraft.multiblock.turbine.TurbinePlacement;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.FissionModeratorRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionReflectorRecipe;
import com.nred.nuclearcraft.recipe.fission.ItemFissionRecipe;
import com.nred.nuclearcraft.util.InfoHelper;
import it.zerono.mods.zerocore.base.multiblock.part.GenericDeviceBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TooltipHandler {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void addAdditionalTooltips(ItemTooltipEvent event) {
        List<Component> tooltip = event.getToolTip();
        ItemStack stack = event.getItemStack();

        addPlacementRuleTooltip(tooltip, stack);

        if (event.getContext().level() != null)
            addRecipeTooltip(tooltip, stack, event.getContext().level());

//        if (radiation_enabled_public) {
//            addArmorRadiationTooltip(tooltip, stack);
//            addRadiationTooltip(tooltip, stack);
//            addFoodRadiationTooltip(tooltip, stack);
//        }
    }

    // Placement Rule Tooltips

    @OnlyIn(Dist.CLIENT)
    private static void addPlacementRuleTooltip(List<Component> tooltip, ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GenericDeviceBlock<?, ?> block && (block.getPartType() == TurbinePartType.DynamoConnector || block.getMultiblockVariant().isPresent())) {
            String rule = null;
            if (block.getPartType() == TurbinePartType.DynamoConnector) {
                rule = TurbinePlacement.TOOLTIP_MAP.get("connector");
            } else if (block.getMultiblockVariant().get() instanceof TurbineDynamoCoilType coil) {
                rule = TurbinePlacement.TOOLTIP_MAP.get(coil.getSerializedName() + "_coil");
            } else if (block.getMultiblockVariant().get() instanceof FissionCoolantHeaterType heater) {
                rule = FissionPlacement.TOOLTIP_MAP.get(heater.getSerializedName() + "_heater");
            } else if (block.getMultiblockVariant().get() instanceof FissionHeatSinkType sink) {
                rule = FissionPlacement.TOOLTIP_MAP.get(sink.getSerializedName() + "_sink");
            }

            if (rule != null) {
                InfoHelper.infoFull(tooltip, ChatFormatting.AQUA, Component.literal(rule));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void addRecipeTooltip(List<Component> tooltips, ItemStack stack, Level level) {
        BasicRecipe recipe;

        Function<BasicRecipeHandler<?>, BasicRecipe> itemRecipe = x -> {
            RecipeInfo<? extends BasicRecipe> recipeInfo = x.getRecipeInfoFromInputs(level, Collections.singletonList(stack), Collections.emptyList());
            return recipeInfo == null ? null : recipeInfo.recipe;
        };

//        recipe = itemRecipe.apply(NCRecipes.machine_diaphragm); TODO
//        if (recipe != null) {
//            InfoHelper.infoFull(tooltip, new ChatFormatting[] {ChatFormatting.UNDERLINE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.RED}, NCInfo.machineDiaphragmFixedInfo(recipe), ChatFormatting.AQUA, NCInfo.machineDiaphragmInfo());
//        }
//
//        recipe = itemRecipe.apply(NCRecipes.machine_sieve_assembly);
//        if (recipe != null) {
//            InfoHelper.infoFull(tooltip, new ChatFormatting[] {ChatFormatting.UNDERLINE, ChatFormatting.LIGHT_PURPLE}, NCInfo.machineSieveAssemblyFixedInfo(recipe), ChatFormatting.AQUA, NCInfo.machineSieveAssemblyInfo());
//        }
//
//        BasicRecipe cathodeRecipe = itemRecipe.apply(NCRecipes.electrolyzer_cathode), anodeRecipe = itemRecipe.apply(NCRecipes.electrolyzer_anode);
//        if (cathodeRecipe != null || anodeRecipe != null) {
//            List<ChatFormatting> fixedColors = Lists.newArrayList(ChatFormatting.UNDERLINE);
//            if (cathodeRecipe != null) {
//                fixedColors.add(ChatFormatting.LIGHT_PURPLE);
//            }
//            if (anodeRecipe != null) {
//                fixedColors.add(ChatFormatting.BLUE);
//            }
//            InfoHelper.infoFull(tooltip, fixedColors.toArray(new ChatFormatting[0]), NCInfo.electrodeFixedInfo(cathodeRecipe, anodeRecipe), ChatFormatting.AQUA, NCInfo.electrodeInfo());
//        }

        recipe = itemRecipe.apply(NCRecipes.fission_moderator);
        if (recipe != null) {
            InfoHelper.infoFull(tooltips, new ChatFormatting[]{ChatFormatting.UNDERLINE, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE}, NCInfo.fissionModeratorFixedInfo((FissionModeratorRecipe) recipe), ChatFormatting.AQUA, NCInfo.fissionModeratorInfo());
        }

        recipe = itemRecipe.apply(NCRecipes.fission_reflector);
        if (recipe != null) {
            InfoHelper.infoFull(tooltips, new ChatFormatting[]{ChatFormatting.UNDERLINE, ChatFormatting.WHITE, ChatFormatting.LIGHT_PURPLE}, NCInfo.fissionReflectorFixedInfo((FissionReflectorRecipe) recipe), ChatFormatting.AQUA); // TODO not in NCO , NCInfo.fissionReflectorInfo()
        }

        recipe = itemRecipe.apply(NCRecipes.solid_fission);
        if (recipe != null) {
            InfoHelper.infoFull(tooltips, new ChatFormatting[]{ChatFormatting.UNDERLINE, ChatFormatting.GREEN, ChatFormatting.YELLOW, ChatFormatting.LIGHT_PURPLE, ChatFormatting.RED, ChatFormatting.GRAY, ChatFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo((ItemFissionRecipe) recipe));
        }

        recipe = itemRecipe.apply(NCRecipes.pebble_fission);
        if (recipe != null) {
            InfoHelper.infoFull(tooltips, new ChatFormatting[]{ChatFormatting.UNDERLINE, ChatFormatting.GREEN, ChatFormatting.YELLOW, ChatFormatting.LIGHT_PURPLE, ChatFormatting.RED, ChatFormatting.GRAY, ChatFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo((ItemFissionRecipe) recipe));
        }
    }
}