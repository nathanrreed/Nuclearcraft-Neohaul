package com.nred.nuclearcraft.menu.slot;

import com.nred.nuclearcraft.screen.processor.FluidSorptionsScreen;
import com.nred.nuclearcraft.screen.processor.ItemSorptionsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.neoforged.fml.loading.FMLEnvironment;

public class ProcessorSlot extends Slot {
    public ProcessorSlot(Container tile, int index, int xPosition, int yPosition) {
        super(tile, index, xPosition, yPosition);
    }

    @Override
    public boolean isActive() {
        if (FMLEnvironment.dist.isDedicatedServer()) {
            return false;
        }
        return !(Minecraft.getInstance().screen instanceof ItemSorptionsScreen<?, ?, ?, ?> || Minecraft.getInstance().screen instanceof FluidSorptionsScreen<?, ?, ?, ?>);
    }
}