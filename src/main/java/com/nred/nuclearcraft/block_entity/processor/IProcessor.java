package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorContainerInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankOutputSetting;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.handler.AbstractRecipeHandler;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.nred.nuclearcraft.config.Config2.smart_processor_input;

public interface IProcessor<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends ITickable, ITileInventory, ITileFluid, ITileGui<TILE, PACKET, INFO> {

    BasicRecipeHandler<? extends BasicRecipe> getRecipeHandler();

    RecipeInfo<? extends BasicRecipe> getRecipeInfo();

    void setRecipeInfo(RecipeInfo<? extends BasicRecipe> recipeInfo);

    default boolean setRecipeStats() {
        RecipeInfo<? extends BasicRecipe> recipeInfo = getRecipeInfo();
        setRecipeStats(recipeInfo == null ? null : recipeInfo.recipe);
        return recipeInfo != null;
    }

    default void setRecipeStats(@Nullable BasicRecipe recipe) {
        INFO info = getContainerInfo();
        if (recipe == null) {
            setBaseProcessTime(info.defaultProcessTime);
            setBaseProcessPower(info.defaultProcessPower);
        } else {
            setBaseProcessTime(recipe.getBaseProcessTime(info.defaultProcessTime));
            setBaseProcessPower(recipe.getBaseProcessPower(info.defaultProcessPower));
        }
    }

    @Nonnull
    NonNullList<ItemStack> getConsumedStacks();

    @Nonnull
    List<Tank> getConsumedTanks();

    default List<ItemStack> getItemInputs(boolean consumed) {
        return consumed ? getConsumedStacks() : getInventoryStacks().subList(0, getContainerInfo().itemInputSize);
    }

    default List<Tank> getFluidInputs(boolean consumed) {
        return consumed ? getConsumedTanks() : getTanks().subList(0, getContainerInfo().fluidInputSize);
    }

    default List<ItemStack> getItemOutputs() {
        int[] slots = getContainerInfo().itemOutputSlots;
        return slots.length == 0 ? Collections.emptyList() : getInventoryStacks().subList(slots[0], slots[0] + slots.length);
    }

    default List<Tank> getFluidOutputs() {
        int[] tanks = getContainerInfo().fluidOutputTanks;
        return tanks.length == 0 ? Collections.emptyList() : getTanks().subList(tanks[0], tanks[0] + tanks.length);
    }

    default List<SizedIngredient> getItemIngredients() {
        return getRecipeInfo().recipe.getItemIngredients();
    }

    default List<SizedFluidIngredient> getFluidIngredients() {
        return getRecipeInfo().recipe.getFluidIngredients();
    }

    default List<SizedIngredient> getItemProducts() {
        return getRecipeInfo().recipe.getItemProducts();
    }

    default List<SizedFluidIngredient> getFluidProducts() {
        return getRecipeInfo().recipe.getFluidProducts();
    }

    default long getEnergyCapacity() {
        return getContainerInfo().getEnergyCapacity(getSpeedMultiplier(), getPowerMultiplier());
    }

    double getBaseProcessTime();

    void setBaseProcessTime(double baseProcessTime);

    double getBaseProcessPower();

    void setBaseProcessPower(double baseProcessPower);

    double getCurrentTime();

    void setCurrentTime(double time);

    double getResetTime();

    void setResetTime(double resetTime);

    boolean getIsProcessing();

    void setIsProcessing(boolean isProcessing);

    boolean getCanProcessInputs();

    void setCanProcessInputs(boolean canProcessInputs);

    boolean getHasConsumed();

    void setHasConsumed(boolean hasConsumed);

    double getSpeedMultiplier();

    double getPowerMultiplier();

    default long getProcessTime() {
        return Math.max(1, (long) Math.ceil(getBaseProcessTime() / getSpeedMultiplier()));
    }

    default long getProcessPower() {
        return (long) Math.ceil(getBaseProcessPower() * getPowerMultiplier());
    }

