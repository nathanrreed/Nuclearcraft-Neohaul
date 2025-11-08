package com.nred.nuclearcraft.block_entity.fission.port;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.payload.multiblock.port.ItemPortUpdatePacket;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.util.NBTHelper;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public abstract class FissionItemPortEntity<PORT extends FissionItemPortEntity<PORT, TARGET> & ITileFilteredInventory, TARGET extends IFissionPortTarget<PORT, TARGET> & ITileFilteredInventory> extends FissionPortEntity<PORT, TARGET> implements ITileFilteredInventory, ITileGui<PORT, ItemPortUpdatePacket, TileContainerInfo<PORT>> {
    protected final TileContainerInfo<PORT> info;

    protected final @Nonnull NonNullList<ItemStack> inventoryStacks = NonNullList.withSize(2, ItemStack.EMPTY);
    protected final @Nonnull NonNullList<ItemStack> filterStacks = NonNullList.withSize(2, ItemStack.EMPTY);

    protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Lists.newArrayList(ItemSorption.IN, ItemSorption.NON));

    public int inventoryStackLimit = 64;

    protected final BasicRecipeHandler recipeHandler;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public FissionItemPortEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name, Class<PORT> portClass, BasicRecipeHandler recipeHandler) {
        super(type, pos, blockState, portClass);
        info = TileInfoHandler.getTileContainerInfo(name);
        this.recipeHandler = recipeHandler;
    }

    @Override
    public TileContainerInfo<PORT> getContainerInfo() {
        return info;
    }

    // MenuProvider

    @Override
    public Component getDisplayName() {
        return getTileBlockDisplayName();
    }

    @Override
    public Object getFilterKey() {
        return getFilterStacks().get(0).isEmpty() ? 0 : RecipeHelper.pack(getFilterStacks().get(0));
    }

    @Override
    public void update() {
        super.update();
        if (!level.isClientSide) {
            Optional<Direction> facing = getPartPosition().getDirection();
            if (facing.isPresent() && !getItem(1).isEmpty() && getItemSorption(facing.get(), 1).canExtract()) {
                pushStacksToSide(facing.get());
            }
            sendTileUpdatePacketToListeners();
        }
    }

    @Override
    public int getMaxStackSize() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getMaxStackSize() : inventoryStackLimit;
    }

    @Override
    public void setInventoryStackLimit(int stackLimit) {
        inventoryStackLimit = stackLimit;
    }

    @Override
    public int getTankCapacityPerConnection() {
        return 0;
    }

    @Override
    public int getTankBaseCapacity() {
        return 1;
    }

    @Override
    public void setTankCapacity(int capacity) {
    }

    @Override
    public boolean canModifyFilter(int slot) {
        return !isMachineAssembled();
    }

    @Override
    public void onFilterChanged(int slot) {
        setChanged();
    }

    // Inventory

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStacks() : inventoryStacks;
    }

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacksInternal() {
        return inventoryStacks;
    }

    @Override
    public @Nonnull NonNullList<ItemStack> getFilterStacks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterStacks() : filterStacks;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        ItemStack stack = ITileFilteredInventory.super.removeItem(slot, count);
        if (!level.isClientSide) {
            if (slot < recipeHandler.itemInputSize) {
                refreshTargetsFlag = true;
            } else if (slot < recipeHandler.itemInputSize + recipeHandler.itemOutputSize) {
                refreshTargetsFlag = true;
            }
        }
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ITileFilteredInventory.super.setItem(slot, stack);
        if (!level.isClientSide) {
            if (slot < recipeHandler.itemInputSize) {
                refreshTargetsFlag = true;
            } else if (slot < recipeHandler.itemInputSize + recipeHandler.itemOutputSize) {
                refreshTargetsFlag = true;
            }
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (stack.isEmpty() || slot >= recipeHandler.itemInputSize) {
            return false;
        }
        ItemStack filter = getFilterStacks().get(slot);
        if (!filter.isEmpty() && !ItemStack.isSameItemSameComponents(stack, filter)) {
            return false;
        }
        return isItemValidForSlotInternal(slot, stack);
    }

    @Override
    public boolean isItemValidForSlotInternal(int slot, ItemStack stack) {
        if (stack.isEmpty() || slot >= recipeHandler.itemInputSize) {
            return false;
        }

        if (NCConfig.smart_processor_input) {
            return recipeHandler.isValidItemInput(stack, slot, getInventoryStacks().subList(0, recipeHandler.itemInputSize), Collections.emptyList(), null);
        } else {
            return recipeHandler.isValidItemInput(stack);
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return ITileFilteredInventory.super.canPlaceItemThroughFace(slot, stack, side) && canPlaceItem(slot, stack);
    }

    @Override
    public void clearAllSlots() {
        ITileFilteredInventory.super.clearAllSlots();
        refreshTargetsFlag = true;
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

    @Override
    public boolean hasConfigurableInventoryConnections() {
        return true;
    }

    // ITileGui

    @Override
    public Set<Player> getTileUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public ItemPortUpdatePacket getTileUpdatePacket() {
        return new ItemPortUpdatePacket(worldPosition, masterPortPos, getFilterStacks());
    }

    @Override
    public void onTileUpdatePacket(ItemPortUpdatePacket message) {
        masterPortPos = message.masterPortPos;
        if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
            refreshMasterPort();
        }
        IntStream.range(0, filterStacks.size()).forEach(x -> filterStacks.set(x, message.filterStacks.get(x)));
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level worldIn, Direction facing, BlockPos hitPos) {
        if (!player.isCrouching()) {
            if (getMultiblockController().isPresent()) {
                if (getItemSorption(facing, 0) != ItemSorption.IN) {
                    for (Direction side : Direction.values()) {
                        setItemSorption(side, 0, ItemSorption.IN);
                        setItemSorption(side, 1, ItemSorption.NON);
                    }
                    setActivity(false);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.in_config").withStyle(ChatFormatting.BLUE)));
                } else {
                    for (Direction side : Direction.values()) {
                        setItemSorption(side, 0, ItemSorption.NON);
                        setItemSorption(side, 1, ItemSorption.OUT);
                    }
                    setActivity(true);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.out_config").withStyle(ChatFormatting.GOLD)));
                }
                markDirtyAndNotify(true);
                return true;
            }
        }
        return super.onUseMultitool(multitool, player, worldIn, facing, hitPos);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeInventory(nbt, registries);
        writeInventoryConnections(nbt, registries);

        nbt.putInt("inventoryStackLimit", inventoryStackLimit);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readInventory(nbt, registries);
        readInventoryConnections(nbt, registries);

        for (Direction side : Direction.values()) {
            if (getItemSorption(side, 0).equals(ItemSorption.IN)) {
                setItemSorption(side, 1, ItemSorption.NON);
            }
        }

        inventoryStackLimit = nbt.getInt("inventoryStackLimit");
    }

    @Override
    public CompoundTag writeInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        int[] counts = new int[inventoryStacks.size()];
        for (int i = 0; i < inventoryStacks.size(); ++i) {
            nbt.putInt("inventoryStackSize" + i, counts[i] = inventoryStacks.get(i).getCount());
            if (!inventoryStacks.get(i).isEmpty()) {
                inventoryStacks.get(i).setCount(1);
            }
        }

        NBTHelper.writeAllItems(nbt, registries, inventoryStacks, filterStacks);

        for (int i = 0; i < inventoryStacks.size(); ++i) {
            if (!inventoryStacks.get(i).isEmpty()) {
                inventoryStacks.get(i).setCount(counts[i]);
            }
        }

        return nbt;
    }

    @Override
    public void readInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        NBTHelper.readAllItems(nbt, registries, inventoryStacks, filterStacks);

        for (int i = 0; i < inventoryStacks.size(); ++i) {
            if (!inventoryStacks.get(i).isEmpty()) {
                inventoryStacks.get(i).setCount(nbt.getInt("inventoryStackSize" + i));
            }
        }
    }
}