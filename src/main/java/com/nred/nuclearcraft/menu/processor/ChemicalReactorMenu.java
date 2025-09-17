package com.nred.nuclearcraft.menu.processor;

import com.nred.nuclearcraft.menu.FluidSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;

public class ChemicalReactorMenu extends ProcessorMenu {
    public static final int INPUT_1 = 0;
    public static final int INPUT_2 = 1;
    public static final int OUTPUT_1 = 2;
    public static final int OUTPUT_2 = 3;

    public ChemicalReactorMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ProcessorInfo info, DataSlot progress) {
        super(containerId, inventory, access, info, progress);

        FLUID_INPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, INPUT_1, 32, 35)));
        FLUID_INPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, INPUT_2, 52, 35)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_1, 108, 31, 24)));
        FLUID_OUTPUTS.add((FluidSlot) this.addSlot(new FluidSlot(fluidHandler, OUTPUT_2, 136, 31, 24)));
    }

    // Client Constructor
    public ChemicalReactorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, ProcessorInfo.read(extraData), DataSlot.standalone());
    }
}