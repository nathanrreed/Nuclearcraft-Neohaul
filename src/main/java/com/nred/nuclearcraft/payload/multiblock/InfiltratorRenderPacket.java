package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class InfiltratorRenderPacket extends MachineRenderPacket {
    public static final Type<InfiltratorRenderPacket> TYPE = new Type<>(ncLoc("infiltrator_render_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, InfiltratorRenderPacket> STREAM_CODEC = StreamCodec.ofMember(
            InfiltratorRenderPacket::toBytes, InfiltratorRenderPacket::fromBytes
    );

    public boolean isProcessing;
    public double time;
    public double baseProcessTime;
    public List<TankInfo> tankInfos;
    public List<TankInfo> reservoirTankInfos;

    public InfiltratorRenderPacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks, List<Tank> reservoirTanks) {
        super(pos, isMachineOn);
        this.isProcessing = isProcessing;
        this.time = time;
        this.baseProcessTime = baseProcessTime;
        tankInfos = TankInfo.getInfoList(tanks);
        reservoirTankInfos = TankInfo.getInfoList(reservoirTanks);
    }

    public InfiltratorRenderPacket(MachineRenderPacket machineRenderPacket, boolean isProcessing, double time, double baseProcessTime, List<TankInfo> tankInfos, List<TankInfo> reservoirTankInfos) {
        super(machineRenderPacket);
        this.isProcessing = isProcessing;
        this.time = time;
        this.baseProcessTime = baseProcessTime;
        this.tankInfos = tankInfos;
        this.reservoirTankInfos = reservoirTankInfos;
    }

    public static InfiltratorRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        MachineRenderPacket machineRenderPacket = MachineRenderPacket.fromBytes(buf);
        boolean isProcessing = buf.readBoolean();
        double time = buf.readDouble();
        double baseProcessTime = buf.readDouble();
        List<TankInfo> tankInfos = readTankInfos(buf);
        List<TankInfo> reservoirTankInfos = readTankInfos(buf);
        return new InfiltratorRenderPacket(machineRenderPacket, isProcessing, time, baseProcessTime, tankInfos, reservoirTankInfos);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isProcessing);
        buf.writeDouble(time);
        buf.writeDouble(baseProcessTime);
        writeTankInfos(buf, tankInfos);
        writeTankInfos(buf, reservoirTankInfos);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Machine, MachineUpdatePacket, InfiltratorControllerEntity, TileContainerInfo<InfiltratorControllerEntity>, InfiltratorRenderPacket> {
        public static void handleOnClient(InfiltratorRenderPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof InfiltratorControllerEntity entity) {
                    Optional<Machine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(machine -> onPacket(payload, machine));
                }
            });
        }

        protected static void onPacket(InfiltratorRenderPacket message, Machine multiblock) {
            multiblock.onRenderPacket(message);
        }
    }
}