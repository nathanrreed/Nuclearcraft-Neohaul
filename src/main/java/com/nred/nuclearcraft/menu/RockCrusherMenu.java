package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;

public class RockCrusherMenu extends ProcessorMenu {
    public static int INPUT = 2;
    public static int OUTPUT_1 = 3;
    public static int OUTPUT_2 = 4;
    public static int OUTPUT_3 = 5;

    public RockCrusherMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, INPUT, 38, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_1, 94, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_2, 114, 35)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_3, 134, 35)));
    }

    // Client Constructor
    public RockCrusherMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}