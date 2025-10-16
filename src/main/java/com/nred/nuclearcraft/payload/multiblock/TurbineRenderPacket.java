package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class TurbineRenderPacket extends MultiblockUpdatePacket {
    public static final Type<TurbineRenderPacket> TYPE = new Type<>(ncLoc("turbine_render_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TurbineRenderPacket> STREAM_CODEC = StreamCodec.ofMember(
            TurbineRenderPacket::toBytes, TurbineRenderPacket::fromBytes
    );

    public List<TankInfo> tankInfos;
    public String particleEffect;
    public double particleSpeedMult, recipeInputRateFP;
    public float angVel;
    public boolean isProcessing;
    public int recipeInputRate;

    public TurbineRenderPacket(BlockPos pos, List<Tank> tanks, String particleEffect, double particleSpeedMult, float angVel, boolean isProcessing, int recipeInputRate, double recipeInputRateFP) {
        super(pos);
        this.tankInfos = TankInfo.getInfoList(tanks);
        this.particleEffect = particleEffect;
        this.particleSpeedMult = particleSpeedMult;
        this.angVel = angVel;
        this.isProcessing = isProcessing;
        this.recipeInputRate = recipeInputRate;
        this.recipeInputRateFP = recipeInputRateFP;
    }

    public TurbineRenderPacket(MultiblockUpdatePacket multiblockUpdatePacket, List<TankInfo> tankInfos, String particleEffect, double particleSpeedMult, float angVel, boolean isProcessing, int recipeInputRate, double recipeInputRateFP) {
        super(multiblockUpdatePacket);
        this.tankInfos = tankInfos;
        this.particleEffect = particleEffect;
        this.particleSpeedMult = particleSpeedMult;
        this.angVel = angVel;
        this.isProcessing = isProcessing;
        this.recipeInputRate = recipeInputRate;
        this.recipeInputRateFP = recipeInputRateFP;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static TurbineRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        MultiblockUpdatePacket multiblockUpdatePacket = MultiblockUpdatePacket.fromBytes(buf);
        List<TankInfo> tankInfos = readTankInfos(buf);
        String particleEffect = readString(buf);
        double particleSpeedMult = buf.readDouble();
        float angVel = buf.readFloat();
        boolean isProcessing = buf.readBoolean();
        int recipeInputRate = buf.readInt();
        double recipeInputRateFP = buf.readDouble();
        return new TurbineRenderPacket(multiblockUpdatePacket, tankInfos, particleEffect, particleSpeedMult, angVel, isProcessing, recipeInputRate, recipeInputRateFP);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        writeTankInfos(buf, tankInfos);
        writeString(buf, particleEffect);
        buf.writeDouble(particleSpeedMult);
        buf.writeFloat(angVel);
        buf.writeBoolean(isProcessing);
        buf.writeInt(recipeInputRate);
        buf.writeDouble(recipeInputRateFP);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Turbine, TurbineUpdatePacket, TurbineControllerEntity, TileContainerInfo<TurbineControllerEntity>, TurbineRenderPacket> {
        public static void handleOnClient(TurbineRenderPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof TurbineControllerEntity entity) {
                    Optional<Turbine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(turbine -> onPacket(payload, turbine));
                }
            });
        }

        protected static void onPacket(TurbineRenderPacket message, Turbine multiblock) {
            multiblock.onRenderPacket(message);
        }
    }
}