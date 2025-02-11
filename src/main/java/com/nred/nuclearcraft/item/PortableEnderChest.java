package com.nred.nuclearcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class PortableEnderChest extends Item {
    private static final Component CONTAINER_TITLE = Component.translatable("container.enderchest");

    public PortableEnderChest(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        level.playSound(player, player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5,
                SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS,
                0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        player.openMenu(new SimpleMenuProvider((containerId, inventory, player1) -> ChestMenu.threeRows(containerId, inventory, player1.getEnderChestInventory()), CONTAINER_TITLE));

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.portable.ender_chest").withStyle(ChatFormatting.AQUA));
    }
}