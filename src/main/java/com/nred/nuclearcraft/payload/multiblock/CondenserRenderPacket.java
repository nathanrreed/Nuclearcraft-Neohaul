package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.hx.CondenserControllerEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class CondenserRenderPacket extends HeatExchangerRenderPacket {
    public static final Type<CondenserRenderPacket> TYPE = new Type<>(ncLoc("condensor_render_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CondenserRenderPacket> STREAM_CODEC = StreamCodec.ofMember(
            CondenserRenderPacket::toBytes, CondenserRenderPacket::fromBytes
    );

    public CondenserRenderPacket(BlockPos pos, List<Tank> shellTanks) {
        super(pos, shellTanks);
    }

    public CondenserRenderPacket(HeatExchangerRenderPacket hx) {
        super(hx.shellTankInfos, hx.pos);
    }

    public static CondenserRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        return new CondenserRenderPacket(HeatExchangerRenderPacket.fromBytes(buf));
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, HeatExchangerUpdatePacket, CondenserControllerEntity, TileContainerInfo<CondenserControllerEntity>, CondenserRenderPacket> {
        public static void handleOnClient(CondenserRenderPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof CondenserControllerEntity entity) {
                    Optional<HeatExchanger> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(heatExchanger -> onPacket(payload, heatExchanger));
                }
            });
        }

        protected static void onPacket(CondenserRenderPacket message, HeatExchanger multiblock) {
            multiblock.onRenderPacket(message);
        }
    }
}