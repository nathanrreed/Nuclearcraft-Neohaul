package com.nred.nuclearcraft.block_entity.generator;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.dummy.IInterfaceable;
import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.energy.TileEnergy;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TilePassiveGenerator extends TileEnergy implements ITickable, IInterfaceable {

    public final int power;

    public TilePassiveGenerator(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int maxPowerGen) {
        super(type, pos, blockState, 4L * maxPowerGen, 4 * maxPowerGen, ITileEnergy.energyConnectionAll(EnergyConnection.OUT));
        power = maxPowerGen;
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            getEnergyStorage().changeEnergyStored(getGenerated());
            pushEnergy();
        }
    }

    public abstract long getGenerated();
}