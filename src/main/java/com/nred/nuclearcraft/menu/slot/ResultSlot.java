package com.nred.nuclearcraft.menu.slot;


import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;

public class ResultSlot extends FurnaceResultSlot {
    public ResultSlot(Player player, Container tile, int index, int xPosition, int yPosition) {
        super(player, tile, index, xPosition, yPosition);
    }
}