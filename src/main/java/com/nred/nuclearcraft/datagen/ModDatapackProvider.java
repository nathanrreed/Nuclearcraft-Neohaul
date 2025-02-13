package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.worldgen.ModBiomeModifiers;
import com.nred.nuclearcraft.worldgen.ModConfiguredFeatures;
import com.nred.nuclearcraft.worldgen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.DamageTypeRegistration.*;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.DAMAGE_TYPE, bootstrap -> {
                bootstrap.register(SUPERFLUID_FREEZE, new DamageType("superfluid_freeze", DamageScaling.NEVER, 0.1f, DamageEffects.FREEZING, DeathMessageType.DEFAULT));
                bootstrap.register(PLASMA_BURN, new DamageType("plasma_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(GAS_BURN, new DamageType("gas_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(STEAM_BURN, new DamageType("steam_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(MOLTEN_BURN, new DamageType("molten_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(CORIUM_BURN, new DamageType("corium_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(HOT_COOLANT_BURN, new DamageType("hot_coolant_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(ACID_BURN, new DamageType("acid_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(FLUID_BURN, new DamageType("fluid_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(HYPOTHERMIA, new DamageType("hypothermia", DamageScaling.NEVER, 0.1f, DamageEffects.FREEZING, DeathMessageType.DEFAULT));
                bootstrap.register(FISSION_BURN, new DamageType("fission_burn", DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT));
                bootstrap.register(FATAL_RADS, new DamageType("fatal_rads", DamageScaling.NEVER, 0.1f, DamageEffects.valueOf("NUCLEARCRAFTNEOHAUL_RADIATION"), DeathMessageType.DEFAULT));
            });

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> event) {
        super(output, event, BUILDER, Set.of(MODID));
    }
}