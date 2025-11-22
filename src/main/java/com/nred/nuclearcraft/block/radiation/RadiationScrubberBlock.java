package com.nred.nuclearcraft.block.radiation;

import com.nred.nuclearcraft.block.tile.BlockTile;
import com.nred.nuclearcraft.block_entity.radiation.RadiationScrubberEntity;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.radiation_lowest_rate;

public class RadiationScrubberBlock extends BlockTile {
    public RadiationScrubberBlock() {
        super(Properties.ofFullCopy(Blocks.IRON_BLOCK));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RadiationScrubberEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (player.isCrouching() && player.getItemInHand(hand).isEmpty()) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (!level.isClientSide() && tile instanceof RadiationScrubberEntity scrubber) {
                scrubber.checkRadiationEnvironmentInfo();
                double radRemoval = scrubber.getRawScrubberRate();
                player.sendSystemMessage(Component.translatable(MODID + ".message.scrubber_removal_rate", (Math.abs(radRemoval) < radiation_lowest_rate ? "0 Rad/t" : RadiationHelper.radsPrefix(radRemoval, true)) + " [" + NCMath.pcDecimalPlaces(Math.abs(scrubber.getRadiationContributionFraction() / RadiationScrubberEntity.getMaxScrubberFraction()), 1) + "]"));
            }
            return ItemInteractionResult.CONSUME;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (level1, pos, state1, blockEntity) -> ((RadiationScrubberEntity) blockEntity).update();
    }
}