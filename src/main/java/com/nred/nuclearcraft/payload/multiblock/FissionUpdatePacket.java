package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.internal.heat.HeatBuffer;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class FissionUpdatePacket extends MultiblockUpdatePacket {
    public static final Type<EnergyProcessorUpdatePacket> TYPE = new Type<>(ncLoc("fission_update_packet"));
    public boolean isReactorOn;
    public long cooling, rawHeating, totalHeatMult, usefulPartCount, heatStored, heatCapacity;
    public int clusterCount, fuelComponentCount;
    public double meanHeatMult, totalEfficiency, meanEfficiency, sparsityEfficiencyMult;

    public FissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult) {
        super(pos);
        this.isReactorOn = isReactorOn;
        this.heatStored = heatBuffer.getHeatStored();
        this.heatCapacity = heatBuffer.getHeatCapacity();
        this.clusterCount = clusterCount;
        this.cooling = cooling;
        this.rawHeating = rawHeating;
        this.totalHeatMult = totalHeatMult;
        this.meanHeatMult = meanHeatMult;
        this.fuelComponentCount = fuelComponentCount;
        this.usefulPartCount = usefulPartCount;
        this.totalEfficiency = totalEfficiency;
        this.meanEfficiency = meanEfficiency;
        this.sparsityEfficiencyMult = sparsityEfficiencyMult;
    }

    public FissionUpdatePacket(MultiblockUpdatePacket multiblockUpdatePacket, boolean isReactorOn, long heatStored, long heatCapacity, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult) {
        super(multiblockUpdatePacket);
        this.isReactorOn = isReactorOn;
        this.heatStored = heatStored;
        this.heatCapacity = heatCapacity;
        this.clusterCount = clusterCount;
        this.cooling = cooling;
        this.rawHeating = rawHeating;
        this.totalHeatMult = totalHeatMult;
        this.meanHeatMult = meanHeatMult;
        this.fuelComponentCount = fuelComponentCount;
        this.usefulPartCount = usefulPartCount;
        this.totalEfficiency = totalEfficiency;
        this.meanEfficiency = meanEfficiency;
        this.sparsityEfficiencyMult = sparsityEfficiencyMult;
    }

    public FissionUpdatePacket(FissionUpdatePacket fissionUpdatePacket) {
        super(fissionUpdatePacket.pos);
        this.isReactorOn = fissionUpdatePacket.isReactorOn;
        this.heatStored = fissionUpdatePacket.heatStored;
        this.heatCapacity = fissionUpdatePacket.heatCapacity;
        this.clusterCount = fissionUpdatePacket.clusterCount;
        this.cooling = fissionUpdatePacket.cooling;
        this.rawHeating = fissionUpdatePacket.rawHeating;
        this.totalHeatMult = fissionUpdatePacket.totalHeatMult;
        this.meanHeatMult = fissionUpdatePacket.meanHeatMult;
        this.fuelComponentCount = fissionUpdatePacket.fuelComponentCount;
        this.usefulPartCount = fissionUpdatePacket.usefulPartCount;
        this.totalEfficiency = fissionUpdatePacket.totalEfficiency;
        this.meanEfficiency = fissionUpdatePacket.meanEfficiency;
        this.sparsityEfficiencyMult = fissionUpdatePacket.sparsityEfficiencyMult;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static FissionUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        MultiblockUpdatePacket multiblockUpdatePacket = MultiblockUpdatePacket.fromBytes(buf);
        boolean isReactorOn = buf.readBoolean();
        long heatStored = buf.readLong();
        long heatCapacity = buf.readLong();
        int clusterCount = buf.readInt();
        long cooling = buf.readLong();
        long rawHeating = buf.readLong();
        long totalHeatMult = buf.readLong();
        double meanHeatMult = buf.readDouble();
        int fuelComponentCount = buf.readInt();
        long usefulPartCount = buf.readLong();
        double totalEfficiency = buf.readDouble();
        double meanEfficiency = buf.readDouble();
        double sparsityEfficiencyMult = buf.readDouble();
        return new FissionUpdatePacket(multiblockUpdatePacket, isReactorOn, heatStored, heatCapacity, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isReactorOn);
        buf.writeLong(heatStored);
        buf.writeLong(heatCapacity);
        buf.writeInt(clusterCount);
        buf.writeLong(cooling);
        buf.writeLong(rawHeating);
        buf.writeLong(totalHeatMult);
        buf.writeDouble(meanHeatMult);
        buf.writeInt(fuelComponentCount);
        buf.writeLong(usefulPartCount);
        buf.writeDouble(totalEfficiency);
        buf.writeDouble(meanEfficiency);
        buf.writeDouble(sparsityEfficiencyMult);
    }
}