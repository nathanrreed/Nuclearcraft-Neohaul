package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
import com.nred.nuclearcraft.multiblock.machine.InfiltratorLogic;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;
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
public record MultiblockInfiltratorRender(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<InfiltratorControllerEntity> {

    @Override
    public void render(InfiltratorControllerEntity controller, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!controller.isRenderer() || !controller.isMachineAssembled()) {
            return;
        }

        Machine machine = controller.getMultiblockController().orElse(null);
        if (machine == null) {
            return;
        }

        MachineLogic logic = machine.getLogic();
        if (!(logic instanceof InfiltratorLogic)) {
            return;
        }

        poseStack.pushPose();

        BlockPos posOffset = machine.getExtremeInteriorCoord(false, false, false).subtract(controller.getBlockPos());
        poseStack.translate(posOffset.getX(), posOffset.getY(), posOffset.getZ());

        int xSize = machine.getInteriorLengthX(), ySize = machine.getInteriorLengthY(), zSize = machine.getInteriorLengthZ();

        poseStack.pushPose();
        poseStack.translate(-ONE_PIXEL, -ONE_PIXEL, -ONE_PIXEL);
        for (Tank tank : machine.reservoirTanks) {
            renderFluid(poseStack, bufferSource, packedLight, tank.getFluid(), tank.getCapacity(), xSize + 2f * ONE_PIXEL, ySize + 2f * ONE_PIXEL, zSize + 2f * ONE_PIXEL, Direction.UP);
        }
        poseStack.popPose();

        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull AABB getRenderBoundingBox(InfiltratorControllerEntity blockEntity) {
        if (blockEntity.getMultiblockController().isPresent()) {
            Machine machine = blockEntity.getMultiblockController().get();
            return machine.getBoundingBox().getAABB().inflate(machine.getInteriorLengthX(), machine.getInteriorLengthY(), machine.getInteriorLengthZ());
        }
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}