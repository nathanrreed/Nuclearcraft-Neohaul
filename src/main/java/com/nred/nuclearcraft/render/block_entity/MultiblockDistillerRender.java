package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.machine.DistillerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.DistillerLiquidDistributorEntity;
import com.nred.nuclearcraft.multiblock.machine.DistillerLogic;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.nred.nuclearcraft.registration.BlockRegistration.MACHINE_MAP;
import static com.nred.nuclearcraft.render.RenderHelper.renderFluid;
import static it.zerono.mods.zerocore.lib.client.render.ModRenderHelper.ONE_PIXEL;
import static it.zerono.mods.zerocore.lib.client.render.ModRenderHelper.bindBlocksTexture;

@OnlyIn(Dist.CLIENT)
public record MultiblockDistillerRender(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<DistillerControllerEntity> {

    @Override
    public void render(DistillerControllerEntity controller, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!controller.isRenderer() || !controller.isMachineAssembled()) {
            return;
        }

        Machine machine = controller.getMultiblockController().orElse(null);
        if (machine == null) {
            return;
        }

        MachineLogic logic = machine.getLogic();
        if (!(logic instanceof DistillerLogic distillerLogic)) {
            return;
        }

        poseStack.pushPose();

        BlockPos cornerPos = machine.getExtremeInteriorCoord(false, false, false);
        BlockPos posOffset = cornerPos.subtract(controller.getTilePos());
        poseStack.translate(posOffset.getX(), posOffset.getY(), posOffset.getZ());

        int brightness = controller.nextRenderBrightness();

        bindBlocksTexture();

        IntList trayLevels = distillerLogic.trayLevels;
        int trayCount = trayLevels.size();
        int xSize = machine.getInteriorLengthX(), ySize = machine.getInteriorLengthY(), zSize = machine.getInteriorLengthZ();
        BlockState trayState = MACHINE_MAP.get("distiller_sieve_tray").get().defaultBlockState();
        for (int i = 0; i < trayCount; ++i) {
            int y = trayLevels.getInt(i);
            double offset = (i & 1) == 0 ? 0.9375D * ONE_PIXEL : -0.9375D * ONE_PIXEL;
            for (int x = 0; x < xSize; ++x) {
                for (int z = 0; z < zSize; ++z) {
                    poseStack.pushPose();
                    poseStack.translate(x + offset, y + 0.5D, z + offset);
                    poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(-90F), 0F, 1F, 0F));
                    context.getBlockRenderDispatcher().renderSingleBlock(trayState, poseStack, bufferSource, brightness, packedOverlay, ModelData.EMPTY, RenderType.SOLID);
                    poseStack.popPose();
                }
            }
        }

        List<Tank> liquids = new ArrayList<>();
        List<Tank> gasses = new ArrayList<>();

        for (Tank tank : machine.processor.getFluidInputs(false)) {
            FluidStack stack = tank.getFluid();
            if (stack.isEmpty() || stack.getAmount() <= 0) {
                continue;
            }

            Fluid fluid = stack.getFluid();

            (fluid.getFluidType().isLighterThanAir() ? gasses : liquids).add(tank);
        }

        if (!liquids.isEmpty()) {
            liquids.sort(Comparator.comparingInt(x -> {
                FluidStack stack = x.getFluid();
                return -stack.getFluid().getFluidType().getDensity(stack);
            }));

            int liquidCount = liquids.size();
            for (int i = 0; i < trayCount; ++i) {
                boolean offsetMin = (i & 1) == 0;
                Direction xFillDir = offsetMin ? Direction.WEST : Direction.EAST, zFillDir = offsetMin ? Direction.NORTH : Direction.SOUTH;

                Tank tank = liquids.get((liquidCount * i) / trayCount);
                double fraction = (double) tank.getFluidAmount() / (double) tank.getCapacity();

                double trayHeight = trayLevels.getInt(i) + 1.5D - 0.25D * ONE_PIXEL;
                double fallHeight = trayLevels.getInt(i) - (i > 0 ? trayLevels.getInt(i - 1) : -1.5D + 0.25D * ONE_PIXEL);

                if (fraction > 0.5D) {
                    double widthMult = Math.min(1D - 0.25 * ONE_PIXEL, 2D * (fraction - 0.5D));

                    poseStack.pushPose();
                    double zWidth = zSize - 0.5D * ONE_PIXEL;
                    poseStack.translate(offsetMin ? 0.5D * ONE_PIXEL : xSize - 1.5D * ONE_PIXEL, trayHeight - fallHeight, 0.5D * (1D - widthMult) * zWidth + (offsetMin ? 0.5D * ONE_PIXEL : 0D));

                    renderFluid(poseStack, bufferSource, packedLight, tank.getFluid(), tank.getCapacity(), ONE_PIXEL, (float) fallHeight, (float) (widthMult * zWidth), xFillDir);

                    poseStack.popPose();

                    poseStack.pushPose();
                    double xWidth = xSize - 0.5D * ONE_PIXEL;
                    poseStack.translate(0.5D * (1D - widthMult) * xWidth + (offsetMin ? 0.5D * ONE_PIXEL : 0D), trayHeight - fallHeight, offsetMin ? 0.5D * ONE_PIXEL : zSize - 1.5D * ONE_PIXEL);
                    renderFluid(poseStack, bufferSource, packedLight, tank.getFluid(), tank.getCapacity(), (float) (widthMult * xWidth), (float) fallHeight, ONE_PIXEL, zFillDir);

                    poseStack.popPose();
                }

                poseStack.pushPose();
                double widthMult = Math.min(1D, 2D * fraction);
                double xWidth = xSize - ONE_PIXEL, zWidth = zSize - ONE_PIXEL;
                poseStack.translate(0.5D * ONE_PIXEL + 0.5D * (1D - widthMult) * xWidth, trayHeight - 0.5D * ONE_PIXEL, 0.5D * ONE_PIXEL + 0.5D * (1D - widthMult) * zWidth);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.translate(-0.5D * ONE_PIXEL, -0.5D * ONE_PIXEL, -0.5D * ONE_PIXEL);
            renderFluid(poseStack, bufferSource, packedLight, liquids.get(0).getFluid(), liquids.get(0).getCapacity(), xSize + ONE_PIXEL, (float) (0.25D + 0.5D * ONE_PIXEL), zSize + ONE_PIXEL);

            poseStack.popPose();

            Tank topLiquid = liquids.get(liquidCount - 1);
            FluidStack topStack = topLiquid.getFluid();
            int topStackSize = topLiquid.getFluidAmount();
            double topStackFraction = (double) topStackSize / (double) topLiquid.getCapacity();

            if (topStackFraction > 0.5D) {
                double width = 1D - 4D * ONE_PIXEL, widthMult = 2D * (topStackFraction - 0.5D), offset = 2D * ONE_PIXEL + 0.5D * (1D - widthMult) * width;
                double fallTop = ySize + 0.5D * ONE_PIXEL;
                double fallBottom = trayCount == 0 ? -0.5D * ONE_PIXEL : trayLevels.getInt(trayCount - 1) + 0.5D + ONE_PIXEL;

                for (DistillerLiquidDistributorEntity ld : machine.getParts(DistillerLiquidDistributorEntity.class)) {
                    BlockPos ldPos = ld.getBlockPos();
                    int ldX = ldPos.getX() - cornerPos.getX(), ldZ = ldPos.getZ() - cornerPos.getZ();

                    poseStack.pushPose();
                    poseStack.translate(ldX + offset, fallBottom, ldZ + offset);

                    renderFluid(poseStack, bufferSource, packedLight, topStack, topStackSize, (float) (widthMult * width), (float) (fallTop - fallBottom), (float) (widthMult * width));

                    poseStack.popPose();
                }
            }
        }

        for (Tank tank : gasses) {
            renderFluid(poseStack, bufferSource, packedLight, tank.getFluid(), tank.getCapacity(), xSize, ySize, zSize);
        }

        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull AABB getRenderBoundingBox(DistillerControllerEntity blockEntity) {
        if (blockEntity.getMultiblockController().isPresent()) {
            Machine hx = blockEntity.getMultiblockController().get();
            return hx.getBoundingBox().getAABB().inflate(hx.getInteriorLengthX(), hx.getInteriorLengthY(), hx.getInteriorLengthZ());
        }
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}