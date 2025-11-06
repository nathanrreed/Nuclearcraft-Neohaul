package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block_entity.hx.CondenserControllerEntity;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.render.RenderHelper.renderFluid;

@OnlyIn(Dist.CLIENT)
public record RenderMultiblockCondenser(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<CondenserControllerEntity> {

    @Override
    public void render(CondenserControllerEntity controller, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!controller.isRenderer() || !controller.isMachineAssembled()) {
            return;
        }

        HeatExchanger hx = controller.getMultiblockController().orElse(null);
        if (hx == null) {
            return;
        }

        int xSize = hx.getInteriorLengthX() - 1, ySize = hx.getInteriorLengthY() - 1, zSize = hx.getInteriorLengthZ() - 1;
        if (xSize < 1 || zSize < 1) {
            return;
        }

        poseStack.pushPose();

        BlockPos posOffset = hx.getExtremeInteriorCoord(false, false, false).subtract(controller.getBlockPos());
        poseStack.translate(posOffset.getX(), posOffset.getY(), posOffset.getZ());

        renderFluid(poseStack, bufferSource, packedLight, hx.shellTanks.getFirst().getFluid(), hx.shellTanks.getFirst().getCapacity(), xSize, ySize, zSize);

        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull AABB getRenderBoundingBox(CondenserControllerEntity blockEntity) {
        if (blockEntity.getMultiblockController().isPresent()) {
            HeatExchanger hx = blockEntity.getMultiblockController().get();
            return hx.getBoundingBox().getAABB().inflate(hx.getInteriorLengthX(), hx.getInteriorLengthY(), hx.getInteriorLengthZ());
        }
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}