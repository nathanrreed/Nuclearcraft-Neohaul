package com.nred.nuclearcraft.multiblock.machine;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.machine.TileDistillerLiquidDistributor;
import com.nred.nuclearcraft.block_entity.machine.TileDistillerReboilingUnit;
import com.nred.nuclearcraft.block_entity.machine.TileDistillerRefluxUnit;
import com.nred.nuclearcraft.block_entity.machine.TileDistillerSieveTray;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SoundHandler;
import com.nred.nuclearcraft.payload.multiblock.DistillerRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.DistillerUpdatePacket;
import com.nred.nuclearcraft.payload.multiblock.MachineRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.machine.MachineSieveAssemblyRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.machine_distiller_sound_volume;
import static com.nred.nuclearcraft.registration.SoundRegistration.distiller_run;

public class DistillerLogic extends MachineLogic {
    public final IntList trayLevels = new IntArrayList();

    public double refluxUnitFraction, reboilingUnitFraction, liquidDistributorFraction;
    public double refluxUnitBonus, reboilingUnitBonus, liquidDistributorBonus;

    public DistillerLogic(Machine machine) {
        super(machine);
    }

    public DistillerLogic(MachineLogic oldLogic) {
        super(oldLogic);
    }

    @Override
    public String getID() {
        return "distiller";
    }

    @Override
    public BasicRecipeHandler<?> getRecipeHandler() {
        return NCRecipes.multiblock_distiller;
    }

    @Override
    public double defaultProcessTime() {
        return NCConfig.machine_distiller_time;
    }

    @Override
    public double defaultProcessPower() {
        return NCConfig.machine_distiller_power;
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
        if (!super.isMachineWhole() || !multiblock.hasPlanarSymmetry(Direction.Axis.Y)) {
            return false;
        }

        multiblock.baseSpeedMultiplier = 0D;
        multiblock.basePowerMultiplier = 0D;

        trayLevels.clear();

        refluxUnitFraction = reboilingUnitFraction = liquidDistributorFraction = 0D;

        int area = multiblock.getInteriorLengthX() * multiblock.getInteriorLengthZ();
        if (area <= 0) {
            return true;
        }

        int minY = multiblock.getMinY(), maxY = multiblock.getMaxY();

        Collection<TileDistillerRefluxUnit> refluxUnits = getParts(TileDistillerRefluxUnit.class);
        for (TileDistillerRefluxUnit refluxUnit : refluxUnits) {
            BlockPos pos = refluxUnit.getBlockPos();
            if (pos.getY() != maxY) {
                multiblock.setLastError(pos, MODID + ".multiblock_validation.distiller.invalid_reflux_unit");
                return false;
            }
        }

        Collection<TileDistillerReboilingUnit> reboilingUnits = getParts(TileDistillerReboilingUnit.class);
        for (TileDistillerReboilingUnit reboilingUnit : reboilingUnits) {
            BlockPos pos = reboilingUnit.getBlockPos();
            if (pos.getY() != minY) {
                multiblock.setLastError(pos, MODID + ".multiblock_validation.distiller.invalid_reboiling_unit");
                return false;
            }
        }

        Collection<TileDistillerLiquidDistributor> liquidDistributors = getParts(TileDistillerLiquidDistributor.class);
        for (TileDistillerLiquidDistributor liquidDistributor : liquidDistributors) {
            BlockPos pos = liquidDistributor.getBlockPos();
            if (pos.getY() != maxY) {
                multiblock.setLastError(pos, MODID + ".multiblock_validation.distiller.invalid_liquid_distributor");
                return false;
            }
        }

        BlockPos corner = multiblock.getExtremeInteriorCoord(false, false, false);
        IntSet traySieveCache = new IntOpenHashSet();

        double traySieveEfficiency = 0D, packedSieveEfficiency = 0D;
        int traySieveCount = 0, packedSieveCount = 0;

        for (int i = 0, len = multiblock.getInteriorLengthY(); i < len; ++i) {
            BlockPos pos = corner.above(i);
            BasicRecipe blockRecipe;
            if (getWorld().getBlockEntity(pos) instanceof TileDistillerSieveTray) {
                blockRecipe = RecipeHelper.blockRecipe(NCRecipes.machine_sieve_assembly, getWorld(), getWorld().getBlockState(pos.above()));
                if (blockRecipe == null) {
                    multiblock.setLastError(pos, MODID + ".multiblock_validation.distiller.invalid_sieve_recipe");
                    return false;
                } else {
                    trayLevels.add(i);
                    traySieveCache.add(i + 1);
                    traySieveEfficiency += ((MachineSieveAssemblyRecipe) blockRecipe).getMachineSieveAssemblyEfficiency();
                    ++traySieveCount;
                }
            } else if (!traySieveCache.contains(i) && (blockRecipe = RecipeHelper.blockRecipe(NCRecipes.machine_sieve_assembly, getWorld(), getWorld().getBlockState(pos))) != null) {
                packedSieveEfficiency += ((MachineSieveAssemblyRecipe) blockRecipe).getMachineSieveAssemblyEfficiency();
                ++packedSieveCount;
            }
        }

        multiblock.baseSpeedMultiplier = (double) area * (traySieveCount == 0 ? packedSieveEfficiency : (packedSieveCount == 0 ? traySieveEfficiency : (packedSieveCount * traySieveEfficiency + packedSieveEfficiency) / (1D + packedSieveCount)));
        multiblock.basePowerMultiplier = reboilingUnits.size() + (double) area * (double) (traySieveCount + packedSieveCount);

        refluxUnitFraction = (double) refluxUnits.size() / (double) area;
        reboilingUnitFraction = (double) reboilingUnits.size() / (double) area;
        liquidDistributorFraction = (double) liquidDistributors.size() / (double) area;

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
        for (TileDistillerReboilingUnit reboilingUnit : getParts(TileDistillerReboilingUnit.class)) {
            reboilingUnit.setActivity(isMachineOn);
        }
    }

