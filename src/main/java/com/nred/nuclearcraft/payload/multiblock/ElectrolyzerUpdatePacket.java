package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.recipe.RecipeUnitInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ElectrolyzerUpdatePacket extends MachineUpdatePacket {
    public static final Type<ElectrolyzerUpdatePacket> TYPE = new Type<>(ncLoc("electrolyzer_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ElectrolyzerUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            ElectrolyzerUpdatePacket::toBytes, ElectrolyzerUpdatePacket::fromBytes
    );

    public double electrolyteEfficiency;

    public ElectrolyzerUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, double electrolyteEfficiency) {
        super(pos, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo);
        this.electrolyteEfficiency = electrolyteEfficiency;
    }

    public ElectrolyzerUpdatePacket(MachineUpdatePacket machineUpdatePacket, double electrolyteEfficiency) {
        super(machineUpdatePacket);
        this.electrolyteEfficiency = electrolyteEfficiency;
    }

    public static ElectrolyzerUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        MachineUpdatePacket machineUpdatePacket = MachineUpdatePacket.fromBytes(buf);
        double electrolyteEfficiency = buf.readDouble();
        return new ElectrolyzerUpdatePacket(machineUpdatePacket, electrolyteEfficiency);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(electrolyteEfficiency);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Machine, MachineUpdatePacket, ElectrolyzerControllerEntity, TileContainerInfo<ElectrolyzerControllerEntity>, ElectrolyzerUpdatePacket> {
        public static void handleOnClient(ElectrolyzerUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof ElectrolyzerControllerEntity entity) {
                    Optional<Machine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(machine -> onPacket(payload, machine));
                }
            });
        }

        protected static void onPacket(ElectrolyzerUpdatePacket message, Machine multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}