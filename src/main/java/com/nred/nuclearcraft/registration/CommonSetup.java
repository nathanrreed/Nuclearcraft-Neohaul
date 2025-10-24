package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.recipe.RecipeStats;
import com.nred.nuclearcraft.util.ModCheck;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

@EventBusSubscriber(modid = MODID)
public class CommonSetup {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) { // TODO is this the right event type
        ModCheck.init();

        PlacementRule.init();
        MultitoolItem.registerRightClickLogic();

        TileInfoHandler.init();
    }

    @SubscribeEvent
    public static void postInit(ServerAboutToStartEvent event) { // TODO is this the right event type
        PlacementRule.postInit();
        NCRecipes.init(event.getServer().getRecipeManager());
        RecipeStats.init();
    }
}