package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.multiblock.PlacementRule;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

@EventBusSubscriber(modid = MODID)
public class CommonSetup {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) { // TODO is this the right event type
        PlacementRule.init();
    }

    @SubscribeEvent
    public static void postInit(ServerStartingEvent event) { // TODO is this the right event type
        PlacementRule.postInit();
    }
}
