package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block.fluid.ITileFluid;
import com.nred.nuclearcraft.block.internal.fluid.TankSorption;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ToggleTankSorptionPacket extends TileGuiPacket {
    public static final Type<ToggleTankSorptionPacket> TYPE = new Type<>(ncLoc("toggle_tank_sorption_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleTankSorptionPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleTankSorptionPacket::toBytes, ToggleTankSorptionPacket::fromBytes
    );

    protected int side;
    protected int tank;
    protected int sorption;

    public ToggleTankSorptionPacket(ITileFluid tile, Direction side, int tank, TankSorption sorption) {
        super(tile);
        this.side = side.ordinal();
        this.tank = tank;
        this.sorption = sorption.ordinal();
    }

    public ToggleTankSorptionPacket(TileGuiPacket tileGuiPacket, int side, int tank, int sorption) {
        super(tileGuiPacket);
        this.side = side;
        this.tank = tank;
        this.sorption = sorption;
    }

    public static ToggleTankSorptionPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int side = buf.readInt();
        int tank = buf.readInt();
        int sorption = buf.readInt();
        return new ToggleTankSorptionPacket(tileGuiPacket, side, tank, sorption);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(side);
        buf.writeInt(tank);
        buf.writeInt(sorption);
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleTankSorptionPacket> {
        public static void handleOnServer(ToggleTankSorptionPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleTankSorptionPacket message, BlockEntity tile) {
            if (tile instanceof ITileFluid machine) {
                machine.setTankSorption(Direction.from3DDataValue(message.side), message.tank, TankSorption.values()[message.sorption]);
                machine.markDirtyAndNotify(true);
            }
        }
    }
}