    default long getProcessEnergy() {
        return (long) (Math.max(1D, Math.ceil(getBaseProcessTime() / getSpeedMultiplier())) * Math.ceil(getBaseProcessPower() * getPowerMultiplier()));
    }

    default boolean isProcessing() {
        return readyToProcess() && !isHalted();
    }

    default boolean isHalted() {
        return false;
    }

    default boolean readyToProcess() {
        return getCanProcessInputs() && (!getContainerInfo().consumesInputs || getHasConsumed());
    }

    default boolean canProcessInputs() {
        boolean validRecipe = setRecipeStats();
        if (getHasConsumed() && !validRecipe) {
            int itemInputSize = getContainerInfo().itemInputSize;
            List<ItemStack> itemInputs = getItemInputs(true);
            for (int i = 0; i < itemInputSize; ++i) {
                itemInputs.set(i, ItemStack.EMPTY);
            }
            for (Tank tank : getFluidInputs(true)) {
                tank.setFluidStored(FluidStack.EMPTY);
            }
            setHasConsumed(false);
        }

        boolean canProcess = validRecipe && canProduceProducts();
        if (!canProcess) {
            setCurrentTime(Mth.clamp(getCurrentTime(), 0D, getBaseProcessTime() - 1D));
        }
        return canProcess;
    }

    default void process() {
        setCurrentTime(getCurrentTime() + getSpeedMultiplier());
        while (getCurrentTime() >= getBaseProcessTime()) {
            finishProcess();
        }
    }

    default void finishProcess() {
        double oldProcessTime = getBaseProcessTime();
        produceProducts();
        refreshRecipe();
        double newTime = Math.max(0D, getCurrentTime() - oldProcessTime);
        setCurrentTime(newTime);
        setResetTime(newTime);
        refreshActivityOnProduction();
        if (!getCanProcessInputs()) {
            setCurrentTime(0D);
            setResetTime(0D);
            int fluidInputSize = getContainerInfo().fluidInputSize;
            List<Tank> tanks = getTanks();
            for (int i = 0; i < fluidInputSize; ++i) {
                if (getVoidUnusableFluidInput(i)) {
                    tanks.get(i).setFluidStored(FluidStack.EMPTY);
                }
            }
        }
    }

