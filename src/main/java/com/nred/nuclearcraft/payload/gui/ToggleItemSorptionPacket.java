package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ToggleItemSorptionPacket extends TileGuiPacket {
    public static final Type<ToggleItemSorptionPacket> TYPE = new Type<>(ncLoc("toggle_item_sorption_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleItemSorptionPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleItemSorptionPacket::toBytes, ToggleItemSorptionPacket::fromBytes
    );

    protected int side;
    protected int slot;
    protected int sorption;

    public ToggleItemSorptionPacket(ITileInventory tile, Direction side, int slot, ItemSorption sorption) {
        super(tile);
        this.side = side.ordinal();
        this.slot = slot;
        this.sorption = sorption.ordinal();
    }

    public ToggleItemSorptionPacket(TileGuiPacket tileGuiPacket, int side, int slot, int sorption) {
        super(tileGuiPacket);
        this.side = side;
        this.slot = slot;
        this.sorption = sorption;
    }

    public static ToggleItemSorptionPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int side = buf.readInt();
        int slot = buf.readInt();
        int sorption = buf.readInt();
        return new ToggleItemSorptionPacket(tileGuiPacket, side, slot, sorption);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(side);
        buf.writeInt(slot);
        buf.writeInt(sorption);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleItemSorptionPacket> {
        public static void handleOnServer(ToggleItemSorptionPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleItemSorptionPacket message, BlockEntity tile) {
            if (tile instanceof ITileInventory machine) {
                machine.setItemSorption(Direction.from3DDataValue(message.side), message.slot, ItemSorption.values()[message.sorption]);
                machine.markDirtyAndNotify(true);
            }
        }
    }
}