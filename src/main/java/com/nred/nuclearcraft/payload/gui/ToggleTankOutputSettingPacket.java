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

public class ToggleTankOutputSettingPacket extends TileGuiPacket {
    public static final Type<ToggleTankOutputSettingPacket> TYPE = new Type<>(ncLoc("toggle_tank_output_setting_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleTankOutputSettingPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleTankOutputSettingPacket::toBytes, ToggleTankOutputSettingPacket::fromBytes
    );

    protected int tank;
    protected int setting;

    public ToggleTankOutputSettingPacket(ITileFluid tile, int tank, TankOutputSetting setting) {
        super(tile);
        this.tank = tank;
        this.setting = setting.ordinal();
    }

    public ToggleTankOutputSettingPacket(TileGuiPacket tileGuiPacket, int tank, int setting) {
        super(tileGuiPacket);
        this.tank = tank;
        this.setting = setting;
    }

    public static ToggleTankOutputSettingPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int tank = buf.readInt();
        int setting = buf.readInt();
        return new ToggleTankOutputSettingPacket(tileGuiPacket, tank, setting);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(tank);
        buf.writeInt(setting);
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleTankOutputSettingPacket> {
        public static void handleOnServer(ToggleTankOutputSettingPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleTankOutputSettingPacket message, BlockEntity tile) {
            if (tile instanceof ITileFluid machine) {
                TankOutputSetting setting = TankOutputSetting.values()[message.setting];
                machine.setTankOutputSetting(message.tank, setting);
                if (setting == TankOutputSetting.VOID) {
                    machine.clearTank(message.tank);
                }
                machine.markDirtyAndNotify(true);
            }
        }
    }
}