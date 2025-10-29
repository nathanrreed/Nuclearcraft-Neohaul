package com.nred.nuclearcraft.block_entity;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IRayTraceLogic extends ITile {
    @OnlyIn(Dist.CLIENT)
    void onPlayerMouseOver(ServerPlayer player, Direction side, float partialTicks);
}