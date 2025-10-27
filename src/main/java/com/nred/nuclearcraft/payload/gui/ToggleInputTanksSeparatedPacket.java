package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ToggleInputTanksSeparatedPacket extends TileGuiPacket {
    public static final Type<ToggleInputTanksSeparatedPacket> TYPE = new Type<>(ncLoc("toggle_input_tanks_separated_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleInputTanksSeparatedPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleInputTanksSeparatedPacket::toBytes, ToggleInputTanksSeparatedPacket::fromBytes
    );

    protected boolean inputTanksSeparated;

    public ToggleInputTanksSeparatedPacket(ITileFluid tile) {
        super(tile);
        inputTanksSeparated = tile.getInputTanksSeparated();
    }

    public ToggleInputTanksSeparatedPacket(TileGuiPacket tileGuiPacket, boolean inputTanksSeparated) {
        super(tileGuiPacket);
        this.inputTanksSeparated = inputTanksSeparated;
    }

    public static ToggleInputTanksSeparatedPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        boolean inputTanksSeparated = buf.readBoolean();
        return new ToggleInputTanksSeparatedPacket(tileGuiPacket, inputTanksSeparated);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(inputTanksSeparated);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleInputTanksSeparatedPacket> {
        public static void handleOnServer(ToggleInputTanksSeparatedPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleInputTanksSeparatedPacket message, BlockEntity tile) {
            if (tile instanceof ITileFluid machine) {
                machine.setInputTanksSeparated(message.inputTanksSeparated);
                tile.setChanged();
            }
        }
    }
}