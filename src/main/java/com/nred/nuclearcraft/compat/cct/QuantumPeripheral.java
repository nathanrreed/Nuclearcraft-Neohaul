package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block_entity.quantum.QuantumComputerPortEntity;
import com.nred.nuclearcraft.multiblock.quantum.QuantumState;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jspecify.annotations.Nullable;

import java.util.stream.IntStream;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record QuantumPeripheral(QuantumComputerPortEntity entity) implements IPeripheral {
    private boolean test() {
        return entity.isMachineAssembled() && entity.getMultiblockController().isPresent();
    }

    @LuaFunction(mainThread = true)
    public boolean isComplete() {
        return test();
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfQubits() {
        return test() ? entity.getMultiblockController().get().getQubitCount() : 0;
    }

    @LuaFunction(mainThread = true)
    public int getStateDim() {
        return test() ? entity.getMultiblockController().get().state.dim : 0;
    }

    @LuaFunction(mainThread = true)
    public Object[] getStateVector() {
        if (!test()) {
            return new Object[]{new double[][]{{1D, 0D}}};
        } else {
            QuantumState state = entity.getMultiblockController().get().state;
            double[] vector = state.vector;
            return new Object[]{IntStream.range(0, state.dim).mapToObj(i -> {
                int x = i << 1;
                return new double[]{vector[x], vector[x + 1]};
            }).toArray(double[][]::new)};
        }
    }

    @LuaFunction(mainThread = true)
    public double[] getProbs() {
        return test() ? entity.getMultiblockController().get().state.probs() : new double[]{1D};
    }

    @Override
    public String getType() {
        return ncLoc("quantum").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof QuantumPeripheral && ((QuantumPeripheral) other).entity.equals(entity);
    }
}