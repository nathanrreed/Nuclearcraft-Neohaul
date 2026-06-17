package com.nred.nuclearcraft.compat.kubejs.custom_processor;

import com.nred.nuclearcraft.block.item.NCItemBlock;
import com.nred.nuclearcraft.util.InfoHelper;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@ReturnsSelf
public class CustomProcessorItemBuilder extends BlockItemBuilder {
    public CustomProcessorItemBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Item createObject() {
        return new NCItemBlock(blockBuilder.get(), ChatFormatting.RED, InfoHelper.EMPTY_ARRAY, false, ChatFormatting.AQUA, false);
    }
}