    @Override
    protected void setRecipeStats(@Nullable BasicRecipe recipe) {
        super.setRecipeStats(recipe);

        if (recipe == null) {
            refluxUnitBonus = reboilingUnitBonus = liquidDistributorBonus = 0D;
        } else {
            int liquidCount = 0, gasCount = 0;

            for (List<SizedChanceFluidIngredient> fluidIngredientList : Arrays.asList(recipe.getFluidIngredients(), recipe.getFluidProducts())) {
                for (SizedChanceFluidIngredient fluidIngredient : fluidIngredientList) {
                    if (!fluidIngredient.ingredient().hasNoFluids()) {
                        FluidStack stack = fluidIngredient.getStack();
                        Fluid fluid = stack.getFluid();
                        if (!stack.isEmpty()) {
                            if (fluid.getFluidType().isLighterThanAir()) {
                                ++gasCount;
                            } else {
                                ++liquidCount;
                            }
                        }
                    }
                }
            }

            int totalCount = liquidCount + gasCount;
            refluxUnitBonus = totalCount == 0 ? 0D : refluxUnitFraction * (double) gasCount / (double) totalCount;
            reboilingUnitBonus = totalCount == 0 ? 0D : reboilingUnitFraction * (double) liquidCount / (double) totalCount;
            liquidDistributorBonus = liquidDistributorFraction / (1D + trayLevels.size());
        }
    }

    @Override
    protected double getSpeedMultiplier() {
        return multiblock.baseSpeedMultiplier * (1D + refluxUnitBonus) * (1D + reboilingUnitBonus) * (1D + liquidDistributorBonus);
    }

    @Override
    protected boolean readyToProcess() {
        if (!super.readyToProcess()) {
            return false;
        }

        long sieveTrayCount = multiblock.processor.recipeInfo == null ? 0L : ((MultiblockDistillerRecipe) multiblock.processor.recipeInfo.recipe).getDistillerSieveTrayCount();
        return sieveTrayCount <= trayLevels.size();
    }

