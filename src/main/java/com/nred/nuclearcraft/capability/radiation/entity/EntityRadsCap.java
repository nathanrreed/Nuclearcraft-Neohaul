package com.nred.nuclearcraft.capability.radiation.entity;

import com.nred.nuclearcraft.radiation.RadEntities;
import net.minecraft.world.entity.LivingEntity;

import static com.nred.nuclearcraft.registration.AttachmentRegistration.ENTITY_RADIATION;

public class EntityRadsCap extends PlayerRadsCap {
    public EntityRadsCap(LivingEntity entity) {
        super(entity);
    }

    @Override
    public double getMaxRads() {
        EntityRads rads = entity.getData(ENTITY_RADIATION);

        if (!rads.setMaxRads) {
            rads.maxRads = RadEntities.MAX_RADS_MAP.containsKey(entity.getClass()) ? RadEntities.MAX_RADS_MAP.getDouble(entity.getClass()) : 50D * entity.getMaxHealth();
            rads.setMaxRads = true;
        }
        entity.setData(ENTITY_RADIATION, rads);

        return rads.maxRads;
    }
}