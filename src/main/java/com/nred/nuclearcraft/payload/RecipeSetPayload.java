package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.screen.processor.ProcessorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record RecipeSetPayload(ResourceLocation recipeKey) implements CustomPacketPayload {
    public static final Type<RecipeSetPayload> TYPE = new Type<>(ncLoc("recipe_set_server_to_client"));
    public static final StreamCodec<FriendlyByteBuf, RecipeSetPayload> STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, RecipeSetPayload::recipeKey, RecipeSetPayload::new);

    @Override
    public Type<RecipeSetPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(final RecipeSetPayload payload, final IPayloadContext context) {
        if (Minecraft.getInstance().screen instanceof ProcessorScreen<?> screen) {
            screen.recipeKey = payload.recipeKey;
        }
    }
}