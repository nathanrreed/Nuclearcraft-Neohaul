package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerControllerEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.render.RenderHelper.renderFluid;
import static it.zerono.mods.zerocore.lib.client.render.ModRenderHelper.ONE_PIXEL;

@OnlyIn(Dist.CLIENT)
public record MultiblockHeatExchangerRender(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<HeatExchangerControllerEntity> {

    @Override
    public void render(HeatExchangerControllerEntity controller, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!controller.isRenderer() || !controller.isMachineAssembled()) {
            return;
        }

        HeatExchanger hx = controller.getMultiblockController().orElse(null);
        if (hx == null) {
            return;
        }


        poseStack.pushPose();

        BlockPos posOffset = hx.getExtremeInteriorCoord(false, false, false).subtract(controller.getBlockPos());
        poseStack.translate(posOffset.getX(), posOffset.getY(), posOffset.getZ());

        int xSize = hx.getInteriorLengthX(), ySize = hx.getInteriorLengthY(), zSize = hx.getInteriorLengthZ();

        poseStack.pushPose();
        poseStack.translate(-ONE_PIXEL, -ONE_PIXEL, -ONE_PIXEL);
        for (Tank tank : hx.shellTanks) {
            renderFluid(poseStack, bufferSource, packedLight, tank.getFluid(), tank.getCapacity(), xSize + 2f * ONE_PIXEL, ySize + 2f * ONE_PIXEL, zSize + 2f * ONE_PIXEL, Direction.UP, x -> true);
        }
        poseStack.popPose();

        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull AABB getRenderBoundingBox(HeatExchangerControllerEntity blockEntity) {
        if (blockEntity.getMultiblockController().isPresent()) {
            HeatExchanger hx = blockEntity.getMultiblockController().get();
            return hx.getBoundingBox().getAABB().inflate(hx.getInteriorLengthX(), hx.getInteriorLengthY(), hx.getInteriorLengthZ());
        }
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}