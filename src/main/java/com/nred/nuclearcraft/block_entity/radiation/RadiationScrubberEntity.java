package com.nred.nuclearcraft.block_entity.radiation;

import com.nred.nuclearcraft.block_entity.processor.TileProcessorImpl.BasicEnergyProcessorEntity;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.radiation.RadiationHandler;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import com.nred.nuclearcraft.radiation.environment.RadiationEnvironmentHandler;
import com.nred.nuclearcraft.radiation.environment.RadiationEnvironmentInfo;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.util.FourPos;
import com.nred.nuclearcraft.util.NBTHelper;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.RADIATION_SCRUBBER_ENTITY_TYPE;

public class RadiationScrubberEntity extends BasicEnergyProcessorEntity<RadiationScrubberEntity> implements ITileRadiationEnvironment {
    private double efficiency = 0D, scrubberFraction = 0D, currentChunkLevel = 0D, currentChunkBuffer = 0D;

    public final ConcurrentMap<BlockPos, Integer> occlusionMap = new ConcurrentHashMap<>();

    private int radCheckCount = RadiationHandler.RAND.nextInt(machine_update_rate * 20);

    public RadiationScrubberEntity(BlockPos pos, BlockState blockState) {
        super(RADIATION_SCRUBBER_ENTITY_TYPE.get(), pos, blockState, "radiation_scrubber");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide()) {
            for (int x = -radiation_scrubber_radius; x <= radiation_scrubber_radius; ++x) {
                for (int y = -radiation_scrubber_radius; y <= radiation_scrubber_radius; ++y) {
                    for (int z = -radiation_scrubber_radius; z <= radiation_scrubber_radius; ++z) {
                        RadiationEnvironmentHandler.addTile(getFourPos().add(x, y, z), this);
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            boolean shouldUpdate = onTick();

            tickRadCount();
            if (shouldUpdate || shouldRadCheck()) {
                checkRadiationEnvironmentInfo();
            }

            if (shouldUpdate) {
                setChanged();
            }
        }
    }

    @Override
    public void setRecipeStats(@Nullable BasicRecipe basicRecipe) {
        if (basicRecipe instanceof RadiationScrubberRecipe recipe) {
            baseProcessTime = recipe.getScrubberProcessTime();
            baseProcessPower = recipe.getScrubberProcessPower();
            efficiency = recipe.getScrubberProcessEfficiency();
        } else {
            baseProcessTime = 1D;
            baseProcessPower = 0D;
            efficiency = 0D;
        }
    }

    public double getRawScrubberRate() {
        if (!isProcessing) {
            return 0D;
        }
        double rateMult = currentChunkBuffer + radiation_spread_rate * Math.max(0D, currentChunkLevel - currentChunkBuffer);
        if (radiation_scrubber_non_linear) {
            IRadiationSource chunkSource = RadiationHelper.getRadiationSource(level.getChunk(worldPosition));
            if (chunkSource == null || chunkSource.getEffectiveScrubberCount() == 0D) {
                return 0D;
            }
            return -rateMult * scrubberFraction * chunkSource.getScrubbingFraction() / chunkSource.getEffectiveScrubberCount();
        }
        return -rateMult * scrubberFraction;
    }

    public void tickRadCount() {
        ++radCheckCount;
        radCheckCount %= machine_update_rate * 20;
    }

    public boolean shouldRadCheck() {
        return radCheckCount == 0;
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        RadiationEnvironmentHandler.removeTile(this);
    }

    // Processing

    @Override
    public boolean readyToProcess() {
        return canProcessInputs && hasConsumed && hasSufficientEnergy();
    }

    @Override
    public boolean hasSufficientEnergy() {
        return getEnergyStoredLong() >= (long) baseProcessPower;
    }

    @Override
    public void process() {
        ++time;
        getEnergyStorage().changeEnergyStored((long) -baseProcessPower);
        if (time >= baseProcessTime) {
            finishProcess();
        }
    }

    // IRadiationEnvironmentHandler

    @Override
    public void checkRadiationEnvironmentInfo() {
        double newScrubberFraction = getMaxScrubberFraction();

        Iterator<Entry<BlockPos, Integer>> occlusionIterator = occlusionMap.entrySet().iterator();

        int occlusionCount = 0;
        double tileCount = 0D;
        while (occlusionIterator.hasNext()) {
            Entry<BlockPos, Integer> occlusion = occlusionIterator.next();

            if (isOcclusive(worldPosition, level, occlusion.getKey())) {
                newScrubberFraction -= getOcclusionPenalty() / worldPosition.distSqr(occlusion.getKey());
                ++occlusionCount;
                tileCount += Math.max(1D, Math.sqrt(occlusion.getValue()));
            } else {
                occlusionIterator.remove();
            }
        }

        scrubberFraction = efficiency * (occlusionCount == 0 ? getMaxScrubberFraction() : Math.max(0D, newScrubberFraction * occlusionCount / tileCount));
    }

    @Override
    public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info) {
        FourPos fourPos = getFourPos(), infoPos = info.pos;
        if (fourPos.getDimension() == infoPos.getDimension() && !fourPos.equals(infoPos) && !info.tileMap.isEmpty() /* && isOcclusive(fourPos.getBlockPos(), world, infoPos.getBlockPos()) */) {
            occlusionMap.put(infoPos.getBlockPos(), Math.max(1, info.tileMap.size()));
        }
    }

    @Override
    public double getRadiationContributionFraction() {
        return isProcessing ? -scrubberFraction : 0D;
    }

    @Override
    public double getCurrentChunkRadiationLevel() {
        return currentChunkLevel;
    }

    @Override
    public void setCurrentChunkRadiationLevel(double level) {
        currentChunkLevel = level;
    }

    @Override
    public double getCurrentChunkRadiationBuffer() {
        return currentChunkBuffer;
    }

    @Override
    public void setCurrentChunkRadiationBuffer(double buffer) {
        currentChunkBuffer = buffer;
    }

    public static double getMaxScrubberFraction() {
        return radiation_scrubber_non_linear ? 1D : radiation_scrubber_fraction;
    }

    private static double getOcclusionPenalty() {
        return getMaxScrubberFraction() / 52D;
    }

    // Helper

    // All opaque blocks plus translucent full blocks are occlusive
    private static boolean isOcclusive(BlockPos pos, Level level, BlockPos otherPos) {
        BlockState state = level.getBlockState(otherPos);
        return pos.distSqr(otherPos) < NCMath.sq(radiation_scrubber_radius) && !state.isAir() && !state.canOcclude();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RadiationScrubberEntity other)) {
            return false;
        }
        return getFourPos().equals(other.getFourPos());
    }

    // NBT

    @Override
    public boolean shouldSaveRadiation() {
        return false;
    }

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putDouble("baseProcessTime", baseProcessTime);
        nbt.putDouble("baseProcessPower", baseProcessPower);
        nbt.putDouble("efficiency", efficiency);

        nbt.putDouble("scrubberFraction", scrubberFraction);
        nbt.putDouble("currentChunkLevel", currentChunkLevel);
        nbt.putDouble("currentChunkBuffer", currentChunkBuffer);

        NBTHelper.writeBlockPosToIntegerMap(nbt, occlusionMap, "occlusionMap");
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        baseProcessTime = nbt.getDouble("baseProcessTime");
        baseProcessPower = nbt.getDouble("baseProcessPower");
        efficiency = nbt.getDouble("efficiency");

        scrubberFraction = nbt.getDouble("scrubberFraction");
        currentChunkLevel = nbt.getDouble("currentChunkLevel");
        currentChunkBuffer = nbt.getDouble("currentChunkBuffer");

        NBTHelper.readBlockPosToIntegerMap(nbt, occlusionMap, "occlusionMap");
    }
}