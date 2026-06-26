package com.nred.nuclearcraft.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.List;
import java.util.Optional;

public class NCFoodItem extends NCItem {
    public NCFoodItem(int amount, float saturation, List<MobEffectInstance> effects, String tooltip) {
        super(new Properties().food(new FoodProperties(amount, saturation, false, 1.6F, Optional.empty(), effects.stream().map(effect -> new FoodProperties.PossibleEffect(() -> effect, 1f)).toList())), tooltip.isEmpty() ? List.of() : List.of(tooltip));
    }

    public NCFoodItem(int amount, float saturation, List<MobEffectInstance> effects) {
        this(amount, saturation, effects, "");
    }

    public NCFoodItem(int amount, float saturation) {
        this(amount, saturation, List.of(), "");
    }
}