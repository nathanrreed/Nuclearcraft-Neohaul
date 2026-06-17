package com.nred.nuclearcraft.block_entity.processor.info.builder;

import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.menu.MenuFunction;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class ProcessorMenuInfoBuilderImpl {
    public static class BasicProcessorMenuInfoBuilder<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET, RECIPE>, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends ProcessorMenuInfoBuilder<TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE, BasicProcessorMenuInfoBuilder<TILE, PACKET, RECIPE>> {
        public BasicProcessorMenuInfoBuilder(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier, MenuFunction<TILE> menuFunction) {
            super(name, tileClass, tileSupplier, menuFunction);
        }

        @Override
        public BasicProcessorMenuInfo<TILE, PACKET, RECIPE> buildContainerInfo() {
            return new BasicProcessorMenuInfo<>(this);
        }
    }

    public static class BasicUpgradableProcessorMenuInfoBuilder<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET, RECIPE>, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends UpgradableProcessorMenuInfoBuilder<TILE, PACKET, BasicUpgradableProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE, BasicUpgradableProcessorMenuInfoBuilder<TILE, PACKET, RECIPE>> {
        public BasicUpgradableProcessorMenuInfoBuilder(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier, MenuFunction<TILE> menuFunction) {
            super(name, tileClass, tileSupplier, menuFunction);
        }

        @Override
        public BasicUpgradableProcessorMenuInfo<TILE, PACKET, RECIPE> buildContainerInfo() {
            return new BasicUpgradableProcessorMenuInfo<>(this);
        }
    }
}