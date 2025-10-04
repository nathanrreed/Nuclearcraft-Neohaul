package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartTypeProvider;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractFissionEntity extends AbstractCuboidMultiblockPart<FissionReactor> implements IMultiblockPartTypeProvider<FissionReactor, IFissionPartType>, IFissionPart {
    public AbstractFissionEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        // Most Reactor parts are not allowed on the Frame an inside the Reactor so reject those positions and allow all the other ones

        final BlockPos coordinates = this.getWorldPosition();

        if (position.isFrame()) {
            validatorCallback.setLastError(coordinates, NuclearcraftNeohaul.MODID + ".multiblock.validation.reactor.invalid_frame_block");
            return false;
        } else if (PartPosition.Interior == position) {
            validatorCallback.setLastError(coordinates, NuclearcraftNeohaul.MODID + ".multiblock.validation.reactor.invalid_part_for_interior");
            return false;
        }

        return true;
    }

    @Override
    public FissionReactor createController() {
        final Level myWorld = this.getLevel();

        if (null == myWorld) {
            throw new RuntimeException("Trying to create a Controller from a Part without a Level");
        }

        return new FissionReactor(this.getLevel());
    }

    @Override
    public Class<FissionReactor> getControllerType() {
        return FissionReactor.class;
    }

    @Override
    public void onMachineActivated() {
    }

    @Override
    public void onMachineDeactivated() {
    }
}