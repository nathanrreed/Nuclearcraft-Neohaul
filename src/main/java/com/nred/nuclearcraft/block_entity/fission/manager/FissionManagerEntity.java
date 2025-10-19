package com.nred.nuclearcraft.block_entity.fission.manager;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.fission.AbstractFissionEntity;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.util.NBTHelper;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public abstract class FissionManagerEntity<MANAGER extends FissionManagerEntity<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends AbstractFissionEntity implements ITickable, IFissionManager<MANAGER, LISTENER> {
    protected final Class<MANAGER> managerClass;
    protected LongSet listenerPosSet = new LongOpenHashSet();
    public boolean refreshListenersFlag = false;

    public FissionManagerEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState, Class<MANAGER> managerClass) {
        super(type, position, blockState);
        this.managerClass = managerClass;
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(FissionReactor controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    @Override
    public LongSet getListenerPosSet() {
        return listenerPosSet;
    }

    @Override
    public boolean getRefreshListenersFlag() {
        return refreshListenersFlag;
    }

    @Override
    public void setRefreshListenersFlag(boolean flag) {
        refreshListenersFlag = flag;
    }

    // Ticking

    @Override
    public void update() {
        if (!level.isClientSide) {
            if (refreshListenersFlag) {
                refreshListeners();
            }
        }
    }

    @Override
    public void setChanged() {
        refreshListenersFlag = true;
        super.setChanged();
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        NBTHelper.writeLongCollection(nbt, listenerPosSet, "listenerPosSet");
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        NBTHelper.readLongCollection(nbt, listenerPosSet, "listenerPosSet");
    }
}