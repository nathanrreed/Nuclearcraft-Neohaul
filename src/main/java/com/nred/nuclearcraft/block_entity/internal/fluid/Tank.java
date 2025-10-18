package com.nred.nuclearcraft.block_entity.internal.fluid;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tank extends FluidTank {
    protected Set<ResourceLocation> allowedFluids; // TODO replace with FluidTank validator

    public Tank(int capacity, Set<ResourceLocation> allowedFluids) {
        super(capacity);
        setAllowedFluids(allowedFluids);
    }

    public static Tank of(HolderLookup.Provider lookupProvider, CompoundTag nbt, String name) {
        return new Tank(0, new HashSet<>()).readFromNBT(nbt, lookupProvider, name);
    }

    // FluidTank

    public boolean isFluidValid(FluidStack fluidIn) {
        return !fluidIn.isEmpty() && isFluidValid(fluidIn.getFluid());
    }

    public boolean isFluidValid(Fluid fluidIn) {
        return fluidIn != null && (allowedFluids == null || allowedFluids.contains(BuiltInRegistries.FLUID.getKey(fluidIn)));
    }

    public void setAllowedFluids(Set<ResourceLocation> allowedFluids) {
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
        fluid = new FluidStack(fluid.getFluid(), Math.min(amount, getCapacity()));
    }

    public void setFluidStored(FluidStack stack) {
        if (stack == null || stack.getAmount() <= 0) {
            fluid = FluidStack.EMPTY;
            return;
        }
        if (stack.getAmount() > getCapacity()) {
            stack.setAmount(getCapacity());
        }
        fluid = stack.copy();
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
        other.setFluidStored(FluidStack.EMPTY);
    }

//    public IFluidTankProperties getFluidTankProperties() {
//        return new FluidTankPropertiesWrapper(this);
//    }

    public ResourceLocation getFluidId() {
        return BuiltInRegistries.FLUID.getKey(fluid.getFluid());
    }

    public Component getFluidName() {
        return fluid.isEmpty() ? Component.empty() : Component.translatable(fluid.getDescriptionId());
    }

    public double getFluidAmountFraction() {
        return (double) getFluidAmount() / (double) getCapacity();
    }

    @Override
    public String toString() {
        return "Tank[" + getFluidId() + " x " + getFluidAmount() + "/" + getCapacity() + "]";
    }

    // NBT

    public final CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider registries, String name) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("capacity", getCapacity());
        writeToNBT(registries, tag);
        nbt.put(name, tag);
        return nbt;
    }

    public final Tank readFromNBT(CompoundTag nbt, HolderLookup.Provider registries, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            setCapacity(tag.getInt("capacity"));
            readFromNBT(registries, tag);
        }
        return this;
    }

    // Packets

    public void readInfo(TankInfo info) {
        setFluid(new FluidStack(BuiltInRegistries.FLUID.get(info.location), info.amount));
    }

    public record TankInfo(ResourceLocation location, int amount) {
        public TankInfo(Tank tank) {
            this(tank.getFluidId(), tank.getFluidAmount());
        }

        public static List<TankInfo> getInfoList(List<Tank> tanks) {
            List<TankInfo> tankInfos = new ArrayList<>();
            for (Tank tank : tanks) {
                tankInfos.add(new TankInfo(tank));
            }
            return tankInfos;
        }

        public static void readInfoList(List<TankInfo> tankInfos, List<Tank> tanks) {
            for (int i = 0; i < tanks.size(); ++i) {
                tanks.get(i).readInfo(tankInfos.get(i));
            }
        }
    }
}