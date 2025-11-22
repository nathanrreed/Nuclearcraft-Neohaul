package com.nred.nuclearcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.DamageTypeRegistration.*;

class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.BYPASSES_ARMOR) // Not ideal to use optional, but doesn't work otherwise
                .addOptional(SUPERFLUID_FREEZE.location())
                .addOptional(PLASMA_BURN.location())
                .addOptional(GAS_BURN.location())
                .addOptional(STEAM_BURN.location())
                .addOptional(HYPOTHERMIA.location())
                .addOptional(FATAL_RADS.location());

        tag(DamageTypeTags.NO_KNOCKBACK)
                .addOptional(SUPERFLUID_FREEZE.location())
                .addOptional(PLASMA_BURN.location())
                .addOptional(GAS_BURN.location())
                .addOptional(STEAM_BURN.location())
                .addOptional(MOLTEN_BURN.location())
                .addOptional(CORIUM_BURN.location())
                .addOptional(HOT_COOLANT_BURN.location())
                .addOptional(ACID_BURN.location())
                .addOptional(FLUID_BURN.location())
                .addOptional(HYPOTHERMIA.location())
                .addOptional(FISSION_BURN.location())
                .addOptional(FATAL_RADS.location());
    }
}