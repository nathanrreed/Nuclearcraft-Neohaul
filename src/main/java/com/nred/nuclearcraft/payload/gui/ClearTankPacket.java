package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block.fluid.ITileFluid;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ClearTankPacket extends TileGuiPacket {
    public static final Type<ClearTankPacket> TYPE = new Type<>(ncLoc("clear_tank_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClearTankPacket> STREAM_CODEC = StreamCodec.ofMember(
            ClearTankPacket::toBytes, ClearTankPacket::fromBytes
    );
    protected int tank;

    public ClearTankPacket(ITileFluid tile, int tank) {
        super(tile);
        this.tank = tank;
    }

    public ClearTankPacket(TileGuiPacket tileGuiPacket, int tank) {
        super(tileGuiPacket);
        this.tank = tank;
    }

    public static ClearTankPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int tank = buf.readInt();
        return new ClearTankPacket(tileGuiPacket, tank);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(tank);
    }

    public static class Handler extends TileGuiPacket.Handler<ClearTankPacket> {
        public static void handleOnServer(ClearTankPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ClearTankPacket message, BlockEntity tile) {
            if (tile instanceof ITileFluid machine) {
                machine.clearTank(message.tank);
                tile.setChanged();
            }
        }
    }
}