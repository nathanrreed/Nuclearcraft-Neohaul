package com.nred.nuclearcraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.nred.nuclearcraft.worldgen.dimension.NCDimensions.WASTELAND_LEVEL_KEY;

public class WastelandPortalBlock extends Block implements Portal {
    public static final MapCodec<EndPortalBlock> CODEC = simpleCodec(EndPortalBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public WastelandPortalBlock() {
        super(BlockBehaviour.Properties.of().strength(-1, 6000000F).noCollission().noLootTable().randomTicks().pushReaction(PushReaction.BLOCK));
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of();
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double d0 = (double) pos.getX() + random.nextDouble();
        double d1 = (double) pos.getY() + 0.8;
        double d2 = (double) pos.getZ() + random.nextDouble();
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
    }

    @Override
    public @Nullable DimensionTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        ResourceKey<Level> resourcekey = currentLevel.dimension() == WASTELAND_LEVEL_KEY ? Level.OVERWORLD : WASTELAND_LEVEL_KEY;
        ServerLevel newLevel = currentLevel.getServer().getLevel(resourcekey);
        if (newLevel == null) {
            return null;
        } else {
            WorldBorder worldborder = newLevel.getWorldBorder();
            double teleportationScale = DimensionType.getTeleportationScale(currentLevel.dimensionType(), newLevel.dimensionType());
            BlockPos.MutableBlockPos columnPos = worldborder.clampToBounds(entity.getX() * teleportationScale, entity.getY(), entity.getZ() * teleportationScale).mutable();

            int maxPlaceableY = Math.min(newLevel.getMaxBuildHeight(), newLevel.getMinBuildHeight() + newLevel.getLogicalHeight() - 1);
            int height = Math.min(maxPlaceableY, newLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, columnPos.getX(), columnPos.getZ()));

            while (this.canPortalReplaceBlock(columnPos.below(), newLevel)) {
                height--;
                columnPos.setY(height);
            }

            for (int y = height; y < newLevel.getMaxBuildHeight(); y++) { // Finds the first open space above the land
                columnPos.setY(y);
                if (this.canPortalReplaceBlock(columnPos, newLevel) && this.canPortalReplaceBlock(columnPos.relative(Direction.UP), newLevel)) {
                    break;
                }
            }

            return new DimensionTransition(
                    newLevel,
                    columnPos.getCenter(),
                    entity.getDeltaMovement(),
                    entity.getYRot(),
                    entity.getXRot(),
                    DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
        }
    }

    private boolean canPortalReplaceBlock(BlockPos pos, Level level) {
        BlockState blockState = level.getBlockState(pos);
        return blockState.canBeReplaced() && blockState.getFluidState().isEmpty();
    }
}