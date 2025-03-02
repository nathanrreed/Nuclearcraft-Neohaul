package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;

public class AssemblerMenu extends ProcessorMenu {
    public static final int INPUT_1 = 2;
    public static final int INPUT_2 = 3;
    public static final int INPUT_3 = 4;
    public static final int INPUT_4 = 5;
    public static final int OUTPUT = 6;

    public AssemblerMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress, 12);

        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_1, 46, 31)));
        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_2, 66, 31)));
        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_3, 46, 51)));
        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_4, 66, 51)));
        ITEM_OUTPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, OUTPUT, 126, 41)));
    }

    // Client Constructor
    public AssemblerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}