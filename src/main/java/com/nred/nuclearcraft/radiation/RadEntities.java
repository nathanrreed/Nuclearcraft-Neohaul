package com.nred.nuclearcraft.radiation;

import com.nred.nuclearcraft.util.RegistryHelper;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import static com.nred.nuclearcraft.config.NCConfig.max_entity_rads;

public class RadEntities {
    public static final Object2DoubleMap<Class<? extends Entity>> MAX_RADS_MAP = new Object2DoubleOpenHashMap<>();

    public static void init() { // TODO call
        for (String entityInfo : max_entity_rads) {
            int scorePos = entityInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            EntityType<?> entityEntry = RegistryHelper.getEntityEntry(entityInfo.substring(0, scorePos));
            if (entityEntry != null) {
                MAX_RADS_MAP.put(entityEntry.getBaseClass(), Double.parseDouble(entityInfo.substring(scorePos + 1)));
            }
        }
    }
}