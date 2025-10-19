package com.nred.nuclearcraft.block.fission.manager;

import com.nred.nuclearcraft.block_entity.fission.FissionSourceEntity;
import com.nred.nuclearcraft.block_entity.fission.manager.FissionSourceManagerEntity;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class FissionSourceManagerBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends BlockFissionManager<Controller, PartType, FissionSourceManagerEntity, FissionSourceEntity> {
    public FissionSourceManagerBlock(MultiblockPartProperties<PartType> properties) {
        super(properties, FissionSourceManagerEntity.class);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side) {
        return side != null;
    }
}