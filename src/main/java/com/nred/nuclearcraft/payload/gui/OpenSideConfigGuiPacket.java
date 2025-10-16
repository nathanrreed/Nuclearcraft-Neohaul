package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block_entity.ITileGui;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class OpenSideConfigGuiPacket extends TileGuiPacket {
    public static final Type<OpenSideConfigGuiPacket> TYPE = new Type<>(ncLoc("open_side_config_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenSideConfigGuiPacket> STREAM_CODEC = StreamCodec.ofMember(
            OpenSideConfigGuiPacket::toBytes, OpenSideConfigGuiPacket::fromBytes
    );

    public OpenSideConfigGuiPacket(ITileGui<?, ?, ?> tile) {
        super(tile);
    }

    public OpenSideConfigGuiPacket(TileGuiPacket tileGuiPacket) {
        super(tileGuiPacket);
    }

    public static OpenSideConfigGuiPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        return new OpenSideConfigGuiPacket(tileGuiPacket);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
    }

    public static class Handler extends TileGuiPacket.Handler<OpenSideConfigGuiPacket> {
        public static void handleOnServer(OpenSideConfigGuiPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, player, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(OpenSideConfigGuiPacket message, ServerPlayer player, BlockEntity tile) {
            if (tile instanceof ITileGui<?, ?, ?> tileGui) {
//                player.openMenu() // TODO
//                FMLNetworkHandler.openGui(player, NuclearCraft.instance, tileGui.getContainerInfo().guiId + 1000, player.level(), message.pos.getX(), message.pos.getY(), message.pos.getZ());
                tileGui.addTileUpdatePacketListener(player);
            }
        }
    }
}