package com.nred.nuclearcraft.block.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.block_entity.processor.NuclearFurnaceEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.NUCLEAR_FURNACE_ENTITY_TYPE;

public class NuclearFurnaceBlock extends AbstractFurnaceBlock {
    public NuclearFurnaceBlock(Properties properties) {
        super(properties);
    }

    MapCodec<NuclearFurnaceBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec()).apply(instance, NuclearFurnaceBlock::new)
    );

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof NuclearFurnaceEntity) {
            player.openMenu((MenuProvider) blockentity);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NuclearFurnaceEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, NUCLEAR_FURNACE_ENTITY_TYPE.get(), NuclearFurnaceEntity::serverTick);
    }
}