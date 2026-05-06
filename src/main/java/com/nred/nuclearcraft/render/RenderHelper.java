package com.nred.nuclearcraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.zerono.mods.zerocore.internal.client.RenderTypes;
import it.zerono.mods.zerocore.lib.client.render.ModRenderHelper;
import it.zerono.mods.zerocore.lib.client.render.buffer.VertexBuilderWrapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Objects;
import java.util.function.IntToDoubleFunction;
import java.util.function.Predicate;

public class RenderHelper {
    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, float xSize, float ySize, float zSize) {
        renderFluid(poseStack, bufferSource, packedLight, stack, capacity, xSize, ySize, zSize, x -> x.getFluid().getFluidType().isLighterThanAir(), x -> x, Direction.UP);
    }

    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, float xSize, float ySize, float zSize, Direction fillDir) {
        renderFluid(poseStack, bufferSource, packedLight, stack, capacity, xSize, ySize, zSize, x -> x.getFluid().getFluidType().isLighterThanAir(), x -> x, fillDir);
    }

    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, float xSize, float ySize, float zSize, Predicate<FluidStack> isGaseous) {
        renderFluid(poseStack, bufferSource, packedLight, stack, capacity, xSize, ySize, zSize, isGaseous, x -> x, Direction.UP);
    }

    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, float xSize, float ySize, float zSize, Direction fillDir, Predicate<FluidStack> isGaseous) {
        renderFluid(poseStack, bufferSource, packedLight, stack, capacity, xSize, ySize, zSize, isGaseous, x -> x, fillDir);
    }

    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, float xSize, float ySize, float zSize, Predicate<FluidStack> isGaseous, IntToDoubleFunction getAmount) {
        renderFluid(poseStack, bufferSource, packedLight, stack, capacity, xSize, ySize, zSize, isGaseous, getAmount, Direction.UP);
    }

    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, float xSize, float ySize, float zSize, Predicate<FluidStack> isGaseous, IntToDoubleFunction getAmount, Direction fillDir) {
        boolean gaseous = isGaseous.test(stack);
        double amount = getAmount.applyAsDouble(stack.getAmount());

        if (amount <= 0D) {
            return;
        }

        if (gaseous) {
            double fraction = Math.min(1D, amount / capacity);
            renderFluid(poseStack, new TintingRenderTypeBufferWrapper(bufferSource, (float) fraction, 1f, 1f, 1f), xSize, ySize, zSize, packedLight, capacity, stack.copyWithAmount(capacity));
        } else {
            poseStack.rotateAround(switch (fillDir) { // TODO check if these are right way around
                case UP -> new Quaternionf();
                case DOWN -> new Quaternionf().setAngleAxis(Math.toRadians(180), 0, 0, 1);
                case SOUTH -> new Quaternionf().setAngleAxis(Math.toRadians(270), 0, 0, 1);
                case NORTH -> new Quaternionf().setAngleAxis(Math.toRadians(90), 0, 0, 1);
                case WEST -> new Quaternionf().setAngleAxis(Math.toRadians(270), 1, 0, 0);
                case EAST -> new Quaternionf().setAngleAxis(Math.toRadians(90), 1, 0, 0);

            }, (xSize + 1) / 2f, (ySize + 1) / 2f, (zSize + 1) / 2f);
            renderFluid(poseStack, bufferSource, xSize, ySize, zSize, packedLight, capacity, stack.copyWithAmount((int) amount));
        }
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, float xSize, float ySize, float zSize, int packedLight, int capacity, FluidStack fluidStack) {
        if (!fluidStack.isEmpty()) {
            ySize *= Math.min(1.0F, (float) fluidStack.getAmount() / (float) capacity); // Fill percent
            int xFull = Mth.ceil(xSize), yFull = Mth.ceil(ySize), zFull = Mth.ceil(zSize);
            float xPartial = Mth.frac(xSize), yPartial = Mth.frac(ySize), zPartial = Mth.frac(zSize);

            Matrix4f matrix = poseStack.last().pose();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderTypes.FLUID_COLUMN);
            Fluid fluid = fluidStack.getFluid();
            FluidType fluidType = fluid.getFluidType();
            TextureAtlasSprite stillSprite = ModRenderHelper.getFluidStillSprite(fluid);
            TextureAtlasSprite flowingSprite = ModRenderHelper.getFluidFlowingSprite(fluid);
            int fluidColour = ModRenderHelper.getFluidTint(fluid);

            packedLight = ModRenderHelper.addBlockLight(packedLight, fluidType.getLightLevel(fluidStack));
            for (int z = 0; z < zFull; z++) {
                float zPart = ((z + 1) > zSize ? zPartial : 1);
                for (float x = 0; x < xFull; x++) {
                    float xPart = ((x + 1) > xSize ? xPartial : 1);
                    ModRenderHelper.renderBlockFace(vertexConsumer, matrix, Direction.UP, x, 0.005F, z, x + xPart, 0.005F, z + zPart, stillSprite.getU0(), stillSprite.getU(xPart), stillSprite.getV0(), stillSprite.getV(zPart), fluidColour, packedLight);
                    ModRenderHelper.renderBlockFace(vertexConsumer, matrix, Direction.DOWN, x, ySize - 0.005F, z, x + xPart, ySize - 0.005F, z + zPart, stillSprite.getU0(), stillSprite.getU(xPart), stillSprite.getV0(), stillSprite.getV(zPart), fluidColour, packedLight);

                    if (z == 0 || z == zFull - 1 || x == 0 || x == xFull - 1) {
                        for (int y = 0; y < yFull; y++) {
                            float yPart = ((y + 1) > ySize ? yPartial : 1);
                            if (z == 0) {
                                ModRenderHelper.renderBlockFace(vertexConsumer, matrix, Direction.NORTH, x, y, z, x + xPart, y + yPart, z + zPart, flowingSprite.getU0(), flowingSprite.getU(xPart), flowingSprite.getV(1 - yPart), flowingSprite.getV1(), fluidColour, packedLight);
                            }
                            if (z == zFull - 1) {
                                ModRenderHelper.renderBlockFace(vertexConsumer, matrix, Direction.SOUTH, x, y, z, x + xPart, y + yPart, z + zPart, flowingSprite.getU0(), flowingSprite.getU(xPart), flowingSprite.getV(1 - yPart), flowingSprite.getV1(), fluidColour, packedLight);
                            }

                            if (x == 0) {
                                ModRenderHelper.renderBlockFace(vertexConsumer, matrix, Direction.WEST, x, y, z, x + xPart, y + yPart, z + zPart, flowingSprite.getU0(), flowingSprite.getU(zPart), flowingSprite.getV(1 - yPart), flowingSprite.getV1(), fluidColour, packedLight);
                            }
                            if (x == xFull - 1) {
                                ModRenderHelper.renderBlockFace(vertexConsumer, matrix, Direction.EAST, x, y, z, x + xPart, y + yPart, z + zPart, flowingSprite.getU0(), flowingSprite.getU(zPart), flowingSprite.getV(1 - yPart), flowingSprite.getV1(), fluidColour, packedLight);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @see it.zerono.mods.zerocore.lib.client.render.buffer.TintingRenderTypeBufferWrapper
     * Above currently not working so I made a custom version for my use case
     * TODO see if they change this in the future (current version is 2.4.18)
     */
    record TintingRenderTypeBufferWrapper(MultiBufferSource _buffer, int _alpha, int _red, int _green, int _blue) implements MultiBufferSource {
        TintingRenderTypeBufferWrapper(MultiBufferSource _buffer, int _alpha, int _red, int _green, int _blue) {
            this._buffer = Objects.requireNonNull(_buffer);
            this._alpha = Mth.clamp(_alpha, 0, 255);
            this._red = Mth.clamp(_red, 0, 255);
            this._green = Mth.clamp(_green, 0, 255);
            this._blue = Mth.clamp(_blue, 0, 255);
        }

        public TintingRenderTypeBufferWrapper(MultiBufferSource originalBuffer, float alpha, float redTint, float greenTint, float blueTint) {
            this(originalBuffer, (int) (alpha * 255.0F), (int) (redTint * 255.0F), (int) (greenTint * 255.0F), (int) (blueTint * 255.0F));
        }

        public VertexConsumer getBuffer(RenderType type) {
            return new TintingVertexBuilder(this._buffer.getBuffer(type), this._alpha, this._red, this._green, this._blue);
        }

        private static class TintingVertexBuilder extends VertexBuilderWrapper {
            private final int _alpha;
            private final int _red;
            private final int _green;
            private final int _blue;

            TintingVertexBuilder(VertexConsumer originalBuilder, int alpha, int red, int green, int blue) {
                super(originalBuilder);
                this._alpha = alpha;
                this._red = red;
                this._green = green;
                this._blue = blue;
            }

            @Override
            public VertexConsumer setColor(int red, int green, int blue, int alpha) {
                return this._builder.setColor(red * this._red / 255, green * this._green / 255, blue * this._blue / 255, alpha * this._alpha / 255);
            }

            @Override
            public VertexConsumer addVertex(float x, float y, float z) {
                super.addVertex(x, y, z);
                return this;
            }
        }
    }
}