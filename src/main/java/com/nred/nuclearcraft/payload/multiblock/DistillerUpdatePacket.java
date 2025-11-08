package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.machine.DistillerControllerEntity;
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

public class DistillerUpdatePacket extends MachineUpdatePacket {
    public static final Type<DistillerUpdatePacket> TYPE = new Type<>(ncLoc("distiller_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DistillerUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            DistillerUpdatePacket::toBytes, DistillerUpdatePacket::fromBytes
    );

    public double refluxUnitBonus;
    public double reboilingUnitBonus;
    public double liquidDistributorBonus;

    public DistillerUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, double refluxUnitBonus, double reboilingUnitBonus, double liquidDistributorBonus) {
        super(pos, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo);
        this.refluxUnitBonus = refluxUnitBonus;
        this.reboilingUnitBonus = reboilingUnitBonus;
        this.liquidDistributorBonus = liquidDistributorBonus;
    }

    public DistillerUpdatePacket(MachineUpdatePacket machineUpdatePacket, double refluxUnitBonus, double reboilingUnitBonus, double liquidDistributorBonus) {
        super(machineUpdatePacket);
        this.refluxUnitBonus = refluxUnitBonus;
        this.reboilingUnitBonus = reboilingUnitBonus;
        this.liquidDistributorBonus = liquidDistributorBonus;
    }

    public static DistillerUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        MachineUpdatePacket machineUpdatePacket = MachineUpdatePacket.fromBytes(buf);
        double refluxUnitBonus = buf.readDouble();
        double reboilingUnitBonus = buf.readDouble();
        double liquidDistributorBonus = buf.readDouble();
        return new DistillerUpdatePacket(machineUpdatePacket, refluxUnitBonus, reboilingUnitBonus, liquidDistributorBonus);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(refluxUnitBonus);
        buf.writeDouble(reboilingUnitBonus);
        buf.writeDouble(liquidDistributorBonus);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Machine, MachineUpdatePacket, DistillerControllerEntity, TileContainerInfo<DistillerControllerEntity>, DistillerUpdatePacket> {
        public static void handleOnClient(DistillerUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof DistillerControllerEntity entity) {
                    Optional<Machine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(machine -> onPacket(payload, machine));
                }
            });
        }

        protected static void onPacket(DistillerUpdatePacket message, Machine multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}