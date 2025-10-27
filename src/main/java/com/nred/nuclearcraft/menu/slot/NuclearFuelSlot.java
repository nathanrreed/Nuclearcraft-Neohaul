package com.nred.nuclearcraft.menu.slot;

import com.nred.nuclearcraft.block_entity.processor.NuclearFurnaceEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NuclearFuelSlot extends Slot {
	public NuclearFuelSlot(Inventory tile, int index, int xPosition, int yPosition) {
		super(tile, index, xPosition, yPosition);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return NuclearFurnaceEntity.isFuel(stack);
	}
}