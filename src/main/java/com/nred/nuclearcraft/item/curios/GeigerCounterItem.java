package com.nred.nuclearcraft.item.curios;

import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.item.TooltipItem;
import com.nred.nuclearcraft.radiation.RadiationHandler;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.radiation_require_counter;

public class GeigerCounterItem extends TooltipItem {
    public GeigerCounterItem() {
        super(new Properties().stacksTo(1), List.of(Component.translatable(MODID + ".tooltip.geiger_counter").withStyle(ChatFormatting.AQUA)), true);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide()) {
            HitResult ray = Minecraft.getInstance().hitResult;
            if (ray == null || ray.getType() != HitResult.Type.ENTITY) {
                IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
                if (playerRads != null) {
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.geiger_counter.rads", (playerRads.isTotalRadsNegligible() ? "0 Rad" : RadiationHelper.radsPrefix(playerRads.getTotalRads(), false)) + " [" + Math.round(playerRads.getRadsPercentage()) + "%], " + RadiationHelper.getRawRadiationTextColor(playerRads) + (playerRads.isRawRadiationNegligible() ? "0 Rad/t" : RadiationHelper.radsPrefix(playerRads.getRawRadiationLevel(), true))).withStyle(RadiationHelper.getRadsTextColor(playerRads)));
                }
            } else if (((EntityHitResult) ray).getEntity() instanceof LivingEntity livingEntity) {
                IEntityRads entityRads = RadiationHelper.getEntityRadiation(livingEntity);
                if (entityRads != null) {
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.geiger_counter.rads", (entityRads.isTotalRadsNegligible() ? "0 Rad" : RadiationHelper.radsPrefix(entityRads.getTotalRads(), false)) + " [" + Math.round(entityRads.getRadsPercentage()) + "%], " + RadiationHelper.getRadiationTextColor(entityRads) + (entityRads.isRadiationNegligible() ? "0 Rad/t" : RadiationHelper.radsPrefix(entityRads.getRadiationLevel(), true))));
                }
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() || !radiation_require_counter || !(entity instanceof Player player)) {
            return;
        }
        if (player.getInventory().items.subList(0, 9).contains(stack)) {
            RadiationHandler.playGeigerSound(player);
        }
    }
}