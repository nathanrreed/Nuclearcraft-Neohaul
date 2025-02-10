package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.block.collector.cobblestone_generator.CobbleGeneratorEntity;
import com.nred.nuclearcraft.block.collector.nitrogen_collector.NitrogenCollectorEntity;
import com.nred.nuclearcraft.block.collector.water_source.WaterSourceEntity;
import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.config.ProcessorConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class CapabilityRegistration {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COBBLE_GENERATOR_TYPES.get(level).get(), (machineEntity, direction) -> ((CobbleGeneratorEntity) machineEntity).itemStackHandler);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, WATER_SOURCE_TYPES.get(level).get(), (machineEntity, direction) -> ((WaterSourceEntity) machineEntity).fluidStackHandler);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, NITROGEN_COLLECTOR_TYPES.get(level).get(), (machineEntity, direction) -> ((NitrogenCollectorEntity) machineEntity).fluidStackHandler);
        }

        for (String typeName : PROCESSOR_MAP.keySet()) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(), ProcessorEntity::getItemHandler);
            ProcessorConfig config = PROCESSOR_CONFIG_MAP.get(typeName);
            if (config.capacity() > 0)
                event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(),ProcessorEntity::getEnergyHandler);

            if (config.fluid_capacity() > 0)
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(),ProcessorEntity::getFluidHandler);
        }
    }
}