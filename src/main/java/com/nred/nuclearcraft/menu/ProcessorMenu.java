package com.nred.nuclearcraft.menu;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.CustomFluidStackHandler;
import com.nred.nuclearcraft.helpers.MenuHelper;
import com.nred.nuclearcraft.payload.RecipeSetPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

import static com.nred.nuclearcraft.helpers.MenuHelper.listPlayerInventoryHotbarPos;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.MenuRegistration.PROCESSOR_MENU_TYPES;

public abstract class ProcessorMenu extends AbstractContainerMenu {
    public IItemHandler itemHandler;
    public CustomFluidStackHandler fluidHandler;
    public IEnergyStorage energyStorage;
    public final Inventory inventory;
    public ContainerLevelAccess access;
    public final ProcessorInfo info;
    public DataSlot progress;
    public static final int SPEED = 0;
    public static final int ENERGY = 1;
    public ArrayList<Slot> ITEM_INPUTS = new ArrayList<>();
    public ArrayList<Slot> ITEM_OUTPUTS = new ArrayList<>();
    public ArrayList<FluidSlot> FLUID_INPUTS = new ArrayList<>();
    public ArrayList<FluidSlot> FLUID_OUTPUTS = new ArrayList<>();
    public Slot SPEED_SLOT;
    public Slot ENERGY_SLOT;
    private ResourceLocation cachedRecipe;

    protected ProcessorMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress, int offset) {
        super(PROCESSOR_MENU_TYPES.get(info.typeName()).get(), containerId);
        this.inventory = inventory;
        this.access = access;
        this.info = info;
        this.progress = progress;

        this.energyStorage = inventory.player.level().getCapability(Capabilities.EnergyStorage.BLOCK, info.pos(), null);
        this.itemHandler = inventory.player.level().getCapability(Capabilities.ItemHandler.BLOCK, info.pos(), null);
        this.fluidHandler = (CustomFluidStackHandler) inventory.player.level().getCapability(Capabilities.FluidHandler.BLOCK, info.pos(), null);

        // Progress
        this.addDataSlot(this.progress);

        // Set Inventory Slot locations
        for (int[] slotInfo : listPlayerInventoryHotbarPos(offset)) {
            this.addSlot(new Slot(inventory, slotInfo[0], slotInfo[1], slotInfo[2]));
        }

        // Upgrade slots
        SPEED_SLOT = this.addSlot(new SlotItemHandler(itemHandler, SPEED, 132, 64 + offset));
        ENERGY_SLOT = this.addSlot(new SlotItemHandler(itemHandler, ENERGY, 152, 64 + offset));
    }

    @Override
    public void broadcastChanges() {
        if (inventory.player instanceof ServerPlayer player && player.level().getBlockEntity(info.pos()) instanceof ProcessorEntity entity) {
            if (entity.recipe != null) {
                ResourceLocation temp = entity.recipe.id();
                if (cachedRecipe == null || !cachedRecipe.equals(temp)) { // Don't send if recipe hasn't changed
                    cachedRecipe = temp;
                    PacketDistributor.sendToPlayer(player, new RecipeSetPayload(temp));
                }
            } else if (cachedRecipe != null) {
                cachedRecipe = null;
                PacketDistributor.sendToPlayer(player, new RecipeSetPayload(ResourceLocation.parse("")));
            }
        }
        super.broadcastChanges();
    }

    protected ProcessorMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        this(containerId, inventory, access, info, progress, 0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return MenuHelper.quickMoveStack(player, index, slots, this::moveItemStackTo, 2 + ITEM_INPUTS.size() + ITEM_OUTPUTS.size());
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, PROCESSOR_MAP.get(info.typeName()).get());
    }
}