package com.nred.nuclearcraft.menu;

import com.nred.nuclearcraft.helpers.CustomFluidStackHandler;
import com.nred.nuclearcraft.helpers.CustomItemStackHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public record ProcessorInfo(BlockPos pos, boolean redstoneMode, ArrayList<Integer> itemOutputSettings, ArrayList<Integer> fluidOutputSettings, String typeName) {
    public ProcessorInfo(BlockPos pos, boolean redstoneMode, CustomItemStackHandler itemStackHandler, CustomFluidStackHandler fluidStackHandler, String typeName) {
        this(pos, redstoneMode, new ArrayList<>(itemStackHandler.outputSettings.stream().map(Enum::ordinal).toList()), new ArrayList<>(fluidStackHandler.outputSettings.stream().map(Enum::ordinal).toList()), typeName);
    }

    public static ProcessorInfo read(FriendlyByteBuf buffer) {
        return new ProcessorInfo(buffer.readBlockPos(), buffer.readBoolean(), new ArrayList<>(Arrays.stream(buffer.readVarIntArray()).boxed().collect(Collectors.toList())), new ArrayList<>(Arrays.stream(buffer.readVarIntArray()).boxed().collect(Collectors.toList())), buffer.readUtf());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        ByteBuf byteBuf = Unpooled.buffer();
        buf.writeBytes(new FriendlyByteBuf(byteBuf).writeLong(pos.asLong()).writeBoolean(redstoneMode).writeVarIntArray(itemOutputSettings.stream().mapToInt(Integer::intValue).toArray()).writeVarIntArray(fluidOutputSettings.stream().mapToInt(Integer::intValue).toArray()).writeUtf(typeName).array());
    }
}