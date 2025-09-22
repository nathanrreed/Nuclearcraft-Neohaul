package com.nred.nuclearcraft.helpers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.HashSet;
import java.util.Set;


public class Tank extends FluidTank {
    public enum IOState {
        INPUT, OUTPUT
    }

    protected Set<String> allowedFluids;
    public IOState ioState;

    public Tank(int capacity, Set<String> allowedFluids, IOState ioState) {
        super(capacity);
        setAllowedFluids(allowedFluids);
        this.ioState = ioState;
    }

    // FluidTank

//	@Override
//	public boolean canFillFluidType(FluidStack fluidIn) {
//		return fluidIn != null && canFillFluidType(fluidIn.getFluid());
//	}

    public boolean canFillFluidType(Fluid fluidIn) {
        return fluidIn != null && (allowedFluids == null || allowedFluids.contains(fluidIn.getFluidType().getDescriptionId()));
    }

    public void setAllowedFluids(Set<String> allowedFluids) {
        this.allowedFluids = allowedFluids;
    }

    // Tank Methods

    public void changeFluidStored(Fluid fluidIn, int amount) {
        amount += getFluidAmount();
        if (fluidIn == null || amount <= 0) {
            this.fluid = FluidStack.EMPTY;
            return;
        }
        this.fluid = new FluidStack(fluidIn, Math.min(amount, getCapacity()));
    }

    public void changeFluidAmount(int amount) {
        amount += getFluidAmount();
        if (fluid.isEmpty() || amount <= 0) {
            fluid = FluidStack.EMPTY;
            return;
        }
        fluid = fluid.copyWithAmount(Math.min(amount, getCapacity()));
    }

    public void setFluidStored(FluidStack stack) {
        if (stack == null || stack.getAmount() <= 0) {
            fluid = FluidStack.EMPTY;
            return;
        }
        if (stack.getAmount() > getCapacity()) {
            stack.setAmount(getCapacity());
        }
        fluid = stack;
    }

    /**
     * Ignores fluid capacity!
     */
    public void setFluidAmount(int amount) {
        if (amount <= 0) {
            fluid = FluidStack.EMPTY;
        }
        if (fluid.isEmpty()) {
            return;
        }
        fluid.setAmount(amount);
    }

    /**
     * Ignores fluid amount!
     */
    public void setTankCapacity(int newCapacity) {
        setCapacity(Math.max(0, newCapacity));
    }

    public void clampTankAmount() {
        if (isFull()) {
            setFluidAmount(getCapacity());
        }
        if (getFluidAmount() <= 0) {
            fluid = FluidStack.EMPTY;
        }
    }

    public boolean isFull() {
        return getFluidAmount() >= getCapacity();
    }

    public boolean isEmpty() {
        return getFluidAmount() == 0;
    }

    public void mergeTank(Tank other) {
        if (fluid.isEmpty()) {
            fluid = other.fluid;
        } else if (!FluidStack.isSameFluidSameComponents(fluid, other.getFluid())) {
            return;
        }
        setFluidAmount(getFluidAmount() + other.getFluidAmount());
        setTankCapacity(getCapacity() + other.getCapacity());
        other.setFluid(FluidStack.EMPTY);
    }

//    public IFluidTankProperties getFluidTankProperties() {
//        return new FluidTankPropertiesWrapper(this);
//    }

    public String getFluidName() {
        return fluid.isEmpty() ? "null" : fluid.getFluid().getFluidType().getDescriptionId();
    }

    public String getFluidLocalizedName() {
        return fluid.isEmpty() ? "" : fluid.getFluidType().getDescription().getString();
    }

    public double getFluidAmountFraction() {
        return (double) getFluidAmount() / (double) getCapacity();
    }

    @Override
    public String toString() {
        return "Tank[" + getFluidName() + " x " + getFluidAmount() + "/" + getCapacity() + "]";
    }

    // NBT

    public final CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt, String name) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("capacity", getCapacity());
        writeToNBT(lookupProvider, tag);
        tag.putShort("ioState", (short) ioState.ordinal());

        nbt.put(name, tag);
        return nbt;
    }

    public final Tank readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            setCapacity(tag.getInt("capacity"));
            setCapacity(tag.getInt("capacity"));
            ioState = IOState.values()[tag.getShort("ioState")];
            readFromNBT(lookupProvider, tag);
        }
        return this;
    }

    public static Tank of(HolderLookup.Provider lookupProvider, CompoundTag nbt, String name) {
        return new Tank(0, new HashSet<>(), IOState.INPUT).readFromNBT(lookupProvider, nbt, name);
    }

    // Packets TODO REMOVE
//
//    public void readInfo(TankInfo info) {
//        setFluid(info.name.equals("null") ? FluidStack.EMPTY : new FluidStack(FluidRegistry.getFluid(info.name), info.amount));
//    }
//
//    public static class TankInfo {
//
//        public final String name;
//        public final int amount;
//
//        public TankInfo(String name, int amount) {
//            this.name = name;
//            this.amount = amount;
//        }
//
//        public TankInfo(Tank tank) {
//            this(tank.getFluidName(), tank.getFluidAmount());
//        }
//
//        public static List<TankInfo> getInfoList(List<Tank> tanks) {
//            List<TankInfo> tankInfos = new ArrayList<>();
//            for (Tank tank : tanks) {
//                tankInfos.add(new TankInfo(tank));
//            }
//            return tankInfos;
//        }
//
//        public static void readInfoList(List<TankInfo> tankInfos, List<Tank> tanks) {
//            for (int i = 0; i < tanks.size(); ++i) {
//                tanks.get(i).readInfo(tankInfos.get(i));
//            }
//        }
//    }
}
