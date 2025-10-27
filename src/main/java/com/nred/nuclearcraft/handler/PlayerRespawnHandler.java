package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.util.ModCheck;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import vazkii.patchouli.api.PatchouliAPI;

import static com.nred.nuclearcraft.config.NCConfig.give_guidebook;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class PlayerRespawnHandler {

//	@SubscribeEvent TODO
//	public void onPlayerRespawn(PlayerEvent.Clone event) {
//		Player newPlayer = event.getEntity();
//		Player oldPlayer = event.getOriginal();
//
//        if (radiation_enabled_public) {
//			if (!oldPlayer.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
//				return;
//			}
//			IEntityRads oldRads = oldPlayer.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
//			if (oldRads == null) {
//				return;
//			}
//
//			if (!newPlayer.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
//				return;
//			}
//			IEntityRads newRads = newPlayer.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
//			if (newRads == null) {
//				return;
//			}
//
//			if (event.isWasDeath()) {
//				if (radiation_death_persist) {
//					newRads.setTotalRads(oldRads.getTotalRads() * radiation_death_persist_fraction % oldRads.getMaxRads(), false);
//				}
//				newRads.setRadiationImmunityTime(radiation_death_immunity_time * 20D);
//			}
//			else {
//				newRads.setConsumedMedicine(oldRads.getConsumedMedicine());
//				newRads.setExternalRadiationResistance(oldRads.getExternalRadiationResistance());
//				newRads.setInternalRadiationResistance(oldRads.getInternalRadiationResistance());
//				newRads.setPoisonBuffer(oldRads.getPoisonBuffer());
//				newRads.setRadawayBuffer(false, oldRads.getRadawayBuffer(false));
//				newRads.setRadawayBuffer(true, oldRads.getRadawayBuffer(true));
//				newRads.setRadawayCooldown(oldRads.getRadawayCooldown());
//				newRads.setRadiationImmunityStage(oldRads.getRadiationImmunityStage());
//				newRads.setRadiationImmunityTime(oldRads.getRadiationImmunityTime());
//				newRads.setRadXCooldown(oldRads.getRadXCooldown());
//				newRads.setRadXUsed(oldRads.getRadXUsed());
//				newRads.setRadXWoreOff(oldRads.getRadXWoreOff());
//				newRads.setRecentPoisonAddition(oldRads.getRecentPoisonAddition());
//				newRads.setRecentRadawayAddition(oldRads.getRecentRadawayAddition());
//				newRads.setRecentRadXAddition(oldRads.getRecentRadXAddition());
//				newRads.setShouldWarn(oldRads.getShouldWarn());
//				newRads.setTotalRads(oldRads.getTotalRads(), false);
//			}
//
//			newRads.setGiveGuidebook(oldRads.getGiveGuidebook());
//		}
//	}

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
//		if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) { TODO
//			return;
//		}
//
//		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
//		if (playerRads == null) {
//			return;
//		}

        if (give_guidebook && ModCheck.patchouliLoaded()) { // && playerRads.getGiveGuidebook() TODO
            ItemStack stack = PatchouliAPI.get().getBookStack(ncLoc("guide"));
            if (!player.getInventory().hasAnyMatching(stack1 -> ItemStack.isSameItemSameComponents(stack, stack1))) {
                boolean success = player.getInventory().add(stack);
//			if (success) { TODO
//				playerRads.setGiveGuidebook(false);
//			}
            }
        }
    }
}
