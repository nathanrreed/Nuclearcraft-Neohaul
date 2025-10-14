package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block.ITileGui;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class OpenTileGuiPacket extends TileGuiPacket {
    public static final Type<OpenTileGuiPacket> TYPE = new Type<>(ncLoc("open_tile_gui_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenTileGuiPacket> STREAM_CODEC = StreamCodec.ofMember(
            OpenTileGuiPacket::toBytes, OpenTileGuiPacket::fromBytes
    );

    public OpenTileGuiPacket(ITileGui<?, ?, ?> tile) {
        super(tile);
    }

    public OpenTileGuiPacket(TileGuiPacket tileGuiPacket) {
        super(tileGuiPacket);
    }

    public static OpenTileGuiPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        return new OpenTileGuiPacket(tileGuiPacket);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
    }

    public static class Handler extends TileGuiPacket.Handler<OpenTileGuiPacket> {
        public static void handleOnServer(OpenTileGuiPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, player, level.getBlockEntity(payload.pos));
            });
        }
        protected static void onPacket(OpenTileGuiPacket message, ServerPlayer player, BlockEntity tile) {
            if (tile instanceof ITileGui<?, ?, ?> tileGui) {
                //TODO
//                FMLNetworkHandler.openGui(player, NuclearCraft.instance, tileGui.getContainerInfo().guiId, player.getServerWorld(), message.pos.getX(), message.pos.getY(), message.pos.getZ());
                tileGui.addTileUpdatePacketListener(player);
            }
        }
    }
}