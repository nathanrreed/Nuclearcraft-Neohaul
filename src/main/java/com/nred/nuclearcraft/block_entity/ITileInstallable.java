package com.nred.nuclearcraft.block_entity;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public interface ITileInstallable extends ITile {
    boolean tryInstall(Player player, InteractionHand hand, Direction facing);
}