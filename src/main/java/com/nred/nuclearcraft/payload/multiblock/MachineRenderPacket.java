package com.nred.nuclearcraft.payload.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class MachineRenderPacket extends MultiblockUpdatePacket {
    public boolean isMachineOn;

    public MachineRenderPacket(BlockPos pos, boolean isMachineOn) {
        super(pos);
        this.isMachineOn = isMachineOn;
    }

    public MachineRenderPacket(MultiblockUpdatePacket multiblockUpdatePacket, boolean isMachineOn) {
        super(multiblockUpdatePacket);
        this.isMachineOn = isMachineOn;
    }

    public static MachineRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        MultiblockUpdatePacket multiblockUpdatePacket = MultiblockUpdatePacket.fromBytes(buf);
        boolean isMachineOn = buf.readBoolean();
        return new MachineRenderPacket(multiblockUpdatePacket, isMachineOn);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isMachineOn);
    }
}