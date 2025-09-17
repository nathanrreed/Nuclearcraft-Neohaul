package com.nred.nuclearcraft.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.multiblock.turbine.Turbine.PlaneDir;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.nred.nuclearcraft.config.Config2.turbine_render_blade_width;
import static com.nred.nuclearcraft.config.Config2.turbine_render_rotor_expansion;
import static it.zerono.mods.zerocore.lib.CodeHelper.getSystemTime;
import static it.zerono.mods.zerocore.lib.client.render.ModRenderHelper.bindBlocksTexture;

@OnlyIn(Dist.CLIENT)
public class TurbineRotorRenderer implements BlockEntityRenderer<TurbineControllerEntity> {
    //	@Override
//	public boolean isGlobalRenderer(TurbineControllerEntity controller) {
//		return controller.isRenderer() && controller.isMultiblockAssembled();
//	}
    private final BlockEntityRendererProvider.Context context;

    public TurbineRotorRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

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

        float brightness = controller.nextRenderBrightness();

        bindBlocksTexture();

        GlStateManager._clearColor(1F, 1F, 1F, 1F); // TODO ??? pose?
//		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0F, 240F); TODO REMOVE

        BlockPos pos = controller.getBlockPos();
        double r = turbine.getRotorRadius(), scale = r / Math.sqrt(r * r + NCMath.sq(shaftWidth) / 4D);
        double rX = -turbine.getMaxX() + pos.getX() + (dir.getAxis() == Axis.X ? 0D : r);
        double rY = -turbine.getMaxY() + pos.getY() + (dir.getAxis() == Axis.Y ? 0D : r);
        double rZ = -turbine.getMaxZ() + pos.getZ() + (dir.getAxis() == Axis.Z ? 0D : r);

        // Enter rotated frame
        poseStack.pushPose();

        poseStack.translate(-pos.getX() + (turbine.flowDir.getAxis() == Axis.X ? 1D : 0), -pos.getY(), -pos.getZ());

        poseStack.translate(pos.getX() - rX, pos.getY() - rY, pos.getZ() - rZ);
        poseStack.scale((float) (dir.getAxis() == Axis.X ? 1D : scale), (float) (dir.getAxis() == Axis.Y ? 1D : scale), (float) (dir.getAxis() == Axis.Z ? 1D : scale));
        {
            long systemTime = getSystemTime() / 10000000;
            if (!Minecraft.getInstance().isPaused()) {
                turbine.rotorAngle = (turbine.rotorAngle + (systemTime - controller.prevRenderTime) * 1.0001f /* TODO turbine.angVel*/) % 360F;
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
//
//        poseStack.pushPose();

//        GlStateManager.color(1F, 1F, 1F, 1F);
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0F, 240F);
//
//        poseStack.enableCull();
//        poseStack.enableBlend();
//        poseStack.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
//
//        BlockPos posOffset = turbine.getExtremeInteriorCoord(false, false, false).subtract(controller.getPos());
//        poseStack.translate(posX + posOffset.getX(), posY + posOffset.getY(), posZ + posOffset.getZ());
//
//        int xSize = turbine.getInteriorLengthX(), ySize = turbine.getInteriorLengthY(), zSize = turbine.getInteriorLengthZ();
//
//        poseStack.pushMatrix();
//        poseStack.translate(-PIXEL, -PIXEL, -PIXEL);
//
//        Tank outputTank = turbine.tanks.get(1);
//        int outputCapacity = outputTank.getCapacity();
//        IWorldRender.renderFluid(outputTank.getFluid(), outputCapacity, xSize + 2D * PIXEL, ySize + 2D * PIXEL, zSize + 2D * PIXEL, EnumFacing.UP, x -> true, x -> 4D * x - 3D * outputCapacity);
//
//        poseStack.popMatrix();
//
//        poseStack.disableBlend();
//        poseStack.disableCull();
//
//        poseStack.popMatrix();
    }

    public void renderRotor(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, float brightness, BlockState shaftState, Direction flowDir, int flowLength, int bladeLength, int shaftWidth, double bladeWidth, int depth) {
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

    public void renderShaft(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, float brightness, BlockState shaftState, Direction flowDir, int flowLength, int shaftWidth, int depth) {
        poseStack.pushPose();

        if (turbine.renderPosArray.length < 1 + 4 * flowLength * shaftWidth + depth) {
            return;
        }

        Vector3f renderPos = turbine.renderPosArray[4 * flowLength * shaftWidth + depth];
        poseStack.translate(renderPos.x + 0.5D, renderPos.y + 0.5D, renderPos.z + 0.5D);
        poseStack.scale(flowDir.getAxis() == Axis.X ? (float) 1D : shaftWidth, flowDir.getAxis() == Axis.Y ? (float) 1D : shaftWidth, flowDir.getAxis() == Axis.Z ? (float) 1D : shaftWidth);

        poseStack.translate(-0.5D, -0.5D, -0.5D);
        poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(-90F), 0F, 1F, 0F));

//        renderer.renderBlockBrightness(shaftState, brightness); //TODO FIX
        context.getBlockRenderDispatcher().renderSingleBlock(shaftState, poseStack, bufferSource, 255, OverlayTexture.NO_OVERLAY, net.neoforged.neoforge.client.model.data.ModelData.EMPTY, RenderType.SOLID);

        poseStack.popPose();
    }

    public void renderBlades(Turbine turbine, PoseStack poseStack, MultiBufferSource bufferSource, float brightness, Direction flowDir, int flowLength, int bladeLength, int shaftWidth, double bladeWidth, int jMult, int depth) {
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

            poseStack.translate(renderPos.x + 0.5D, renderPos.y + 0.5D, renderPos.z + 0.5D);
            poseStack.scale((float) (flowDir.getAxis() == Axis.X ? 1D : (turbine.getBladeDir(planeDir) == TurbinePartDir.X ? bladeLength : bladeWidth)), (float) (flowDir.getAxis() == Axis.Y ? 1D : (turbine.getBladeDir(planeDir) == TurbinePartDir.Y ? bladeLength : bladeWidth)), (float) (flowDir.getAxis() == Axis.Z ? 1D : (turbine.getBladeDir(planeDir) == TurbinePartDir.Z ? bladeLength : bladeWidth)));
            poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(turbine.bladeAngleArray[i] * (flowDir.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ flowDir.getAxis() == Axis.X ? 1F : -1F)), bladeDir == TurbinePartDir.X ? 1F : 0F, bladeDir == TurbinePartDir.Y ? 1F : 0F, bladeDir == TurbinePartDir.Z ? 1F : 0F));

            poseStack.translate(-0.5D, -0.5D, -0.5D);
            poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(-90F), 0F, 1F, 0F));

//            renderer.renderBlockBrightness(rotorState, brightness);
            context.getBlockRenderDispatcher().renderSingleBlock(rotorState, poseStack, bufferSource, 255, OverlayTexture.NO_OVERLAY, net.neoforged.neoforge.client.model.data.ModelData.EMPTY, RenderType.SOLID);

            poseStack.popPose();
        }
    }
}
