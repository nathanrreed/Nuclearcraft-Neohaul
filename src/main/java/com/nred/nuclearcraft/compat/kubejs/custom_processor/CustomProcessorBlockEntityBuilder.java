package com.nred.nuclearcraft.compat.kubejs.custom_processor;

import com.nred.nuclearcraft.block_entity.processor.ProcessorEntityImpl;
import dev.latvian.mods.kubejs.block.entity.BlockEntityBuilder;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.handler.BlockEntityInfoHandler.getProcessorMenuUpgradable;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.PROCESSOR_ENTITY_TYPE;

@ReturnsSelf
public class CustomProcessorBlockEntityBuilder extends BlockEntityBuilder {
    public CustomProcessorBlockEntityBuilder(ResourceLocation i, BlockEntityInfo info) {
        super(i, info);
    }

    @Override
    public BlockEntityType<?> createObject() {
        String name = id.toString();
        if (getProcessorMenuUpgradable(name)) {
            info.entityType = BlockEntityType.Builder.of((pos, blockState) -> new ProcessorEntityImpl.BasicUpgradableEnergyProcessorEntityDyn(pos, blockState, name), info.blockBuilder.get()).build(null);
        } else {
            info.entityType = BlockEntityType.Builder.of((pos, blockState) -> new ProcessorEntityImpl.BasicEnergyProcessorEntityDyn(pos, blockState, name), info.blockBuilder.get()).build(null);
        }
        PROCESSOR_ENTITY_TYPE.put(name, DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, this.id));
        return info.entityType;
    }
}