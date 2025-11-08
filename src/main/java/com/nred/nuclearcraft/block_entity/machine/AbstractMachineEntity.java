package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.block_entity.TilePartAbstract;
import com.nred.nuclearcraft.multiblock.machine.IMachinePartType;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartTypeProvider;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMachineEntity extends TilePartAbstract<Machine> implements IMultiblockPartTypeProvider<Machine, IMachinePartType>, IMachinePart {
    public AbstractMachineEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
    }

    @Override
    public @NotNull Machine createController() {
        final Level myWorld = this.getLevel();

        if (null == myWorld) {
            throw new RuntimeException("Trying to create a Controller from a Part without a Level");
        }

        return new Machine(this.getLevel());
    }

//    @Override TODO
//    protected @Nullable IRadiationSource getMultiblockRadiationSourceInternal() {
//        Machine machine = getMultiblock();
//        return machine == null ? null : machine.radiation;
//    }

    public boolean isTransparent() {
        return false;
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
//        if (position.isFace()) { TODO add if needed
//            if (isTransparent() && getMultiblockController().isPresent()) {
//                getMultiblockController().get().shouldSpecialRenderRotor = true;
//            }
//        }
        return super.isGoodForPosition(position, validatorCallback);
    }

    @Override
    public @NotNull Class<Machine> getControllerType() {
        return Machine.class;
    }
}