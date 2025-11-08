package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
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

public class InfiltratorUpdatePacket extends MachineUpdatePacket {
    public static final Type<InfiltratorUpdatePacket> TYPE = new Type<>(ncLoc("infiltrator_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, InfiltratorUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            InfiltratorUpdatePacket::toBytes, InfiltratorUpdatePacket::fromBytes
    );

    public double pressureFluidEfficiency;
    public double heatingBonus;

    public InfiltratorUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, double pressureFluidEfficiency, double heatingBonus) {
        super(pos, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo);
        this.pressureFluidEfficiency = pressureFluidEfficiency;
        this.heatingBonus = heatingBonus;
    }

    public InfiltratorUpdatePacket(MachineUpdatePacket machineUpdatePacket, double pressureFluidEfficiency, double heatingBonus) {
        super(machineUpdatePacket);
        this.pressureFluidEfficiency = pressureFluidEfficiency;
        this.heatingBonus = heatingBonus;
    }

    public static InfiltratorUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        MachineUpdatePacket machineUpdatePacket = MachineUpdatePacket.fromBytes(buf);
        double pressureFluidEfficiency = buf.readDouble();
        double heatingBonus = buf.readDouble();
        return new InfiltratorUpdatePacket(machineUpdatePacket, pressureFluidEfficiency, heatingBonus);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(pressureFluidEfficiency);
        buf.writeDouble(heatingBonus);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Machine, MachineUpdatePacket, InfiltratorControllerEntity, TileContainerInfo<InfiltratorControllerEntity>, InfiltratorUpdatePacket> {
        public static void handleOnClient(InfiltratorUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof InfiltratorControllerEntity entity) {
                    Optional<Machine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(machine -> onPacket(payload, machine));
                }
            });
        }

        protected static void onPacket(InfiltratorUpdatePacket message, Machine multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}