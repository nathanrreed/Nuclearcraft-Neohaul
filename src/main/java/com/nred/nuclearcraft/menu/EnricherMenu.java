package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EnricherMenu extends ProcessorMenu {
    public static final int INPUT = 2;
    public static final int FLUID_INPUT = 0;
    public static final int OUTPUT = 1;

    public EnricherMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, INPUT, 46, 35)));
        FLUID_INPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, FLUID_INPUT, 66, 35)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT, 122, 31, 24)));
    }

    // Client Constructor
    public EnricherMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}