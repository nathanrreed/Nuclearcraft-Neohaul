package com.nred.nuclearcraft.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class DamageTypeRegistration { // TODO add these to fluids
    public static final ResourceKey<DamageType> SUPERFLUID_FREEZE = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("superfluid_freeze"));
    public static final ResourceKey<DamageType> PLASMA_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("plasma_burn"));
    public static final ResourceKey<DamageType> GAS_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("gas_burn"));
    public static final ResourceKey<DamageType> STEAM_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("steam_burn"));
    public static final ResourceKey<DamageType> MOLTEN_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("molten_burn"));
    public static final ResourceKey<DamageType> CORIUM_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("corium_burn"));
    public static final ResourceKey<DamageType> HOT_COOLANT_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("hot_coolant_burn"));
    public static final ResourceKey<DamageType> ACID_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("acid_burn"));
    public static final ResourceKey<DamageType> FLUID_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("fluid_burn"));
    public static final ResourceKey<DamageType> HYPOTHERMIA = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("hypothermia"));
    public static final ResourceKey<DamageType> FISSION_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("fission_burn"));
    public static final ResourceKey<DamageType> FATAL_RADS = ResourceKey.create(Registries.DAMAGE_TYPE, ncLoc("fatal_rads"));

    public static void init() {
    }
}