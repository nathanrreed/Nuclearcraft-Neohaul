package com.nred.nuclearcraft.multiblock.machine;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorHeatingUnitEntity;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorPressureChamberEntity;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.SoundHandler;
import com.nred.nuclearcraft.payload.multiblock.InfiltratorRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.InfiltratorUpdatePacket;
import com.nred.nuclearcraft.payload.multiblock.MachineRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.machine.InfiltratorPressureFluidRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.nred.nuclearcraft.config.NCConfig.machine_infiltrator_sound_volume;
import static com.nred.nuclearcraft.registration.SoundRegistration.infiltrator_run;

public class InfiltratorLogic extends MachineLogic {
    public double pressureFluidEfficiency = 0D;

    public long heatingCount = 0L;

    public double heatingBonus = 0D;

    public InfiltratorLogic(Machine machine) {
        super(machine);
    }

    public InfiltratorLogic(MachineLogic oldLogic) {
        super(oldLogic);
    }

    @Override
    public String getID() {
        return "infiltrator";
    }

    @Override
    public int reservoirTankCount() {
        return 1;
    }

    @Override
    public List<Set<ResourceLocation>> getReservoirValidFluids() {
        return NCRecipes.infiltrator_pressure_fluid.validFluids;
    }

    @Override
    public BasicRecipeHandler<?> getRecipeHandler() {
        return NCRecipes.multiblock_infiltrator;
    }

    @Override
    public double defaultProcessTime() {
        return NCConfig.machine_infiltrator_time;
    }

    @Override
    public double defaultProcessPower() {
        return NCConfig.machine_infiltrator_power;
    }

    @Override
    public void onMachineBroken() {
        super.onMachineBroken();

        if (getWorld().isClientSide()) {
            clearSounds();
        }
    }

    @Override
    public boolean isMachineWhole() {
        if (!super.isMachineWhole()) {
            return false;
        }

        multiblock.baseSpeedMultiplier = 0D;
        multiblock.basePowerMultiplier = 0D;

        heatingCount = 0L;

        Map<Long, InfiltratorPressureChamberEntity> pressureChamberMap = getPartMap(InfiltratorPressureChamberEntity.class);
        Map<Long, InfiltratorHeatingUnitEntity> heatingUnitMap = getPartMap(InfiltratorHeatingUnitEntity.class);

        for (InfiltratorPressureChamberEntity pressureChamber : pressureChamberMap.values()) {
            BlockPos pos = pressureChamber.getBlockPos();
            for (Direction dir : Direction.values()) {
                if (heatingUnitMap.containsKey(pos.relative(dir).asLong())) {
                    ++heatingCount;
                    break;
                }
            }
        }

        multiblock.baseSpeedMultiplier = pressureChamberMap.size();
        multiblock.basePowerMultiplier = pressureChamberMap.size() + heatingUnitMap.size();

        return true;
    }

    @Override
    public void onAssimilated(IMultiblockController<Machine> assimilator) {
        super.onAssimilated(assimilator);

        if (getWorld().isClientSide()) {
            clearSounds();
        }
    }

    // Server

    @Override
    public void setActivity(boolean isMachineOn) {
        super.setActivity(isMachineOn);
        for (InfiltratorHeatingUnitEntity heatingUnit : getParts(InfiltratorHeatingUnitEntity.class)) {
            heatingUnit.setActivity(isMachineOn);
        }
    }

    @Override
    protected void setRecipeStats(@Nullable BasicRecipe recipe) {
        super.setRecipeStats(recipe);
        heatingBonus = recipe == null ? 0D : heatingCount * ((MultiblockInfiltratorRecipe) recipe).getInfiltratorHeatingFactor() / getPartCount(InfiltratorPressureChamberEntity.class);
    }

    protected double getReservoirLevelFraction() {
        return multiblock.reservoirTanks.get(0).getFluidAmountFraction();
    }

    @Override
    protected double getSpeedMultiplier() {
        return multiblock.baseSpeedMultiplier * pressureFluidEfficiency * (1D + heatingBonus) * getReservoirLevelFraction();
    }

    @Override
    protected double getPowerMultiplier() {
        return multiblock.basePowerMultiplier * getReservoirLevelFraction();
    }

    @Override
    protected boolean readyToProcess() {
        return super.readyToProcess() && getReservoirLevelFraction() > 0D;
    }

