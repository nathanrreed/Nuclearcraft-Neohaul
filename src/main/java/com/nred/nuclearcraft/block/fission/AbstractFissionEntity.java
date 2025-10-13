package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.ITile;
import com.nred.nuclearcraft.block.TilePartAbstract;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartTypeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractFissionEntity extends TilePartAbstract<FissionReactor> implements IMultiblockPartTypeProvider<FissionReactor, IFissionPartType>, IFissionPart, ITile {
    public AbstractFissionEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
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
}