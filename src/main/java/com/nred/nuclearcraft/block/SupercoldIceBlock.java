package com.nred.nuclearcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.DamageTypeRegistration.HYPOTHERMIA;

public class SupercoldIceBlock extends IceBlock {
    public SupercoldIceBlock() {
        super(BlockBehaviour.Properties.of().friction(0.99F).sound(SoundType.GLASS).noOcclusion().strength(0.5f).isValidSpawn((state, level, pos, value) -> false).isRedstoneConductor((state, level, pos) -> false));
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        DamageSource hypothermia = new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(HYPOTHERMIA)) {
            @Override
            public boolean is(TagKey<DamageType> damageTypeKey) {
                return damageTypeKey == DamageTypeTags.IS_FREEZING || damageTypeKey == DamageTypeTags.NO_KNOCKBACK;
            }

            @Override
            public boolean is(ResourceKey<DamageType> damageTypeKey) {
                return damageTypeKey == DamageTypes.FREEZE;
            }
        };
        entity.hurt(hypothermia, 2.0f);

        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        level.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}