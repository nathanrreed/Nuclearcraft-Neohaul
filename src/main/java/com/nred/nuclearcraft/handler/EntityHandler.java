package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.entity.FeralGhoul;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent.SpawnPlacementCheck.Result;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.radiation_enabled_public;
import static com.nred.nuclearcraft.config.NCConfig.radiation_hardcore_stacks;
import static com.nred.nuclearcraft.registration.EntityRegistration.FERAL_GHOUL;
import static net.minecraft.world.entity.MobSpawnType.SPAWNER;

public class EntityHandler {

    @SubscribeEvent
    public void onEntityLivingSpawn(MobSpawnEvent.SpawnPlacementCheck event) {
        EntityType<?> entity = event.getEntityType();
        if (entity.equals(FERAL_GHOUL)) {
            if (event.getSpawnType() == SPAWNER) {
                Level level = event.getLevel().getLevel();
                BlockPos pos = event.getPos();

                boolean canSeeSky = level.canSeeSky(pos);

                if (!canSeeSky) {
                    event.setResult(Result.FAIL);
                    return;
                }

                ChunkPos chunkPos = level.getChunk(pos).getPos();
                List<FeralGhoul> ghoulList = level.getEntitiesOfClass(FeralGhoul.class, new AABB(chunkPos.getMinBlockX(), level.getMinBuildHeight(), chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX(), level.getMaxBuildHeight(), chunkPos.getMaxBlockZ()));

                if (ghoulList.size() > 1) {
                    event.setResult(Result.FAIL);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemExpired(ItemExpireEvent event) {
        final ItemEntity entity = event.getEntity();

        final ItemStack stack = entity.getItem();
        if (stack.isEmpty()) {
            return;
        }

        if (radiation_enabled_public && radiation_hardcore_stacks) {
            ChunkAccess chunk = entity.level().getChunk(entity.blockPosition());
            if (chunk.isUnsaved()) {
                IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
                if (chunkSource != null) {
                    RadiationHelper.addToSourceRadiation(chunkSource, RadiationHelper.getRadiationFromStack(stack, 8D));
                }
            }
        }
    }
}