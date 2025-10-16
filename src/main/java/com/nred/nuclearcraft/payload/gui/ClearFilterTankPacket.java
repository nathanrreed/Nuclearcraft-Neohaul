package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block_entity.fission.port.ITileFilteredFluid;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ClearFilterTankPacket extends TileGuiPacket {
    public static final Type<ClearFilterTankPacket> TYPE = new Type<>(ncLoc("clear_filter_tank_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClearFilterTankPacket> STREAM_CODEC = StreamCodec.ofMember(
            ClearFilterTankPacket::toBytes, ClearFilterTankPacket::fromBytes
    );

    protected int tank;

    public ClearFilterTankPacket(ITileFilteredFluid tile, int tank) {
        super(tile);
        this.tank = tank;
    }

    public ClearFilterTankPacket(TileGuiPacket tileGuiPacket, int tank) {
        super(tileGuiPacket);
        this.tank = tank;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static ClearFilterTankPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int tank = buf.readInt();

        return new ClearFilterTankPacket(tileGuiPacket, tank);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(tank);
    }

    public static class Handler extends TileGuiPacket.Handler<ClearFilterTankPacket> {
        public static void handleOnServer(ClearFilterTankPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ClearFilterTankPacket message, BlockEntity tile) {
            if (tile instanceof ITileFilteredFluid machine) {
                machine.clearFilterTank(message.tank);
                tile.setChanged();
            }
        }
    }
}