package com.nred.nuclearcraft.block_entity.distributor;

import com.nred.nuclearcraft.block_entity.multiblock.AbstractPartBlockEntity;
import com.nred.nuclearcraft.multiblock.distributor.Distributor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartTypeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractDistributorEntity extends AbstractPartBlockEntity<Distributor> implements IMultiblockPartTypeProvider<FissionReactor, IFissionPartType> {
    public AbstractDistributorEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
    }

    @Override
    public Distributor createController() {
        final Level myWorld = this.getLevel();

        if (null == myWorld) {
            throw new RuntimeException("Trying to create a Controller from a Part without a Level");
        }

        return new Distributor(this.getLevel());
    }

    @Override
    public Class<Distributor> getControllerType() {
        return Distributor.class;
    }
}
