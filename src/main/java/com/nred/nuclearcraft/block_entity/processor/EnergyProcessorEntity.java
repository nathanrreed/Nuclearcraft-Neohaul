package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.block_entity.dummy.IInterfaceable;
import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.energyFluid.TileEnergyFluidSidedInventory;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.FluidConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.util.NBTHelper;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class EnergyProcessorEntity<TILE extends EnergyProcessorEntity<TILE, INFO>, INFO extends ProcessorMenuInfo<TILE, EnergyProcessorUpdatePacket, INFO>> extends TileEnergyFluidSidedInventory implements IProcessor<TILE, EnergyProcessorUpdatePacket, INFO>, IInterfaceable {
    protected INFO info;

    protected @Nonnull NonNullList<ItemStack> consumedStacks = null;
    protected @Nonnull List<Tank> consumedTanks = null;

    public double baseProcessTime, baseProcessPower, baseProcessRadiation;

    public double time, resetTime;
    public boolean isProcessing, canProcessInputs, hasConsumed;

    protected BasicRecipeHandler<ProcessorRecipe> recipeHandler;
    protected RecipeInfo<ProcessorRecipe> recipeInfo = null;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    protected final HandlerPair[] adjacentHandlers = new HandlerPair[6];

    public boolean fullHalt = false;

    protected EnergyProcessorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name) {
        this(type, pos, blockState, name, TileInfoHandler.getProcessorContainerInfo(name));
    }

    private EnergyProcessorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, INFO info) {
        this(type, pos, blockState, name, info, ITileInventory.inventoryConnectionAll(info.defaultItemSorptions()), info.getEnergyCapacity(1D, 1D), ITileEnergy.energyConnectionAll(info.defaultEnergyConnection()), info.defaultTankCapacities(), NCRecipes.getValidFluids(info.recipeHandlerName), ITileFluid.fluidConnectionAll(info.defaultTankSorptions()));
    }

    private EnergyProcessorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, INFO info, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<Set<ResourceLocation>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, name, info.getInventorySize(), inventoryConnections, capacity, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
        initFromInfo(info, false);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return info.createMenu(containerId, playerInventory, (TILE) this);
    }

    @Override
    public Component getDisplayName() {
        return info.getDisplayName();
    }

    protected void initFromInfo(INFO info, boolean superConstructor) {
        this.info = info;

        if (superConstructor) {
            long capacity = info.getEnergyCapacity(1D, 1D);
            initTileEnergy(capacity, NCMath.toInt(capacity), ITileEnergy.energyConnectionAll(info.defaultEnergyConnection()));
            initTileEnergyFluid(info.defaultTankCapacities(), NCRecipes.getValidFluids(info.recipeHandlerName), ITileFluid.fluidConnectionAll(info.defaultTankSorptions()));
            initTileEnergyFluidInventory(info.name, info.getInventorySize(), ITileInventory.inventoryConnectionAll(info.defaultItemSorptions()));
        }

        baseProcessTime = info.getDefaultProcessTime();
        baseProcessPower = info.getDefaultProcessPower();

        setInputTanksSeparated(info.fluidInputSize > 1);

        consumedStacks = info.getConsumedStacks();
        consumedTanks = info.getConsumedTanks();

        recipeHandler = info.getRecipeHandler();
    }

    // Ticking

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide()) {
            updateAdjacentHandlers();
            refreshAll();
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            if (onTick()) {
                setChanged();
            }

            if (info.isGenerator) {
                pushEnergy();
            }
        }
    }

    @Override
    public @Nullable HandlerPair[] getAdjacentHandlers() {
        return adjacentHandlers;
    }

    @Override
    public void updateAdjacentHandlers() {
        for (int i = 0; i < 6; ++i) {
            Direction side = Direction.values()[i], opposite = side.getOpposite();
            BlockEntity tile = level.getBlockEntity(worldPosition.relative(side));
            IItemHandler itemHandler = tile == null ? null : level.getCapability(Capabilities.ItemHandler.BLOCK, tile.getBlockPos(), opposite);
            IFluidHandler fluidHandler = tile == null ? null : level.getCapability(Capabilities.FluidHandler.BLOCK, tile.getBlockPos(), opposite);
            adjacentHandlers[i] = itemHandler == null && fluidHandler == null ? null : new HandlerPair(itemHandler, fluidHandler);
        }
    }

    @Override
    public void refreshDirty() {
        IProcessor.super.refreshDirty();
        refreshEnergyCapacity();
    }

    public void refreshEnergyCapacity() {
    }

    // IProcessor

    @Override
    public INFO getContainerInfo() {
        return info;
    }

    @Override
    public BasicRecipeHandler<ProcessorRecipe> getRecipeHandler() {
        return recipeHandler;
    }

    @Override
    public RecipeInfo<ProcessorRecipe> getRecipeInfo() {
        return recipeInfo;
    }

    @Override
    public void setRecipeInfo(RecipeInfo<? extends BasicRecipe> recipeInfo) {
        this.recipeInfo = (RecipeInfo<ProcessorRecipe>) recipeInfo;
    }

    @Override
    public void setRecipeStats(@Nullable BasicRecipe recipe) {
        IProcessor.super.setRecipeStats(recipe);
        baseProcessRadiation = recipe == null ? 0D : ((ProcessorRecipe) recipe).getBaseProcessRadiation();
    }

    @Override
    public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
        return consumedStacks;
    }

    @Override
    public @Nonnull List<Tank> getConsumedTanks() {
        return consumedTanks;
    }

    @Override
    public double getBaseProcessTime() {
        return baseProcessTime;
    }

    @Override
    public void setBaseProcessTime(double baseProcessTime) {
        this.baseProcessTime = baseProcessTime;
    }

    @Override
    public double getBaseProcessPower() {
        return baseProcessPower;
    }

    @Override
    public void setBaseProcessPower(double baseProcessPower) {
        this.baseProcessPower = baseProcessPower;
    }

    @Override
    public double getCurrentTime() {
        return time;
    }

    @Override
    public void setCurrentTime(double time) {
        this.time = time;
    }

    @Override
    public double getResetTime() {
        return resetTime;
    }

    @Override
    public void setResetTime(double resetTime) {
        this.resetTime = resetTime;
    }

    @Override
    public boolean getIsProcessing() {
        return isProcessing;
    }

    @Override
    public void setIsProcessing(boolean isProcessing) {
        this.isProcessing = isProcessing;
    }

    @Override
    public boolean getCanProcessInputs() {
        return canProcessInputs;
    }

    @Override
    public void setCanProcessInputs(boolean canProcessInputs) {
        this.canProcessInputs = canProcessInputs;
    }

    @Override
    public boolean getHasConsumed() {
        return hasConsumed;
    }

    @Override
    public void setHasConsumed(boolean hasConsumed) {
        this.hasConsumed = hasConsumed;
    }

    @Override
    public double getSpeedMultiplier() {
        return 1D;
    }

    @Override
    public double getPowerMultiplier() {
        return 1D;
    }

    @Override
    public boolean isHalted() {
        return fullHalt || (getRedstoneControl() && getIsRedstonePowered());
    }

    @Override
    public boolean readyToProcess() {
        return IProcessor.super.readyToProcess() && (info.isGenerator || hasSufficientEnergy());
    }

    public boolean hasSufficientEnergy() {
        if (time <= resetTime) {
            long processEnergy = getProcessEnergy(), maxEnergy = getMaxEnergyStoredLong();
            if (processEnergy >= maxEnergy) {
                return getEnergyStoredLong() >= maxEnergy;
            } else {
                return getEnergyStoredLong() >= processEnergy;
            }
        } else {
            return getEnergyStoredLong() >= getProcessPower();
        }
    }

    @Override
    public void process() {
        getEnergyStorage().changeEnergyStored(info.isGenerator ? getProcessPower() : -getProcessPower());
//        getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier()); TODO
        IProcessor.super.process();
    }

    @Override
    public void setChanged() {
        refreshDirty();
        super.setChanged();
    }

    // ITileInventory

    @Override
    public boolean hasConfigurableInventoryConnections() {
        return true;
    }

    // ITileFluid

    @Override
    public boolean hasConfigurableFluidConnections() {
        return true;
    }

    // IGui

    @Override
    public Set<Player> getTileUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public EnergyProcessorUpdatePacket getTileUpdatePacket() {
        return new EnergyProcessorUpdatePacket(worldPosition, isProcessing, time, baseProcessTime, getTanks(), baseProcessPower, getEnergyStoredLong());
    }

    @Override
    public void onTileUpdatePacket(EnergyProcessorUpdatePacket message) {
        IProcessor.super.onTileUpdatePacket(message);
        baseProcessPower = message.baseProcessPower;
        getEnergyStorage().setEnergyStored(message.energyStored);
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        CompoundTag nbt;
        if (info.machineConfigGuiX >= 0 && info.machineConfigGuiY >= 0 && info.redstoneControlGuiX >= 0 && info.redstoneControlGuiY >= 0 && (nbt = NBTHelper.getStackNBT(multitool, "ncMultitool")) != null) {
            HolderLookup.Provider registries = level.registryAccess();
            Component displayName = getTileBlockDisplayName();
            if (player.isCrouching()) {
                CompoundTag config = new CompoundTag();
                Direction dir = getFacingHorizontal();
                config.putString("infoName", info.name);
                config.putString("displayName", displayName.getString());
                if (hasConfigurableInventoryConnections()) {
                    config.put("inventoryConnections", writeInventoryConnectionsDirectional(new CompoundTag(), registries, dir));
                    config.put("slotSettings", writeSlotSettings(new CompoundTag(), registries));
                }
                if (hasConfigurableFluidConnections()) {
                    config.put("fluidConnections", writeFluidConnectionsDirectional(new CompoundTag(), registries, dir));
                    config.put("tankSettings", writeTankSettings(new CompoundTag(), registries));
                }
                config.putBoolean("alternateComparator", getAlternateComparator());
                config.putBoolean("redstoneControl", getRedstoneControl());
                player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.save_processor_config", displayName));
                nbt.put("processorConfig", config);
                return true;
            } else if (nbt.contains("processorConfig", 10)) {
                CompoundTag config = nbt.getCompound("processorConfig");
                if (info.name.equals(config.getString("infoName"))) {
                    Direction dir = getFacingHorizontal();
                    if (hasConfigurableInventoryConnections()) {
                        readInventoryConnectionsDirectional(config.getCompound("inventoryConnections"), registries, dir);
                        readSlotSettings(config.getCompound("slotSettings"), registries);
                    }
                    if (hasConfigurableFluidConnections()) {
                        readFluidConnectionsDirectional(config.getCompound("fluidConnections"), registries, dir);
                        readTankSettings(config.getCompound("tankSettings"), registries);
                    }
                    setAlternateComparator(config.getBoolean("alternateComparator"));
                    setRedstoneControl(config.getBoolean("redstoneControl"));
                    player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.load_processor_config", displayName));
                    return true;
                } else {
                    player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.invalid_processor_config", config.getString("displayName"), displayName));
                }
            }
        }
        return IInterfaceable.super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putString("infoName", info.name);
        super.writeAll(nbt, registries);
        writeProcessorNBT(nbt, registries);
        nbt.putBoolean("fullHalt", fullHalt);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        if (info == null && nbt.contains("infoName")) {
            initFromInfo(TileInfoHandler.getProcessorContainerInfo(nbt.getString("infoName")), true);
        }
        super.readAll(nbt, registries);
        readProcessorNBT(nbt, registries);
        fullHalt = nbt.getBoolean("fullHalt");
    }
}