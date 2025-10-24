package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.block_entity.fission.FissionMonitorEntity;
import com.nred.nuclearcraft.block_entity.fission.IFissionComponent;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.render.BlockHighlightTracker;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class FissionMonitorBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericDirectionalTooltipDeviceBlock<Controller, PartType> implements IActivatable {
    public FissionMonitorBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING_ALL);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND || player.isCrouching()) {
            return ItemInteractionResult.FAIL;
        }

        if (!MultitoolItem.isMultitool(player.getItemInHand(hand))) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof FissionMonitorEntity monitor) {
                if (!level.isClientSide) {
                    Optional<FissionReactor> reactor = monitor.getMultiblockController();
                    if (reactor.isPresent()) {
                        IFissionComponent component = reactor.get().getPartMap(IFissionComponent.class).get(monitor.getComponentPos().asLong());
                        if (component != null) {
                            FissionCluster cluster = component.getCluster();
                            if (cluster != null) {
                                for (long posLong : cluster.getComponentMap().keySet()) {
                                    BlockHighlightTracker.sendPacket((ServerPlayer) player, posLong, 5000);
                                }
                            }
                        }
                    }
                }
                return ItemInteractionResult.CONSUME;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}