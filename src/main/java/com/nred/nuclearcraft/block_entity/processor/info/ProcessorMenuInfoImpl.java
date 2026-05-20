package com.nred.nuclearcraft.block_entity.processor.info;

import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.IBasicUpgradableProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilderImpl.BasicProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilderImpl.BasicUpgradableProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ProcessorMenuInfoImpl {
    public static class BasicProcessorMenuInfo<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ProcessorMenuInfo<TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET>> {
        public BasicProcessorMenuInfo(BasicProcessorContainerInfoBuilder<TILE, PACKET> builder) {
            super(builder);
        }
    }

    public static class BasicUpgradableProcessorMenuInfo<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends UpgradableProcessorMenuInfo<TILE, PACKET, BasicUpgradableProcessorMenuInfo<TILE, PACKET>> {
        public BasicUpgradableProcessorMenuInfo(BasicUpgradableProcessorContainerInfoBuilder<TILE, PACKET> builder) {
            super(builder);
        }
    }
}