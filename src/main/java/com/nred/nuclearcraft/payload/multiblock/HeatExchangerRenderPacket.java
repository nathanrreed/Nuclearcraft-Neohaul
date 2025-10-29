package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.hx.HeatExchangerControllerEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class HeatExchangerRenderPacket extends MultiblockUpdatePacket {
    public static final Type<HeatExchangerRenderPacket> TYPE = new Type<>(ncLoc("heat_exchanger_render_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, HeatExchangerRenderPacket> STREAM_CODEC = StreamCodec.ofMember(
            HeatExchangerRenderPacket::toBytes, HeatExchangerRenderPacket::fromBytes
    );
    public List<Tank.TankInfo> shellTankInfos;

    public HeatExchangerRenderPacket(BlockPos pos, List<Tank> shellTanks) {
        super(pos);
        shellTankInfos = Tank.TankInfo.getInfoList(shellTanks);
    }

    public HeatExchangerRenderPacket(MultiblockUpdatePacket multiblockUpdatePacket, List<Tank.TankInfo> shellTankInfos) {
        super(multiblockUpdatePacket);
        this.shellTankInfos = shellTankInfos;
    }

    public HeatExchangerRenderPacket(List<Tank.TankInfo> shellTankInfos, BlockPos pos) {
        super(pos);
        this.shellTankInfos = shellTankInfos;
    }

    public static HeatExchangerRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        MultiblockUpdatePacket multiblockUpdatePacket = MultiblockUpdatePacket.fromBytes(buf);
        List<Tank.TankInfo> shellTankInfos = readTankInfos(buf);

        return new HeatExchangerRenderPacket(multiblockUpdatePacket, shellTankInfos);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writeTankInfos(buf, shellTankInfos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, HeatExchangerUpdatePacket, HeatExchangerControllerEntity, TileContainerInfo<HeatExchangerControllerEntity>, HeatExchangerRenderPacket> {
        public static void handleOnClient(HeatExchangerRenderPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof HeatExchangerControllerEntity entity) {
                    Optional<HeatExchanger> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(heatExchanger -> onPacket(payload, heatExchanger));
                }
            });
        }

        protected static void onPacket(HeatExchangerRenderPacket message, HeatExchanger multiblock) {
            multiblock.onRenderPacket(message);
        }
    }
}