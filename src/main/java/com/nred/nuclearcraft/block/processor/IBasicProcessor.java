package com.nred.nuclearcraft.block.processor;

import com.nred.nuclearcraft.block.info.ProcessorContainerInfoImpl;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IBasicProcessor<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends IProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, PACKET>> {
}