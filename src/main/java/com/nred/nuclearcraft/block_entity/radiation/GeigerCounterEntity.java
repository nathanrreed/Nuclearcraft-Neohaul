package com.nred.nuclearcraft.block_entity.radiation;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.NCTile;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.config.NCConfig.radiation_geiger_block_redstone;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.GEIGER_COUNTER_ENTITY_TYPE;

public class GeigerCounterEntity extends NCTile implements ITickable {
    public int comparatorStrength = 0;

    public GeigerCounterEntity(BlockPos pos, BlockState blockState) {
        super(GEIGER_COUNTER_ENTITY_TYPE.get(), pos, blockState);
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            boolean shouldUpdate = false;
            int compStrength = getComparatorStrength();
            if (comparatorStrength != compStrength) {
                shouldUpdate = true;
            }
            comparatorStrength = compStrength;
            if (shouldUpdate) {
                setChanged();
                updateComparatorOutputLevel();
            }
        }
    }

    public double getChunkRadiationLevel() {
        IRadiationSource chunkRadiation = RadiationHelper.getRadiationSource(level.getChunk(worldPosition));
        return chunkRadiation.getRadiationLevel();
    }

    public int getComparatorStrength() {
        double radiation = getChunkRadiationLevel();
        return radiation <= 0D ? 0 : Math.max(0, NCMath.toInt(15D + Math.log10(radiation) - radiation_geiger_block_redstone));
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putInt("comparatorStrength", comparatorStrength);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        comparatorStrength = nbt.getInt("comparatorStrength");
    }
}