package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.machine.DistillerControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class DistillerRenderPacket extends MachineRenderPacket {
    public static final Type<DistillerRenderPacket> TYPE = new Type<>(ncLoc("distiller_render_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DistillerRenderPacket> STREAM_CODEC = StreamCodec.ofMember(
            DistillerRenderPacket::toBytes, DistillerRenderPacket::fromBytes
    );

    public boolean isProcessing;
    public List<TankInfo> tankInfos;
    public IntList trayLevels;

    public DistillerRenderPacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, List<Tank> tanks, IntList trayLevels) {
        super(pos, isMachineOn);
        this.isProcessing = isProcessing;
        tankInfos = TankInfo.getInfoList(tanks);
        this.trayLevels = trayLevels;
    }

    public DistillerRenderPacket(MachineRenderPacket machineRenderPacket, boolean isProcessing, List<TankInfo> tankInfos, IntList trayLevels) {
        super(machineRenderPacket);
        this.isProcessing = isProcessing;
        this.tankInfos = tankInfos;
        this.trayLevels = trayLevels;
    }

    public static DistillerRenderPacket fromBytes(RegistryFriendlyByteBuf buf) {
        MachineRenderPacket machineRenderPacket = MachineRenderPacket.fromBytes(buf);
        boolean isProcessing = buf.readBoolean();
        List<TankInfo> tankInfos = readTankInfos(buf);
        IntList trayLevels = readInts(buf);
        return new DistillerRenderPacket(machineRenderPacket, isProcessing, tankInfos, trayLevels);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isProcessing);
        writeTankInfos(buf, tankInfos);
        writeInts(buf, trayLevels);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Machine, MachineUpdatePacket, DistillerControllerEntity, TileContainerInfo<DistillerControllerEntity>, DistillerRenderPacket> {
        public static void handleOnClient(DistillerRenderPacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof DistillerControllerEntity entity) {
                    Optional<Machine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(machine -> onPacket(payload, machine));
                }
            });
        }

        protected static void onPacket(DistillerRenderPacket message, Machine multiblock) {
            multiblock.onRenderPacket(message);
        }
    }
}