package com.nred.nuclearcraft.registration;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.Registers.ARMOR_MATERIALS;

public class ArmorMaterialRegistration {
    public static final Holder<ArmorMaterial> HAZMAT_MATERIAL =
            ARMOR_MATERIALS.register("hazmat", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.LEGGINGS, 6);
                        map.put(ArmorItem.Type.CHESTPLATE, 7);
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.BODY, 5);
                    }),
                    5,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(Items.LEATHER),
                    // Determines the texture locations of the armor to apply when rendering
                    // This can also be specified by overriding 'IItemExtension#getArmorTexture' on your item if the armor texture needs to be more dynamic
                    List.of(
                            new ArmorMaterial.Layer(ncLoc("hazmat"))
                    ),
                    0,
                    0
            ));

    public static void init() {
    }
}