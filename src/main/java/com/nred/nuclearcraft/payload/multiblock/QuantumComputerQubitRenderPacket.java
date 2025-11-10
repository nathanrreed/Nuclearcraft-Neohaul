package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.quantum.QuantumComputerQubitEntity;
import com.nred.nuclearcraft.payload.TileUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class QuantumComputerQubitRenderPacket extends TileUpdatePacket {
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
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeFloat(measureColor);
    }

    public static class Handler extends TileUpdatePacket.Handler<QuantumComputerQubitRenderPacket, QuantumComputerQubitEntity> {
    }
}