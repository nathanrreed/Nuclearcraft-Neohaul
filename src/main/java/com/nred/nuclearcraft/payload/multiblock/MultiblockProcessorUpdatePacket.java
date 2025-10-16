package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public abstract class MultiblockProcessorUpdatePacket extends ProcessorUpdatePacket {
    public MultiblockProcessorUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks) {
        super(pos, isProcessing, time, baseProcessTime, tanks);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static MultiblockProcessorUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        return (MultiblockProcessorUpdatePacket) ProcessorUpdatePacket.fromBytes(buf);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
    }
}