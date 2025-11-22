package com.nred.nuclearcraft.radiation;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.entity.FeralGhoul;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.recipe.*;
import com.nred.nuclearcraft.payload.radiation.PlayerRadsUpdatePacket;
import com.nred.nuclearcraft.util.Lazy;
import com.nred.nuclearcraft.util.ModCheck;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.StackHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    public static Direction tile_side = Direction.DOWN;

    public static boolean default_rad_immunity = false;
    public static String[] rad_immunity_stages = new String[]{};

    public static final Lazy<BasicRecipeHandler<?>> RADIATION_BLOCK_PURIFICATION = new Lazy<>(() -> NCRecipes.radiation_block_purification);

    @SubscribeEvent
    public void updatePlayerRadiation(PlayerTickEvent event) {
        if (radiation_enabled_public && !radiation_require_counter && event instanceof PlayerTickEvent.Pre) {
            playGeigerSound(event.getEntity());
        }

        UUID playerUUID = event.getEntity().getUUID();
        if (event instanceof PlayerTickEvent.Post || (event.getEntity().level().getGameTime() + playerUUID.hashCode()) % radiation_player_tick_rate != 0) {
            return;
        }

        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
            IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
            if (playerRads == null) {
                return;
            }

            if (give_guidebook && ModCheck.patchouliLoaded() && playerRads.getGiveGuidebook()) {
                boolean success = player.getInventory().add(PatchouliAPI.get().getBookStack(ncLoc("guide")));
                if (success) {
                    playerRads.setGiveGuidebook(false);
                }
            }

            if (!radiation_enabled_public) {
                return;
            }

//            if (ModCheck.gameStagesLoaded()) { TODO
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
                } else if (!RadPotionEffects.PLAYER_RAD_LEVEL_LIST.isEmpty() && previousRadPercentage < RadPotionEffects.PLAYER_RAD_LEVEL_LIST.get(0) && playerRads.getRadsPercentage() >= RadPotionEffects.PLAYER_RAD_LEVEL_LIST.get(0) && !RadiationHelper.shouldShowHUD(player)) {
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

//    @SubscribeEvent TODO
//    @OnlyIn(Dist.DEDICATED_SERVER)
//    public void updateLevelRadiation(LevelTickEvent.Pre event) {
//        if (!radiation_enabled_public) {
//            return;
//        }
//
//        if (!(event.getLevel() instanceof ServerLevel level)) {
//            return;
//        }
//
//        ServerChunkCache chunkProvider = level.getChunkSource();
//        Collection<LevelChunk> chunks = chunkProvider.getLoadedChunksCount();
//        if (chunks.isEmpty()) {
//            return;
//        }
//
//        int chunkCount = chunkProvider.getLoadedChunksCount();
//        int chunkStart = RAND.nextInt(chunkCount);
//        int chunksPerTick = Math.min(radiation_level_chunks_per_tick, chunkCount);
//        double tickMult = Math.max(1D, (double) chunkCount / (double) chunksPerTick);
//
//        ResourceKey<Level> dimension = level.dimension();
//        BlockPos randomOffsetPos = newRandomOffsetPos(level);
//        BiomeManager biomeProvider = level.getBiomeManager();
//        String randomStructure = ModCheck.cubicChunksLoaded() || RadStructures.STRUCTURE_LIST.isEmpty() ? null : RadStructures.STRUCTURE_LIST.get(RAND.nextInt(RadStructures.STRUCTURE_LIST.size()));
//
//        int chunkIndex = -1;
//        for (LevelChunk chunk : chunks) {
//            ++chunkIndex;
//
//            if ((chunkIndex - chunkStart) % chunkCount >= chunksPerTick) {
//                continue;
//            }
//
//            if (!chunk.loaded) {
//                continue;
//            }
//
//            IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
//            if (chunkSource == null) {
//                continue;
//            }
//
//            ClassInheritanceMultiMap<Entity>[] entityListArray = chunk.getEntityLists();
//            for (ClassInheritanceMultiMap<Entity> entities : entityListArray) {
//                Entity[] entityArray = entities.toArray(new Entity[0]);
//                for (Entity entity : entityArray) {
//                    if (entity instanceof EntityPlayer player) {
//                        RadiationHelper.transferRadsFromInventoryToChunkBuffer(player.inventory, chunkSource);
//                    } else if (radiation_dropped_items && entity instanceof EntityItem entityItem) {
//                        RadiationHelper.transferRadiationFromStackToChunkBuffer(entityItem.getItem(), chunkSource, 1D);
//                    } else if (entity instanceof LivingEntity entityLiving) {
//                        IEntityRads entityRads = RadiationHelper.getEntityRadiation(entityLiving);
//                        if (entityRads == null) {
//                            continue;
//                        }
//
//                        entityRads.setExternalRadiationResistance(RadiationHelper.getEntityArmorRadResistance(entityLiving));
//
//                        if (radiation_entity_decay_rate > 0D) {
//                            entityRads.setTotalRads(entityRads.getTotalRads() * Math.pow(1D - radiation_entity_decay_rate, tickMult), false);
//                        }
//
//                        RadiationHelper.transferRadsFromSourceToEntity(chunkSource, entityRads, entityLiving, tickMult);
//
//                        if (entityRads.getPoisonBuffer() > 0D) {
//                            double poisonRads = Math.min(entityRads.getPoisonBuffer(), entityRads.getRecentPoisonAddition() * tickMult / radiation_poison_time);
//                            entityRads.setTotalRads(entityRads.getTotalRads() + poisonRads, false);
//                            entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() - poisonRads);
//                            if (entityRads.getPoisonBuffer() == 0D) {
//                                entityRads.resetRecentPoisonAddition();
//                            }
//                        } else {
//                            entityRads.resetRecentPoisonAddition();
//                        }
//
//                        if (entityLiving instanceof Mob) {
//                            if (radiation_mob_rads_fatal && entityRads.isFatal()) {
//                                entityLiving.hurt(level.damageSources().source(FATAL_RADS), Float.MAX_VALUE);
//                            } else {
//                                RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.MOB_RAD_LEVEL_LIST, RadPotionEffects.MOB_RAD_EFFECT_LISTS, RadPotionEffects.MOB_RAD_ATTRIBUTE_MAP);
//                            }
//                        } else {
//                            if (entityRads.isFatal()) {
//                                if (register_entity[0] && entityLiving instanceof INpc) {
//                                    spawnFeralGhoul(level, entityLiving);
//                                } else if (radiation_passive_rads_fatal) {
//                                    entityLiving.hurt(level.damageSources().source(FATAL_RADS), Float.MAX_VALUE);
//                                }
//                            } else {
//                                RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.ENTITY_RAD_LEVEL_LIST, RadPotionEffects.ENTITY_RAD_EFFECT_LISTS, RadPotionEffects.ENTITY_RAD_ATTRIBUTE_MAP);
//                            }
//                        }
//                        entityRads.setRadiationLevel(entityRads.getRadiationLevel() * Math.pow(1D - radiation_decay_rate, tickMult));
//                    }
//                }
//            }
//
//            chunkSource.setScrubbingFraction(0D);
//            chunkSource.setEffectiveScrubberCount(0D);
//
//            Collection<BlockEntity> tiles = chunk.getTileEntityMap().values();
//
//            if (radiation_tile_entities) {
//                for (BlockEntity tile : tiles) {
//                    RadiationHelper.transferRadiationFromProviderToChunkBuffer(tile, tile_side, chunkSource);
//                }
//            }
//
//            if (RadWorlds.RAD_MAP.containsKey(dimension)) {
//                RadiationHelper.addToSourceBuffer(chunkSource, RadWorlds.RAD_MAP.get(dimension));
//            }
//
//            Biome biome = getBiome(chunk, randomOffsetPos, biomeProvider);
//            if (biome != null && !RadBiomes.DIM_BLACKLIST.contains(dimension)) {
//                Double biomeRadiation = RadBiomes.RAD_MAP.get(biome);
//                if (biomeRadiation != null) {
//                    RadiationHelper.addToSourceBuffer(chunkSource, biomeRadiation);
//                }
//            }
//
//            BlockPos randomChunkPos = newRandomPosInChunk(level, chunk);
//            if (randomStructure != null && StructureHelper.CACHE.isInStructure(level, randomStructure, randomChunkPos)) {
//                Double structureRadiation = RadStructures.RAD_MAP.get(randomStructure);
//                if (structureRadiation != null) {
//                    RadiationHelper.addToSourceBuffer(chunkSource, structureRadiation);
//                }
//            }
//
//            if (radiation_check_blocks && chunkIndex == chunkStart) {
//                int packed = RecipeHelper.pack(StackHelper.blockStateToStack(level.getBlockState(randomChunkPos)));
//                if (RadSources.STACK_MAP.containsKey(packed)) {
//                    RadiationHelper.addToSourceBuffer(chunkSource, RadSources.STACK_MAP.get(packed));
//                }
//            }
//
//            double currentLevel = chunkSource.getRadiationLevel(), currentBuffer = chunkSource.getRadiationBuffer();
//            for (BlockEntity tile : tiles) {
//                if (tile instanceof ITileRadiationEnvironment tileRadiationEnvironment) {
//                    tileRadiationEnvironment.setCurrentChunkRadiationLevel(currentLevel);
//                    tileRadiationEnvironment.setCurrentChunkRadiationBuffer(currentBuffer);
//                    RadiationHelper.addScrubbingFractionToChunk(RadiationHelper.getRadiationSource(chunk), tileRadiationEnvironment);
//                }
//            }
//
//            if (radiation_scrubber_non_linear) {
//                double scrubbers = chunkSource.getEffectiveScrubberCount();
//                double scrubbingFraction = RadiationHelper.getAltScrubbingFraction(scrubbers);
//
//                RadiationHelper.addToSourceBuffer(chunkSource, -scrubbingFraction * chunkSource.getRadiationBuffer());
//                chunkSource.setScrubbingFraction(scrubbingFraction);
//            }
//
//            double changeRate = chunkSource.getRadiationLevel() < chunkSource.getRadiationBuffer() ? radiation_spread_rate : radiation_decay_rate * (1D - chunkSource.getScrubbingFraction()) + radiation_spread_rate * chunkSource.getScrubbingFraction();
//
//            double newLevel = Math.max(0D, chunkSource.getRadiationLevel() + (chunkSource.getRadiationBuffer() - chunkSource.getRadiationLevel()) * changeRate);
//            if (radiation_chunk_limit >= 0D) {
//                newLevel = Math.min(newLevel, radiation_chunk_limit);
//            }
//            if (biome != null && RadBiomes.LIMIT_MAP.containsKey(biome)) {
//                newLevel = Math.min(newLevel, RadBiomes.LIMIT_MAP.get(biome));
//            }
//            if (RadWorlds.LIMIT_MAP.containsKey(dimension)) {
//                newLevel = Math.min(newLevel, RadWorlds.LIMIT_MAP.get(dimension));
//            }
//
//            chunkSource.setRadiationLevel(newLevel);
//
//            mutateTerrain(level, chunk, newLevel);
//        }
//
//        chunkIndex = -1;
//        for (LevelChunk chunk : chunks) {
//            ++chunkIndex;
//
//            if ((chunkIndex - chunkStart) % chunkCount >= chunksPerTick) {
//                continue;
//            }
//
//            RadiationHelper.spreadRadiationFromChunk(chunk, getRandomAdjacentChunk(chunkProvider, chunk));
//        }
//
//        tile_side = Direction.from3DDataValue(tile_side.ordinal() + 1);
//    }

    public static final List<int[]> ADJACENT_COORDS = Lists.newArrayList(new int[]{1, 0}, new int[]{0, 1}, new int[]{-1, 0}, new int[]{0, -1});

//    public static LevelChunk getRandomAdjacentChunk(ServerChunkCache chunkProvider, LevelChunk chunk) {
//        if (chunkProvider == null || chunk == null || !chunk.loaded) {
//            return null;
//        }
//        ChunkPos chunkPos = chunk.getPos();
//        int x = chunkPos.x;
//        int z = chunkPos.z;
//        Collections.shuffle(ADJACENT_COORDS);
//        for (int[] pos : ADJACENT_COORDS) {
//            if (chunkProvider.hasChunk(x + pos[0], z + pos[1])) {
//                LevelChunk adjChunk = chunkProvider.getLoadedChunk(x + pos[0], z + pos[1]);
//                if (adjChunk != null) {
//                    return adjChunk;
//                }
//            }
//        }
//        return null;
//    }

    public static BlockPos newRandomOffsetPos(Level level) {
        return new BlockPos(RAND.nextInt(16), RAND.nextInt(level.getHeight()), RAND.nextInt(16));
    }

    public static BlockPos newRandomPosInChunk(Level level, LevelChunk chunk) {
        return chunk.getPos().getBlockAt(RAND.nextInt(16), RAND.nextInt(level.getHeight()), RAND.nextInt(16));
    }

    public static Biome getBiome(LevelChunk chunk, BlockPos randomOffsetPos, BiomeManager biomeProvider) {
        try {
            return biomeProvider.getBiome(randomOffsetPos).value();
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
