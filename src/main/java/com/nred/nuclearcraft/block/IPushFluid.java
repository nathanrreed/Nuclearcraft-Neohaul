package com.nred.nuclearcraft.block;

import com.nred.nuclearcraft.block.internal.fluid.Tank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IPushFluid extends IPush {
    default void pushFluid(Level level, Tank tank, BlockPos pos, BlockEntity be) {
        if (!level.isClientSide && tank.getFluidAmount() > 0) {
            for (Direction dir : getDirections()) {
                if (getCapCache().get(dir) == null) {
                    getCapCache().put(dir, BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, ((ServerLevel) level), pos.relative(dir), dir.getOpposite(), () -> !be.isRemoved(), this::onCapInvalidate));
                }
                if (getCapCache().get(dir).getCapability() != null) {
                    @Nullable IFluidHandler cap = getCapCache().get(dir).getCapability();
                    FluidStack internal_simulate = tank.drain(tank.getFluidAmount(), IFluidHandler.FluidAction.SIMULATE);
                    int amount = cap.fill(internal_simulate, IFluidHandler.FluidAction.SIMULATE);

                    FluidStack internal = tank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                    tank.fill(internal.copyWithAmount(internal.getAmount() - cap.fill(internal, IFluidHandler.FluidAction.EXECUTE)), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    Map<Direction, BlockCapabilityCache<IFluidHandler, Direction>> getCapCache();
}
