package com.nred.nuclearcraft.block_entity.processor.info;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.MenuFunction;
import com.nred.nuclearcraft.menu.slot.ProcessorSlot;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
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

public abstract class ProcessorMenuInfo<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends TileContainerInfo<TILE> {
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

    public final String ocComponentName;

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

    public final boolean jeiCategoryEnabled;

    public final String jeiCategoryUid;
    public final String jeiTitle;
    public final String jeiTexture;

    public final int jeiBackgroundX;
    public final int jeiBackgroundY;
    public final int jeiBackgroundW;
    public final int jeiBackgroundH;

    public final int jeiTooltipX;
    public final int jeiTooltipY;
    public final int jeiTooltipW;
    public final int jeiTooltipH;

    public final int jeiClickAreaX;
    public final int jeiClickAreaY;
    public final int jeiClickAreaW;
    public final int jeiClickAreaH;

    public double maxBaseProcessTime = 1D;
    public double maxBaseProcessPower = 0D;

    protected ProcessorMenuInfo(String name, Class<TILE> tileClass, MenuFunction<TILE> menuFunction, String recipeHandlerName, int inputTankCapacity, int outputTankCapacity, Supplier<Integer> defaultProcessTime, Supplier<Integer> defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, String ocComponentName, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiCategoryEnabled, String jeiCategoryUid, String jeiTitle, String jeiTexture, int[] jeiBackgroundXYWH, int[] jeiTooltipXYWH, int[] jeiClickAreaXYWH) {
        super(name, tileClass, menuFunction);

        this.recipeHandlerName = recipeHandlerName;

        itemInputSize = itemInputGuiXYWH.size();
        fluidInputSize = fluidInputGuiXYWH.size();
        itemOutputSize = itemOutputGuiXYWH.size();
        fluidOutputSize = fluidOutputGuiXYWH.size();

        itemInputSlots = CollectionHelper.increasingArray(itemInputSize);
        itemOutputSlots = CollectionHelper.increasingArray(itemInputSize, itemOutputSize);

        fluidInputTanks = CollectionHelper.increasingArray(fluidInputSize);
        fluidOutputTanks = CollectionHelper.increasingArray(fluidInputSize, fluidOutputSize);

        this.inputTankCapacity = inputTankCapacity;
        this.outputTankCapacity = outputTankCapacity;

        this.defaultProcessTime = defaultProcessTime;
        this.defaultProcessPower = defaultProcessPower;

        this.isGenerator = isGenerator;

        this.consumesInputs = consumesInputs;
        this.losesProgress = losesProgress;

        this.ocComponentName = ocComponentName;

        guiWidth = guiWH[0];
        guiHeight = guiWH[1];

        this.itemInputGuiXYWH = itemInputGuiXYWH;
        this.fluidInputGuiXYWH = fluidInputGuiXYWH;
        this.itemOutputGuiXYWH = itemOutputGuiXYWH;
        this.fluidOutputGuiXYWH = fluidOutputGuiXYWH;

        itemInputStackXY = ContainerInfoHelper.stackXYList(itemInputGuiXYWH);
        itemOutputStackXY = ContainerInfoHelper.stackXYList(itemOutputGuiXYWH);

        itemInputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize);
        fluidInputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize, fluidInputSize);
        itemOutputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize + fluidInputSize, itemOutputSize);
        fluidOutputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize + fluidInputSize + itemOutputSize, fluidOutputSize);

        playerGuiX = playerGuiXY[0];
        playerGuiY = playerGuiXY[1];

        progressBarGuiX = progressBarGuiXYWHUV[0];
        progressBarGuiY = progressBarGuiXYWHUV[1];
        progressBarGuiW = progressBarGuiXYWHUV[2];
        progressBarGuiH = progressBarGuiXYWHUV[3];
        progressBarGuiU = progressBarGuiXYWHUV[4];
        progressBarGuiV = progressBarGuiXYWHUV[5];

        energyBarGuiX = energyBarGuiXYWHUV[0];
        energyBarGuiY = energyBarGuiXYWHUV[1];
        energyBarGuiW = energyBarGuiXYWHUV[2];
        energyBarGuiH = energyBarGuiXYWHUV[3];
        energyBarGuiU = energyBarGuiXYWHUV[4];
        energyBarGuiV = energyBarGuiXYWHUV[5];

        machineConfigGuiX = machineConfigGuiXY[0];
        machineConfigGuiY = machineConfigGuiXY[1];

        redstoneControlGuiX = redstoneControlGuiXY[0];
        redstoneControlGuiY = redstoneControlGuiXY[1];

        this.jeiCategoryEnabled = jeiCategoryEnabled;

        this.jeiCategoryUid = jeiCategoryUid;
        this.jeiTitle = jeiTitle;
        this.jeiTexture = jeiTexture;

        jeiBackgroundX = jeiBackgroundXYWH[0];
        jeiBackgroundY = jeiBackgroundXYWH[1];
        jeiBackgroundW = jeiBackgroundXYWH[2];
        jeiBackgroundH = jeiBackgroundXYWH[3];

        jeiTooltipX = jeiTooltipXYWH[0];
        jeiTooltipY = jeiTooltipXYWH[1];
        jeiTooltipW = jeiTooltipXYWH[2];
        jeiTooltipH = jeiTooltipXYWH[3];

        jeiClickAreaX = jeiClickAreaXYWH[0];
        jeiClickAreaY = jeiClickAreaXYWH[1];
        jeiClickAreaW = jeiClickAreaXYWH[2];
        jeiClickAreaH = jeiClickAreaXYWH[3];
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

//    public JEIContainerConnection getJEIContainerConnection() { TODO
//        return new JEIContainerConnection(containerClass, guiClass, 0, itemInputSize, getInventorySize(), jeiClickAreaX, jeiClickAreaY, jeiClickAreaW, jeiClickAreaH);
//    }
}