package com.nred.nuclearcraft.radiation;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.radiation.ITileRadiationEnvironment;
import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.entity.FeralGhoul;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.payload.radiation.PlayerRadsUpdatePacket;
import com.nred.nuclearcraft.recipe.*;
import com.nred.nuclearcraft.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;
import java.util.function.Predicate;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.DamageTypeRegistration.FATAL_RADS;
import static com.nred.nuclearcraft.registration.EntityRegistration.FERAL_GHOUL;
import static com.nred.nuclearcraft.registration.SoundRegistration.chems_wear_off;
import static com.nred.nuclearcraft.registration.SoundRegistration.geiger_tick;

public class RadiationHandler {
    public static final Random RAND = new Random();

    public static final Component RAD_X_WORE_OFF = Component.translatable(MODID + ".message.rad_x_wore_off").withStyle(ChatFormatting.ITALIC);
    public static final Component RAD_WARNING = Component.translatable(MODID + ".message.rad_warning").withStyle(ChatFormatting.GOLD);

    public static Direction blockEntitySide = Direction.DOWN;

//    public static boolean default_rad_immunity = false; TODO Game Stages
//    public static String[] rad_immunity_stages = new String[]{};

    public static final Lazy<BasicRecipeHandler<?>> RADIATION_BLOCK_PURIFICATION = new Lazy<>(() -> NCRecipes.radiation_block_purification);

