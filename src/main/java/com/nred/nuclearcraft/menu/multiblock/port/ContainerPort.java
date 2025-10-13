package com.nred.nuclearcraft.menu.multiblock.port;

import com.nred.nuclearcraft.block.ITileGui;
import com.nred.nuclearcraft.block.ITilePort;
import com.nred.nuclearcraft.block.ITilePortTarget;
import com.nred.nuclearcraft.block.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.handler.*;
import com.nred.nuclearcraft.menu.ContainerInfoTile;
import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.MultiblockLogic;
import com.nred.nuclearcraft.payload.TileUpdatePacket;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public abstract class ContainerPort<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>, PORT extends BlockEntity & ITilePort<MULTIBLOCK, LOGIC, PORT, TARGET> & ITileGui<PORT, PACKET, INFO>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, PORT, TARGET>, PACKET extends TileUpdatePacket, INFO extends TileContainerInfo<PORT>> extends ContainerInfoTile<PORT, PACKET, INFO> {
    protected final PORT tile;

    public ContainerPort(MenuType<?> menuType, int containerId, Inventory inventory, PORT tile) {
        super(menuType, containerId, inventory, tile);
        this.tile = tile;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack stackCopy = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        int invStart = tile instanceof ITileFilteredInventory ? 2 : 0;
        int invEnd = 36 + invStart;

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            stackCopy = stack.copy();

            if (index >= (tile instanceof ITileFilteredInventory ? 1 : 0) && index < invStart) {
                if (!moveItemStackTo(stack, invStart, invEnd, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, stackCopy);
            } else if (index >= invStart) {
                ItemStack transfer = transferPlayerStack(player, index, invStart, invEnd, stack);
                if (transfer != null) {
                    return transfer;
                }
            } else if (!moveItemStackTo(stack, invStart, invEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == stackCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }
        return stackCopy;
    }

    public ItemStack transferPlayerStack(Player player, int index, int invStart, int invEnd, ItemStack stack) {
        if (getRecipeHandler().isValidItemInput(stack)) {
            if (!moveItemStackTo(stack, 0, tile instanceof ITileFilteredInventory ? 1 : 0, false)) {
                return ItemStack.EMPTY;
            }
        } else if (tile instanceof ITileFilteredInventory tileFiltered) {
            NonNullList<ItemStack> filterStacks = tileFiltered.getFilterStacks();
            for (int i = 0; i < filterStacks.size(); ++i) {
                if (tileFiltered.canModifyFilter(i) && !filterStacks.get(i).isEmpty()) {
                    filterStacks.set(i, ItemStack.EMPTY);
                    tileFiltered.onFilterChanged(i);
                    slots.get(i).setChanged();
                    return ItemStack.EMPTY;
                }
            }
        }
        return transferPlayerStackDefault(player, index, invStart, invEnd, stack);
    }

    protected abstract BasicRecipeHandler<?> getRecipeHandler();
}
