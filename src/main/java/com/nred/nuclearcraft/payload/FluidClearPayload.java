package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record FluidClearPayload(int tank, BlockPos pos) implements CustomPacketPayload {
    public static final Type<FluidClearPayload> TYPE = new Type<>(ncLoc("fluid_clear_client_to_server"));
    public static final StreamCodec<FriendlyByteBuf, FluidClearPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, FluidClearPayload::tank, BlockPos.STREAM_CODEC, FluidClearPayload::pos, FluidClearPayload::new);

    @Override
    public Type<FluidClearPayload> type() {
        return TYPE;
    }

    public static void handleOnServer(final FluidClearPayload payload, final IPayloadContext context) {
        ((ProcessorEntity) Objects.requireNonNull(context.player().level().getBlockEntity(payload.pos))).handleFluidClear(payload.tank);
    }
}