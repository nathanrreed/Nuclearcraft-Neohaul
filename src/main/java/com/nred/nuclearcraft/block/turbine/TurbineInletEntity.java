package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.zerono.mods.zerocore.lib.fluid.handler.FluidHandlerForwarder;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.EmptyFluidHandler;

import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class TurbineInletEntity extends AbstractTurbineEntity {
    private final FluidHandlerForwarder _capabilityForwarder;

    public TurbineInletEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("inlet").get(), position, blockState);
        _capabilityForwarder = new FluidHandlerForwarder(EmptyFluidHandler.INSTANCE);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace() || super.isGoodForPosition(position, validatorCallback);
    }

    public IFluidHandler getFluidHandler() {
        Optional<Turbine> controller = this.getMultiblockController();
        if (controller.isPresent()) {
            _capabilityForwarder.setHandler(this.getMultiblockController().get().fluidTankHandler);
        }

        return _capabilityForwarder;
    }
}
