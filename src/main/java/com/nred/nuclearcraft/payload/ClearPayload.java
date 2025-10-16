package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record ClearPayload(int tank, BlockPos pos) implements CustomPacketPayload {
    public ClearPayload(BlockPos pos) {
        this(0, pos);
    }

    public static final Type<ClearPayload> TYPE = new Type<>(ncLoc("clear_client_to_server"));
    public static final StreamCodec<FriendlyByteBuf, ClearPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, ClearPayload::tank, BlockPos.STREAM_CODEC, ClearPayload::pos, ClearPayload::new);

    @Override
    public Type<ClearPayload> type() {
        return TYPE;
    }

    public static void handleOnServer(final ClearPayload payload, final IPayloadContext context) {
        BlockEntity be = context.player().level().getBlockEntity(payload.pos);

        if (be instanceof ProcessorEntity processor) {
            processor.handleFluidClear(payload.tank);
        }
    }
}