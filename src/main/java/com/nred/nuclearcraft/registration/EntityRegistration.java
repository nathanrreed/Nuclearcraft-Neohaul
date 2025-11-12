package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.entity.FeralGhoul;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.Registers.ENTITY_TYPES;

public class EntityRegistration {
    public static final Supplier<EntityType<FeralGhoul>> FERAL_GHOUL = ENTITY_TYPES.register("feral_ghoul", () -> EntityType.Builder.of(FeralGhoul::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8).build("feral_ghoul"));

    public static void init() {
    }
}