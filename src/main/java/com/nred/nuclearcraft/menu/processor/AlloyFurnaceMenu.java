package com.nred.nuclearcraft.menu.processor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;

public class AlloyFurnaceMenu extends ProcessorMenu {
    public static final int INPUT_1 = 2;
    public static final int INPUT_2 = 3;
    public static final int OUTPUT = 4;

    public AlloyFurnaceMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_1, 46, 35)));
        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_2, 66, 35)));
        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, OUTPUT, 126, 35)));
    }

    // Client Constructor
    public AlloyFurnaceMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}