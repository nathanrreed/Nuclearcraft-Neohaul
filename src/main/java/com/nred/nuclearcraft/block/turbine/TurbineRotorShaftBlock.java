package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import it.zerono.mods.zerocore.base.multiblock.part.INeverCauseRenderingSkip;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.DIR;

public class TurbineRotorShaftBlock extends GenericTooltipDeviceBlock<Turbine, ITurbinePartType> implements INeverCauseRenderingSkip {
    public TurbineRotorShaftBlock(@NotNull MultiblockPartProperties<ITurbinePartType> iTurbinePartTypeMultiblockPartProperties) {
        super(iTurbinePartTypeMultiblockPartProperties.setBlockProperties(Properties.of().noOcclusion()));
        registerDefaultState(this.defaultBlockState().setValue(DIR, TurbinePartDir.Y));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIR);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        return defaultBlockState().setValue(DIR, TurbinePartDir.fromFacingAxis(direction.getAxis()));
    }
}