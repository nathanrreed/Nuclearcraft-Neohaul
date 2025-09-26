package com.nred.nuclearcraft.compat.cct;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.PROCESSOR_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_COMPUTER_PORT;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

public class RegisterPeripherals {
    public static void registerPeripherals(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(PeripheralCapability.get(), TURBINE_COMPUTER_PORT.get(), (blockEntity, context) -> new TurbinePeripheral(blockEntity));

        for (String typeName : PROCESSOR_MAP.keySet()) {
            event.registerBlockEntity(PeripheralCapability.get(), PROCESSOR_ENTITY_TYPE.get(typeName).get(), (blockEntity, context) -> new ProcessorPeripheral(blockEntity));
        }
    }
}