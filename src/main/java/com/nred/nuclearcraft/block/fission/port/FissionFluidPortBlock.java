package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block_entity.fission.port.FissionFluidPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.IFissionPortTarget;
import com.nred.nuclearcraft.block_entity.fission.port.ITileFilteredFluid;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import com.nred.nuclearcraft.util.FluidStackHelper;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import net.minecraft.ChatFormatting;
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
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class FissionFluidPortBlock<PORT extends FissionFluidPortEntity<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET> & ITileFilteredFluid> extends FissionPortBlock<PORT, TARGET> {
    public FissionFluidPortBlock(MultiblockPartBlock.MultiblockPartProperties<IFissionPartType> properties, Class<PORT> portClass) {
        super(properties, portClass);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND || player.isCrouching()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (portClass.isInstance(tile)) {
                PORT port = portClass.cast(tile);
                Optional<FissionReactor> reactor = port.getMultiblockController();
                if (reactor.isPresent()) {
                    FluidStack fluidStack = FluidStackHelper.getFluid(player.getItemInHand(hand));
                    if (port.canModifyFilter(0) && port.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(port.getFilterTanks().get(0).getFluid(), fluidStack) && port.isFluidValidForTank(0, fluidStack)) {
                        player.sendSystemMessage(Component.translatable(MODID + ".message.filter", Component.translatable(fluidStack.getDescriptionId()).withStyle(ChatFormatting.BOLD)));
                        FluidStack filter = fluidStack.copy();
                        filter.setAmount(1000);
                        port.getFilterTanks().get(0).setFluid(filter);
                        port.onFilterChanged(0);
                        return ItemInteractionResult.CONSUME;
                    }
                }
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}