package com.nred.nuclearcraft.registration;

import net.neoforged.bus.api.IEventBus;

import static com.nred.nuclearcraft.registration.Registers.*;

public class Registration {
    public static void init(){
        BlockRegistration.init();
        ItemRegistration.init();
    }

    public static void register(IEventBus modEventBus){
        init();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}