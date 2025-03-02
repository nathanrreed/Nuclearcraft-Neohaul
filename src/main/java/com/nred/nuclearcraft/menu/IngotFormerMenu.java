package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;

public class IngotFormerMenu extends ProcessorMenu {
    public static final int INPUT = 0;
    public static final int OUTPUT = 2;

    public IngotFormerMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        FLUID_INPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, INPUT, 56, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, OUTPUT, 116, 35)));
    }

    // Client Constructor
    public IngotFormerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}