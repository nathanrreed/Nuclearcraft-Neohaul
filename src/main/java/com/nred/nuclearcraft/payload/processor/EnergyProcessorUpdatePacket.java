package com.nred.nuclearcraft.payload.processor;

import com.nred.nuclearcraft.block_entity.ITilePacket;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class EnergyProcessorUpdatePacket extends ProcessorUpdatePacket {
    public static final Type<EnergyProcessorUpdatePacket> TYPE = new Type<>(ncLoc("energy_processor_update_packet"));
    public double baseProcessPower;
    public long energyStored;

    public EnergyProcessorUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks, double baseProcessPower, long energyStored) {
        super(pos, isProcessing, time, baseProcessTime, tanks);
        this.baseProcessPower = baseProcessPower;
        this.energyStored = energyStored;
    }
    public EnergyProcessorUpdatePacket(ProcessorUpdatePacket processorUpdatePacket, double baseProcessPower, long energyStored) {
        super(processorUpdatePacket);
        this.baseProcessPower = baseProcessPower;
        this.energyStored = energyStored;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, EnergyProcessorUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(EnergyProcessorUpdatePacket::toBytes, EnergyProcessorUpdatePacket::fromBytes);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static EnergyProcessorUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        ProcessorUpdatePacket processorUpdatePacket = ProcessorUpdatePacket.fromBytes(buf);
        double baseProcessPower = buf.readDouble();
        long energyStored = buf.readLong();
        return new EnergyProcessorUpdatePacket(processorUpdatePacket, baseProcessPower, energyStored);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(baseProcessPower);
        buf.writeLong(energyStored);
    }

    public static class Handler extends ProcessorUpdatePacket.Handler<EnergyProcessorUpdatePacket, ITilePacket<EnergyProcessorUpdatePacket>> {
    }
}
