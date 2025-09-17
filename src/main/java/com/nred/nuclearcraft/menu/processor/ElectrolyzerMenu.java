package com.nred.nuclearcraft.menu.processor;

import com.nred.nuclearcraft.menu.FluidSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;

public class ElectrolyzerMenu extends ProcessorMenu {
    public static final int INPUT = 0;
    public static final int OUTPUT_1 = 1;
    public static final int OUTPUT_2 = 2;
    public static final int OUTPUT_3 = 3;
    public static final int OUTPUT_4 = 4;

    public ElectrolyzerMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress, 12);

        FLUID_INPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, INPUT, 50, 41)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_1, 106, 31)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_2, 126, 31)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_3, 106, 51)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_4, 126, 51)));
    }

    // Client Constructor
    public ElectrolyzerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}