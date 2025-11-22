package com.nred.nuclearcraft.block.fluid;

import com.nred.nuclearcraft.helpers.SimpleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import static com.nred.nuclearcraft.registration.DamageTypeRegistration.ACID_BURN;

public class AcidFluidBlock extends NCFluidBlock {
    public AcidFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.hurt(level.damageSources().source(ACID_BURN), 3F);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(SimpleHelper.newEffect(MobEffects.POISON, 2, 100));
            livingEntity.addEffect(SimpleHelper.newEffect(MobEffects.WEAKNESS, 2, 100));
        }
    }

    @Override
    public BlockState getSourceMixingState() {
        return null;
    }

    @Override
    public BlockState getFlowingMixingState() {
        return null;
    }

    @Override
    protected boolean canSetFireToSurroundings(Level level, BlockPos pos, BlockState state, RandomSource rand) {
        return false;
    }

    @Override
    public BlockState getFlowingIntoWaterState() {
        return null;
    }
}