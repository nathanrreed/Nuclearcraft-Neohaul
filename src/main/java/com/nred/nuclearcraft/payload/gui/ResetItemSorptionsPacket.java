package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ResetItemSorptionsPacket extends TileGuiPacket {
    public static final Type<ResetItemSorptionsPacket> TYPE = new Type<>(ncLoc("reset_item_sorptions_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetItemSorptionsPacket> STREAM_CODEC = StreamCodec.ofMember(
            ResetItemSorptionsPacket::toBytes, ResetItemSorptionsPacket::fromBytes
    );
    protected int slot;
    protected boolean defaults;

    public ResetItemSorptionsPacket(ITileInventory tile, int slot, boolean defaults) {
        super(tile);
        this.slot = slot;
        this.defaults = defaults;
    }

    public ResetItemSorptionsPacket(TileGuiPacket tileGuiPacket, int slot, boolean defaults) {
        super(tileGuiPacket);
        this.slot = slot;
        this.defaults = defaults;
    }

    public static ResetItemSorptionsPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int slot = buf.readInt();
        boolean defaults = buf.readBoolean();
        return new ResetItemSorptionsPacket(tileGuiPacket, slot, defaults);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(slot);
        buf.writeBoolean(defaults);
    }

    public static class Handler extends TileGuiPacket.Handler<ResetItemSorptionsPacket> {
        public static void handleOnServer(ResetItemSorptionsPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ResetItemSorptionsPacket message, BlockEntity tile) {
            if (tile instanceof ITileInventory machine) {
                for (Direction side : Direction.values()) {
                    if (message.defaults) {
                        machine.setItemSorption(side, message.slot, machine.getInventoryConnection(side).getDefaultItemSorption(message.slot));
                    } else {
                        machine.setItemSorption(side, message.slot, ItemSorption.NON);
                    }
                }
                machine.markDirtyAndNotify(true);
            }
        }
    }
}