    @Override
    public void refreshActivity() {
        super.refreshActivity();

        RecipeInfo<InfiltratorPressureFluidRecipe> recipeInfo = NCRecipes.infiltrator_pressure_fluid.getRecipeInfoFromInputs(getWorld(), Collections.emptyList(), multiblock.reservoirTanks.subList(0, 1));
        pressureFluidEfficiency = recipeInfo == null ? 0D : recipeInfo.recipe.getInfiltratorPressureFluidEfficiency();
    }

    // Client

    @Override
    public void onUpdateClient() {
        super.onUpdateClient();

        updateSounds();
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateSounds() {
        if (machine_infiltrator_sound_volume == 0D) {
            clearSounds();
            return;
        }

        if (multiblock.processor.isProcessing && multiblock.isAssembled()) {
            double speedMultiplier = getSpeedMultiplier();
            double ratio = (NCMath.EPSILON + Math.abs(speedMultiplier)) / (NCMath.EPSILON + Math.abs(multiblock.prevSpeedMultiplier));
            multiblock.refreshSounds |= ratio < 0.8D || ratio > 1.25D || getSoundMap().isEmpty();

            if (!multiblock.refreshSounds) {
                return;
            }
            multiblock.refreshSounds = false;

            clearSounds();

            if (speedMultiplier <= 0D) {
                return;
            }

            float volume = (float) (machine_infiltrator_sound_volume * Math.log1p(Math.cbrt(speedMultiplier)) / 128D);
            Consumer<BlockPos> addSound = x -> getSoundMap().put(x, SoundHandler.startBlockSound(infiltrator_run.get(), x, volume, 1F));

            for (int i = 0; i < 8; ++i) {
                addSound.accept(multiblock.getExtremeInteriorCoord(NCMath.getBit(i, 0) == 1, NCMath.getBit(i, 1) == 1, NCMath.getBit(i, 2) == 1));
            }

            multiblock.prevSpeedMultiplier = speedMultiplier;
        } else {
            multiblock.refreshSounds = true;
            clearSounds();
        }
    }

    // NBT


    @Override
    public void writeToLogicTag(CompoundTag logicTag, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
        super.writeToLogicTag(logicTag, registries, syncReason);
        logicTag.putDouble("pressureFluidEfficiency", pressureFluidEfficiency);
        logicTag.putLong("heatingCount", heatingCount);
        logicTag.putDouble("heatingBonus", heatingBonus);
    }

    @Override
    public void readFromLogicTag(CompoundTag logicTag, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
        super.readFromLogicTag(logicTag, registries, syncReason);
        pressureFluidEfficiency = logicTag.getDouble("pressureFluidEfficiency");
        heatingCount = logicTag.getLong("heatingCount");
        heatingBonus = logicTag.getDouble("heatingBonus");
    }

    // Packets

    @Override
    public MachineUpdatePacket getMultiblockUpdatePacket() {
        return new InfiltratorUpdatePacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.processor.time, multiblock.processor.baseProcessTime, multiblock.baseProcessPower, multiblock.tanks, multiblock.baseSpeedMultiplier, multiblock.basePowerMultiplier, multiblock.recipeUnitInfo, pressureFluidEfficiency, heatingBonus);
    }

    @Override
    public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
        super.onMultiblockUpdatePacket(message);
        if (message instanceof InfiltratorUpdatePacket packet) {
            pressureFluidEfficiency = packet.pressureFluidEfficiency;
            heatingBonus = packet.heatingBonus;
        }
    }

    @Override
    public InfiltratorRenderPacket getRenderPacket() {
        return new InfiltratorRenderPacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.processor.time, multiblock.processor.baseProcessTime, multiblock.tanks, multiblock.reservoirTanks);
    }

    @Override
    public void onRenderPacket(MachineRenderPacket message) {
        super.onRenderPacket(message);
        if (message instanceof InfiltratorRenderPacket packet) {
            boolean wasProcessing = multiblock.processor.isProcessing;
            multiblock.processor.isProcessing = packet.isProcessing;
            if (wasProcessing != multiblock.processor.isProcessing) {
                multiblock.refreshSounds = true;
            }
            multiblock.processor.time = packet.time;
            multiblock.processor.baseProcessTime = packet.baseProcessTime;
            TankInfo.readInfoList(packet.tankInfos, multiblock.tanks);
            TankInfo.readInfoList(packet.reservoirTankInfos, multiblock.reservoirTanks);
        }
    }
}