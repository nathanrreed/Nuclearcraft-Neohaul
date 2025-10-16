package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.ITilePacket;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.payload.TileUpdatePacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class SaltFissionVesselUpdatePacket extends ProcessorUpdatePacket {
    public static final Type<SaltFissionVesselUpdatePacket> TYPE = new Type<>(ncLoc("salt_fission_vessel_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SaltFissionVesselUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            SaltFissionVesselUpdatePacket::toBytes, SaltFissionVesselUpdatePacket::fromBytes
    );

    public BlockPos masterPortPos;
    public List<TankInfo> filterTankInfos;
    public long clusterHeatStored, clusterHeatCapacity;

    public SaltFissionVesselUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks, BlockPos masterPortPos, List<Tank> filterTanks, FissionCluster cluster) {
        super(pos, isProcessing, time, baseProcessTime, tanks);
        this.masterPortPos = masterPortPos;
        filterTankInfos = TankInfo.getInfoList(filterTanks);
        clusterHeatStored = cluster == null ? -1L : cluster.heatBuffer.getHeatStored();
        clusterHeatCapacity = cluster == null ? -1L : cluster.heatBuffer.getHeatCapacity();
    }

    public SaltFissionVesselUpdatePacket(ProcessorUpdatePacket processorUpdatePacket, BlockPos masterPortPos, List<TankInfo> filterTanks, long clusterHeatStored, long clusterHeatCapacity) {
        super(processorUpdatePacket);
        this.masterPortPos = masterPortPos;
        this.filterTankInfos = filterTanks;
        this.clusterHeatStored = clusterHeatStored;
        this.clusterHeatCapacity = clusterHeatCapacity;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static SaltFissionVesselUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        ProcessorUpdatePacket processorUpdatePacket = ProcessorUpdatePacket.fromBytes(buf);
        BlockPos masterPortPos = readPos(buf);
        List<TankInfo> filterTankInfos = readTankInfos(buf);
        long clusterHeatStored = buf.readLong();
        long clusterHeatCapacity = buf.readLong();
        return new SaltFissionVesselUpdatePacket(processorUpdatePacket, masterPortPos, filterTankInfos, clusterHeatStored, clusterHeatCapacity);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writePos(buf, masterPortPos);
        writeTankInfos(buf, filterTankInfos);
        buf.writeLong(clusterHeatStored);
        buf.writeLong(clusterHeatCapacity);
    }

    public static class Handler extends TileUpdatePacket.Handler<SaltFissionVesselUpdatePacket, ITilePacket<SaltFissionVesselUpdatePacket>> {
    }
}