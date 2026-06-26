package com.nred.nuclearcraft.multiblock.distributor;

import com.nred.nuclearcraft.block_entity.distributor.AbstractDistributorEntity;
import com.nred.nuclearcraft.block_entity.distributor.DistributorBufferEntity;
import com.nred.nuclearcraft.block_entity.distributor.DistributorInletEntity;
import com.nred.nuclearcraft.block_entity.distributor.DistributorOutletEntity;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.passive.ITilePassive;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.util.InventoryStackList;
import com.nred.nuclearcraft.util.NBTHelper;
import com.nred.nuclearcraft.util.NCInventoryHelper;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.stream.Collectors;

public class Distributor extends Multiblock<Distributor> {
    public static final long BASE_ENERGY_CAPACITY = 16000L;
    public static final int BASE_ITEM_STACK_LIMIT = 64;
    public static final int BASE_FLUID_CAPACITY = 4000;

    public final EnergyStorage energyStorage = new EnergyStorage(BASE_ENERGY_CAPACITY);
    public InventoryStackList inventoryStacks = InventoryStackList.withSize(1);
    public int itemStackLimit = BASE_ITEM_STACK_LIMIT;
    public final Tank tank = new Tank(BASE_FLUID_CAPACITY, null);

    protected boolean refreshStorage = false;
    public boolean storageUpdated = false;

    protected int energyOutputOffset = 0, itemOutputOffset = 0, fluidOutputOffset = 0;

    public Distributor(Level level) {
        super(level);
    }

    @Override
    public int getMinimumInteriorLength() {
        return 0;
    }

    @Override
    public int getMaximumInteriorLength() {
        return 0;
    }

    @Override
    protected void onPartAdded(IMultiblockPart<Distributor> newPart) {
        super.onPartAdded(newPart);
        refreshStorage = true;
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<Distributor> oldPart) {
        super.onPartRemoved(oldPart);
        refreshStorage = true;
    }

    @Override
    protected void onMachineAssembled() {
        onMultiblockFormed();
    }

    @Override
    protected void onMachineRestored() {
        onMultiblockFormed();
    }

    protected void onMultiblockFormed() {
        if (!getWorld().isClientSide) {
            refreshStorage = true;
        }
    }

    @Override
    protected void onMachinePaused() {
    }

    @Override
    protected void onMachineDisassembled() {
    }

    @Override
    protected int getMinimumNumberOfPartsForAssembledMachine() {
        return 1;
    }

    @Override
    protected int getMaximumXSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaximumZSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaximumYSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    protected void onAssimilate(IMultiblockController<Distributor> iMultiblockController) {
        Distributor assimilated = (Distributor) iMultiblockController;
        energyStorage.setStorageCapacity(energyStorage.getMaxEnergyStoredLong() + assimilated.energyStorage.getMaxEnergyStoredLong());
        energyStorage.setEnergyStored(energyStorage.getEnergyStoredLong() + assimilated.energyStorage.getEnergyStoredLong());
        assimilated.energyStorage.setEnergyStored(0L);

        ItemStack assimilatedStack = assimilated.inventoryStacks.getFirst();
        if (!assimilatedStack.isEmpty()) {
            ItemStack remaining = insertIntoInventory(assimilatedStack.copy());
            if (!remaining.isEmpty()) {
                dropOverflow(Collections.singletonList(remaining));
            }
        }

        FluidStack otherFluid = assimilated.tank.getFluid();
        if (otherFluid != FluidStack.EMPTY && (tank.getFluid() == FluidStack.EMPTY || FluidStack.isSameFluidSameComponents(tank.getFluid(), otherFluid))) {
            tank.setTankCapacity(tank.getCapacity() + assimilated.tank.getCapacity());
            int filled = tank.fill(otherFluid.copy(), IFluidHandler.FluidAction.EXECUTE);
            assimilated.tank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
        }

        energyOutputOffset = Math.max(energyOutputOffset, assimilated.energyOutputOffset);
        itemOutputOffset = Math.max(itemOutputOffset, assimilated.itemOutputOffset);
        fluidOutputOffset = Math.max(fluidOutputOffset, assimilated.fluidOutputOffset);

        refreshStorage = true;
    }

