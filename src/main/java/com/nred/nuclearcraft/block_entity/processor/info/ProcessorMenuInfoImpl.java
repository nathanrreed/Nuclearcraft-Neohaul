package com.nred.nuclearcraft.block_entity.processor.info;

import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorMenuInfoBuilderImpl.BasicProcessorMenuInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorMenuInfoBuilderImpl.BasicUpgradableProcessorMenuInfoBuilder;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ProcessorMenuInfoImpl {
    public static class BasicProcessorMenuInfo<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET, RECIPE>, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends ProcessorMenuInfo<TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE> {
        public BasicProcessorMenuInfo(BasicProcessorMenuInfoBuilder<TILE, PACKET, RECIPE> builder) {
            super(builder);
        }
    }

    public static class BasicUpgradableProcessorMenuInfo<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET, RECIPE>, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends UpgradableProcessorMenuInfo<TILE, PACKET, BasicUpgradableProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE> {
        public BasicUpgradableProcessorMenuInfo(BasicUpgradableProcessorMenuInfoBuilder<TILE, PACKET, RECIPE> builder) {
            super(builder);
        }
    }
}