package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.ITilePacket;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.payload.TileUpdatePacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class PebbleFissionChamberUpdatePacket extends ProcessorUpdatePacket {
    public static final CustomPacketPayload.Type<PebbleFissionChamberUpdatePacket> TYPE = new CustomPacketPayload.Type<>(ncLoc("pebble_fission_chamber_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PebbleFissionChamberUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            PebbleFissionChamberUpdatePacket::toBytes, PebbleFissionChamberUpdatePacket::fromBytes
    );
    public BlockPos masterPortPos;
    public List<ItemStack> filterStacks;
    public long clusterHeatStored, clusterHeatCapacity;


    public PebbleFissionChamberUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks, FissionCluster cluster) {
        super(pos, isProcessing, time, baseProcessTime, tanks);
        this.masterPortPos = masterPortPos;
        this.filterStacks = filterStacks;
        clusterHeatStored = cluster == null ? -1L : cluster.heatBuffer.getHeatStored();
        clusterHeatCapacity = cluster == null ? -1L : cluster.heatBuffer.getHeatCapacity();
    }

    public PebbleFissionChamberUpdatePacket(ProcessorUpdatePacket processorUpdatePacket, BlockPos masterPortPos, List<ItemStack> filterStacks, long clusterHeatStored, long clusterHeatCapacity) {
        super(processorUpdatePacket);
        this.masterPortPos = masterPortPos;
        this.filterStacks = filterStacks;
        this.clusterHeatStored = clusterHeatStored;
        this.clusterHeatCapacity = clusterHeatCapacity;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PebbleFissionChamberUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        ProcessorUpdatePacket processorUpdatePacket = ProcessorUpdatePacket.fromBytes(buf);
        BlockPos masterPortPos = readPos(buf);
        List<ItemStack> filterStacks = readStacks(buf);
        long clusterHeatStored = buf.readLong();
        long clusterHeatCapacity = buf.readLong();
        return new PebbleFissionChamberUpdatePacket(processorUpdatePacket, masterPortPos, filterStacks, clusterHeatStored, clusterHeatCapacity);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writePos(buf, masterPortPos);
        writeStacks(buf, filterStacks);
        buf.writeLong(clusterHeatStored);
        buf.writeLong(clusterHeatCapacity);
    }

    public static class Handler extends TileUpdatePacket.Handler<PebbleFissionChamberUpdatePacket, ITilePacket<PebbleFissionChamberUpdatePacket>> {
    }
}