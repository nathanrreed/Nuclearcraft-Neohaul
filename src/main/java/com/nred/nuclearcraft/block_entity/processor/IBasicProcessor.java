package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicProcessorMenuInfo;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IBasicProcessor<TILE extends BlockEntity & IBasicProcessor<TILE, PACKET, RECIPE>, PACKET extends ProcessorUpdatePacket, RECIPE extends BasicRecipe> extends IProcessor<TILE, PACKET, BasicProcessorMenuInfo<TILE, PACKET, RECIPE>, RECIPE> {
}