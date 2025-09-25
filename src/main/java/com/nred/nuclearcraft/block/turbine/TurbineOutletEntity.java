package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.IPushFluid;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_OUTLET;

public class TurbineOutletEntity extends AbstractTurbineEntity implements IPushFluid {
    public TurbineOutletEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_OUTLET.get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace() || super.isGoodForPosition(position, validatorCallback);
    }

    Map<Direction, BlockCapabilityCache<IFluidHandler, Direction>> capCache = new HashMap<>();

    @Override
    public Map<Direction, BlockCapabilityCache<IFluidHandler, Direction>> getCapCache() {
        return capCache;
    }

    public void onCapInvalidate() {

    }

    public void update() {
        pushFluid(level, getMultiblockController().get().fluidTankHandler.get(1), worldPosition, this);
    }

    @Override
    public Collection<Direction> getDirections() {
        Direction flow = getMultiblockController().get().flowDir;
        return List.of(flow, flow.getOpposite());
    }
}
