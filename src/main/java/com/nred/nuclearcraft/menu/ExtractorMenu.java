package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ExtractorMenu extends ProcessorMenu {
    public static int INPUT = 0;
    public static int OUTPUT = 1;
    public static int FLUID_OUTPUT = 0;

    public ExtractorMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, INPUT, 42, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT, 102, 35)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, FLUID_OUTPUT, 126, 31, 24)));
    }

    // Client Constructor
    public ExtractorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}