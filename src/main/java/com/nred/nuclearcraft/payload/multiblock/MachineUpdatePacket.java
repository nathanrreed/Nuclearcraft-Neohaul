package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.recipe.RecipeUnitInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.List;

public class MachineUpdatePacket extends MultiblockUpdatePacket {
    public boolean isMachineOn;
    public boolean isProcessing;
    public double time;
    public double baseProcessTime;
    public double baseProcessPower;
    public List<TankInfo> tankInfos;
    public double baseSpeedMultiplier;
    public double basePowerMultiplier;
    public RecipeUnitInfo recipeUnitInfo;

    public MachineUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo) {
        super(pos);
        this.isMachineOn = isMachineOn;
        this.isProcessing = isProcessing;
        this.time = time;
        this.baseProcessTime = baseProcessTime;
        this.baseProcessPower = baseProcessPower;
        tankInfos = TankInfo.getInfoList(tanks);
        this.baseSpeedMultiplier = baseSpeedMultiplier;
        this.basePowerMultiplier = basePowerMultiplier;
        this.recipeUnitInfo = recipeUnitInfo;
    }

    public MachineUpdatePacket(MultiblockUpdatePacket multiblockUpdatePacket, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<TankInfo> tankInfos, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo) {
        super(multiblockUpdatePacket);
        this.isMachineOn = isMachineOn;
        this.isProcessing = isProcessing;
        this.time = time;
        this.baseProcessTime = baseProcessTime;
        this.baseProcessPower = baseProcessPower;
        this.tankInfos = tankInfos;
        this.baseSpeedMultiplier = baseSpeedMultiplier;
        this.basePowerMultiplier = basePowerMultiplier;
        this.recipeUnitInfo = recipeUnitInfo;
    }

    public static MachineUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        MultiblockUpdatePacket multiblockUpdatePacket = MultiblockUpdatePacket.fromBytes(buf);
        boolean isMachineOn = buf.readBoolean();
        boolean isProcessing = buf.readBoolean();
        double time = buf.readDouble();
        double baseProcessTime = buf.readDouble();
        double baseProcessPower = buf.readDouble();
        List<TankInfo> tankInfos = readTankInfos(buf);
        double baseSpeedMultiplier = buf.readDouble();
        double basePowerMultiplier = buf.readDouble();
        RecipeUnitInfo recipeUnitInfo = readRecipeUnitInfo(buf);
        return new MachineUpdatePacket(multiblockUpdatePacket, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tankInfos, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isMachineOn);
        buf.writeBoolean(isProcessing);
        buf.writeDouble(time);
        buf.writeDouble(baseProcessTime);
        buf.writeDouble(baseProcessPower);
        writeTankInfos(buf, tankInfos);
        buf.writeDouble(baseSpeedMultiplier);
        buf.writeDouble(basePowerMultiplier);
        writeRecipeUnitInfo(buf, recipeUnitInfo);
    }
}