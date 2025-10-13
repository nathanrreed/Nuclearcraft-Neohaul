package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.item.MultiToolItem;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

@EventBusSubscriber(modid = MODID)
public class CommonSetup {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) { // TODO is this the right event type
        PlacementRule.init();
        MultiToolItem.registerRightClickLogic();
//        NeoForge.EVENT_BUS.register(new NCRecipes());
    }

    @SubscribeEvent
    public static void postInit(ServerStartingEvent event) { // TODO is this the right event type
        PlacementRule.postInit();
        NCRecipes.init(event.getServer().getRecipeManager());
    }
}