    @SubscribeEvent
    public void updatePlayerRadiation(PlayerTickEvent.Pre event) {
        if (radiation_enabled_public && !radiation_require_counter && event.getEntity().level().isClientSide()) {
            playGeigerSound(event.getEntity());
        }

        UUID playerUUID = event.getEntity().getUUID();
        if ((event.getEntity().level().getGameTime() + playerUUID.hashCode()) % radiation_player_tick_rate != 0) {
            return;
        }

        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
            IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
            if (playerRads == null) {
                return;
            }

            if (give_guidebook && playerRads.getGiveGuidebook()) {
                boolean success = false;
                if (ModCheck.guidemeLoaded()) {
                    success = player.getInventory().add(guideme.Guides.createGuideItem(ncLoc("guide")));
                } else if (ModCheck.patchouliLoaded()) {
                    success = player.getInventory().add(vazkii.patchouli.api.PatchouliAPI.get().getBookStack(ncLoc("guide")));
                }
                if (success) {
                    playerRads.setGiveGuidebook(false);
                }
            }

            if (!radiation_enabled_public) {
                return;
            }

//            if (ModCheck.gameStagesLoaded()) { TODO add
//                playerRads.setRadiationImmunityStage(default_rad_immunity ^ GameStageHelper.hasAnyOf(player, rad_immunity_stages));
//            }

            String uuidString = playerUUID.toString();
            for (String uuid : radiation_immune_players) {
                if (uuidString.equals(uuid)) {
                    playerRads.setRadiationImmunityStage(true);
                    break;
                }
            }

            if (radiation_player_rads_fatal && !player.isCreative() && !player.isSpectator() && !playerRads.isImmune() && playerRads.isFatal()) {
                player.hurt(player.damageSources().source(FATAL_RADS), Float.MAX_VALUE);
            }

            double previousImmunityTime = playerRads.getRadiationImmunityTime();
            if (previousImmunityTime > 0D) {
                playerRads.setRadiationImmunityTime(previousImmunityTime - radiation_player_tick_rate);
            }
            double previousRadPercentage = playerRads.getRadsPercentage();

            playerRads.setExternalRadiationResistance(RadiationHelper.getArmorInventoryRadResistance(player));

            if (radiation_player_decay_rate > 0D) {
                playerRads.setTotalRads(playerRads.getTotalRads() * Math.pow(1D - radiation_player_decay_rate, radiation_player_tick_rate), false);
            }

            double radiationLevel = RadiationHelper.transferRadsFromInventoryToPlayer(playerRads, player, radiation_player_tick_rate);
            LevelChunk chunk = player.level().getChunk((int) Math.floor(player.position().x) >> 4, (int) Math.floor(player.position().z) >> 4);
            if (chunk.loaded) {
                IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
                radiationLevel += RadiationHelper.transferRadsToPlayer(chunkSource, playerRads, player, radiation_player_tick_rate);
            }

            if (playerRads.getPoisonBuffer() > 0D) {
                double poisonRads = Math.min(playerRads.getPoisonBuffer() / radiation_player_tick_rate, playerRads.getRecentPoisonAddition() / radiation_poison_time);
                radiationLevel += RadiationHelper.addRadsToEntity(playerRads, player, poisonRads, true, true, radiation_player_tick_rate);
                playerRads.setPoisonBuffer(playerRads.getPoisonBuffer() - poisonRads * radiation_player_tick_rate);
                if (playerRads.getPoisonBuffer() == 0D) {
                    playerRads.resetRecentPoisonAddition();
                }
            } else {
                playerRads.resetRecentPoisonAddition();
            }

            playerRads.setMessageCooldownTime(playerRads.getMessageCooldownTime() - radiation_player_tick_rate);

            playerRads.setRadiationLevel(radiationLevel);

            if (!player.isCreative() && !player.isSpectator() && !playerRads.isImmune()) {
                if (radiation_player_rads_fatal && playerRads.isFatal()) {
                    player.hurt(player.damageSources().source(FATAL_RADS), Float.MAX_VALUE);
                } else if (!RadPotionEffects.PLAYER_RAD_LEVEL_LIST.isEmpty() && previousRadPercentage < RadPotionEffects.PLAYER_RAD_LEVEL_LIST.getFirst() && playerRads.getRadsPercentage() >= RadPotionEffects.PLAYER_RAD_LEVEL_LIST.getFirst() && !RadiationHelper.shouldShowHUD(player)) {
                    playerRads.setShouldWarn(true);
                } else {
                    playerRads.setShouldWarn(false);
                }
            }

            double previousInternalResistance = playerRads.getInternalRadiationResistance();
            double recentRadXAdditionModified = radiation_rad_x_amount * (1D + playerRads.getRecentRadXAddition()) / (1D + radiation_rad_x_amount);
            if (previousInternalResistance > 0D) {
                double radXDecayRate = Math.max(previousInternalResistance, recentRadXAdditionModified) / radiation_rad_x_lifetime;
                playerRads.setInternalRadiationResistance(Math.max(0D, previousInternalResistance - radXDecayRate * radiation_player_tick_rate));
                if (playerRads.getInternalRadiationResistance() == 0D) {
                    playerRads.resetRecentRadXAddition();
                    playerRads.setRadXWoreOff(true);
                }
            } else {
                if (previousInternalResistance < 0D) {
                    double radXDecayRate = Math.max(-previousInternalResistance, recentRadXAdditionModified) / radiation_rad_x_lifetime;
                    playerRads.setInternalRadiationResistance(Math.min(0D, previousInternalResistance + radXDecayRate * radiation_player_tick_rate));
                    if (playerRads.getInternalRadiationResistance() == 0D) {
                        playerRads.resetRecentRadXAddition();
                    }
                } else {
                    playerRads.resetRecentRadXAddition();
                }
                playerRads.setRadXWoreOff(false);
            }

            if (playerRads.getRadXWoreOff() && playerRads.getRadXUsed()) {
                playerRads.setRadXUsed(false);
            }

            if (playerRads.getRadawayBuffer(false) > 0D) {
                double change = Math.min(playerRads.getRadawayBuffer(false), playerRads.getRecentRadawayAddition() * radiation_radaway_rate * radiation_player_tick_rate / radiation_radaway_amount);
                playerRads.setTotalRads(playerRads.getTotalRads() - change, false);
                playerRads.setRadawayBuffer(false, playerRads.getRadawayBuffer(false) - change);
                if (playerRads.getRadawayBuffer(false) == 0D) {
                    playerRads.resetRecentRadawayAddition();
                }
            } else {
                playerRads.resetRecentRadawayAddition();
            }

            if (playerRads.getRadawayBuffer(true) > 0D) {
                double change = Math.min(playerRads.getRadawayBuffer(true), radiation_radaway_slow_rate * radiation_player_tick_rate);
                playerRads.setTotalRads(playerRads.getTotalRads() - change, false);
                playerRads.setRadawayBuffer(true, playerRads.getRadawayBuffer(true) - change);
            }

            if (playerRads.getRadawayCooldown() > 0D) {
                playerRads.setRadawayCooldown(playerRads.getRadawayCooldown() - radiation_player_tick_rate);
            }
            if (playerRads.getRadXCooldown() > 0D) {
                playerRads.setRadXCooldown(playerRads.getRadXCooldown() - radiation_player_tick_rate);
            }

            new PlayerRadsUpdatePacket(playerRads).sendTo(player);

            if (!player.isCreative() && !player.isSpectator() && !playerRads.isImmune()) {
                RadiationHelper.applyEntityEffects(player, playerRads, 1D, RadPotionEffects.PLAYER_RAD_LEVEL_LIST, RadPotionEffects.PLAYER_RAD_EFFECT_LISTS, RadPotionEffects.PLAYER_RAD_ATTRIBUTE_MAP);
            }
        } else {
            if (!radiation_enabled_public) {
                return;
            }

            Player player = event.getEntity();
            IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
            if (playerRads == null) {
                return;
            }
            if (playerRads.getRadXWoreOff() && playerRads.getRadXUsed()) {
                player.playSound(chems_wear_off.get(), (float) (0.65D * radiation_sound_volumes[4]), 1F);
                player.sendSystemMessage(RAD_X_WORE_OFF);
            }
            if (playerRads.getShouldWarn()) {
                player.playSound(chems_wear_off.get(), (float) (0.8D * radiation_sound_volumes[6]), 0.7F);
                player.sendSystemMessage(RAD_WARNING);
            }
        }
    }

    @SubscribeEvent
    public void updateLevelRadiation(LevelTickEvent.Pre event) {
        if (!radiation_enabled_public) {
            return;
        }

        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        ServerChunkCache chunkProvider = level.getChunkSource();
        ChunkMap chunkMap = chunkProvider.chunkMap;
        int chunkCount = chunkProvider.getLoadedChunksCount();
        if (chunkCount == 0) {
            return;
        }

        int chunkStart = RAND.nextInt(chunkCount);
        int chunksPerTick = Math.min(radiation_level_chunks_per_tick, chunkCount);
        double tickMult = Math.max(1D, (double) chunkCount / (double) chunksPerTick);

        ResourceKey<Level> dimension = level.dimension();
        ResourceLocation dimensionId = dimension.location();
        BlockPos randomOffsetPos = newRandomOffsetPos(level);

//        ModCheck.cubicChunksLoaded() || TODO
        String randomStructure = RadStructures.STRUCTURE_LIST.isEmpty() ? null : RadStructures.STRUCTURE_LIST.get(RAND.nextInt(RadStructures.STRUCTURE_LIST.size()));

        // Per-tick constants, hoisted out of the per-chunk / per-entity loops.
        boolean hasDimensionRads = RadDimensions.RAD_MAP.containsKey(dimensionId);
        double dimensionRads = hasDimensionRads ? RadDimensions.RAD_MAP.get(dimensionId) : 0D;
        boolean hasDimensionLimit = RadDimensions.LIMIT_MAP.containsKey(dimensionId);
        double dimensionLimit = hasDimensionLimit ? RadDimensions.LIMIT_MAP.get(dimensionId) : 0D;
        boolean biomeRadsAllowed = !RadBiomes.DIM_BLACKLIST.contains(dimensionId);
        double entityDecayMult = Math.pow(1D - radiation_entity_decay_rate, tickMult);
        double radiationDecayMult = Math.pow(1D - radiation_decay_rate, tickMult);

        LevelScratch scratch = SCRATCH.get();
        try {
            // Select this tick's chunks: the window [chunkStart, chunkStart + chunksPerTick) of the chunk holder
            // iteration order, wrapping around to the start. This used to be tested with
            // `(chunkIndex - chunkStart) % chunkCount >= chunksPerTick`, but Java's % is negative for every index
            // before chunkStart, so all of those chunks were processed too: on average half of all loaded chunks
            // every tick instead of radiation_level_chunks_per_tick (thousands of chunks at high view distances).
            List<ChunkHolder> selectedChunks = scratch.selectedChunks;
            int chunkEnd = chunkStart + chunksPerTick;
            boolean wraps = chunkEnd > chunkCount;
            int startSlot = -1;
            int chunkIndex = 0;
            for (ChunkHolder chunk : chunkMap.getChunks()) {
                if (Math.floorMod(chunkIndex - chunkStart, chunkCount) < chunksPerTick) {
                    if (chunkIndex == chunkStart) {
                        startSlot = selectedChunks.size();
                    }
                    selectedChunks.add(chunk);
                }
                if (++chunkIndex >= chunkEnd && !wraps) {
                    break; // Nothing after the window can be selected.
                }
            }

            BiomeManager biomeProvider = level.getBiomeManager().withDifferentSource(scratch.biomeSource);
            scratch.biomeSource.level = level;

            for (int slot = 0, selectedCount = selectedChunks.size(); slot < selectedCount; ++slot) {
                ChunkHolder chunk = selectedChunks.get(slot);
                LevelChunk loadedChunk = chunk.getTickingChunk();
                if (loadedChunk == null || !loadedChunk.loaded) {
                    continue;
                }
                updateChunkRadiation(level, chunk, loadedChunk, scratch, biomeProvider, randomOffsetPos, randomStructure, slot == startSlot, tickMult, entityDecayMult, radiationDecayMult, hasDimensionRads, dimensionRads, hasDimensionLimit, dimensionLimit, biomeRadsAllowed);
            }

            for (int slot = 0, selectedCount = selectedChunks.size(); slot < selectedCount; ++slot) {
                ChunkHolder chunk = selectedChunks.get(slot);
                RadiationHelper.spreadRadiationFromChunk(chunk.getTickingChunk(), getRandomAdjacentChunk(chunkProvider, chunk.getTickingChunk()));
            }
        } finally {
            scratch.clear();
        }

        blockEntitySide = Direction.from3DDataValue(blockEntitySide.ordinal() + 1);
    }

    /**
     * Matches every entity the per-chunk entity loop acts on: the same non-spectator entities
     * {@link Level#getEntities(Entity, AABB)} returns, minus types the loop ignores anyway
     * (part entities, projectiles, item entities when radiation_dropped_items is off, etc.).
     */
    private static final EntityTypeTest<Entity, Entity> ANY_ENTITY = EntityTypeTest.forClass(Entity.class);
    private static final Predicate<Entity> RADIATION_AFFECTED_ENTITY = entity -> !entity.isSpectator() && (entity instanceof LivingEntity || (radiation_dropped_items && entity instanceof ItemEntity));

    private static void updateChunkRadiation(ServerLevel level, ChunkHolder chunk, LevelChunk loadedChunk, LevelScratch scratch, BiomeManager biomeProvider, BlockPos randomOffsetPos, String randomStructure, boolean checkBlocks, double tickMult, double entityDecayMult, double radiationDecayMult, boolean hasDimensionRads, double dimensionRads, boolean hasDimensionLimit, double dimensionLimit, boolean biomeRadsAllowed) {
        IRadiationSource chunkSource = RadiationHelper.getRadiationSource(loadedChunk);
        ChunkPos chunkPos = chunk.getPos();

        List<Entity> entityListArray = scratch.entities;
        entityListArray.clear();
        level.getEntities(ANY_ENTITY, new AABB(chunkPos.getMinBlockX(), loadedChunk.getMinBuildHeight(), chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX(), loadedChunk.getMaxBuildHeight(), chunkPos.getMaxBlockZ()), RADIATION_AFFECTED_ENTITY, entityListArray);
        for (int entityIndex = 0, entityCount = entityListArray.size(); entityIndex < entityCount; ++entityIndex) {
            Entity entity = entityListArray.get(entityIndex);
            if (entity instanceof Player player) {
                RadiationHelper.transferRadsFromInventoryToChunkBuffer(player.getInventory(), chunkSource);
            } else if (radiation_dropped_items && entity instanceof ItemEntity entityItem) {
                RadiationHelper.transferRadiationFromStackToChunkBuffer(entityItem.getItem(), chunkSource, 1D);
            } else if (entity instanceof LivingEntity entityLiving) {
                IEntityRads entityRads = RadiationHelper.getEntityRadiation(entityLiving);
                if (entityRads == null) {
                    continue;
                }

                entityRads.setExternalRadiationResistance(RadiationHelper.getEntityArmorRadResistance(entityLiving));

                if (radiation_entity_decay_rate > 0D) {
                    entityRads.setTotalRads(entityRads.getTotalRads() * entityDecayMult, false);
                }

                RadiationHelper.transferRadsFromSourceToEntity(chunkSource, entityRads, entityLiving, tickMult);

                if (entityRads.getPoisonBuffer() > 0D) {
                    double poisonRads = Math.min(entityRads.getPoisonBuffer(), entityRads.getRecentPoisonAddition() * tickMult / radiation_poison_time);
                    entityRads.setTotalRads(entityRads.getTotalRads() + poisonRads, false);
                    entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() - poisonRads);
                    if (entityRads.getPoisonBuffer() == 0D) {
                        entityRads.resetRecentPoisonAddition();
                    }
                } else {
                    entityRads.resetRecentPoisonAddition();
                }

                if (entityLiving instanceof Mob) {
                    if (radiation_mob_rads_fatal && entityRads.isFatal()) {
                        entityLiving.hurt(level.damageSources().source(FATAL_RADS), Float.MAX_VALUE);
                    } else {
                        RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.MOB_RAD_LEVEL_LIST, RadPotionEffects.MOB_RAD_EFFECT_LISTS, RadPotionEffects.MOB_RAD_ATTRIBUTE_MAP);
                    }
                } else {
                    if (entityRads.isFatal()) {
                        if (entityLiving instanceof Npc) {
                            spawnFeralGhoul(level, entityLiving);
                        } else if (radiation_passive_rads_fatal) {
                            entityLiving.hurt(level.damageSources().source(FATAL_RADS), Float.MAX_VALUE);
                        }
                    } else {
                        RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.ENTITY_RAD_LEVEL_LIST, RadPotionEffects.ENTITY_RAD_EFFECT_LISTS, RadPotionEffects.ENTITY_RAD_ATTRIBUTE_MAP);
                    }
                }
                entityRads.setRadiationLevel(entityRads.getRadiationLevel() * radiationDecayMult);
            }
        }
        entityListArray.clear();

        chunkSource.setScrubbingFraction(0D);
        chunkSource.setEffectiveScrubberCount(0D);

        // Snapshot (into a reused list) so capability lookups that touch the chunk's block entity map can't break iteration.
        List<BlockEntity> blockEntities = scratch.blockEntities;
        blockEntities.clear();
        for (BlockEntity blockEntity : loadedChunk.getBlockEntities().values()) {
            blockEntities.add(blockEntity);
        }
        int blockEntityCount = blockEntities.size();

        if (radiation_block_entities) {
            for (int i = 0; i < blockEntityCount; ++i) {
                RadiationHelper.transferRadiationFromProviderToChunkBuffer(blockEntities.get(i), blockEntitySide, chunkSource);
            }
        }

        if (hasDimensionRads) {
            RadiationHelper.addToSourceBuffer(chunkSource, dimensionRads);
        }

        scratch.biomeSource.setChunk(loadedChunk);
        Biome biome = getBiome(chunkPos.getMinBlockX() + randomOffsetPos.getX(), randomOffsetPos.getY(), chunkPos.getMinBlockZ() + randomOffsetPos.getZ(), scratch.biomePos, biomeProvider);
        if (biome != null && biomeRadsAllowed) {
            if (RadBiomes.RAD_MAP.containsKey(biome)) {
                RadiationHelper.addToSourceBuffer(chunkSource, RadBiomes.RAD_MAP.getDouble(biome));
            }
        }

        // Same coordinates and RAND call order as newRandomPosInChunk, without allocating a BlockPos.
        BlockPos randomChunkPos = scratch.randomChunkPos.set(chunkPos.getBlockX(RAND.nextInt(16)), RAND.nextInt(level.getHeight()), chunkPos.getBlockZ(RAND.nextInt(16)));
        if (randomStructure != null && StructureHelper.CACHE.isInStructure(level, randomStructure, randomChunkPos)) {
            if (RadStructures.RAD_MAP.containsKey(randomStructure)) {
                RadiationHelper.addToSourceBuffer(chunkSource, RadStructures.RAD_MAP.getDouble(randomStructure));
            }
        }

        if (radiation_check_blocks && checkBlocks) {
            int packed = RecipeHelper.pack(StackHelper.blockStateToStack(level.getBlockState(randomChunkPos)));
            if (RadSources.STACK_MAP.containsKey(packed)) {
                RadiationHelper.addToSourceBuffer(chunkSource, RadSources.STACK_MAP.get(packed));
            }
        }

        double currentLevel = chunkSource.getRadiationLevel(), currentBuffer = chunkSource.getRadiationBuffer();
        for (int i = 0; i < blockEntityCount; ++i) {
            if (blockEntities.get(i) instanceof ITileRadiationEnvironment tileRadiationEnvironment) {
                tileRadiationEnvironment.setCurrentChunkRadiationLevel(currentLevel);
                tileRadiationEnvironment.setCurrentChunkRadiationBuffer(currentBuffer);
                RadiationHelper.addScrubbingFractionToChunk(chunkSource, tileRadiationEnvironment);
            }
        }
        blockEntities.clear();

        if (radiation_scrubber_non_linear) {
            double scrubbers = chunkSource.getEffectiveScrubberCount();
            double scrubbingFraction = RadiationHelper.getAltScrubbingFraction(scrubbers);

            RadiationHelper.addToSourceBuffer(chunkSource, -scrubbingFraction * chunkSource.getRadiationBuffer());
            chunkSource.setScrubbingFraction(scrubbingFraction);
        }

        double changeRate = chunkSource.getRadiationLevel() < chunkSource.getRadiationBuffer() ? radiation_spread_rate : radiation_decay_rate * (1D - chunkSource.getScrubbingFraction()) + radiation_spread_rate * chunkSource.getScrubbingFraction();

        double newLevel = Math.max(0D, chunkSource.getRadiationLevel() + (chunkSource.getRadiationBuffer() - chunkSource.getRadiationLevel()) * changeRate);
        if (radiation_chunk_limit >= 0D) {
            newLevel = Math.min(newLevel, radiation_chunk_limit);
        }
        if (biome != null && RadBiomes.LIMIT_MAP.containsKey(biome)) {
            newLevel = Math.min(newLevel, RadBiomes.LIMIT_MAP.getDouble(biome));
        }
        if (hasDimensionLimit) {
            newLevel = Math.min(newLevel, dimensionLimit);
        }

        chunkSource.setRadiationLevel(newLevel);

        mutateTerrain(level, loadedChunk, newLevel);
    }

    /**
     * Reusable per-thread working storage for {@link #updateLevelRadiation}, so the level tick doesn't allocate
     * fresh lists and positions for every chunk it updates. Everything is cleared at the end of each level tick.
     */
    private static final class LevelScratch {
        final ArrayList<ChunkHolder> selectedChunks = new ArrayList<>();
        final ArrayList<Entity> entities = new ArrayList<>();
        final ArrayList<BlockEntity> blockEntities = new ArrayList<>();
        final BlockPos.MutableBlockPos biomePos = new BlockPos.MutableBlockPos();
        final BlockPos.MutableBlockPos randomChunkPos = new BlockPos.MutableBlockPos();
        final ChunkFirstBiomeSource biomeSource = new ChunkFirstBiomeSource();

        void clear() {
            selectedChunks.clear();
            entities.clear();
            blockEntities.clear();
            biomeSource.level = null;
            biomeSource.setChunk(null);
        }
    }

    private static final ThreadLocal<LevelScratch> SCRATCH = ThreadLocal.withInitial(LevelScratch::new);

    /**
     * Biome source that answers lookups inside the chunk being updated directly from that chunk, and passes
     * everything else to the level. {@link Level#getNoiseBiome} gives the same answer for that chunk, but goes
     * through {@link ServerChunkCache#getChunk}, whose 4-entry cache misses on every chunk here and takes the
     * slow future-based path.
     */
    private static final class ChunkFirstBiomeSource implements BiomeManager.NoiseBiomeSource {
        Level level;
        private LevelChunk chunk;
        private int chunkX, chunkZ;

        void setChunk(LevelChunk chunk) {
            this.chunk = chunk;
            if (chunk != null) {
                chunkX = chunk.getPos().x;
                chunkZ = chunk.getPos().z;
            }
        }

        @Override
        public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
            LevelChunk current = chunk;
            if (current != null && QuartPos.toSection(quartX) == chunkX && QuartPos.toSection(quartZ) == chunkZ) {
                return current.getNoiseBiome(quartX, quartY, quartZ);
            }
            return level.getNoiseBiome(quartX, quartY, quartZ);
        }
    }

    private static Biome getBiome(int x, int y, int z, BlockPos.MutableBlockPos pos, BiomeManager biomeProvider) {
        try {
            return biomeProvider.getBiome(pos.set(x, y, z)).value();
        } catch (Exception e) {
            return null;
        }
    }

    public static final List<int[]> ADJACENT_COORDS = Lists.newArrayList(new int[]{1, 0}, new int[]{0, 1}, new int[]{-1, 0}, new int[]{0, -1});

    public static LevelChunk getRandomAdjacentChunk(ServerChunkCache chunkProvider, LevelChunk chunk) {
        if (chunkProvider == null || chunk == null || !chunk.loaded) {
            return null;
        }
        ChunkPos chunkPos = chunk.getPos();
        int x = chunkPos.x;
        int z = chunkPos.z;
        Collections.shuffle(ADJACENT_COORDS);
        for (int[] pos : ADJACENT_COORDS) {
            if (chunkProvider.hasChunk(x + pos[0], z + pos[1])) {
                LevelChunk adjChunk = chunkProvider.getChunkNow(x + pos[0], z + pos[1]);
                if (adjChunk != null) {
                    return adjChunk;
                }
            }
        }
        return null;
    }

    public static BlockPos newRandomOffsetPos(Level level) {
        return new BlockPos(RAND.nextInt(16), RAND.nextInt(level.getHeight()), RAND.nextInt(16));
    }

    public static BlockPos newRandomPosInChunk(Level level, LevelChunk chunk) {
        return chunk.getPos().getBlockAt(RAND.nextInt(16), RAND.nextInt(level.getHeight()), RAND.nextInt(16));
    }

    public static Biome getBiome(LevelChunk chunk, BlockPos randomOffsetPos, BiomeManager biomeProvider) {
        try {
            return biomeProvider.getBiome(chunk.getPos().getWorldPosition().offset(randomOffsetPos)).value();
        } catch (Exception e) {
            return null;
        }
    }

    public static void mutateTerrain(Level level, LevelChunk chunk, double radiation) {
        long j = Math.min(radiation_block_effect_max_rate, (long) Math.log(Math.E - 1D + radiation / RecipeStats.getBlockMutationThreshold()));
        while (j > 0) {
            --j;
            BlockPos randomChunkPos = newRandomPosInChunk(level, chunk);
            BlockState state = level.getBlockState(randomChunkPos);

            ItemStack stack = StackHelper.blockStateToStack(state);
            if (stack != null && !stack.isEmpty()) {
                RecipeInfo<RadiationBlockMutationRecipe> mutationInfo = (RecipeInfo<RadiationBlockMutationRecipe>) RADIATION_BLOCK_PURIFICATION.get().getRecipeInfoFromInputs(level, Lists.newArrayList(stack), Collections.emptyList());
                if (mutationInfo != null && radiation >= mutationInfo.recipe.getBlockMutationThreshold()) {
                    ItemStack output = RecipeHelper.getItemStackFromIngredientList(mutationInfo.recipe.getItemProducts(), 0);
                    if (output != null) {
                        BlockState result = StackHelper.getBlockStateFromStack(output);
                        if (result != null) {
                            level.setBlockAndUpdate(randomChunkPos, result);
                        }
                    }
                }
            }
        }

        j = radiation == 0D ? radiation_block_effect_max_rate : Math.min(radiation_block_effect_max_rate, (long) Math.log(Math.E - 1D + RecipeStats.getBlockPurificationThreshold() / radiation));
        while (j > 0) {
            --j;
            BlockPos randomChunkPos = newRandomPosInChunk(level, chunk);
            BlockState state = level.getBlockState(randomChunkPos);
            ItemStack stack = StackHelper.blockStateToStack(state);
            if (stack != null && !stack.isEmpty()) {
                RecipeInfo<RadiationBlockMutationRecipe> mutationInfo = (RecipeInfo<RadiationBlockMutationRecipe>) RADIATION_BLOCK_PURIFICATION.get().getRecipeInfoFromInputs(level, Lists.newArrayList(stack), Collections.emptyList());
                if (mutationInfo != null && radiation < mutationInfo.recipe.getBlockMutationThreshold()) {
                    ItemStack output = RecipeHelper.getItemStackFromIngredientList(mutationInfo.recipe.getItemProducts(), 0);
                    if (output != null) {
                        BlockState result = StackHelper.getBlockStateFromStack(output);
                        if (result != null) {
                            level.setBlockAndUpdate(randomChunkPos, result);
                        }
                    }
                }
            }
        }
    }

    public static void playGeigerSound(Player player) {
        IEntityRads entityRads = RadiationHelper.getEntityRadiation(player);
        if (entityRads == null || entityRads.isRawRadiationNegligible()) {
            return;
        }

        double radiation = entityRads.getRawRadiationLevel();
        int loops = radiation == 0D ? 0 : Math.min(4, NCMath.toInt(Math.log(Math.E + entityRads.getRawRadiationLevel())));
        if (loops == 0) {
            return;
        }

        double soundChance = Math.cbrt(entityRads.getRawRadiationLevel() / 200D);
        double soundVolume = Mth.clamp(8D * soundChance, 0.55D, 1.1D);
        for (int i = 0; i < loops; ++i) {
            if (RAND.nextDouble() < soundChance) {
                player.playSound(geiger_tick.get(), (float) ((soundVolume + RAND.nextDouble() * 0.12D) * radiation_sound_volumes[0]), 0.92F + RAND.nextFloat() * 0.16F);
            }
        }
    }

    public static void spawnFeralGhoul(Level level, LivingEntity livingEntity) {
        FeralGhoul feralGhoul = new FeralGhoul(FERAL_GHOUL.get(), level);
        feralGhoul.setPos(livingEntity.position());
        feralGhoul.setXRot(livingEntity.getXRot());
        feralGhoul.setYRot(livingEntity.getYRot());
        if (livingEntity instanceof Mob mob)
            feralGhoul.setNoAi(mob.isNoAi());
        if (livingEntity.hasCustomName()) {
            feralGhoul.setCustomName(livingEntity.getCustomName());
            feralGhoul.setCustomNameVisible(livingEntity.isCustomNameVisible());
        }
        level.addFreshEntity(feralGhoul);
        livingEntity.kill();
    }
}
