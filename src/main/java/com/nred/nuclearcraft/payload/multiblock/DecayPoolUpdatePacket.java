package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.machine.DecayPoolControllerEntity;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.recipe.RecipeUnitInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class DecayPoolUpdatePacket extends MachineUpdatePacket {
    public static final Type<DecayPoolUpdatePacket> TYPE = new Type<>(ncLoc("decay_pool_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DecayPoolUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            DecayPoolUpdatePacket::toBytes, DecayPoolUpdatePacket::fromBytes
    );

    public double totalDecayRate;

    public DecayPoolUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, boolean readyToProcess, double totalDecayRate) {
        super(pos, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo, readyToProcess);
        this.totalDecayRate = totalDecayRate;
    }

    public DecayPoolUpdatePacket(MachineUpdatePacket machineUpdatePacket, double totalDecayRate) {
        super(machineUpdatePacket);
        this.totalDecayRate = totalDecayRate;
    }

    public static DecayPoolUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        MachineUpdatePacket machineUpdatePacket = MachineUpdatePacket.fromBytes(buf);
        double totalDecayRate = buf.readDouble();
        return new DecayPoolUpdatePacket(machineUpdatePacket, totalDecayRate);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(totalDecayRate);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Machine, MachineUpdatePacket, DecayPoolControllerEntity, BlockEntityMenuInfo<DecayPoolControllerEntity>, DecayPoolUpdatePacket> {
        public static void handleOnClient(DecayPoolUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof DecayPoolControllerEntity entity) {
                    Optional<Machine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(machine -> onPacket(payload, machine));
                }
            });
        }

        protected static void onPacket(DecayPoolUpdatePacket message, Machine multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}