    default boolean hasConsumed() {
        if (!getContainerInfo().consumesInputs) {
            return false;
        }

        if (getTileWorld().isClientSide) {
            return getHasConsumed();
        }

        for (ItemStack stack : getConsumedStacks()) {
            if (!stack.isEmpty()) {
                return true;
            }
        }
        for (Tank tank : getConsumedTanks()) {
            if (!tank.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    default boolean canProduceProducts() {
        INFO info = getContainerInfo();
        int itemOutputSize = info.itemOutputSize;

        List<ItemStack> stacks = getInventoryStacks();
        for (int i = 0; i < itemOutputSize; ++i) {
            int slot = info.itemOutputSlots[i];
            ItemOutputSetting outputSetting = getItemOutputSetting(slot);

            if (outputSetting == ItemOutputSetting.VOID) {
                stacks.set(slot, ItemStack.EMPTY);
                continue;
            }

            SizedIngredient product = getItemProducts().get(i);
            int productMaxStackSize = product.count();
            Optional<ItemStack> productStack = Arrays.stream(product.getItems()).findFirst();

            if (productMaxStackSize <= 0) {
                continue;
            }
            if (productStack.isEmpty()) {
                return false;
            } else {
                ItemStack stack = stacks.get(slot);
                if (!stack.isEmpty()) {
                    if (!ItemStack.isSameItemSameComponents(stack, productStack.get())) {
                        return false;
                    } else if (outputSetting == ItemOutputSetting.DEFAULT && stack.getCount() + productMaxStackSize > getItemProductCapacity(slot, stack)) {
                        return false;
                    }
                }
            }
        }

        int fluidOutputSize = info.fluidOutputSize;

        List<Tank> tanks = getTanks();
        for (int i = 0; i < fluidOutputSize; ++i) {
            int tankIndex = info.fluidOutputTanks[i];
            TankOutputSetting outputSetting = getTankOutputSetting(tankIndex);

            if (outputSetting == TankOutputSetting.VOID) {
                clearTank(tankIndex);
                continue;
            }

            SizedFluidIngredient product = getFluidProducts().get(i);
            int productMaxStackSize = product.amount();
            Optional<FluidStack> productStack = Arrays.stream(product.getFluids()).findFirst();

            if (productMaxStackSize <= 0) {
                continue;
            }
            if (productStack.isEmpty()) {
                return false;
            } else {
                Tank tank = tanks.get(tankIndex);
                if (!tank.isEmpty()) {
                    if (!FluidStack.isSameFluidSameComponents(tank.getFluid(), productStack.get())) {
                        return false;
                    } else if (outputSetting == TankOutputSetting.DEFAULT && tank.getFluidAmount() + productMaxStackSize > getFluidProductCapacity(tank, productStack.get())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    default int getItemProductCapacity(int slot, ItemStack stack) {
        return stack.getMaxStackSize();
    }

    default int getFluidProductCapacity(Tank tank, FluidStack stack) {
        return tank.getCapacity();
    }

    default void consumeInputs() {
        RecipeInfo<? extends BasicRecipe> recipeInfo;
        if (getHasConsumed() || (recipeInfo = getRecipeInfo()) == null) {
            return;
        }

        IntList itemInputOrder = recipeInfo.getItemInputOrder();
        if (itemInputOrder == AbstractRecipeHandler.INVALID) {
            return;
        }

        IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
        if (fluidInputOrder == AbstractRecipeHandler.INVALID) {
            return;
        }

        INFO info = getContainerInfo();
        boolean consumesInputs = info.consumesInputs;
        int itemInputSize = info.itemInputSize;
        int fluidInputSize = info.fluidInputSize;

        NonNullList<ItemStack> consumedStacks = getConsumedStacks();
        List<Tank> consumedTanks = getConsumedTanks();

        if (consumesInputs) {
            for (int i = 0; i < itemInputSize; ++i) {
                consumedStacks.set(i, ItemStack.EMPTY);
            }
            for (Tank tank : consumedTanks) {
                tank.setFluidStored(FluidStack.EMPTY);
            }
        }

        List<ItemStack> stacks = getInventoryStacks();
        for (int i = 0; i < itemInputSize; ++i) {
            int slot = info.itemInputSlots[i];
            int itemIngredientStackSize = getItemIngredients().get(itemInputOrder.get(i)).count();
            ItemStack stack = stacks.get(slot);

            if (itemIngredientStackSize > 0) {
                if (consumesInputs) {
                    consumedStacks.set(i, new ItemStack(stack.getItem(), itemIngredientStackSize));
                }
                stack.shrink(itemIngredientStackSize);
            }

            if (stack.getCount() <= 0) {
                stacks.set(slot, ItemStack.EMPTY);
            }
        }

        List<Tank> tanks = getTanks();
        for (int i = 0; i < fluidInputSize; ++i) {
            Tank tank = tanks.get(info.fluidInputTanks[i]);
            int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).amount();
            if (fluidIngredientStackSize > 0) {
                if (consumesInputs) {
                    consumedTanks.get(i).setFluidStored(new FluidStack(tank.getFluid().getFluid(), fluidIngredientStackSize));
                }
                tank.changeFluidAmount(-fluidIngredientStackSize);
            }
            if (tank.getFluidAmount() <= 0) {
                tank.setFluidStored(FluidStack.EMPTY);
            }
        }

        if (consumesInputs) {
            setHasConsumed(true);
        }
    }

    default void produceProducts() {
        INFO info = getContainerInfo();
        boolean consumesInputs = info.consumesInputs;
        int itemInputSize = info.itemInputSize;
        int fluidInputSize = info.fluidInputSize;

        if (consumesInputs) {
            NonNullList<ItemStack> consumedStacks = getConsumedStacks();
            for (int i = 0; i < itemInputSize; ++i) {
                consumedStacks.set(i, ItemStack.EMPTY);
            }
            for (Tank tank : getConsumedTanks()) {
                tank.setFluidStored(FluidStack.EMPTY);
            }
        }

        if ((consumesInputs && !getHasConsumed()) || getRecipeInfo() == null) {
            return;
        }

        if (!consumesInputs) {
            consumeInputs();
        }

        int itemOutputSize = info.itemOutputSize;

        List<ItemStack> stacks = getInventoryStacks();
        for (int i = 0; i < itemOutputSize; ++i) {
            int slot = info.itemOutputSlots[i];
            if (getItemOutputSetting(slot) == ItemOutputSetting.VOID) {
                stacks.set(slot, ItemStack.EMPTY);
                continue;
            }

            SizedIngredient product = getItemProducts().get(i);

            if (product.count() <= 0) {
                continue;
            }

            ItemStack currentStack = stacks.get(slot);
            ItemStack nextStack = Arrays.stream(product.getItems()).findFirst().orElse(ItemStack.EMPTY);

            if (currentStack.isEmpty()) {
                stacks.set(slot, nextStack);
            } else if (product.test(currentStack)) {
                int count = Math.min(getMaxStackSize(), currentStack.getCount() + nextStack.getCount());
                currentStack.setCount(count);
            }
        }

        int fluidOutputSize = info.fluidOutputSize;

        List<Tank> tanks = getTanks();
        for (int i = 0; i < fluidOutputSize; ++i) {
            int tankIndex = info.fluidOutputTanks[i];
            if (getTankOutputSetting(tankIndex) == TankOutputSetting.VOID) {
                clearTank(tankIndex);
                continue;
            }

            SizedFluidIngredient product = getFluidProducts().get(i);

            if (product.amount() <= 0) {
                continue;
            }

            Tank tank = tanks.get(tankIndex);
            FluidStack nextStack = Arrays.stream(product.getFluids()).findFirst().orElse(FluidStack.EMPTY);

            if (tank.isEmpty()) {
                tank.setFluidStored(nextStack);
            } else if (product.test(tank.getFluid())) {
                tank.changeFluidAmount(nextStack.getAmount());
            }
        }

        if (consumesInputs) {
            setHasConsumed(false);
        }

        autoPush();
    }

    // ITickable

    default boolean onTick() {
        boolean wasProcessing = getIsProcessing();
        setIsProcessing(isProcessing());
        boolean shouldUpdate = false;

        if (getIsProcessing()) {
            process();
        } else {
            shouldUpdate = onIdle(wasProcessing);
        }

        if (wasProcessing == getIsProcessing()) {
            onResumeProcessingState();
        } else {
            shouldUpdate = true;
            onChangeProcessingState();
        }

        return shouldUpdate;
    }

    default boolean onIdle(boolean wasProcessing) {
//        getRadiationSource().setRadiationLevel(0D);

        if (getCurrentTime() > 0D) {
            if (getContainerInfo().losesProgress && !isHalted()) {
                loseProgress();
            } else if (!getCanProcessInputs()) {
                setCurrentTime(0D);
                setResetTime(0D);
            }
        }

        if (wasProcessing) {
            return false;
        } else {
            return autoPush();
        }
    }

    default void loseProgress() {
        double newTime = Mth.clamp(getCurrentTime() - 1.5D * getSpeedMultiplier(), 0D, getBaseProcessTime());
        setCurrentTime(newTime);
        if (newTime < getResetTime()) {
            setResetTime(newTime);
        }
    }

    default void onResumeProcessingState() {
        sendTileUpdatePacketToListeners();
    }

    default void onChangeProcessingState() {
        setActivity(getIsProcessing());
        sendTileUpdatePacketToAll();
    }

    default boolean autoPush() {
        HandlerPair[] adjacentHandlers = getAdjacentHandlers();
        if (adjacentHandlers == null) {
            return false;
        }

        List<Direction> dirs = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            if (adjacentHandlers[i] != null) {
                dirs.add(Direction.values()[i]);
            }
        }

        return autoPushInternal(adjacentHandlers, getInventoryStacks(), getTanks(), dirs, dirs.size(), (int) getTileWorld().getGameTime());
    }

    default boolean autoPushInternal(HandlerPair[] adjacentHandlers, NonNullList<ItemStack> stacks, List<Tank> tanks, List<Direction> dirs, int dirCount, int indexOffset) {
        INFO info = getContainerInfo();
        boolean pushed = false;

        for (int i = 0; i < info.itemInputSize; ++i) {
            pushed |= tryPushSlot(adjacentHandlers, stacks, info.itemInputSlots[i], dirs, dirCount, indexOffset);
        }
        for (int i = 0; i < info.fluidInputSize; ++i) {
            pushed |= tryPushTank(adjacentHandlers, tanks, info.fluidInputTanks[i], dirs, dirCount, indexOffset);
        }
        for (int i = 0; i < info.itemOutputSize; ++i) {
            pushed |= tryPushSlot(adjacentHandlers, stacks, info.itemOutputSlots[i], dirs, dirCount, indexOffset);
        }
        for (int i = 0; i < info.fluidOutputSize; ++i) {
            pushed |= tryPushTank(adjacentHandlers, tanks, info.fluidOutputTanks[i], dirs, dirCount, indexOffset);
        }

        return pushed;
    }

    default void onBlockNeighborChanged(BlockState state, Level level, BlockPos pos, BlockPos fromPos) {
        ITileGui.super.onBlockNeighborChanged(state, level, pos, fromPos);
        updateAdjacentHandlers();
    }

    class HandlerPair {
        public final IItemHandler itemHandler;
        public final IFluidHandler fluidHandler;

        public HandlerPair(IItemHandler itemHandler, IFluidHandler fluidHandler) {
            this.itemHandler = itemHandler;
            this.fluidHandler = fluidHandler;
        }
    }

    default @Nullable HandlerPair[] getAdjacentHandlers() {
        return null;
    }

    default void updateAdjacentHandlers() {
    }

    default void refreshAll() {
        refreshDirty();
        setIsProcessing(isProcessing());
        setHasConsumed(hasConsumed());
    }

    default void refreshDirty() {
        refreshRecipe();
        refreshActivity();
    }

    default void refreshRecipe() {
        boolean hasConsumed = getHasConsumed();
        setRecipeInfo(getRecipeHandler().getRecipeInfoFromInputs(getTileWorld(), getItemInputs(hasConsumed), getFluidInputs(hasConsumed)));
        if (getContainerInfo().consumesInputs) {
            consumeInputs();
        }
    }

    default void refreshActivity() {
        setCanProcessInputs(canProcessInputs());
    }

    default void refreshActivityOnProduction() {
        setCanProcessInputs(canProcessInputs());
    }

    // Container

    @Override
    default @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ITileInventory.super.removeItem(slot, amount);
        if (!getTileWorld().isClientSide) {
            INFO info = getContainerInfo();
            if (slot < info.itemInputSize) {
                refreshRecipe();
                refreshActivity();
            } else if (slot < info.itemInputSize + info.itemOutputSize) {
                refreshActivity();
            }
        }
        return stack;
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        ITileInventory.super.setItem(slot, stack);
        if (!getTileWorld().isClientSide) {
            INFO info = getContainerInfo();
            if (slot < info.itemInputSize) {
                refreshRecipe();
                refreshActivity();
            } else if (slot < info.itemInputSize + info.itemOutputSize) {
                refreshActivity();
            }
        }
    }

    @Override
    default boolean canPlaceItem(int slot, ItemStack stack) {
        INFO info = getContainerInfo();
        if (stack.isEmpty() || (slot >= info.itemInputSize && slot < info.itemInputSize + info.itemOutputSize)) {
            return false;
        }

        if (smart_processor_input) {
            return getRecipeHandler().isValidItemInput(stack, slot, getItemInputs(false), getFluidInputs(false), getRecipeInfo());
        } else {
            return getRecipeHandler().isValidItemInput(stack);
        }
    }

    @Override
    default boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return ITileInventory.super.canPlaceItemThroughFace(slot, stack, side) && canPlaceItem(slot, stack);
    }

    @Override
    default void clearAllSlots() {
        ITileInventory.super.clearAllSlots();
        @Nonnull NonNullList<ItemStack> consumedStacks = getConsumedStacks();
        Collections.fill(consumedStacks, ItemStack.EMPTY);
        refreshAll();
    }

    @Override
    default CompoundTag writeInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        ContainerHelper.saveAllItems(nbt, getInventoryStacks(), registries);
        return nbt;
    }

    @Override
    default void readInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        if (nbt.contains("hasConsumed")) {
            ContainerHelper.loadAllItems(nbt, getInventoryStacks(), registries);
        } else {
            ITileInventory.super.readInventory(nbt, registries);
        }
    }

    // ITileFluid

    @Override
    default boolean isFluidValidForTank(int tankNumber, FluidStack stack) {
        INFO info = getContainerInfo();
        if (stack == null || stack.getAmount() <= 0 || (tankNumber >= info.fluidInputSize && tankNumber < info.fluidInputSize + info.fluidOutputSize)) {
            return false;
        }

        if (smart_processor_input) {
            return getRecipeHandler().isValidFluidInput(stack, tankNumber, getFluidInputs(false), getItemInputs(false), getRecipeInfo());
        } else {
            return getRecipeHandler().isValidFluidInput(stack);
        }
    }

    @Override
    default void clearAllTanks() {
        ITileFluid.super.clearAllTanks();
        for (Tank tank : getConsumedTanks()) {
            tank.setFluidStored(FluidStack.EMPTY);
        }
        refreshAll();
    }

    @Override
    default CompoundTag writeTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        ITileFluid.super.writeTanks(nbt, registries);
        @Nonnull List<Tank> consumedTanks = getConsumedTanks();
        for (int i = 0; i < consumedTanks.size(); ++i) {
            consumedTanks.get(i).writeToNBT(nbt, registries, "consumedTanks" + i);
        }
        return nbt;
    }

    @Override
    default void readTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        ITileFluid.super.readTanks(nbt, registries);
        @Nonnull List<Tank> consumedTanks = getConsumedTanks();
        for (int i = 0; i < consumedTanks.size(); ++i) {
            consumedTanks.get(i).readFromNBT(nbt, registries, "consumedTanks" + i);
        }
    }

    // IGui

    @Override
    default void onTileUpdatePacket(PACKET message) {
        setIsProcessing(message.isProcessing);
        setCurrentTime(message.time);
        setBaseProcessTime(message.baseProcessTime);
        Tank.TankInfo.readInfoList(message.tankInfos, getTanks());
    }

    // NBT

    default CompoundTag writeProcessorNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putDouble("time", getCurrentTime());
        nbt.putDouble("resetTime", getResetTime());
        nbt.putBoolean("isProcessing", getIsProcessing());
        nbt.putBoolean("canProcessInputs", getCanProcessInputs());
        nbt.putBoolean("hasConsumed", getHasConsumed());
        return nbt;
    }

    default void readProcessorNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        setCurrentTime(nbt.getDouble("time"));
        setResetTime(nbt.getDouble("resetTime"));
        setIsProcessing(nbt.getBoolean("isProcessing"));
        setCanProcessInputs(nbt.getBoolean("canProcessInputs"));
        setHasConsumed(nbt.getBoolean("hasConsumed"));
    }
}
