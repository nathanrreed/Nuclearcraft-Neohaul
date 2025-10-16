package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block_entity.ITile;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ToggleAlternateComparatorPacket extends TileGuiPacket {
    public static final Type<ToggleAlternateComparatorPacket> TYPE = new Type<>(ncLoc("toggle_alternate_comparator_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleAlternateComparatorPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleAlternateComparatorPacket::toBytes, ToggleAlternateComparatorPacket::fromBytes
    );

    protected boolean alternateComparator;

    public ToggleAlternateComparatorPacket(ITile tile) {
        super(tile);
        alternateComparator = tile.getAlternateComparator();
    }

    public ToggleAlternateComparatorPacket(TileGuiPacket tileGuiPacket, boolean alternateComparator) {
        super(tileGuiPacket);
        this.alternateComparator = alternateComparator;
    }

    public static ToggleAlternateComparatorPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        boolean alternateComparator = buf.readBoolean();
        return new ToggleAlternateComparatorPacket(tileGuiPacket, alternateComparator);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(alternateComparator);
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleAlternateComparatorPacket> {
        public static void handleOnServer(ToggleAlternateComparatorPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleAlternateComparatorPacket message, BlockEntity tile) {
            if (tile instanceof ITile t) {
                t.setAlternateComparator(message.alternateComparator);
                tile.setChanged();
            }
        }
    }
}