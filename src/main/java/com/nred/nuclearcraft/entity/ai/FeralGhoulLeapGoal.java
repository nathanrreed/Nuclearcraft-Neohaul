package com.nred.nuclearcraft.entity.ai;

import com.nred.nuclearcraft.entity.FeralGhoul;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static com.nred.nuclearcraft.registration.SoundRegistration.feral_ghoul_charge;

public class FeralGhoulLeapGoal extends Goal {
    private final FeralGhoul ghoul;
    private LivingEntity leapTarget;

    public FeralGhoulLeapGoal(FeralGhoul ghoul) {
        this.ghoul = ghoul;
        setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        this.leapTarget = this.ghoul.getTarget();

        if (this.leapTarget == null) {
            return false;
        } else {
            double distance = this.ghoul.distanceToSqr(this.leapTarget);
            if (distance > 16.0 && distance < 64.0) {
                return this.ghoul.onGround() && this.ghoul.getRandom().nextInt(4) == 0;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.ghoul.onGround();
    }

    @Override
    public void start() {
        Vec3 vec3 = this.ghoul.getDeltaMovement();
        Vec3 motion = new Vec3(this.leapTarget.getX() - this.ghoul.getX(), 0.0, this.leapTarget.getZ() - this.ghoul.getZ());
        if (motion.lengthSqr() > 1.0E-7) {
            motion = motion.normalize().scale(1.25).add(vec3.scale(0.2));
        }

        double y_motion = 0.5;
        if (ghoul.hasEffect(MobEffects.JUMP)) {
            MobEffectInstance jumpBoost = ghoul.getEffect(MobEffects.JUMP);
            if (jumpBoost != null) {
                y_motion += (jumpBoost.getAmplifier() + 1) * 0.1;
            }
        }

        this.ghoul.setDeltaMovement(motion.x, y_motion, motion.z);

        this.ghoul.playSound(feral_ghoul_charge.get(), 1F, 1F + 0.2F * (this.ghoul.getRandom().nextFloat() - this.ghoul.getRandom().nextFloat()));
    }
}