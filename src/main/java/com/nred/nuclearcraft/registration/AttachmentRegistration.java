package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.capability.radiation.entity.EntityRads;
import com.nred.nuclearcraft.capability.radiation.entity.PlayerRads;
import com.nred.nuclearcraft.capability.radiation.source.RadiationSource;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.Registers.ATTACHMENT_TYPES;

public class AttachmentRegistration {
    public static final Supplier<AttachmentType<RadiationSource>> RADIATION_SOURCE = ATTACHMENT_TYPES.register("radiation_source", () -> AttachmentType.serializable(() -> new RadiationSource(0.0)).build());

    public static final Supplier<AttachmentType<PlayerRads>> PLAYER_RADIATION = ATTACHMENT_TYPES.register("player_radiation", () -> AttachmentType.serializable(PlayerRads::new).build());
    public static final Supplier<AttachmentType<EntityRads>> ENTITY_RADIATION = ATTACHMENT_TYPES.register("entity_radiation", () -> AttachmentType.serializable(EntityRads::new).build());

    public static void init() {
    }
}