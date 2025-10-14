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

public class ResetTankSorptionsPacket extends TileGuiPacket {
    public static final Type<ResetTankSorptionsPacket> TYPE = new Type<>(ncLoc("reset_tank_sorptions_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetTankSorptionsPacket> STREAM_CODEC = StreamCodec.ofMember(
            ResetTankSorptionsPacket::toBytes, ResetTankSorptionsPacket::fromBytes
    );

    protected int tank;
    protected boolean defaults;

    public ResetTankSorptionsPacket(ITileFluid tile, int tank, boolean defaults) {
        super(tile);
        this.tank = tank;
        this.defaults = defaults;
    }

    public ResetTankSorptionsPacket(TileGuiPacket tileGuiPacket, int tank, boolean defaults) {
        super(tileGuiPacket);
        this.tank = tank;
        this.defaults = defaults;
    }

    public static ResetTankSorptionsPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int tank = buf.readInt();
        boolean defaults = buf.readBoolean();
        return new ResetTankSorptionsPacket(tileGuiPacket, tank, defaults);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(tank);
        buf.writeBoolean(defaults);
    }

    public static class Handler extends TileGuiPacket.Handler<ResetTankSorptionsPacket> {
        public static void handleOnServer(ResetTankSorptionsPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, player, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ResetTankSorptionsPacket message, ServerPlayer player, BlockEntity tile) {
            if (tile instanceof ITileFluid machine) {
                for (Direction side : Direction.values()) {
                    if (message.defaults) {
                        machine.setTankSorption(side, message.tank, machine.getFluidConnection(side).getDefaultTankSorption(message.tank));
                    } else {
                        machine.setTankSorption(side, message.tank, TankSorption.NON);
                    }
                }
                machine.markDirtyAndNotify(true);
            }
        }
    }
}