package com.nred.nuclearcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.upgrade_stack_sizes;

public class UpgradeItem extends TooltipItem {
    private final int id;

    public UpgradeItem(int id, List<MutableComponent> tooltips, boolean b, boolean b1) {
        super(new Properties(), tooltips, b, b1);
        this.id = id;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return upgrade_stack_sizes == null ? 64 : upgrade_stack_sizes[id];
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return true;
    }
}