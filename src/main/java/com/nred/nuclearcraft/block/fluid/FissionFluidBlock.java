package com.nred.nuclearcraft.block.fluid;

import com.nred.nuclearcraft.helpers.SimpleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import static com.nred.nuclearcraft.registration.BlockRegistration.WASTELAND_EARTH;

public class FissionFluidBlock extends MoltenFluidBlock {
    public FissionFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.setRemainingFireTicks(10);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(SimpleHelper.newEffect(MobEffects.WEAKNESS, 1, 100));
            livingEntity.addEffect(SimpleHelper.newEffect(MobEffects.POISON, 1, 100));
        }
    }

    @Override
    public BlockState getFlowingIntoWaterState() {
        return WASTELAND_EARTH.get().defaultBlockState();
    }
}