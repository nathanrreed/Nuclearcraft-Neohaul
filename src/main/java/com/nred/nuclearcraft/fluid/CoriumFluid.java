package com.nred.nuclearcraft.fluid;

import com.nred.nuclearcraft.config.Config2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.registration.BlockRegistration.SOLIDIFIED_CORIUM;

public abstract class CoriumFluid extends BaseFlowingFluid { //TODO
    protected static Set<ResourceLocation> solidification_dim_set;

    protected CoriumFluid(Properties properties) {
        super(properties.tickRate(60));
    }

//    @Override TODO added entityInside in 1.21.
//    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
//        entityIn.attackEntityFrom(DamageSources.CORIUM_BURN, 4F);
//        super.onEntityCollision(worldIn, pos, state, entityIn);
//    }

    @Override
    protected void randomTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
//        Chunk chunk = world.getChunk(pos); TODO add radiation
//        if (chunk.isLoaded()) {
//            IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
//            if (chunkSource != null) {
//                RadiationHelper.addToSourceRadiation(chunkSource, RadSources.CORIUM * getQuantaValue(world, pos));
//            }
//        }

        super.randomTick(level, pos, state, random);

        if (solidification_dim_set == null) {
            solidification_dim_set = Arrays.stream(Config2.corium_solidification).collect(Collectors.toSet());
        }

        if (state.isSource() && solidification_dim_set.contains(level.dimension().location()) != Config2.corium_solidification_list_type) {
            int count = 0;
            for (Direction side : Direction.values()) {
                FluidState fluidState = level.getFluidState(pos.relative(side));
                if (fluidState.is(getSource()) && fluidState.isSource()) {
                    ++count;
                    if (count > 3) {
                        return;
                    }
                }
            }

            if (count == 0 || random.nextInt(count) == 0) {
                level.setBlockAndUpdate(pos, SOLIDIFIED_CORIUM.get().defaultBlockState());
            }
        }
    }

    public static class Flowing extends CoriumFluid {
        public Flowing(Properties properties) {
            super(properties);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends CoriumFluid {
        public Source(Properties properties) {
            super(properties);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        protected boolean isRandomlyTicking() {
            return true;
        }
    }
}