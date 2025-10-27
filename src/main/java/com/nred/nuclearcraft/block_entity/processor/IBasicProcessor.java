package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IBasicProcessor<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends IProcessor<TILE, PACKET, ProcessorMenuInfoImpl.BasicProcessorMenuInfo<TILE, PACKET>> {
}