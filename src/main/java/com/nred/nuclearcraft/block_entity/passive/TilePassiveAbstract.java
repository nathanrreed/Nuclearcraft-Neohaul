package com.nred.nuclearcraft.block_entity.passive;

import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.energyFluid.TileEnergyFluidSidedInventory;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.IngredientSorption;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.Arrays;
import java.util.Collections;

import static com.nred.nuclearcraft.config.NCConfig.machine_update_rate;
import static com.nred.nuclearcraft.config.NCConfig.passive_push;


public abstract class TilePassiveAbstract extends TileEnergyFluidSidedInventory implements ITilePassive {

    protected int tickCount;

    public boolean isActive;
    public boolean energyBool, itemBool, fluidBool;

    public final double energyRate, itemRate, fluidRate;
    public double energyBuffer, itemBuffer, fluidBuffer;

    public final Ingredient itemType;
    public final FluidIngredient fluidType;

    private static final Ingredient ITEM_BACKUP = Ingredient.of(Items.STICK);
    private static final FluidIngredient FLUID_BACKUP = FluidIngredient.of(Fluids.WATER);

    public TilePassiveAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, double energyRate) {
        this(type, pos, blockState, name, ITEM_BACKUP, 0, energyRate, FLUID_BACKUP, 0);
    }

    public TilePassiveAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, Ingredient itemType, double itemRate) {
        this(type, pos, blockState, name, itemType, itemRate, 0, FLUID_BACKUP, 0);
    }

    public TilePassiveAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, FluidIngredient fluidType, double fluidRate) {
        this(type, pos, blockState, name, ITEM_BACKUP, 0, 0, fluidType, fluidRate);
    }

    public TilePassiveAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, Ingredient itemType, double itemRate, double energyRate) {
        this(type, pos, blockState, name, itemType, itemRate, energyRate, FLUID_BACKUP, 0);
    }

    public TilePassiveAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, double energyRate, FluidIngredient fluidType, double fluidRate) {
        this(type, pos, blockState, name, ITEM_BACKUP, 0, energyRate, fluidType, fluidRate);
    }

    public TilePassiveAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, Ingredient itemType, double itemRate, FluidIngredient fluidType, double fluidRate) {
        this(type, pos, blockState, name, itemType, itemRate, 0, fluidType, fluidRate);
    }

    protected TilePassiveAbstract(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, Ingredient itemType, double itemRate, double energyRate, FluidIngredient fluidType, double fluidRate) {
        super(type, pos, blockState, name, 1, itemRate > 0D ? ITileInventory.inventoryConnectionAll(ItemSorption.OUT) : itemRate < 0D ? ITileInventory.inventoryConnectionAll(ItemSorption.IN) : ITileInventory.inventoryConnectionAll(ItemSorption.NON), energyRate == 0D ? 1 : Mth.ceil(machine_update_rate * Math.abs(energyRate)), energyRate == 0D ? 0 : Mth.ceil(machine_update_rate * Math.abs(energyRate)), energyRate > 0D ? ITileEnergy.energyConnectionAll(EnergyConnection.OUT) : energyRate < 0D ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON), fluidRate == 0D ? 1 : Mth.ceil(6 * machine_update_rate * Math.abs(fluidRate)), Collections.singleton(BuiltInRegistries.FLUID.getKey(Arrays.stream(fluidType.getStacks()).findFirst().orElse(FluidStack.EMPTY).getFluid())), fluidRate > 0D ? ITileFluid.fluidConnectionAll(TankSorption.OUT) : fluidRate < 0D ? ITileFluid.fluidConnectionAll(TankSorption.IN) : ITileFluid.fluidConnectionAll(TankSorption.NON));
        this.energyRate = energyRate;
        this.itemRate = itemRate;
        this.itemType = itemType;
        this.fluidRate = fluidRate;
        this.fluidType = fluidType;
    }

    @Override
    public void update() {
        if (!level.isClientSide) {
            boolean wasActive = isActive, shouldUpdate = false;
            energyBool = changeEnergy(false);
            itemBool = changeStack(false);
            fluidBool = changeFluid(false);
            isActive = isRunning(energyBool, itemBool, fluidBool);
            if (wasActive != isActive) {
                shouldUpdate = true;
                setActivity(isActive);
            }
            if (tickCount == 0) {
                if (passive_push) {
                    if (itemRate > 0D) {
                        pushStacks();
                    }
                    if (fluidRate > 0D) {
                        pushFluid();
                    }
                }
                if (energyRate > 0D) {
                    pushEnergy();
                }
            }

            tickCount();

            if (shouldUpdate) {
                setChanged();
            }
        }
    }

    public void tickCount() {
        ++tickCount;
        tickCount %= machine_update_rate;
    }

    protected boolean changeEnergy(boolean simulateChange) {
        if (energyRate == 0) {
            return simulateChange;
        }

        energyBuffer += energyRate;
        long energyChange = (long) energyBuffer;
        energyBuffer -= energyChange;

        if (energyRate > 0 && getEnergyStorage().getEnergyStoredLong() >= getEnergyStorage().getMaxEnergyStoredLong()) {
            return false;
        }
        if (energyRate < 0 && getEnergyStorage().getEnergyStoredLong() < Math.abs(energyChange)) {
            return false;
        }

        if (!simulateChange) {
            if (energyChange != 0 && changeStack(true) && changeFluid(true)) {
                getEnergyStorage().changeEnergyStored(energyChange);
            }
        }
        return true;
    }

    protected boolean changeStack(boolean simulateChange) {
        if (itemRate == 0) {
            return simulateChange;
        }

        itemBuffer += itemRate;
        int itemChange = NCMath.toInt(itemBuffer);
        itemBuffer -= itemChange;

        if (!simulateChange && !RecipeHelper.matchIngredient(new SizedChanceItemIngredient(itemType, 1), getInventoryStacks().get(0), IngredientSorption.NEUTRAL).matches()) {
            getInventoryStacks().set(0, ItemStack.EMPTY);
        }

        if (itemRate > 0) {
            if (getInventoryStacks().get(0).getCount() + itemChange > getMaxStackSize()) {
                return false;
            }
            if (!simulateChange && changeEnergy(true) && changeFluid(true)) {
                if (getInventoryStacks().get(0).isEmpty()) {
                    getInventoryStacks().set(0, Arrays.stream(itemType.getItems()).findFirst().orElse(ItemStack.EMPTY).copy());
                } else {
                    getInventoryStacks().get(0).grow(itemChange);
                }
            }
        } else {
            if (getInventoryStacks().get(0).getCount() < Math.abs(itemChange)) {
                return false;
            }
            if (!simulateChange && changeEnergy(true) && changeFluid(true)) {
                if (getInventoryStacks().get(0).getCount() > Math.abs(itemChange)) {
                    getInventoryStacks().get(0).grow(itemChange);
                } else if (getInventoryStacks().get(0).getCount() == Math.abs(itemChange)) {
                    getInventoryStacks().set(0, ItemStack.EMPTY);
                }
            }
        }
        return true;
    }

    protected boolean changeFluid(boolean simulateChange) {
        if (fluidRate == 0) {
            return simulateChange;
        }

        fluidBuffer += fluidRate;
        int fluidChange = NCMath.toInt(fluidBuffer);
        fluidBuffer -= fluidChange;

        if (fluidRate > 0 && getTanks().get(0).getFluidAmount() >= getTanks().get(0).getCapacity()) {
            return false;
        }
        if (fluidRate < 0 && getTanks().get(0).getFluidAmount() < Math.abs(fluidChange)) {
            return false;
        }

        if (!simulateChange) {
            if (changeEnergy(true) && changeStack(true)) {
                if (fluidRate > 0) {
                    getTanks().get(0).changeFluidStored(Arrays.stream(fluidType.getStacks()).findFirst().orElse(FluidStack.EMPTY).getFluid(), fluidChange);
                } else {
                    getTanks().get(0).changeFluidAmount(fluidChange);
                }
            }
        }
        return true;
    }

    protected boolean isRunning(boolean energy, boolean stack, boolean fluid) {
        if (energyRate == 0 && itemRate == 0 && fluidRate == 0) {
            return true;
        }
        if (energyRate >= 0) {
            if (itemRate >= 0) {
                if (fluidRate >= 0) {
                    return energy || stack || fluid;
                } else {
                    return fluid;
                }
            } else {
                if (fluidRate >= 0) {
                    return stack;
                } else {
                    return stack && fluid;
                }
            }
        } else {
            if (itemRate >= 0) {
                if (fluidRate >= 0) {
                    return energy;
                } else {
                    return energy && fluid;
                }
            } else {
                if (fluidRate >= 0) {
                    return energy && stack;
                } else {
                    return energy && stack && fluid;
                }
            }
        }
    }

    @Override
    public double getEnergyRate() {
        return energyRate;
    }

    @Override
    public double getItemRate() {
        return itemRate;
    }

    @Override
    public double getFluidRate() {
        return fluidRate;
    }

    @Override
    public boolean canPushEnergyTo() {
        return energyRate < 0;
    }

    @Override
    public boolean canPushItemsTo() {
        return itemRate < 0;
    }

    @Override
    public boolean canPushFluidsTo() {
        return fluidRate < 0;
    }

    // ITileInventory

    @Override
    public int getMaxStackSize() {
        return itemRate == 0 ? 1 : Mth.ceil(6 * machine_update_rate * Math.abs(itemRate));
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return itemRate < 0 && super.canPlaceItemThroughFace(slot, stack, side) && RecipeHelper.matchIngredient(new SizedChanceItemIngredient(itemType, 1), stack, IngredientSorption.NEUTRAL).matches();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return itemRate > 0 && super.canTakeItemThroughFace(slot, stack, side);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putBoolean("isRunning", isActive);
        nbt.putBoolean("energyBool", energyBool);
        nbt.putBoolean("itemBool", itemBool);
        nbt.putBoolean("fluidBool", fluidBool);
        nbt.putDouble("energyBuffer", energyBuffer);
        nbt.putDouble("itemBuffer", itemBuffer);
        nbt.putDouble("fluidBuffer", fluidBuffer);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        isActive = nbt.getBoolean("isRunning");
        energyBool = nbt.getBoolean("energyBool");
        itemBool = nbt.getBoolean("itemBool");
        fluidBool = nbt.getBoolean("fluidBool");
        energyBuffer = nbt.getDouble("energyBuffer");
        itemBuffer = nbt.getDouble("itemBuffer");
        fluidBuffer = nbt.getDouble("fluidBuffer");
    }
}