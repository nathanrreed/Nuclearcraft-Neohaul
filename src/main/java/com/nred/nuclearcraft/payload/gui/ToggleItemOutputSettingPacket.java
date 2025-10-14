package com.nred.nuclearcraft.payload.gui;

import com.nred.nuclearcraft.block.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block.inventory.ITileInventory;
import com.nred.nuclearcraft.payload.multiblock.ClearAllMaterialPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ToggleItemOutputSettingPacket extends TileGuiPacket {
    public static final Type<ToggleItemOutputSettingPacket> TYPE = new Type<>(ncLoc("toggle_item_output_setting_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleItemOutputSettingPacket> STREAM_CODEC = StreamCodec.ofMember(
            ToggleItemOutputSettingPacket::toBytes, ToggleItemOutputSettingPacket::fromBytes
    );

    protected int slot;
    protected int setting;

    public ToggleItemOutputSettingPacket(ITileInventory tile, int slot, ItemOutputSetting setting) {
        super(tile);
        this.slot = slot;
        this.setting = setting.ordinal();
    }

    public ToggleItemOutputSettingPacket(TileGuiPacket tileGuiPacket, int slot, int setting) {
        super(tileGuiPacket);
        this.slot = slot;
        this.setting = setting;
    }

    public static ToggleItemOutputSettingPacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileGuiPacket tileGuiPacket = TileGuiPacket.fromBytes(buf);
        int slot = buf.readInt();
        int setting = buf.readInt();
        return new ToggleItemOutputSettingPacket(tileGuiPacket, slot, setting);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(slot);
        buf.writeInt(setting);
    }

    public static class Handler extends TileGuiPacket.Handler<ToggleItemOutputSettingPacket> {
        public static void handleOnServer(ToggleItemOutputSettingPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                Level level = player.level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(player, payload.pos)) {
                    return;
                }
                onPacket(payload, level.getBlockEntity(payload.pos));
            });
        }

        protected static void onPacket(ToggleItemOutputSettingPacket message, BlockEntity tile) {
            if (tile instanceof ITileInventory machine) {
                ItemOutputSetting setting = ItemOutputSetting.values()[message.setting];
                machine.setItemOutputSetting(message.slot, setting);
                if (setting == ItemOutputSetting.VOID) {
                    machine.getInventoryStacks().set(message.slot, ItemStack.EMPTY);
                }
                machine.markDirtyAndNotify(true);
            }
        }
    }
}