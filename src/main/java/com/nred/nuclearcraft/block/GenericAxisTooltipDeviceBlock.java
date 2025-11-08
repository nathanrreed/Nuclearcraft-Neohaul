package com.nred.nuclearcraft.block;

import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.AXIS_ALL;

public class GenericAxisTooltipDeviceBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericTooltipDeviceBlock<Controller, PartType> {
    public GenericAxisTooltipDeviceBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS_ALL, context.getHorizontalDirection().getAxis());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS_ALL);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}