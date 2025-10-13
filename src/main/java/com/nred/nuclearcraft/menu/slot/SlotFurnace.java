package com.nred.nuclearcraft.menu.slot;


import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;

public class SlotFurnace extends FurnaceResultSlot {
    public SlotFurnace(Player player, Container tile, int index, int xPosition, int yPosition) {
        super(player, tile, index, xPosition, yPosition);
    }
}