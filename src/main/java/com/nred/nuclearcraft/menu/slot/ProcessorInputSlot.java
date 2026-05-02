package com.nred.nuclearcraft.menu.slot;

import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.screen.processor.FluidSorptionsScreen;
import com.nred.nuclearcraft.screen.processor.ItemSorptionsScreen;
import com.nred.nuclearcraft.screen.processor.ProcessorScreen;
import com.nred.nuclearcraft.screen.processor.UpgradableProcessorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ProcessorInputSlot extends Slot {
    protected final BasicRecipeHandler<?> recipeHandler;

    public ProcessorInputSlot(IProcessor tile, BasicRecipeHandler<?> recipeHandler, int index, int xPosition, int yPosition) {
        super(tile, index, xPosition, yPosition);
        this.recipeHandler = recipeHandler;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return recipeHandler.isValidItemInput(stack, ((IProcessor<?, ?, ?>) container).getTileWorld());
    }

    @Override
    public boolean isActive() {
        return !(Minecraft.getInstance().screen instanceof ProcessorScreen.SideConfigScreen || Minecraft.getInstance().screen instanceof UpgradableProcessorScreen.SideConfigScreen || Minecraft.getInstance().screen instanceof ItemSorptionsScreen<?, ?, ?> || Minecraft.getInstance().screen instanceof FluidSorptionsScreen<?, ?, ?>);
    }
}