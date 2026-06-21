package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.machine.DecayPoolControllerEntity;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class DecayPoolRenderPacket extends MachineRenderPacket {
    public static final Type<DecayPoolRenderPacket> TYPE = new Type<>(ncLoc("decay_pool_render_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DecayPoolRenderPacket> STREAM_CODEC = StreamCodec.ofMember(
            DecayPoolRenderPacket::toBytes, DecayPoolRenderPacket::fromBytes
    );

    public boolean isProcessing;
    public List<TankInfo> tankInfos;

    public DecayPoolRenderPacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, List<Tank> tanks) {
        super(pos, isMachineOn);
        this.isProcessing = isProcessing;
        tankInfos = TankInfo.getInfoList(tanks);
    }

    public DecayPoolRenderPacket(MachineRenderPacket machineRenderPacket, boolean isProcessing, List<TankInfo> tankInfos) {
        super(machineRenderPacket);
        this.isProcessing = isProcessing;
        this.tankInfos = tankInfos;
    }

    public static DecayPoolRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        MachineRenderPacket machineRenderPacket = MachineRenderPacket.fromBytes(buf);
        boolean isProcessing = buf.readBoolean();
        List<TankInfo> tankInfos = readTankInfos(buf);
        return new DecayPoolRenderPacket(machineRenderPacket, isProcessing, tankInfos);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isProcessing);
        writeTankInfos(buf, tankInfos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Machine, MachineUpdatePacket, DecayPoolControllerEntity, BlockEntityMenuInfo<DecayPoolControllerEntity>, DecayPoolRenderPacket> {
        public static void handleOnClient(DecayPoolRenderPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof DecayPoolControllerEntity entity) {
                    Optional<Machine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(machine -> onPacket(payload, machine));
                }
            });
        }

        protected static void onPacket(DecayPoolRenderPacket message, Machine multiblock) {
            multiblock.onRenderPacket(message);
        }
    }
}