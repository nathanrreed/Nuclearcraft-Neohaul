package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.PlayerRespawnHandler;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.recipe.RecipeStats;
import com.nred.nuclearcraft.util.ModCheck;
import com.nred.nuclearcraft.worldgen.biome.NuclearWastelandBiome;
import com.nred.nuclearcraft.worldgen.region.NuclearWastelandRegion;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.EntityRegistration.FERAL_GHOUL;
import static com.nred.nuclearcraft.registration.ItemRegistration.FERAL_GHOUL_SPAWN_EGG;

@EventBusSubscriber(modid = MODID)
public class CommonSetup {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) { // TODO is this the right event type
        ModCheck.init();

        PlacementRule.init();
        MultitoolItem.registerRightClickLogic();

        TileInfoHandler.init();

        // TerraBlender
        Regions.register(new NuclearWastelandRegion(ncLoc("nuclear_wasteland"), 2));
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, NuclearWastelandBiome.makeRules());
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, NuclearWastelandBiome.makeRules());
    }

    @SubscribeEvent
    public static void postInit(ServerAboutToStartEvent event) { // TODO is this the right event type
        PlacementRule.postInit();
        NCRecipes.init(event.getServer().getRecipeManager());
        RecipeStats.init();

        NeoForge.EVENT_BUS.register(new PlayerRespawnHandler());
    }

    @SubscribeEvent
    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(FERAL_GHOUL_SPAWN_EGG);
        }
    }

    @SubscribeEvent
    public static void addSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(FERAL_GHOUL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }
}