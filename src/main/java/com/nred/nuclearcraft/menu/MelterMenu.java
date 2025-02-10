package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MelterMenu extends ProcessorMenu {
    public static int INPUT = 0;
    public static int OUTPUT = 0;

    public MelterMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, INPUT, 56, 35)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT, 112, 31, 24)));
    }

    // Client Constructor
    public MelterMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}