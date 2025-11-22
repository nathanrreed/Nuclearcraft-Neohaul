package com.nred.nuclearcraft.item.curios;

import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.capability.radiation.sink.IRadiationSink;
import com.nred.nuclearcraft.item.TooltipItem;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.CapabilityRegistration.CAPABILITY_ENTITY_RADS;
import static com.nred.nuclearcraft.registration.CapabilityRegistration.ITEM_CAPABILITY_RADIATION_SINK;
import static com.nred.nuclearcraft.registration.SoundRegistration.chems_wear_off;

public class RadiationBadgeItem extends TooltipItem {
    private static final Component BADGE_BROKEN = Component.translatable(MODID + ".radiation_badge.broken").withStyle(ChatFormatting.ITALIC);

    public RadiationBadgeItem() {
        super(new Properties().stacksTo(1), List.of(Component.translatable(MODID + ".radiation_badge", (Supplier<String>) () -> UnitHelper.prefix(radiation_badge_durability * radiation_badge_info_rate, 3, "Rad"), (Supplier<String>) () -> UnitHelper.prefix(radiation_badge_durability, 3, "Rad")).withStyle(ChatFormatting.AQUA)), true);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        IRadiationSink radiation = stack.getCapability(ITEM_CAPABILITY_RADIATION_SINK, null);
        if (radiation == null) {
            return false;
        }
        return radiation.getRadiationLevel() > 0D;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        IRadiationSink badge = stack.getCapability(ITEM_CAPABILITY_RADIATION_SINK, null);
        if (badge == null) {
            return 0;
        }
        return Mth.ceil(Mth.clamp(badge.getRadiationLevel() / radiation_badge_durability, 0D, 1D) * 13f);
    }

    public static void updateBadge(ItemStack stack, Player player) {
        IEntityRads entityRads = player.getCapability(CAPABILITY_ENTITY_RADS, null);
        IRadiationSink badge = stack.getCapability(ITEM_CAPABILITY_RADIATION_SINK, null);
        if (entityRads == null || badge == null) {
            return;
        }
        int infoCount = Mth.floor(badge.getRadiationLevel() / (radiation_badge_info_rate * radiation_badge_durability));
        badge.setRadiationLevel(badge.getRadiationLevel() + entityRads.getRadiationLevel());
        Level level = player.level();
        if (badge.getRadiationLevel() >= radiation_badge_durability) {
            if (!level.isClientSide()) {
                player.sendSystemMessage(Component.translatable(MODID + ".radiation_badge.exposure", UnitHelper.prefix(badge.getRadiationLevel(), 3, "Rad")).withStyle(ChatFormatting.ITALIC));
                player.sendSystemMessage(BADGE_BROKEN);
            } else {
                player.playSound(chems_wear_off.get(), (float) (0.65D * radiation_sound_volumes[5]), 1F);
            }
            stack.shrink(1);
        } else if (!level.isClientSide() && infoCount != Mth.floor(badge.getRadiationLevel() / (radiation_badge_info_rate * radiation_badge_durability))) {
            player.sendSystemMessage(Component.translatable(MODID + ".radiation_badge.exposure", UnitHelper.prefix(badge.getRadiationLevel(), 3, "Rad")).withStyle(ChatFormatting.ITALIC));
        }
    }
}