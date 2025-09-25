package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.registration.SoundRegistration;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;

/**
 * Block sound handling - thanks to the Mekanism devs for this system!
 */
@OnlyIn(Dist.CLIENT)
public class SoundHandler {
    protected static final Object2ObjectMap<Vec3, SoundInstance> BLOCK_SOUND_MAP = new Object2ObjectOpenHashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayBlockSound(PlaySoundEvent event) {
        // Ignore any sound event which is null or is happening in a muffled check
        SoundInstance resultSound = event.getSound();
        if (resultSound == null) {
            return;
        }

        // Ignore any unrelated sound event
        SoundInstance originalSound = event.getSound();
        ResourceLocation soundName = originalSound.getLocation();
        if (!SoundRegistration.TRACKABLE_SOUNDS.contains(soundName.toString())) {
            return;
        }

        // Make sure sound accessor is not null
        if (resultSound.getSound() == null) {
            resultSound.resolve(Minecraft.getInstance().getSoundManager());
        }

        // At this point, we've got a known block sound
        resultSound = new BlockSound(originalSound, resultSound.getVolume(), resultSound.getPitch());
        event.setSound(resultSound);

        // Finally, update our soundMap so that we can actually have a shot at stopping this sound
        BLOCK_SOUND_MAP.put(new Vec3(resultSound.getX(), resultSound.getY(), resultSound.getZ()), resultSound);
    }

    public static SoundInstance startBlockSound(SoundEvent soundEvent, BlockPos pos, float volume, float pitch) {
        SoundManager sounds = Minecraft.getInstance().getSoundManager();

        // First, check to see if there's already a sound playing at the desired location
        Vec3 vec = new Vec3(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
        SoundInstance sound = BLOCK_SOUND_MAP.get(vec);
        if (sound == null || !sounds.isActive(sound)) {
            if (sound != null) {
                sounds.stop(sound);
            }

            // No sound playing, start one up
            // We assume that the sound will play until explicitly stopped
            sound = new SimpleSoundInstance(soundEvent.getLocation(), SoundSource.BLOCKS, volume, pitch, Minecraft.getInstance().level.getRandom(), true, 0, SoundInstance.Attenuation.LINEAR, vec.x, vec.y, vec.z, false) {

                @Override
                public float getVolume() {
                    if (sound == null) {
                        resolve(sounds);
                    }
                    return super.getVolume();
                }

                @Override
                public float getPitch() {
                    if (sound == null) {
                        resolve(sounds);
                    }
                    return super.getPitch();
                }
            };

            // Start the sound, firing a PlaySoundEvent
            sounds.play(sound);

            // N.B. By the time playSound returns, our expectation is that our wrapping-detector handler has fired
            // and dealt with any muting interceptions and, CRITICALLY, updated the soundMap with the final SoundInstance.
            sound = BLOCK_SOUND_MAP.get(vec);
        }
        return sound;
    }

    public static void stopBlockSound(BlockPos pos) {
        Vec3 vec = new Vec3(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
        SoundInstance sound = BLOCK_SOUND_MAP.get(vec);
        if (sound != null) {
            Minecraft.getInstance().getSoundManager().stop(sound);
            BLOCK_SOUND_MAP.remove(vec);
        }
    }

    protected static class BlockSound implements SoundInstance {

        private final SoundInstance sound;
        private final float volume, pitch;

        BlockSound(SoundInstance sound, float volume, float pitch) {
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
        }

        @Override
        public ResourceLocation getLocation() {
            return sound.getLocation();
        }

        @Override
        public @org.jetbrains.annotations.Nullable WeighedSoundEvents resolve(SoundManager manager) {
            return sound.resolve(manager);
        }

        @Override
        public Sound getSound() {
            return sound.getSound();
        }

        @Override
        public SoundSource getSource() {
            return sound.getSource();
        }

        @Override
        public boolean isLooping() {
            return sound.isLooping();
        }

        @Override
        public boolean isRelative() {
            return sound.isRelative();
        }

        @Override
        public int getDelay() {
            return sound.getDelay();
        }

//		@Override
//		public SoundCategory getCategory() {
//			return sound.getCategory();
//		}

        @Override
        public float getVolume() {
            return volume;
        }

        @Override
        public float getPitch() {
            return pitch;
        }

        @Override
        public double getX() {
            return sound.getX();
        }

        @Override
        public double getY() {
            return sound.getY();
        }

        @Override
        public double getZ() {
            return sound.getZ();
        }

        @Override
        public Attenuation getAttenuation() {
            return sound.getAttenuation();
        }
    }
}
