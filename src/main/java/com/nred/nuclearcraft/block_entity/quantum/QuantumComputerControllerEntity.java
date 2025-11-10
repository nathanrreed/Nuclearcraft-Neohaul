package com.nred.nuclearcraft.block_entity.quantum;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.multiblock.quantum.QuantumOperationWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.QUANTUM_ENTITY_TYPE;

public class QuantumComputerControllerEntity extends AbstractQuantumComputerEntity implements ITickable {
    public boolean pulsed = false;

    public QuantumComputerControllerEntity(final BlockPos position, final BlockState blockState) {
        super(QUANTUM_ENTITY_TYPE.get("quantum_computer_controller").get(), position, blockState);
    }

    @Override
    public int[] weakSidesToCheck(Level worldIn, BlockPos posIn) {
        return new int[]{2, 3, 4, 5};
    }

    @Override
    public void update() {
        if (!pulsed && getIsRedstonePowered()) {
            queueReset();
            pulsed = true;
        } else if (pulsed && !getIsRedstonePowered()) {
            pulsed = false;
        }
    }

    public final void queueReset() {
        if (isMachineAssembled()) {
            getMultiblockController().ifPresent(quantumComputer -> quantumComputer.queue.add(new QuantumOperationWrapper.Reset(getMultiblockController().get())));
        }
    }

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putBoolean("pulsed", pulsed);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        pulsed = nbt.getBoolean("pulsed");
    }
}