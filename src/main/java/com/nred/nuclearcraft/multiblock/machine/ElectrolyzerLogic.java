package com.nred.nuclearcraft.multiblock.machine;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.machine.AbstractMachineEntity;
import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerAnodeTerminalEntity;
import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerCathodeTerminalEntity;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.handler.SoundHandler;
import com.nred.nuclearcraft.payload.multiblock.ElectrolyzerRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.ElectrolyzerUpdatePacket;
import com.nred.nuclearcraft.payload.multiblock.MachineRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.machine.*;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.Vec2i;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.machine_electrolyzer_sound_volume;
import static com.nred.nuclearcraft.registration.SoundRegistration.electrolyzer_run;

public class ElectrolyzerLogic extends MachineLogic {
    public double electrolyteEfficiency = 0D;
    public String electrolyte_group = "";

    public ElectrolyzerLogic(MachineLogic oldLogic) {
        super(oldLogic);
    }

    @Override
    public String getID() {
        return "electrolyzer";
    }

    @Override
    public int reservoirTankCount() {
        return 1;
    }

    @Override
    public List<Set<ResourceLocation>> getReservoirValidFluids() {
        return NCRecipes.multiblock_electrolyzer.validFluids;
    }

    @Override
    public BasicRecipeHandler<?> getRecipeHandler() {
        return NCRecipes.multiblock_electrolyzer;
    }

    @Override
    public double defaultProcessTime() {
        return NCConfig.machine_electrolyzer_time;
    }

    @Override
    public double defaultProcessPower() {
        return NCConfig.machine_electrolyzer_power;
    }

    protected <T extends AbstractMachineEntity> boolean checkElectrodeTerminals(Map<Long, T> terminalMap, String type) {
        int minY = multiblock.getMinY(), maxY = multiblock.getMaxY();
        for (T terminal : terminalMap.values()) {
            BlockPos pos = terminal.getBlockPos();
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            if ((y != minY && y != maxY) || ((y != minY || !terminalMap.containsKey(new BlockPos(x, maxY, z).asLong())) && (y != maxY || !terminalMap.containsKey(new BlockPos(x, minY, z).asLong())))) {
                multiblock.setLastError(pos, MODID + ".multiblock_validation.electrolyzer.invalid_" + type + "_terminal");
                return false;
            }
        }
        return true;
    }

