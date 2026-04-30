package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.block_entity.internal.fluid.TankVoid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ChemicalUniversalBinEntity extends UniversalBinEntity {
    public ChemicalUniversalBinEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    protected TankVoid createTank(){
        return new com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTankVoid();
    }
}