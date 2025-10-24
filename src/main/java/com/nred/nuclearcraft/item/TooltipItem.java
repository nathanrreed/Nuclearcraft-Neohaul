package com.nred.nuclearcraft.item;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.LevelReader;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

import static com.nred.nuclearcraft.info.Radiation.RAD_MAP;

public class TooltipItem extends Item {
    private Component tooltip;
    public List<MutableComponent> shiftTooltips;
    public static final Component shiftForDetails = Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.shift_for_info").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    private final boolean byPassShift;

    public TooltipItem(Properties properties, List<MutableComponent> tooltips, boolean hasShiftTooltips, boolean radiation, boolean byPassShift) {
        super(properties);

        if (tooltips.size() > 1 || hasShiftTooltips) {
            shiftTooltips = tooltips;
        } else if (!tooltips.isEmpty()) {
            tooltip = tooltips.getFirst();
        }

        this.byPassShift = byPassShift; // TODO
    }

    public TooltipItem(Properties properties, List<MutableComponent> tooltips, boolean byPassShift) {
        this(properties, tooltips, false, false, byPassShift);
    }

    public TooltipItem(Properties properties, List<MutableComponent> tooltips, boolean hasShiftTooltips, boolean byPassShift) {
        this(properties, tooltips, hasShiftTooltips, false, byPassShift);
    }

    public TooltipItem(Properties properties, List<String> tooltips) {
        this(properties, tooltips.stream().map(tooltip -> Component.translatable(tooltip).withStyle(ChatFormatting.AQUA)).toList(), false);
    }

    public TooltipItem(Properties properties) {
        this(properties, List.of(), false, false, false);
    }

    public TooltipItem(Properties properties, boolean byPassShift) {
        this(properties, List.of(), byPassShift);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return byPassShift;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (RAD_MAP.containsKey(ResourceLocation.parse(stack.getItem().toString()).getPath())) {
            double radiation = RAD_MAP.get(ResourceLocation.parse(stack.getItem().toString()).getPath());

            Triple<Integer, String, Integer> info = radiationColour(radiation);
            tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.radiation", info.getLeft(), info.getMiddle()).withColor(info.getRight()));
        }

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

    // Reference of colouring from original code TODO
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
