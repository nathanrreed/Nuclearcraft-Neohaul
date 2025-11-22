package com.nred.nuclearcraft.payload.radiation;

import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.CapabilityRegistration.CAPABILITY_ENTITY_RADS;

public class PlayerRadsUpdatePacket extends NCPacket {
    public static final Type<PlayerRadsUpdatePacket> TYPE = new Type<>(ncLoc("player_rads_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerRadsUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            PlayerRadsUpdatePacket::toBytes, PlayerRadsUpdatePacket::fromBytes
    );

    protected double totalRads;
    protected double radiationLevel;
    protected double internalRadiationResistance, externalRadiationResistance;
    protected boolean radXUsed;
    protected boolean radXWoreOff;
    protected double radawayBuffer, radawayBufferSlow;
    protected double poisonBuffer;
    protected boolean consumed;
    protected double radawayCooldown;
    protected double recentRadawayAddition;
    protected double radXCooldown;
    protected double recentRadXAddition;
    protected int messageCooldownTime;
    protected double recentPoisonAddition;
    protected double radiationImmunityTime;
    protected boolean radiationImmunityStage;
    protected boolean shouldWarn;
    protected boolean giveGuidebook;

    public PlayerRadsUpdatePacket(IEntityRads playerRads) {
        totalRads = playerRads.getTotalRads();
        radiationLevel = playerRads.getRadiationLevel();
        internalRadiationResistance = playerRads.getInternalRadiationResistance();
        externalRadiationResistance = playerRads.getExternalRadiationResistance();
        radXUsed = playerRads.getRadXUsed();
        radXWoreOff = playerRads.getRadXWoreOff();
        radawayBuffer = playerRads.getRadawayBuffer(false);
        radawayBufferSlow = playerRads.getRadawayBuffer(true);
        poisonBuffer = playerRads.getPoisonBuffer();
        consumed = playerRads.getConsumedMedicine();
        radawayCooldown = playerRads.getRadawayCooldown();
        recentRadawayAddition = playerRads.getRecentRadawayAddition();
        radXCooldown = playerRads.getRadXCooldown();
        recentRadXAddition = playerRads.getRecentRadXAddition();
        messageCooldownTime = playerRads.getMessageCooldownTime();
        recentPoisonAddition = playerRads.getRecentPoisonAddition();
        radiationImmunityTime = playerRads.getRadiationImmunityTime();
        radiationImmunityStage = playerRads.getRadiationImmunityStage();
        shouldWarn = playerRads.getShouldWarn();
        giveGuidebook = playerRads.getGiveGuidebook();
    }

    public PlayerRadsUpdatePacket(double totalRads, double radiationLevel, double internalRadiationResistance, double externalRadiationResistance,
                                  boolean radXUsed, boolean radXWoreOff, double radawayBuffer, double radawayBufferSlow, double poisonBuffer, boolean consumed,
                                  double radawayCooldown, double recentRadawayAddition, double radXCooldown, double recentRadXAddition, int messageCooldownTime,
                                  double recentPoisonAddition, double radiationImmunityTime, boolean radiationImmunityStage, boolean shouldWarn, boolean giveGuidebook) {

        this.totalRads = totalRads;
        this.radiationLevel = radiationLevel;
        this.internalRadiationResistance = internalRadiationResistance;
        this.externalRadiationResistance = externalRadiationResistance;
        this.radXUsed = radXUsed;
        this.radXWoreOff = radXWoreOff;
        this.radawayBuffer = radawayBuffer;
        this.radawayBufferSlow = radawayBufferSlow;
        this.poisonBuffer = poisonBuffer;
        this.consumed = consumed;
        this.radawayCooldown = radawayCooldown;
        this.recentRadawayAddition = recentRadawayAddition;
        this.radXCooldown = radXCooldown;
        this.recentRadXAddition = recentRadXAddition;
        this.messageCooldownTime = messageCooldownTime;
        this.recentPoisonAddition = recentPoisonAddition;
        this.radiationImmunityTime = radiationImmunityTime;
        this.radiationImmunityStage = radiationImmunityStage;
        this.shouldWarn = shouldWarn;
        this.giveGuidebook = giveGuidebook;
    }

    public static PlayerRadsUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        double totalRads = buf.readDouble();
        double radiationLevel = buf.readDouble();
        double internalRadiationResistance = buf.readDouble();
        double externalRadiationResistance = buf.readDouble();
        boolean radXUsed = buf.readBoolean();
        boolean radXWoreOff = buf.readBoolean();
        double radawayBuffer = buf.readDouble();
        double radawayBufferSlow = buf.readDouble();
        double poisonBuffer = buf.readDouble();
        boolean consumed = buf.readBoolean();
        double radawayCooldown = buf.readDouble();
        double recentRadawayAddition = buf.readDouble();
        double radXCooldown = buf.readDouble();
        double recentRadXAddition = buf.readDouble();
        int messageCooldownTime = buf.readInt();
        double recentPoisonAddition = buf.readDouble();
        double radiationImmunityTime = buf.readDouble();
        boolean radiationImmunityStage = buf.readBoolean();
        boolean shouldWarn = buf.readBoolean();
        boolean giveGuidebook = buf.readBoolean();
        return new PlayerRadsUpdatePacket(totalRads, radiationLevel, internalRadiationResistance, externalRadiationResistance, radXUsed, radXWoreOff, radawayBuffer, radawayBufferSlow,
                poisonBuffer, consumed, radawayCooldown, recentRadawayAddition, radXCooldown, recentRadXAddition, messageCooldownTime, recentPoisonAddition,
                radiationImmunityTime, radiationImmunityStage, shouldWarn, giveGuidebook);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(totalRads);
        buf.writeDouble(radiationLevel);
        buf.writeDouble(internalRadiationResistance);
        buf.writeDouble(externalRadiationResistance);
        buf.writeBoolean(radXUsed);
        buf.writeBoolean(radXWoreOff);
        buf.writeDouble(radawayBuffer);
        buf.writeDouble(radawayBufferSlow);
        buf.writeDouble(poisonBuffer);
        buf.writeBoolean(consumed);
        buf.writeDouble(radawayCooldown);
        buf.writeDouble(recentRadawayAddition);
        buf.writeDouble(radXCooldown);
        buf.writeDouble(recentRadXAddition);
        buf.writeInt(messageCooldownTime);
        buf.writeDouble(recentPoisonAddition);
        buf.writeDouble(radiationImmunityTime);
        buf.writeBoolean(radiationImmunityStage);
        buf.writeBoolean(shouldWarn);
        buf.writeBoolean(giveGuidebook);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler {
        public static void handleOnClient(PlayerRadsUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) {
                    return;
                }

                IEntityRads playerRads = player.getCapability(CAPABILITY_ENTITY_RADS, null);
                if (playerRads == null) {
                    return;
                }

                playerRads.setTotalRads(payload.totalRads, false);
                playerRads.setRadiationLevel(payload.radiationLevel);
                playerRads.setInternalRadiationResistance(payload.internalRadiationResistance);
                playerRads.setExternalRadiationResistance(payload.externalRadiationResistance);
                playerRads.setRadXUsed(payload.radXUsed);
                playerRads.setRadXWoreOff(payload.radXWoreOff);
                playerRads.setRadawayBuffer(false, payload.radawayBuffer);
                playerRads.setRadawayBuffer(true, payload.radawayBufferSlow);
                playerRads.setPoisonBuffer(payload.poisonBuffer);
                playerRads.setConsumedMedicine(payload.consumed);
                playerRads.setRadawayCooldown(payload.radawayCooldown);
                playerRads.setRecentRadawayAddition(payload.recentRadawayAddition);
                playerRads.setRadXCooldown(payload.radXCooldown);
                playerRads.setRecentRadXAddition(payload.recentRadXAddition);
                playerRads.setMessageCooldownTime(payload.messageCooldownTime);
                playerRads.setRecentPoisonAddition(payload.recentPoisonAddition);
                playerRads.setRadiationImmunityTime(payload.radiationImmunityTime);
                playerRads.setRadiationImmunityStage(payload.radiationImmunityStage);
                playerRads.setShouldWarn(payload.shouldWarn);
                playerRads.setGiveGuidebook(payload.giveGuidebook);
            });
        }
    }
}