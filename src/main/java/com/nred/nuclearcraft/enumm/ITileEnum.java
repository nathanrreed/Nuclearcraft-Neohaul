package com.nred.nuclearcraft.enumm;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface ITileEnum<T extends BlockEntity> {

    Class<? extends T> getTileClass();
}
