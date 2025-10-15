package com.nred.nuclearcraft.block.fission;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block.fission.IFissionFuelComponent.ModeratorBlockInfo;
import com.nred.nuclearcraft.block.fission.port.FissionHeaterPortEntity;
import com.nred.nuclearcraft.block.fission.port.IFissionPortTarget;
import com.nred.nuclearcraft.block.fission.port.ITileFilteredFluid;
import com.nred.nuclearcraft.block.fluid.ITileFluid;
import com.nred.nuclearcraft.block.info.ProcessorContainerInfoImpl;
import com.nred.nuclearcraft.block.internal.fluid.*;
import com.nred.nuclearcraft.block.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block.inventory.ITileInventory;
import com.nred.nuclearcraft.block.processor.IBasicProcessor;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.menu.ContainerProcessorImpl;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.payload.multiblock.SaltFissionHeaterUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import com.nred.nuclearcraft.util.CCHelper;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class SaltFissionHeaterEntity extends AbstractFissionEntity implements IBasicProcessor<SaltFissionHeaterEntity, SaltFissionHeaterUpdatePacket>, ITileFilteredFluid, IFissionCoolingComponent, IFissionPortTarget<FissionHeaterPortEntity, SaltFissionHeaterEntity> {
    public FissionCoolantHeaterType heaterType;
    public static final Object2ObjectMap<String, ResourceKey<Fluid>> DYN_COOLANT_NAME_MAP = new Object2ObjectOpenHashMap<>();

    protected final ProcessorContainerInfoImpl.BasicProcessorContainerInfo<SaltFissionHeaterEntity, SaltFissionHeaterUpdatePacket> info;

    protected final @Nonnull NonNullList<ItemStack> inventoryStacks;
    protected final @Nonnull NonNullList<ItemStack> consumedStacks;

    protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Collections.emptyList());

    protected final @Nonnull List<Tank> tanks;
    protected final @Nonnull List<Tank> consumedTanks;

    protected final @Nonnull List<Tank> filterTanks;

    protected @Nonnull FluidConnection[] fluidConnections;

    protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
    protected @Nonnull ChemicalTileWrapper[] chemicalSides = ITileFluid.getDefaultChemicalSides(this);

    protected int baseProcessCooling;
    protected PlacementRule<FissionReactor, AbstractFissionEntity> placementRule = FissionPlacement.RULE_MAP.get("");
    public double heatingSpeedMultiplier; // Based on the cluster efficiency, but with heat/cooling taken into account

    public double time, resetTime;
    public boolean isProcessing, canProcessInputs, hasConsumed;
    public boolean isRunningSimulated;

    protected RecipeInfo<BasicRecipe> recipeInfo = null;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public ResourceKey<Fluid> coolantName;

    protected FissionCluster cluster = null;
    protected long heat = 0L;
    protected boolean isInValidPosition = false;

    public long clusterHeatStored, clusterHeatCapacity;

    protected BlockPos masterPortPos = DEFAULT_NON;
    protected FissionHeaterPortEntity masterPort = null;

    public SaltFissionHeaterEntity(final BlockPos position, final BlockState blockState, FissionCoolantHeaterType heaterType) {
        super(FISSION_ENTITY_TYPE.get("coolant_heater").get(), position, blockState);
        this.heaterType = heaterType;
        info = TileInfoHandler.getProcessorContainerInfo("salt_fission_heater");

        inventoryStacks = NonNullList.withSize(0, ItemStack.EMPTY);
        consumedStacks = info.getConsumedStacks();

        tanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, null), new Tank(INGOT_BLOCK_VOLUME, new ObjectOpenHashSet<>()));
        consumedTanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, new ObjectOpenHashSet<>()));

        filterTanks = Lists.newArrayList(new Tank(1000, null), new Tank(1000, new ObjectOpenHashSet<>()));

        fluidConnections = ITileFluid.fluidConnectionAll(info.nonTankSorptions());
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }

    public static class Variant extends SaltFissionHeaterEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionCoolantHeaterType dynamoCoilType) {
            super(position, blockState, dynamoCoilType);
        }
    }

    public static class Standard extends SaltFissionHeaterEntity.Variant {
        public Standard(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.STANDARD);
        }
    }

    public static class Iron extends SaltFissionHeaterEntity.Variant {
        public Iron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.IRON);
        }
    }

    public static class Redstone extends SaltFissionHeaterEntity.Variant {
        public Redstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.REDSTONE);
        }
    }

    public static class Quartz extends SaltFissionHeaterEntity.Variant {
        public Quartz(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.QUARTZ);
        }
    }

    public static class Obsidian extends SaltFissionHeaterEntity.Variant {
        public Obsidian(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.OBSIDIAN);
        }
    }

    public static class NetherBrick extends SaltFissionHeaterEntity.Variant {
        public NetherBrick(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.NETHER_BRICK);
        }
    }

    public static class Glowstone extends SaltFissionHeaterEntity.Variant {
        public Glowstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.GLOWSTONE);
        }
    }

    public static class Lapis extends SaltFissionHeaterEntity.Variant {
        public Lapis(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LAPIS);
        }
    }

    public static class Gold extends SaltFissionHeaterEntity.Variant {
        public Gold(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.GOLD);
        }
    }

    public static class Prismarine extends SaltFissionHeaterEntity.Variant {
        public Prismarine(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.PRISMARINE);
        }
    }

    public static class Slime extends SaltFissionHeaterEntity.Variant {
        public Slime(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.SLIME);
        }
    }

    public static class EndStone extends SaltFissionHeaterEntity.Variant {
        public EndStone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.END_STONE);
        }
    }

    public static class Purpur extends SaltFissionHeaterEntity.Variant {
        public Purpur(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.PURPUR);
        }
    }

    public static class Diamond extends SaltFissionHeaterEntity.Variant {
        public Diamond(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.DIAMOND);
        }
    }

    public static class Emerald extends SaltFissionHeaterEntity.Variant {
        public Emerald(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.EMERALD);
        }
    }

    public static class Copper extends SaltFissionHeaterEntity.Variant {
        public Copper(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.COPPER);
        }
    }

    public static class Tin extends SaltFissionHeaterEntity.Variant {
        public Tin(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.TIN);
        }
    }

    public static class Lead extends SaltFissionHeaterEntity.Variant {
        public Lead(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LEAD);
        }
    }

    public static class Boron extends SaltFissionHeaterEntity.Variant {
        public Boron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.BORON);
        }
    }

    public static class Lithium extends SaltFissionHeaterEntity.Variant {
        public Lithium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LITHIUM);
        }
    }

    public static class Magnesium extends SaltFissionHeaterEntity.Variant {
        public Magnesium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.MAGNESIUM);
        }
    }

    public static class Manganese extends SaltFissionHeaterEntity.Variant {
        public Manganese(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.MANGANESE);
        }
    }

    public static class Aluminum extends SaltFissionHeaterEntity.Variant {
        public Aluminum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ALUMINUM);
        }
    }

    public static class Silver extends SaltFissionHeaterEntity.Variant {
        public Silver(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.SILVER);
        }
    }

    public static class Fluorite extends SaltFissionHeaterEntity.Variant {
        public Fluorite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.FLUORITE);
        }
    }

    public static class Villiaumite extends SaltFissionHeaterEntity.Variant {
        public Villiaumite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.VILLIAUMITE);
        }
    }

    public static class Carobbiite extends SaltFissionHeaterEntity.Variant {
        public Carobbiite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.CAROBBIITE);
        }
    }

    public static class Arsenic extends SaltFissionHeaterEntity.Variant {
        public Arsenic(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ARSENIC);
        }
    }

    public static class LiquidNitrogen extends SaltFissionHeaterEntity.Variant {
        public LiquidNitrogen(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LIQUID_NITROGEN);
        }
    }

    public static class LiquidHelium extends SaltFissionHeaterEntity.Variant {
        public LiquidHelium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LIQUID_HELIUM);
        }
    }

    public static class Enderium extends SaltFissionHeaterEntity.Variant {
        public Enderium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ENDERIUM);
        }
    }

    public static class Cryotheum extends SaltFissionHeaterEntity.Variant {
        public Cryotheum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.CRYOTHEUM);
        }
    }

    // MenuProvider

    @Override
    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerProcessorImpl.SaltFissionHeaterMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return getTileBlockDisplayName();
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }

    // IFissionComponent

    @Override
    public @Nullable FissionCluster getCluster() {
        return cluster;
    }

    @Override
    public void setClusterInternal(@Nullable FissionCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        if (!isProcessing(false, simulate) || componentFailCache.containsKey(worldPosition.asLong())) {
            return isInValidPosition = false;
        } else if (placementRule.requiresRecheck()) {
            isInValidPosition = placementRule.satisfied(this, simulate);
            if (isInValidPosition) {
                assumedValidCache.put(worldPosition.asLong(), this);
            }
            return isInValidPosition;
        } else if (isInValidPosition) {
            return true;
        }
        return isInValidPosition = placementRule.satisfied(this, simulate);
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return isRunning(simulate);
    }

    @Override
    public void resetStats() {
        isInValidPosition = false;
        heatingSpeedMultiplier = 0;

//        refreshAll();
    }

    @Override
    public boolean isClusterRoot() {
        return false;
    }

    @Override
    public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
