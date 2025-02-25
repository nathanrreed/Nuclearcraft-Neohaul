package com.nred.nuclearcraft.registration;

import net.neoforged.bus.api.IEventBus;

import static com.nred.nuclearcraft.registration.Registers.*;

public class Registration {
    public static void init() {
        DamageTypeRegistration.init();
        BlockRegistration.init();
        ItemRegistration.init();
        FluidRegistration.init();
        BlockEntityRegistration.init();
        RecipeSerializerRegistration.init();
        RecipeTypeRegistration.init();
        MenuRegistration.init();
        CreativeTabsRegistration.init();
        SoundRegistration.init();
    }

    public static void register(IEventBus modEventBus) {
        init();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        FLUIDS.register(modEventBus);
        FLUID_TYPES.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        MENUS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
    }
}