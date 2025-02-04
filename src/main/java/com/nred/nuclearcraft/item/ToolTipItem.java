package com.nred.nuclearcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

import static com.nred.nuclearcraft.info.Radiation.RAD_MAP;

public class ToolTipItem extends Item {
    private Component tooltip;
    private Component shiftTooltip;

    public ToolTipItem(Properties properties, boolean tooltip, boolean radiation) {
        this(properties);
    }

    public ToolTipItem(Properties properties, boolean tooltip) {
        this(properties);
    }

    public ToolTipItem(Properties properties) {
        //TODO precompile static tooltips
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (RAD_MAP.containsKey(ResourceLocation.parse(stack.getItem().toString()).getPath())) {
            double radiation = RAD_MAP.get(ResourceLocation.parse(stack.getItem().toString()).getPath());

            Triple<Integer, String, Integer> info = radiationColour(radiation);
            tooltipComponents.add(Component.translatable("tooltip.radiation", info.getLeft(), info.getMiddle()).withColor(info.getRight()));
        }
    }

    public Triple<Integer, String, Integer> radiationColour(double radiation) {
        if (radiation < 0.000000001) {
            return Triple.of((int) Math.ceil(radiation * 100000000), "p", ChatFormatting.WHITE.getColor());
        } else if (radiation < 0.001) {
            return Triple.of((int) Math.ceil(radiation * 1000), "n", ChatFormatting.YELLOW.getColor());
        } else if (radiation < 0.1) {
            return Triple.of((int) Math.ceil(radiation * 10), "μ", ChatFormatting.GOLD.getColor());
        } else if (radiation < 1.0) {
            return Triple.of((int) Math.ceil(radiation), "m", ChatFormatting.RED.getColor());
        } else {
            return Triple.of((int) radiation, "c", ChatFormatting.DARK_RED.getColor());
        }
    }

//    public static TextFormatting getRadiationTextColor(double radiation) {
//        return radiation < 0.000000001D ? TextFormatting.WHITE : radiation < 0.001D ? TextFormatting.YELLOW : radiation < 0.1D ? TextFormatting.GOLD : radiation < 1D ? TextFormatting.RED : TextFormatting.DARK_RED;
//    }
//
//    public static TextFormatting getFoodRadiationTextColor(double radiation) {
//        return radiation <= -100D ? TextFormatting.LIGHT_PURPLE : radiation <= -10D ? TextFormatting.BLUE : radiation < 0D ? TextFormatting.AQUA : radiation < 0.1D ? TextFormatting.WHITE : radiation < 1D ? TextFormatting.YELLOW : radiation < 10D ? TextFormatting.GOLD : radiation < 100D ? TextFormatting.RED : TextFormatting.DARK_RED;
//    }
//
//    public static TextFormatting getFoodResistanceTextColor(double resistance) {
//        return resistance < 0D ? TextFormatting.GRAY : TextFormatting.WHITE;
//    }
}
