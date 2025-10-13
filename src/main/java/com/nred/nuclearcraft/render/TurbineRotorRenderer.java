package com.nred.nuclearcraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.block.internal.fluid.Tank;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.multiblock.turbine.Turbine.PlaneDir;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.client.render.FluidTankRenderer;
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

import static com.nred.nuclearcraft.config.Config2.turbine_render_blade_width;
import static com.nred.nuclearcraft.config.Config2.turbine_render_rotor_expansion;
import static it.zerono.mods.zerocore.lib.CodeHelper.getSystemTime;
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

        poseStack.translate(-pos.getX() + (turbine.flowDir.getAxis() == Axis.X ? 1D : 0), -pos.getY(), -pos.getZ()); // Start location

        poseStack.translate(pos.getX() - rX, pos.getY() - rY, pos.getZ() - rZ);
        poseStack.scale((float) (dir.getAxis() == Axis.X ? 1D : scale), (float) (dir.getAxis() == Axis.Y ? 1D : scale), (float) (dir.getAxis() == Axis.Z ? 1D : scale));
        {
            long systemTime = getSystemTime() / 1000;
            if (!Minecraft.getInstance().isPaused()) {
                turbine.rotorAngle = (turbine.rotorAngle + (systemTime - controller.prevRenderTime) * turbine.angVel) % 360F;
            }
            controller.prevRenderTime = systemTime;
            poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(turbine.rotorAngle), dir.getAxis() == Axis.X ? 1F : 0F, dir.getAxis() == Axis.Y ? 1F : 0F, dir.getAxis() == Axis.Z ? 1F : 0F));
        }
        poseStack.translate(-pos.getX() + rX, -pos.getY() + rY, -pos.getZ() + rZ);

        for (int depth : turbine.bladeDepths) {
            renderRotor(turbine, poseStack, bufferSource, brightness, shaftState, dir, flowLength, bladeLength, shaftWidth, turbine_render_blade_width, depth);
        }

        // Leave rotated frame
        poseStack.popPose();

        // Enter stationary frame
        poseStack.pushPose();

        poseStack.translate(pos.getX() - rX, pos.getY() - rY, pos.getZ() - rZ);
        poseStack.scale((float) (dir.getAxis() == Axis.X ? 1D : scale), (float) (dir.getAxis() == Axis.Y ? 1D : scale), (float) (dir.getAxis() == Axis.Z ? 1D : scale));
        poseStack.translate(-pos.getX() + rX, -pos.getY() + rY, -pos.getZ() + rZ);

        for (int depth : turbine.statorDepths) {
            renderRotor(turbine, poseStack, bufferSource, brightness, shaftState, dir, flowLength, bladeLength, shaftWidth, turbine_render_blade_width, depth);
        }

        // Leave stationary frame
        poseStack.popPose();

        // Tanks
        poseStack.pushPose();

        BlockPos posOffset = turbine.getExtremeInteriorCoord(false, false, false).subtract(controller.getTilePos());
        poseStack.translate(posOffset.getX(), posOffset.getY(), posOffset.getZ());
        int xSize = turbine.getInteriorLengthX() - 1, ySize = turbine.getInteriorLengthY() - 1, zSize = turbine.getInteriorLengthZ() - 1;
        Tank outputTank = turbine.tanks.get(1);
        FluidTankRenderer.Single renderer = new FluidTankRenderer.Single(outputTank.getCapacity(), 0 , 0, 0, xSize, ySize, zSize);
        renderer.render(poseStack, bufferSource, brightness, outputTank.getFluid());

        poseStack.popPose();
    }

    public void renderRotor(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, int brightness, BlockState shaftState, Direction flowDir, int flowLength, int bladeLength, int shaftWidth, double bladeWidth, int depth) {
        double depthScale = Math.pow(turbine_render_rotor_expansion, (double) (1 + depth - flowLength) / (double) flowLength);

        poseStack.pushPose();

        if (turbine.renderPosArray.length < 1 + 4 * flowLength * shaftWidth + depth) {
            return;
        }

        Vector3f renderPos = turbine.renderPosArray[4 * flowLength * shaftWidth + depth];
        poseStack.translate(renderPos.x + 0.5D, renderPos.y + 0.5D, renderPos.z + 0.5D);
        poseStack.scale((float) (flowDir.getAxis() == Axis.X ? 1D : depthScale), (float) (flowDir.getAxis() == Axis.Y ? 1D : depthScale), (float) (flowDir.getAxis() == Axis.Z ? 1D : depthScale));
        poseStack.translate(-renderPos.x - 0.5D, -renderPos.y - 0.5D, -renderPos.z - 0.5D);

        renderShaft(turbine, poseStack, bufferSource, brightness, shaftState, flowDir, flowLength, shaftWidth, depth);
        for (int j : new int[]{0, flowLength, 2 * flowLength, 3 * flowLength}) {
            renderBlades(turbine, poseStack, bufferSource, brightness, flowDir, flowLength, bladeLength, shaftWidth, bladeWidth, j, depth);
        }

        poseStack.popPose();
    }

    public void renderShaft(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, int brightness, BlockState shaftState, Direction flowDir, int flowLength, int shaftWidth, int depth) {
        poseStack.pushPose();

        if (turbine.renderPosArray.length < 1 + 4 * flowLength * shaftWidth + depth) {
            return;
        }

        Vector3f renderPos = turbine.renderPosArray[4 * flowLength * shaftWidth + depth];
        poseStack.translate(renderPos.x + 0.5D + (turbine.flowDir.getAxis() == Axis.Z ? 1D : 0), renderPos.y + 0.5D, renderPos.z + 0.5D);
        poseStack.scale(flowDir.getAxis() == Axis.X ? (float) 1D : shaftWidth, flowDir.getAxis() == Axis.Y ? (float) 1D : shaftWidth, flowDir.getAxis() == Axis.Z ? (float) 1D : shaftWidth);

        poseStack.translate(-0.5D, -0.5D, -0.5D);

        poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(-90F), 0F, 1F, 0F));

        context.getBlockRenderDispatcher().renderSingleBlock(shaftState, poseStack, bufferSource, brightness, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.SOLID);

        poseStack.popPose();
    }

    public void renderBlades(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, int brightness, Direction flowDir, int flowLength, int bladeLength, int shaftWidth, double bladeWidth, int jMult, int depth) {
        Vector3f renderPos;
        BlockState rotorState;
        TurbinePartDir bladeDir;
        PlaneDir planeDir;

        int i = jMult + depth;

        if (turbine.rotorStateArray.length < i + 1) {
            return;
        }

        for (int w = 0; w < shaftWidth; ++w) {
            poseStack.pushPose();

            renderPos = turbine.renderPosArray[w + i * shaftWidth];
            rotorState = turbine.rotorStateArray[i];
            bladeDir = rotorState.getValue(TurbineRotorBladeUtil.DIR);
            planeDir = (i < flowLength || i >= 3 * flowLength) ? PlaneDir.V : PlaneDir.U;

            poseStack.translate(renderPos.x + 0.5D - (turbine.flowDir.getAxis() == Axis.X ? 1D : 0), renderPos.y + 0.5D, renderPos.z + 0.5D);
            poseStack.scale((float) (flowDir.getAxis() == Axis.X ? 1D : (turbine.getBladeDir(planeDir) == TurbinePartDir.X ? bladeLength : bladeWidth)), (float) (flowDir.getAxis() == Axis.Y ? 1D : (turbine.getBladeDir(planeDir) == TurbinePartDir.Y ? bladeLength : bladeWidth)), (float) (flowDir.getAxis() == Axis.Z ? 1D : (turbine.getBladeDir(planeDir) == TurbinePartDir.Z ? bladeLength : bladeWidth)));
            poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(turbine.bladeAngleArray[i] * (flowDir.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ flowDir.getAxis() == Axis.X ? 1F : -1F)), bladeDir == TurbinePartDir.X ? 1F : 0F, bladeDir == TurbinePartDir.Y ? 1F : 0F, bladeDir == TurbinePartDir.Z ? 1F : 0F));

            poseStack.translate(-0.5D, -0.5D, -0.5D);

            context.getBlockRenderDispatcher().renderSingleBlock(rotorState, poseStack, bufferSource, brightness, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.SOLID);

            poseStack.popPose();
        }
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(TurbineControllerEntity blockEntity) {
        if (blockEntity.getMultiblockController().isPresent()) {
            Turbine turbine = blockEntity.getMultiblockController().get();
            return turbine.getBoundingBox().getAABB().inflate(turbine.getInteriorLengthX(), turbine.getInteriorLengthY(), turbine.getInteriorLengthZ());
        }
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}
