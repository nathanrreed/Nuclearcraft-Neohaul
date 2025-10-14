package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block.ITile;
import com.nred.nuclearcraft.payload.NCPacket;
import com.nred.nuclearcraft.payload.render.BlockHighlightUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TileGuiPacket extends NCPacket {
    protected BlockPos pos;

    public TileGuiPacket(BlockPos pos) {
        this.pos = pos;
    }

    public TileGuiPacket(TileGuiPacket pos) {
        this.pos = pos.pos;
    }

    public TileGuiPacket(ITile tile) {
        this(tile.getTilePos());
    }

    public static TileGuiPacket fromBytes(RegistryFriendlyByteBuf buf) {
        BlockPos pos = readPos(buf);
        return new TileGuiPacket(pos);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writePos(buf, pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        throw new RuntimeException("Raw use of TileGuiPacket");
    }


    public static abstract class Handler<MESSAGE extends TileGuiPacket> {
    }
}