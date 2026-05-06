package com.nred.nuclearcraft.block.machine;

import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MachineTransparentBlock extends TransparentBlock {
    public MachineTransparentBlock() {
        super(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2.0f, 15.0f).noOcclusion());
    }
}