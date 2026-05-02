package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block_entity.fission.FissionCoolerEntity;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import com.nred.nuclearcraft.util.FluidStackHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class FissionCoolerBlock extends GenericTooltipDeviceBlock<FissionReactor, IFissionPartType> {
    public FissionCoolerBlock(MultiblockPartProperties<IFissionPartType> properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND || player.isCrouching()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof FissionCoolerEntity cooler) {
            FissionReactor reactor = cooler.getMultiblockController().orElse(null);
            if (reactor != null) {
                FluidStack fluidStack = FluidStackHelper.getFluid(player.getItemInHand(hand));
                if (cooler.canModifyFilter(0) && cooler.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(cooler.getFilterTanks().get(0).getFluid(), fluidStack) && cooler.isFluidValidForTank(0, fluidStack)) {
                    player.sendSystemMessage(Component.translatable(MODID + ".message.filter", fluidStack.getHoverName().copy().withStyle(ChatFormatting.BOLD)));

                    FluidStack filter = fluidStack.copy();
                    filter.setAmount(1000);
                    cooler.getFilterTanks().get(0).setFluid(filter);
                    cooler.onFilterChanged(0);
                    return ItemInteractionResult.CONSUME;
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}