package com.nred.nuclearcraft.menu.slot;


import com.nred.nuclearcraft.screen.processor.GuiFluidSorptions;
import com.nred.nuclearcraft.screen.processor.GuiItemSorptions;
import com.nred.nuclearcraft.screen.processor.GuiProcessor;
import com.nred.nuclearcraft.screen.processor.GuiUpgradableProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public class ProcessorResultSlot extends ResultSlot {
    public ProcessorResultSlot(Player player, Container tile, int index, int xPosition, int yPosition) {
        super(player, tile, index, xPosition, yPosition);
    }

    @Override
    public boolean isActive() {
        return !(Minecraft.getInstance().screen instanceof GuiProcessor.SideConfigScreen || Minecraft.getInstance().screen instanceof GuiUpgradableProcessor.SideConfigScreen || Minecraft.getInstance().screen instanceof GuiItemSorptions<?,?,?> || Minecraft.getInstance().screen instanceof GuiFluidSorptions<?,?,?>);
    }
}