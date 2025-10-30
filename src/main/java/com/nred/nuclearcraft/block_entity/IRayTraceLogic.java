package com.nred.nuclearcraft.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IRayTraceLogic extends ITile {
    @OnlyIn(Dist.CLIENT)
    void onPlayerMouseOver(LocalPlayer player, PoseStack poseStack, Vec3 projectedView, Direction side, float partialTicks);
}