package com.nred.nuclearcraft.payload.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;

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

    public MachineRenderPacket(MachineRenderPacket machineRenderPacket) {
        super(machineRenderPacket);
        this.isMachineOn = machineRenderPacket.isMachineOn;
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