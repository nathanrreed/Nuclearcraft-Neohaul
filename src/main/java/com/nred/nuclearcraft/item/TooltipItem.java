package com.nred.nuclearcraft.item;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TooltipItem extends Item {
    private Component tooltip;
    public List<MutableComponent> shiftTooltips;
    public static final Component shiftForDetails = Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.shift_for_info").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

    public TooltipItem(Properties properties, List<MutableComponent> tooltips, boolean hasShiftTooltips, boolean radiation) {
        super(properties);

        if (tooltips.size() > 1 || hasShiftTooltips) {
            shiftTooltips = tooltips;
        } else if (!tooltips.isEmpty()) {
            tooltip = tooltips.getFirst();
        }
    }

    public TooltipItem(Properties properties, List<MutableComponent> tooltips, boolean hasShiftTooltips) {
        this(properties, tooltips, hasShiftTooltips, false);
    }

    public TooltipItem(Properties properties, List<String> tooltips) {
        this(properties, tooltips.stream().map(tooltip -> Component.translatable(tooltip).withStyle(ChatFormatting.AQUA)).toList(), false);
    }

    public TooltipItem(Properties properties) {
        this(properties, List.of(), false, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (shiftTooltips != null) {
            if (tooltipFlag.hasShiftDown()) {
                tooltipComponents.addAll(shiftTooltips);
            } else {
                tooltipComponents.add(shiftForDetails);
            }
        } else if (tooltip != null) {
            tooltipComponents.add(tooltip);
        }
    }
}