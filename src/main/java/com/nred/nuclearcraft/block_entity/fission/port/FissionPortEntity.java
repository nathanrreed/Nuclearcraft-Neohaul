package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.AbstractFissionEntity;
import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public abstract class FissionPortEntity<PORT extends FissionPortEntity<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends AbstractFissionEntity implements ITickable, IFissionPort<PORT, TARGET> {
    protected final Class<PORT> portClass;
    protected BlockPos masterPortPos = DEFAULT_NON;
    protected PORT masterPort = null;
    protected ObjectSet<TARGET> targets = new ObjectOpenHashSet<>();
    public boolean refreshTargetsFlag = false;

    public Axis axis = Axis.Z;

    public FissionPortEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState, Class<PORT> portClass) {
        super(type, position, blockState);
        this.portClass = portClass;
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPostMachineAssembled(FissionReactor controller) {
        super.onPostMachineAssembled(controller);
        if (!level.isClientSide) {
            Optional<Direction> posFacing = getPartPosition().getDirection();
            posFacing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2)); // TODO was AXIS_ALL
        }
    }

    @Override
    public @Nonnull PartPosition getPartPosition() {
        PartPosition partPos = super.getPartPosition();
        if (partPos.getDirection().isPresent()) {
            axis = partPos.getDirection().get().getAxis();
        }
        return partPos;
    }

    @Override
    public ObjectSet<TARGET> getTargets() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTargets() : targets;
    }

    @Override
    public BlockPos getMasterPortPos() {
        return masterPortPos;
    }

    @Override
    public void setMasterPortPos(BlockPos pos) {
        masterPortPos = pos;
    }

    @Override
    public void clearMasterPort() {
        masterPort = null;
        masterPortPos = DEFAULT_NON;
    }

    @Override
    public void refreshMasterPort() {
        Optional<FissionReactor> multiblock = getMultiblockController();
        masterPort = multiblock.map(fissionReactor -> fissionReactor.getPartMap(portClass).get(masterPortPos.asLong())).orElse(null);
        if (masterPort == null) {
            masterPortPos = DEFAULT_NON;
        }
    }

    @Override
    public void refreshTargets() {
        refreshTargetsFlag = false;
        if (isMachineAssembled()) {
            for (TARGET part : getTargets()) {
                part.onPortRefresh();
            }
        }
    }

    @Override
    public void setRefreshTargetsFlag(boolean refreshTargetsFlag) {
        this.refreshTargetsFlag = refreshTargetsFlag;
    }

    // ITickable

    @Override
    public void update() {
        if (!level.isClientSide) {
            if (refreshTargetsFlag) {
                refreshTargets();
            }
        }
    }

    @Override
    public void setChanged() {
        refreshTargetsFlag = true;
        super.setChanged();
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        // nbt.setLong("masterPortPos", masterPortPos.toLong());
        nbt.putString("axis", axis.getName());
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        // masterPortPos = BlockPos.fromLong(nbt.getLong("masterPortPos"));
        Axis ax = Axis.byName(nbt.getString("axis"));
        this.axis = ax == null ? Axis.Z : ax;
    }
}