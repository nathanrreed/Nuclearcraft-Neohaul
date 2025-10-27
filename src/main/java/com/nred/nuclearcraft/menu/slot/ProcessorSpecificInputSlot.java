package com.nred.nuclearcraft.menu.slot;

import com.nred.nuclearcraft.screen.processor.GuiFluidSorptions;
import com.nred.nuclearcraft.screen.processor.GuiItemSorptions;
import com.nred.nuclearcraft.screen.processor.GuiProcessor;
import com.nred.nuclearcraft.screen.processor.GuiUpgradableProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ProcessorSpecificInputSlot extends Slot {
    public final Object[] inputs;

    public ProcessorSpecificInputSlot(Container inv, int index, int xPosition, int yPosition, Object... inputs) {
        super(inv, index, xPosition, yPosition);
        this.inputs = inputs;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        for (Object input : inputs) {
            if (input instanceof TagKey<?> tagKey && tagKey.isFor(BuiltInRegistries.ITEM.key())) {
                if (Ingredient.of((TagKey<Item>) tagKey).test(stack)) {
                    return true;
                }
            } else if (input instanceof ItemStack) {
                if (ItemStack.isSameItem((ItemStack) input, stack)) {
                    return true;
                }
            } else if (input instanceof Item) {
                if (input == stack.getItem()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isActive() {
        return !(Minecraft.getInstance().screen instanceof GuiProcessor.SideConfigScreen || Minecraft.getInstance().screen instanceof GuiUpgradableProcessor.SideConfigScreen || Minecraft.getInstance().screen instanceof GuiItemSorptions<?,?,?> || Minecraft.getInstance().screen instanceof GuiFluidSorptions<?,?,?>);
    }
}