package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.SoundRegistration.*;

public class ItemUseHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        final ItemStack stack = event.getItem();
        if (stack.isEmpty()) {
            return;
        }

        final LivingEntity entity = event.getEntity();

        if (radiation_enabled_public && stack.has(DataComponents.FOOD)) {
            int packed = RecipeHelper.pack(stack);
            if (!RadSources.FOOD_RAD_MAP.containsKey(packed)) {
                return;
            }

            IEntityRads entityRads = RadiationHelper.getEntityRadiation(entity);
            if (entityRads == null) {
                return;
            }

            double rads = RadSources.FOOD_RAD_MAP.get(packed);
            double resistance = RadSources.FOOD_RESISTANCE_MAP.get(packed);

            if (rads > 0D) {
                entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() + rads);
                entityRads.setRecentPoisonAddition(rads);
                entity.playSound(rad_poisoning.get(), (float) ((1D - Math.pow(10D, -Math.sqrt(rads) / 10D)) * radiation_sound_volumes[3]), 1F);
            } else {
                entityRads.setRadawayBuffer(false, entityRads.getRadawayBuffer(false) - rads);
                entityRads.setRecentRadawayAddition(-rads);
                entity.playSound(radaway.get(), (float) ((1D - Math.pow(10D, -Math.sqrt(-rads) / 10D)) * radiation_sound_volumes[3]), 1F);
            }

            if (entityRads.getRadXCooldown() <= 0D) {
                entityRads.setInternalRadiationResistance(entityRads.getInternalRadiationResistance() + resistance);
                entityRads.setRecentRadXAddition(Math.abs(resistance));
                if (resistance > 0D) {
                    entity.playSound(rad_x.get(), (float) ((1D - Math.pow(10D, -5D * Math.sqrt(resistance) / radiation_rad_x_amount)) * radiation_sound_volumes[3]), 1F);
                } else {
                    entity.playSound(chems_wear_off.get(), (float) ((1D - Math.pow(10D, -5D * Math.sqrt(-resistance) / radiation_rad_x_amount)) * radiation_sound_volumes[3]), 1F);
                }
            }
        }
    }
}
