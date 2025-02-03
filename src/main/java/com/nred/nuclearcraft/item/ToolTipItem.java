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

    public ToolTipItem(Properties properties) {
        //TODO precompile static tooltips
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (RAD_MAP.containsKey(ResourceLocation.parse(stack.getItem().toString()).getPath())) {
            long radiation = RAD_MAP.get(ResourceLocation.parse(stack.getItem().toString()).getPath());

            Triple<Integer, String, Integer> info = radiationColour(radiation);
            tooltipComponents.add(Component.translatable("tooltip.radiation", info.getLeft(), info.getMiddle()).withColor(info.getRight()));
        }
    }

    public Triple<Integer, String, Integer> radiationColour(long radiation) {

        if (radiation > 100000000) { //TODO figure out how the colouring works
            return Triple.of(Math.toIntExact(radiation / 100000000), "m", ChatFormatting.RED.getColor());
        } else if (radiation > 1000000) {
            return Triple.of(Math.toIntExact(radiation / 1000000), "μ", ChatFormatting.GOLD.getColor());
        } else if (radiation > 1000) {
            return Triple.of(Math.toIntExact(radiation / 1000), "n", ChatFormatting.YELLOW.getColor());
        } else {
            return Triple.of(Math.toIntExact(radiation), "p", ChatFormatting.WHITE.getColor());
        }
    }
}