    @Override
    protected boolean updateServer() {
        super.updateServer();
        boolean shouldUpdate = storageUpdated;
        storageUpdated = false;

        if (refreshStorage) {
            shouldUpdate |= refreshCapacity();
            refreshStorage = false;
        }

        List<DistributorOutletEntity> outlets = getParts(DistributorOutletEntity.class).stream().sorted(Comparator.comparingLong(x -> x.getBlockPos().asLong())).collect(Collectors.toList());

        if (!outlets.isEmpty()) {
            shouldUpdate |= distributeEnergy(outlets);
            shouldUpdate |= distributeItems(outlets);
            shouldUpdate |= distributeFluid(outlets);
        }

        return shouldUpdate;
    }

    protected boolean refreshCapacity() {
        boolean changed = false;

        long mult = Math.max(1L, (long) getPartCount(DistributorBufferEntity.class) + (long) getPartCount(DistributorInletEntity.class) + (long) getPartCount(DistributorOutletEntity.class));

        long newEnergyCapacity = BASE_ENERGY_CAPACITY * mult;
        if (energyStorage.getMaxEnergyStoredLong() != newEnergyCapacity) {
            energyStorage.setStorageCapacity(newEnergyCapacity);
            energyStorage.setMaxTransfer(newEnergyCapacity);
            energyStorage.cullEnergyStored();
            changed = true;
        }

        int newItemStackLimit = Math.max(64, NCMath.toInt(BASE_ITEM_STACK_LIMIT * mult));
        if (itemStackLimit != newItemStackLimit) {
            itemStackLimit = newItemStackLimit;
            changed = true;
        }

        changed |= cullInventory();

        int newTankCapacity = NCMath.toInt(BASE_FLUID_CAPACITY * mult);
        if (tank.getCapacity() != newTankCapacity) {
            tank.setTankCapacity(newTankCapacity);
            tank.clampTankAmount();
            changed = true;
        }

        return changed;
    }

    protected boolean cullInventory() {
        ItemStack stack = inventoryStacks.getFirst();
        if (stack.isEmpty() || stack.getCount() <= itemStackLimit) {
            return false;
        }

        int overflowCount = stack.getCount() - itemStackLimit;
        stack.setCount(itemStackLimit);
        ItemStack overflow = stack.copy();
        overflow.setCount(overflowCount);
        dropOverflow(Collections.singletonList(overflow));
        return true;
    }

    protected void dropOverflow(List<ItemStack> overflow) {
        if (getWorld().isClientSide() || overflow.isEmpty()) {
            return;
        }
        getReferenceCoord().ifPresent(blockPos -> NCInventoryHelper.dropInventoryItems(getWorld(), blockPos, overflow));
    }

    protected boolean distributeEnergy(List<DistributorOutletEntity> outlets) {
        int storedEnergy = NCMath.toInt(energyStorage.getEnergyStoredLong());
        if (storedEnergy <= 0) {
            return false;
        }

        List<IEnergyStorage> availableHandlers = new ArrayList<>();
        List<Integer> capacityList = new ArrayList<>();
        long capacitySum = 0L;

        for (DistributorOutletEntity outlet : outlets) {
            for (Direction side : Direction.values()) {
                BlockEntity blockEntity = getWorld().getBlockEntity(outlet.getBlockPos().relative(side));
                if (blockEntity == null || blockEntity instanceof AbstractDistributorEntity || (blockEntity instanceof ITilePassive tilePassive && !tilePassive.canPushEnergyTo())) {
                    continue;
                }

                IEnergyStorage handler = getWorld().getCapability(Capabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), side.getOpposite());
                if (handler == null) {
                    continue;
                }

                int capacity = handler.receiveEnergy(storedEnergy, true);
                if (capacity > 0) {
                    availableHandlers.add(handler);
                    capacityList.add(capacity);
                    capacitySum += capacity;
                }
            }
        }

        if (availableHandlers.isEmpty()) {
            return false;
        }

        int[] capacities = capacityList.stream().mapToInt(Integer::intValue).toArray();
        int distributable = Math.min(storedEnergy, NCMath.toInt(capacitySum));
        int[] allocations = getAllocations(distributable, capacities, energyOutputOffset);

        long totalSent = 0L;
        for (int i = 0; i < availableHandlers.size(); ++i) {
            int allocation = allocations[i];
            if (allocation <= 0) {
                continue;
            }

            int toSend = Math.min(allocation, NCMath.toInt(energyStorage.getEnergyStoredLong()));
            if (toSend <= 0) {
                continue;
            }
            int sent = availableHandlers.get(i).receiveEnergy(toSend, false);
            if (sent > 0) {
                energyStorage.extractEnergy(sent, false);
                totalSent += sent;
            }
        }

