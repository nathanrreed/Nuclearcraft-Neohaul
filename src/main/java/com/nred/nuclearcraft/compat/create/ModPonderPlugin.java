package com.nred.nuclearcraft.compat.create;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.create.CreateRegistration.CREATE_TURBINE_ROTOR_BEARING;

public class ModPonderPlugin implements PonderPlugin {
    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(CREATE_TURBINE_ROTOR_BEARING.getId())
                .addStoryBoard("bearing_ponder", (builder, util) -> {
                    CreateSceneBuilder scene = new CreateSceneBuilder(builder);
                    scene.title("bearing_ponder", "Setting up Create Turbine Bearing Attachment");
                    scene.configureBasePlate(0, 0, 7);
                    scene.rotateCameraY(90);
                    scene.world().showSection(util.select().layers(0, 7), Direction.UP);
                });
    }

    @Override
    public String getModId() {
        return MODID;
    }

    public static void init() {
        PonderIndex.addPlugin(new ModPonderPlugin());
    }
}