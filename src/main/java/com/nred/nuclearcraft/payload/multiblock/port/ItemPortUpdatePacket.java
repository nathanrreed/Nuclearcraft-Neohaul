package com.nred.nuclearcraft.payload.multiblock.port;

import com.nred.nuclearcraft.block_entity.ITilePacket;
import com.nred.nuclearcraft.payload.TileUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ItemPortUpdatePacket extends TileUpdatePacket {
    public static final Type<ItemPortUpdatePacket> TYPE = new Type<>(ncLoc("item_port_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPortUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            ItemPortUpdatePacket::toBytes, ItemPortUpdatePacket::fromBytes
    );

    public BlockPos masterPortPos;
    public List<ItemStack> filterStacks;

    public ItemPortUpdatePacket(BlockPos pos, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks) {
        super(pos);
        this.masterPortPos = masterPortPos;
        this.filterStacks = filterStacks;
    }

    public ItemPortUpdatePacket(TileUpdatePacket tileUpdatePacket, BlockPos masterPortPos, List<ItemStack> filterStacks) {
        super(tileUpdatePacket);
        this.masterPortPos = masterPortPos;
        this.filterStacks = filterStacks;
    }

    public static ItemPortUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        TileUpdatePacket tileUpdatePacket = TileUpdatePacket.fromBytes(buf);
        BlockPos masterPortPos = readPos(buf);
        List<ItemStack> filterStacks = readStacks(buf);
        return new ItemPortUpdatePacket(tileUpdatePacket, masterPortPos, filterStacks);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writePos(buf, masterPortPos);
        writeStacks(buf, filterStacks);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends TileUpdatePacket.Handler<ItemPortUpdatePacket, ITilePacket<ItemPortUpdatePacket>> {
    }
}
