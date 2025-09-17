package com.nred.nuclearcraft.block.batteries;

import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
import com.nred.nuclearcraft.info.energy.EnergyConnection;
import com.nred.nuclearcraft.multiblock.battery.BatteryMultiblock;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.config.Config.BATTERY_CONFIG_CAPACITY;
import static com.nred.nuclearcraft.config.Config.VOLTAIC_PILE_CAPACITY;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.BATTERY_ENTITY_TYPE;

public class BatteryEntity extends AbstractCuboidMultiblockPart<BatteryMultiblock> {
    private final int tier;
    public CustomEnergyHandler backupStorage;
    public int capacity;
    private final Map<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> capCache = new HashMap<>();
    final Map<Direction, EnergyConnection> sideOptions = Arrays.stream(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> EnergyConnection.IN));
    protected boolean[] ignoreSide = new boolean[]{false, false, false, false, false, false};

    public BatteryEntity(BlockPos pos, BlockState blockState, int tier) {
        super(BATTERY_ENTITY_TYPE.get(tier).get(), pos, blockState);
        this.tier = tier;

        capacity = this.tier < 10 ? BATTERY_CONFIG_CAPACITY.get(this.tier) : VOLTAIC_PILE_CAPACITY.get(this.tier - 10);
        backupStorage = new CustomEnergyHandler(capacity, false, false);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
//        tag.put("energy", backupStorage.serializeNBT(registries)); TODO REMOVE
        tag.putByteArray("ignoreSide", NCMath.booleansToBytes(ignoreSide));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

//        if (tag.contains("energy"))
//            backupStorage.deserializeNBT(registries, tag.get("energy"));

        boolean[] arr = NCMath.bytesToBooleans(tag.getByteArray("ignoreSide"));
        if (arr.length == 6) {
            ignoreSide = arr;
        }
    }

    @Override
    public boolean isGoodForPosition(PartPosition partPosition, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    @Override
    public BatteryMultiblock createController() {
        return new BatteryMultiblock(level);
    }

    @Override
    public Class<BatteryMultiblock> getControllerType() {
        return BatteryMultiblock.class;
    }

    public boolean ignoreSide(Direction side) {
        return side != null && ignoreSide[side.ordinal()];
    }

    @Override
    public void onMachineActivated() {
    }

    @Override
    public void onMachineDeactivated() {
    }

    public CustomEnergyHandler getEnergyStorage() {
        Optional<BatteryMultiblock> multiblock = getMultiblockController();
        return multiblock.map(BatteryMultiblock::getEnergyStorage).orElseGet(() -> backupStorage);
    }

    public void onMultiblockRefresh() {
        for (Direction side : Direction.values()) {
            ignoreSide[side.ordinal()] = level.getBlockEntity(worldPosition.relative(side)) instanceof BatteryEntity;
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        for (Direction side : Direction.values()) {
            if (getEnergyStorage().getEnergyStored() <= 0) {
                return;
            }
            pushEnergyToSide(side);
        }
    }

    public void onCapInvalidate() {

    }

    public void pushEnergyToSide(@Nonnull Direction side) {
        if (this.capCache.get(side) == null) {
            this.capCache.put(side, BlockCapabilityCache.create(Capabilities.EnergyStorage.BLOCK, ((ServerLevel) level), worldPosition.relative(side), side.getOpposite(), () -> !this.isRemoved(), this::onCapInvalidate));
        }

        if (!ignoreSide(side) && level != null && sideOptions.get(side).canExtract() && capCache.get(side).getCapability() != null) {
            @Nullable IEnergyStorage storage = capCache.get(side).getCapability();
//            IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, worldPosition.relative(side), side.getOpposite());
            if (storage != null && storage.canReceive()) {
                CustomEnergyHandler energyHandler = getEnergyStorage();
                int extracted = energyHandler.getEnergyStored();
                energyHandler.extractEnergy(extracted, false);
                int actual = storage.receiveEnergy(extracted, false);
                if (extracted != actual) {
                    energyHandler.internalInsertEnergy(extracted - actual, false);
                }
            }
        }
    }
}