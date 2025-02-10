package com.nred.nuclearcraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;

public class SaltMixerMenu extends ProcessorMenu {
    public static final int INPUT_1 = 0;
    public static final int INPUT_2 = 1;
    public static final int OUTPUT = 2;

    public SaltMixerMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        FLUID_INPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, INPUT_1, 46, 35)));
        FLUID_INPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, INPUT_2, 66, 35)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT, 122, 31, 24)));
    }

    // Client Constructor
    public SaltMixerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}