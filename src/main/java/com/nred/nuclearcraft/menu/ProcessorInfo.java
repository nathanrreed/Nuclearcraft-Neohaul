package com.nred.nuclearcraft.menu;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

public record ProcessorInfo(BlockPos pos, boolean redstoneMode, String typeName) {

    public static ProcessorInfo read(FriendlyByteBuf buffer) {
        return new ProcessorInfo(buffer.readBlockPos(), buffer.readBoolean(), buffer.readUtf());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        ByteBuf byteBuf = Unpooled.buffer();
        buf.writeBytes(new FriendlyByteBuf(byteBuf).writeLong(pos.asLong()).writeBoolean(redstoneMode).writeUtf(typeName).array());
    }
}
