package com.nred.nuclearcraft.item.armour;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class HazmatSuitItem extends ArmorItem {
    public final double radiationProtection;

    public HazmatSuitItem(Holder<ArmorMaterial> materialIn, ArmorItem.Type type, Properties properties, double radiationProtection) {
        super(materialIn, type, properties);
        this.radiationProtection = radiationProtection;
    }
}