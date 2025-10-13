package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block.ITilePacket;
import com.nred.nuclearcraft.block.internal.fluid.Tank;
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

public class FissionIrradiatorUpdatePacket extends ProcessorUpdatePacket {
    public static final Type<FissionIrradiatorUpdatePacket> TYPE = new Type<>(ncLoc("fission_irradiator_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FissionIrradiatorUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            FissionIrradiatorUpdatePacket::toBytes, FissionIrradiatorUpdatePacket::fromBytes
    );
    public BlockPos masterPortPos;
    public List<ItemStack> filterStacks;
    public long clusterHeatStored, clusterHeatCapacity;

    public FissionIrradiatorUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks, FissionCluster cluster) {
        super(pos, isProcessing, time, baseProcessTime, tanks);
        this.masterPortPos = masterPortPos;
        this.filterStacks = filterStacks;
        clusterHeatStored = cluster == null ? -1L : cluster.heatBuffer.getHeatStored();
        clusterHeatCapacity = cluster == null ? -1L : cluster.heatBuffer.getHeatCapacity();
    }

    public FissionIrradiatorUpdatePacket(ProcessorUpdatePacket processorUpdatePacket, BlockPos masterPortPos, List<ItemStack> filterStacks, long clusterHeatStored, long clusterHeatCapacity) {
        super(processorUpdatePacket);
        this.masterPortPos = masterPortPos;
        this.filterStacks = filterStacks;
        this.clusterHeatStored = clusterHeatStored;
        this.clusterHeatCapacity = clusterHeatCapacity;
    }

    public static FissionIrradiatorUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        ProcessorUpdatePacket processorUpdatePacket = ProcessorUpdatePacket.fromBytes(buf);
        BlockPos masterPortPos = readPos(buf);
        List<ItemStack> filterStacks = readStacks(buf);
        long clusterHeatStored = buf.readLong();
        long clusterHeatCapacity = buf.readLong();

        return new FissionIrradiatorUpdatePacket(processorUpdatePacket, masterPortPos, filterStacks, clusterHeatStored, clusterHeatCapacity);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writePos(buf, masterPortPos);
        writeStacks(buf, filterStacks);
        buf.writeLong(clusterHeatStored);
        buf.writeLong(clusterHeatCapacity);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends TileUpdatePacket.Handler<FissionIrradiatorUpdatePacket, ITilePacket<FissionIrradiatorUpdatePacket>> {
    }
}