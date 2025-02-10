package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SeparatorMenu extends ProcessorMenu {
    public static int INPUT = 0;
    public static int OUTPUT_1 = 1;
    public static int OUTPUT_2 = 2;

    public SeparatorMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, INPUT, 42, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_1, 102, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_2, 130, 35)));
    }

    // Client Constructor
    public SeparatorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}