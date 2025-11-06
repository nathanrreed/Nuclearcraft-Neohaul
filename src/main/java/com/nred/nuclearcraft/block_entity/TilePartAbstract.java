package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import it.zerono.mods.zerocore.lib.data.nbt.INestedSyncableEntity;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class TilePartAbstract<MULTIBLOCK extends AbstractCuboidMultiblockController<MULTIBLOCK>> extends AbstractCuboidMultiblockPart<MULTIBLOCK> implements ITile, INestedSyncableEntity {
    private boolean isRedstonePowered = false, alternateComparator = false, redstoneControl = false;

//	private final IRadiationSource radiation; TODO

    public TilePartAbstract(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
//		radiation = new RadiationSource(0D);
    }

    @Override
    public void onLoad() {
        if (level.isClientSide) {
//            level.markBlockRangeForRenderUpdate(pos, pos); TODO
            refreshIsRedstonePowered(level, worldPosition);
            setChanged();
            updateComparatorOutputLevel();
        }
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        // Most Reactor parts are not allowed on the Frame an inside the Reactor so reject those positions and allow all the other ones

        final BlockPos coordinates = this.getWorldPosition();

        if (position.isFrame()) {
            validatorCallback.setLastError(coordinates, NuclearcraftNeohaul.MODID + ".multiblock.validation.invalid_frame_block", coordinates.getX(), coordinates.getY(), coordinates.getZ());
            return false;
        } else if (PartPosition.Interior == position) {
            validatorCallback.setLastError(coordinates, NuclearcraftNeohaul.MODID + ".multiblock.validation.invalid_part_for_interior", coordinates.getX(), coordinates.getY(), coordinates.getZ());
            return false;
        }

        return true;
    }

    @Override
    public void onMachineActivated() {
    }

    @Override
    public void onMachineDeactivated() {
    }

    @Override
    public BlockEntity getTile() {
        return this;
    }

    @Override
    public Level getTileWorld() {
        return level;
    }

    @Override
    public BlockPos getTilePos() {
        return worldPosition;
    }

    @Override
    public Block getTileBlockType() {
        return getBlockState().getBlock();
    }

//	@Override
//	public IRadiationSource getRadiationSource() {
//		return radiation;
//	}

    public boolean isUseableByPlayer(Player entityplayer) {
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return entityplayer.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= 64D;
    }

    @Override
    public final void markTileDirty() {
        setChanged();
    }

    @Override
    public void setChanged() {
        if (level != null) {
            level.blockEntityChanged(worldPosition);
        }
    }

    // Redstone

    @Override
    public boolean getIsRedstonePowered() {
        return isRedstonePowered;
    }

    @Override
    public void setIsRedstonePowered(boolean isRedstonePowered) {
        this.isRedstonePowered = isRedstonePowered;
    }

    @Override
    public boolean getAlternateComparator() {
        return alternateComparator;
    }

    @Override
    public void setAlternateComparator(boolean alternate) {
        alternateComparator = alternate;
    }

    @Override
    public boolean getRedstoneControl() {
        return redstoneControl;
    }

    @Override
    public void setRedstoneControl(boolean redstoneControl) {
        this.redstoneControl = redstoneControl;
    }

    // NBT

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        writeAll(tag, registries);
    }

    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putBoolean("isRedstonePowered", isRedstonePowered);
        nbt.putBoolean("alternateComparator", alternateComparator);
        nbt.putBoolean("redstoneControl", redstoneControl);
        if (shouldSaveRadiation()) {
            writeRadiation(nbt, registries);
        }
        return nbt;
    }

    public CompoundTag writeRadiation(CompoundTag nbt, HolderLookup.Provider registries) {
//		nbt.putDouble("radiationLevel", getRadiationSource().getRadiationLevel()); TODO
        return nbt;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        readAll(tag, registries);
    }

    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        isRedstonePowered = nbt.getBoolean("isRedstonePowered");
        alternateComparator = nbt.getBoolean("alternateComparator");
        redstoneControl = nbt.getBoolean("redstoneControl");
        if (shouldSaveRadiation()) {
            readRadiation(nbt, registries);
        }
    }

    public void readRadiation(CompoundTag nbt, HolderLookup.Provider registries) {
        if (nbt.contains("radiationLevel")) {
//			getRadiationSource().setRadiationLevel(nbt.getDouble("radiationLevel")); TODO
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Optional<ISyncableEntity> getNestedSyncableEntity() {
        return this.getMultiblockController().map(c -> c);
    }
}