//        refreshDirty();

        IFissionCoolingComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache, simulate);

        refreshIsProcessing(true, simulate);
    }

    public void refreshIsProcessing(boolean checkValid, boolean simulate) {
        if (simulate) {
            isProcessing = false;
            isRunningSimulated = isProcessing(checkValid, simulate);
        } else {
            isProcessing = isProcessing(checkValid, simulate);
            isRunningSimulated = false;
        }
//        hasConsumed = hasConsumed();
    }

    @Override
    public long getHeatStored() {
        return heat;
    }

    @Override
    public void setHeatStored(long heat) {
        this.heat = heat;
    }

    @Override
    public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {

    }

    @Override
    public boolean isNullifyingSources(Direction side, boolean simulate) {
        return false;
    }

    @Override
    public long getCooling(boolean simulate) {
        return baseProcessCooling;
    }

    // Moderator Line

    @Override
    public ModeratorBlockInfo getModeratorBlockInfo(Direction dir, boolean activeModeratorPos) {
        return new ModeratorBlockInfo(worldPosition, this, false, false, 0, 0D);
    }

    @Override
    public void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {
    }

    // IFissionPortTarget

    @Override
    public BlockPos getMasterPortPos() {
        return masterPortPos;
    }


    @Override
    public void setMasterPortPos(BlockPos pos) {
        masterPortPos = pos;
    }

    @Override
    public void clearMasterPort() {
        masterPort = null;
        masterPortPos = DEFAULT_NON;
    }

    @Override
    public void refreshMasterPort() {
        Optional<FissionReactor> multiblock = getMultiblockController();
        masterPort = multiblock.map(fissionReactor -> fissionReactor.getPartMap(FissionHeaterPortEntity.class).get(masterPortPos.asLong())).orElse(null);
        if (masterPort == null) {
            masterPortPos = DEFAULT_NON;
        }
    }

    @Override
    public void onPortRefresh() {
        refreshAll();
    }

    // Ticking

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            refreshMasterPort();
            refreshAll();
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide) {
            Optional<FissionReactor> reactor = getMultiblockController();
            boolean shouldRefresh = reactor.isPresent() && reactor.get().isReactorOn && (cluster == null || !isInValidPosition) && isProcessing(false, false);

            if (onTick()) {
                setChanged();
            }

            if (shouldRefresh) {
                reactor.get().refreshFlag = true;
            }
        }
    }

    @Override
    public void refreshAll() {
        refreshDirty();
        refreshIsProcessing(true, false);
    }

    @Override
    public void refreshRecipe() {
        boolean hasConsumed = getHasConsumed();
//        recipeInfo = NCRecipes.coolant_heater.getRecipeInfoFromHeaterInputs(heaterType, getFluidInputs(hasConsumed)); TODO
        if (info.consumesInputs) {
            consumeInputs();
        }
    }

    @Override
    public void refreshActivity() {
        canProcessInputs = canProcessInputs();
    }

    public boolean isRunning(boolean simulate) {
        return simulate ? isRunningSimulated : isProcessing;
    }

    // IProcessor

    @Override
    public ProcessorContainerInfoImpl.BasicProcessorContainerInfo<SaltFissionHeaterEntity, SaltFissionHeaterUpdatePacket> getContainerInfo() {
        return info;
    }

    @Override
    public BasicRecipeHandler getRecipeHandler() {
        return null;//NCRecipes.coolant_heater; TODO
    }

    @Override
    public RecipeInfo<BasicRecipe> getRecipeInfo() {
        return recipeInfo;
    }

    @Override
    public void setRecipeInfo(RecipeInfo<BasicRecipe> recipeInfo) {
        this.recipeInfo = recipeInfo;
    }

    @Override
    public void setRecipeStats(@Nullable BasicRecipe basic) {
        if (basic instanceof FissionCoolantHeaterRecipe recipe) {
            baseProcessCooling = recipe.getCoolantHeaterCoolingRate();
            placementRule = FissionPlacement.RULE_MAP.get(recipe.getCoolantHeaterPlacementRule());
        } else {
            baseProcessCooling = 0;
            placementRule = FissionPlacement.RULE_MAP.get("");
        }
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
        return 1D;
    }

    @Override
    public void setBaseProcessTime(double baseProcessTime) {
    }

    @Override
    public double getBaseProcessPower() {
        return 0D;
    }

    @Override
    public void setBaseProcessPower(double baseProcessPower) {
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
        return heatingSpeedMultiplier;
    }

    @Override
    public double getPowerMultiplier() {
        return 0D;
    }

    @Override
    public boolean isProcessing() {
        return !isSimulation() && isProcessing(true, false);
    }

    public boolean isProcessing(boolean checkValid, boolean simulate) {
        return readyToProcess(checkValid);
    }

    @Override
    public boolean readyToProcess() {
        return readyToProcess(true);
    }

    public boolean readyToProcess(boolean checkValid) {
        return canProcessInputs && hasConsumed && isMachineAssembled() && (!checkValid || (cluster != null && isInValidPosition));
    }

    @Override
    public void finishProcess() {
        int oldProcessCooling = baseProcessCooling;
        produceProducts();
        refreshRecipe();
        time = Math.max(0D, time - 1D);
        refreshActivityOnProduction();
        if (!canProcessInputs) {
            time = 0;
        }

        Optional<FissionReactor> multiblock = getMultiblockController();
        if (multiblock.isPresent()) {
            if (canProcessInputs) {
                if (oldProcessCooling != baseProcessCooling) {
                    multiblock.get().addClusterToRefresh(cluster);
                }
            } else {
                multiblock.get().refreshFlag = true;
            }
        }
    }

    // ITileInventory

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        return inventoryStacks;
    }

    @Override
    public void setChanged() {
        refreshDirty();
        super.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // TODO
    }

    @Override
    public @Nonnull InventoryConnection[] getInventoryConnections() {
        return inventoryConnections;
    }

    @Override
    public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
        inventoryConnections = connections;
    }

    @Override
    public ItemOutputSetting getItemOutputSetting(int slot) {
        return ItemOutputSetting.DEFAULT;
    }

    @Override
    public void setItemOutputSetting(int slot, ItemOutputSetting setting) {
    }

    // ITileFluid

    @Override
    public @Nonnull List<Tank> getTanks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
    }

    @Override
    public @Nonnull FluidConnection[] getFluidConnections() {
        return fluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
        fluidConnections = connections;
    }

    @Override
    public @Nonnull FluidTileWrapper[] getFluidSides() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFluidSides() : fluidSides;
    }

    @Override
    public @Nonnull ChemicalTileWrapper[] getChemicalSides() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getChemicalSides() : chemicalSides;
    }

    @Override
    public boolean getInputTanksSeparated() {
        return false;
    }

    @Override
    public void setInputTanksSeparated(boolean separated) {
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        return false;
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        return TankOutputSetting.DEFAULT;
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
    }

    @Override
    public boolean hasConfigurableFluidConnections() {
        return false;
    }

    @Override
    public void clearAllTanks() {
        for (Tank tank : tanks) {
            tank.setFluidStored(FluidStack.EMPTY);
        }
        for (Tank tank : consumedTanks) {
            tank.setFluidStored(FluidStack.EMPTY);
        }
        refreshAll();
    }

    // ITileFilteredFluid

    @Override
    public @Nonnull List<Tank> getTanksInternal() {
        return tanks;
    }

    @Override
    public @Nonnull List<Tank> getFilterTanks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
    }

    @Override
    public boolean canModifyFilter(int tank) {
        return !isMachineAssembled();
    }

    @Override
    public void onFilterChanged(int slot) {
        setChanged();
    }

    @Override
    public Object getFilterKey() {
        return heaterType;
    }

    // ITileGui

    @Override
    public Set<Player> getTileUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public SaltFissionHeaterUpdatePacket getTileUpdatePacket() {
        return new SaltFissionHeaterUpdatePacket(worldPosition, isProcessing, time, 1D, getTanks(), masterPortPos, getFilterTanks(), cluster);
    }

    @Override
    public void onTileUpdatePacket(SaltFissionHeaterUpdatePacket message) {
        IBasicProcessor.super.onTileUpdatePacket(message);
        if (DEFAULT_NON.equals(masterPortPos = message.masterPortPos) ^ masterPort == null) {
            refreshMasterPort();
        }
        TankInfo.readInfoList(message.filterTankInfos, getFilterTanks());
        clusterHeatStored = message.clusterHeatStored;
        clusterHeatCapacity = message.clusterHeatCapacity;
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeTanks(nbt, registries);

        writeProcessorNBT(nbt, registries);

        nbt.putInt("baseProcessCooling", baseProcessCooling);
        nbt.putDouble("heatingSpeedMultiplier", heatingSpeedMultiplier);

        nbt.putLong("clusterHeat", heat);
        nbt.putBoolean("isInValidPosition", isInValidPosition);

        nbt.putBoolean("isRunningSimulated", isRunningSimulated);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);

        if (DYN_COOLANT_NAME_MAP.containsKey(heaterType.getName())) {
            coolantName = DYN_COOLANT_NAME_MAP.get(heaterType);
            tanks.get(0).setAllowedFluids(Collections.singleton(coolantName));
            filterTanks.get(0).setAllowedFluids(Collections.singleton(coolantName));
        }

        readTanks(nbt, registries);

        readProcessorNBT(nbt, registries);

        baseProcessCooling = nbt.getInt("baseProcessCooling");
        heatingSpeedMultiplier = nbt.getDouble("heatingSpeedMultiplier");

        heat = nbt.getLong("clusterHeat");
        isInValidPosition = nbt.getBoolean("isInValidPosition");

        isRunningSimulated = nbt.getBoolean("isRunningSimulated");
    }

    @Override
    public CompoundTag writeTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).writeToNBT(nbt, registries, "tanks" + i);
        }
        for (int i = 0; i < filterTanks.size(); ++i) {
            filterTanks.get(i).writeToNBT(nbt, registries, "filterTanks" + i);
        }
        for (int i = 0; i < consumedTanks.size(); ++i) {
            consumedTanks.get(i).writeToNBT(nbt, registries, "consumedTanks" + i);
        }
        return nbt;
    }

    @Override
    public void readTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).readFromNBT(nbt, registries, "tanks" + i);
        }
        for (int i = 0; i < filterTanks.size(); ++i) {
            filterTanks.get(i).readFromNBT(nbt, registries, "filterTanks" + i);
        }
        for (int i = 0; i < consumedTanks.size(); ++i) {
            consumedTanks.get(i).readFromNBT(nbt, registries, "consumedTanks" + i);
        }
    }

//    // Capability TODO
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY)) {
//            return !getTanks().isEmpty() && hasFluidSideCapability(side);
//        }
//        return super.hasCapability(capability, side);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//            if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
//                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
//            }
//            return null;
//        }
//        else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
//            if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
//                return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
//            }
//            return null;
//        }
//        return super.getCapability(capability, side);
//    }

    // ComputerCraft

    @Override
    public String getCCKey() {
        return "heater";
    }

    @Override
    public Object getCCInfo() {
        Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
        List<Tank> tanks = getTanks();
        entry.put("coolant", CCHelper.tankInfo(tanks.get(0)));
        entry.put("hot_coolant", CCHelper.tankInfo(tanks.get(1)));
        entry.put("type", heaterType);
        entry.put("cooling", getCooling(false));
        entry.put("speed_multiplier", getSpeedMultiplier());
        entry.put("is_processing", getIsProcessing());
        entry.put("current_time", getCurrentTime());
        entry.put("base_process_time", getBaseProcessTime());
        return entry;
    }
}
