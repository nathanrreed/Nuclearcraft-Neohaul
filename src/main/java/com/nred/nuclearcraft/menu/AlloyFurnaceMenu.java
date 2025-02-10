package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AlloyFurnaceMenu extends ProcessorMenu {
    public static int INPUT_1 = 0;
    public static int INPUT_2 = 1;
    public static int OUTPUT = 3;

    public AlloyFurnaceMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, INPUT_1, 46, 35)));
        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, INPUT_2, 66, 35)));
        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT, 126, 35)));
    }

    // Client Constructor
    public AlloyFurnaceMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}