package com.nred.nuclearcraft.block.batteries;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class BatteryRenderer implements BlockEntityRenderer<BatteryEntity> {
    private final BlockEntityRendererProvider.Context context;

    public BatteryRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(BatteryEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        for (Direction dir : Direction.values()) {
            //TODO
//            switch (blockEntity.sideOptions.get(dir)){
//                BatteryOptions.IN->;
//                BatteryOptions.OUT->;
//                BatteryOptions.NON->;
//            }
        }
    }
}