package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FuelReprocessorMenu extends ProcessorMenu {
    public static final int INPUT = 2;
    public static final int OUTPUT_1 = 3;
    public static final int OUTPUT_2 = 4;
    public static final int OUTPUT_3 = 5;
    public static final int OUTPUT_4 = 6;
    public static final int OUTPUT_5 = 7;
    public static final int OUTPUT_6 = 8;
    public static final int OUTPUT_7 = 9;
    public static final int OUTPUT_8 = 10;

    public FuelReprocessorMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress, 12);

        ITEM_INPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, INPUT, 30, 41)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_1, 86, 31)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_2, 106, 31)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_3, 126, 31)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_4, 146, 31)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_5, 86, 51)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_6, 106, 51)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_7, 126, 51)));
        ITEM_OUTPUTS.add(this.addSlot(new SlotItemHandler(itemHandler, OUTPUT_8, 146, 51)));
    }

    // Client Constructor
    public FuelReprocessorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}