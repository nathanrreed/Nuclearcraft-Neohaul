package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.quantum.QuantumComputerQubitEntity;
import com.nred.nuclearcraft.payload.TileUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class QuantumComputerQubitRenderPacket extends TileUpdatePacket {
    public static final Type<QuantumComputerQubitRenderPacket> TYPE = new Type<>(ncLoc("quantum_computer_qubit_render_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, QuantumComputerQubitRenderPacket> STREAM_CODEC = StreamCodec.ofMember(
            QuantumComputerQubitRenderPacket::toBytes, QuantumComputerQubitRenderPacket::fromBytes
    );

    public float measureColor;

    public QuantumComputerQubitRenderPacket(BlockPos pos, float measureColor) {
        super(pos);
        this.measureColor = measureColor;
    }

    public QuantumComputerQubitRenderPacket(TileUpdatePacket tileUpdatePacket, float measureColor) {
        super(tileUpdatePacket);
        this.measureColor = measureColor;
    }

    public static QuantumComputerQubitRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileUpdatePacket tileUpdatePacket = TileUpdatePacket.fromBytes(buf);
        float measureColor = buf.readFloat();
        return new QuantumComputerQubitRenderPacket(tileUpdatePacket, measureColor);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeFloat(measureColor);
    }

    public static class Handler extends TileUpdatePacket.Handler<QuantumComputerQubitRenderPacket, QuantumComputerQubitEntity> {
    }
}