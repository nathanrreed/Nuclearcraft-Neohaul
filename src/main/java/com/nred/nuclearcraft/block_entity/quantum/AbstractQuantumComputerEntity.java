package com.nred.nuclearcraft.block_entity.quantum;


import com.nred.nuclearcraft.block_entity.IMultitoolLogic;
import com.nred.nuclearcraft.block_entity.TilePartAbstract;
import com.nred.nuclearcraft.multiblock.quantum.IQuantumComputerPartType;
import com.nred.nuclearcraft.multiblock.quantum.QuantumComputer;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartTypeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractQuantumComputerEntity extends TilePartAbstract<QuantumComputer> implements IMultiblockPartTypeProvider<QuantumComputer, IQuantumComputerPartType>, IQuantumComputerPart, IMultitoolLogic {
    public AbstractQuantumComputerEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
    }

    @Override
    public @NotNull QuantumComputer createController() {
        final Level myWorld = this.getLevel();

        if (null == myWorld) {
            throw new RuntimeException("Trying to create a Controller from a Part without a Level");
        }

        return new QuantumComputer(this.getLevel());
    }

    @Override
    public @NotNull Class<QuantumComputer> getControllerType() {
        return QuantumComputer.class;
    }
}