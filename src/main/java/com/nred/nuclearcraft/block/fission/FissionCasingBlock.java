package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.zerono.mods.zerocore.base.multiblock.part.INeverCauseRenderingSkip;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.registration.BlockRegistration.FRAME;

public class FissionCasingBlock extends GenericTooltipDeviceBlock<FissionReactor, IFissionPartType> implements INeverCauseRenderingSkip {
    public FissionCasingBlock(@NotNull MultiblockPartProperties<IFissionPartType> iFissionPartTypeMultiblockPartProperties) {
        super(iFissionPartTypeMultiblockPartProperties);
        registerDefaultState(this.defaultBlockState().setValue(FRAME, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRAME);
    }
}