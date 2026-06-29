package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericActiveTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.block_entity.fission.FissionShieldEntity;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import it.zerono.mods.zerocore.base.multiblock.part.INeverCauseRenderingSkip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;

public class FissionShieldBlock extends GenericActiveTooltipDeviceBlock<FissionReactor, IFissionPartType> implements INeverCauseRenderingSkip, IActivatable {
    public FissionShieldBlock(@NotNull MultiblockPartProperties<IFissionPartType> iFissionPartTypeMultiblockPartProperties) {
        super(iFissionPartTypeMultiblockPartProperties.setBlockProperties(Properties.of().noOcclusion()));
    }

    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return this == adjacentBlockState.getBlock();
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof FissionShieldEntity shield) {
            level.setBlock(pos, state.setValue(ACTIVE, shield.isShielding), 2);
        }
    }
}