    protected <T extends AbstractMachineEntity> boolean checkElectrodeRecipes(Map<Long, T> terminalMap, ObjectSet<Vec2i> electrodes, String type) {
        BlockPos corner = multiblock.getExtremeInteriorCoord(false, false, false);
        int minY = multiblock.getMinY();
        for (T terminal : terminalMap.values()) {
            BlockPos pos = terminal.getBlockPos();
            if (pos.getY() == minY && !electrodes.contains(new Vec2i(pos.getX() - corner.getX(), pos.getZ() - corner.getZ()))) {
                multiblock.setLastError(pos, MODID + ".multiblock_validation.electrolyzer.invalid_" + type + "_recipe");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onMachineBroken() {
        super.onMachineBroken();

        if (getWorld().isClientSide()) {
            clearSounds();
        }
    }

    public static class ElectrolyzerRegion {
        public final Object2DoubleMap<Vec2i> cathodeMap = new Object2DoubleOpenHashMap<>();
        public final Object2DoubleMap<Vec2i> anodeMap = new Object2DoubleOpenHashMap<>();
        public final Object2DoubleMap<Vec2i> diaphragmMap = new Object2DoubleOpenHashMap<>();

        public double efficiencyMult;
    }

    @Override
    public boolean isMachineWhole() {
        if (!super.isMachineWhole() || !multiblock.hasAxialSymmetry(Axis.Y)) {
            return false;
        }

        multiblock.baseSpeedMultiplier = 0D;
        multiblock.basePowerMultiplier = 0D;

        Map<Long, ElectrolyzerCathodeTerminalEntity> cathodeMap = getPartMap(ElectrolyzerCathodeTerminalEntity.class);
        Map<Long, ElectrolyzerAnodeTerminalEntity> anodeMap = getPartMap(ElectrolyzerAnodeTerminalEntity.class);

        if (!checkElectrodeTerminals(cathodeMap, "cathode") || !checkElectrodeTerminals(anodeMap, "anode")) {
            return false;
        }

        BlockPos corner = multiblock.getExtremeInteriorCoord(false, false, false);
        int interiorX = multiblock.getInteriorLengthX(), interiorZ = multiblock.getInteriorLengthZ();

        BlockState[][] plane = new BlockState[interiorX][];

        for (int i = 0; i < interiorX; ++i) {
            BlockState[] row = new BlockState[interiorZ];
            for (int j = 0; j < interiorZ; ++j) {
                row[j] = getWorld().getBlockState(corner.offset(i, 0, j));
            }
            plane[i] = row;
        }

        boolean[][] visited = new boolean[interiorX][];
        for (int i = 0; i < interiorX; ++i) {
            visited[i] = new boolean[interiorZ];
        }

        ObjectSet<ElectrolyzerRegion> regions = new ObjectOpenHashSet<>();

        ObjectSet<Vec2i> globalCathodes = new ObjectOpenHashSet<>(), globalAnodes = new ObjectOpenHashSet<>();
        Object2DoubleMap<Vec2i> globalDiaphragmMap = new Object2DoubleOpenHashMap<>();

        for (int i = 0; i < interiorX; ++i) {
            for (int j = 0; j < interiorZ; ++j) {
                if (visited[i][j]) {
                    continue;
                }

                Deque<Vec2i> vecs = new ArrayDeque<>();

                Consumer<Vec2i> push = x -> {
                    if (!visited[x.u][x.v]) {
                        vecs.push(x);
                        visited[x.u][x.v] = true;
                    }
                };

                push.accept(new Vec2i(i, j));

                ElectrolyzerRegion region = new ElectrolyzerRegion();
                stackLoop:
                while (!vecs.isEmpty()) {
                    Vec2i next = vecs.pop();

                    for (Vec2i dir : Vec2i.DIRS_WITH_ZERO) {
                        Vec2i vec = next.add(dir);
                        if (vec.u >= 0 && vec.u < interiorX && vec.v >= 0 && vec.v < interiorZ) {
                            BlockState blockState = plane[vec.u][vec.v];
                            if (blockState.isAir()) {
                                push.accept(vec);
                                continue;
                            }

                            long minPosLongXZ = corner.offset(vec.u, -1, vec.v).asLong();
                            BasicRecipe blockRecipe;
                            if (cathodeMap.containsKey(minPosLongXZ)) {
                                if ((blockRecipe = RecipeHelper.blockRecipe(NCRecipes.electrolyzer_cathode, getWorld(), blockState)) != null) {
                                    region.cathodeMap.put(vec, ((ElectrolyzerCathodeRecipe) blockRecipe).getElectrolyzerElectrodeEfficiency());
                                    globalCathodes.add(vec);
                                    push.accept(vec);
                                    continue;
                                }
                            } else if (anodeMap.containsKey(minPosLongXZ)) {
                                if ((blockRecipe = RecipeHelper.blockRecipe(NCRecipes.electrolyzer_anode, getWorld(), blockState)) != null) {
                                    region.anodeMap.put(vec, ((ElectrolyzerAnodeRecipe) blockRecipe).getElectrolyzerElectrodeEfficiency());
                                    globalAnodes.add(vec);
                                    push.accept(vec);
                                    continue;
                                }
                            }

                            if ((blockRecipe = RecipeHelper.blockRecipe(NCRecipes.machine_diaphragm, getWorld(), blockState)) != null) {
                                if (dir.equals(Vec2i.ZERO)) {
                                    continue stackLoop;
                                } else {
                                    region.diaphragmMap.put(vec, ((MachineDiaphragmRecipe) blockRecipe).getMachineDiaphragmEfficiency());
                                    globalDiaphragmMap.put(vec, ((MachineDiaphragmRecipe) blockRecipe).getMachineDiaphragmContactFactor());
                                    continue;
                                }
                            }

                            BlockPos pos = corner.offset(vec.u, 0, vec.v);
                            multiblock.setLastError("zerocore.api.nc.multiblock.validation.invalid_part_for_interior", pos, pos.getX(), pos.getY(), pos.getZ());
                            return false;
                        }
                    }
                }

                if (!region.cathodeMap.isEmpty() || !region.anodeMap.isEmpty()) {
                    regions.add(region);
                }
            }
        }

        if (!checkElectrodeRecipes(cathodeMap, globalCathodes, "cathode") || !checkElectrodeRecipes(anodeMap, globalAnodes, "anode")) {
            return false;
        }

        int interiorY = multiblock.getInteriorLengthY();

        for (ElectrolyzerRegion region : regions) {
            boolean hasCathodes = !region.cathodeMap.isEmpty(), hasAnodes = !region.anodeMap.isEmpty();
            if (hasCathodes && hasAnodes) {
                Vec2i vec = (hasCathodes ? region.cathodeMap : region.anodeMap).keySet().iterator().next();
                BlockPos pos = corner.offset(vec.u, interiorY, vec.v);
                multiblock.setLastError(pos, MODID + ".multiblock_validation.electrolyzer.short_circuit");
                return false;
            }

            double diaphragmEfficiencyMult;
            if (region.diaphragmMap.isEmpty()) {
                diaphragmEfficiencyMult = 1D;
            } else {
                diaphragmEfficiencyMult = 0D;
                for (Object2DoubleMap.Entry<Vec2i> entry : region.diaphragmMap.object2DoubleEntrySet()) {
                    diaphragmEfficiencyMult += entry.getDoubleValue();
                }
                diaphragmEfficiencyMult /= region.diaphragmMap.size();
            }

            region.efficiencyMult = diaphragmEfficiencyMult;
        }

        for (ElectrolyzerRegion region : regions) {
            double cathodeRegionEfficiency = region.efficiencyMult;
            for (Object2DoubleMap.Entry<Vec2i> cathode : region.cathodeMap.object2DoubleEntrySet()) {
                Vec2i cathodeVec = cathode.getKey();
                double cathodeEfficiency = cathodeRegionEfficiency * cathode.getDoubleValue();
                for (ElectrolyzerRegion other : regions) {
                    double anodeRegionEfficiency = other.efficiencyMult;
                    for (Object2DoubleMap.Entry<Vec2i> anode : other.anodeMap.object2DoubleEntrySet()) {
                        Vec2i anodeVec = anode.getKey();
                        double anodeEfficiency = anodeRegionEfficiency * anode.getDoubleValue();
                        multiblock.baseSpeedMultiplier += cathodeEfficiency * anodeEfficiency / anodeVec.subtract(cathodeVec).abs();
                    }
                }
            }
        }

        int cathodeCount = cathodeMap.size(), anodeCount = anodeMap.size();
        multiblock.baseSpeedMultiplier *= (double) interiorY * (double) Math.min(cathodeCount, anodeCount) / (double) Math.max(cathodeCount, anodeCount);

        for (ObjectSet<Vec2i> electrodes : Arrays.asList(globalCathodes, globalAnodes)) {
            for (Vec2i vec : electrodes) {
                multiblock.basePowerMultiplier += 1D;

                for (Vec2i dir : Vec2i.DIRS) {
                    Vec2i offset = vec.add(dir);
                    if (globalDiaphragmMap.containsKey(offset)) {
                        multiblock.basePowerMultiplier += globalDiaphragmMap.getDouble(offset);
                    }
                }
            }
        }

        multiblock.basePowerMultiplier *= interiorY;

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
    protected void setRecipeStats(@Nullable BasicRecipe basicRecipe) {
        super.setRecipeStats(basicRecipe);
        if (basicRecipe instanceof MultiblockElectrolyzerRecipe recipe)
            electrolyte_group = recipe.getElectrolyteGroup();
    }

    protected double getReservoirLevelFraction() {
        return multiblock.reservoirTanks.get(0).getFluidAmountFraction();
    }

    @Override
    protected double getSpeedMultiplier() {
        return multiblock.baseSpeedMultiplier * electrolyteEfficiency * getReservoirLevelFraction();
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

        RecipeInfo<ElectrolyzerElectrolyteRecipe> recipeInfo = NCRecipes.electrolyzer_electrolyte.getRecipeInfoFromInputs(getWorld(), electrolyte_group, multiblock.reservoirTanks.subList(0, 1));
        electrolyteEfficiency = recipeInfo == null ? 0D : recipeInfo.recipe.getElectrolyzerElectrolyteEfficiency();
    }

    // Client

    @Override
    public void onUpdateClient() {
        super.onUpdateClient();

        updateSounds();
        // updateParticles();
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateSounds() {
        if (machine_electrolyzer_sound_volume == 0D) {
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

            Map<Long, ElectrolyzerCathodeTerminalEntity> cathodeMap = getPartMap(ElectrolyzerCathodeTerminalEntity.class);
            Map<Long, ElectrolyzerAnodeTerminalEntity> anodeMap = getPartMap(ElectrolyzerAnodeTerminalEntity.class);
            int electrodeCount = cathodeMap.size() + anodeMap.size();
            if (electrodeCount <= 0) {
                return;
            }

            float volume = (float) (machine_electrolyzer_sound_volume * Math.log1p(Math.sqrt(speedMultiplier) / (4D * Math.sqrt(1D + multiblock.getInteriorLengthY()) * electrodeCount * electrodeCount)));
            Consumer<BlockPos> addSound = x -> getSoundMap().put(x, SoundHandler.startBlockSound(electrolyzer_run.get(), x, volume, 1F));

            for (long posLong : cathodeMap.keySet()) {
                addSound.accept(BlockPos.of(posLong));
            }
            for (long posLong : anodeMap.keySet()) {
                addSound.accept(BlockPos.of(posLong));
            }

            multiblock.prevSpeedMultiplier = speedMultiplier;
        } else {
            multiblock.refreshSounds = true;
            clearSounds();
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateParticles() {
		/*if (isProcessing && multiblock.isAssembled() && !Minecraft.getMinecraft().isGamePaused()) {
			int minY = multiblock.getMinY(), interiorY = multiblock.getInteriorLengthY();
			for (TileElectrolyzerCathodeTerminal cathode : getParts(TileElectrolyzerCathodeTerminal.class)) {
				BlockPos pos = cathode.getPos();
				if (pos.getY() == minY) {
					spawnElectrodeParticles(pos.up(), interiorY);
				}
			}
			for (TileElectrolyzerAnodeTerminal anode : getParts(TileElectrolyzerAnodeTerminal.class)) {
				BlockPos pos = anode.getPos();
				if (pos.getY() == minY) {
					spawnElectrodeParticles(pos.up(), interiorY);
				}
			}
		}*/
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnElectrodeParticles(BlockPos pos, int height) {
		/*double centerX = pos.getX() + 0.5D, minCenterY = pos.getY() + 0.5D, centerZ = pos.getZ() + 0.5D;
		for (int i = 0; i < height; ++i) {
			if (rand.nextDouble() < machine_electrolyzer_particles) {
				double x = centerX + (rand.nextBoolean() ? 1D : -1D) * (0.5D + 0.125 * rand.nextDouble());
				double y = minCenterY + i + 1 - 2 * rand.nextDouble();
				double z = centerZ + (rand.nextBoolean() ? 1D : -1D) * (0.5D + 0.125 * rand.nextDouble());
				getWorld().spawnParticle(EnumParticleTypes.WATER_BUBBLE, false, x, y, z, 0D, 0D, 0D);
			}
		}*/
    }

    // NBT

    @Override
    public void writeToLogicTag(CompoundTag logicTag, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
        super.writeToLogicTag(logicTag, registries, syncReason);
        logicTag.putDouble("electrolyteEfficiency", electrolyteEfficiency);
    }

    @Override
    public void readFromLogicTag(CompoundTag logicTag, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
        super.readFromLogicTag(logicTag, registries, syncReason);
        electrolyteEfficiency = logicTag.getDouble("electrolyteEfficiency");
    }

    // Packets

    @Override
    public MachineUpdatePacket getMultiblockUpdatePacket() {
        return new ElectrolyzerUpdatePacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.processor.time, multiblock.processor.baseProcessTime, multiblock.baseProcessPower, multiblock.tanks, multiblock.baseSpeedMultiplier, multiblock.basePowerMultiplier, multiblock.recipeUnitInfo, electrolyteEfficiency);
    }

    @Override
    public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
        super.onMultiblockUpdatePacket(message);
        if (message instanceof ElectrolyzerUpdatePacket packet) {
            electrolyteEfficiency = packet.electrolyteEfficiency;
        }
    }

    @Override
    public ElectrolyzerRenderPacket getRenderPacket() {
        return new ElectrolyzerRenderPacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.reservoirTanks);
    }

    @Override
    public void onRenderPacket(MachineRenderPacket message) {
        super.onRenderPacket(message);
        if (message instanceof ElectrolyzerRenderPacket packet) {
            boolean wasProcessing = multiblock.processor.isProcessing;
            multiblock.processor.isProcessing = packet.isProcessing;
            if (wasProcessing != multiblock.processor.isProcessing) {
                multiblock.refreshSounds = true;
            }
            TankInfo.readInfoList(packet.reservoirTankInfos, multiblock.reservoirTanks);
        }
    }
}