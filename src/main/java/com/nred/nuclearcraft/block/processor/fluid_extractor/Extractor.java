package com.nred.nuclearcraft.block.processor.fluid_extractor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.block.processor.Processor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Extractor extends Processor {
    public Extractor(Properties properties) {
        super(properties, "fluid_extractor");

        particles = List.of(ParticleTypes.ASH, DustParticleOptions.REDSTONE);
    }

    MapCodec<Extractor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec()).apply(instance, Extractor::new)
    );

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExtractorEntity(pos, state);
    }
}