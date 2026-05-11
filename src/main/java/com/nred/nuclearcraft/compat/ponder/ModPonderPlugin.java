package com.nred.nuclearcraft.compat.ponder;

import com.nred.nuclearcraft.datamap.FissionModeratorData;
import com.nred.nuclearcraft.datamap.FissionReflectorData;
import com.nred.nuclearcraft.util.DataMapHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;

import java.util.Objects;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.FISSION_MODERATOR_DATA;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.FISSION_REFLECTOR_DATA;
import static com.nred.nuclearcraft.registration.ItemRegistration.PELLET_URANIUM_MAP;
import static net.minecraft.world.level.block.LeverBlock.POWERED;
import static net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent.UpdateCause.CLIENT_SYNC;

public class ModPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        BuiltInRegistries.BLOCK.getDataMap(FISSION_MODERATOR_DATA).forEach((key, value) -> helper.addStoryBoard(key.location(), ncLoc("basic"), ((scene, util) -> moderator(scene, util, Objects.requireNonNull(BuiltInRegistries.BLOCK.get(key)), value))));
        BuiltInRegistries.BLOCK.getDataMap(FISSION_REFLECTOR_DATA).forEach((key, value) -> helper.addStoryBoard(key.location(), ncLoc("basic"), ((scene, util) -> reflector(scene, util, Objects.requireNonNull(BuiltInRegistries.BLOCK.get(key)), value))));
    }

    public static void moderator(SceneBuilder scene, SceneBuildingUtil util, Block moderator, FissionModeratorData data) {
        scene.title(ncLoc("moderator").getPath(), "Moderator");

        scene.world().showSection(util.select().everywhere(), Direction.DOWN);
        scene.idle(5);
        scene.world().setBlock(util.grid().at(1, 1, 2), FISSION_REACTOR_MAP.get("fission_fuel_cell").get().defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(2, 1, 2), moderator.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(3, 1, 2), moderator.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(1, 1, 1), FISSION_REACTOR_MAP.get("radium_beryllium_source").get().defaultBlockState().setValue(FACING_HORIZONTAL, Direction.SOUTH).setValue(ACTIVE, true), false);
        scene.world().setBlock(util.grid().at(1, 1, 0), Blocks.LEVER.defaultBlockState().setValue(POWERED, true), false);

        scene.overlay().showControls(util.grid().at(1, 1, 2).getCenter(), Pointing.UP, 45).withItem(PELLET_URANIUM_MAP.get("leu_235").toStack());
        scene.overlay().showOutlineWithText(util.select().position(2, 1, 2), 20).sharedText("moderator.active").colored(PonderPalette.INPUT);
        scene.idle(25);
        scene.overlay().showOutlineWithText(util.select().position(3, 1, 2), 20).sharedText("moderator.inactive").colored(PonderPalette.RED);
        scene.addKeyframe();
        scene.idle(40);

        scene.overlay().showOutlineWithText(util.select().fromTo(2, 1, 2, 3, 1, 2), 20).sharedText("moderator.inactive").colored(PonderPalette.RED);
        scene.addKeyframe();
        scene.idle(20);
        scene.addKeyframe();

        scene.markAsFinished();
    }

    public static void reflector(SceneBuilder scene, SceneBuildingUtil util, Block reflector, FissionReflectorData data) {
        scene.title(ncLoc("reflector").getPath(), "Reflector");

        Block moderator = DataMapHelper.getFirst(FISSION_MODERATOR_DATA);
        scene.world().showSection(util.select().everywhere(), Direction.DOWN);
        scene.idle(5);
        scene.world().setBlock(util.grid().at(1, 1, 2), reflector.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(2, 1, 2), moderator.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(3, 1, 2), moderator.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(4, 1, 2), FISSION_REACTOR_MAP.get("fission_fuel_cell").get().defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(4, 1, 1), FISSION_REACTOR_MAP.get("radium_beryllium_source").get().defaultBlockState().setValue(FACING_HORIZONTAL, Direction.SOUTH).setValue(ACTIVE, true), false);
        scene.world().setBlock(util.grid().at(4, 1, 0), Blocks.LEVER.defaultBlockState().setValue(POWERED, true), false);

        scene.markAsFinished();
    }

    @Mod(value = MODID, dist = Dist.CLIENT)
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ModPondersClient {

        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            PonderIndex.addPlugin(new ModPonderPlugin());
        }

        @SubscribeEvent
        static void onDataMapChanged(DataMapsUpdatedEvent event) {
            if (event.getCause().equals(CLIENT_SYNC)) {
                PonderIndex.reload();
            }
        }
    }
}