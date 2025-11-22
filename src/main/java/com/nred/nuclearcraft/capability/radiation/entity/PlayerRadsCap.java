package com.nred.nuclearcraft.capability.radiation.entity;

import net.minecraft.world.entity.LivingEntity;

import static com.nred.nuclearcraft.registration.AttachmentRegistration.PLAYER_RADIATION;

public class PlayerRadsCap implements IEntityRads {
    protected final LivingEntity entity;

    public PlayerRadsCap(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public double getTotalRads() {
        return entity.getData(PLAYER_RADIATION).getTotalRads();
    }

    @Override
    public void setTotalRads(double newTotalRads, boolean useImmunity) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setTotalRads(newTotalRads, useImmunity);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getMaxRads() {
        return entity.getData(PLAYER_RADIATION).getMaxRads();

    }

    @Override
    public double getRadiationLevel() {
        return entity.getData(PLAYER_RADIATION).getRadiationLevel();

    }

    @Override
    public void setRadiationLevel(double newRadiationLevel) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRadiationLevel(newRadiationLevel);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getInternalRadiationResistance() {
        return entity.getData(PLAYER_RADIATION).getInternalRadiationResistance();

    }

    @Override
    public void setInternalRadiationResistance(double newInternalRadiationResistance) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setInternalRadiationResistance(newInternalRadiationResistance);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getExternalRadiationResistance() {
        return entity.getData(PLAYER_RADIATION).getExternalRadiationResistance();
    }

    @Override
    public void setExternalRadiationResistance(double newExternalRadiationResistance) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setExternalRadiationResistance(newExternalRadiationResistance);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public boolean getRadXUsed() {
        return entity.getData(PLAYER_RADIATION).getRadXUsed();
    }

    @Override
    public void setRadXUsed(boolean radXUsed) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRadXUsed(radXUsed);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public boolean getRadXWoreOff() {
        return entity.getData(PLAYER_RADIATION).getRadXWoreOff();
    }

    @Override
    public void setRadXWoreOff(boolean radXWoreOff) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRadXWoreOff(radXWoreOff);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getRadawayBuffer(boolean slow) {
        return entity.getData(PLAYER_RADIATION).getRadawayBuffer(slow);
    }

    @Override
    public void setRadawayBuffer(boolean slow, double newBuffer) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRadawayBuffer(slow, newBuffer);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getPoisonBuffer() {
        return entity.getData(PLAYER_RADIATION).getPoisonBuffer();
    }

    @Override
    public void setPoisonBuffer(double newBuffer) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setPoisonBuffer(newBuffer);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public boolean getConsumedMedicine() {
        return entity.getData(PLAYER_RADIATION).getConsumedMedicine();
    }

    @Override
    public void setConsumedMedicine(boolean consumed) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setConsumedMedicine(consumed);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getRadawayCooldown() {
        return entity.getData(PLAYER_RADIATION).getRadawayCooldown();
    }

    @Override
    public void setRadawayCooldown(double cooldown) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRadawayCooldown(cooldown);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public boolean canConsumeRadaway() {
        return entity.getData(PLAYER_RADIATION).canConsumeRadaway();
    }

    @Override
    public double getRecentRadawayAddition() {
        return entity.getData(PLAYER_RADIATION).getRecentRadawayAddition();
    }

    @Override
    public void setRecentRadawayAddition(double newRecentRadawayAddition) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRecentRadawayAddition(newRecentRadawayAddition);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public void resetRecentRadawayAddition() {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.resetRecentRadawayAddition();
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getRadXCooldown() {
        return entity.getData(PLAYER_RADIATION).getRadXCooldown();
    }

    @Override
    public void setRadXCooldown(double cooldown) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRadXCooldown(cooldown);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public boolean canConsumeRadX() {
        return entity.getData(PLAYER_RADIATION).canConsumeRadX();
    }

    @Override
    public double getRecentRadXAddition() {
        return entity.getData(PLAYER_RADIATION).getRecentRadXAddition();
    }

    @Override
    public void setRecentRadXAddition(double newRecentRadXAddition) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRecentRadXAddition(newRecentRadXAddition);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public void resetRecentRadXAddition() {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.resetRecentRadXAddition();
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public int getMessageCooldownTime() {
        return entity.getData(PLAYER_RADIATION).getMessageCooldownTime();
    }

    @Override
    public void setMessageCooldownTime(int messageTime) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setMessageCooldownTime(messageTime);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getRecentPoisonAddition() {
        return entity.getData(PLAYER_RADIATION).getRecentPoisonAddition();
    }

    @Override
    public void setRecentPoisonAddition(double newRecentPoisonAddition) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRecentPoisonAddition(newRecentPoisonAddition);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public void resetRecentPoisonAddition() {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.resetRecentPoisonAddition();
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public double getRadiationImmunityTime() {
        return entity.getData(PLAYER_RADIATION).getRadiationImmunityTime();
    }

    @Override
    public void setRadiationImmunityTime(double newRadiationImmunityTime) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRadiationImmunityTime(newRadiationImmunityTime);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public boolean getRadiationImmunityStage() {
        return entity.getData(PLAYER_RADIATION).getRadiationImmunityStage();
    }

    @Override
    public void setRadiationImmunityStage(boolean newRadiationImmunityStage) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setRadiationImmunityStage(newRadiationImmunityStage);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public boolean getShouldWarn() {
        return entity.getData(PLAYER_RADIATION).getShouldWarn();
    }

    @Override
    public void setShouldWarn(boolean shouldWarn) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setShouldWarn(shouldWarn);
        entity.setData(PLAYER_RADIATION, rads);
    }

    @Override
    public boolean getGiveGuidebook() {
        return entity.getData(PLAYER_RADIATION).getGiveGuidebook();
    }

    @Override
    public void setGiveGuidebook(boolean giveGuidebook) {
        PlayerRads rads = entity.getData(PLAYER_RADIATION);
        rads.setGiveGuidebook(giveGuidebook);
        entity.setData(PLAYER_RADIATION, rads);
    }
}