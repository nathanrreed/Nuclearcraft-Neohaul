package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankOutputSetting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ToggleVoidExcessFluidOutputPacket extends TileGuiPacket {
    public static final Type<ToggleVoidExcessFluidOutputPacket> TYPE = new Type<>(ncLoc("toggle_void_excess_fluid_output_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleVoidExcessFluidOutputPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleVoidExcessFluidOutputPacket::toBytes, ToggleVoidExcessFluidOutputPacket::fromBytes
    );

    protected int voidExcessFluidOutput;
    protected int tankNumber;

    public ToggleVoidExcessFluidOutputPacket(ITileFluid tile, int tankNumber) {
        super(tile);
        voidExcessFluidOutput = tile.getTankOutputSetting(tankNumber).ordinal();
        this.tankNumber = tankNumber;
    }

    public ToggleVoidExcessFluidOutputPacket(TileGuiPacket tileGuiPacket, int voidExcessFluidOutput, int tankNumber) {
        super(tileGuiPacket);
        this.voidExcessFluidOutput = voidExcessFluidOutput;
        this.tankNumber = tankNumber;
    }

    public static ToggleVoidExcessFluidOutputPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int voidExcessFluidOutput = buf.readInt();
        int tankNumber = buf.readInt();
        return new ToggleVoidExcessFluidOutputPacket(tileGuiPacket, voidExcessFluidOutput, tankNumber);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(voidExcessFluidOutput);
        buf.writeInt(tankNumber);
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleVoidExcessFluidOutputPacket> {
        public static void handleOnServer(ToggleVoidExcessFluidOutputPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleVoidExcessFluidOutputPacket message, BlockEntity tile) {
            if (tile instanceof ITileFluid machine) {
                machine.setTankOutputSetting(message.tankNumber, TankOutputSetting.values()[message.voidExcessFluidOutput]);
                tile.setChanged();
            }
        }
    }
}