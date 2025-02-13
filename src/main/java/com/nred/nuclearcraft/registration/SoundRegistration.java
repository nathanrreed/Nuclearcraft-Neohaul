package com.nred.nuclearcraft.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;

import java.util.function.Supplier;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.Registers.SOUND_EVENTS;

public class SoundRegistration {
    public static final Supplier<SoundEvent> WANDERER = SOUND_EVENTS.register("music_disc.wanderer", SoundEvent::createVariableRangeEvent);
    public static final ResourceKey<JukeboxSong> WANDERER_KEY = createSong("wanderer");

    public static final Supplier<SoundEvent> END_OF_THE_WORLD = SOUND_EVENTS.register("music_disc.end_of_the_world", SoundEvent::createVariableRangeEvent);
    public static final ResourceKey<JukeboxSong> END_OF_THE_WORLD_KEY = createSong("end_of_the_world");

    public static final Supplier<SoundEvent> MONEY_FOR_NOTHING = SOUND_EVENTS.register("music_disc.money_for_nothing", SoundEvent::createVariableRangeEvent);
    public static final ResourceKey<JukeboxSong> MONEY_FOR_NOTHING_KEY = createSong("money_for_nothing");

    public static final Supplier<SoundEvent> HYPERSPACE = SOUND_EVENTS.register("music_disc.hyperspace", SoundEvent::createVariableRangeEvent);
    public static final ResourceKey<JukeboxSong> HYPERSPACE_KEY = createSong("hyperspace");

    public static final Supplier<SoundEvent> RAD_POISONING = SOUND_EVENTS.register("rad_poisoning", SoundEvent::createVariableRangeEvent);


    public static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static void init() {
    }
}
