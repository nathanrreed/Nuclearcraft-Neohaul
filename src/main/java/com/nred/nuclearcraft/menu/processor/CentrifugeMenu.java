package com.nred.nuclearcraft.menu.processor;

import com.nred.nuclearcraft.menu.FluidSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;

public class CentrifugeMenu extends ProcessorMenu {
    public static final int INPUT = 0;
    public static final int OUTPUT_1 = 1;
    public static final int OUTPUT_2 = 2;
    public static final int OUTPUT_3 = 3;
    public static final int OUTPUT_4 = 4;
    public static final int OUTPUT_5 = 5;
    public static final int OUTPUT_6 = 6;

    public CentrifugeMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress, 12);

        FLUID_INPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, INPUT, 40, 41)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_1, 96, 31)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_2, 116, 31)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_3, 136, 31)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_4, 96, 51)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_5, 116, 51)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_6, 136, 51)));
    }

    // Client Constructor
    public CentrifugeMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}