package com.nred.nuclearcraft.compat.cct;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_COMPUTER_PORT;

public class RegisterPeripherals {
    public static void registerPeripherals(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(PeripheralCapability.get(), TURBINE_COMPUTER_PORT.get(), (blockEntity, context) -> new TurbinePeripheral(blockEntity));
    }
}