package com.nred.nuclearcraft.block.radiation;

import com.nred.nuclearcraft.block.tile.BlockSimpleTile;
import com.nred.nuclearcraft.block_entity.radiation.GeigerCounterEntity;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.radiation_lowest_rate;

public class GeigerCounterBlock extends BlockSimpleTile<GeigerCounterEntity> {
    public GeigerCounterBlock() {
        super("geiger_block");
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (player.getItemInHand(hand).isEmpty()) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (!level.isClientSide() && tile instanceof GeigerCounterEntity geiger) {
                double radiation = geiger.getChunkRadiationLevel();
                player.sendSystemMessage(Component.translatable(MODID + ".tooltip.geiger_counter.rads", RadiationHelper.getRadiationTextColor(radiation) + (radiation < radiation_lowest_rate ? "0 Rad/t" : RadiationHelper.radsPrefix(radiation, true))));
            }
            return ItemInteractionResult.CONSUME;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof GeigerCounterEntity geigerCounter) {
            return geigerCounter.comparatorStrength;
        }
        return 0;
    }
}