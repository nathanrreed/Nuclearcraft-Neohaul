package com.nred.nuclearcraft.block.processor.chemical_reactor;

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

public class ChemicalReactor extends Processor {
    public ChemicalReactor(Properties properties) {
        super(properties, "chemical_reactor");

        particles = List.of(DustParticleOptions.REDSTONE);
    }

    MapCodec<ChemicalReactor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec()).apply(instance, ChemicalReactor::new)
    );

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChemicalReactorEntity(pos, state);
    }
}