package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.info.Fluids;
import net.neoforged.bus.api.IEventBus;

import static com.nred.nuclearcraft.registration.Registers.*;

public class Registration {
    public static void init(){
        BlockRegistration.init();
        ItemRegistration.init();
        FluidRegistration.init();
        BlockEntityRegistration.init();
        RecipeSerializerRegistration.init();
        RecipeTypeRegistration.init();
    }

    public static void register(IEventBus modEventBus){
        init();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        FLUIDS.register(modEventBus);
        FLUID_TYPES.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}