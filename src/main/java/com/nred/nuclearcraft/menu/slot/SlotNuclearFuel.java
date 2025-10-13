package com.nred.nuclearcraft.menu.slot;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotNuclearFuel extends Slot {
	public SlotNuclearFuel(Inventory tile, int index, int xPosition, int yPosition) {
		super(tile, index, xPosition, yPosition);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false; // TODO TileNuclearFurnace.isItemFuel(stack);
	}
}
