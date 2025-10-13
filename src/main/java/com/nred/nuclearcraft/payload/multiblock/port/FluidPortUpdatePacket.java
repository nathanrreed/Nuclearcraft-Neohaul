package com.nred.nuclearcraft.payload.multiblock.port;

import com.nred.nuclearcraft.block.ITilePacket;
import com.nred.nuclearcraft.block.internal.fluid.Tank;
import com.nred.nuclearcraft.block.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.payload.TileUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public final class FluidPortUpdatePacket extends TileUpdatePacket {
    public static final Type<FluidPortUpdatePacket> TYPE = new Type<>(ncLoc("fluid_port_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidPortUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            FluidPortUpdatePacket::toBytes, FluidPortUpdatePacket::fromBytes
    );

    public BlockPos masterPortPos;
    public List<TankInfo> tankInfos;
    public List<TankInfo> filterTankInfos;

    public FluidPortUpdatePacket(BlockPos pos, BlockPos masterPortPos, List<Tank> tanks, List<Tank> filterTanks) {
        super(pos);
        this.masterPortPos = masterPortPos;
        this.tankInfos = TankInfo.getInfoList(tanks);
        this.filterTankInfos = TankInfo.getInfoList(filterTanks);
    }

    public FluidPortUpdatePacket(TileUpdatePacket tileUpdatePacket, BlockPos masterPortPos, List<TankInfo> tankInfos, List<TankInfo> filterTankInfos) {
        super(tileUpdatePacket);
        this.masterPortPos = masterPortPos;
        this.tankInfos = tankInfos;
        this.filterTankInfos = filterTankInfos;
    }

    public static FluidPortUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileUpdatePacket tileUpdatePacket = TileUpdatePacket.fromBytes(buf);
        BlockPos masterPortPos = readPos(buf);
        List<TankInfo> tankInfos = readTankInfos(buf);
        List<TankInfo> filterTankInfos = readTankInfos(buf);
        return new FluidPortUpdatePacket(tileUpdatePacket, masterPortPos, tankInfos, filterTankInfos);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writePos(buf, masterPortPos);
        writeTankInfos(buf, tankInfos);
        writeTankInfos(buf, filterTankInfos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends TileUpdatePacket.Handler<FluidPortUpdatePacket, ITilePacket<FluidPortUpdatePacket>> {
    }
}