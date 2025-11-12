package com.nred.nuclearcraft.block;

import com.nred.nuclearcraft.worldgen.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GlowingMushroomBlock extends MushroomBlock {
    public GlowingMushroomBlock(Properties properties) {
        super(ModConfiguredFeatures.HUGE_GLOWING_MUSHROOM_CURVE_KEY, properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolidRender(level, pos.below());
    }

    @Override
    public boolean growMushroom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        this.feature = level.random.nextBoolean() ? ModConfiguredFeatures.HUGE_GLOWING_MUSHROOM_CURVE_KEY : ModConfiguredFeatures.HUGE_GLOWING_MUSHROOM_FLAT_KEY;
        return super.growMushroom(level, pos, state, random);
    }
}