package com.nred.nuclearcraft.config;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.CATEGORY_MISC;
import static com.nred.nuclearcraft.config.NCConfig.add;

@EventBusSubscriber(modid = MODID)
public class NCServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static double create_bearing_ang_vel_to_speed;
    public static double create_bearing_max_stress;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLoad(final ModConfigEvent event) {
        if (event instanceof ModConfigEvent.Unloading || event.getConfig().getSpec() != SPEC)
            return;
        loadConfig();
    }

    private static final ModConfigSpec.DoubleValue CREATE_BEARING_ANG_VEL_TO_SPEED = add(BUILDER, CATEGORY_MISC, "create_bearing_ang_vel_to_speed", 80.0, 0.0, 10000.0);
    private static final ModConfigSpec.DoubleValue CREATE_BEARING_MAX_STRESS = add(BUILDER, CATEGORY_MISC, "create_bearing_max_stress", 1024.0, 0.0, 100000.0);

    public static void loadConfig() {
        create_bearing_ang_vel_to_speed = CREATE_BEARING_ANG_VEL_TO_SPEED.getAsDouble();
        create_bearing_max_stress = CREATE_BEARING_MAX_STRESS.getAsDouble();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}