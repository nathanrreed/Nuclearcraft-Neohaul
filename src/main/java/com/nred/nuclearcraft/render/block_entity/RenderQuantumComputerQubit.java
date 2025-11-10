package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.nred.nuclearcraft.block_entity.quantum.QuantumComputerQubitEntity;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static net.minecraft.client.renderer.RenderStateShard.*;

@OnlyIn(Dist.CLIENT)
public record RenderQuantumComputerQubit(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<QuantumComputerQubitEntity> {
    static AABB bounds = Block.box(4, 4, 4, 12, 12, 12).bounds();
    static List<Vec3> vertexes = List.of(
            new Vec3(bounds.minX, bounds.maxY, bounds.minZ),
            new Vec3(bounds.minX, bounds.minY, bounds.minZ),
            new Vec3(bounds.minX, bounds.minY, bounds.maxZ),
            new Vec3(bounds.minX, bounds.maxY, bounds.maxZ),
            new Vec3(bounds.maxX, bounds.maxY, bounds.maxZ),
            new Vec3(bounds.maxX, bounds.minY, bounds.maxZ),
            new Vec3(bounds.maxX, bounds.minY, bounds.minZ),
            new Vec3(bounds.maxX, bounds.maxY, bounds.minZ)
    );

    static RenderType qubitRender = RenderType.create(
            MODID + ":qubitRender",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LINES_SHADER)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(NO_CULL)
                    .createCompositeState(false)
    );

    @Override
    public void render(QuantumComputerQubitEntity qubit, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        long time = System.currentTimeMillis();

        boolean up = qubit.measureColor > 0F, down = qubit.measureColor < 0F;
        float r = (float) NCMath.trapezoidalWave(time * 0.12D, 0D) + 6F * (down ? -qubit.measureColor : 0F);
        float g = (float) NCMath.trapezoidalWave(time * 0.12D, 120D);
        float b = (float) NCMath.trapezoidalWave(time * 0.12D, 240D) + 6F * (up ? qubit.measureColor : 0F);

        if (up) {
            qubit.measureColor = Math.max(0F, qubit.measureColor - 0.0005F);
        } else if (down) {
            qubit.measureColor = Math.min(0F, qubit.measureColor + 0.0005F);
        }

        float d = Math.max(r, Math.max(g, b));
        r /= d;
        g /= d;
        b /= d;

        poseStack.pushPose();

        PoseStack.Pose posestack$pose = poseStack.last();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderQuantumComputerQubit.qubitRender);

        aad(posestack$pose, vertexConsumer, vertexes.get(0), vertexes.get(1), vertexes.get(2), vertexes.get(3), r, g, b);
        aad(posestack$pose, vertexConsumer, vertexes.get(3), vertexes.get(2), vertexes.get(5), vertexes.get(4), r, g, b);
        aad(posestack$pose, vertexConsumer, vertexes.get(4), vertexes.get(5), vertexes.get(6), vertexes.get(7), r, g, b);
        aad(posestack$pose, vertexConsumer, vertexes.get(7), vertexes.get(6), vertexes.get(1), vertexes.get(0), r, g, b);
        aad(posestack$pose, vertexConsumer, vertexes.get(7), vertexes.get(0), vertexes.get(3), vertexes.get(4), r, g, b);
        aad(posestack$pose, vertexConsumer, vertexes.get(6), vertexes.get(5), vertexes.get(2), vertexes.get(1), r, g, b);

        poseStack.popPose();
    }

    private void aad(PoseStack.Pose posestack$pose, VertexConsumer vertexConsumer, Vec3 first, Vec3 second, Vec3 third, Vec3 fourth, float r, float g, float b) {
        Vec3 normal = (second.subtract(first)).cross(fourth.subtract(third)).normalize();
        vertexConsumer.addVertex(posestack$pose, (float) (first.x), (float) (first.y), (float) (first.z))
                .setColor(r, g, b, 1f)
                .setUv(0, 0)
                .setUv2(0, 0)
                .setNormal(posestack$pose, (float) normal.x, (float) normal.y, (float) normal.z);
        vertexConsumer.addVertex(posestack$pose, (float) (second.x), (float) (second.y), (float) (second.z))
                .setColor(r, g, b, 1f)
                .setUv(0, 0)
                .setUv2(0, 0)
                .setNormal(posestack$pose, (float) normal.x, (float) normal.y, (float) normal.z);
        vertexConsumer.addVertex(posestack$pose, (float) (third.x), (float) (third.y), (float) (third.z))
                .setColor(r, g, b, 1f)
                .setUv(0, 0)
                .setUv2(0, 0)
                .setNormal(posestack$pose, (float) normal.x, (float) normal.y, (float) normal.z);
        vertexConsumer.addVertex(posestack$pose, (float) (fourth.x), (float) (fourth.y), (float) (fourth.z))
                .setColor(r, g, b, 1f)
                .setUv(0, 0)
                .setUv2(0, 0)
                .setNormal(posestack$pose, (float) normal.x, (float) normal.y, (float) normal.z);
    }
}