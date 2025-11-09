package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerControllerEntity;
import com.nred.nuclearcraft.multiblock.machine.ElectrolyzerLogic;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;
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
public record RenderMultiblockElectrolyzer(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<ElectrolyzerControllerEntity> {

    @Override
    public void render(ElectrolyzerControllerEntity controller, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!controller.isRenderer() || !controller.isMachineAssembled()) {
            return;
        }

        Machine machine = controller.getMultiblockController().orElse(null);
        if (machine == null) {
            return;
        }

        MachineLogic logic = machine.getLogic();
        if (!(logic instanceof ElectrolyzerLogic)) {
            return;
        }

        poseStack.pushPose();

        BlockPos posOffset = machine.getExtremeInteriorCoord(false, false, false).subtract(controller.getBlockPos());
        poseStack.translate(posOffset.getX(), posOffset.getY(), posOffset.getZ());

        int xSize = machine.getInteriorLengthX(), ySize = machine.getInteriorLengthY(), zSize = machine.getInteriorLengthZ();
        for (Tank tank : machine.reservoirTanks) {
            renderFluid(poseStack, bufferSource, packedLight, tank.getFluid(), tank.getCapacity(), xSize, ySize, zSize);
        }

        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull AABB getRenderBoundingBox(ElectrolyzerControllerEntity blockEntity) {
        if (blockEntity.getMultiblockController().isPresent()) {
            Machine hx = blockEntity.getMultiblockController().get();
            return hx.getBoundingBox().getAABB().inflate(hx.getInteriorLengthX(), hx.getInteriorLengthY(), hx.getInteriorLengthZ());
        }
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}