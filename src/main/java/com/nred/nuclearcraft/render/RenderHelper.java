package com.nred.nuclearcraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.zerono.mods.zerocore.lib.client.render.FluidTankRenderer;
import it.zerono.mods.zerocore.lib.client.render.buffer.VertexBuilderWrapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Objects;
import java.util.function.IntToDoubleFunction;
import java.util.function.Predicate;

public class RenderHelper {
    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, int xSize, int ySize, int zSize) {
        renderFluid(poseStack, bufferSource, packedLight, stack, capacity, xSize, ySize, zSize, x -> x.getFluid().getFluidType().isLighterThanAir(), x -> x);
    }

    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, int xSize, int ySize, int zSize, Predicate<FluidStack> isGaseous) {
        renderFluid(poseStack, bufferSource, packedLight, stack, capacity, xSize, ySize, zSize, isGaseous, x -> x);
    }

    public static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, FluidStack stack, int capacity, int xSize, int ySize, int zSize, Predicate<FluidStack> isGaseous, IntToDoubleFunction getAmount) {
        FluidTankRenderer.Single renderer = new FluidTankRenderer.Single(capacity, 0, 0, 0, xSize, ySize, zSize);

        boolean gaseous = isGaseous.test(stack);
        double amount = getAmount.applyAsDouble(stack.getAmount());

        if (amount <= 0D) {
            return;
        }

        if (gaseous) {
            double fraction = Math.min(1D, amount / capacity);
            renderer.render(poseStack, new TintingRenderTypeBufferWrapper(bufferSource, (float) fraction, 1f, 1f, 1f), packedLight, stack.copyWithAmount(capacity));
        } else {
            renderer.render(poseStack, bufferSource, packedLight, stack.copyWithAmount((int) amount));
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