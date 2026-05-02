package com.nred.nuclearcraft.menu.slot;


import com.nred.nuclearcraft.screen.processor.FluidSorptionsScreen;
import com.nred.nuclearcraft.screen.processor.ItemSorptionsScreen;
import com.nred.nuclearcraft.screen.processor.ProcessorScreen;
import com.nred.nuclearcraft.screen.processor.UpgradableProcessorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public class ProcessorResultSlot extends ResultSlot {
    public ProcessorResultSlot(Player player, Container tile, int index, int xPosition, int yPosition) {
        super(player, tile, index, xPosition, yPosition);
    }

    @Override
    public boolean isActive() {
        return !(Minecraft.getInstance().screen instanceof ProcessorScreen.SideConfigScreen || Minecraft.getInstance().screen instanceof UpgradableProcessorScreen.SideConfigScreen || Minecraft.getInstance().screen instanceof ItemSorptionsScreen<?,?,?> || Minecraft.getInstance().screen instanceof FluidSorptionsScreen<?,?,?>);
    }
}