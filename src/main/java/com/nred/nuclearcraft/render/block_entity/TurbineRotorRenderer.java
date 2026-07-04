package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.multiblock.turbine.Turbine.PlaneDir;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import com.nred.nuclearcraft.render.RenderHelper;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static it.zerono.mods.zerocore.lib.CodeHelper.getSystemTime;
import static it.zerono.mods.zerocore.lib.client.render.ModRenderHelper.ONE_PIXEL;
import static it.zerono.mods.zerocore.lib.client.render.ModRenderHelper.bindBlocksTexture;

@OnlyIn(Dist.CLIENT)
public record TurbineRotorRenderer(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<TurbineControllerEntity> {

    @Override
    public void render(TurbineControllerEntity controller, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!controller.isRenderer() || !controller.isMachineAssembled()) {
            return;
        }

        Turbine turbine = controller.getMultiblockController().orElse(null);
        if (turbine == null || turbine.nbtUpdateRenderDataFlag) {
            return;
        }

        int flowLength = turbine.getFlowLength(), bladeLength = turbine.bladeLength, shaftWidth = turbine.shaftWidth;
        if (flowLength < 1 || turbine.renderPosArray == null || turbine.bladeAngleArray == null || turbine.rotorStateArray == null || turbine.rotorStateArray.length < 1 + 4 * flowLength) {
            return;
        }

        BlockState shaftState = turbine.rotorStateArray[4 * flowLength];
        Direction dir = turbine.flowDir;
        if (shaftState == null || dir == null) {
            return;
        }

        int brightness = controller.nextRenderBrightness();

        bindBlocksTexture();

        BlockPos pos = controller.getBlockPos();
        double r = turbine.getRotorRadius(), scale = r / Math.sqrt(r * r + NCMath.sq(shaftWidth) / 4D);
        double rX = -turbine.getMaxX() + pos.getX() + (dir.getAxis() == Axis.X ? 0D : r);
        double rY = -turbine.getMaxY() + pos.getY() + (dir.getAxis() == Axis.Y ? 0D : r);
        double rZ = -turbine.getMaxZ() + pos.getZ() + (dir.getAxis() == Axis.Z ? 0D : r);

        // Enter rotated frame
        poseStack.pushPose();

        poseStack.translate((turbine.flowDir.getAxis() == Axis.X ? 1D : 0) - rX, -rY, -rZ); // Start location
        poseStack.scale((float) (dir.getAxis() == Axis.X ? 1D : scale), (float) (dir.getAxis() == Axis.Y ? 1D : scale), (float) (dir.getAxis() == Axis.Z ? 1D : scale));
        {
            long systemTime = getSystemTime() / 1000;
            if (!Minecraft.getInstance().isPaused()) {
                turbine.rotorAngle = (turbine.rotorAngle + (systemTime - controller.prevRenderTime) * turbine.angVel) % 360F;
            }
            controller.prevRenderTime = systemTime;
            poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(turbine.rotorAngle), dir.getAxis() == Axis.X ? 1F : 0F, dir.getAxis() == Axis.Y ? 1F : 0F, dir.getAxis() == Axis.Z ? 1F : 0F));
        }
        poseStack.translate(rX, rY, rZ);

        for (int depth : turbine.bladeDepths) {
            renderRotor(turbine, poseStack, bufferSource, brightness, shaftState, dir, flowLength, bladeLength, shaftWidth, turbine_render_blade_width, depth);
        }

        // Leave rotated frame
        poseStack.popPose();

        // Enter stationary frame
        poseStack.pushPose();

        poseStack.translate(-rX, -rY, -rZ);
        poseStack.scale((float) (dir.getAxis() == Axis.X ? 1D : scale), (float) (dir.getAxis() == Axis.Y ? 1D : scale), (float) (dir.getAxis() == Axis.Z ? 1D : scale));
        poseStack.translate(rX, rY, rZ);

        for (int depth : turbine.statorDepths) {
            renderRotor(turbine, poseStack, bufferSource, brightness, shaftState, dir, flowLength, bladeLength, shaftWidth, turbine_render_blade_width, depth);
        }

        // Leave stationary frame
        poseStack.popPose();

        // Tanks
        poseStack.pushPose();

        BlockPos posOffset = turbine.getExtremeInteriorCoord(false, false, false).subtract(controller.getTilePos());
        poseStack.translate(posOffset.getX(), posOffset.getY(), posOffset.getZ());
        int xSize = turbine.getInteriorLengthX(), ySize = turbine.getInteriorLengthY(), zSize = turbine.getInteriorLengthZ();
        Tank outputTank = turbine.tanks.get(1);

        RenderHelper.renderFluid(poseStack, bufferSource, packedLight, outputTank.getFluid(), outputTank.getCapacity(), xSize + 2f * ONE_PIXEL, ySize + 2f * ONE_PIXEL, zSize + 2f * ONE_PIXEL, x -> true, x -> 4D * x - 3D * outputTank.getCapacity(), Direction.UP);

        poseStack.popPose();
    }

    public void renderRotor(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, int brightness, BlockState shaftState, Direction flowDir, int flowLength, int bladeLength, int shaftWidth, double bladeWidth, int depth) {
        double depthScale = Math.pow(turbine_render_rotor_expansion, (double) (1 + depth - flowLength) / (double) flowLength);

        if (turbine.renderPosArray.length < 1 + 4 * flowLength * shaftWidth + depth) {
            return;
        }

        poseStack.pushPose();

        Vector3f renderPos = turbine.renderPosArray[4 * flowLength * shaftWidth + depth];
        poseStack.translate(renderPos.x + 0.5D, renderPos.y + 0.5D, renderPos.z + 0.5D);

        Axis flowAxis = flowDir.getAxis();
        poseStack.scale((float) (flowAxis == Axis.X ? 1D : depthScale), (float) (flowAxis == Axis.Y ? 1D : depthScale), (float) (flowAxis == Axis.Z ? 1D : depthScale));

        poseStack.translate(-renderPos.x - 0.5D, -renderPos.y - 0.5D, -renderPos.z - 0.5D);

        renderShaft(turbine, poseStack, bufferSource, brightness, shaftState, flowDir, flowLength, shaftWidth, depth);
        for (int j : new int[]{0, flowLength, 2 * flowLength, 3 * flowLength}) {
            renderBlades(turbine, poseStack, bufferSource, brightness, flowDir, flowLength, bladeLength, shaftWidth, bladeWidth, j, depth);
        }

        poseStack.popPose();
    }

    public void renderShaft(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, int brightness, BlockState shaftState, Direction flowDir, int flowLength, int shaftWidth, int depth) {
        if (turbine.renderPosArray.length < 1 + 4 * flowLength * shaftWidth + depth) {
            return;
        }

        poseStack.pushPose();

        Vector3f renderPos = turbine.renderPosArray[4 * flowLength * shaftWidth + depth];

        Axis flowAxis = flowDir.getAxis();
        poseStack.translate(renderPos.x + 0.5D + (flowAxis == Axis.Z ? shaftWidth: 0), renderPos.y + 0.5D, renderPos.z + 0.5D);

        poseStack.scale(flowAxis == Axis.X ? (float) 1D : shaftWidth, flowAxis == Axis.Y ? (float) 1D : shaftWidth, flowAxis == Axis.Z ? (float) 1D : shaftWidth);

        poseStack.translate(-0.5D, -0.5D, -0.5D);

        poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(-90F), 0F, 1F, 0F)); // Get into position
        poseStack.rotateAround(new Quaternionf().setAngleAxis(Math.toRadians(90F), 0F, 1F, 0F), 0.5f, 0.5f, 0.5f); // Reorient so block if facing correct direction

        context.getBlockRenderDispatcher().renderSingleBlock(shaftState, poseStack, bufferSource, brightness, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.SOLID);

        poseStack.popPose();
    }

    public void renderBlades(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, int brightness, Direction flowDir, int flowLength, int bladeLength, int shaftWidth, double bladeWidth, int jMult, int depth) {
        final int i = jMult + depth;

        if (turbine.rotorStateArray.length < i + 1) {
            return;
        }

        Vector3f renderPos;
        BlockState rotorState;
        TurbinePartDir rotorDir;
        PlaneDir planeDir = (i < flowLength || i >= 3 * flowLength) ? PlaneDir.V : PlaneDir.U;
        Axis flowAxis = flowDir.getAxis();

        if (turbine_render_blade_fast) {
            bladeWidth *= shaftWidth;
        }

        for (int w = 0, end = turbine_render_blade_fast ? 1 : shaftWidth; w < end; ++w) {
            poseStack.pushPose();

            if (turbine_render_blade_fast) {
                Vector3f first = turbine.renderPosArray[i * shaftWidth];
                Vector3f last = turbine.renderPosArray[(i + 1) * shaftWidth - 1];
                renderPos = new Vector3f(0.5F * (first.x + last.x), 0.5F * (first.y + last.y), 0.5F * (first.z + last.z));
            } else {
                renderPos = turbine.renderPosArray[w + i * shaftWidth];
            }

            rotorState = turbine.rotorStateArray[i];
            rotorDir = rotorState.getValue(TurbineRotorBladeUtil.DIR);
            TurbinePartDir bladeDir = turbine.getBladeDir(planeDir);

            poseStack.translate(renderPos.x + 0.5D - (flowAxis == Axis.X ? 1D : 0), renderPos.y + 0.5D, renderPos.z + 0.5D);
            poseStack.scale((float) (flowAxis == Axis.X ? 1D : (bladeDir == TurbinePartDir.X ? bladeLength : bladeWidth)), (float) (flowAxis == Axis.Y ? 1D : (bladeDir == TurbinePartDir.Y ? bladeLength : bladeWidth)), (float) (flowDir.getAxis() == Axis.Z ? 1D : (bladeDir == TurbinePartDir.Z ? bladeLength : bladeWidth)));
            poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(turbine.bladeAngleArray[i] * (flowDir.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ flowAxis == Axis.X ? 1F : -1F)), rotorDir == TurbinePartDir.X ? 1F : 0F, rotorDir == TurbinePartDir.Y ? 1F : 0F, rotorDir == TurbinePartDir.Z ? 1F : 0F));

            poseStack.translate(-0.5D, -0.5D, -0.5D);

            context.getBlockRenderDispatcher().renderSingleBlock(rotorState, poseStack, bufferSource, brightness, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.SOLID);

            poseStack.popPose();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull AABB getRenderBoundingBox(TurbineControllerEntity blockEntity) {
        if (blockEntity.getMultiblockController().isPresent()) {
            Turbine turbine = blockEntity.getMultiblockController().get();
            return turbine.getBoundingBox().getAABB().inflate(turbine.getInteriorLengthX(), turbine.getInteriorLengthY(), turbine.getInteriorLengthZ());
        }
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}
