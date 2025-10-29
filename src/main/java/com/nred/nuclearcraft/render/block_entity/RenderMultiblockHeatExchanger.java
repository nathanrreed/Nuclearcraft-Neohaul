package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerControllerEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import it.zerono.mods.zerocore.lib.client.render.FluidTankRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public record RenderMultiblockHeatExchanger(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<HeatExchangerControllerEntity> {

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

        for (Tank tank : hx.shellTanks) { // TODO check how this looks vs original
            FluidTankRenderer.Single renderer = new FluidTankRenderer.Single(tank.getCapacity(), 0, 0, 0, xSize, ySize, zSize);
            renderer.render(poseStack, bufferSource, packedLight, tank.getFluid());
        }

        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull AABB getRenderBoundingBox(HeatExchangerControllerEntity blockEntity) {
        if (blockEntity.getMultiblockController().isPresent()) {
            HeatExchanger turbine = blockEntity.getMultiblockController().get();
            return turbine.getBoundingBox().getAABB().inflate(turbine.getInteriorLengthX(), turbine.getInteriorLengthY(), turbine.getInteriorLengthZ());
        }
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}