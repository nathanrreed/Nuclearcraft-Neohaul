package com.nred.nuclearcraft.entity;

import com.nred.nuclearcraft.entity.ai.FeralGhoulLeapGoal;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.nred.nuclearcraft.config.NCConfig.radiation_sound_volumes;
import static com.nred.nuclearcraft.registration.SoundRegistration.*;
import static net.minecraft.world.entity.ai.attributes.Attributes.SPAWN_REINFORCEMENTS_CHANCE;

public class FeralGhoul extends Zombie {
    private static final EntityDataAccessor<Byte> DATA_CLIMBING_ID = SynchedEntityData.defineId(FeralGhoul.class, EntityDataSerializers.BYTE);

    public FeralGhoul(EntityType<FeralGhoul> entityType, Level level) {
        super((EntityType<FeralGhoul>) entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CLIMBING_ID, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FeralGhoulLeapGoal(this));
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(ZombifiedPiglin.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4f)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4)
                .add(Attributes.ARMOR, 4.0)
                .add(SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return feral_ghoul_charge.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return feral_ghoul_death.get();
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        playSound(getHurtSound(source), getSoundVolume(), getVoicePitch() - 0.3F);
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean attackedRecently) {
        super.dropFromLootTable(damageSource, attackedRecently);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            setClimbing(horizontalCollision);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean flag = super.doHurtTarget(entity);

        if (flag && entity instanceof LivingEntity target && !(entity instanceof Mob)) {
            int mult = NCMath.toInt(30F * Mth.clamp(level().getCurrentDifficultyAt(this.getOnPos()).getEffectiveDifficulty(), 1F, 2.5F));
            target.addEffect(new MobEffectInstance(MobEffects.POISON, mult));

//            IEntityRads entityRads = RadiationHelper.getEntityRadiation(target); TODO
//            if (entityRads != null) {
//                double attackRadiation = NCConfig.radiation_feral_ghoul_attack * mult;
//                entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() + attackRadiation);
//                entityRads.setRecentPoisonAddition(attackRadiation);
//                playSound(rad_poisoning.get(), (float) (1.35D * radiation_sound_volumes[7]), 1F + 0.2F * (random.nextFloat() - random.nextFloat()));
//            }

            playSound(rad_poisoning.get(), (float) (1.35D * radiation_sound_volumes[7]), 1F + 0.2F * (random.nextFloat() - random.nextFloat())); // TODO REMOVE after radiation added
        }

        return flag;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    public int getMaxFallDistance() {
        return 255;
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_CLIMBING_ID) & 1) != 0;
    }

    public void setClimbing(boolean climbing) {
        byte b0 = this.getEntityData().get(DATA_CLIMBING_ID);

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.getEntityData().set(DATA_CLIMBING_ID, b0);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean childZombie) {
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}