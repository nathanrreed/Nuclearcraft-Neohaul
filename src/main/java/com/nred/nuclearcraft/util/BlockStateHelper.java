package com.nred.nuclearcraft.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class BlockStateHelper {
    public static boolean isEmpty(BlockState state) {
        return state.canBeReplaced() || state.isAir();
    }

    public static boolean isReplaceable(BlockState state) {
        return isEmpty(state) || state.getPistonPushReaction().equals(PushReaction.DESTROY);
    }
}
