package com.nred.nuclearcraft.helpers;

import com.nred.nuclearcraft.helpers.SideConfigEnums.OutputSetting;
import com.nred.nuclearcraft.helpers.SideConfigEnums.SideConfigSetting;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CustomFluidStackHandler implements IFluidHandler, INBTSerializable<CompoundTag>, IProcessorHandlerConfigs {
    private final int capacity;
    private final int tanks;
    private final boolean allowInput;
    private final boolean allowOutput;
    protected NonNullList<FluidStack> fluids;
    public ArrayList<OutputSetting> outputSettings;
    public Map<Direction, List<SideConfigSetting>> sideConfig;

    public CustomFluidStackHandler(int capacity, int tanks, boolean allowInput, boolean allowOutput) {
        this.capacity = capacity;
        this.tanks = tanks;
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;

        setSize(tanks);
    }

    public void createOutputSettings() {
        if (outputSettings == null) {
            this.outputSettings = new ArrayList<>(Collections.nCopies(tanks, OutputSetting.DEFAULT));
        } else {
            throw new IllegalStateException("Output setting already exists");
        }
    }

    @Override
    public void createSideConfig(int inputs, int outputs) {
        if (sideConfig == null) {
            this.sideConfig = Arrays.stream(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new ArrayList<>(Stream.of(Collections.nCopies(inputs, SideConfigEnums.SideConfigSetting.INPUT), Collections.nCopies(outputs, SideConfigEnums.SideConfigSetting.OUTPUT)).flatMap(Collection::stream).toList())));
        } else {
            throw new IllegalStateException("Output setting already exists");
        }
    }

    @Override
    public int getTanks() {
        return tanks;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        validateSlotIndex(tank);
        return fluids.get(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public abstract boolean isFluidValid(int tank, FluidStack stack);

    public abstract boolean canOutput(int tank);

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : fluids.size());
        ListTag tagList = nbt.getList("Fluids", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag fluidTags = tagList.getCompound(i);
            int slot = fluidTags.getInt("Slot");

            if (slot >= 0 && slot < fluids.size()) {
                FluidStack.parse(provider, fluidTags).ifPresent(fluid -> fluids.set(slot, fluid));
            }
        }
        this.outputSettings = OutputSetting.deserializeNBT(provider, nbt);
        this.sideConfig = SideConfigSetting.deserializeNBT(provider, nbt);

        onLoad();
    }

    public void setSize(int size) {
        fluids = NonNullList.withSize(size, FluidStack.EMPTY);
        onContentsChanged();
    }

    public int getFluidAmount(int tank) {
        return fluids.get(tank).getAmount();
    }

    public void setFluid(int tank, FluidStack fluidStack) {
        validateSlotIndex(tank);
        fluids.set(tank, fluidStack);
        onContentsChanged();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < this.getTanks(); i++) {
            if (!fluids.get(i).isEmpty()) {
                CompoundTag fluidTag = new CompoundTag();
                fluidTag.putInt("Slot", i);
                nbtTagList.add(fluids.get(i).save(provider, fluidTag));
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Fluids", nbtTagList);
        nbt.putInt("Size", fluids.size());
        nbt = OutputSetting.serializeNBT(provider, nbt, outputSettings);
        nbt = SideConfigSetting.serializeNBT(provider, nbt, sideConfig);

        return nbt;
    }

    // Stop other mods using capability from doing unexpected things
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (allowInput) {
            return internalInsertFluid(resource, action);
        }
        return 0;
    }

    public int fill(int tank, FluidStack resource, FluidAction action) {
        if (!FluidStack.isSameFluidSameComponents(getFluidInTank(tank), resource) && !getFluidInTank(tank).isEmpty()) {
            return 0;
        }

        int amount = resource.getAmount();
        if (action.execute()) {
            setFluid(tank, resource.copyWithAmount(resource.getAmount() + getFluidAmount(tank)));
        }

        if (getFluidAmount(tank) + amount > capacity) {
            setFluid(tank, getFluidInTank(tank).copyWithAmount(capacity));
            return capacity - getFluidAmount(tank);
        }
        return amount;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (allowOutput) {
            return internalExtractFluid(resource, action);
        }
        return FluidStack.EMPTY;
    }

    public FluidStack drain(int tank, int maxDrain, FluidAction action) {
        FluidStack fluid = fluids.get(tank);
        if (fluid.isEmpty()) return FluidStack.EMPTY;
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = fluid.copyWithAmount(drained);
        if (action.execute() && drained > 0) {
            fluid.shrink(drained);
            onContentsChanged();
        }
        if (drained > 0) {
            return stack;
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (allowOutput) {
            return internalExtractFluid(maxDrain, action);
        }
        return FluidStack.EMPTY;
    }

    // Normal insert and extract for use within the mod
    public int internalInsertFluid(FluidStack resource, FluidAction action) {
        return internalInsertFluid(resource, action, false, Direction.NORTH);
    }

    public int internalInsertFluid(FluidStack resource, FluidAction action, boolean check, Direction dir) {
        if (resource.isEmpty()) {
            return 0;
        }
        if (action.simulate()) {
            int amount = 0;
            int i = -1; // Will start at 0 always since i++ is right after, just makes continues easier
            for (FluidStack fluid : fluids) {
                i++;
                if ((check && !sideConfig.get(dir).get(i).equals(SideConfigSetting.INPUT)) || !isFluidValid(i, resource)) continue;
                if (fluid.isEmpty()) {
                    amount += Math.min(capacity, resource.getAmount());
                }
                if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                    continue;
                }
                amount += Math.min(capacity - fluid.getAmount(), resource.getAmount());
            }
            return amount;
        } else {
            int i = -1; // Will start at 0 always since i++ is right after, just makes continues easier
            for (FluidStack fluid : fluids) {
                i++;
                if ((check && !sideConfig.get(dir).get(i).equals(SideConfigSetting.INPUT)) || !isFluidValid(i, resource)) continue;
                if (fluid.isEmpty()) {
                    fluids.set(i, resource.copyWithAmount(Math.min(capacity, resource.getAmount())));
                    onContentsChanged();
                    return fluids.get(i).getAmount();
                }
                if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                    return 0;
                }
                int filled = capacity - fluid.getAmount();

                if (resource.getAmount() < filled) {
                    fluid.grow(resource.getAmount());
                    filled = resource.getAmount();
                } else {
                    fluid.setAmount(capacity);
                }
                if (filled > 0) {
                    onContentsChanged();
                    return filled;
                }
            }
            return 0;
        }
    }

    public FluidStack internalExtractFluid(int maxDrain, FluidAction action) {
        return internalExtractFluid(maxDrain, action, false, Direction.NORTH);
    }

    public FluidStack internalExtractFluid(int maxDrain, FluidAction action, boolean check, Direction dir) {
        for (int i = 0; i < fluids.size(); i++) {
            FluidStack fluid = fluids.get(i);
            if ((check && !sideConfig.get(dir).get(i).equals(SideConfigSetting.OUTPUT)) || fluid.isEmpty() || !canOutput(i)) continue;
            int drained = maxDrain;
            if (fluid.getAmount() < drained) {
                drained = fluid.getAmount();
            }
            FluidStack stack = fluid.copyWithAmount(drained);
            if (action.execute() && drained > 0) {
                fluid.shrink(drained);
                onContentsChanged();
            }
            if (drained > 0) {
                return stack;
            }
        }
        return FluidStack.EMPTY;
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= fluids.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + fluids.size() + ")");
    }

    public FluidStack internalExtractFluid(FluidStack resource, FluidAction action) {
        return internalExtractFluid(resource, action, false, Direction.NORTH);
    }

    public FluidStack internalExtractFluid(FluidStack resource, FluidAction action, boolean check, Direction dir) {
        if (fluids.stream().anyMatch(fluidStack -> fluidStack.is(resource.getFluid()))) {
            return internalExtractFluid(resource.getAmount(), action, check, dir);
        }
        return FluidStack.EMPTY;
    }

    protected void onLoad() {
    }

    public void outputSetting(int index, boolean left) {
        this.outputSettings.set(index, this.outputSettings.get(index).next(!left));
        onContentsChanged();
    }

    public void sideConfig(int index, Direction dir, int inputs, boolean left) {
        this.sideConfig.get(dir).set(index, index >= inputs ? this.sideConfig.get(dir).get(index).nextOutput(left) : this.sideConfig.get(dir).get(index).nextInput(left));

        onContentsChanged();
    }

    public OutputSetting getOutputSetting(int slot) {
        if (outputSettings == null) return null;
        return outputSettings.get(slot);
    }

    public SideConfigSetting getSideConfig(int slot, Direction direction) {
        if (sideConfig == null) return null;
        return sideConfig.get(direction).get(slot);
    }

    protected void onContentsChanged() {
    }
}