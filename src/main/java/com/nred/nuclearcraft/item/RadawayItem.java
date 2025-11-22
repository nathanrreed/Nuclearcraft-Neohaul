package com.nred.nuclearcraft.item;

import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.CapabilityRegistration.CAPABILITY_ENTITY_RADS;
import static com.nred.nuclearcraft.registration.SoundRegistration.chems_wear_off;
import static com.nred.nuclearcraft.registration.SoundRegistration.radaway;

public class RadawayItem extends TooltipItem {
    private final boolean slow;

    public RadawayItem(boolean slow, List<MutableComponent> tooltip) {
        super(new Properties(), tooltip, false);
        this.slow = slow;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            IEntityRads playerRads = player.getCapability(CAPABILITY_ENTITY_RADS, null);
            if (playerRads == null) {
                return stack;
            }
            if (playerRads.canConsumeRadaway()) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), radaway.get(), SoundSource.PLAYERS, (float) (0.5D * radiation_sound_volumes[1]), 1F);
                onRadawayConsumed(stack, level, player);
                player.awardStat(Stats.ITEM_USED.get(this));
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
                }
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                playerRads.setConsumedMedicine(true);
                playerRads.setRadawayCooldown(radiation_radaway_cooldown);
                if (radiation_radaway_cooldown >= 10D) {
                    sendCooldownMessage(level, player, playerRads, false);
                }
            } else {
                playerRads.setConsumedMedicine(false);
            }
        }
        return stack;
    }

    private static void sendCooldownMessage(Level level, Player player, IEntityRads playerRads, boolean playSound) {
        if (playerRads.getRadawayCooldown() > 0D && playerRads.getMessageCooldownTime() <= 0) {
            if (playSound && level.isClientSide()) {
                player.playSound(chems_wear_off.get(), (float) (0.5D * radiation_sound_volumes[4]), 1F);
            }
            if (!level.isClientSide()) {
                playerRads.setMessageCooldownTime(20);
                player.sendSystemMessage(Component.translatable(MODID + ".message.radaway_cooling_down", UnitHelper.applyTimeUnitShort(Math.ceil(playerRads.getRadawayCooldown()), 3, 1)).withStyle(ChatFormatting.ITALIC));
            }
        }
    }

    private void onRadawayConsumed(ItemStack stack, Level world, Player player) {
        if (world.isClientSide()) {
            return;
        }
        IEntityRads playerRads = player.getCapability(CAPABILITY_ENTITY_RADS, null);
        if (playerRads == null) {
            return;
        }
        playerRads.setRadawayBuffer(slow, playerRads.getRadawayBuffer(slow) + (slow ? radiation_radaway_slow_amount : radiation_radaway_amount));
        if (!slow) {
            playerRads.setRecentRadawayAddition(radiation_radaway_amount);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 16;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        IEntityRads playerRads = player.getCapability(CAPABILITY_ENTITY_RADS, null);
        if (playerRads == null) {
            return InteractionResultHolder.fail(stack);
        }

        if (!playerRads.canConsumeRadaway()) {
            playerRads.setConsumedMedicine(false);
            sendCooldownMessage(level, player, playerRads, true);
            return InteractionResultHolder.fail(stack);
        }
        if (!playerRads.isTotalRadsNegligible()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }
}