    // Client

    @Override
    public void onUpdateClient() {
        super.onUpdateClient();

        updateSounds();
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateSounds() {
        if (machine_distiller_sound_volume == 0D) {
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

            float volume = (float) (machine_distiller_sound_volume * Math.log1p(Math.cbrt(speedMultiplier)) / 128D);
            Consumer<BlockPos> addSound = x -> getSoundMap().put(x, SoundHandler.startBlockSound(distiller_run.get(), x, volume, 1F));

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
        int trayLevelCount = trayLevels.size();
        logicTag.putInt("trayLevelCount", trayLevelCount);
        for (int i = 0; i < trayLevelCount; ++i) {
            logicTag.putInt("trayLevel" + i, trayLevels.getInt(i));
        }
        logicTag.putDouble("refluxUnitFraction", refluxUnitFraction);
        logicTag.putDouble("reboilingUnitFraction", reboilingUnitFraction);
        logicTag.putDouble("liquidDistributorFraction", liquidDistributorFraction);
        logicTag.putDouble("refluxUnitBonus", refluxUnitBonus);
        logicTag.putDouble("reboilingUnitBonus", reboilingUnitBonus);
        logicTag.putDouble("liquidDistributorBonus", liquidDistributorBonus);
    }

    @Override
    public void readFromLogicTag(CompoundTag logicTag, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
        super.readFromLogicTag(logicTag, registries, syncReason);
        if (logicTag.contains("trayLevelCount")) {
            int trayLevelCount = logicTag.getInt("trayLevelCount");
            trayLevels.clear();
            for (int i = 0; i < trayLevelCount; ++i) {
                trayLevels.add(logicTag.getInt("trayLevel" + i));
            }
        }
        refluxUnitFraction = logicTag.getDouble("refluxUnitFraction");
        reboilingUnitFraction = logicTag.getDouble("reboilingUnitFraction");
        liquidDistributorFraction = logicTag.getDouble("liquidDistributorFraction");
        refluxUnitBonus = logicTag.getDouble("refluxUnitBonus");
        reboilingUnitBonus = logicTag.getDouble("reboilingUnitBonus");
        liquidDistributorBonus = logicTag.getDouble("liquidDistributorBonus");
    }

    // Packets

    @Override
    public MachineUpdatePacket getMultiblockUpdatePacket() {
        return new DistillerUpdatePacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.processor.time, multiblock.processor.baseProcessTime, multiblock.baseProcessPower, multiblock.tanks, multiblock.baseSpeedMultiplier, multiblock.basePowerMultiplier, multiblock.recipeUnitInfo, refluxUnitBonus, reboilingUnitBonus, liquidDistributorBonus);
    }

    @Override
    public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
        super.onMultiblockUpdatePacket(message);
        if (message instanceof DistillerUpdatePacket packet) {
            refluxUnitBonus = packet.refluxUnitBonus;
            reboilingUnitBonus = packet.reboilingUnitBonus;
            liquidDistributorBonus = packet.liquidDistributorBonus;
        }
    }

    @Override
    public DistillerRenderPacket getRenderPacket() {
        return new DistillerRenderPacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.processor.getFluidInputs(false), trayLevels);
    }

    @Override
    public void onRenderPacket(MachineRenderPacket message) {
        super.onRenderPacket(message);
        if (message instanceof DistillerRenderPacket packet) {
            boolean wasProcessing = multiblock.processor.isProcessing;
            multiblock.processor.isProcessing = packet.isProcessing;
            if (wasProcessing != multiblock.processor.isProcessing) {
                multiblock.refreshSounds = true;
            }
            TankInfo.readInfoList(packet.tankInfos, multiblock.processor.getFluidInputs(false));
            trayLevels.clear();
            trayLevels.addAll(packet.trayLevels);
        }
    }
}