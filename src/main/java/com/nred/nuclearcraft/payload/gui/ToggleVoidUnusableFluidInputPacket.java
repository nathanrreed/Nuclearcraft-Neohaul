package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ToggleVoidUnusableFluidInputPacket extends TileGuiPacket {
    public static final Type<ToggleVoidUnusableFluidInputPacket> TYPE = new Type<>(ncLoc("toggle_void_unusable_fluid_input_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleVoidUnusableFluidInputPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleVoidUnusableFluidInputPacket::toBytes, ToggleVoidUnusableFluidInputPacket::fromBytes
    );

    protected boolean voidUnusableFluidInput;
    protected int tankNumber;

    public ToggleVoidUnusableFluidInputPacket(ITileFluid tile, int tankNumber) {
        super(tile);
        voidUnusableFluidInput = tile.getVoidUnusableFluidInput(tankNumber);
        this.tankNumber = tankNumber;
    }

    public ToggleVoidUnusableFluidInputPacket(TileGuiPacket tileGuiPacket, boolean voidUnusableFluidInput, int tankNumber) {
        super(tileGuiPacket);
        this.voidUnusableFluidInput = voidUnusableFluidInput;
        this.tankNumber = tankNumber;
    }

    public static ToggleVoidUnusableFluidInputPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        boolean voidUnusableFluidInput = buf.readBoolean();
        int tankNumber = buf.readInt();
        return new ToggleVoidUnusableFluidInputPacket(tileGuiPacket, voidUnusableFluidInput, tankNumber);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(voidUnusableFluidInput);
        buf.writeInt(tankNumber);
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleVoidUnusableFluidInputPacket> {
        public static void handleOnServer(ToggleVoidUnusableFluidInputPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleVoidUnusableFluidInputPacket message, BlockEntity tile) {
            if (tile instanceof ITileFluid machine) {
                machine.setVoidUnusableFluidInput(message.tankNumber, message.voidUnusableFluidInput);
                tile.setChanged();
            }
        }
    }
}