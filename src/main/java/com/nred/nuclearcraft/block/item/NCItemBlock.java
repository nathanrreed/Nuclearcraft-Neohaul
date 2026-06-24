package com.nred.nuclearcraft.block.item;

import com.nred.nuclearcraft.util.InfoHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class NCItemBlock extends BlockItem {
    private final ChatFormatting[] fixedColors;
    private final ChatFormatting infoColor;
    public final Component[] fixedInfo, info;

    public NCItemBlock(Block block, Properties properties, ChatFormatting[] fixedColors, Component[] fixedTooltip, boolean hasFixed, ChatFormatting infoColor, boolean hasInfo, Component... tooltip) {
        super(block, properties);
        this.fixedColors = fixedColors;
        this.fixedInfo = hasFixed ? InfoHelper.buildFixedInfo(block.getDescriptionId(), fixedTooltip) : new Component[]{};
        this.infoColor = infoColor;
        this.info = hasInfo ? InfoHelper.buildInfo(block.getDescriptionId(), tooltip) : new Component[]{};
    }

    public NCItemBlock(Block block, ChatFormatting[] fixedColors, Component[] fixedTooltip, boolean hasFixed, ChatFormatting infoColor, boolean hasInfo, Component... tooltip) {
        this(block, new Properties(), fixedColors, fixedTooltip, hasFixed, infoColor, hasInfo, tooltip);
    }

    public NCItemBlock(Block block, Properties properties, ChatFormatting[] fixedColors, Component[] fixedTooltip, boolean hasFixed, ChatFormatting infoColor, Component... tooltip) {
        this(block, properties, fixedColors, fixedTooltip, hasFixed, infoColor, true, tooltip);
    }

    public NCItemBlock(Block block, ChatFormatting[] fixedColors, Component[] fixedTooltip, boolean hasFixed, ChatFormatting infoColor, Component... tooltip) {
        this(block, new Properties(), fixedColors, fixedTooltip, hasFixed, infoColor, true, tooltip);
    }

    public NCItemBlock(Block block, Properties properties, ChatFormatting fixedColor, Component[] fixedTooltip, boolean hasFixed, ChatFormatting infoColor, boolean hasInfo, Component... tooltip) {
        this(block, properties, new ChatFormatting[]{fixedColor}, fixedTooltip, hasFixed, infoColor, hasInfo, tooltip);
    }

    public NCItemBlock(Block block, ChatFormatting fixedColor, Component[] fixedTooltip, boolean hasFixed, ChatFormatting infoColor, boolean hasInfo, Component... tooltip) {
        this(block, new Properties(), new ChatFormatting[]{fixedColor}, fixedTooltip, hasFixed, infoColor, hasInfo, tooltip);
    }

    public NCItemBlock(Block block, Properties properties, ChatFormatting fixedColor, Component[] fixedTooltip, boolean hasFixed, ChatFormatting infoColor, Component... tooltip) {
        this(block, properties, new ChatFormatting[]{fixedColor}, fixedTooltip, hasFixed, infoColor, true, tooltip);
    }

    public NCItemBlock(Block block, ChatFormatting fixedColor, Component[] fixedTooltip, boolean hasFixed, ChatFormatting infoColor, Component... tooltip) {
        this(block, new Properties(), new ChatFormatting[]{fixedColor}, fixedTooltip, hasFixed, infoColor, true, tooltip);
    }

    public NCItemBlock(Block block, Properties properties, ChatFormatting infoColor, Component... tooltip) {
        this(block, properties, ChatFormatting.RED, InfoHelper.EMPTY_ARRAY, false, infoColor, tooltip);
    }

    public NCItemBlock(Block block, Properties properties, Component... tooltip) {
        this(block, properties, ChatFormatting.RED, InfoHelper.EMPTY_ARRAY, false, ChatFormatting.AQUA, tooltip);
    }

    public NCItemBlock(Block block, Component... tooltip) {
        this(block, new Properties(), ChatFormatting.RED, InfoHelper.EMPTY_ARRAY, false, ChatFormatting.AQUA, tooltip);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        if (fixedInfo.length > 0 || info.length > 0) {
            if (fixedColors.length == 1) {
                InfoHelper.infoFull(tooltip, fixedColors[0], fixedInfo, infoColor, info);
            } else {
                InfoHelper.infoFull(tooltip, fixedColors, fixedInfo, infoColor, info);
            }
        }
    }
}