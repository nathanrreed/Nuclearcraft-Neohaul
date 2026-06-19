package com.nred.nuclearcraft.block.machine;

import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.UnaryOperator;

public class MachineTransparentBlock extends TransparentBlock {
    public MachineTransparentBlock(UnaryOperator<Properties> properties) {
        super(properties.apply(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2.0f, 15.0f).noOcclusion()));
    }
}