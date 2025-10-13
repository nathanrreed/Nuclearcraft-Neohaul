package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.multiblock.IMultiblockController;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MultiblockUpdatePacket extends NCPacket {
    protected BlockPos pos;

    public MultiblockUpdatePacket(BlockPos pos) {
        this.pos = pos;
    }

    public MultiblockUpdatePacket(MultiblockUpdatePacket multiblockUpdatePacket) {
        this.pos = multiblockUpdatePacket.pos;
    }


    public static MultiblockUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        return new MultiblockUpdatePacket(readPos(buf));
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writePos(buf, pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        throw new RuntimeException("Raw use of MultiblockUpdatePacket");
    }

    public static abstract class Handler<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends BlockEntity & IMultiblockController<MULTIBLOCK, PACKET, CONTROLLER, INFO>, INFO extends TileContainerInfo<CONTROLLER>, MESSAGE extends MultiblockUpdatePacket> {
    }
}