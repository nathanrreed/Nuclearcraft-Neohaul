package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerControllerEntity;
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

public class ElectrolyzerRenderPacket extends MachineRenderPacket {
    public static final Type<ElectrolyzerRenderPacket> TYPE = new Type<>(ncLoc("electrolyzer_render_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ElectrolyzerRenderPacket> STREAM_CODEC = StreamCodec.ofMember(
            ElectrolyzerRenderPacket::toBytes, ElectrolyzerRenderPacket::fromBytes
    );

    public boolean isProcessing;
    public List<TankInfo> reservoirTankInfos;

    public ElectrolyzerRenderPacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, List<Tank> reservoirTanks) {
        super(pos, isMachineOn);
        this.isProcessing = isProcessing;
        reservoirTankInfos = TankInfo.getInfoList(reservoirTanks);
    }

    public ElectrolyzerRenderPacket(MachineRenderPacket machineRenderPacket, boolean isProcessing, List<TankInfo> reservoirTankInfos) {
        super(machineRenderPacket);
        this.isProcessing = isProcessing;
        this.reservoirTankInfos = reservoirTankInfos;
    }

    public static ElectrolyzerRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        MachineRenderPacket machineRenderPacket = MachineRenderPacket.fromBytes(buf);
        boolean isProcessing = buf.readBoolean();
        List<TankInfo> reservoirTankInfos = readTankInfos(buf);
        return new ElectrolyzerRenderPacket(machineRenderPacket, isProcessing, reservoirTankInfos);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isProcessing);
        writeTankInfos(buf, reservoirTankInfos);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Machine, MachineUpdatePacket, ElectrolyzerControllerEntity, TileContainerInfo<ElectrolyzerControllerEntity>, ElectrolyzerRenderPacket> {
        public static void handleOnClient(ElectrolyzerRenderPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof ElectrolyzerControllerEntity entity) {
                    Optional<Machine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(machine -> onPacket(payload, machine));
                }
            });
        }

        protected static void onPacket(ElectrolyzerRenderPacket message, Machine multiblock) {
            multiblock.onRenderPacket(message);
        }
    }
}