package com.nred.nuclearcraft.block_entity.processor.info;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.menu.slot.ProcessorSlot;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.util.CollectionHelper;
import com.nred.nuclearcraft.util.ContainerInfoHelper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.processor_power_multiplier;
import static com.nred.nuclearcraft.config.NCConfig.processor_time_multiplier;

public abstract class ProcessorMenuInfo<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends BlockEntityMenuInfo<TILE> {
    public final String recipeHandlerName;

    public final int itemInputSize;
    public final int fluidInputSize;
    public final int itemOutputSize;
    public final int fluidOutputSize;

    public final int[] itemInputSlots;
    public final int[] itemOutputSlots;

    public final int[] fluidInputTanks;
    public final int[] fluidOutputTanks;

    public int inputTankCapacity;
    public int outputTankCapacity;

    private final Supplier<Integer> defaultProcessTime;
    private final Supplier<Integer> defaultProcessPower;

    public final boolean isGenerator;

    public final boolean consumesInputs;
    public final boolean losesProgress;

    public final String ccComponentName;

    public final int guiWidth;
    public final int guiHeight;

    public final List<int[]> itemInputGuiXYWH;
    public final List<int[]> fluidInputGuiXYWH;
    public final List<int[]> itemOutputGuiXYWH;
    public final List<int[]> fluidOutputGuiXYWH;

    public final List<int[]> itemInputStackXY;
    public final List<int[]> itemOutputStackXY;

    public final int[] itemInputSorptionButtonID;
    public final int[] fluidInputSorptionButtonID;
    public final int[] itemOutputSorptionButtonID;
    public final int[] fluidOutputSorptionButtonID;

    public final int playerGuiX;
    public final int playerGuiY;

    public final int progressBarGuiX;
    public final int progressBarGuiY;
    public final int progressBarGuiW;
    public final int progressBarGuiH;
    public final int progressBarGuiU;
    public final int progressBarGuiV;

    public final int energyBarGuiX;
    public final int energyBarGuiY;
    public final int energyBarGuiW;
    public final int energyBarGuiH;
    public final int energyBarGuiU;
    public final int energyBarGuiV;

    public final int machineConfigGuiX;
    public final int machineConfigGuiY;

    public final int redstoneControlGuiX;
    public final int redstoneControlGuiY;

    public double maxBaseProcessTime = 1D;
    public double maxBaseProcessPower = 0D;

