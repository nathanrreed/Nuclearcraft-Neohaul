package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block.TilePartAbstract;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ClearAllMaterialPacket extends NCPacket {
    public static final Type<ClearAllMaterialPacket> TYPE = new Type<>(ncLoc("clear_all_material_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClearAllMaterialPacket> STREAM_CODEC = StreamCodec.ofMember(
            ClearAllMaterialPacket::toBytes, ClearAllMaterialPacket::fromBytes
    );
    protected BlockPos pos;

    public ClearAllMaterialPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static ClearAllMaterialPacket fromBytes(RegistryFriendlyByteBuf buf) {
        return new ClearAllMaterialPacket(readPos(buf));
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        writePos(buf, pos);
    }

    public static class Handler {
        public static void handleOnServer(ClearAllMaterialPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                Level level = context.player().level();
                if (!level.isLoaded(payload.pos) || !level.mayInteract(context.player(), payload.pos)) {
                    return;
                }
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof TilePartAbstract<?> part) {
                    part.getMultiblockController().ifPresent(multiblock -> ((Multiblock<?>) multiblock).clearAllMaterial());
                }
            });
        }
    }
}