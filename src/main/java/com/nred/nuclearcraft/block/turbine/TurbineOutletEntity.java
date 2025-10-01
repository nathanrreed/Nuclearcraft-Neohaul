package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.IPushFluid;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.zerono.mods.zerocore.lib.fluid.handler.FluidHandlerForwarder;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.EmptyFluidHandler;

import java.util.*;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class TurbineOutletEntity extends AbstractTurbineEntity implements IPushFluid {
    private final FluidHandlerForwarder _capabilityForwarder;

    public TurbineOutletEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("outlet").get(), position, blockState);
        _capabilityForwarder = new FluidHandlerForwarder(EmptyFluidHandler.INSTANCE);
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

    public IFluidHandler getFluidHandler() {
        Optional<Turbine> controller = this.getMultiblockController();
        if (controller.isPresent()) {
            _capabilityForwarder.setHandler(this.getMultiblockController().get().fluidTankHandler);
        }

        return _capabilityForwarder;
    }
}
