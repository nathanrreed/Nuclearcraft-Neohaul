package com.nred.nuclearcraft.payload.processor;

import com.nred.nuclearcraft.block.internal.fluid.Tank;
import com.nred.nuclearcraft.block.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.payload.TileUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ProcessorUpdatePacket extends TileUpdatePacket {
    public static final Type<EnergyProcessorUpdatePacket> TYPE = new Type<>(ncLoc("processor_update_packet"));
    public boolean isProcessing;
    public double time;
    public double baseProcessTime;
    public List<TankInfo> tankInfos;

    public ProcessorUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks) {
        super(pos);
        this.isProcessing = isProcessing;
        this.time = time;
        this.baseProcessTime = baseProcessTime;
        this.tankInfos = TankInfo.getInfoList(tanks);
    }

    public ProcessorUpdatePacket(TileUpdatePacket tileUpdatePacket, boolean isProcessing, double time, double baseProcessTime, List<TankInfo> tankInfos) {
        super(tileUpdatePacket.pos);
        this.isProcessing = isProcessing;
        this.time = time;
        this.baseProcessTime = baseProcessTime;
        this.tankInfos = tankInfos;
    }

    public ProcessorUpdatePacket(ProcessorUpdatePacket processorUpdatePacket) {
        super(processorUpdatePacket.pos);
        this.isProcessing = processorUpdatePacket.isProcessing;
        this.time = processorUpdatePacket.time;
        this.baseProcessTime = processorUpdatePacket.baseProcessTime;
        this.tankInfos = processorUpdatePacket.tankInfos;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static ProcessorUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileUpdatePacket tileUpdatePacket = TileUpdatePacket.fromBytes(buf);
        boolean isProcessing = buf.readBoolean();
        double time = buf.readDouble();
        double baseProcessTime = buf.readDouble();
        List<TankInfo> tankInfos = readTankInfos(buf);

        return new ProcessorUpdatePacket(tileUpdatePacket, isProcessing, time, baseProcessTime, tankInfos);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isProcessing);
        buf.writeDouble(time);
        buf.writeDouble(baseProcessTime);
        writeTankInfos(buf, tankInfos);
    }
}