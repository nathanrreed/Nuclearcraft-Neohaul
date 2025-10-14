package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block.ITile;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ToggleRedstoneControlPacket extends TileGuiPacket {
    public static final Type<ToggleRedstoneControlPacket> TYPE = new Type<>(ncLoc("toggle_restone_control_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleRedstoneControlPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleRedstoneControlPacket::toBytes, ToggleRedstoneControlPacket::fromBytes
    );

    protected boolean redstoneControl;

    public ToggleRedstoneControlPacket(ITile tile) {
        super(tile);
        redstoneControl = tile.getRedstoneControl();
    }

    public ToggleRedstoneControlPacket(TileGuiPacket tileGuiPacket, boolean redstoneControl) {
        super(tileGuiPacket);
        this.redstoneControl = redstoneControl;
    }

    public static ToggleRedstoneControlPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        boolean redstoneControl = buf.readBoolean();
        return new ToggleRedstoneControlPacket(tileGuiPacket, redstoneControl);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(redstoneControl);
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleRedstoneControlPacket> {
        public static void handleOnServer(ToggleRedstoneControlPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleRedstoneControlPacket message, BlockEntity tile) {
            if (tile instanceof ITile t) {
                t.setRedstoneControl(message.redstoneControl);
                tile.setChanged();
            }
        }
    }
}