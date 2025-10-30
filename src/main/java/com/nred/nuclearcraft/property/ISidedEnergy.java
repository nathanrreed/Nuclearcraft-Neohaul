package com.nred.nuclearcraft.property;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public interface ISidedEnergy {
    SidedEnumProperty<EnergyConnection> ENERGY_DOWN = energySide("down", Direction.DOWN);
    SidedEnumProperty<EnergyConnection> ENERGY_UP = energySide("up", Direction.UP);
    SidedEnumProperty<EnergyConnection> ENERGY_NORTH = energySide("north", Direction.NORTH);
    SidedEnumProperty<EnergyConnection> ENERGY_SOUTH = energySide("south", Direction.SOUTH);
    SidedEnumProperty<EnergyConnection> ENERGY_WEST = energySide("west", Direction.WEST);
    SidedEnumProperty<EnergyConnection> ENERGY_EAST = energySide("east", Direction.EAST);

    static SidedEnumProperty<EnergyConnection> energySide(String name, Direction facing) {
        return SidedEnumProperty.create(name, EnergyConnection.class, new EnergyConnection[]{EnergyConnection.IN, EnergyConnection.OUT, EnergyConnection.NON}, facing);
    }

    default void createEnergyBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENERGY_DOWN, ENERGY_UP, ENERGY_NORTH, ENERGY_SOUTH, ENERGY_WEST, ENERGY_EAST);
    }
}