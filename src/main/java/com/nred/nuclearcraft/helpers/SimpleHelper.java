package com.nred.nuclearcraft.helpers;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import static java.lang.Math.max;

public class SimpleHelper {
    public static MobEffectInstance newEffect(Holder<MobEffect> effect, int strength, int ticks) {
        return new MobEffectInstance(effect, ticks, max(0, strength - 1));
    }
}