    protected ProcessorMenuInfo(ProcessorContainerInfoBuilder<TILE, PACKET, INFO, ?> builder) {
        super(builder.name, builder.tileClass, builder.menuFunction);

        this.recipeHandlerName = builder.recipeHandlerName;

        itemInputSize = builder.itemInputGuiXYWH.size();
        fluidInputSize = builder.fluidInputGuiXYWH.size();
        itemOutputSize = builder.itemOutputGuiXYWH.size();
        fluidOutputSize = builder.fluidOutputGuiXYWH.size();

        itemInputSlots = CollectionHelper.increasingArray(itemInputSize);
        itemOutputSlots = CollectionHelper.increasingArray(itemInputSize, itemOutputSize);

        fluidInputTanks = CollectionHelper.increasingArray(fluidInputSize);
        fluidOutputTanks = CollectionHelper.increasingArray(fluidInputSize, fluidOutputSize);

        this.inputTankCapacity = builder.inputTankCapacity;
        this.outputTankCapacity = builder.outputTankCapacity;

        this.defaultProcessTime = builder.defaultProcessTime;
        this.defaultProcessPower = builder.defaultProcessPower;

        this.isGenerator = builder.isGenerator;

        this.consumesInputs = builder.consumesInputs;
        this.losesProgress = builder.losesProgress;

        this.ccComponentName = builder.ccComponentName;

        guiWidth = builder.guiWH[0];
        guiHeight = builder.guiWH[1];

        this.itemInputGuiXYWH = builder.itemInputGuiXYWH;
        this.fluidInputGuiXYWH = builder.fluidInputGuiXYWH;
        this.itemOutputGuiXYWH = builder.itemOutputGuiXYWH;
        this.fluidOutputGuiXYWH = builder.fluidOutputGuiXYWH;

        itemInputStackXY = ContainerInfoHelper.stackXYList(itemInputGuiXYWH);
        itemOutputStackXY = ContainerInfoHelper.stackXYList(itemOutputGuiXYWH);

        itemInputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize);
        fluidInputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize, fluidInputSize);
        itemOutputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize + fluidInputSize, itemOutputSize);
        fluidOutputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize + fluidInputSize + itemOutputSize, fluidOutputSize);

        playerGuiX = builder.playerGuiXY[0];
        playerGuiY = builder.playerGuiXY[1];

        progressBarGuiX = builder.progressBarGuiXYWHUV[0];
        progressBarGuiY = builder.progressBarGuiXYWHUV[1];
        progressBarGuiW = builder.progressBarGuiXYWHUV[2];
        progressBarGuiH = builder.progressBarGuiXYWHUV[3];
        progressBarGuiU = builder.progressBarGuiXYWHUV[4];
        progressBarGuiV = builder.progressBarGuiXYWHUV[5];

        energyBarGuiX = builder.energyBarGuiXYWHUV[0];
        energyBarGuiY = builder.energyBarGuiXYWHUV[1];
        energyBarGuiW = builder.energyBarGuiXYWHUV[2];
        energyBarGuiH = builder.energyBarGuiXYWHUV[3];
        energyBarGuiU = builder.energyBarGuiXYWHUV[4];
        energyBarGuiV = builder.energyBarGuiXYWHUV[5];

        machineConfigGuiX = builder.machineConfigGuiXY[0];
        machineConfigGuiY = builder.machineConfigGuiXY[1];

        redstoneControlGuiX = builder.redstoneControlGuiXY[0];
        redstoneControlGuiY = builder.redstoneControlGuiXY[1];
    }

    public BasicRecipeHandler<ProcessorRecipe> getRecipeHandler() {
        return NCRecipes.getHandler(recipeHandlerName);
    }

    public double getDefaultProcessTime() {
        return processor_time_multiplier * defaultProcessTime.get();
    }

    public double getDefaultProcessPower() {
        return processor_power_multiplier * defaultProcessPower.get();
    }

    public int getInventorySize() {
        return itemInputSize + itemOutputSize;
    }

    public int getCombinedInventorySize() {
        return 36 + getInventorySize();
    }

    public int getTankCount() {
        return fluidInputSize + fluidOutputSize;
    }

    public EnergyConnection defaultEnergyConnection() {
        return maxBaseProcessPower <= 0 ? EnergyConnection.NON : (isGenerator ? EnergyConnection.OUT : EnergyConnection.IN);
    }

    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        return NonNullList.withSize(getInventorySize(), ItemStack.EMPTY);
    }

    public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
        return NonNullList.withSize(consumesInputs ? itemInputSize : 0, ItemStack.EMPTY);
    }

    public @Nonnull List<Tank> getConsumedTanks() {
        @Nonnull List<Tank> consumedTanks = new ArrayList<>();
        if (consumesInputs) {
            for (int i = 0; i < fluidInputSize; ++i) {
                consumedTanks.add(new Tank(inputTankCapacity, new ObjectOpenHashSet<>()));
            }
        }
        return consumedTanks;
    }

    public int getMachineConfigButtonID() {
        return getTankCount();
    }

    public int getRedstoneControlButtonID() {
        return getTankCount() + 1;
    }

    public List<ItemSorption> defaultItemSorptions() {
        List<ItemSorption> itemSorptions = new ArrayList<>();
        for (int i = 0; i < itemInputSize; ++i) {
            itemSorptions.add(ItemSorption.IN);
        }
        for (int i = 0; i < itemOutputSize; ++i) {
            itemSorptions.add(ItemSorption.OUT);
        }
        return itemSorptions;
    }

    public List<ItemSorption> nonItemSorptions() {
        List<ItemSorption> itemSorptions = new ArrayList<>();
        for (int i = 0; i < getInventorySize(); ++i) {
            itemSorptions.add(ItemSorption.NON);
        }
        return itemSorptions;
    }

    public IntList defaultTankCapacities() {
        IntList tankCapacities = new IntArrayList();
        for (int i = 0; i < fluidInputSize; ++i) {
            tankCapacities.add(inputTankCapacity);
        }
        for (int i = 0; i < fluidOutputSize; ++i) {
            tankCapacities.add(outputTankCapacity);
        }
        return tankCapacities;
    }

    public List<TankSorption> defaultTankSorptions() {
        List<TankSorption> tankSorptions = new ArrayList<>();
        for (int i = 0; i < fluidInputSize; ++i) {
            tankSorptions.add(TankSorption.IN);
        }
        for (int i = 0; i < fluidOutputSize; ++i) {
            tankSorptions.add(TankSorption.OUT);
        }
        return tankSorptions;
    }

    public List<TankSorption> nonTankSorptions() {
        List<TankSorption> tankSorptions = new ArrayList<>();
        for (int i = 0; i < getTankCount(); ++i) {
            tankSorptions.add(TankSorption.NON);
        }
        return tankSorptions;
    }

    public void addPlayerSlots(Consumer<Slot> addSlotToContainer, Player player) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer.accept(new ProcessorSlot(player.getInventory(), j + 9 * i + 9, playerGuiX + 18 * j, playerGuiY + 18 * i));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer.accept(new ProcessorSlot(player.getInventory(), i, playerGuiX + 18 * i, 58 + playerGuiY));
        }
    }

    public long getEnergyCapacity(double speedMultiplier, double powerMultiplier) {
        return (long) (Math.ceil(maxBaseProcessTime / speedMultiplier) * Math.ceil(maxBaseProcessPower * powerMultiplier));
    }
}