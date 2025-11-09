package com.nred.nuclearcraft.block_entity.rtg;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.TilePartAbstract;
import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.multiblock.rtg.RTGMultiblock;
import com.nred.nuclearcraft.multiblock.rtg.RTGType;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.RTG_ENTITY_TYPE;

public class RTGEntity extends TilePartAbstract<RTGMultiblock> implements ITickable, ITileEnergy {
    private final EnergyStorage backupStorage = new EnergyStorage(0L);

    private @Nonnull
    final EnergyConnection[] energyConnections;
    private boolean[] ignoreSide = new boolean[]{false, false, false, false, false, false};

    private @Nonnull
    final EnergyTileWrapper[] energySides;

    public final RTGType rtgType;

    public RTGEntity(BlockPos position, BlockState blockState, RTGType rtgType) {
        super(RTG_ENTITY_TYPE.get(), position, blockState);
        energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.OUT);
        energySides = ITileEnergy.getDefaultEnergySides(this);
        this.rtgType = rtgType;
//		getRadiationSource().setRadiationLevel(rtgType.radiation); TODO
    }

    public boolean ignoreSide(Direction side) {
        return side != null && ignoreSide[side.ordinal()];
    }

    @Override
    public RTGMultiblock createController() {
        return new RTGMultiblock(level);
    }

    @Override
    public Class<RTGMultiblock> getControllerType() {
        return RTGMultiblock.class;
    }

    @Override
    public BlockEntity getTile() {
        return this;
    }

    @Override
    public Level getTileWorld() {
        return getCurrentWorld();
    }

    @Override
    public Block getTileBlockType() {
        return getBlockType();
    }

    @Override
    public boolean getIsRedstonePowered() {
        return false;
    }

    @Override
    public void setIsRedstonePowered(boolean isRedstonePowered) {
    }

    @Override
    public boolean getAlternateComparator() {
        return false;
    }

    @Override
    public void setAlternateComparator(boolean alternate) {
    }

    @Override
    public boolean getRedstoneControl() {
        return false;
    }

    @Override
    public void setRedstoneControl(boolean redstoneControl) {
    }

    @Override
    public void onPreMachineAssembled(RTGMultiblock controller) {
    }

    @Override
    public void onPostMachineAssembled(RTGMultiblock controller) {
    }

    @Override
    public void onPreMachineBroken() {
    }

    @Override
    public void onPostMachineBroken() {
    }

    @Override
    public void onMachineActivated() {
    }

    @Override
    public void onMachineDeactivated() {
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            pushEnergy();
        }
    }

    @Override
    public void pushEnergyToSide(@Nonnull Direction side) {
        if (!ignoreSide(side)) {
            ITileEnergy.super.pushEnergyToSide(side);
        }
    }

    public void onMultiblockRefresh() {
        for (Direction side : Direction.values()) {
            ignoreSide[side.ordinal()] = level.getBlockEntity(worldPosition.relative(side)) instanceof RTGEntity;
        }
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        RTGMultiblock multiblock = getMultiblockController().orElse(null);
        return multiblock != null ? multiblock.getEnergyStorage() : backupStorage;
    }

    @Override
    public EnergyConnection[] getEnergyConnections() {
        return energyConnections;
    }

    @Override
    public @Nonnull EnergyTileWrapper[] getEnergySides() {
        return energySides;
    }

    @Override
    public boolean hasConfigurableEnergyConnections() {
        return true;
    }

    // NBT

    @Override
    protected void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        writeEnergyConnections(data, registries);
        data.putByteArray("ignoreSide", NCMath.booleansToBytes(ignoreSide));
    }

    @Override
    public void loadAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.loadAdditional(data, registries);
        readEnergyConnections(data, registries);
        boolean[] arr = NCMath.bytesToBooleans(data.getByteArray("ignoreSide"));
        if (arr.length == 6) {
            ignoreSide = arr;
        }
    }
}