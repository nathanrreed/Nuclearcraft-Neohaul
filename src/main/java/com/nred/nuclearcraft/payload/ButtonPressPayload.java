package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.enumm.ButtonEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record ButtonPressPayload(ButtonEnum id, BlockPos pos) implements CustomPacketPayload {
    public static final Type<ButtonPressPayload> TYPE = new Type<>(ncLoc("button_press_client_to_server"));
    public static final StreamCodec<FriendlyByteBuf, ButtonPressPayload> STREAM_CODEC = StreamCodec.composite(NeoForgeStreamCodecs.enumCodec(ButtonEnum.class), ButtonPressPayload::id, BlockPos.STREAM_CODEC, ButtonPressPayload::pos, ButtonPressPayload::new);

    @Override
    public Type<ButtonPressPayload> type() {
        return TYPE;
    }

    public static void handleOnServer(final ButtonPressPayload payload, final IPayloadContext context) {
        ((ProcessorEntity) Objects.requireNonNull(context.player().level().getBlockEntity(payload.pos))).handleButtonPress(payload.id);
    }
}