package com.nred.nuclearcraft.compat.create;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class GeneratingKineticTurbineRotorBearingRenderer extends KineticBlockEntityRenderer<GeneratingKineticTurbineRotorBearingEntity> {
    public GeneratingKineticTurbineRotorBearingRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(GeneratingKineticTurbineRotorBearingEntity be, BlockState state) {
        Direction facing = state.getValue(DirectionalKineticBlock.FACING);
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, facing);
    }
}