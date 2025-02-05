package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.collector.CobbleGeneratorEntity;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.block.collector.NitrogenCollectorEntity;
import com.nred.nuclearcraft.block.collector.WaterSourceEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class CapabilityRegistration {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COBBLE_GENERATOR_TYPES.get(level).get(), (machineEntity, direction) -> ((CobbleGeneratorEntity) machineEntity).itemStackHandler);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, WATER_SOURCE_TYPES.get(level).get(), (machineEntity, direction) -> ((WaterSourceEntity) machineEntity).fluidStackHandler);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, NITROGEN_COLLECTOR_TYPES.get(level).get(), (machineEntity, direction) -> ((NitrogenCollectorEntity) machineEntity).fluidStackHandler);
        }
    }
}