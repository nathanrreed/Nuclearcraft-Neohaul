package com.nred.nuclearcraft.block.fluid;

import com.nred.nuclearcraft.helpers.SimpleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import static com.nred.nuclearcraft.registration.DamageTypeRegistration.HOT_COOLANT_BURN;

public class HotCoolantFluidBlock extends NCFluidBlock {
    public HotCoolantFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties.lightLevel(s -> 10));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.hurt(level.damageSources().source(HOT_COOLANT_BURN), 2F);
        entity.setRemainingFireTicks(3);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(SimpleHelper.newEffect(MobEffects.MOVEMENT_SLOWDOWN, 1, 100));
            livingEntity.addEffect(SimpleHelper.newEffect(MobEffects.WEAKNESS, 1, 100));
        }
    }

    @Override
    public BlockState getSourceMixingState() {
        return Blocks.STONE.defaultBlockState();
    }

    @Override
    public BlockState getFlowingMixingState() {
        return Blocks.COBBLESTONE.defaultBlockState();
    }

    @Override
    protected boolean canSetFireToSurroundings(Level level, BlockPos pos, BlockState state, RandomSource rand) {
        return true;
    }

    @Override
    public BlockState getFlowingIntoWaterState() {
        return Blocks.STONE.defaultBlockState();
    }
}