        energyOutputOffset = availableHandlers.isEmpty() ? 0 : (energyOutputOffset + 1) % availableHandlers.size();
        return totalSent > 0L;
    }

    protected boolean distributeItems(List<DistributorOutletEntity> outlets) {
        if (inventoryStacks.getFirst().isEmpty()) {
            return false;
        }

        ItemStack storedStack = inventoryStacks.getFirst();
        List<IItemHandler> availableHandlers = new ArrayList<>();
        List<Integer> capacityList = new ArrayList<>();
        long capacitySum = 0L;

        for (DistributorOutletEntity outlet : outlets) {
            for (Direction side : Direction.values()) {
                BlockEntity blockEntity = getWorld().getBlockEntity(outlet.getBlockPos().relative(side));
                if (blockEntity == null || blockEntity instanceof AbstractDistributorEntity || (blockEntity instanceof ITilePassive tilePassive && !tilePassive.canPushItemsTo())) {
                    continue;
                }

                IItemHandler handler = getWorld().getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), side.getOpposite());
                if (handler == null || handler.getSlots() <= 0) {
                    continue;
                }

                ItemStack offered = storedStack.copy();
                offered.setCount(storedStack.getCount());
                int capacity = offered.getCount() - ItemHandlerHelper.insertItemStacked(handler, offered, true).getCount();
                if (capacity > 0) {
                    availableHandlers.add(handler);
                    capacityList.add(capacity);
                    capacitySum += capacity;
                }
            }
        }

        if (availableHandlers.isEmpty()) {
            return false;
        }

        int[] capacities = capacityList.stream().mapToInt(Integer::intValue).toArray();
        int distributable = Math.min(storedStack.getCount(), NCMath.toInt(capacitySum));
        int[] allocations = getAllocations(distributable, capacities, itemOutputOffset);

        boolean changed = false;
        for (int i = 0; i < availableHandlers.size(); ++i) {
            int allocation = allocations[i];
            if (allocation <= 0 || storedStack.isEmpty()) {
                continue;
            }

            int toSend = Math.min(allocation, storedStack.getCount());
            ItemStack offered = storedStack.copy();
            offered.setCount(toSend);
            int sent = toSend - ItemHandlerHelper.insertItemStacked(availableHandlers.get(i), offered, false).getCount();
            if (sent > 0) {
                storedStack.shrink(sent);
                if (storedStack.getCount() <= 0) {
                    inventoryStacks.set(0, ItemStack.EMPTY);
                }
                changed = true;
            }
        }

        itemOutputOffset = availableHandlers.isEmpty() ? 0 : (itemOutputOffset + 1) % availableHandlers.size();
        return changed;
    }

    protected boolean distributeFluid(List<DistributorOutletEntity> outlets) {
        FluidStack fluid = tank.getFluid();
        if (fluid == FluidStack.EMPTY) {
            return false;
        }

        List<IFluidHandler> availableHandlers = new ArrayList<>();
        List<Integer> capacityList = new ArrayList<>();
        long capacitySum = 0L;

        for (DistributorOutletEntity outlet : outlets) {
            for (Direction side : Direction.values()) {
                BlockEntity blockEntity = getWorld().getBlockEntity(outlet.getBlockPos().relative(side));
                if (blockEntity == null || blockEntity instanceof AbstractDistributorEntity || (blockEntity instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo())) {
                    continue;
                }

                // TODO cannot output Mekanism chemicals
                IFluidHandler handler = getWorld().getCapability(Capabilities.FluidHandler.BLOCK, blockEntity.getBlockPos(), side.getOpposite());
                if (handler == null) {
                    continue;
                }

                int capacity = handler.fill(fluid.copyWithAmount(tank.getFluidAmount()), IFluidHandler.FluidAction.SIMULATE);
                if (capacity > 0) {
                    availableHandlers.add(handler);
                    capacityList.add(capacity);
                    capacitySum += capacity;
                }
            }
        }

        if (availableHandlers.isEmpty()) {
            return false;
        }

        int[] capacities = capacityList.stream().mapToInt(Integer::intValue).toArray();
        int distributable = Math.min(tank.getFluidAmount(), NCMath.toInt(capacitySum));
        int[] allocations = getAllocations(distributable, capacities, fluidOutputOffset);

        int totalSent = 0;
        for (int i = 0; i < availableHandlers.size(); ++i) {
            int allocation = allocations[i];
            if (allocation <= 0 || tank.getFluid() == FluidStack.EMPTY) {
                continue;
            }

            int toSend = Math.min(allocation, tank.getFluidAmount());
            if (toSend <= 0) {
                continue;
            }
            int sent = availableHandlers.get(i).fill(fluid.copyWithAmount(toSend), IFluidHandler.FluidAction.EXECUTE);
            if (sent > 0) {
                tank.drain(sent, IFluidHandler.FluidAction.EXECUTE);
                totalSent += sent;
            }
        }

        fluidOutputOffset = availableHandlers.isEmpty() ? 0 : (fluidOutputOffset + 1) % availableHandlers.size();
        return totalSent > 0;
    }

    protected int[] getAllocations(int total, int[] capacities, int offset) {
        int size = capacities.length;
        int[] allocations = new int[size];
        if (total <= 0 || size == 0) {
            return allocations;
        }

        List<Integer> active = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            if (capacities[i] > 0) {
                active.add(i);
            }
        }

        if (active.isEmpty()) {
            return allocations;
        }

        int activeOffset = Math.floorMod(offset, active.size());
        if (activeOffset != 0) {
            Collections.rotate(active, -activeOffset);
        }

        int[] remainingCapacity = Arrays.copyOf(capacities, size);
        int remaining = total;

        while (remaining > 0 && !active.isEmpty()) {
            int share = remaining / active.size();
            if (share == 0) {
                share = 1;
            }

            boolean progressed = false;
            List<Integer> nextActive = new ArrayList<>();

            for (int i : active) {
                if (remaining <= 0) {
                    break;
                }

                int give = Math.min(share, Math.min(remaining, remainingCapacity[i]));
                if (give > 0) {
                    allocations[i] += give;
                    remainingCapacity[i] -= give;
                    remaining -= give;
                    progressed = true;
                }

                if (remainingCapacity[i] > 0) {
                    nextActive.add(i);
                }
            }

            if (!progressed) {
                break;
            }

            active = nextActive;
        }

        return allocations;
    }

    protected ItemStack insertIntoInventory(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack slotStack = inventoryStacks.getFirst();
        if (slotStack.isEmpty()) {
            int toMove = Math.min(itemStackLimit, stack.getCount());
            ItemStack inserted = stack.copy();
            inserted.setCount(toMove);
            inventoryStacks.set(0, inserted);
            stack.shrink(toMove);
            return stack.isEmpty() ? ItemStack.EMPTY : stack;
        }

        if (!ItemStack.isSameItemSameComponents(slotStack, stack)) {
            return stack;
        }

        int room = itemStackLimit - slotStack.getCount();
        if (room <= 0) {
            return stack;
        }

        int toMove = Math.min(room, stack.getCount());
        slotStack.grow(toMove);
        stack.shrink(toMove);
        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    @Override
    protected void updateClient() {
    }

    @Override
    protected boolean isBlockGoodForInterior(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        readEnergy(energyStorage, data, registries, "energyStorage");

        itemStackLimit = data.contains("itemStackLimit") ? data.getInt("itemStackLimit") : BASE_ITEM_STACK_LIMIT;

        inventoryStacks = InventoryStackList.withSize(1);
        NBTHelper.readAllItems(data, registries, "distributorInventory", inventoryStacks);
        cullInventory();

        tank.readFromNBT(data, registries, "distributorTank");

        itemOutputOffset = data.getInt("itemOutputOffset");
        fluidOutputOffset = data.getInt("fluidOutputOffset");
        energyOutputOffset = data.getInt("energyOutputOffset");

        refreshStorage = true;
    }

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        writeEnergy(energyStorage, data, registries, "energyStorage");
        data.putInt("itemStackLimit", itemStackLimit);
        NBTHelper.writeAllItems(data, registries, "distributorInventory", inventoryStacks);
        tank.writeToNBT(data, registries, "distributorTank");

        data.putInt("itemOutputOffset", itemOutputOffset);
        data.putInt("fluidOutputOffset", fluidOutputOffset);
        data.putInt("energyOutputOffset", energyOutputOffset);

        return data;
    }
}