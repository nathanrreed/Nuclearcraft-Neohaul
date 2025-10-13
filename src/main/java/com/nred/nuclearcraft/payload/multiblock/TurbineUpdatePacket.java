package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.block.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class TurbineUpdatePacket extends MultiblockUpdatePacket {
    public static final Type<TurbineUpdatePacket> TYPE = new Type<>(ncLoc("turbine_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TurbineUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            TurbineUpdatePacket::toBytes, TurbineUpdatePacket::fromBytes
    );

    public boolean isTurbineOn;
    public long energy, capacity;
    public double power, rawPower, conductivity, rotorEfficiency, powerBonus, totalExpansionLevel, idealTotalExpansionLevel, bearingTension;
    public int shaftWidth, bladeLength, noBladeSets, dynamoCoilCount, dynamoCoilCountOpposite;

    public TurbineUpdatePacket(BlockPos pos, boolean isTurbineOn, EnergyStorage energyStorage, double power, double rawPower, double conductivity, double rotorEfficiency, double powerBonus, double totalExpansionLevel, double idealTotalExpansionLevel, int shaftWidth, int bladeLength, int noBladeSets, int dynamoCoilCount, int dynamoCoilCountOpposite, double bearingTension) {
        super(pos);
        this.isTurbineOn = isTurbineOn;
        this.energy = energyStorage.getEnergyStoredLong();
        this.capacity = energyStorage.getMaxEnergyStoredLong();
        this.power = power;
        this.rawPower = rawPower;
        this.conductivity = conductivity;
        this.rotorEfficiency = rotorEfficiency;
        this.powerBonus = powerBonus;
        this.totalExpansionLevel = totalExpansionLevel;
        this.idealTotalExpansionLevel = idealTotalExpansionLevel;
        this.shaftWidth = shaftWidth;
        this.bladeLength = bladeLength;
        this.noBladeSets = noBladeSets;
        this.dynamoCoilCount = dynamoCoilCount;
        this.dynamoCoilCountOpposite = dynamoCoilCountOpposite;
        this.bearingTension = bearingTension;
    }

    public TurbineUpdatePacket(MultiblockUpdatePacket multiblockUpdatePacket, boolean isTurbineOn, long energy, long capacity, double power, double rawPower, double conductivity, double rotorEfficiency, double powerBonus, double totalExpansionLevel, double idealTotalExpansionLevel, int shaftWidth, int bladeLength, int noBladeSets, int dynamoCoilCount, int dynamoCoilCountOpposite, double bearingTension) {
        super(multiblockUpdatePacket);
        this.isTurbineOn = isTurbineOn;
        this.energy = energy;
        this.capacity = capacity;
        this.power = power;
        this.rawPower = rawPower;
        this.conductivity = conductivity;
        this.rotorEfficiency = rotorEfficiency;
        this.powerBonus = powerBonus;
        this.totalExpansionLevel = totalExpansionLevel;
        this.idealTotalExpansionLevel = idealTotalExpansionLevel;
        this.shaftWidth = shaftWidth;
        this.bladeLength = bladeLength;
        this.noBladeSets = noBladeSets;
        this.dynamoCoilCount = dynamoCoilCount;
        this.dynamoCoilCountOpposite = dynamoCoilCountOpposite;
        this.bearingTension = bearingTension;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static TurbineUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        MultiblockUpdatePacket multiblockUpdatePacket = MultiblockUpdatePacket.fromBytes(buf);
        boolean isTurbineOn = buf.readBoolean();
        long energy = buf.readLong();
        long capacity = buf.readLong();
        double power = buf.readDouble();
        double rawPower = buf.readDouble();
        double conductivity = buf.readDouble();
        double rotorEfficiency = buf.readDouble();
        double powerBonus = buf.readDouble();
        double totalExpansionLevel = buf.readDouble();
        double idealTotalExpansionLevel = buf.readDouble();
        int shaftWidth = buf.readInt();
        int bladeLength = buf.readInt();
        int noBladeSets = buf.readInt();
        int dynamoCoilCount = buf.readInt();
        int dynamoCoilCountOpposite = buf.readInt();
        double bearingTension = buf.readDouble();
        return new TurbineUpdatePacket(multiblockUpdatePacket, isTurbineOn, energy, capacity, power, rawPower, conductivity, rotorEfficiency, powerBonus, totalExpansionLevel, idealTotalExpansionLevel, shaftWidth, bladeLength, noBladeSets, dynamoCoilCount, dynamoCoilCountOpposite, bearingTension);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isTurbineOn);
        buf.writeLong(energy);
        buf.writeLong(capacity);
        buf.writeDouble(power);
        buf.writeDouble(rawPower);
        buf.writeDouble(conductivity);
        buf.writeDouble(rotorEfficiency);
        buf.writeDouble(powerBonus);
        buf.writeDouble(totalExpansionLevel);
        buf.writeDouble(idealTotalExpansionLevel);
        buf.writeInt(shaftWidth);
        buf.writeInt(bladeLength);
        buf.writeInt(noBladeSets);
        buf.writeInt(dynamoCoilCount);
        buf.writeInt(dynamoCoilCountOpposite);
        buf.writeDouble(bearingTension);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<Turbine, TurbineUpdatePacket, TurbineControllerEntity, TileContainerInfo<TurbineControllerEntity>, TurbineUpdatePacket> {
        public static void handleOnClient(TurbineUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof TurbineControllerEntity entity) {
                    Optional<Turbine> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(turbine -> onPacket(payload, turbine));
                }
            });
        }

        protected static void onPacket(TurbineUpdatePacket message, Turbine multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}