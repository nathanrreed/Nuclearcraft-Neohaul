package com.nred.nuclearcraft.menu.processor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;

public class SeparatorMenu extends ProcessorMenu {
    public static final int INPUT = 2;
    public static final int OUTPUT_1 = 3;
    public static final int OUTPUT_2 = 4;

    public SeparatorMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT, 42, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, OUTPUT_1, 102, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, OUTPUT_2, 130, 35)));
    }

    // Client Constructor
    public SeparatorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}